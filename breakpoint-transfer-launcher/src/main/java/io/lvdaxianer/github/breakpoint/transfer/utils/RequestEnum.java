package io.lvdaxianer.github.breakpoint.transfer.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * HTTP 请求方法枚举
 *
 * @author lihh
 */
@Getter
@AllArgsConstructor
public enum RequestEnum {

    /**
     * GET请求方法
     */
    GET("GET"),

    /**
     * DELETE请求方法
     */
    DELETE("DELETE"),

    /**
     * PATCH请求方法
     */
    PATCH("PATCH"),

    /**
     * POST请求方法
     */
    POST("POST");

    private final String method;
}
