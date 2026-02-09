package io.lvdaxianer.github.breakpoint.transfer.interceptor;

import io.lvdaxianer.github.breakpoint.transfer.entity.UploadFileFullProperties;
import io.lvdaxianer.github.breakpoint.transfer.utils.Constants;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.annotation.Resource;

@Configuration("aliasInterceptorConfig")
public class InterceptorConfig implements WebMvcConfigurer {

    @Resource
    private UploadFileFullProperties fullProperties;

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

        registry.addInterceptor(createRequestInterceptor()).addPathPatterns(contextPrefix + Constants.REQUEST_URL.BASE_URL + "/upload/**").order(httpInterceptorOrder);
    }
}
