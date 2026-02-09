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
 * 查询 上传文件的列表
 *
 * @author lihh
 */
public class ListStrategyImpl implements SelectStrategy {

    @Override
    public ResponseEntity apply(HttpServletRequest req, FileOperate fileOperate) {
        String requestURI = req.getRequestURI();
        List<Object> list = CommonUtils.resolveRestParams(requestURI, Constants.REQUEST_URL.LIST_REQUEST_URL);

        if (null == list || list.size() != 1)
            throw new ParamCountException(Constants.REQUEST_URL.LIST_REQUEST_URL, 1, list == null ? 0 : list.size());

        return fileOperate.list((String) list.get(0));
    }
}
