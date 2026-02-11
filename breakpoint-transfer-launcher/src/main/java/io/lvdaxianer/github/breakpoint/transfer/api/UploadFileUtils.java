package io.lvdaxianer.github.breakpoint.transfer.api;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;

/**
 * 文件上传工具接口
 * 提供File与MultipartFile之间的转换操作
 *
 * @author lihh
 */
public interface UploadFileUtils {

    /**
     * 将File转换为MultipartFile
     *
     * @param file     源文件对象，不能为null
     * @param filename 自定义文件名，为null时使用原文件名
     * @return MultipartFile对象
     */
    MultipartFile convertFileToMultipartFile(File file, String filename);

    /**
     * 将File转换为MultipartFile
     *
     * @param file 源文件对象，不能为null
     * @return MultipartFile对象
     */
    MultipartFile convertFileToMultipartFile(File file);

    /**
     * 根据文件名获取MultipartFile对象
     *
     * @param newFilename  新文件名，不能为null或空
     * @param oldFilename  原文件名，为null时使用新文件名
     * @return MultipartFile对象
     */
    MultipartFile getMultipartFileByName(String newFilename, String oldFilename);

    /**
     * 根据文件名获取MultipartFile对象
     *
     * @param newFilename 新文件名，不能为null或空
     * @return MultipartFile对象
     */
    MultipartFile getMultipartFileByName(String newFilename);

    /**
     * 根据文件名获取File对象
     *
     * @param fileName 文件名，不能为null或空
     * @return File对象，如果文件不存在则抛出异常
     * @throws io.lvdaxianer.github.breakpoint.transfer.exception.UploadFileNotFoundException 文件不存在时抛出
     */
    File getFileByName(String fileName);

    /**
     * 根据文件名获取Path对象
     *
     * @param newFilename 文件名，不能为null或空
     * @return Path对象，如果文件不存在则抛出异常
     * @throws io.lvdaxianer.github.breakpoint.transfer.exception.UploadFileNotFoundException 文件不存在时抛出
     */
    Path getPath(String newFilename);
}
