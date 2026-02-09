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
 * 验证 文件是否存在
 *
 * @author lihh
 */
public class VerifyStrategyImpl implements SelectStrategy {

    @Override
    public ResponseEntity apply(HttpServletRequest req, FileOperate fileOperate) {
        String requestURI = req.getRequestURI();
        List<Object> list = CommonUtils.resolveRestParams(requestURI, Constants.REQUEST_URL.VERIFY_REQUEST_URL);

        if (null == list || list.size() != 1)
            throw new ParamCountException(Constants.REQUEST_URL.VERIFY_REQUEST_URL, 1, list == null ? 0 : list.size());

        return fileOperate.verify((String) list.get(0));
    }
}
