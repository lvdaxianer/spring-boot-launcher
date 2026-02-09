package io.lvdaxianer.github.breakpoint.transfer.exception;

import lombok.Getter;

/**
 * 错误码枚举
 * 统一管理项目中的错误码，便于问题排查和监控
 *
 * @author lihh
 */
@Getter
public enum ErrorCode {

    // ========== 参数相关错误 ==========
    /**
     * 参数数量错误
     */
    PARAM_COUNT_ERROR("PARAM_001", "请求参数数量不正确"),

    /**
     * 参数格式错误
     */
    PARAM_FORMAT_ERROR("PARAM_002", "请求参数格式不正确"),

    /**
     * 参数值无效
     */
    PARAM_INVALID("PARAM_003", "参数值无效"),

    // ========== 文件上传相关错误 ==========
    /**
     * 文件为空
     */
    FILE_EMPTY("FILE_001", "上传文件为空"),

    /**
     * 文件类型不支持
     */
    FILE_TYPE_NOT_SUPPORTED("FILE_002", "文件类型不支持"),

    /**
     * 文件大小超出限制
     */
    FILE_SIZE_EXCEEDED("FILE_003", "文件大小超出限制"),

    /**
     * 文件上传失败
     */
    FILE_UPLOAD_FAILED("FILE_004", "文件上传失败"),

    /**
     * 文件合并失败
     */
    FILE_MERGE_FAILED("FILE_005", "文件合并失败"),

    /**
     * 文件不存在
     */
    FILE_NOT_FOUND("FILE_006", "文件不存在"),

    /**
     * 文件存储错误
     */
    FILE_STORAGE_ERROR("FILE_007", "文件存储错误"),

    // ========== 路径相关错误 ==========
    /**
     * 路径无效
     */
    PATH_INVALID("PATH_001", "路径无效"),

    /**
     * 路径遍历攻击检测
     */
    PATH_TRAVERSAL_DETECTED("PATH_002", "检测到潜在的路径遍历攻击"),

    // ========== 配置相关错误 ==========
    /**
     * 配置错误
     */
    CONFIG_ERROR("CFG_001", "配置错误"),

    /**
     * 存储类型未配置
     */
    STORAGE_TYPE_NOT_CONFIGURED("CFG_002", "存储类型未配置");

    /**
     * 错误码
     */
    private final String code;

    /**
     * 错误描述
     */
    private final String description;

    ErrorCode(String code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * 根据错误码获取枚举
     *
     * @param code 错误码
     * @return ErrorCode 枚举
     */
    public static ErrorCode fromCode(String code) {
        for (ErrorCode errorCode : values()) {
            if (errorCode.code.equals(code)) {
                return errorCode;
            }
        }
        return null;
    }
}
