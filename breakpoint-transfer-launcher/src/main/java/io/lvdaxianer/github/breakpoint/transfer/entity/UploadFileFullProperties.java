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

    /**
     * -- GETTER --
     *  获取基础目录
     *
     */
    private String baseDir;
    /**
     * -- GETTER --
     *  获取临时目录
     *
     */
    private String tmpDir;
    /**
     * -- GETTER --
     *  获取转换目录
     *
     */
    private String convertDir;
    /**
     * -- GETTER --
     *  获取公共目录
     *
     */
    private String publicDir;
    /**
     * -- GETTER --
     *  获取内部属性
     *
     */
    private final UploadFileProperties innerProperties;

    /**
     * 构造方法
     *
     * @param properties 上传文件属性配置，不能为null
     * @author lvdaxianer
     */
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

    /**
     * 设置基础目录
     *
     * @param baseDir 基础目录路径
     * @return 设置后的路径
     */
    public String setBaseDir(String baseDir) {
        return this.baseDir = baseDir;
    }

    /**
     * 设置临时目录
     *
     * @param tmpDir 临时目录路径
     * @return 设置后的路径
     */
    public String setTmpDir(String tmpDir) {
        return this.tmpDir = tmpDir;
    }

    /**
     * 设置转换目录
     *
     * @param convertDir 转换目录路径
     * @return 设置后的路径
     */
    public String setConvertDir(String convertDir) {
        return this.convertDir = convertDir;
    }

    /**
     * 设置公共目录
     *
     * @param publicDir 公共目录路径
     * @return 设置后的路径
     */
    public String setPublicDir(String publicDir) {
        return this.publicDir = publicDir;
    }
}
