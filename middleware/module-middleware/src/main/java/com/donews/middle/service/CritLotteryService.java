package com.donews.middle.service;

import static com.donews.common.config.CritParameterConfig.CRIT_STATE;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.AppUtils;
import com.donews.base.utils.ToastUtil;
import com.donews.middle.ui.GoodLuckDoubleDialog;
import com.donews.utilslibrary.utils.SPUtils;

import java.util.Timer;
import java.util.TimerTask;

public class CritLotteryService extends Service {

    private Timer mTimer;
    private int mStartTime;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            ToastUtil.showShort(getApplicationContext(), msg.obj + "");
        }
    };

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
        boolean startCrit = intent.getBooleanExtra("start_crit", false);
        mStartTime = intent.getIntExtra("start_time", 0);
        if (startCrit) {
            //开始服务
            //判断暴击模式是否在运行
            int critState = SPUtils.getInformain(CRIT_STATE, 0);
            if (critState == 0) {
                startTask();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public void test(String valsue) {

        Message mes = new Message();
        mes.obj = valsue;
        handler.sendMessage(mes);

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
                        test("没在前台,倒计时中" + mStartTime);
                        mStartTime = mStartTime - 1;
                    }else{
                        test("可以开始暴击模式了");
                    }
                } else {
                    if (mStartTime <= 0 && foreground) {
                        mTimer.cancel();
                        mTimer = null;
                        //倒计时结束 可以打开开启暴击模式的弹框
                        Intent intent = new Intent(CritLotteryService.this, GoodLuckDoubleDialog.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        CritLotteryService.this.startActivity(intent);
                    } else {
                        test("在前台，体验体验时间不足" + mStartTime);
                    }
                }
            }
        }, 0, 1000);
    }
}