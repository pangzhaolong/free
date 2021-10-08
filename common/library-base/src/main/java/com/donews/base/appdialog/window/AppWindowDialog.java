package com.donews.base.appdialog.window;

import android.content.Context;
import android.view.View;

/**
 * @author by SnowDragon
 * Date on 2020/11/25
 * Description:
 */
public class AppWindowDialog extends BaseDialog {

    private AppWindowDialog(Context context) {
        super(context);
    }


    public static AppWindowDialog build(Context context) {
        return new AppWindowDialog(context);
    }

    /**
     * 是否允许点击返回键弹窗消失
     *
     * @param dismissOnBackPressed true：点击返回键，弹窗消失
     * @return
     */
    public AppWindowDialog setDismissOnBackPressed(boolean dismissOnBackPressed) {
        this.dismissOnBackPressed = dismissOnTouchOutside;
        return this;
    }

    /**
     * 是否允许点击弹窗外区域，关闭弹窗
     *
     * @param dismissOnTouchOutside true:表示谈窗外点击关闭
     * @return
     */
    public AppWindowDialog setDismissOnTouchOutside(boolean dismissOnTouchOutside) {
        this.dismissOnTouchOutside = dismissOnTouchOutside;
        return this;
    }

    /**
     * 设置弹窗宽高
     *
     * @param widthPx  弹窗宽度 单位：px
     * @param heightPx 弹窗高度 单位：px
     * @return
     */
    public AppWindowDialog setSize(int widthPx, int heightPx) {
        this.width = widthPx;
        this.height = heightPx;
        return this;
    }

    /**
     * 展示弹窗
     *
     * @param view 布局
     */
    public void showDialog(View view) {
        showPopupWindow(view);
    }

    /**
     * 关闭弹窗
     */
    public void dismiss() {
        hidePopupWindow();
    }


}
