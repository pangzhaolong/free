package com.donews.network.room.bean;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * 下载文件信息,存入数据库,方便下一次直接展示下载信息，用于断点续传
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/8 16:50
 */
@Entity(tableName = "download_info", indices = {@Index(value = {"url", "save_path", "file_suffix"}, unique = true)})
public class DownloadInfo {

    /** 数据库key id */
    @PrimaryKey(autoGenerate = true)
    private long id;
    /** 下载地址 */
    private String url;
    /** 下载文件保存地址 */
    @ColumnInfo(name = "save_path")
    private String savePath;
    /** 文件后缀名 */
    @ColumnInfo(name = "file_suffix")
    private String fileSuffix;
    /** 缓存文件地址 */
    @ColumnInfo(name = "temp_file_path")
    private String tempFilePath;
    /** 最终下载文件保存地址 */
    @ColumnInfo(name = "download_file_path")
    private String downloadFilePath;
    /** 已下载文件长度 */
    @ColumnInfo(name = "current_length")
    private long currentLength;
    /** 下载文件总长度 */
    @ColumnInfo(name = "total_length")
    private long totalLength;
    /** 状态，备用 */
    private int status;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public String getFileSuffix() {
        return fileSuffix;
    }

    public void setFileSuffix(String fileSuffix) {
        this.fileSuffix = fileSuffix;
    }

    public String getTempFilePath() {
        return tempFilePath;
    }

    public void setTempFilePath(String tempFilePath) {
        this.tempFilePath = tempFilePath;
    }

    public String getDownloadFilePath() {
        return downloadFilePath;
    }

    public void setDownloadFilePath(String downloadFilePath) {
        this.downloadFilePath = downloadFilePath;
    }

    public long getCurrentLength() {
        return currentLength;
    }

    public void setCurrentLength(long currentLength) {
        this.currentLength = currentLength;
    }

    public long getTotalLength() {
        return totalLength;
    }

    public void setTotalLength(long totalLength) {
        this.totalLength = totalLength;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @NonNull
    @Override
    public String toString() {
        return "DownloadInfo{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", savePath='" + savePath + '\'' +
                ", fileSuffix='" + fileSuffix + '\'' +
                ", tempFilePath='" + tempFilePath + '\'' +
                ", downloadFilePath='" + downloadFilePath + '\'' +
                ", currentLength=" + currentLength +
                ", totalLength=" + totalLength +
                ", status=" + status +
                '}';
    }
}
