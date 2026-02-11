package io.lvdaxianer.github.breakpoint.transfer.configuration;

import io.lvdaxianer.github.breakpoint.transfer.entity.UploadFileProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * 日志自动配置类
 * 根据配置文件动态设置日志级别
 *
 * @author lihh
 */
@Configuration
@EnableConfigurationProperties(UploadFileProperties.class)
public class BreakpointLogAutoConfiguration {

    /**
     * 配置日志级别
     *
     * @param properties 上传文件配置属性
     * @return 日志配置器
     */
    @Bean
    public LogLevelConfigurer logLevelConfigurer(UploadFileProperties properties) {
        return new LogLevelConfigurer(properties);
    }

    /**
     * 日志级别配置器
     * 负责根据配置动态设置日志级别
     */
    public static class LogLevelConfigurer {
        private static final Logger log = LoggerFactory.getLogger(LogLevelConfigurer.class);

        private final UploadFileProperties properties;

        public LogLevelConfigurer(UploadFileProperties properties) {
            this.properties = properties;
            configureLogLevel();
        }

        /**
         * 配置日志级别
         */
        private void configureLogLevel() {
            String logLevel = properties.getLogLevel();

            if (!StringUtils.hasText(logLevel)) {
                logLevel = "INFO";
            }

            // 设置当前包的日志级别
            ch.qos.logback.classic.LoggerContext loggerContext =
                    (ch.qos.logback.classic.LoggerContext) LoggerFactory.getILoggerFactory();

            ch.qos.logback.classic.Logger logger =
                    loggerContext.getLogger("io.lvdaxianer.github.breakpoint.transfer");

            switch (logLevel.toUpperCase()) {
                case "DEBUG":
                    logger.setLevel(ch.qos.logback.classic.Level.DEBUG);
                    log.info("日志级别已设置为 DEBUG");
                    break;
                case "INFO":
                    logger.setLevel(ch.qos.logback.classic.Level.INFO);
                    log.info("日志级别已设置为 INFO");
                    break;
                case "WARN":
                    logger.setLevel(ch.qos.logback.classic.Level.WARN);
                    log.info("日志级别已设置为 WARN");
                    break;
                case "ERROR":
                    logger.setLevel(ch.qos.logback.classic.Level.ERROR);
                    log.info("日志级别已设置为 ERROR");
                    break;
                default:
                    logger.setLevel(ch.qos.logback.classic.Level.INFO);
                    log.warn("未知的日志级别[{}]，已设置为 INFO", logLevel);
            }

            // 配置详细日志
            if (properties.isDetailedLogging()) {
                log.info("详细日志已启用");
            }
        }

    }
}
