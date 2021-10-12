package com.donews.network.down;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.donews.network.room.NetworkDatabase;
import com.donews.network.room.bean.DownloadInfo;
import com.donews.network.room.dao.DownloadInfoDao;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.StringWriter;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @author by SnowDragon
 * Date on 2020/11/26
 * Description:
 */
public class DownloadTask {
    private static final String TAG = "DownloadTask";

    /** 缓存文件后缀 */
    private static final String TEMP_FILE_SUFFIX = ".temp";
    /** 更新间隔，避免频繁更新(以进度每增长1为单位) */
    private static final int UPDATE_INTERVAL = 1;
    /** 超时时间 */
    private static final int DEFAULT_TIME_OUT = 60 * 5;
    /** 读取超时时间 */
    private static final int DEFAULT_READ_TIME_OUT = 60 * 5;
    /** 下载错误重试次数 */
    private static final int RETRY_DOWNLOAD_TIMES = 3;

    /** 暂停下载 */
    private volatile boolean isPaused = false;
    /** 关闭下载 */
    private volatile boolean isCancel = false;

    private static final int DOWNLOAD_INIT = 1;
    private static final int DOWNLOAD_READY = 2;
    private static final int DOWNLOAD_RUNNING = 3;
    private static final int DOWNLOAD_SUCCESS = 4;
    private static final int DOWNLOAD_PAUSE = 5;
    private static final int DOWNLOAD_CANCEL = 6;
    private static final int DOWNLOAD_FAILED = 7;
    private static final int SDCARD_MISS = 8;
    private static final int UPDATE_PROGRESS = 9;


    private Context mContext;
    private final String mPkName;
    private final String mUrl;
    private final String mSavePath;
    private final String mFileSuffix;
    private final TaskDownloadListener mListener;

    private final File mTempFile;
    private final File mDownloadFile;

    private String mFilePath;

    /** 下载状态 */
    private int mDownloadState;

    /** 重试次数 */
    private int retryTimes = 0;


    private OkHttpClient mOkHttpClient;
    private Call mCall;
    private Disposable mDisposable;

    private DownloadInfoDao mDownloadInfoDao;
    /** 对应数据库对象 */
    private DownloadInfo mDownloadInfo;

    private final Handler mHandler = new Handler(Looper.getMainLooper());

    public DownloadTask(Context mContext, String pkName, String url, String path, TaskDownloadListener listener) {
        this(mContext, pkName, url, path, PathUtils.getFileExtensionFromUrl(url), listener);
    }

    public DownloadTask(Context mContext, String pkName, String url, TaskDownloadListener listener) {
        this(mContext, pkName, url, null, listener);
    }


    /***
     *
     *  Url没有后缀的时候，下载.apk
     *
     * @param context 上下文对象
     * @param pkName  下载文件（apk）包名
     * @param url 下载文件地址
     * @param savePath 指定文件保存地址
     * @param fileSuffix 指定文件保存后缀
     * @param listener 下载监听器
     */
    public DownloadTask(Context context, String pkName, String url, String savePath, String fileSuffix,
            TaskDownloadListener listener) {
        this.mContext = context.getApplicationContext();
        this.mPkName = pkName;
        this.mUrl = url;
        this.mSavePath = savePath;
        this.mFileSuffix = fileSuffix;
        this.mListener = listener;

        String fileNameTemp = MD5Util.MD5(url) + TEMP_FILE_SUFFIX;
        String fileNameReally = MD5Util.MD5(url) + fileSuffix;

        mTempFile = PathUtils.getPathFile(context, savePath, fileNameTemp);
        String tempFilePath = mTempFile.getAbsolutePath();
        mDownloadFile = new File(tempFilePath.substring(0, tempFilePath.lastIndexOf("/")), fileNameReally);
        retryTimes = 0;
        mOkHttpClient = getOkHttpClient();
        mDownloadState = DOWNLOAD_INIT;
    }


    public void execute() {
        if (mDownloadState == DOWNLOAD_RUNNING) {
            return;
        }
        if (mDownloadInfoDao == null) {
            mDownloadInfoDao = NetworkDatabase.getInstance(mContext).getDownloadInfoDao();
        }
        isPaused = false;
        isCancel = false;
        startDownload();
    }

