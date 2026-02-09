package io.lvdaxianer.github.breakpoint.transfer.strategy;

import io.lvdaxianer.github.breakpoint.transfer.strategy.impl.ListStrategyImpl;
import io.lvdaxianer.github.breakpoint.transfer.strategy.impl.MergeStrategyImpl;
import io.lvdaxianer.github.breakpoint.transfer.strategy.impl.SectionStrategyImpl;
import io.lvdaxianer.github.breakpoint.transfer.strategy.impl.VerifyStrategyImpl;
import io.lvdaxianer.github.breakpoint.transfer.utils.Constants;
import io.lvdaxianer.github.breakpoint.transfer.utils.RequestEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 默认策略工厂
 * 负责注册和管理文件上传相关的请求处理策略
 *
 * @author lihh
 */
@Slf4j
public class DefaultSelectStrategyFactory extends AbstractSelectStrategyFactory implements SelectStrategyFactory {

    /**
     * 默认的工厂实例
     */
    public static DefaultSelectStrategyFactory INSTANCE = new DefaultSelectStrategyFactory();

    /**
     * 注册策略处理器
     *
     * @author lihh
     */
    public void register() {
        log.info("开始注册文件上传策略处理器");

        // 注册分片上传策略
        INSTANCE.register(
                RequestMatchEntity.builder()
                        .method(RequestEnum.POST)
                        .isMatch(true)
                        .requestUrl(Constants.REQUEST_URL.SECTION_REQUEST_URL)
                        .build(),
                new SectionStrategyImpl()
        );
        log.debug("注册策略: {} {}", RequestEnum.POST, Constants.REQUEST_URL.SECTION_REQUEST_URL);

        // 注册文件验证策略
        INSTANCE.register(
                RequestMatchEntity.builder()
                        .isMatch(true)
                        .method(RequestEnum.GET)
                        .requestUrl(Constants.REQUEST_URL.VERIFY_REQUEST_URL)
                        .build(),
                new VerifyStrategyImpl()
        );
        log.debug("注册策略: {} {}", RequestEnum.GET, Constants.REQUEST_URL.VERIFY_REQUEST_URL);

        // 注册文件列表策略
        INSTANCE.register(
                RequestMatchEntity.builder()
                        .isMatch(true)
                        .method(RequestEnum.GET)
                        .requestUrl(Constants.REQUEST_URL.LIST_REQUEST_URL)
                        .build(),
                new ListStrategyImpl()
        );
        log.debug("注册策略: {} {}", RequestEnum.GET, Constants.REQUEST_URL.LIST_REQUEST_URL);

        // 注册文件合并策略
        INSTANCE.register(
                RequestMatchEntity.builder()
                        .isMatch(true)
                        .method(RequestEnum.GET)
                        .requestUrl(Constants.REQUEST_URL.MERGE_REQUEST_URL)
                        .build(),
                new MergeStrategyImpl()
        );
        log.debug("注册策略: {} {}", RequestEnum.GET, Constants.REQUEST_URL.MERGE_REQUEST_URL);

        log.info("文件上传策略处理器注册完成，共注册 {} 个策略", strategyMapping.size());
    }

    private DefaultSelectStrategyFactory() {
    }

    @Override
    public SelectStrategy newSelectStrategy(HttpServletRequest req) {
        String method = req.getMethod();
        String reqURL = req.getRequestURI();

        for (Map.Entry<RequestMatchEntity, SelectStrategy> map : strategyMapping.entrySet()) {
            // 解构 对象变量
            RequestMatchEntity entity = map.getKey();
            String requestURL = entity.getRequestUrl();
            RequestEnum requestEnum = entity.getMethod();

            // 先 匹配 method
            if (!StringUtils.equals(method.toLowerCase(), requestEnum.getMethod().toLowerCase())) {
                continue;
            }

            // requestURL 这是正则 URL，使用正则匹配
            Pattern pattern = Pattern.compile(requestURL);
            Matcher matcher = pattern.matcher(reqURL);
            if (matcher.find()) {
                log.debug("策略匹配成功: {} -> {}", reqURL, map.getValue().getClass().getSimpleName());
                return map.getValue();
            }
        }

        log.debug("未找到匹配的策略: {} {}", method, reqURL);
        return null;
    }
}
