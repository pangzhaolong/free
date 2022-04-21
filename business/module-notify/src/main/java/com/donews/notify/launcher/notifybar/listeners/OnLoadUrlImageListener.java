package com.donews.notify.launcher.notifybar.listeners;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.donews.notify.launcher.utils.NotifyLog;

/**
 * @author lcl
 * Date on 2022/3/15
 * Description:
 * 网络图片加载的回调监听
 */
public interface OnLoadUrlImageListener {
    /**
     * 加载成功
     * @param res 成功之后的Bitmap
     */
    void loadOk(Bitmap res);

    /**
     * 加载失败
     * @param errDrawble glide配置的默认错误图标资源
     */
    default void loadError(Drawable errDrawble){
        NotifyLog.logBar("加载出错了，回调方法未实现。默认日志~~");
    }
}
