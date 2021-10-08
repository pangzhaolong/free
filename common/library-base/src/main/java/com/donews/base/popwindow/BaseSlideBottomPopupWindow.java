package com.donews.base.popwindow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import androidx.fragment.app.FragmentActivity;

import com.donews.base.R;
import com.donews.base.activity.MvvmBaseLiveDataActivity;
import com.donews.utilslibrary.utils.DeviceUtils;


/**
 * @Author: honeylife
 * @CreateDate: 2020/4/15 16:57
 * @Description:
 */
public abstract class BaseSlideBottomPopupWindow extends BasePopupWindow {

    protected View bg;
    protected View content;

    private View showAtView;
    private int offsetDp;

    public BaseSlideBottomPopupWindow(FragmentActivity context) {
        super(context);
    }

    @Override
    protected void showAnimation() {


        if (showAtView != null) {
            int[] rootViewLocation = new int[2];
            ((MvvmBaseLiveDataActivity) getBaseActivity()).getRootView().getLocationInWindow(rootViewLocation);
            int[] showAtViewLocation = new int[2];
            showAtView.getLocationInWindow(showAtViewLocation);
//            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) content.getLayoutParams();
//            layoutParams.bottomMargin =showAtView.getMeasuredHeight() + DeviceUtils.dip2px(offsetDp); ;
//            content.setLayoutParams(layoutParams);
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) rootView.getLayoutParams();
            params.bottomMargin =showAtView.getMeasuredHeight() + DeviceUtils.dip2px(offsetDp); ;
            rootView.setLayoutParams(params);
        }
        rootView.setVisibility(View.VISIBLE);
        Animation alphaAnimation = AnimationUtils.loadAnimation(baseActivity.getApplicationContext(), R.anim.base_popup_bg_fadein);
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
        Animation translateAnimation = AnimationUtils.loadAnimation(baseActivity.getApplicationContext(), R.anim.base_popup_content_slidein);
        content.startAnimation(translateAnimation);
        bg.startAnimation(alphaAnimation);

    }

    @Override
    protected void hideAnimation() {
        Animation alphaAnimation = AnimationUtils.loadAnimation(baseActivity.getApplicationContext(), R.anim.base_popup_bg_fadeout);
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
        Animation translateAnimation = AnimationUtils.loadAnimation(baseActivity.getApplicationContext(), R.anim.base_popup_content_slideout);
        content.startAnimation(translateAnimation);
        bg.startAnimation(alphaAnimation);
    }

    protected abstract int layoutId();

    protected abstract void initContentView();

    protected abstract boolean autoDismiss();

    @Override
    protected final void initLayout(Context context) {
        rootView = LayoutInflater.from(context).inflate(layoutId(), null);
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (autoDismiss()) {
                    hide();
                }
            }
        });
        bg = rootView.findViewById(R.id.bg);
        content = rootView.findViewById(R.id.content);
        if (bg == null || content == null) {
        }
        initContentView();
    }

    public final void showAtView(View view) {
        showAtView(view, 0);
    }

    public final void showAtView(View view, int offsetDp) {
        this.showAtView = view;
        this.offsetDp = offsetDp;
        show();
    }


}
