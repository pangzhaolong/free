package com.donews.notify.launcher.utils.funs;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
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
import com.donews.notify.launcher.utils.AbsNotifyInvokTask;

/**
 * @author lcl
 * Date on 2022/1/5
 * Description:
 *  初始类型的处理逻辑。原类型的处理对象
 */
public class NotifyItemTypeLottTop1Impl extends AbsNotifyInvokTask {

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

        //设置值
        title.setText("测试标题(提示)");

        //回调视图任务
        if (lastBindTask != null) {
            lastBindTask.run(); //调用外部更新视图
        }
        //开始显示
        targetView.setHideDuration(NotifyLuncherConfigManager.getInstance().getAppGlobalConfigBean().notifyShowTime);
        targetView.start();
    }
}
