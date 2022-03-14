package com.donews.keepalive;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.keepalive.daemon.core.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class DazzleService extends Service {
    public static final String TAG = "keepalive-global";

    private static final long DELAY = DazzleReal.debug ? 3 * 1000L : 30 * 60 * 1000L;

    private final ServiceHandler handler = new ServiceHandler(this);

    private static MediaPlayer mediaPlayer = null;

    private static void release() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private static String play(Context context) {
        if (mediaPlayer == null) {
            if (DazzleReal.debug) {
                mediaPlayer = MediaPlayer.create(context, R.raw.voice);
                mediaPlayer.setVolume(0.1f, 0.1f);
            } else {
                mediaPlayer = MediaPlayer.create(context, R.raw.novioce);
                mediaPlayer.setVolume(0f, 0f);
            }
            mediaPlayer.setLooping(true);
        }
        try {
            if (!mediaPlayer.isPlaying()) {
                mediaPlayer.start();
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return "1";
    }

    private static void stop() {
        try {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.prepare();
                mediaPlayer.seekTo(0);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "DazzleService onCreate...");
        CommonNotification.startForeground(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            //播放无声音乐
            playMusic();
            //启用前台服务，提升优先级
            CommonNotification.startForeground(this);
            DazzleReal.callback.onWorking();
        } catch (Exception e) {
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DazzleReal.unregReceiver(this);
        if (DazzleReal.callback != null) {
            DazzleReal.callback.onStop();
        }
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    void playMusic() {
        String playType = play(this);
        List<Object> list = new ArrayList<>();
        list.add(playType);
        list.add(SystemClock.elapsedRealtime());

        handler.removeCallbacksAndMessages(null);
        handler.sendMessageDelayed(handler.obtainMessage(0, list), DELAY);
    }


    private static class ServiceHandler extends Handler {
        private final Context context;

        @SuppressWarnings("deprecation")
        public ServiceHandler(Context context) {
            this.context = context;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            List<Object> list = (List<Object>) msg.obj;

            if (list != null && list.size() > 1) {
                long start = Long.parseLong(list.get(1).toString());
                String originType = list.get(0).toString();

                WeakReference<DazzleActivity> sKeepLiveActivity = DazzleActivity.sKeepLiveActivity;
                Activity activity = sKeepLiveActivity != null ? sKeepLiveActivity.get() : null;
                boolean hasOne = activity != null && !activity.isDestroyed();

                StringBuilder builder = new StringBuilder(originType);
                if (hasOne) {
                    builder.append(",").append("2");
                }
                String type = builder.toString();

                Intent intent = new Intent();
                intent.putExtra("type", type);
                intent.putExtra("time", start);

                if (Build.VERSION.SDK_INT >= 26) {
                    JobHeartService.enqueueWork(context, intent);
                } else {
                    intent.setComponent(new ComponentName(context, HeartService.class));
                    context.startService(intent);
                }
                sendMessageDelayed(obtainMessage(0, list), DELAY);
            }
        }
    }
}
