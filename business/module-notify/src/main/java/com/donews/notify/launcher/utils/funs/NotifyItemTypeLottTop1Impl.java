package com.donews.notify.launcher.utils.funs;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.donews.base.utils.ToastUtil;
import com.donews.base.utils.glide.GlideUtils;
import com.donews.common.NotifyLuncherConfigManager;
import com.donews.notify.BuildConfig;
import com.donews.notify.R;
import com.donews.notify.launcher.NotifyActivity;
import com.donews.notify.launcher.NotifyAnimationView;
import com.donews.notify.launcher.NotifyInitProvider;
import com.donews.notify.launcher.configs.Notify2ConfigManager;
import com.donews.notify.launcher.configs.baens.Notify2DataConfigBean;
import com.donews.notify.launcher.utils.AbsNotifyInvokTask;
import com.donews.notify.launcher.utils.JumpActionUtils;
import com.donews.notify.launcher.utils.NotifyItemUtils;
import com.donews.notify.launcher.utils.fix.FixTagUtils;
import com.donews.notify.launcher.utils.fix.covert.ResConvertUtils;

/**
 * @author lcl
 * Date on 2022/1/5
 * Description:
 * ui模板1的处理逻辑
 */
public class NotifyItemTypeLottTop1Impl extends AbsNotifyInvokTask {

    @Override
    public boolean itemClick(NotifyAnimationView targetView, Notify2DataConfigBean.UiTemplat uiTemplat) {
        return JumpActionUtils.jump((Activity) targetView.getContext(), uiTemplat.getAction());
    }

    @Override
    public void bindTypeData(NotifyAnimationView targetView, Runnable lastBindTask) {
        if (targetView.getChildCount() <= 0 || targetView.getTag() == null) {
            targetView.setHideDuration(NotifyLuncherConfigManager.getInstance().getAppGlobalConfigBean().notifyShowTime);
            targetView.start();
            if (lastBindTask != null) {
                lastBindTask.run(); //调用外部更新视图
            }
            return; //没有添加视图
        }
        //获取视图
        //通知的图标
        ImageView icon = targetView.findViewById(R.id.notify_item_lott_icon);
        //通知的title
        TextView title = targetView.findViewById(R.id.notify_item_lott_title);
        //昵称(微信昵称)
        TextView name = targetView.findViewById(R.id.notify_item_lott_name);
        //描述信息
        TextView desc = targetView.findViewById(R.id.notify_item_lott_desc);
        //右侧商品icon
        ImageView goodIcon = targetView.findViewById(R.id.notify_item_lott_jp);

        Notify2DataConfigBean.UiTemplat uiTemplat = (Notify2DataConfigBean.UiTemplat) targetView.getTag();

        //设置值
        if(uiTemplat.getIconLeftTopMin() == null || uiTemplat.getIconLeftTopMin().isEmpty()){
            icon.setVisibility(View.GONE);
        }else{
            ResConvertUtils.buidIcon(icon,uiTemplat.getIconLeftTopMin());
        }
        title.setText(FixTagUtils.convertHtml(uiTemplat.getTitle()));
        name.setText(FixTagUtils.convertHtml(uiTemplat.getName()));
        desc.setText(FixTagUtils.convertHtml(uiTemplat.getDesc()));
        if(uiTemplat.getIconRight() == null || uiTemplat.getIconRight().isEmpty()){
            goodIcon.setVisibility(View.GONE);
        }else{
            ResConvertUtils.buidIcon(goodIcon,uiTemplat.getIconRight());
        }

        //回调视图任务
        if (lastBindTask != null) {
            lastBindTask.run(); //调用外部更新视图
        }
        //开始显示
        targetView.setHideDuration(NotifyLuncherConfigManager.getInstance().getAppGlobalConfigBean().notifyShowTime);
        targetView.start();
    }
}
