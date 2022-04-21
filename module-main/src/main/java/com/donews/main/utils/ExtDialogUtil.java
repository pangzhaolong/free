package com.donews.main.utils;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.dn.integral.jdd.integral.ProxyIntegral;
import com.donews.base.fragmentdialog.AbstractFragmentDialog;
import com.donews.main.dialog.ext.CritDownAppDialogFragment;
import com.donews.main.dialog.ext.CritWelfareDialogFragment;
import com.donews.main.dialog.ext.FreeChargeAndWelfareDialog;
import com.donews.main.dialog.ext.LuckyDoubleOneDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lcl
 * Date on 2021/12/13
 * Description:
 */
public class ExtDialogUtil {

    /**
     * 暴击时刻新人专属、暴击时刻今日福利 dialog的显示
     *
     * @param fa
     * @param type        类型：0=新人特权，1今日福利
     * @param currJd      当前的进度
     * @param totalJd     总的进度
     * @param surListener 快速解锁按钮的监听
     * @return
     */
    public static DialogFragment showCritWelfareDialog(
            FragmentActivity fa,
            int type,
            int currJd,
            int totalJd,
            CritWelfareDialogFragment.OnSurListener surListener
    ) {
        CritWelfareDialogFragment dialog = new CritWelfareDialogFragment();
        dialog.currJd = currJd;
        dialog.totalJd = totalJd;
        if (type == 0 || type == 1) {
            dialog.type = type;
        }
        dialog.setSurListener(surListener);
        dialog.show(fa.getSupportFragmentManager());
        return dialog;
    }

    /**
     * 下载App应用列表的弹窗,
     *
     * @param fa
     * @param list 列表数据
     * @param surListener 快速解锁按钮的监听(null:表示让内部自己处理，F:业务层自己处理下载等逻辑)
     * @return
     */
    public static DialogFragment showCritDownAppDialog(
            FragmentActivity fa,
            List<ProxyIntegral> list,
            CritDownAppDialogFragment.OnItemClickListener surListener
    ) {
        List<CritDownAppDialogFragment.ItemData> datas = new ArrayList<>();
        for (ProxyIntegral proxyIntegral : list) {
            datas.add(new CritDownAppDialogFragment.ItemData(proxyIntegral));
        }
        CritDownAppDialogFragment dialog = new CritDownAppDialogFragment();
        dialog.setSurListener(surListener);
        dialog.setItemDatas(datas);
        dialog.show(fa.getSupportFragmentManager());
        return dialog;
    }


    /**
     * 幸运翻倍只差一步的弹窗
     *
     * @param fa
     * @param downTimeCount 延迟几秒钟
     * @return
     */
    public static DialogFragment showLuckyDoubleOneDialog(
            FragmentActivity fa,
            int downTimeCount,
            AbstractFragmentDialog.SureListener sureListener
    ) {
        LuckyDoubleOneDialog dialog = new LuckyDoubleOneDialog(downTimeCount);
        if (sureListener != null) {
            dialog.setOnSureListener(() -> {
                sureListener.onSure();
                dialog.disMissDialog();
            });
        }
        dialog.show(fa.getSupportFragmentManager(), dialog.toString());
        return dialog;
    }

    /**
     * 免费福利还未领取页面(全场商品免费领)
     *
     * @param fa
     * @param sureListener 按钮监听
     * @return
     */
    public static DialogFragment showFreeChargeAndWelfareDialogg(
            FragmentActivity fa,
            AbstractFragmentDialog.SureListener sureListener
    ) {
        FreeChargeAndWelfareDialog dialog = new FreeChargeAndWelfareDialog();
        if (sureListener != null) {
            dialog.setOnSureListener(() -> {
                sureListener.onSure();
                dialog.disMissDialog();
            });
        }
        dialog.show(fa.getSupportFragmentManager(), dialog.toString());
        return dialog;
    }

}
