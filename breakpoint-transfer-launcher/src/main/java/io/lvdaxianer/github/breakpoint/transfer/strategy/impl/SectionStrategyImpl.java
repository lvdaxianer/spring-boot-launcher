package io.lvdaxianer.github.breakpoint.transfer.strategy.impl;

import io.lvdaxianer.github.breakpoint.transfer.exception.ErrorCode;
import io.lvdaxianer.github.breakpoint.transfer.exception.ParamCountException;
import io.lvdaxianer.github.breakpoint.transfer.exception.UploadFileException;
import io.lvdaxianer.github.breakpoint.transfer.extend.FileOperate;
import io.lvdaxianer.github.breakpoint.transfer.strategy.SelectStrategy;
import io.lvdaxianer.github.breakpoint.transfer.utils.CommonUtils;
import io.lvdaxianer.github.breakpoint.transfer.utils.Constants;
import io.lvdaxianer.github.breakpoint.transfer.utils.result.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * 这是 切片文件上传的 策略实现
 *
 * @author lihh
 */
public class SectionStrategyImpl implements SelectStrategy {

    @Override
    public ResponseEntity apply(HttpServletRequest req, FileOperate fileOperate) {
        String requestURI = req.getRequestURI();
        List<Object> list = CommonUtils.resolveRestParams(requestURI, Constants.REQUEST_URL.SECTION_REQUEST_URL);

        if (null == list || list.size() != 2)
            throw new ParamCountException(Constants.REQUEST_URL.SECTION_REQUEST_URL, 2, list == null ? 0 : list.size());

        // 从这里获取 file
        MultipartFile file = CommonUtils.getMultipartFileByRequest(req);
        if (null == file)
            throw new UploadFileException("上传文件为空或类型错误", ErrorCode.FILE_EMPTY);

        return fileOperate.upload(file, (String) list.get(0), (String) list.get(1));
    }
}
