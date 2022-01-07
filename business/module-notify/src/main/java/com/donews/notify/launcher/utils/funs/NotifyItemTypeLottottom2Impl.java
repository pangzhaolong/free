package com.donews.notify.launcher.utils.funs;

import android.widget.ImageView;
import android.widget.TextView;

import com.donews.common.NotifyLuncherConfigManager;
import com.donews.notify.R;
import com.donews.notify.launcher.NotifyAnimationView;
import com.donews.notify.launcher.utils.AbsNotifyInvokTask;

/**
 * @author lcl
 * Date on 2022/1/5
 * Description:
 * 初始类型的处理逻辑。原类型的处理对象
 */
public class NotifyItemTypeLottottom2Impl extends AbsNotifyInvokTask {

    @Override
    public void bindTypeData(NotifyAnimationView targetView, Runnable lastBindTask) {
        if (targetView.getChildCount() <= 0) {
            targetView.setHideDuration(NotifyLuncherConfigManager.getInstance().getAppGlobalConfigBean().notifyShowTime);
            targetView.start();
            if (lastBindTask != null) {
                lastBindTask.run(); //调用外部更新视图
            }
            return; //没有添加视图
        }
        //获取视图
        ImageView close = targetView.findViewById(R.id.notify_item_lott2_close);
        //顶部图标(微信头像位置)
        ImageView icon = targetView.findViewById(R.id.notify_item_lott2_icon);
        //微信名称前的描述信息
        TextView wxNameFlg = targetView.findViewById(R.id.notify_item_lott2_name_d);
        //微信名称位置
        TextView wxName = targetView.findViewById(R.id.notify_item_lott2_name);
        //商品的名称描述信息
        TextView goodNameFlg = targetView.findViewById(R.id.notify_item_lott2_good_d);
        //商品的名称
        TextView goodName = targetView.findViewById(R.id.notify_item_lott2_good);
        //按钮
        TextView but = targetView.findViewById(R.id.notify_item_lott2_bug);

        //回调视图任务
        if (lastBindTask != null) {
            lastBindTask.run(); //调用外部更新视图
        }
        //开始显示
        targetView.setHideDuration(NotifyLuncherConfigManager.getInstance().getAppGlobalConfigBean().notifyShowTime);
        targetView.start();
    }
}
