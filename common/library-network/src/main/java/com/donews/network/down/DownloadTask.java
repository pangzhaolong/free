package com.donews.network.down;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.StringWriter;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
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

    private volatile boolean isPaused = false;
    private volatile boolean isCancel = false;


    /**
     * 单线程下载
     */
    private volatile static boolean downloading = false;
    private int RETRY_DOWNLOAD_TIMES = 3;
    private int downLoadFlag = 0;

    private static final int DOWNLOAD_SUCCESS = 0;
    private static final int DOWNLOAD_FAILED = 1;
    private static final int DOWNLOAD_PAUSE = 2;
    private static final int DOWNLOAD_CANCEL = 3;
    private static final int SDCARD_MISS = 4;
    private static final int UPDATE_PROGRESS = 5;

    /**
     * 更新间隔，避免频繁更新
     */
    private static final int updateInterval = 1;
    /**
     * 当前下载进度
     */
    private int currentProgress = 0;

    private TaskDownloadListener mListener;

    private String mUrl;


    private Context context;

    /**
     * 下载文件
     */
    private File fileTemp;
    private File fileDownloaded;
    private String filePath;
    private String fileNameTemp;
    private String fileNameReally;
    private String pkName;


    private OkHttpClient okHttpClient;

    /**
     * 超时时间
     */
    private static final int DEFAULT_TIME_OUT = 60 * 5;

    /**
     * 读取超时时间
     */
    private static final int DEFAULT_READ_TIME_OUT = 60 * 5;

    private String fileSuffix;
    private String tempFileSuffix = ".temp";
    private String path;

    private OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient.Builder()
                    //连接超时时间
                    .connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
                    //写操作 超时时间
                    .writeTimeout(DEFAULT_READ_TIME_OUT, TimeUnit.SECONDS)
                    //读操作超时时间
                    .readTimeout(DEFAULT_READ_TIME_OUT, TimeUnit.SECONDS)
                    //尝试重连，默认
                    .retryOnConnectionFailure(true)
                    //重定向
