package io.lvdaxianer.github.breakpoint.transfer.entity;

import io.lvdaxianer.github.breakpoint.transfer.utils.Constants;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 文件上传配置文件
 *
 * @author lvdaxian
 */
@Setter
@Getter
@ConfigurationProperties(prefix = "io.lvdaxianer.upload.file")
public class UploadFileProperties {

    /**
     * 文件保存目录
     */
    private String saveDir;

    /**
     * HTTP 拦截器顺序
     */
    private int httpInterceptorOrder = -1;

    /**
     * 上下文路径前缀
     */
    private String contextPrefix = Constants.REQUEST_URL.BASE_URL;

    /**
     * 日志级别：DEBUG, INFO, WARN, ERROR
     * 默认 INFO
     */
    private String logLevel = "DEBUG";

    /**
     * 是否启用详细日志
     * 启用后会输出更详细的操作信息
     */
    private boolean detailedLogging = false;

}
