package com.donews.alive;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.core.content.ContextCompat;

import com.donews.alive.api.KeepHttp;
import com.donews.alive.service.KeepAliveProviderService;
import com.keepalive.daemon.core.Constants;
import com.tencent.mmkv.MMKV;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by SnowDragon
 * Date on 2021/1/6
 * Description:
 */
public class KeepAlive {
    private static volatile KeepAlive instance;

    private KeepAlive() {

    }

    public static KeepAlive getInstance() {
        if (instance == null) {
            synchronized (KeepAlive.class) {
                if (instance == null) {
                    instance = new KeepAlive();
                }
            }
        }
        return instance;
    }


    private boolean mKeepAliveInitialized = false;
    private final List<String> mIKeepAliveProviderPathList = new ArrayList<>();

    public KeepAlive addIProvider(String iKeepAliveProviderPath) {
        if (mKeepAliveInitialized) {
            throw new RuntimeException("KeepAlive had Init(),please call addIProvider before init()");
        }
        mIKeepAliveProviderPathList.add(iKeepAliveProviderPath);
        return this;
    }

    public List<String> getIKeepAliveProviderPathList() {
        return mIKeepAliveProviderPathList;
    }

    public void init(Application context, Class cls) {
        if (!mKeepAliveInitialized) {
            MMKV.initialize(context);
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                startKeepLive(context, cls);
            }
            mKeepAliveInitialized = true;
        }


        //应用外广告配置
//        getAppOutConfig();

        //优先缓存壁纸，提升locker启动时间
//        ThreadUtils.getExecutor().execute(() -> WallpaperUtils.getWallpaper(context));
    }

    private void startKeepLive(Application application, Class cls) {
        //启动广告服务
        Intent intent = new Intent(application, KeepAliveProviderService.class);
        //通知小图标
        intent.getIntExtra(Constants.NOTI_SMALL_ICON_ID, 0);
        //通知栏大图标
        intent.getIntExtra(Constants.NOTI_LARGE_ICON_ID, 0);
        //标题
        intent.putExtra(Constants.NOTI_TITLE, application.getApplicationInfo().loadLabel(application.getPackageManager()));
        //描述
        intent.putExtra(Constants.NOTI_TEXT, "奖多多正在运行");

        intent.putExtra(Constants.NOTI_REMOTE_VIEWS, getContentView(application));

        //点击通知启类
        Intent startIntent = new Intent(application, cls);
        startIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pi = PendingIntent.getActivity(application, 0, startIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        intent.putExtra(Constants.NOTI_PENDING_INTENT, pi);
        ContextCompat.startForegroundService(application, intent);
    }

    @SuppressLint("RemoteViewLayout")
    private RemoteViews getContentView(Application application) {
        RemoteViews mView = new RemoteViews(application.getPackageName(), R.layout.common_view_notify_bk);
        return mView;
    }


    /**
     * 应用外弹窗广告配置
     */
    private void getAppOutConfig() {
        KeepHttp adConfigApi = new KeepHttp();
        adConfigApi.getAppOutConfig();
    }

}
