package io.lvdaxianer.github.breakpoint.transfer.strategy.impl;

import io.lvdaxianer.github.breakpoint.transfer.exception.ParamCountException;
import io.lvdaxianer.github.breakpoint.transfer.extend.FileOperate;
import io.lvdaxianer.github.breakpoint.transfer.strategy.SelectStrategy;
import io.lvdaxianer.github.breakpoint.transfer.utils.CommonUtils;
import io.lvdaxianer.github.breakpoint.transfer.utils.Constants;
import io.lvdaxianer.github.breakpoint.transfer.utils.result.ResponseEntity;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * 文件 merge 策略实现
 *
 * @author lihh
 */
public class MergeStrategyImpl implements SelectStrategy {

    @Override
    public ResponseEntity apply(HttpServletRequest req, FileOperate fileOperate) {
        String requestURI = req.getRequestURI();
        List<Object> list = CommonUtils.resolveRestParams(requestURI, Constants.REQUEST_URL.MERGE_REQUEST_URL);

        if (null == list || list.size() != 2)
            throw new ParamCountException(Constants.REQUEST_URL.MERGE_REQUEST_URL, 2, list == null ? 0 : list.size());

        return fileOperate.merge((String) list.get(0), (String) list.get(1));
    }
}
