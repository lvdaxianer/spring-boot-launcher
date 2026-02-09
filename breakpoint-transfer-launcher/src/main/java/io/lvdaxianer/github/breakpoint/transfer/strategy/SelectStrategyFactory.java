package io.lvdaxianer.github.breakpoint.transfer.strategy;

import jakarta.servlet.http.HttpServletRequest;

public interface SelectStrategyFactory {
  SelectStrategy newSelectStrategy(HttpServletRequest req);
}
