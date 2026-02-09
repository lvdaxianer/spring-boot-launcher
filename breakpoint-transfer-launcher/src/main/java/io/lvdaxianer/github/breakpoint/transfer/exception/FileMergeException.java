package io.lvdaxianer.github.breakpoint.transfer.exception;

/**
 * 文件合并失败异常
 * 用于处理文件合并过程中的具体错误
 *
 * @author lihh
 */
public class FileMergeException extends BaseException {

    /**
     * 构造方法
     *
     * @param message 错误消息
     */
    public FileMergeException(String message) {
        super(message, ErrorCode.FILE_MERGE_FAILED.getCode());
    }

    /**
     * 构造方法（带文件名）
     *
     * @param filename 文件名
     * @param message  错误消息
     */
    public FileMergeException(String filename, String message) {
        super(String.format("文件[%s]合并失败: %s", filename, message), ErrorCode.FILE_MERGE_FAILED.getCode());
    }

    /**
     * 构造方法（带根因异常）
     *
     * @param message 错误消息
     * @param cause   根因异常
     */
    public FileMergeException(String message, Throwable cause) {
        super(message, ErrorCode.FILE_MERGE_FAILED.getCode(), cause);
    }

    /**
     * 构造方法（带文件名和根因异常）
     *
     * @param filename 文件名
     * @param message  错误消息
     * @param cause    根因异常
     */
    public FileMergeException(String filename, String message, Throwable cause) {
        super(String.format("文件[%s]合并失败: %s", filename, message), ErrorCode.FILE_MERGE_FAILED.getCode(), cause);
    }
}
