package io.lvdaxianer.github.breakpoint.transfer.strategy.impl;

import io.lvdaxianer.github.breakpoint.transfer.exception.ParamCountException;
import io.lvdaxianer.github.breakpoint.transfer.extend.FileOperate;
import io.lvdaxianer.github.breakpoint.transfer.strategy.SelectStrategy;
import io.lvdaxianer.github.breakpoint.transfer.utils.CommonUtils;
import io.lvdaxianer.github.breakpoint.transfer.utils.Constants;
import io.lvdaxianer.github.breakpoint.transfer.utils.result.ResponseEntity;
import lombok.extern.slf4j.Slf4j;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 文件列表策略实现
 * 处理文件列表查询请求，解析请求参数并调用文件操作接口查询列表
 *
 * @author lvdaxianer
 */
@Slf4j
public class ListStrategyImpl implements SelectStrategy {

    /**
     * 处理文件列表查询请求
     * 解析请求URL中的目录名参数，调用文件操作接口查询该目录下的文件列表
     *
     * @param req         HTTP请求对象，包含请求URL
     * @param fileOperate 文件操作接口，用于执行查询操作
     * @return 包含文件数量和大小的列表，如果目录不存在则返回null
     * @throws ParamCountException 参数数量不匹配时抛出
     * @author lvdaxianer
     */
    @Override
    public ResponseEntity apply(HttpServletRequest req, FileOperate fileOperate) {
        String requestURI = req.getRequestURI();
        List<String> list = CommonUtils.resolveRestParams(requestURI, Constants.REQUEST_URL.LIST_REQUEST_URL);

        // 验证参数数量，必须包含目录名一个参数
        if (list == null || list.size() != 1) {
            log.warn("文件列表参数数量错误，期望1个，实际{}个", list == null ? 0 : list.size());
            throw new ParamCountException(Constants.REQUEST_URL.LIST_REQUEST_URL, 1, list == null ? 0 : list.size());
        }

        String baseDir = CommonUtils.sanitizePath(list.get(0));
        log.debug("查询文件列表: dir={}", baseDir);

        return fileOperate.list(baseDir);
    }
}
