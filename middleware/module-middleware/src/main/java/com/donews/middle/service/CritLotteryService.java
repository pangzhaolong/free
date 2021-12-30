package com.donews.middle.service;

import static com.donews.common.config.CritParameterConfig.CRIT_STATE;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.blankj.utilcode.util.AppUtils;
import com.donews.base.BuildConfig;
import com.donews.base.base.AppManager;
import com.donews.base.utils.ToastUtil;
import com.donews.middle.bean.DownloadStateDean;
import com.donews.middle.ui.GoodLuckDoubleDialog;
import com.donews.network.EasyHttp;
import com.donews.network.cache.model.CacheMode;
import com.donews.network.callback.SimpleCallBack;
import com.donews.network.exception.ApiException;
import com.donews.utilslibrary.utils.SPUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class CritLotteryService extends Service {

    private static String INTEGRAL_BASE = BuildConfig.API_INTEGRAL_URL;
    public static String INTEGRAL_REWARD = INTEGRAL_BASE + "v1/wall-task-report";
    private Timer mTimer;
    //暴击体验时长
    private int mStartTime;
    private int mStartSumTime;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            ToastUtil.showShort(getApplicationContext(), msg.obj + "");
        }
    };
    private boolean ifTimerRun = false;
    DownloadStateDean mDownloadStateDean;
    //访问服务器请求参数
    private String wall_request_id;
    private String source_request_id;
    private String source_ad_type;
    private String package_name;
    private String app_name;
    private String icon;
    private String deep_link;
    private String source_platform;
    private String desc;
    private String task_type;
    private String price;
    private String apk_url;
    private String wall_event;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }


    private void startLotteryForeground() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("100365", "测试奖多多", NotificationManager.IMPORTANCE_LOW);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);
            Notification notification = new Notification.Builder(CritLotteryService.this, "100365").build();
            startForeground(100365, notification);
        }


    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startLotteryForeground();
        if (intent != null) {
            boolean startCrit = intent.getBooleanExtra("start_crit", false);
            mStartTime = intent.getIntExtra("start_time", 0);
            mStartSumTime = mStartTime;
            wall_request_id = intent.getStringExtra("wall_request_id");
            source_request_id = intent.getStringExtra("source_request_id");
            source_ad_type = intent.getStringExtra("source_ad_type");
            package_name = intent.getStringExtra("package_name");
            app_name = intent.getStringExtra("app_name");
            icon = intent.getStringExtra("icon");
            deep_link = intent.getStringExtra("deep_link");
            source_platform = intent.getStringExtra("source_platform");
            desc = intent.getStringExtra("desc");
            task_type = intent.getStringExtra("task_type");
            price = intent.getStringExtra("price");
            apk_url = intent.getStringExtra("apk_url");
            wall_event = intent.getStringExtra("wall_event");
            if (startCrit) {
                //开始服务
                //判断暴击模式是否在运行
                int critState = SPUtils.getInformain(CRIT_STATE, 0);
                if (critState == 0) {
                    startTask();
                }
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public void test(String valsue) {

        Message mes = new Message();
        mes.obj = valsue;
        handler.sendMessage(mes);

    }


    public void getDownloadStatus() {
        if (wall_request_id != null) {
            Map<String, String> params = new HashMap<>();
            params.put("wall_request_id", wall_request_id);
            params.put("source_request_id", source_request_id);
            params.put("source_ad_type", source_ad_type);
            params.put("package_name", package_name);
            params.put("app_name", app_name);
            params.put("icon", icon);
            params.put("deep_link", deep_link);
            params.put("source_platform", source_platform);
            params.put("desc", desc);
            params.put("task_type", task_type);
            params.put("price", price);
            params.put("apk_url", apk_url);
            params.put("wall_event", wall_event);
            JSONObject json = new JSONObject(params);
            unDisposable();
            addDisposable(EasyHttp.post(INTEGRAL_REWARD)
                    .cacheMode(CacheMode.NO_CACHE)
                    .isShowToast(false)
                    .upJson(json.toString())
                    .execute(new SimpleCallBack<DownloadStateDean>() {
                        @Override
                        public void onError(ApiException e) {
                            mDownloadStateDean = null;
                            if (!ifTimerRun) {
                                ToastUtil.showShort(getApplicationContext(), "任务失败，请从本页面下载并打开对应APP");
                            }
                        }

                        @Override
                        public void onSuccess(DownloadStateDean stateDean) {
                            if (stateDean != null) {
                                mDownloadStateDean = stateDean;
                                if (!ifTimerRun) {
                                    if (mDownloadStateDean.getHandout()) {
                                        ToastUtil.showShort(getApplicationContext(), "任务激活成功");
                                        jump();
                                    } else {
                                        ToastUtil.showShort(getApplicationContext(), "任务激活失败");
                                    }

                                }
                            }
                        }
                    }));
        }

    }


    private void startTask() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;


        }
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                //只有应用不在前台才会继续倒计时
                boolean foreground = AppUtils.isAppForeground();
                test("开始倒计时");
                if (!foreground) {
                    if (mStartTime > 0) {
                        ifTimerRun = true;
                        test("没在前台,倒计时中" + mStartTime);
                        mStartTime = mStartTime - 1;

                        if (mStartTime == (mStartSumTime / 2)) {
                            //请求服务器处理结果
                            Log.d("startTask", "请求服务器获取结果");
                            getDownloadStatus();
                        }
                    } else {
                        test("可以开始暴击模式了");
                    }
                } else {
                    if (mStartTime <= 0 && foreground) {
                        Activity activity = AppManager.getInstance().getTopActivity();
                        if (activity != null && activity instanceof FragmentActivity) {
                            if (((FragmentActivity) activity).getSupportFragmentManager() != null) {
                                Fragment hotStartDialogFragment = ((FragmentActivity) activity).getSupportFragmentManager().findFragmentByTag("HotStartDialog");
                                if (hotStartDialogFragment != null) {
                                    test("齐平页面等待");
                                } else {
                                    if ((activity.getClass().getSimpleName().equals("MainActivity") || activity.getClass().getSimpleName().equals("LotteryActivity"))) {
                                        cancelTimer();
                                    } else {
                                        test("等待首页，或者抽奖页显示后，弹起暴击弹框" + mStartTime);
                                    }
                                }
                            }
                        } else {
                            cancelTimer();
                        }

                    } else {
                        test("在前台，体验体验时间不足" + mStartTime);
                    }
                }
            }
        }, 0, 1000);
    }

    private void cancelTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        ifTimerRun = false;
        Log.d("startTask", "任务完成");
        if (mDownloadStateDean == null || !mDownloadStateDean.getHandout()) {
            //请求服务器处理结果
            mStartTime = 0;
            Log.d("startTask", "上次没有请求到或者请求失败");
            getDownloadStatus();
        } else {
            Log.d("startTask", "上次请求成功");
            jump();
        }
    }

    private void jump() {
        //倒计时结束 可以打开开启暴击模式的弹框
        Intent intent = new Intent(CritLotteryService.this, GoodLuckDoubleDialog.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CritLotteryService.this.startActivity(intent);
    }


    private CompositeDisposable mCompositeDisposable;

    public void addDisposable(Disposable disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);
    }

    public void unDisposable() {
        if (mCompositeDisposable != null && mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.clear();
        }
    }

}