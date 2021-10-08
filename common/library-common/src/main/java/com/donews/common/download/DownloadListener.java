package com.donews.common.download;

/**
 * @author by SnowDragon
 * Date on 2020/11/27
 * Description:
 */
public interface DownloadListener {
    /**
     * 下载进度
     *
     * @param progress 进度
     */
    void updateProgress(int progress);

    /**
     * 下载完成
     *
     * @param pkName
     * @param path 文件路径
     */
    void downloadComplete(String pkName,String path);

    /**
     * 下载失败
     *
     * @param error 错误信息
     */
    void downloadError(String error);

}
