package com.donews.mine.views.scrollview;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.donews.mine.bean.AwardBean;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class BarrageView extends FrameLayout {
    private UserAwardInfoView mAwardView1;
    private UserAwardInfoView mAwardView2;

    private final List<AwardBean.AwardInfo> mAwardList = new ArrayList<>();

    private ScrollHandler mScrollHandler = null;

    private ObjectAnimator mMoveAnimator1;
    private ValueAnimator mAlphaAnimator1;
    private ObjectAnimator mMoveAnimator2;
    private ValueAnimator mAlphaAnimator2;

    private int mListIndex = 0;

    private boolean mExchange = false;

    public BarrageView(Context context) {
        super(context);
    }

    public BarrageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mAwardView1 = new UserAwardInfoView(context, attrs);
        mAwardView1.setVisibility(GONE);
        mAwardView2 = new UserAwardInfoView(context, attrs);
        mAwardView2.setVisibility(GONE);

        addView(mAwardView1);
        addView(mAwardView2);

        mScrollHandler = new ScrollHandler(this);

        initAnimation1();
        initAnimation2();
    }

    private void initAnimation1() {
        mMoveAnimator1 = ObjectAnimator.ofFloat(mAwardView1, "translationY", 0);
        mMoveAnimator1.setDuration(2000);
        mMoveAnimator1.addListener(new Animator.AnimatorListener() {
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
        mAlphaAnimator1 = ValueAnimator.ofFloat(1.0f, 0.0f);
        mAlphaAnimator1.addUpdateListener(animation -> mAwardView1.setAlpha((float) animation.getAnimatedValue()));
        mAlphaAnimator1.setDuration(1000);
        mAlphaAnimator1.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mAwardView1.setVisibility(GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void initAnimation2() {
        mMoveAnimator2 = ObjectAnimator.ofFloat(mAwardView2, "translationY", 0);
        mMoveAnimator2.setDuration(2000);
        mMoveAnimator2.addListener(new Animator.AnimatorListener() {
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
        mAlphaAnimator2 = ValueAnimator.ofFloat(1.0f, 0.0f);
        mAlphaAnimator2.addUpdateListener(animation -> mAwardView2.setAlpha((float) animation.getAnimatedValue()));
        mAlphaAnimator2.setDuration(1000);
        mAlphaAnimator2.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mAwardView2.setVisibility(GONE);
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
        if (mMoveAnimator1 != null) {
            mMoveAnimator1.pause();
        }
        if (mAlphaAnimator1 != null) {
            mAlphaAnimator1.pause();
        }
        if (mMoveAnimator2 != null) {
            mMoveAnimator2.pause();
        }
        if (mAlphaAnimator2 != null) {
            mAlphaAnimator2.pause();
        }
    }

    public void resumeScroll() {
        if (mMoveAnimator1 != null) {
            mMoveAnimator1.resume();
        }
        if (mAlphaAnimator1 != null) {
            mAlphaAnimator1.resume();
        }
        if (mMoveAnimator2 != null) {
            mMoveAnimator2.resume();
        }
        if (mAlphaAnimator2 != null) {
            mAlphaAnimator2.resume();
        }
    }

    public void stopScroll() {
        if (mScrollHandler != null) {
            mScrollHandler.removeCallbacksAndMessages(null);
            mScrollHandler = null;
        }
        if (mMoveAnimator1 != null) {
            mMoveAnimator1.removeAllUpdateListeners();
            mMoveAnimator1.removeAllListeners();
            mMoveAnimator1.cancel();
            mMoveAnimator1 = null;
        }
        if (mAlphaAnimator1 != null) {
            mAlphaAnimator1.removeAllUpdateListeners();
            mAlphaAnimator1.removeAllListeners();
            mAlphaAnimator1.cancel();
            mAlphaAnimator1 = null;
        }
        if (mMoveAnimator2 != null) {
            mMoveAnimator2.removeAllUpdateListeners();
            ;
            mMoveAnimator2.removeAllListeners();
            mMoveAnimator2.cancel();
            mMoveAnimator2 = null;
        }
        if (mAlphaAnimator2 != null) {
            mAlphaAnimator2.removeAllUpdateListeners();
            mAlphaAnimator2.removeAllListeners();
            mAlphaAnimator2.cancel();
            mAlphaAnimator2 = null;
        }
    }

    private void initView() {
        mListIndex++;
        if (mListIndex < 0 || mListIndex >= mAwardList.size()) {
            mListIndex = 0;
        }

        AwardBean.AwardInfo awardInfo = mAwardList.get(mListIndex);
        if (!mExchange) {
            mAwardView1.setUserAwardInfo(awardInfo.getAvatar(), awardInfo.getName(), awardInfo.getProduceName());
            mAwardView1.setTranslationY(60);
            mAwardView1.setVisibility(VISIBLE);
            mAwardView1.setAlpha(1.0f);
            mMoveAnimator1.start();
        } else {
            mAwardView2.setUserAwardInfo(awardInfo.getAvatar(), awardInfo.getName(), awardInfo.getProduceName());
            mAwardView2.setTranslationY(60);
            mAwardView2.setVisibility(VISIBLE);
            mAwardView2.setAlpha(1.0f);
            mMoveAnimator2.start();
        }
    }

    private void hideView() {
        if (mAlphaAnimator1 == null || mAlphaAnimator2 == null) {
            mScrollHandler.sendEmptyMessageDelayed(10001, 1000);
            return;
        }

        if (!mExchange) {
            mAlphaAnimator1.start();
        } else {
            mAlphaAnimator2.start();
        }
        mExchange = !mExchange;
        mScrollHandler.sendEmptyMessage(10001);
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
