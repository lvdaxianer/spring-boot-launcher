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
    GET("GET"),
    DELETE("DELETE"),
    PATCH("PATCH"),
    POST("POST");

    private final String method;
}
