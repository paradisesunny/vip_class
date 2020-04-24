package com.kingyee.common.baidu.ueditor.upload;

import com.kingyee.common.baidu.ueditor.PathFormat;
import com.kingyee.common.baidu.ueditor.define.AppInfo;
import com.kingyee.common.baidu.ueditor.define.BaseState;
import com.kingyee.common.baidu.ueditor.define.FileType;
import com.kingyee.common.baidu.ueditor.define.State;
import com.kingyee.common.spring.mvc.WebUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class BinaryUploader {
    private static Logger log = LoggerFactory
            .getLogger(BinaryUploader.class);

    public static final State save(HttpServletRequest request,
                                   Map<String, Object> conf) {
        try {
            List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("upfile");
            MultipartFile file = files.get(0);

            String savePath = (String) conf.get("savePath");
            String originFileName = files.get(0).getOriginalFilename();
            String suffix = FileType.getSuffixByFilename(originFileName);

            originFileName = originFileName.substring(0,
                    originFileName.length() - suffix.length());
            savePath = savePath + suffix;

            long maxSize = ((Long) conf.get("maxSize")).longValue();

            if (!validType(suffix, (String[]) conf.get("allowFiles"))) {
                return new BaseState(false, AppInfo.NOT_ALLOW_FILE_TYPE);
            }

            savePath = PathFormat.parse(savePath, originFileName);

            String physicalPath = WebUtil.getRealPath("") + savePath;

            File target = new File(physicalPath.substring(0, physicalPath.lastIndexOf("/")));
            if (!target.exists()) {
                target.mkdirs();
            }
            File targetFile = new File(physicalPath);
            file.transferTo(targetFile);
            State storageState = new BaseState(true);
            storageState.putInfo("size", targetFile.length());
            storageState.putInfo("title", targetFile.getName());
            storageState.putInfo("url", PathFormat.format(savePath));
            storageState.putInfo("type", suffix);
            storageState.putInfo("original", originFileName + suffix);
            return storageState;
        } catch (IOException e) {
            log.error("", e);
        }
        return new BaseState(false, AppInfo.IO_ERROR);
    }

    private static boolean validType(String type, String[] allowTypes) {
        List<String> list = Arrays.asList(allowTypes);

        return list.contains(type);
    }
}
