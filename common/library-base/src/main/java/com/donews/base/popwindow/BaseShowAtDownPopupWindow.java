package com.donews.base.popwindow;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import androidx.fragment.app.FragmentActivity;

import com.donews.base.R;
import com.donews.base.activity.MvvmBaseLiveDataActivity;
import com.donews.base.base.BaseApplication;
import com.donews.utilslibrary.utils.DeviceUtils;


public abstract class BaseShowAtDownPopupWindow extends BasePopupWindow {

    private View bg;
    private View content;
    private View showAtView;
    private int offsetDp;

    public BaseShowAtDownPopupWindow(FragmentActivity baseActivity) {
        super(baseActivity);
    }

    @Override
    protected void showAnimation() {
        int[] rootViewLocation = new int[2];
        ((MvvmBaseLiveDataActivity) getBaseActivity()).getRootView().getLocationInWindow(rootViewLocation);

        int[] showAtViewLocation = new int[2];
        if (showAtView != null) {
            showAtView.getLocationInWindow(showAtViewLocation);
        } else {
            Log.e("TAG", "BaseShowAtDownPopupWindow must call showAtView(View view)");
            return;
        }

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) content.getLayoutParams();
        layoutParams.topMargin = showAtViewLocation[1] - rootViewLocation[1] + showAtView.getMeasuredHeight() + DeviceUtils.dip2px(offsetDp);
        content.setLayoutParams(layoutParams);

        rootView.setVisibility(View.VISIBLE);
        Animation alphaAnimation = AnimationUtils.loadAnimation(BaseApplication.getInstance(), R.anim.base_popup_bg_fadein);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                setIsAnimating(true);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setIsAnimating(false);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        content.startAnimation(alphaAnimation);
        bg.startAnimation(alphaAnimation);
    }

    @Override
    protected void hideAnimation() {
        Animation alphaAnimation = AnimationUtils.loadAnimation(BaseApplication.getInstance(), R.anim.base_popup_bg_fadeout);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                setIsAnimating(true);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                rootView.setVisibility(View.GONE);
                setIsAnimating(false);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        content.startAnimation(alphaAnimation);
        bg.startAnimation(alphaAnimation);
    }

    protected abstract int layoutId();

    protected abstract void initContentView();

    public final void showAtView(View view) {
        showAtView(view, 0);
    }

    public final void showAtView(View view, int offsetDp) {
        this.showAtView = view;
        this.offsetDp = offsetDp;
        show();
    }

    @Override
    protected final void initLayout(Context context) {
        rootView = LayoutInflater.from(context).inflate(layoutId(), null);
        bg = rootView.findViewById(R.id.bg);
        content = rootView.findViewById(R.id.content);
        if (bg == null || content == null) {
            Log.e("BasePopupWindow", "layout must has id bg and content");
        }
        initContentView();
    }

}
