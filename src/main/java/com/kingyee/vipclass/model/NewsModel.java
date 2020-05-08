package com.kingyee.vipclass.model;

import com.kingyee.common.util.TimeUtil;

public class NewsModel {
    private Long fileId;
    private String fileName;
    private String filePic;
    private Integer newsNum;
    private Long updateDate;
    /**
     * 百分比
     */
    private String percent;

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePic() {
        return filePic;
    }

    public void setFilePic(String filePic) {
        this.filePic = filePic;
    }

    public Integer getNewsNum() {
        return newsNum;
    }

    public void setNewsNum(Integer newsNum) {
        this.newsNum = newsNum;
    }

    public Long getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Long updateDate) {
        this.updateDate = updateDate;
    }

    private String updateDateStr;

    public String getUpdateDateStr() {
        updateDateStr = TimeUtil.longToString(this.getUpdateDate(), TimeUtil.FORMAT_DATE);
        return updateDateStr;
    }

    public void setUpdateDateStr(String updateDateStr) {
        this.updateDateStr = updateDateStr;
    }
}
