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
 * 文件合并策略实现
 * 处理文件合并请求，解析请求参数并调用文件操作接口执行合并
 *
 * @author lvdaxianer
 */
@Slf4j
public class MergeStrategyImpl implements SelectStrategy {

    /**
     * 处理文件合并请求
     * 解析请求URL中的目录名和文件名参数，调用文件操作接口执行合并
     *
     * @param req         HTTP请求对象，包含请求URL
     * @param fileOperate 文件操作接口，用于执行实际上传操作
     * @return 合并操作结果，success字段表示是否合并成功
     * @throws ParamCountException 参数数量不匹配时抛出
     * @author lvdaxianer
     */
    @Override
    public ResponseEntity apply(HttpServletRequest req, FileOperate fileOperate) {
        String requestURI = req.getRequestURI();
        List<String> list = CommonUtils.resolveRestParams(requestURI, Constants.REQUEST_URL.MERGE_REQUEST_URL);

        // 验证参数数量，必须包含目录名和文件名两个参数
        if (list == null || list.size() != 2) {
            log.warn("文件合并参数数量错误，期望2个，实际{}个", list == null ? 0 : list.size());
            throw new ParamCountException(Constants.REQUEST_URL.MERGE_REQUEST_URL, 2, list == null ? 0 : list.size());
        }

        String baseDir = CommonUtils.sanitizePath(list.get(0));
        String filename = CommonUtils.sanitizePath(list.get(1));
        log.debug("开始合并文件: dir={}, filename={}", baseDir, filename);

        return fileOperate.merge(baseDir, filename);
    }
}
