package io.lvdaxianer.github.breakpoint.transfer.extend.impl;

import io.lvdaxianer.github.breakpoint.transfer.entity.UploadFileFullProperties;
import io.lvdaxianer.github.breakpoint.transfer.exception.ErrorCode;
import io.lvdaxianer.github.breakpoint.transfer.exception.FileMergeException;
import io.lvdaxianer.github.breakpoint.transfer.exception.FileUploadException;
import io.lvdaxianer.github.breakpoint.transfer.exception.UploadFileException;
import io.lvdaxianer.github.breakpoint.transfer.extend.FileOperate;
import io.lvdaxianer.github.breakpoint.transfer.utils.CommonUtils;
import io.lvdaxianer.github.breakpoint.transfer.utils.Constants;
import io.lvdaxianer.github.breakpoint.transfer.utils.FileUtils;
import io.lvdaxianer.github.breakpoint.transfer.utils.result.ResponseEntity;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

/**
 * 磁盘文件操作实现类
 * 提供基于本地磁盘的文件上传、验证、列表查询和合并功能
 *
 * @author lvdaxianer
 */
@Slf4j
@Component
public class DiskFileOperate implements FileOperate {
    @Resource
    private UploadFileFullProperties fullProperties;

    /**
     * PostConstruct 初始化
     * 创建必要的目录结构
     */
    @PostConstruct
    public void postHandler() {
        log.info("初始化磁盘文件操作，存储类型: {}", fullProperties.getInnerProperties().getEnabledType());

        // 基础目录
        String baseDir = fullProperties.setBaseDir(CommonUtils.getAbsolutePath(fullProperties.getInnerProperties().getSaveDir()).toString());
        String tmpDir = fullProperties.setTmpDir(baseDir + File.separator + Constants.CONST_TMP_NAME);
        String publicDir = fullProperties.setPublicDir(baseDir + File.separator + Constants.CONST_PUBLIC_NAME);
        String convertDir = fullProperties.setConvertDir(baseDir + File.separator + Constants.CONST_CONVERT_NAME);

        FileUtils.mkDir(baseDir);
        FileUtils.mkDir(tmpDir);
        FileUtils.mkDir(publicDir);
        FileUtils.mkDir(convertDir);

        log.info("磁盘文件操作初始化完成，基础目录: {}", baseDir);
    }

    /**
     * 文件 分片上传的逻辑
     * 使用 MultipartFile.transferTo 直接写入文件，避免内存拷贝
     *
     * @param file     上传的文件分片
     * @param baseDir  临时目录名称
     * @param filename 分片文件名，格式：原始文件名-索引号
     * @return 文件写入成功后响应值
     * @author lvdaxianer
     */
    @Override
    public ResponseEntity upload(MultipartFile file, String baseDir, String filename) {
        if (file == null || file.isEmpty()) {
            log.warn("文件上传失败: 文件为空");
            throw new UploadFileException("上传文件为空", ErrorCode.FILE_EMPTY);
        }

        log.debug("开始上传分片文件: {}, 原始文件名: {}, 大小: {} bytes",
                filename, file.getOriginalFilename(), file.getSize());

        String targetBaseDir = fullProperties.getTmpDir() + File.separator + baseDir;
        FileUtils.mkDir(targetBaseDir);
        String filePath = targetBaseDir + File.separator + filename;

        try {
            File destFile = new File(filePath);
            // 直接transferTo写入，避免内存拷贝，这是Spring专门为文件上传优化的方法
            file.transferTo(destFile);

            log.info("分片文件上传成功: {}, 大小: {} bytes", filePath, file.getSize());
            return ResponseEntity.ok(true);
        } catch (IOException e) {
            log.error("分片文件上传失败: {}, 错误: {}", filePath, e.getMessage());
            throw new FileUploadException(filename, e.getMessage(), e);
        }
    }

