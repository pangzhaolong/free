package com.donews.common.download;

import android.content.Context;
import android.text.TextUtils;

import com.donews.network.down.DownloadTask;

/**
 * @author by SnowDragon
 * Date on 2020/11/27
 * Description:
 */
public class DownloadManager {
    public static final String APK_SUFFIX = ".apk";

    DownloadTask downloadTask;
    boolean immInstall = true;
    private Context mContext;
    private String url;
    private String path;
    private DownloadListener listener;
    private String fileNameSuffix;
    private String pkName;

    public DownloadManager(Context context, String pkName, String url, DownloadListener listener) {
        this(context, pkName, url, null, null, listener);
    }

    public DownloadManager(Context context, String pkName, String url, String fileNameSuffix, DownloadListener listener) {
        this(context, pkName, url, null, fileNameSuffix, listener);
    }

    public DownloadManager(Context context, String pkName, String url, String path, String fileNameSuffix, DownloadListener listener) {
        this.mContext = context.getApplicationContext();
        this.url = url;
        this.path = path;
        this.listener = listener;
        this.fileNameSuffix = fileNameSuffix;
        this.pkName = pkName;

    }

    /**
     * 开始下载
     */
    public void start() {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        if (downloadTask == null) {
            if (immInstall) {
                fileNameSuffix = APK_SUFFIX;
            }
            if (TextUtils.isEmpty(fileNameSuffix)) {
                downloadTask = new DownloadTask(mContext, pkName, url, path, new DownloadTaskListener(listener));
            } else {
                downloadTask = new DownloadTask(mContext, pkName, url, path, fileNameSuffix, new DownloadTaskListener(listener));
            }

        }

        downloadTask.execute();
    }

    /**
     * 如果下载的是apk，默认为下载完成立即安装；false，需要调用安装
     *
     * @param immInstall 是否立即安装apk，默认为true
     */
    public void setImmInstall(boolean immInstall) {
        this.immInstall = immInstall;
    }

    /**
     * 暂停下载
     */
    public void pause() {
        if (downloadTask != null) {
            downloadTask.pause();
        }
    }

    private class DownloadTaskListener implements DownloadTask.TaskDownloadListener {

        private DownloadListener listener;

        public DownloadTaskListener(DownloadListener downloadListener) {
            this.listener = downloadListener;

        }

        @Override
        public void onUpdate(int progress, String filepath) {
            if (listener != null) {
                listener.updateProgress(progress);
            }
        }

        @Override
        public void onSuccess(String pkName, String filepath) {
            if (filepath == null) {
                return;
            }

            if (listener != null) {
                listener.downloadComplete(pkName, filepath);
            }

            if (immInstall && filepath.contains(APK_SUFFIX)) {
                DownloadHelper.installApp(mContext, filepath);
            }
        }

        @Override
        public void onFailed(String errorMsg) {
            if (listener != null) {
                listener.downloadError(errorMsg);
            }
        }

        @Override
        public void onPaused(int progress, String filepath) {

        }

        @Override
        public void onCancel(String filepath) {

        }

        @Override
        public void onPathError(String filepath) {

        }
    }


}
