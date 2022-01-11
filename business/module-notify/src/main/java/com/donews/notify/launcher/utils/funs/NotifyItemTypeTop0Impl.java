package com.donews.notify.launcher.utils.funs;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.donews.common.NotifyLuncherConfigManager;
import com.donews.notify.R;
import com.donews.notify.launcher.NotifyActivity;
import com.donews.notify.launcher.NotifyAnimationView;
import com.donews.notify.launcher.NotifyInitProvider;
import com.donews.notify.launcher.utils.AbsNotifyInvokTask;

/**
 * @author lcl
 * Date on 2022/1/5
 * Description:
 *  ui模板0的处理逻辑
 */
public class NotifyItemTypeTop0Impl extends AbsNotifyInvokTask {

    @Override
    public void bindTypeData(NotifyAnimationView targetView,Runnable lastBindTask) {
        if (targetView.getChildCount() <= 0) {
            targetView.setHideDuration(NotifyLuncherConfigManager.getInstance().getAppGlobalConfigBean().notifyShowTime);
            targetView.start();
            if (lastBindTask != null) {
                lastBindTask.run(); //调用外部更新视图
            }
            return; //没有添加视图
        }
        ImageView mNotifyIv = targetView.findViewById(R.id.img_notify);
        int notifyShowSizeType = NotifyLuncherConfigManager.getInstance().getAppGlobalConfigBean().notifyShowSizeType;
        if (notifyShowSizeType == 0) {
            mNotifyIv.setImageResource(R.drawable.notifycation_notify_content);
        } else {
            mNotifyIv.setImageResource(R.drawable.launcher_notify_big);
        }
        String url = NotifyLuncherConfigManager.getInstance().getAppGlobalConfigBean().notifyLauncherImg;
        Log.d(NotifyActivity.TAG, "start url ,url = " + url);
        if (!TextUtils.isEmpty(url) && url.startsWith("http")) {
            Glide.with(targetView.getContext()).asBitmap().load(url).into(new CustomTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    mNotifyIv.setImageBitmap(resource);
                    if (lastBindTask != null) {
                        lastBindTask.run(); //调用外部更新视图
                    }
                    targetView.setHideDuration(NotifyLuncherConfigManager.getInstance().getAppGlobalConfigBean().notifyShowTime);
                    Log.d(NotifyActivity.TAG, "img url loaded , url = " + url);
                    targetView.start();
                }

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {
                    Log.d(NotifyActivity.TAG, "img url onLoadCleared , url =" + url);
                }

                @Override
                public void onLoadFailed(@Nullable Drawable errorDrawable) {
                    super.onLoadFailed(errorDrawable);
                    Log.d(NotifyInitProvider.TAG, "tryLoadNewImg onLoadFailed , url = " + url);
                    if (lastBindTask != null) {
                        lastBindTask.run(); //调用外部更新视图
                    }
                    targetView.setHideDuration(NotifyLuncherConfigManager.getInstance().getAppGlobalConfigBean().notifyShowTime);
                    targetView.start();
                }
            });
        } else {
            if (lastBindTask != null) {
                lastBindTask.run(); //调用外部更新视图
            }
            targetView.setHideDuration(NotifyLuncherConfigManager.getInstance().getAppGlobalConfigBean().notifyShowTime);
            targetView.start();
        }
    }
}
