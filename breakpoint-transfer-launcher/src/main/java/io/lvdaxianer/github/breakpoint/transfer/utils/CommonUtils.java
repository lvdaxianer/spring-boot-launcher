package io.lvdaxianer.github.breakpoint.transfer.utils;

import io.lvdaxianer.github.breakpoint.transfer.exception.ErrorCode;
import io.lvdaxianer.github.breakpoint.transfer.exception.UploadFileException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import jakarta.servlet.http.HttpServletRequest;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 通用工具类
 * 提供路径处理、请求解析等常用工具方法
 *
 * @author lvdaxianer
 */
public class CommonUtils {

    /**
     * 解析 REST 请求的 URL 参数
     * 使用正则表达式匹配请求URL，提取路径参数
     *
     * @param requestURL 当前请求的完整URL
     * @param regexURL   用于匹配的正则表达式URL，包含捕获组
     * @return 提取的字符串参数列表，如果无匹配返回null
     * @author lvdaxianer
     */
    public static List<String> resolveRestParams(String requestURL, String regexURL) {
        Pattern pattern = Pattern.compile(regexURL);
        Matcher matcher = pattern.matcher(requestURL);
        if (matcher.find()) {
            List<String> paramList = new ArrayList<>();
            for (int i = 1; i <= matcher.groupCount(); i++) {
                String group = matcher.group(i);
                if (group != null) {
                    paramList.add(group);
                }
            }
            return paramList.isEmpty() ? null : paramList;
        }
        return null;
    }

    /**
     * 将相对路径转换为绝对路径
     *
     * @param relativePath 文件或目录的相对路径
     * @return 转换后的绝对路径
     * @author lvdaxianer
     */
    public static Path getAbsolutePath(String relativePath) {
        Path path = Paths.get(relativePath);
        return path.toAbsolutePath();
    }

    /**
     * 获取带格式的日志前缀后缀
     * 用于统一日志输出格式，便于在日志中快速定位
     *
     * @param str 日志消息内容
     * @return 格式化后的日志字符串
     * @author lvdaxianer
     */
    public static String getCommonPrefixAndSuffix(String str) {
        return String.format(" >>> upload file jdk: %s <<< ", str);
    }

    /**
     * 解析请求路径，返回格式化的提示字符串
     *
     * @param str 原始请求路径
     * @return 格式化的提示字符串
     * @author lvdaxianer
     */
    public static String resolveRequestPathString(String str) {
        return String.format("resolve request params: %s", str);
    }

    /**
     * 获取值，如果为空则返回默认值
     *
     * @param value        原始值，可能为空
     * @param replaceValue 空值时的替换值
     * @return 非空时返回原始值，否则返回替换值
     * @author lvdaxianer
     */
    public static String getValueOrDefault(String value, String replaceValue) {
        return StringUtils.isEmpty(value) ? replaceValue : value;
    }

    /**
     * 从 HTTP 请求中获取上传的文件
     *
     * @param req HTTP请求对象，必须是 multipart 请求
     * @return 上传的文件
     * @throws UploadFileException 如果请求不是 multipart 类型或获取文件失败
     * @author lvdaxianer
     */
    public static MultipartFile getMultipartFileByRequest(HttpServletRequest req) {
        if (!(req instanceof MultipartHttpServletRequest)) {
            throw new UploadFileException("请求必须是 multipart 类型", ErrorCode.PARAM_INVALID);
        }

        MultipartHttpServletRequest servletRequest = (MultipartHttpServletRequest) req;
        MultipartFile file = servletRequest.getFile("file");
        if (file == null) {
            throw new UploadFileException("无法获取上传文件", ErrorCode.FILE_EMPTY);
        }
        return file;
    }

    /**
     * 验证路径是否安全，防止路径遍历攻击
     * 只允许相对路径，不允许绝对路径和路径遍历字符
     *
     * @param path 用户输入的路径
     * @return 验证通过后的安全路径
     * @throws IllegalArgumentException 如果路径包含非法字符或越界访问
     * @author lvdaxianer
     */
    public static String sanitizePath(String path) {
        if (path == null || path.isEmpty()) {
            throw new IllegalArgumentException("路径不能为空");
        }

        // 检查路径遍历攻击
        if (path.contains("..") || path.contains("./") || path.contains(".\\")) {
            throw new IllegalArgumentException("路径包含非法字符");
        }

        // 检查绝对路径
        if (new File(path).isAbsolute()) {
            throw new IllegalArgumentException("不支持绝对路径");
        }

        // 规范化路径并返回
        return Paths.get(path).normalize().toString();
    }
}
