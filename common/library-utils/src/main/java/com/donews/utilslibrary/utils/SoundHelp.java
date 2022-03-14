package com.donews.utilslibrary.utils;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.IOException;

/**
 * @Author: honeylife
 * @CreateDate: 2020/5/16 14:42
 * @Description: 声音的播放类
 */
public class SoundHelp {

    private static SoundHelp instance;
    // true 开启了声音播放，false 关闭了
    private boolean isSound = true;
    private MediaPlayer mediaPlayer;
    private AssetManager assetManager;
    private AssetFileDescriptor fileDescriptor = null;
    private Context context;

    private SoundHelp() {

//        init();
    }

    public static SoundHelp newInstance() {

        if (instance == null) {
            instance = new SoundHelp();
        }
        return instance;
    }

    public void init(Context context) {
        this.context = context;
        try {
            mediaPlayer = new MediaPlayer();
            assetManager = context.getResources().getAssets();
            fileDescriptor = assetManager.openFd("sound.mp3");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isSound() {
        return isSound;
    }

    public void setSound(boolean sound) {
        isSound = sound;
    }

    public String onStart() {
        if (assetManager == null)
            return "请初始化操作！";
        try {
            if (isSound() && onAndroidSound()) {
                if (mediaPlayer == null) {
                    mediaPlayer = new MediaPlayer();
                }
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;
                    mediaPlayer = new MediaPlayer();
                }
                mediaPlayer.reset();
                mediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(), fileDescriptor.getStartOffset());
                mediaPlayer.prepare();
                mediaPlayer.start();
//                mediaPlayer.prepareAsync();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }


    private boolean onAndroidSound() {
        boolean open = false;
        try {
            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            if (audioManager == null) return open;
            final int ringerMode = audioManager.getRingerMode();
            switch (ringerMode) {
                case AudioManager.RINGER_MODE_NORMAL:
                case AudioManager.RINGER_MODE_VIBRATE: // 震动
                    open = true;
                    break;
                case AudioManager.RINGER_MODE_SILENT: // 静音
                    open = false;
                    break;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return open;
    }

    public void onRelease() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
