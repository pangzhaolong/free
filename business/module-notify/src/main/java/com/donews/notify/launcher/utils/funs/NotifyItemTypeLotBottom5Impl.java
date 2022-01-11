package com.donews.notify.launcher.utils.funs;

import static com.donews.utilslibrary.utils.KeySharePreferences.NOTIFY_RANDOM_RED_AMOUNT;
import static com.donews.utilslibrary.utils.KeySharePreferences.TIME_SERVICE;

import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.donews.base.utils.glide.GlideUtils;
import com.donews.common.NotifyLuncherConfigManager;
import com.donews.notify.R;
import com.donews.notify.launcher.NotifyAnimationView;
import com.donews.notify.launcher.configs.Notify2ConfigManager;
import com.donews.notify.launcher.configs.baens.Notify2DataConfigBean;
import com.donews.notify.launcher.utils.AbsNotifyInvokTask;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.List;
import java.util.Random;

/**
 * @author lcl
 * Date on 2022/1/5
 * Description:
 * 初始类型的处理逻辑。原类型的处理对象
 */
public class NotifyItemTypeLotBottom5Impl extends AbsNotifyInvokTask {

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

        //金额的容器
        LinearLayout jeLL = targetView.findViewById(R.id.notify_item_lott2_xj_ll);
        //获取视图
        ImageView close = targetView.findViewById(R.id.notify_item_lott2_close);
        //顶部图标
        ImageView icon = targetView.findViewById(R.id.notify_item_lott2_icon);
        //金额
        TextView numner = targetView.findViewById(R.id.notify_item_lott2_num);
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

        Notify2DataConfigBean.UiTemplat uiTemplat = (Notify2DataConfigBean.UiTemplat) targetView.getTag();
        wxNameFlg.setText(Html.fromHtml(uiTemplat.getTitlePrefix()));
        wxName.setText(Html.fromHtml(uiTemplat.getTitle()));
        goodNameFlg.setText(Html.fromHtml(uiTemplat.getDescPrefix()));
        goodName.setText(Html.fromHtml(uiTemplat.getDesc()));
        but.setText(Html.fromHtml(uiTemplat.getButtonLeft()));
        if (uiTemplat.getType() == 0) {
            //金额
            jeLL.setVisibility(View.VISIBLE);
            icon.setVisibility(View.GONE);
            float num = getRandom(
                    Notify2ConfigManager.Ins().getNotifyConfigBean().redPackageMinAmount,
                    Notify2ConfigManager.Ins().getNotifyConfigBean().redPackageMaxAmount
            );
            numner.setText("" + num);
            //将金额保存到文件。做到首页同步
            com.donews.utilslibrary.utils.SPUtils.setInformain(NOTIFY_RANDOM_RED_AMOUNT, num);
        } else {
            //图标
            jeLL.setVisibility(View.GONE);
            icon.setVisibility(View.VISIBLE);
            List<Notify2DataConfigBean.UiTemplatImageItem> ls = uiTemplat.getGoodImages();
            if (ls == null || ls.isEmpty()) {
                icon.setImageResource(R.mipmap.ic_launcher);
            } else {
                Notify2DataConfigBean.UiTemplatImageItem item = ls.get(new Random().nextInt(ls.size()));
                GlideUtils.loadImageView(targetView.getContext(), item.goodIcon, icon);
            }
        }

        //回调视图任务
        if (lastBindTask != null) {
            lastBindTask.run(); //调用外部更新视图
        }
        //开始显示
        targetView.setHideDuration(NotifyLuncherConfigManager.getInstance().getAppGlobalConfigBean().notifyShowTime);
        targetView.start();
    }

    /**
     * 获取一个范围内随机数
     *
     * @param start
     * @param end
     * @return
     */
    public static float getRandom(float start, float end) {
        double d = (Math.random() * (end - start) + start);
        try {
            NumberFormat nf = NumberFormat.getNumberInstance();
            // 保留两位小数
            nf.setMaximumFractionDigits(2);
            // 如果不需要四舍五入，可以使用RoundingMode.DOWN
            nf.setRoundingMode(RoundingMode.UP);
            return Float.parseFloat(nf.format(d));
        } catch (Exception e) {
            return ((int) d * 100) / 100F;
        }
    }

}
