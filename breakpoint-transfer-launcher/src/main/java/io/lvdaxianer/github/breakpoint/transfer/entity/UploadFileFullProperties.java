package io.lvdaxianer.github.breakpoint.transfer.entity;

import io.lvdaxianer.github.breakpoint.transfer.utils.Constants;
import lombok.Getter;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * 上传文件全部属性
 *
 * @author lvdaxianer
 */
@Getter
@Configuration
public class UploadFileFullProperties {

    private String baseDir;
    private String tmpDir;
    private String convertDir;
    private String publicDir;
    private final UploadFileProperties innerProperties;

    public UploadFileFullProperties(UploadFileProperties properties) {
        this.innerProperties = properties;

        if (!StringUtils.hasLength(properties.getSaveDir()))
            this.innerProperties.setSaveDir(Constants.fileSaveTmpDir);
        else {
            String saveDir = properties.getSaveDir();
            if (properties.getSaveDir().startsWith("./"))
                saveDir = properties.getSaveDir().substring(2, saveDir.length());
            this.innerProperties.setSaveDir(saveDir);
        }
        if (properties.getHttpInterceptorOrder() < 0)
            this.innerProperties.setHttpInterceptorOrder(Constants.DEFAULT_INTERCEPTOR_ORDER);

        if (!StringUtils.hasLength(properties.getContextPrefix()))
            this.innerProperties.setContextPrefix("");
    }

    public String setBaseDir(String baseDir) {
        return this.baseDir = baseDir;
    }

    public String setTmpDir(String tmpDir) {
        return this.tmpDir = tmpDir;
    }

    public String setConvertDir(String convertDir) {
        return this.convertDir = convertDir;
    }

    public String setPublicDir(String publicDir) {
        return this.publicDir = publicDir;
    }
}
