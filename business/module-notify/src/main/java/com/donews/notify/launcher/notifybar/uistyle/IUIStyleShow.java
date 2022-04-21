package com.donews.notify.launcher.notifybar.uistyle;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.donews.base.base.BaseApplication;
import com.donews.notify.R;
import com.donews.notify.launcher.NotifyInitProvider;
import com.donews.notify.launcher.configs.baens.NotifyBarDataConfig;
import com.donews.notify.launcher.notifybar.listeners.OnLoadUrlImageListener;
import com.donews.notify.launcher.notifybar.utils.NotifyBarJumpActionUtils;
import com.donews.notify.launcher.utils.NotifyLog;

/**
 * @author lcl
 * Date on 2022/3/10
 * Description:
 * 注意：如果需要使用{@link RemoteViews}请参考支持类型：
 * {@link RemoteViews} 支持的类型参考:https://blog.csdn.net/lxzmmd/article/details/111566908
 */
public interface IUIStyleShow {

    /**
     * 显示通知的方法
     *
     * @param context 上下文
     * @param data
     */
    void showNotify(Context context, NotifyBarDataConfig.NotifyBarUIDataConfig data);

    /**
     * 获取app的icon
     * @return
     */
    default int getAppIcon(){
        return R.drawable.ic_launcher_round;
    }

    /**
     * 获取跳转的Intent
     *
     * @param data UI数据
     * @return null:构建失败(默认前往首页)，Intent:去往的页面相关信息
     */
    default Intent getJmupIntent(NotifyBarDataConfig.NotifyBarUIDataConfig data) {
        Intent intent = NotifyBarJumpActionUtils.jumpBuild(BaseApplication.getInstance(), data);
        if (intent == null) {
            NotifyLog.logBar("构建跳转参数异常,开始构建默认启动intent~~");
            intent = BaseApplication.getInstance().getPackageManager().getLaunchIntentForPackage(
                    BaseApplication.getInstance().getPackageName());
        }
        return intent;
    }

    /**
     * 构建组件之前的参数设置,与{@link #buildCompatBuider}调用其中一个
     *
     * @param tag     标志。唯一的标识
     * @param builder
     */
    default void buildBuider(String tag, Notification.Builder builder) {
    }

    /**
     * 构建兼容的参数上设置,与{@link #buildBuider}调用其中一个
     *
     * @param tag     标志。唯一的标识
     * @param builder
     */
    default void buildCompatBuider(String tag, NotificationCompat.Builder builder) {
    }

    /**
     * 加载网络图片得到Bitmap对象
     *
     * @param imgUrl   图片地址
     * @param listener 结果监听
     */
    default void loadUrlImg(String imgUrl, OnLoadUrlImageListener listener) {
        if (imgUrl == null || imgUrl.isEmpty()) {
            listener.loadOk(null);
            return;
        }
        NotifyLog.logBar("开始加载需要的资源图标,地址=" + imgUrl);
        Glide.with(BaseApplication.getInstance())
                .asBitmap()
                .load(imgUrl)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        listener.loadOk(resource);
                        NotifyLog.logBar("资源加载成功:resource=" + resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        NotifyLog.logBar("资源开始加载。开始显示预加载的资源:onLoadCleared");
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        listener.loadError(errorDrawable);
                        NotifyLog.logBar("资源加载失败，显示错误的资源:onLoadFailed");
                    }
                });
    }
}
