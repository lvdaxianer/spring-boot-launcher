package io.lvdaxianer.github.breakpoint.transfer.interceptor;

import com.alibaba.fastjson2.JSON;
import io.lvdaxianer.github.breakpoint.transfer.extend.FileOperate;
import io.lvdaxianer.github.breakpoint.transfer.strategy.SelectStrategy;
import io.lvdaxianer.github.breakpoint.transfer.strategy.impl.adapter.DefaultSelectStrategyAdapter;
import io.lvdaxianer.github.breakpoint.transfer.utils.result.ResponseEntity;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * HTTP 请求拦截器
 * 负责拦截文件上传相关请求并分发到对应的策略处理器
 *
 * @author lihh
 */
@Slf4j
@Component
public class HttpRequestInterceptor implements HandlerInterceptor {

    @Resource
    private FileOperate fileOperate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String method = request.getMethod();
        String requestURI = request.getRequestURI();

        log.debug("收到请求: {} {}", method, requestURI);

        // 拿到执行策略
        SelectStrategy selectStrategy = DefaultSelectStrategyAdapter.INSTANCE.newSelectStrategy(request);
        if (selectStrategy == null) {
            log.debug("未找到匹配的策略，放行请求: {} {}", method, requestURI);
            return HandlerInterceptor.super.preHandle(request, response, handler);
        }

        log.debug("匹配到策略处理器，执行请求: {} {}", method, requestURI);

        // 从这里拿到策略开始执行
        ResponseEntity entity = selectStrategy.apply(request, fileOperate);

        log.info("请求处理完成: {} {}, 结果: {}", method, requestURI, entity.getCode());

        // 设置响应的类型
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());

        // 往回写数据
        PrintWriter out = response.getWriter();
        out.write(JSON.toJSONString(entity));
        out.flush();

        return false;
    }
}
