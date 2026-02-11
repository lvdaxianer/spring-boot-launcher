package io.lvdaxianer.github.breakpoint.transfer.utils.result;

import lombok.*;

import java.io.Serializable;

/**
 * 统一响应实体
 * 用于封装API接口的响应数据
 *
 * @author lvdaxianer
 */
@Getter
@AllArgsConstructor
@Data
@NoArgsConstructor
public class ResponseEntity implements Serializable {

    /**
     * 操作是否成功
     * -- GETTER --
     * 获取是否成功
     */
    private boolean success;

    /**
     * 响应数据
     * -- GETTER --
     * 获取数据
     */
    private Object data;

    /**
     * 响应状态码，默认为"200"
     * -- GETTER --
     * 获取状态码
     */
    private String code = "200";

    /**
     * 响应消息
     * -- GETTER --
     * 获取消息
     */
    private String message;

    /**
     * 设置是否成功
     *
     * @param success 是否成功
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * 设置数据
     *
     * @param data 数据
     */
    public void setData(Object data) {
        this.data = data;
    }

    /**
     * 设置状态码
     *
     * @param code 状态码
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 设置消息
     *
     * @param message 消息
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 创建成功响应
     *
     * @param data 响应数据
     * @return ResponseEntity实例
     */
    public static ResponseEntity ok(Object data) {
        ResponseEntity entity = new ResponseEntity();
        entity.setSuccess(true);
        entity.setData(data);
        entity.setCode("200");
        return entity;
    }
}
