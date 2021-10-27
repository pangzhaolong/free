package com.donews.front.views;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.donews.front.bean.AwardBean;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class BarrageView extends LinearLayout {
    private UserAwardInfoView mAwardView1;

    private final List<AwardBean.AwardInfo> mAwardList = new ArrayList<>();

    private ScrollHandler mScrollHandler = null;

    private ObjectAnimator mMoveAnimator;
    private ValueAnimator mAlphaAnimator;

    private int mListIndex = 0;

    public BarrageView(Context context) {
        super(context);
    }

    public BarrageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mAwardView1 = new UserAwardInfoView(context, attrs);
        mAwardView1.setVisibility(GONE);

        addView(mAwardView1);

        mScrollHandler = new ScrollHandler(this);

        mMoveAnimator = ObjectAnimator.ofFloat(mAwardView1, "translationX", 0);
        mMoveAnimator.setDuration(4000);
        mMoveAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mScrollHandler.sendEmptyMessageDelayed(10000, 3000);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mAlphaAnimator = ValueAnimator.ofFloat(1.0f, 0.0f);
        mAlphaAnimator.addUpdateListener(animation -> mAwardView1.setAlpha((float) animation.getAnimatedValue()));
        mAlphaAnimator.setDuration(1000);
        mAlphaAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mScrollHandler.sendEmptyMessage(10001);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    public void refreshData(List<AwardBean.AwardInfo> list) {
        mAwardList.clear();
        mAwardList.addAll(list);

        if (mAwardList.size() <= 0) {
            return;
        }
        startScroll();
    }

    public void startScroll() {
        initView();
    }

    public void pauseScroll() {
        if (mMoveAnimator != null) {
            mMoveAnimator.pause();
        }
        if (mAlphaAnimator != null) {
            mAlphaAnimator.pause();
        }
    }

    public void resumeScroll() {
        if (mMoveAnimator != null) {
            mMoveAnimator.resume();
        }
        if (mAlphaAnimator != null) {
            mAlphaAnimator.resume();
        }
    }

    public void stopScroll() {
        if (mScrollHandler != null) {
            mScrollHandler.removeCallbacksAndMessages(null);
            mScrollHandler = null;
        }
        if (mMoveAnimator != null) {
            mMoveAnimator.removeAllListeners();
            mMoveAnimator.cancel();
            mMoveAnimator = null;
        }
        if (mAlphaAnimator != null) {
            mAlphaAnimator.removeAllListeners();
            mAlphaAnimator.cancel();
            mAlphaAnimator = null;
        }
    }

    private void initView() {
        mListIndex++;
        if (mListIndex < 0 || mListIndex >= mAwardList.size()) {
            mListIndex = 0;
        }

        AwardBean.AwardInfo awardInfo = mAwardList.get(mListIndex);
        mAwardView1.setUserAwardInfo(awardInfo.getAvatar(), awardInfo.getName(), awardInfo.getProduceName());
        mAwardView1.setTranslationX(700);
        mAwardView1.setVisibility(VISIBLE);
        mAwardView1.setAlpha(1.0f);

        mMoveAnimator.start();
    }

    private void hideView() {
        if (mAlphaAnimator == null) {
            mScrollHandler.sendEmptyMessageDelayed(10001, 1000);
            return;
        }

        mAlphaAnimator.start();
    }

    private static class ScrollHandler extends Handler {
        private final WeakReference<BarrageView> mBarrageView;

        public ScrollHandler(BarrageView barrageView) {
            super();
            mBarrageView = new WeakReference<>(barrageView);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 10000) {
                mBarrageView.get().hideView();
            } else if (msg.what == 10001) {
                mBarrageView.get().initView();
            }
        }
    }
}
