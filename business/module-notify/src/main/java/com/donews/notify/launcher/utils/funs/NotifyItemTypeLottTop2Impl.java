package com.donews.notify.launcher.utils.funs;

import android.app.Activity;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.donews.base.utils.ToastUtil;
import com.donews.base.utils.glide.GlideUtils;
import com.donews.common.NotifyLuncherConfigManager;
import com.donews.notify.BuildConfig;
import com.donews.notify.R;
import com.donews.notify.launcher.NotifyAnimationView;
import com.donews.notify.launcher.configs.baens.Notify2DataConfigBean;
import com.donews.notify.launcher.utils.AbsNotifyInvokTask;
import com.donews.notify.launcher.utils.JumpActionUtils;
import com.donews.notify.launcher.utils.fix.FixTagUtils;
import com.donews.notify.launcher.utils.fix.covert.ResConvertUtils;

/**
 * @author lcl
 * Date on 2022/1/5
 * Description:
 * ui模板2的处理逻辑
 */
public class NotifyItemTypeLottTop2Impl extends AbsNotifyInvokTask {

    @Override
    public boolean itemClick(NotifyAnimationView targetView, Notify2DataConfigBean.UiTemplat uiTemplat) {
        return JumpActionUtils.jump((Activity) targetView.getContext(), uiTemplat);
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
        ImageView iconLeft = targetView.findViewById(R.id.notify_item_icon);
        //通知的title
        TextView title = targetView.findViewById(R.id.notify_item_title);
        //描述信息
        TextView desc = targetView.findViewById(R.id.notify_item_desc);
        //左侧按钮
        TextView leftBut = targetView.findViewById(R.id.notify_item_but_left);
        //右侧侧按钮
        TextView rightBut = targetView.findViewById(R.id.notify_item_but_right);

        Notify2DataConfigBean.UiTemplat uiTemplat = (Notify2DataConfigBean.UiTemplat) targetView.getTag();

        //设置值
        if (uiTemplat.getIconLeft() != null && !uiTemplat.getIconLeft().isEmpty()) {
            ResConvertUtils.buidIcon(iconLeft,uiTemplat.getIconLeft());
        }
        title.setText(FixTagUtils.convertHtml(uiTemplat.getTitle()));
        desc.setText(FixTagUtils.convertHtml(uiTemplat.getDesc()));
        if (uiTemplat.getButtonLeft() == null || uiTemplat.getButtonLeft().isEmpty()) {
            leftBut.setVisibility(View.INVISIBLE);
        } else {
            leftBut.setText(FixTagUtils.convertHtml(uiTemplat.getButtonLeft()));
        }
        if (uiTemplat.getButtonRight() == null || uiTemplat.getButtonRight().isEmpty()) {
            rightBut.setVisibility(View.INVISIBLE);
        } else {
            rightBut.setText(FixTagUtils.convertHtml(uiTemplat.getButtonRight()));
        }
        if(leftBut.getVisibility() != View.VISIBLE &&
                rightBut.getVisibility() != View.VISIBLE){
            leftBut.setVisibility(View.GONE);
            rightBut.setVisibility(View.GONE);
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