    /**
     * 判断 文件是否存在
     *
     * @param filename 文件名称
     * @return 返回文件验证状态
     * @author lihh
     */
    @Override
    public ResponseEntity verify(String filename) {
        String filePath = FileUtils.joinPath(fullProperties.getPublicDir(), filename);
        boolean exists = FileUtils.isFileExist(filePath);
        log.debug("验证文件是否存在: {}, 结果: {}", filePath, exists);
        return ResponseEntity.ok(exists);
    }

    /**
     * 根据文件目录 拿到文件size 以及个数
     *
     * @param baseDir 文件目录
     * @return 返回文件size 以及临时文件个数
     * @author lihh
     */
    @Override
    public ResponseEntity list(String baseDir) {
        String dirPath = fullProperties.getTmpDir() + File.separator + baseDir;
        File basePathFile = new File(dirPath);
        // 表示目录是否存在
        if (!basePathFile.isDirectory() || !basePathFile.exists()) {
            log.debug("查询文件列表，目录不存在: {}", dirPath);
            return ResponseEntity.ok(null);
        }

        // 读取目录下的文件
        String[] list = Optional.ofNullable(basePathFile.list()).orElse(new String[]{});

        if (list.length == 0) {
            log.debug("查询文件列表，目录为空: {}", dirPath);
            return ResponseEntity.ok(Arrays.asList(0, 0L));
        }

        // 使用工具方法计算目录大小，避免溢出
        long fileSize = FileUtils.getDirectorySize(basePathFile.getAbsolutePath());
        if (fileSize < 0) {
            fileSize = 0;
        }

        log.info("查询文件列表: {}, 文件数: {}, 总大小: {} bytes", dirPath, list.length, fileSize);
        return ResponseEntity.ok(Arrays.asList(list.length, fileSize));
    }

    /**
     * 根据 文件目录 以及文件名称 进行文件合并
     *
     * @param baseDir  文件基础目录
     * @param filename 文件名称
     * @return 文件合并结果
     * @author lihh
     */
    @Override
    public ResponseEntity merge(String baseDir, String filename) {
        String tempDir = FileUtils.joinPath(fullProperties.getTmpDir(), baseDir);
        log.info("开始合并文件: {}, 临时目录: {}", filename, tempDir);

        // 读取目录
        File[] files = FileUtils.readDirectoryListing(tempDir);
        // 判断目录是否为空
        if (files == null || files.length == 0) {
            log.warn("合并失败，临时目录为空: {}", tempDir);
            return ResponseEntity.ok(false);
        }

        log.debug("找到 {} 个分片文件待合并", files.length);

        // 表示合并后的目录
        String mergePublicDir = fullProperties.getPublicDir() + File.separator + filename;
        // 记录合并开始时间
        long startTime = System.currentTimeMillis();
        // 判断是否合并成功
        boolean mergeFlag;
        try {
            mergeFlag = FileUtils.mergeFile(files, mergePublicDir);
        } catch (IOException e) {
            long costTime = System.currentTimeMillis() - startTime;
            log.error("文件合并失败: {}, 错误: {}, 耗时: {} ms", mergePublicDir, e.getMessage(), costTime);
            throw new FileMergeException(filename, e.getMessage(), e);
        }
        long costTime = System.currentTimeMillis() - startTime;
        log.info("文件合并完成: {}, 耗时: {} ms", filename, costTime);

        if (mergeFlag) {
            log.info("文件合并成功: {}, 目标路径: {}", filename, mergePublicDir);

            // 删除临时文件
            boolean deleteSuccess = FileUtils.deleteIfExists(tempDir);
            if (deleteSuccess) {
                log.debug("临时文件删除成功: {}", tempDir);
            } else {
                log.warn("临时文件删除失败: {}", tempDir);
            }
        } else {
            log.error("文件合并失败: {}", filename);
        }

        return ResponseEntity.ok(mergeFlag);
    }
}
