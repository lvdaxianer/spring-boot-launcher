package io.lvdaxianer.github.breakpoint.transfer.strategy;

import io.lvdaxianer.github.breakpoint.transfer.utils.RequestEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 请求匹配实体
 * 用于匹配请求的方法和URL
 *
 * @author lihh
 */
@Data
@AllArgsConstructor
@Builder
public class RequestMatchEntity implements Serializable {

    /**
     * 请求URL
     */
    private String requestUrl;

    /**
     * 请求方法类型
     */
    private RequestEnum method;

    /**
     * 是否匹配
     */
    private boolean isMatch;

    /**
     * 无参构造函数
     */
    public RequestMatchEntity() {
    }
}
