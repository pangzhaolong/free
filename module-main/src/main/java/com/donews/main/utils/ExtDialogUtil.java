package com.donews.main.utils;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.donews.base.fragmentdialog.AbstractFragmentDialog;
import com.donews.main.dialog.ext.CritDownAppDialogFragment;
import com.donews.main.dialog.ext.CritWelfareDialogFragment;
import com.donews.main.dialog.ext.GoodLuckDoubleDialog;

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
     * @param surListener 快速解锁按钮的监听
     * @return
     */
    public static DialogFragment showCritDownAppDialog(
            FragmentActivity fa,
            CritDownAppDialogFragment.OnItemClickListener surListener
    ) {
        CritDownAppDialogFragment dialog = new CritDownAppDialogFragment();
        dialog.setSurListener(surListener);
        dialog.show(fa.getSupportFragmentManager());
        return dialog;
    }

    /**
     * 显示暴击翻倍的弹窗(好运翻倍)
     *
     * @param fa
     * @param count         次数
     * @param downTimeCount 延迟几秒钟
     * @return
     */
    public static DialogFragment showGooLuckDoubleDialog(
            FragmentActivity fa,
            int count,
            int downTimeCount,
            AbstractFragmentDialog.SureListener sureListener
    ) {
        GoodLuckDoubleDialog dialog = new GoodLuckDoubleDialog("" + count, downTimeCount);
        if(sureListener != null){
            dialog.setOnSureListener(sureListener);
        }
        dialog.show(fa.getSupportFragmentManager(), dialog.toString());
        return dialog;
    }

}
