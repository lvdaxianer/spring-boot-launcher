package io.lvdaxianer.github.breakpoint.transfer.lifecycle;

import io.lvdaxianer.github.breakpoint.transfer.entity.UploadFileFullProperties;
import io.lvdaxianer.github.breakpoint.transfer.utils.CommonUtils;
import io.lvdaxianer.github.breakpoint.transfer.utils.Constants;
import io.lvdaxianer.github.breakpoint.transfer.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;

/**
 * 容器启动完成后执行的钩子
 * 用于初始化日志配置和清理临时文件
 *
 * @author lihh
 */
@Slf4j
@Component
public class ContainerRefreshDone implements ApplicationRunner {

    @Resource
    private UploadFileFullProperties fullProperties;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("========================================");
        log.info("断点续传启动器启动成功");
        log.info("========================================");

        // 输出配置信息
        String contextPrefix = fullProperties.getInnerProperties().getContextPrefix();
        String enabledType = fullProperties.getInnerProperties().getEnabledType();
        int httpInterceptorOrder = fullProperties.getInnerProperties().getHttpInterceptorOrder();
        String logLevel = fullProperties.getInnerProperties().getLogLevel();

        log.info("配置信息:");
        log.info("  上下文前缀: {}", CommonUtils.getValueOrDefault(contextPrefix, "-"));
        log.info("  存储类型: {}", enabledType);
        log.info("  HTTP拦截器顺序: {}", httpInterceptorOrder);
        log.info("  日志级别: {}", logLevel);

        // 根据存储类型执行初始化
        if (StringUtils.equals(enabledType, Constants.ENABLED_TYPE_DISK)) {
            log.info("存储目录: {}", fullProperties.getBaseDir());
            log.info("临时目录: {}", fullProperties.getTmpDir());
            log.info("公开目录: {}", fullProperties.getPublicDir());
            log.info("转换目录: {}", fullProperties.getConvertDir());

            // 清理转换目录
            log.info("清理转换目录: {}", fullProperties.getConvertDir());
            FileUtils.deleteDirectoryListing(FileUtils.joinPath(fullProperties.getConvertDir()));
        }

        log.info("========================================");
        log.info("断点续传启动器初始化完成");
        log.info("========================================");
    }
}
