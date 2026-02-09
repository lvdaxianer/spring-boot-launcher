package io.lvdaxianer.github.breakpoint.transfer.extend.impl;

import io.lvdaxianer.github.breakpoint.transfer.extend.FileOperate;
import io.lvdaxianer.github.breakpoint.transfer.utils.result.ResponseEntity;
import io.minio.MinioClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;

@ConditionalOnProperty(
        name = "io.lvdaxianer.upload.file.enabled-type",
        havingValue = "mino"
)
@ConditionalOnClass({MinioClient.class})
@Configuration
public class MinoFileOperateImpl implements FileOperate {

    @Override
    public ResponseEntity upload(MultipartFile file, String baseDir, String filename) {
        return null;
    }

    @Override
    public ResponseEntity verify(String filename) {
        return null;
    }

    @Override
    public ResponseEntity list(String baseDir) {
        return null;
    }

    @Override
    public ResponseEntity merge(String baseDir, String filename) {
        return null;
    }
}
