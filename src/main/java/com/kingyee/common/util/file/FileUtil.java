package com.kingyee.common.util.file;

import com.kingyee.common.spring.mvc.WebUtil;
import com.kingyee.common.util.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * Created by zhl on 2018/5/3.
 * 文件保存
 */
public class FileUtil {
    private final static Logger log = LoggerFactory.getLogger(FileUtil.class);

    /**
     * 保存文件
     */
    public static FileBean saveFile(MultipartFile file) {
        FileBean bean = new FileBean();
        String basePath = WebUtil.getRealPath("/");
        Long time = TimeUtil.dateToLong();
        bean.setFileDate(TimeUtil.longToString(time, "yyyy-MM-dd HH:mm:ss"));
        String fileOriginalName = file.getOriginalFilename();
        String ext = (fileOriginalName.lastIndexOf('.') < 0 ? null : fileOriginalName.substring(fileOriginalName.lastIndexOf('.')));
        String desc = (fileOriginalName.lastIndexOf('.') < 0 ? null : fileOriginalName.substring(0, fileOriginalName.lastIndexOf('.')));
        bean.setFileDesc(desc);
        //model.setFileExtName((ext == null || ext.length() < 1) ? file.getContentType() : ext.toLowerCase()
        // .substring(1));
        // 2019年3月20日 11:08:33 默认使用原始文件名
        bean.setFileName(fileOriginalName);
        bean.setFileSize(Long.valueOf(file.getSize()).intValue());
        String date = TimeUtil.longToString(time, "yyyyMMdd");
        //每天一个文件夹
        String folder = "/upload/" + date;
        //原始图文件
        String path = folder + "/pic/";
        //缩略图文件
        String thumb = folder + "/thumbs/";
        String fileName = basePath + path + time + ext;
        String fileThumbName = basePath + thumb + time + "_thumb" + ext;
        if (!new File(basePath + path).exists()) {
            new File(basePath + path).mkdirs();
        }
        if (!new File(basePath + thumb).exists()) {
            new File(basePath + thumb).mkdirs();
        }
        File dest = new File(fileName);
        //保存原始文件
        try {
            file.transferTo(dest);
            if (!ext.equalsIgnoreCase(".png")) {
                //自动压缩jpg原图
                ImageUtil.saveImage(dest, dest);
            }
            //保存缩略图
            ImageUtil.saveImage(fileName, fileThumbName, 166, 212);
        } catch (IOException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }

        bean.setFileUrl(path + time + ext);
        bean.setFileThumbUrl(thumb + thumb + time + "_thumb" + ext);
        return bean;
    }

}
