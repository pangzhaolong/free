package com.donews.common.base.popwindow;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.donews.base.R;


public abstract class AbstractConfirmPopupWindow extends BasePopupWindow {

    private static final int SHOW_DURATION = 300;

    private View bg;
    private View content;
    private TextView okView;
    private TextView cancelView;
    private TextView titleView;
    private TextView noticeView;
    private TextView titleHintView;

    public AbstractConfirmPopupWindow(FragmentActivity context) {
        super(context);
    }

    @Override
    protected void showAnimation() {
        rootView.setVisibility(View.VISIBLE);
        final ScaleAnimation animation = new ScaleAnimation(0.5f, 1.0f, 0.5f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setInterpolator(new OvershootInterpolator());
        animation.setDuration(SHOW_DURATION);
        animation.setAnimationListener(new Animation.AnimationListener() {
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
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.6f, 1.0f);
        alphaAnimation.setInterpolator(new OvershootInterpolator());
        alphaAnimation.setDuration(SHOW_DURATION);
        bg.startAnimation(alphaAnimation);
        content.startAnimation(animation);
    }

    @Override
    protected void hideAnimation() {
        final AlphaAnimation animation = new AlphaAnimation(1.0f, 0.0f);
        animation.setInterpolator(new AccelerateInterpolator());
        animation.setDuration(SHOW_DURATION);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                setIsAnimating(true);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setIsAnimating(false);
                rootView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setInterpolator(new AccelerateInterpolator());
        alphaAnimation.setDuration(SHOW_DURATION);
        bg.startAnimation(alphaAnimation);
        content.startAnimation(animation);
    }


    protected abstract int layoutId();

    protected abstract void initContentView();

    public AbstractConfirmPopupWindow setTitleText(final CharSequence charSequence) {
        titleView.setText(charSequence);
        return this;
    }

    public AbstractConfirmPopupWindow setOkText(final CharSequence charSequence) {
        okView.setText(charSequence);
        return this;
    }

    public AbstractConfirmPopupWindow setOkOnClick(View.OnClickListener onClickListener) {
        okView.setOnClickListener(onClickListener);
        return this;
    }

    public AbstractConfirmPopupWindow setCancelText(final CharSequence charSequence) {
        cancelView.setText(charSequence);
        return this;
    }

    public AbstractConfirmPopupWindow setCancelOnClick(View.OnClickListener onClickListener) {
        cancelView.setOnClickListener(onClickListener);
        return this;
    }

    public AbstractConfirmPopupWindow setNoticeText(final CharSequence charSequence) {
        noticeView.setText(charSequence);
        return this;
    }

    public AbstractConfirmPopupWindow showNoticeView(final boolean isShow) {
        noticeView.setVisibility(isShow ? View.GONE : View.VISIBLE);
        return this;
    }

    public AbstractConfirmPopupWindow showTitleHintView(final boolean isShow) {
        titleHintView.setVisibility(isShow ? View.GONE : View.VISIBLE);
        return this;
    }

    public AbstractConfirmPopupWindow showTitleView(final boolean isShow) {
        titleView.setVisibility(isShow ? View.GONE : View.VISIBLE);
        return this;
    }

    public AbstractConfirmPopupWindow setTitleHintText(final CharSequence charSequence) {
        titleHintView.setText(charSequence);
        return this;
    }

    @Override
    protected final void initLayout(Context context) {
        rootView = LayoutInflater.from(context).inflate(layoutId(), null);
        okView = (TextView) rootView.findViewById(R.id.ok);
        cancelView = (TextView) rootView.findViewById(R.id.cancel);
        titleView = (TextView) rootView.findViewById(R.id.title);
        titleHintView = ((TextView) rootView.findViewById(R.id.title_hint));
        noticeView = ((TextView) rootView.findViewById(R.id.notice));
        bg = rootView.findViewById(R.id.bg);
        content = rootView.findViewById(R.id.content);
        if (bg == null || content == null || okView == null || cancelView == null || noticeView
                == null || titleHintView == null) {
            Log.e("PopupWindow", "layout must has id bg content ok and cancel");
        }

        if (cancelView != null) {
            cancelView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hide();
                }
            });
        }

        bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
            }
        });
        initContentView();
    }

}
