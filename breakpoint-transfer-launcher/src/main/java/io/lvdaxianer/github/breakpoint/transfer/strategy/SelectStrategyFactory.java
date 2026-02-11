package io.lvdaxianer.github.breakpoint.transfer.strategy;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 策略选择工厂接口
 * 根据HTTP请求选择对应的处理策略
 *
 * @author lvdaxianer
 */
public interface SelectStrategyFactory {

    /**
     * 根据请求选择对应的策略
     *
     * @param req HTTP请求对象
     * @return 选中的处理策略
     */
    SelectStrategy newSelectStrategy(HttpServletRequest req);
}
