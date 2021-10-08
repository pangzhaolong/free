package com.dn.sdk.dialog;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.dn.sdk.R;

/**
 * @author by SnowDragon
 * Date on 2020/11/20
 * Description:
 */
public abstract class BaseFragmentDialog extends DialogFragment {
    private boolean dismissOnBackPressed = true;
    private boolean dismissOnTouchOutside = true;

    private int animId = R.style.DialogOutInAnim;

    /**
     * 背景色是否变暗
     */
    private boolean backgroundDim = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, getThemeStyle());

    }

    public BaseFragmentDialog() {
    }


    public BaseFragmentDialog(boolean dismissOnBackPressed, boolean dismissOnTouchOutside) {
        this.dismissOnBackPressed = dismissOnBackPressed;
        this.dismissOnTouchOutside = dismissOnTouchOutside;
    }

    public BaseFragmentDialog(boolean dismissOnBackPressed, boolean dismissOnTouchOutside, int animStyleId) {
        this.animId = animStyleId;
        this.dismissOnBackPressed = dismissOnBackPressed;
        this.dismissOnTouchOutside = dismissOnTouchOutside;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // 弹出框外面是否可取消
        getDialog().setCanceledOnTouchOutside(dismissOnTouchOutside);

        // 返回true表示消费事件，拦截
        getDialog().setOnKeyListener((dialog, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                //不往上传递则关闭不了，默认是可以取消，即return false，
                if (dismissOnBackPressed) {
                    onBackPressDismissBefore();
                }
                return !dismissOnBackPressed;
            } else {
                return false;
            }
        });
        rootView = inflater.inflate(getLayoutId(), container);
        initView();
        return rootView;
    }

    private Context mContext;

    @Override
    public void onStart() {
        super.onStart();
        //获取手机屏幕的长和宽
        mContext = getContext();
        if (mContext != null) {
            WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            Point point = new Point();

            if (Build.VERSION.SDK_INT>Build.VERSION_CODES.M){
                mContext.getDisplay().getRealSize(point);
            }else {
                wm.getDefaultDisplay().getSize(point);
            }
            int width = point.x;

            //这个设置宽高的必须放在onstart方法里，不能放oncreateview里面
            Window dialogWindow = getDialog().getWindow();
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();

            // 布局文件居中
            dialogWindow.setGravity(Gravity.CENTER);
            lp.windowAnimations = animId;
            // dialogWindow.setLayout(lp.MATCH_PARENT, lp.WRAP_CONTENT);// 为了让对话框宽度铺满
            //设置弹窗的宽度，
            lp.width = width;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            //lp.alpha = 0.1f;
            dialogWindow.setAttributes(lp);


        }

    }

    public int getThemeStyle() {
        return backgroundDim ? R.style.DialogStyle : R.style.DialogNoBackground;
    }

    public BaseFragmentDialog setBackgroundDim(boolean backgroundDim) {
        this.backgroundDim = backgroundDim;
        return this;
    }

    public BaseFragmentDialog setDismissOnBackPressed(boolean dismissOnBackPressed) {
        this.dismissOnBackPressed = dismissOnBackPressed;
        return this;
    }

    public BaseFragmentDialog setDismissOnTouchOutside(boolean dismissOnTouchOutside) {
        this.dismissOnTouchOutside = dismissOnTouchOutside;
        return this;
    }

    /**
     * 布局文件id
     *
     * @return 返回布局文件Layout 的id
     */
    protected abstract int getLayoutId();

    /**
     * 初始化布局
     */
    protected abstract void initView();

    /**
     * 关闭弹窗
     */
    public static final int VERSION_CODES = 29;

    public void disMissDialog() {

        dismissAllowingStateLoss();


    }


    /**
     * 调用返回键，弹窗消失前调用
     */
    public void onBackPressDismissBefore() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    protected View rootView;

    protected <T extends View> T $(int id) {
        if (rootView == null) {
            return null;
        }
        return (T) rootView.findViewById(id);
    }

    protected DialogCloseListener closeListener;

    public BaseFragmentDialog setCloseListener(DialogCloseListener closeListener) {
        this.closeListener = closeListener;
        return this;
    }
}
