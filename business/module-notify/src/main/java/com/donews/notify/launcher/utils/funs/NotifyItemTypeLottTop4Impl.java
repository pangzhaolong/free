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

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author lcl
 * Date on 2022/1/5
 * Description:
 * ui模板4的处理逻辑
 */
public class NotifyItemTypeLottTop4Impl extends AbsNotifyInvokTask {

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
        TextView title = targetView.findViewById(R.id.notify_item_title);
        TextView name = targetView.findViewById(R.id.notify_item_name);
        TextView desc = targetView.findViewById(R.id.notify_item_desc);
        TextView time = targetView.findViewById(R.id.notify_item_time);
        //右侧的icon
        ImageView iconRight = targetView.findViewById(R.id.notify_item_icon_right);


        Notify2DataConfigBean.UiTemplat uiTemplat = (Notify2DataConfigBean.UiTemplat) targetView.getTag();

        title.setText(Html.fromHtml(uiTemplat.getTitle()));
        name.setText(Html.fromHtml(uiTemplat.getName()));
        desc.setText(Html.fromHtml(uiTemplat.getDesc()));
        SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        time.setText(sf.format(new Date(System.currentTimeMillis())));
        //设置值
        if (uiTemplat.getIconRight() != null && !uiTemplat.getIconRight().isEmpty()) {
            GlideUtils.loadImageView(targetView.getContext(), uiTemplat.getIconRight(), iconRight);
        } else {
            iconRight.setVisibility(View.GONE);
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
