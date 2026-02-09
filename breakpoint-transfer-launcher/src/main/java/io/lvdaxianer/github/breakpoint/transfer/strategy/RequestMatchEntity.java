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
@NoArgsConstructor
@Builder
public class RequestMatchEntity implements Serializable {
    private String requestUrl;
    private RequestEnum method;
    private boolean isMatch;
}
