package io.lvdaxianer.github.breakpoint.transfer.interceptor;

import io.lvdaxianer.github.breakpoint.transfer.entity.UploadFileFullProperties;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器配置类
 * 配置HTTP请求拦截器，用于处理断点续传相关的请求
 *
 * @author lvdaxianer
 */
@Configuration("aliasInterceptorConfig")
public class InterceptorConfig implements WebMvcConfigurer {

    @Resource
    private UploadFileFullProperties fullProperties;

    /**
     * 创建请求拦截器实例
     *
     * @return HttpRequestInterceptor实例
     */
    @Bean
    public HttpRequestInterceptor createRequestInterceptor() {
        return new HttpRequestInterceptor();
    }

    @Override
    public void addInterceptors(@NotNull InterceptorRegistry registry) {
        int httpInterceptorOrder = fullProperties.getInnerProperties().getHttpInterceptorOrder();
        String contextPrefix = fullProperties.getInnerProperties().getContextPrefix();

        if (StringUtils.isNotEmpty(contextPrefix)) {
            if (!contextPrefix.startsWith("/"))
                contextPrefix = "/" + contextPrefix;
            if (contextPrefix.endsWith("/"))
                contextPrefix = contextPrefix.substring(0, contextPrefix.length() - 1);
        }

        registry.addInterceptor(createRequestInterceptor()).addPathPatterns(contextPrefix + "/upload/**").order(httpInterceptorOrder);
    }
}
