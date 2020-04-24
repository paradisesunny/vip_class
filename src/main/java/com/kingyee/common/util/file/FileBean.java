package com.kingyee.common.util.file;


/**
 * 文件Bean
 *
 * @author 张烨
 * @version 2017年03月22日
 */
public class FileBean {
    private String fileName;
    private String fileType;
    private String fileUrl;
    private String fileThumbUrl;
    private String fileDesc;
    private String fileDate;
    private Long fileCatPk;
    private String fileCat;
    private String fileCatDesc;
    private Long fileUserId;
    private String fileData;
    private Integer fileSize;

    public Integer getFileSize() {
        return fileSize;
    }

    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileThumbUrl() {
        return fileThumbUrl;
    }

    public void setFileThumbUrl(String fileThumbUrl) {
        this.fileThumbUrl = fileThumbUrl;
    }

    public String getFileDesc() {
        return fileDesc;
    }

    public void setFileDesc(String fileDesc) {
        this.fileDesc = fileDesc;
    }

    public String getFileDate() {
        return fileDate;
    }

    public void setFileDate(String fileDate) {
        this.fileDate = fileDate;
    }

    public Long getFileCatPk() {
        return fileCatPk;
    }

    public void setFileCatPk(Long fileCatPk) {
        this.fileCatPk = fileCatPk;
    }

    public String getFileCat() {
        return fileCat;
    }

    public void setFileCat(String fileCat) {
        this.fileCat = fileCat;
    }

    public String getFileCatDesc() {
        return fileCatDesc;
    }

    public void setFileCatDesc(String fileCatDesc) {
        this.fileCatDesc = fileCatDesc;
    }

    public Long getFileUserId() {
        return fileUserId;
    }

    public void setFileUserId(Long fileUserId) {
        this.fileUserId = fileUserId;
    }

    public String getFileData() {
        return fileData;
    }

    public void setFileData(String fileData) {
        this.fileData = fileData;
    }
}
