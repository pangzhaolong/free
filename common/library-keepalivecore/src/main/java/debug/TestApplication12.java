package debug;

import android.app.ActivityManager;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.donews.base.base.BaseApplication;
import com.donews.common.NotifyLuncherConfigManager;
import com.donews.crashhandler.core.CrashCoreHandler;
import com.donews.keepalive.Dazzle;
import com.donews.keepalive.DazzleCallback;
import com.donews.keepalive.ForegroundNotificationClickListener;
import com.donews.keepalive.NotificationClickReceiver;
import com.donews.network.EasyHttp;
import com.donews.network.cache.converter.GsonDiskConverter;
import com.donews.network.cache.model.CacheMode;
import com.donews.network.cookie.CookieManger;
import com.donews.network.model.HttpHeaders;
import com.donews.utilslibrary.base.UtilsConfig;
import com.donews.utilslibrary.utils.AppInfo;
import com.donews.utilslibrary.utils.KeyConstant;
import com.keepalive.daemon.core.BuildConfig;
import com.keepalive.daemon.core.DaemonHolder;
import com.keepalive.daemon.core.R;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.List;


/**
 * 类描述
 *
 * @author Swei
 * @date 2021/4/7 17:30
 * @since v1.0
 */
public class TestApplication12 extends BaseApplication {
    public static final String TAG = "keepalive-test";

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        Log.d(TAG, "application attachBaseContext..");

        CrashCoreHandler.install(true);

        DaemonHolder.setGlobalNotifycation(createNotification());
        DaemonHolder.getInstance().attach( this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        String process = getProcessName(this);
        Log.d(TAG, "application onCreate,processName = " + process);
        if (!TextUtils.isEmpty(process) && process.equals(getPackageName())) {
            UtilsConfig.init(this);
            EasyHttp.init(this);
            if (this.issDebug()) {
                EasyHttp.getInstance().debug("HoneyLife", true);
            }
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.put(HttpHeaders.HEAD_AUTHORIZATION, AppInfo.getToken(""));

            EasyHttp.getInstance()
                    .setBaseUrl("http://baobab.kaiyanapp.com")
                    .setReadTimeOut(15 * 1000)
                    .setWriteTimeOut(15 * 1000)
                    .setConnectTimeout(15 * 1000)
                    .setRetryCount(3)
                    .setCookieStore(new CookieManger(this))
                    .setCacheDiskConverter(new GsonDiskConverter())
                    .setCacheMode(CacheMode.FIRSTREMOTE)
                    .addCommonHeaders(httpHeaders);
            CrashReport.initCrashReport(getApplicationContext(), KeyConstant.getBuglyId(), BuildConfig.DEBUG);
            NotifyLuncherConfigManager.update();


            initDazzle();
        }
    }

    private Notification createNotification(){
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, NotificationClickReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, -1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new NotificationApiCompat.Builder(this,
                nm,
                "kxyd_channel_red",
                getString(R.string.app_name), R.drawable.noti_icon)
                .setContentTitle("点我领福利~~~")
                .setContentText("热门活动等待你的参与，参与即送金币~")
                .setAutoCancel(false)
                .setContentIntent(pendingIntent)
//                .setStyle()
                .setCategory()
                .setTicker(getString(R.string.app_name))
                .setOngoing(true)
                .setPriority(Notification.PRIORITY_MIN)
                .setColor()
                .setOnlyAlertOnce(true)
                .builder().notificationApiCompat;

        return notification;
    }

    private void initDazzle(){

        int NOTIFY_KEEP_ID = Dazzle.NOTIFICATION_KEEPALIVE;//要与保活sdk中的id保持一致
        Dazzle.init(this, true, createNotification(), NOTIFY_KEEP_ID, new DazzleCallback() {

            @Override
            public void onWorking() {
                System.out.println("onWorking");
            }

            @Override
            public void onStop() {

            }

            @Override
            public void doReport(String type, int pid, long usageTime, long intervalTime) {
                System.out.println("doReport : type = " + type + " pid = " + pid + " usageTime = " + usageTime + " intervalTime" +
                        " = " + intervalTime);
            }
        }, new ForegroundNotificationClickListener() {

            @Override
            public void foregroundNotificationClick(Context context, Intent intent) {
                System.out.println("foregroundNotificationClick");
            }
        });
    }

    private static String getProcessName(Application app) {
        int pid = android.os.Process.myPid();
        String processName = "";
        try {
            ActivityManager mActivityManager = (ActivityManager) app.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = mActivityManager.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo appProcess : runningAppProcesses) {
                if (appProcess.pid == pid) {
                    processName = appProcess.processName;
                    break;
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return processName;
    }

}
