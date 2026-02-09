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
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
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
@ConditionalOnProperty(name = "io.lvdaxianer.upload.file.enabled-type", havingValue = "disk")
public class DiskFileOperateImpl implements FileOperate {
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
     *
     * @param file     上传的文件
     * @param baseDir  目录名称
     * @param filename 文件名称
     * @return 文件写入成功后 响应值
     * @author lihh
     */
    @Override
    public ResponseEntity upload(MultipartFile file, String baseDir, String filename) {
        // 检查文件是否为空
        if (file == null || file.isEmpty()) {
            log.warn("文件上传失败: 文件为空");
            throw new UploadFileException("上传文件为空", ErrorCode.FILE_EMPTY);
        }

        log.debug("开始上传文件: {}, 原始文件名: {}, 大小: {} bytes", filename, file.getOriginalFilename(), file.getSize());

        String targetBaseDir = fullProperties.getTmpDir() + File.separator + baseDir;

        // 创建基础目录
        FileUtils.mkDir(targetBaseDir);
        // 表示 文件地址
        String filePath = targetBaseDir + File.separator + filename;

        // 将 文件 写入到磁盘中
        try {
            FileUtils.writeFile(filePath, file.getInputStream());
            log.info("文件上传成功: {}, 大小: {} bytes", filePath, file.getSize());
            return ResponseEntity.ok(true);
        } catch (IOException e) {
            log.error("文件上传失败: {}, 错误: {}", filePath, e.getMessage());
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
        // 判断是否合并成功
        boolean mergeFlag;
        try {
            mergeFlag = FileUtils.mergeFile(files, mergePublicDir);
        } catch (IOException e) {
            log.error("文件合并失败: {}, 错误: {}", mergePublicDir, e.getMessage());
            throw new FileMergeException(filename, e.getMessage(), e);
        }

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
