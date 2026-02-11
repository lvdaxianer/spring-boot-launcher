package io.lvdaxianer.github.breakpoint.transfer.lifecycle;

import io.lvdaxianer.github.breakpoint.transfer.entity.UploadFileFullProperties;
import io.lvdaxianer.github.breakpoint.transfer.utils.CommonUtils;
import io.lvdaxianer.github.breakpoint.transfer.utils.Constants;
import io.lvdaxianer.github.breakpoint.transfer.utils.FileUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

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

        String contextPrefix = fullProperties.getInnerProperties().getContextPrefix();
        String logLevel = fullProperties.getInnerProperties().getLogLevel();
        int httpInterceptorOrder = fullProperties.getInnerProperties().getHttpInterceptorOrder();

        log.info("访问路径配置:");
        log.info("  分片上传路径: {}{}", contextPrefix, Constants.REQUEST_URL.SECTION_REQUEST_URL);
        log.info("  验证路径: {}{}", contextPrefix, Constants.REQUEST_URL.VERIFY_REQUEST_URL);
        log.info("  列表路径: {}{}", contextPrefix, Constants.REQUEST_URL.LIST_REQUEST_URL);
        log.info("  合并路径: {}{}", contextPrefix, Constants.REQUEST_URL.MERGE_REQUEST_URL);

        boolean isDebug = "DEBUG".equalsIgnoreCase(logLevel);
        if (isDebug) {
            log.debug("配置信息:");
            log.debug("  上下文前缀: {}", CommonUtils.getValueOrDefault(contextPrefix, "-"));
            log.debug("  HTTP拦截器顺序: {}", httpInterceptorOrder);
            log.debug("  日志级别: {}", logLevel);

            log.debug("存储目录: {}", fullProperties.getBaseDir());
            log.debug("临时目录: {}", fullProperties.getTmpDir());
            log.debug("公开目录: {}", fullProperties.getPublicDir());
            log.debug("转换目录: {}", fullProperties.getConvertDir());

            log.debug("清理转换目录: {}", fullProperties.getConvertDir());
            FileUtils.deleteDirectoryListing(FileUtils.joinPath(fullProperties.getConvertDir()));
        } else {
            log.info("清理转换目录: {}", fullProperties.getConvertDir());
            FileUtils.deleteDirectoryListing(FileUtils.joinPath(fullProperties.getConvertDir()));
        }

        log.info("========================================");
        log.info("断点续传启动器初始化完成");
        log.info("========================================");
    }
}
