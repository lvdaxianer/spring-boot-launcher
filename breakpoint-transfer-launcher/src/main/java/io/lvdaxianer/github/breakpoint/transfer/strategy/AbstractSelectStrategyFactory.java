package io.lvdaxianer.github.breakpoint.transfer.strategy;

import java.util.HashMap;
import java.util.Map;

/**
 * 抽象 选择策略工厂
 *
 * @author lihh
 */
abstract class AbstractSelectStrategyFactory {
    protected final Map<RequestMatchEntity, SelectStrategy> strategyMapping = new HashMap<>();

    protected void register(RequestMatchEntity k, SelectStrategy v) {
        strategyMapping.put(k, v);
    }
}