//                    .addInterceptor(new RedirectIntercept())
                    .build();
        }
        return okHttpClient;
    }


    public DownloadTask(Context context, String pkName, String url, TaskDownloadListener listener) {
        this(context, pkName, url, null, listener);
    }

    /**
     * @param context
     * @param url
     * @param path
     * @param listener
     */
    public DownloadTask(Context context, String pkName, String url, String path, TaskDownloadListener listener) {
        this(context, pkName, url, path, PathUtils.getFileExtensionFromUrl(url), listener);
    }

    //Url没有后缀的时候，下载.apk
    public DownloadTask(Context context, String pkName, String url, String path, String fileSuffix, TaskDownloadListener listener) {
        this.context = context;
        this.fileSuffix = fileSuffix;
        this.path = path;
        this.pkName = pkName;
        this.fileNameTemp = MD5Util.MD5(url) + tempFileSuffix;
        this.fileNameReally = MD5Util.MD5(url) + fileSuffix;

        fileTemp = PathUtils.getPathFile(context, path, fileNameTemp);

        String tempPath = fileTemp.getAbsolutePath();

        fileDownloaded = new File(tempPath.substring(0, tempPath.lastIndexOf("/")), fileNameReally);

        this.mUrl = url;

        this.mListener = listener;
        downLoadFlag = 0;
        okHttpClient = getOkHttpClient();
        Log.i(TAG," filePath: "+tempPath);
    }


    private Call mCall;

    /**
     * 有断点续传功能的
     */
    public void getRenewalDownRequest() {
        downloading = true;
        if (fileDownloaded.exists()) {
            Log.e(TAG, "downloadFilePath: " + fileDownloaded.getAbsolutePath());
            if (fileTemp.exists()) {
                fileTemp.delete();
            }
            mListener.onSuccess(pkName, fileDownloaded.getAbsolutePath());
            downloading = false;
            return;
        }

        //开始下载时，已下载的文件大小
        long startLength = fileTemp.length();
        Log.e(TAG, "startLength: " + startLength);
        Request request = new Request.Builder()
                .url(mUrl)
                .header("RANGE", "bytes=" + startLength + "-")
                .addHeader("Connection", "close")
                .build();

        downloading = true;

        //构建了一个完整的http请求
        mCall = getOkHttpClient().newCall(request);
        //发送请求
        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "onFailure: " + getStackTraceString(e));
                sendErrorMsg(e.getMessage());

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                ResponseBody responseBody = response.body();
                InputStream inputStream = responseBody.byteStream();//得到输入流
                RandomAccessFile randomAccessFile = new RandomAccessFile(fileTemp.getAbsolutePath(), "rw");//得到任意保存文件处理类实例

                //剩余下载的文件大小
                long mResidueTotalLength = responseBody.contentLength();
                Log.e(TAG, " onResponse: startLength " + startLength + " residueContentLength: " + mResidueTotalLength);
                if (startLength > 0 && mResidueTotalLength == 0) {
                    upDateProgress(100);
                }
                Log.i(TAG, " responseBody getLength  " + responseBody.contentLength());
                if (fileTemp.length() != 0) {
                    randomAccessFile.seek(fileTemp.length());
                }

                readFileStream(inputStream, randomAccessFile, startLength, mResidueTotalLength);
            }

        });
    }


    public void execute() {
        if (fileTemp == null) {
            sendErrorMsg("The download path is empty");
            return;
        }
        filePath = fileTemp.getAbsolutePath();

        if (downloading) {
            return;
        }
        getRenewalDownRequest();
    }


    /**
     * @param ips
     */
    private void readFileStream(InputStream ips, RandomAccessFile raf, long startLength, long residueContentLength) {
        Log.e(TAG, " readFileStream: startLength " + startLength + " residueContentLength: " + residueContentLength);
        byte[] buffer = new byte[1024 * 4];
        int total = 0;
        int len;
        upDateProgress(0);
        try {
            while ((len = ips.read(buffer)) != -1) {
                total += len;
                //写入文件
                raf.write(buffer, 0, len);
                int progress = (int) ((total + startLength) * 100 / (startLength + residueContentLength));

                if (isPaused) {
                    mHandle.sendEmptyMessage(DOWNLOAD_PAUSE);
                    break;
                }
                upDateProgress(progress);
            }
        } catch (IOException exception) {

            exception.printStackTrace();
            sendErrorMsg(getStackTraceString(exception));

        } finally {
            try {
                ips.close();
                raf.close();
            } catch (IOException exception) {
                exception.printStackTrace();
                Log.e(TAG, "stream close exception : " + getStackTraceString(exception));
            }

        }
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

    private void sendErrorMsg(String errorMsg) {
        Log.e(TAG, "sendErrorMsg :--->>>>>>>>>>>>" + errorMsg);

        downloading = false;
        if (downLoadFlag < RETRY_DOWNLOAD_TIMES) {
            Log.e(TAG, " retry_download_times : " + downLoadFlag);
            execute();
            downLoadFlag++;
        } else {
            Message message = mHandle.obtainMessage();
            message.what = DOWNLOAD_FAILED;
            message.obj = errorMsg;
            mHandle.sendMessage(message);
        }
    }

    /**
     * 更新下载进度
     *
     * @param progress 下载进度
     */
    private void upDateProgress(int progress) {

        if (mListener != null) {
            if ((progress - currentProgress) >= updateInterval && progress < 100) {
                currentProgress = progress;
                Message message = mHandle.obtainMessage();
                message.arg1 = progress;
                message.what = UPDATE_PROGRESS;
                mHandle.sendMessage(message);
            }
            if (progress >= 100) {
                mHandle.sendEmptyMessage(DOWNLOAD_SUCCESS);
            }
        }
    }

    private Handler mHandle = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWNLOAD_SUCCESS:
                    if (mListener != null) {


                        if (filePath.contains(tempFileSuffix)) {
                            boolean renameResult = fileTemp.renameTo(fileDownloaded);
                            Log.e(TAG, " renameResult:" + fileDownloaded.getAbsolutePath() + "  isExistance: " + renameResult);
                            filePath = fileDownloaded.getAbsolutePath();
                        }
                        Log.e(TAG, " handleMessage onSuccess:" + filePath);
                        mListener.onSuccess(pkName, filePath);
                    }
                    downloading = false;
                    break;
                case DOWNLOAD_FAILED:
                    String errorMsg = (String) msg.obj;
                    if (mListener != null) {
                        mListener.onFailed(errorMsg);
                    }
                    downloading = false;
                    break;
                case DOWNLOAD_PAUSE:
                    if (mListener != null) {
                        mListener.onPaused(currentProgress, filePath);
                    }
                    downloading = false;
                    break;
                case DOWNLOAD_CANCEL:
                    if (mListener != null) {
                        mListener.onCancel(filePath);
                    }
                    downloading = false;
                    break;
                case SDCARD_MISS:
                    if (mListener != null) {
                        mListener.onPathError(filePath);
                    }
                    downloading = false;
                    break;
                case UPDATE_PROGRESS:
                    if (mListener != null) {
                        mListener.onUpdate(msg.arg1, filePath);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    public enum STATE {IDLE, RUNNING}


    public boolean isCancel() {
        return isCancel;
    }

    public void setCancel(boolean cancel) {
        isCancel = cancel;
        if (mCall != null) {
            mCall.cancel();
            mHandle.sendEmptyMessage(DOWNLOAD_CANCEL);
        }


    }

    public void pause() {
        isPaused = true;

    }


    public interface TaskDownloadListener {
        /**
         * 下载更新
         *
         * @param progress 下载进度
         * @param filepath 文件路径
         */
        void onUpdate(int progress, String filepath);

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
         * @param progress 下载进度
         * @param filepath 文件路径
         */
        void onPaused(int progress, String filepath);

        /**
         * 取消下载
         *
         * @param filepath 文件路径
         */
        void onCancel(String filepath);

        /**
         * 文件路径错误
         *
         * @param filepath 文件路径
         */
        void onPathError(String filepath);
    }
}