    /** 检测下载是否添加到数据库 */
    private void startDownload() {
        mDownloadState = DOWNLOAD_READY;
        if (mDownloadFile.exists()) {
            //如果已经存在并且下载完成
            mDownloadState = DOWNLOAD_RUNNING;
            if (mListener != null) {
                mListener.onStart();
            }
            mDownloadState = DOWNLOAD_SUCCESS;
            mFilePath = mDownloadFile.getAbsolutePath();
            if (mListener != null) {
                mListener.onSuccess(mPkName, mFilePath);
                mListener.onFinish();
            }
            if (mTempFile.exists()) {
                if (!mTempFile.delete()) {
                    Log.i(TAG, "tempFile is exists,but delete failed,url = " + mUrl);
                }
            }
            return;
        }
        Observable.just(mUrl)
                .flatMap(new Function<String, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(@NonNull String s) throws Exception {
                        //判断数据库是否有下载
                        if (TextUtils.isEmpty(s)) {
                            return Observable.error(new Throwable("download url is empty"));
                        }
                        if (mTempFile == null) {
                            return Observable.error(new Throwable("mTempFile is null"));
                        }
                        mFilePath = mTempFile.getAbsolutePath();
                        mDownloadInfo = mDownloadInfoDao.queryDownloadInfo(mUrl, mSavePath, mFileSuffix);
                        try {
                            if (mDownloadInfo == null) {
                                mDownloadInfo = new DownloadInfo();
                                mDownloadInfo.setUrl(mUrl);
                                mDownloadInfo.setSavePath(mSavePath);
                                mDownloadInfo.setFileSuffix(mFileSuffix);
                                mDownloadInfo.setTempFilePath(mTempFile.getAbsolutePath());
                                mDownloadInfo.setDownloadFilePath(mDownloadFile.getAbsolutePath());
                                mDownloadInfo.setTotalLength(-1);
                                mDownloadInfo.setCurrentLength(mTempFile.length());
                                mDownloadInfo.setStatus(0);
                                long id = mDownloadInfoDao.insertDownloadInfo(mDownloadInfo);
                                mDownloadInfo.setId(id);
                            } else {
                                mDownloadInfo.setCurrentLength(mTempFile.length());
                                mDownloadInfoDao.uploadDownloadInfo(mDownloadInfo);
                            }
                            Logger.d(mDownloadInfo.toString());
                        } catch (Exception e) {
                            Logger.e(e, "");
                        }
                        return Observable.just(mUrl);
                    }
                })
                .flatMap(new Function<String, ObservableSource<String>>() {

                    @Override
                    public ObservableSource<String> apply(@NonNull String s) throws Exception {
                        //开始下载时，已下载的文件大小
                        long startLength = mTempFile.length();
                        Request request = new Request.Builder()
                                .url(mUrl)
                                .header("RANGE", "bytes=" + startLength + "-")
                                .build();
                        mCall = mOkHttpClient.newCall(request);
                        InputStream inputStream = null;
                        RandomAccessFile randomAccessFile = null;
                        try {
                            Response response = mCall.execute();
                            ResponseBody responseBody = response.body();
                            if (responseBody == null) {
                                return Observable.error(new Throwable("responseBody is null"));
                            }
                            //得到输入流
                            inputStream = responseBody.byteStream();
                            //得到任意保存文件处理类实例,断点续传
                            randomAccessFile = new RandomAccessFile(mTempFile.getAbsolutePath(), "rw");
                            if (startLength != 0) {
                                randomAccessFile.seek(startLength);
                            }
                            //剩余下载的文件大小
                            long contentLength = responseBody.contentLength();
                            long totalLength = contentLength + startLength;
                            mDownloadInfo.setTotalLength(totalLength);
                            long currentLength = startLength;
                            byte[] buffer = new byte[1024 * 4];
                            int length;
                            updateProgress(currentLength, totalLength);
                            while ((length = inputStream.read(buffer)) != -1) {
                                currentLength += length;
                                mDownloadInfo.setCurrentLength(currentLength);
                                //写入文件
                                randomAccessFile.write(buffer, 0, length);
                                updateProgress(currentLength, totalLength);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            Logger.e(e, "");
                            //写入错误，则更新进度
                            mDownloadInfoDao.uploadDownloadInfo(mDownloadInfo);
                            //读写错误则删除缓存文件，防止下一次继续读写导致文件包错误
                            mTempFile.delete();
                            return Observable.error(new Throwable("下载失败"));
                        } finally {
                            try {
                                if (inputStream != null) {
                                    inputStream.close();
                                }
                                if (randomAccessFile != null) {
                                    randomAccessFile.close();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        //下载完成，则删除这条记录
                        mDownloadInfoDao.deleteDownloadInfo(mDownloadInfo);
                        if (mFilePath.contains(TEMP_FILE_SUFFIX)) {
                            boolean renameResult = mTempFile.renameTo(mDownloadFile);
                            if (renameResult) {
                                mFilePath = mDownloadFile.getAbsolutePath();
                                Logger.d("download Success = " + mDownloadFile.getAbsolutePath());
                            } else {
                                return Observable.error(new Throwable("temp file rename to downloadFile failed"));
                            }
                        }
                        return Observable.just(mFilePath);
                    }
                })

                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mDisposable = d;
                        mDownloadState = DOWNLOAD_RUNNING;
                        if (mListener != null) {
                            mListener.onStart();
                        }
                    }

                    @Override
                    public void onNext(@NonNull String s) {
                        mDownloadState = DOWNLOAD_SUCCESS;
                        if (mListener != null) {
                            mListener.onSuccess(mPkName, s);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Logger.e(e, "");
                        if (mDownloadState == DOWNLOAD_PAUSE) {
                            if (mListener != null) {
                                mListener.onPaused(mFilePath);
                            }
                        } else {
                            mDownloadState = DOWNLOAD_FAILED;
                            if (retryTimes < RETRY_DOWNLOAD_TIMES) {
                                execute();
                                retryTimes++;
                                Log.e(TAG, " retry_download_times : " + retryTimes);
                            } else {
                                if (mListener != null) {
                                    mListener.onFailed(getStackTraceString(e));
                                }
                            }
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }


    protected static String getStackTraceString(Throwable tr) {
        if (tr == null) {
            return "";
        }
        Throwable t = tr;
        while (t != null) {
            if (t instanceof UnknownHostException) {
                return "";
            }
            t = t.getCause();
        }
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        tr.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }


    /**
     * 更新下载进度
     */
    private void updateProgress(long currentLength, long totalLength) {
        if (mListener != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mListener.onProgress(currentLength, totalLength, mFilePath);
                }
            });
        }
    }

    private OkHttpClient getOkHttpClient() {
        if (mOkHttpClient == null) {
            mOkHttpClient = new OkHttpClient.Builder()
                    //连接超时时间
                    .connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
                    //写操作 超时时间
                    .writeTimeout(DEFAULT_READ_TIME_OUT, TimeUnit.SECONDS)
                    //读操作超时时间
                    .readTimeout(DEFAULT_READ_TIME_OUT, TimeUnit.SECONDS)
                    //尝试重连，默认
                    .retryOnConnectionFailure(false)
                    //重定向
//                    .addInterceptor(new RedirectIntercept())
                    .build();
        }
        return mOkHttpClient;
    }


    public enum STATE {IDLE, RUNNING}


    public boolean isCancel() {
        return mDownloadState == DOWNLOAD_CANCEL;
    }

    public boolean isPaused() {
        return mDownloadState == DOWNLOAD_PAUSE;
    }

    public void pause() {
        if (isPaused && mDownloadState == DOWNLOAD_PAUSE) {
            return;
        }
        mDownloadState = DOWNLOAD_PAUSE;
        isPaused = true;
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
        if (mCall != null) {
            mCall.cancel();
        }
        if (mListener != null) {
            mListener.onPaused(mFilePath);
        }
    }

    public void cancel() {
        if (isCancel && mDownloadState == DOWNLOAD_CANCEL) {
            return;
        }
        mDownloadState = DOWNLOAD_PAUSE;
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
        if (mCall != null) {
            mCall.cancel();
        }
        if (mListener != null) {
            mListener.onCancel(mFilePath);
        }
        isCancel = true;
    }


    public interface TaskDownloadListener {

        /**
         * 下载开始
         */
        void onStart();

        /**
         * 下载进度更新
         *
         * @param currentLength 开始下载的文件长度
         * @param totalLength   总的文件长度
         * @param filepath      文件路径
         */
        void onProgress(long currentLength, long totalLength, String filepath);

        /**
         * 下载成功
         *
         * @param packageName 包名
         * @param filepath    文件路径
         */
        void onSuccess(String packageName, String filepath);

        /**
         * 下载失败
         *
         * @param errorMsg 错误信息
         */
        void onFailed(String errorMsg);

        /**
         * 暂停下载
         *
         * @param filepath 文件路径
         */
        void onPaused(String filepath);

        /**
         * 取消下载
         *
         * @param filepath 文件路径
         */
        void onCancel(String filepath);

        /**
         * 下载结束，无论成功还是失败
         */
        void onFinish();
    }
}
