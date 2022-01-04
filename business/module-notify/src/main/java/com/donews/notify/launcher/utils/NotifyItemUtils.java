package com.donews.notify.launcher.utils;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

/**
 * @author lcl
 * Date on 2022/1/4
 * Description:
 * 顶部桌面通知工具
 */
public class NotifyItemUtils {

    /**
     * 原始的图片类型(分类可配置大图和小图模式)
     */
    public static int TYPE_1 = 0;
    /**
     * 抽奖类型1(抽奖引导1：桌面顶部)
     */
    public static int TYPE_LOTT_1 = 1;

    /**
     * 需要望指定的item视图中加入当前的消息通知视图
     *
     * @param targetView
     */
    public static void addItemView(NotifyAnimationView targetView) {
        if (targetView.notifyType == TYPE_1) { //原始类型
            View v = LayoutInflater.from(targetView.getContext())
                    .inflate(R.layout.notify_item_type1, targetView, false);
            targetView.addView(v);
        } else if (targetView.notifyType == TYPE_LOTT_1) { //抽奖引导1通知
            View v = LayoutInflater.from(targetView.getContext())
                    .inflate(R.layout.notify_item_type_lott_1, targetView, false);
            targetView.addView(v);
        }
    }

    /**
     * 绑定数据
     *
     * @param targetView   目标视图
     * @param lastBindTask 之后的任务。给外部一个更改视图的时机。再默认更新之后，如果不需要特殊处理为:NULL 即可
     */
    public static void bindData(NotifyAnimationView targetView, Runnable lastBindTask) {
        if (targetView.notifyType == TYPE_1) { //原始类型的通知显示
            bindType1Data(targetView);
        } else if (targetView.notifyType == TYPE_LOTT_1) { //原始类型的通知显示
            bindTypeLott1Data(targetView);
        }
        if (lastBindTask != null) {
            lastBindTask.run(); //调用外部更新视图
        }
    }

    /**
     * 抽奖引导类型1的通知弹窗
     *
     * @param targetView
     */
    private static void bindTypeLott1Data(NotifyAnimationView targetView) {
        if (targetView.getChildCount() <= 0) {
            targetView.setHideDuration(NotifyLuncherConfigManager.getInstance().getAppGlobalConfigBean().notifyShowTime);
            targetView.start();
            return; //没有添加视图
        }
        //获取视图
        ImageView icon = targetView.findViewById(R.id.notify_item_lott_icon);
        TextView title = targetView.findViewById(R.id.notify_item_lott_title);
        TextView name = targetView.findViewById(R.id.notify_item_lott_name);
        TextView desc = targetView.findViewById(R.id.notify_item_lott_desc);

        //设置值
        title.setText("测试标题(提示)");

        //开始显示
        targetView.setHideDuration(NotifyLuncherConfigManager.getInstance().getAppGlobalConfigBean().notifyShowTime);
        targetView.start();
    }

    /**
     * 原始类型的通知处理(一张图片)
     *
     * @param targetView
     */
    private static void bindType1Data(NotifyAnimationView targetView) {
        if (targetView.getChildCount() <= 0) {
            targetView.setHideDuration(NotifyLuncherConfigManager.getInstance().getAppGlobalConfigBean().notifyShowTime);
            targetView.start();
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
                    targetView.setHideDuration(NotifyLuncherConfigManager.getInstance().getAppGlobalConfigBean().notifyShowTime);
                    targetView.start();
                }
            });
        } else {
            targetView.setHideDuration(NotifyLuncherConfigManager.getInstance().getAppGlobalConfigBean().notifyShowTime);
            targetView.start();
        }
    }

}
