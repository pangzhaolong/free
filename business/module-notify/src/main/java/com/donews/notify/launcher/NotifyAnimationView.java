package com.donews.notify.launcher;


import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

import com.donews.notify.launcher.utils.NotifyItemType;
import com.donews.utilslibrary.utils.AppInfo;


public class NotifyAnimationView extends LinearLayout {

    /**
     * 是否自动隐藏
     */
    private boolean mAutoHide = true;
    /**
     * 自动隐藏时间
     */
    private long mHideDuration = 5000;
    /**
     * 动画执行时间
     */
    private int mShowAniTime = 800;
    private int mHideAniTime = 240;

    /**
     * 通知的类型，参考: {@link com.donews.notify.launcher.utils.NotifyItemType} 相关常量
     */
    public NotifyItemType notifyType;
    /**
     * 是否为顶部通知。T:是，F:底部通知
     */
    public boolean isTopNotify = false;

    private ViewStatusListener mViewStatusListener = new DefaultViewStatusListener();

    private android.os.Handler mHandler = new Handler(Looper.getMainLooper());

    private Runnable mHideRunnable = new Runnable() {

        @Override
        public void run() {
            hideWithAnimation();
        }
    };

    public void setAutoHide(boolean autoHide) {
        this.mAutoHide = autoHide;
    }

    public void setHideDuration(long hideDuration) {
        if (hideDuration >= 0) {
            this.mHideDuration = hideDuration;
        }
    }

    public void setShowAniTime(int showAniTime) {
        if (showAniTime >= 0) {
            this.mShowAniTime = showAniTime;
        }
    }

    public void setHideAniTime(int hideAniTime) {
        if (hideAniTime >= 0) {
            this.mHideAniTime = hideAniTime;
        }
    }

    public void setViewDimissListener(ViewStatusListener viewStatusListener) {
        this.mViewStatusListener = viewStatusListener;
    }

    public NotifyAnimationView(Context context) {
        super(context);
    }

    public NotifyAnimationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public NotifyAnimationView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void start() {
        try {
            setVisibility(View.VISIBLE);
            if (isTopNotify) {
                startWithAnimation(); //顶部动画
            } else {
                startBotWithAnimation(); //底部动画
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public void hide() {
        try {
            mHandler.removeCallbacks(mHideRunnable);
            mHideRunnable.run();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private void startWithAnimation() {
        TranslateAnimation translateAnimation = createTranslateAnimation(0f, 0f, -1f, 0f);
        translateAnimation.setDuration(mShowAniTime);
        translateAnimation.setInterpolator(new OvershootInterpolator());
        translateAnimation.setAnimationListener(new DefaultAnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                mViewStatusListener.onNotifyShow(NotifyAnimationView.this);
                if (mAutoHide) {
                    mHandler.postDelayed(mHideRunnable, mHideDuration);
                }
            }
        });
        this.startAnimation(translateAnimation);
    }

    //底部动画
    private void startBotWithAnimation() {
        TranslateAnimation translateAnimation = createTranslateAnimation(0f, 0f, 1f, 0f);
        translateAnimation.setDuration(mShowAniTime);
        translateAnimation.setInterpolator(new OvershootInterpolator());
        translateAnimation.setAnimationListener(new DefaultAnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                mViewStatusListener.onNotifyShow(NotifyAnimationView.this);
                if (mAutoHide) {
                    mHandler.postDelayed(mHideRunnable, mHideDuration);
                }
            }
        });
        this.startAnimation(translateAnimation);
    }

    private void hideWithAnimation() {
        TranslateAnimation translateAnimation;
        if (isTopNotify) {
            translateAnimation = createTranslateAnimation(0f, 0f, 0f, -1f);
        } else {
            translateAnimation = createTranslateAnimation(0f, 0f, 0f, 1f);
        }
        translateAnimation.setDuration(mHideAniTime);
        translateAnimation.setInterpolator(new LinearInterpolator());
        translateAnimation.setAnimationListener(new DefaultAnimationListener() {

            @Override
            public void onAnimationEnd(Animation animation) {
                try {
                    if (NotifyAnimationView.this.isShown()) {
                        NotifyAnimationView.this.setVisibility(View.GONE);
                        ((ViewGroup) NotifyAnimationView.this.getParent()).removeView(NotifyAnimationView.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mViewStatusListener.onNotifyClose(NotifyAnimationView.this);
            }
        });
        NotifyAnimationView.this.startAnimation(translateAnimation);
    }

    private TranslateAnimation createTranslateAnimation(float x, float toX, float y, float toY) {
        TranslateAnimation translateAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, x, Animation.RELATIVE_TO_SELF, toX,
                Animation.RELATIVE_TO_SELF, y, Animation.RELATIVE_TO_SELF, toY);
        translateAnimation.setFillAfter(true);
        return translateAnimation;
    }

    private class DefaultAnimationListener implements AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    public static class DefaultViewStatusListener implements ViewStatusListener {

        @Override
        public void onNotifyClose(View view) {

        }

        @Override
        public void onNotifyShow(View view) {

        }
    }


    public interface ViewStatusListener {

        void onNotifyClose(View view);

        void onNotifyShow(View view);

    }

}
