package io.lvdaxianer.github.breakpoint.transfer.strategy;

import io.lvdaxianer.github.breakpoint.transfer.extend.FileOperate;
import io.lvdaxianer.github.breakpoint.transfer.utils.result.ResponseEntity;

import jakarta.servlet.http.HttpServletRequest;
import java.util.function.BiFunction;

/**
 * 策略选择接口
 * 用于处理不同类型的请求
 *
 * @author lihh
 */
public interface SelectStrategy extends BiFunction<HttpServletRequest, FileOperate, ResponseEntity> {

}
