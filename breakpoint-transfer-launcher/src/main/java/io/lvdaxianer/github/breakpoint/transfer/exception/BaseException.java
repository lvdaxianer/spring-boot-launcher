package io.lvdaxianer.github.breakpoint.transfer.exception;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * 异常基类
 * 提供统一的异常处理机制，包括错误码和日志记录
 *
 * @author lihh
 */
@Slf4j
@Getter
public abstract class BaseException extends RuntimeException {

    /**
     * 错误码
     */
    private final String errorCode;

    /**
     * 是否记录错误日志
     */
    private final boolean logError;

    /**
     * 构造方法
     *
     * @param message   错误消息
     * @param errorCode 错误码
     */
    protected BaseException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
        this.logError = true;
        log.error("[{}] {}", errorCode, message);
    }

    /**
     * 构造方法
     *
     * @param message   错误消息
     * @param errorCode 错误码
     * @param logError  是否记录错误日志
     */
    protected BaseException(String message, String errorCode, boolean logError) {
        super(message);
        this.errorCode = errorCode;
        this.logError = logError;
        if (logError) {
            log.error("[{}] {}", errorCode, message);
        }
    }

    /**
     * 构造方法（带根因异常）
     *
     * @param message   错误消息
     * @param errorCode 错误码
     * @param cause     根因异常
     */
    protected BaseException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.logError = true;
        log.error("[{}] {} - {}", errorCode, message, cause.getMessage());
    }

    /**
     * 构造方法（带根因异常）
     *
     * @param message   错误消息
     * @param errorCode 错误码
     * @param cause     根因异常
     * @param logError  是否记录错误日志
     */
    protected BaseException(String message, String errorCode, Throwable cause, boolean logError) {
        super(message, cause);
        this.errorCode = errorCode;
        this.logError = logError;
        if (logError) {
            log.error("[{}] {} - {}", errorCode, message, cause.getMessage());
        }
    }
}
