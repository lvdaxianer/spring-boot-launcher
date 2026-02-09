package io.lvdaxianer.github.breakpoint.transfer.exception;

/**
 * 参数格式异常
 * 用于处理请求参数格式不正确的错误
 *
 * @author lihh
 */
public class ParamErrorException extends BaseException {

    /**
     * 构造方法
     *
     * @param message 错误消息
     */
    public ParamErrorException(String message) {
        super(message, ErrorCode.PARAM_FORMAT_ERROR.getCode());
    }

    /**
     * 构造方法（带参数名和期望值）
     *
     * @param paramName  参数名称
     * @param expected  期望值
     * @param actual    实际值
     */
    public ParamErrorException(String paramName, String expected, String actual) {
        super(String.format("参数[%s]格式不正确: 期望%s，实际%s", paramName, expected, actual),
                ErrorCode.PARAM_FORMAT_ERROR.getCode());
    }

    /**
     * 构造方法（带参数名）
     *
     * @param paramName 参数名称
     * @param message   错误消息
     */
    public ParamErrorException(String paramName, String message) {
        super(String.format("参数[%s]错误: %s", paramName, message),
                ErrorCode.PARAM_FORMAT_ERROR.getCode());
    }
}
