package com.donews.base.appdialog.window;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.donews.base.appdialog.permission.GrantActivity;
import com.donews.base.appdialog.permission.PermissionListener;

/**
 * @author by SnowDragon
 * Date on 2020/11/25
 * Description:
 */
class BaseDialog {
    private static final String TAG = "BaseDialog";

    private static final int DEFAULT_MARGIN = 30 * 2;
    private static final int DEFAULT_HEIGHT = 500;

    private boolean isShow = false;

    private View mView = null;
    private WindowManager mWindowManager = null;
    private Context mContext = null;


    protected boolean dismissOnBackPressed = true;
    protected boolean dismissOnTouchOutside = false;

    protected int width = 0;
    protected int height = 0;


    public BaseDialog() {

    }

    public BaseDialog(Context context) {
        mContext = context;
    }

    /**
     * 显示弹出框
     *
     * @param view
     */
    protected void showPopupWindow(View view) {
        //检查并申请权限
        GrantActivity.request(mContext, new PermissionListener() {
            @Override
            public void onSuccess() {
                mView = view;
                createDialog(view);
            }

            @Override
            public void onFail() {
                Log.v(TAG, "Permission application failed");
            }
        });


    }

    private void createDialog(View view) {

        if (isShow) {
            Log.i(TAG, "return cause already shown");
            return;
        }

        mWindowManager = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.type = getParamsType();


        /**
         * 可以点击取消，可以点击back键返回，但是弹窗出来之后，
         * 屏幕不能进行滑动操作,无法操作其他应用
         *WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
         *
         * 可以监听Veiw/view外部点击,返回键监听无效，屏幕可进行其他操作，如打开应用/滑动屏幕
         * WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
         *                 | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
         * */

        params.flags = WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        ;
        // 不设置这个弹出框的透明遮罩显示为黑色
        params.format = PixelFormat.TRANSLUCENT;

        if (width == 0 || height == 0) {
            DisplayMetrics metrics = new DisplayMetrics();
            mContext.getDisplay().getRealMetrics(metrics);

            //默认宽高
            width = metrics.widthPixels - DEFAULT_MARGIN;
            height = DEFAULT_HEIGHT;
        }

        params.width = width;
        params.height = height;
        //弹窗居中
        params.gravity = Gravity.CENTER;

        //返回键监听
        mView.setOnKeyListener(new OnBackKeyListener());

        //点击事件监听
        mView.setOnTouchListener(new OnViewTouchListener());

        isShow = true;
        mWindowManager.addView(mView, params);

    }

    private int getParamsType() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            return req();
        }
        if (MiUi.rom()) {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ?
                    req() : WindowManager.LayoutParams.TYPE_PHONE;
        }
        return WindowManager.LayoutParams.TYPE_TOAST;
    }

    public int req() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ?
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                : WindowManager.LayoutParams.TYPE_PHONE;
    }


    /**
     * 隐藏弹框
     */
    protected void hidePopupWindow() {
        if (isShow && null != mView && mView.getParent() != null) {
            mWindowManager.removeView(mView);
            isShow = false;
        }
    }


    /**
     * 点击监听
     */
    private class OnViewTouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            Log.i(TAG, "onTouch");
            int x = (int) event.getX();
            int y = (int) event.getY();

            Rect rect = new Rect();
            mView.getGlobalVisibleRect(rect);

            //点击弹窗
            if (rect.contains(x, y) && x != 0 && y != 0) {
                //hidePopupWindow();

            } else if (dismissOnTouchOutside) {
                //点击弹窗外部，并且允许点击外部弹窗消失
                hidePopupWindow();
            }

            return false;
        }
    }


    /**
     * 返回键监听
     */
    private class OnBackKeyListener implements View.OnKeyListener {

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {

            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (dismissOnBackPressed) {
                        hidePopupWindow();
                    }
                    return true;
                default:
                    return false;
            }

        }
    }


}
