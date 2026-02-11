package io.lvdaxianer.github.breakpoint.transfer.extend;

import io.lvdaxianer.github.breakpoint.transfer.utils.result.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件操作接口
 * 提供断点续传相关的文件操作能力
 *
 * @author lvdaxianer
 */
public interface FileOperate {

    /**
     * 上传文件
     *
     * @param file     上传的文件，不能为null
     * @param baseDir  基础目录
     * @param filename 文件名
     * @return 响应结果
     */
    ResponseEntity upload(MultipartFile file, String baseDir, String filename);

    /**
     * 验证文件
     *
     * @param filename 文件名
     * @return 响应结果
     */
    ResponseEntity verify(String filename);

    /**
     * 列出文件
     *
     * @param baseDir 基础目录
     * @return 响应结果
     */
    ResponseEntity list(String baseDir);

    /**
     * 合并文件
     *
     * @param baseDir  基础目录
     * @param filename 文件名
     * @return 响应结果
     */
    ResponseEntity merge(String baseDir, String filename);
}
