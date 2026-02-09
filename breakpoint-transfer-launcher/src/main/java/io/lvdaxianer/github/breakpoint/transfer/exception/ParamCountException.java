package io.lvdaxianer.github.breakpoint.transfer.exception;

/**
 * 参数数量异常
 * 用于处理请求参数数量不正确的错误
 *
 * @author lihh
 */
public class ParamCountException extends BaseException {

    /**
     * 构造方法
     *
     * @param message 错误消息
     */
    public ParamCountException(String message) {
        super(message, ErrorCode.PARAM_COUNT_ERROR.getCode());
    }

    /**
     * 构造方法（带请求URL）
     *
     * @param requestURL 请求URL
     * @param expected   期望参数数量
     * @param actual     实际参数数量
     */
    public ParamCountException(String requestURL, int expected, int actual) {
        super(String.format("请求URL[%s]参数数量不正确: 期望%d个参数，实际%d个参数", requestURL, expected, actual),
                ErrorCode.PARAM_COUNT_ERROR.getCode());
    }
}
