package io.lvdaxianer.github.breakpoint.transfer.exception;

/**
 * 文件未找到异常
 * 当请求的文件不存在时抛出此异常
 *
 * @author lihh
 */
public class UploadFileNotFoundException extends BaseException {

    /**
     * 构造方法
     *
     * @param message 错误消息
     */
    public UploadFileNotFoundException(String message) {
        super(message, ErrorCode.FILE_NOT_FOUND.getCode(), false);
    }

    /**
     * 构造方法（带文件名）
     *
     * @param filename 文件名
     * @param dir     目录
     */
    public UploadFileNotFoundException(String filename, String dir) {
        super(String.format("文件[%s]不存在于目录[%s]中", filename, dir),
                ErrorCode.FILE_NOT_FOUND.getCode(), false);
    }
}
