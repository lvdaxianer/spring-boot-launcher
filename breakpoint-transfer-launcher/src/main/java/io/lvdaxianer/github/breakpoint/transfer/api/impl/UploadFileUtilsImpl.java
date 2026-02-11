package io.lvdaxianer.github.breakpoint.transfer.api.impl;

import io.lvdaxianer.github.breakpoint.transfer.api.UploadFileUtils;
import io.lvdaxianer.github.breakpoint.transfer.entity.UploadFileFullProperties;
import io.lvdaxianer.github.breakpoint.transfer.exception.UploadFileNotFoundException;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * 文件上传工具实现类
 * 提供File与MultipartFile之间的转换操作
 *
 * @author lihh
 */
@Component
public class UploadFileUtilsImpl implements UploadFileUtils {

    private static final Logger log = LoggerFactory.getLogger(UploadFileUtilsImpl.class);
    private final UploadFileFullProperties fullProperties;

    /**
     * 构造方法
     *
     * @param fullProperties 文件配置属性，不能为null
     */
    public UploadFileUtilsImpl(UploadFileFullProperties fullProperties) {
        this.fullProperties = fullProperties;
    }

    @Override
    public MultipartFile convertFileToMultipartFile(File file, String filename) {
        if (null == filename) filename = file.getName();

        Resource resource = new FileSystemResource(file);
        return new ResourceMultipartFile(resource, filename);
    }

    @Override
    public MultipartFile convertFileToMultipartFile(File file) {
        return convertFileToMultipartFile(file, null);
    }

    @Override
    public MultipartFile getMultipartFileByName(String newFilename, String oldFilename) {
        return convertFileToMultipartFile(getFileByName(newFilename), oldFilename);
    }

    @Override
    public MultipartFile getMultipartFileByName(String newFilename) {
        return getMultipartFileByName(newFilename, null);
    }

    @Override
    public File getFileByName(String fileName) {
        String filePath = fullProperties.getPublicDir() + File.separator + fileName;

        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            String message = String.format("upload file[%s] not found, in dir[%s]", fileName, fullProperties.getPublicDir());
            log.error(message);
            throw new UploadFileNotFoundException(message);
        }

        return file;
    }

    /**
     * 获取文件的 Path 对象
     *
     * @param newFilename 文件名称
     * @return Path 对象
     */
    @Override
    public Path getPath(String newFilename) {
        File file = getFileByName(newFilename);
        if (file == null) {
            return null;
        }
        return file.toPath();
    }

    private static class ResourceMultipartFile implements MultipartFile {
        private final Resource resource;
        private final String filename;

        public ResourceMultipartFile(Resource resource, String filename) {
            this.resource = resource;
            this.filename = filename;
        }

        @Override
        public String getName() {
            return filename;
        }

        @Override
        public String getOriginalFilename() {
            return filename;
        }

        @Override
        public String getContentType() {
            return "application/octet-stream";
        }

        @Override
        public boolean isEmpty() {
            return !resource.exists();
        }

        @Override
        public long getSize() {
            try {
                return resource.contentLength();
            } catch (Exception e) {
                // 资源不可用时返回0，表示空文件
                return 0;
            }
        }

        @Override
        public byte @NotNull [] getBytes() throws IOException {
            return resource.getInputStream().readAllBytes();
        }

        @Override
        public java.io.@NotNull InputStream getInputStream() throws IOException {
            return resource.getInputStream();
        }

        @Override
        public void transferTo(java.io.File dest) throws IOException {
            java.nio.file.Files.copy(resource.getFile().toPath(), dest.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        }
    }
}
