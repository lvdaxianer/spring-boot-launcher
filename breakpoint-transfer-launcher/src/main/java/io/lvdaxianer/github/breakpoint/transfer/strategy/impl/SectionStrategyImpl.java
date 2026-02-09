package io.lvdaxianer.github.breakpoint.transfer.strategy.impl;

import io.lvdaxianer.github.breakpoint.transfer.exception.ErrorCode;
import io.lvdaxianer.github.breakpoint.transfer.exception.ParamCountException;
import io.lvdaxianer.github.breakpoint.transfer.exception.UploadFileException;
import io.lvdaxianer.github.breakpoint.transfer.extend.FileOperate;
import io.lvdaxianer.github.breakpoint.transfer.strategy.SelectStrategy;
import io.lvdaxianer.github.breakpoint.transfer.utils.CommonUtils;
import io.lvdaxianer.github.breakpoint.transfer.utils.Constants;
import io.lvdaxianer.github.breakpoint.transfer.utils.result.ResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 分片上传策略实现
 * 处理文件分片上传请求，从请求中解析分片参数并调用文件操作接口
 *
 * @author lvdaxianer
 */
@Slf4j
public class SectionStrategyImpl implements SelectStrategy {

    /**
     * 处理分片上传请求
     * 解析请求URL中的目录名和文件名参数，获取上传的分片文件并执行上传
     *
     * @param req         HTTP请求对象，包含请求URL和上传文件
     * @param fileOperate 文件操作接口，用于执行实际上传操作
     * @return 上传操作结果，success字段表示是否上传成功
     * @throws ParamCountException    参数数量不匹配时抛出
     * @throws UploadFileException    文件为空或类型错误时抛出
     * @author lvdaxianer
     */
    @Override
    public ResponseEntity apply(HttpServletRequest req, FileOperate fileOperate) {
        String requestURI = req.getRequestURI();
        List<String> list = CommonUtils.resolveRestParams(requestURI, Constants.REQUEST_URL.SECTION_REQUEST_URL);

        // 验证参数数量，必须包含目录名和文件名两个参数
        if (list == null || list.size() != 2) {
            log.warn("分片上传参数数量错误，期望2个，实际{}个", list == null ? 0 : list.size());
            throw new ParamCountException(Constants.REQUEST_URL.SECTION_REQUEST_URL, 2, list == null ? 0 : list.size());
        }

        // 从请求中获取上传的分片文件
        MultipartFile file = CommonUtils.getMultipartFileByRequest(req);
        if (file == null || file.isEmpty()) {
            log.warn("分片上传失败：文件为空");
            throw new UploadFileException("上传文件为空或类型错误", ErrorCode.FILE_EMPTY);
        }

        String baseDir = CommonUtils.sanitizePath(list.get(0));
        String filename = CommonUtils.sanitizePath(list.get(1));
        log.debug("开始上传分片文件: dir={}, filename={}", baseDir, filename);

        return fileOperate.upload(file, baseDir, filename);
    }
}
