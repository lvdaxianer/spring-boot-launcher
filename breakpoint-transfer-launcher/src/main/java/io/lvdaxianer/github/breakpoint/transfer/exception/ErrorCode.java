package io.lvdaxianer.github.breakpoint.transfer.exception;

/**
 * 错误码枚举
 * 统一管理项目中的错误码，便于问题排查和监控
 *
 * @author lihh
 */
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
    FILE_STORAGE_ERROR("FILE_007", "文件存储错误");

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
     * 获取错误码
     *
     * @return 错误码
     */
    public String getCode() {
        return code;
    }

    /**
     * 获取错误描述
     *
     * @return 错误描述
     */
    public String getDescription() {
        return description;
    }
}
