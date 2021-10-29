package com.donews.common.base.popwindow;

import android.content.Context;
import android.view.View;

import androidx.fragment.app.FragmentActivity;

import com.donews.common.base.MvvmBaseLiveDataActivity;


/**
 * @Author: honeylife
 * @CreateDate: 2020/4/15 16:48
 * @Description:
 */
public abstract class BasePopupWindow {
    protected View rootView;
    protected FragmentActivity baseActivity;
    protected boolean isAnimating;

    public BasePopupWindow(FragmentActivity baseActivity) {
        this.baseActivity = baseActivity;
    }

    public final FragmentActivity getBaseActivity() {
        return baseActivity;
    }


    public boolean isShown() {
        return rootView != null && rootView.isShown();
    }


    public void show() {
        if (isAnimating) {
            return;
        }
        if (rootView == null) {
            initLayout(getBaseActivity());
            ((MvvmBaseLiveDataActivity) getBaseActivity()).getRootView().addView(rootView);
            ((MvvmBaseLiveDataActivity) getBaseActivity()).addPDPopupWindow(this);
        }
        rootView.bringToFront();
        showAnimation();
    }

    protected final void setIsAnimating(boolean isAnimating) {
        this.isAnimating = isAnimating;
    }

    protected final boolean isAnimating() {
        return isAnimating;
    }

    protected abstract void showAnimation();

    protected abstract void hideAnimation();

    public abstract void onResume();


    public void hide() {
        if (isAnimating) {
            return;
        }
        if (rootView != null) {
            hideAnimation();
        }
    }

    public boolean backPressDismiss() {
        return true;
    }

    protected abstract void initLayout(Context context);
}
