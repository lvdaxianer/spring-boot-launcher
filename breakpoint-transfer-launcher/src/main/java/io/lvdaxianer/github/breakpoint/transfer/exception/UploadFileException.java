package io.lvdaxianer.github.breakpoint.transfer.exception;

/**
 * 文件上传异常
 * 用于处理文件操作相关的通用错误
 *
 * @author lihh
 */
public class UploadFileException extends BaseException {

    /**
     * 构造方法
     *
     * @param message 错误消息
     */
    public UploadFileException(String message) {
        super(message, ErrorCode.FILE_STORAGE_ERROR.getCode());
    }

    /**
     * 构造方法（带错误码）
     *
     * @param message   错误消息
     * @param errorCode 错误码
     */
    public UploadFileException(String message, ErrorCode errorCode) {
        super(message, errorCode.getCode());
    }

    /**
     * 构造方法（带根因异常）
     *
     * @param message 错误消息
     * @param cause   根因异常
     */
    public UploadFileException(String message, Throwable cause) {
        super(message, ErrorCode.FILE_STORAGE_ERROR.getCode(), cause);
    }

    /**
     * 构造方法（带错误码和根因异常）
     *
     * @param message   错误消息
     * @param errorCode 错误码
     * @param cause     根因异常
     */
    public UploadFileException(String message, ErrorCode errorCode, Throwable cause) {
        super(message, errorCode.getCode(), cause);
    }
}
