package com.donews.notify.launcher.utils.funs;

import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.donews.base.utils.glide.GlideUtils;
import com.donews.common.NotifyLuncherConfigManager;
import com.donews.notify.R;
import com.donews.notify.launcher.NotifyAnimationView;
import com.donews.notify.launcher.configs.baens.Notify2DataConfigBean;
import com.donews.notify.launcher.utils.AbsNotifyInvokTask;

/**
 * @author lcl
 * Date on 2022/1/5
 * Description:
 * ui模板3的处理逻辑
 */
public class NotifyItemTypeLottTop3Impl extends AbsNotifyInvokTask {

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
        //右侧的icon
        ImageView iconRight = targetView.findViewById(R.id.notify_item_icon_right);


        Notify2DataConfigBean.UiTemplat uiTemplat = (Notify2DataConfigBean.UiTemplat) targetView.getTag();

        //设置值
        if (uiTemplat.getIconLeft() != null && !uiTemplat.getIconLeft().isEmpty()) {
            GlideUtils.loadImageView(targetView.getContext(), uiTemplat.getIconLeft(), iconLeft);
        }
        if (uiTemplat.getIconRight() != null && !uiTemplat.getIconRight().isEmpty()) {
            GlideUtils.loadImageView(targetView.getContext(), uiTemplat.getIconRight(), iconRight);
        } else {
            iconRight.setVisibility(View.GONE);
        }
        title.setText(Html.fromHtml(uiTemplat.getTitle()));
        desc.setText(Html.fromHtml(uiTemplat.getDesc()));

        //回调视图任务
        if (lastBindTask != null) {
            lastBindTask.run(); //调用外部更新视图
        }
        //开始显示
        targetView.setHideDuration(NotifyLuncherConfigManager.getInstance().getAppGlobalConfigBean().notifyShowTime);
        targetView.start();
    }
}
