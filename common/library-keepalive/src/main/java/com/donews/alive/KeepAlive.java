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
import java.util.Calendar;
import java.util.List;
import java.util.Random;

/**
 * @author by SnowDragon
 * Date on 2021/1/6
 * Description:
 */
public class KeepAlive {
    private static volatile KeepAlive instance;
    private Class mClass;
    private Application mApplication;

    private boolean mNeedChange = false;

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
        mApplication = context;
        mClass = cls;
        if (!mKeepAliveInitialized) {
            MMKV.initialize(context);
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                startKeepLive(context, cls);
                startThread();
            }
            mKeepAliveInitialized = true;
        }


        //应用外广告配置
//        getAppOutConfig();

        //优先缓存壁纸，提升locker启动时间
//        ThreadUtils.getExecutor().execute(() -> WallpaperUtils.getWallpaper(context));
    }

    private void startThread() {
        new Thread(() -> {
            Calendar c = null;
            while (true) {
                try {
                    Thread.sleep(60 * 1000);
                    c = Calendar.getInstance();
                    int hour = c.get(Calendar.HOUR_OF_DAY);
                    int minute = c.get(Calendar.MINUTE);
//                        LogUtil.e("time:" + new Date().toString());
//                    LogUtil.e("time: Hour:" + hour + " Minute:" + minute);
                    if (hour == 8 && minute == 30) {
                        updateKeepAliveNotification(mApplication, mClass, 1);
                    } else if (hour == 10 && minute == 3) {
                        updateKeepAliveNotification(mApplication, mClass, 2);
                    } else if (hour == 12 && minute == 30) {
                        updateKeepAliveNotification(mApplication, mClass, 3);
                    } else if (hour == 15 && minute == 0) {
                        updateKeepAliveNotification(mApplication, mClass, 4);
                    } else if (hour == 19 && minute == 30) {
                        updateKeepAliveNotification(mApplication, mClass, 5);
                    } else if (hour == 22 && minute == 0) {
                        updateKeepAliveNotification(mApplication, mClass, 6);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
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
        RemoteViews mView = new RemoteViews(application.getPackageName(), R.layout.common_view_notify);
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        mView.setTextViewText(R.id.nf_time, hour + ":" + minute);

        mView.setTextViewText(R.id.nf_title, "√【代签收】");
        mView.setTextViewText(R.id.nf_content, "派送中...已向您的账户转入华为P40一台\uD83D\uDCF1 点击查收");
        return mView;
    }

    public void updateKeepAliveNotification(Application application, Class cls, int index) {
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

        intent.putExtra(Constants.NOTI_REMOTE_VIEWS, getNotificationContentView(application, index));

        //点击通知启类
        Intent startIntent = new Intent(application, cls);
        startIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pi = PendingIntent.getActivity(application, 0, startIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        intent.putExtra(Constants.NOTI_PENDING_INTENT, pi);
        ContextCompat.startForegroundService(application, intent);
    }

    @SuppressLint("RemoteViewLayout")
    private RemoteViews getNotificationContentView(Application application, int index) {
        RemoteViews mView = new RemoteViews(application.getPackageName(), R.layout.common_view_notify);
        int random = 1;
        String title = "";
        String content = "";
        String[] titles;
        String[] contents;
        switch (index) {
            case 1:
                random = new Random().nextInt(3);
                titles = application.getResources().getStringArray(R.array.yx_am_title);
                contents = application.getResources().getStringArray(R.array.yx_am_content);
                break;
            case 2:
                random = new Random().nextInt(3);
                titles = application.getResources().getStringArray(R.array.nf_am_title);
                contents = application.getResources().getStringArray(R.array.nf_am_content);
                break;
            case 3:
                random = new Random().nextInt(3);
                titles = application.getResources().getStringArray(R.array.yx_pm_title);
                contents = application.getResources().getStringArray(R.array.yx_pm_content);
                break;
            case 4:
                random = new Random().nextInt(3);
                titles = application.getResources().getStringArray(R.array.yx_pm_rp_title);
                contents = application.getResources().getStringArray(R.array.yx_pm_rp_content);
                break;
            case 5:
                random = new Random().nextInt(4);
                titles = application.getResources().getStringArray(R.array.yx_pm_score_title);
                contents = application.getResources().getStringArray(R.array.yx_pm_score_content);
                break;
            case 6:
                random = new Random().nextInt(3);
                titles = application.getResources().getStringArray(R.array.yx_pm_gift_title);
                contents = application.getResources().getStringArray(R.array.yx_pm_gift_content);
                break;
            default:
                titles = application.getResources().getStringArray(R.array.yx_pm_title);
                contents = application.getResources().getStringArray(R.array.yx_pm_content);
        }

//        LogUtil.e("random index:" + random);
        title = titles[random];
        content = contents[random];
        mView.setTextViewText(R.id.nf_title, title);
        mView.setTextViewText(R.id.nf_content, content);
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        mView.setTextViewText(R.id.nf_time, hour + ":" + minute);
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
