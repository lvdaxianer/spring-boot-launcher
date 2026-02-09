package io.lvdaxianer.github.breakpoint.transfer.entity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 文件上传配置文件
 *
 * @author lvdaxian
 */
@ConfigurationProperties(prefix = "io.lvdaxianer.upload.file")
@Data
public class UploadFileProperties {
    /**
     * 存储类型：disk 或 mino
     */
    private String enabledType;

    /**
     * 文件保存目录
     */
    private String saveDir;

    /**
     * HTTP 拦截器顺序
     */
    private int httpInterceptorOrder;

    /**
     * 上下文路径前缀
     */
    private String contextPrefix;

    /**
     * 日志级别：DEBUG, INFO, WARN, ERROR
     * 默认 INFO
     */
    private String logLevel = "INFO";

    /**
     * 是否启用详细日志
     * 启用后会输出更详细的操作信息
     */
    private boolean detailedLogging = false;
}
