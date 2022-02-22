package com.donews.keepalive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import com.donews.middle.abswitch.ABSwitch;
import com.donews.utilslibrary.utils.DateManager;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class ScreenStateReceiver extends BroadcastReceiver {
    private static boolean process = false;
    static String action = "";
    private ScreenHandler mScreenHandler = new ScreenHandler(this);
    private Context mContext;

    private long mIntervalsTime = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        if (Objects.equals(Intent.ACTION_SCREEN_ON, intent.getAction())) {
            if (ABSwitch.Ins().isScreenUnlockJumpSwitch()) {
                //判断每日次数
                if (DateManager.getInstance().timesLimit(DateManager.JUMP_TIMESTAMP, DateManager.JUMP_NUMBER, ABSwitch.Ins().getRevealNumber())) {
                    //开启了解锁后商场跳转
                    Message message = new Message();
                    mScreenHandler.removeMessages(0);
                    mScreenHandler.removeCallbacksAndMessages(null);
                    message.what = 1;
                    mScreenHandler.sendMessageDelayed(message, ABSwitch.Ins().getDelayedJump());
                }

            }
        }

        if (Objects.equals(Intent.ACTION_SCREEN_ON, intent.getAction()) || Objects.equals(Intent.ACTION_USER_PRESENT,
                intent.getAction())) {
            action = intent.getAction();
            DazzleActivity.destroyOnePixelActivity();
            process = false;
        } else if (Objects.equals(Intent.ACTION_SCREEN_OFF, intent.getAction())) {
            // 对应前台进程，通常不会被杀
            action = intent.getAction();
            if (!process) {
                DazzleActivity.showOnePixelActivity(context);
                process = true;
            }
        }
    }


    public void jumpUrl(Context context) {
        if (context == null) {
            return;
        }
        String rulValue = "";
        Uri uri = null;
        final List<String> urlList = ABSwitch.Ins().getApplicationShareJumpUrl();
        if (urlList != null) {
            Random rand = new Random();
            int id = rand.nextInt(urlList.size());
            rulValue = urlList.get(id);
            uri = Uri.parse(rulValue);// 商品地址
        }
        if (uri != null) {
            if ((System.currentTimeMillis() - mIntervalsTime) > ABSwitch.Ins().getIntervalsTime()) {
                mIntervalsTime = System.currentTimeMillis();
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    context.startActivity(intent);
                } catch (Exception e) {
                    if (rulValue != null && !rulValue.equals("")) {
                        String splitValueList[] = rulValue.split("//");
                        if (splitValueList != null && splitValueList.length >= 2) {
                            String url = "https://" + splitValueList[1];
                            uri = Uri.parse(url);// 商品地址
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            context.startActivity(intent);
                        }
                    }
                }
            }
        }
    }


    private static class ScreenHandler extends Handler {
        private WeakReference<ScreenStateReceiver> reference;   //

        ScreenHandler(ScreenStateReceiver context) {
            reference = new WeakReference(context);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (reference.get() != null) {
                        reference.get().jumpUrl(reference.get().mContext);
                    }
                    break;
            }
        }
    }

}
