package com.donews.middle.views;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.donews.middle.bean.front.AwardBean;
import com.donews.utilslibrary.utils.DensityUtils;
import com.donews.utilslibrary.utils.LogUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class LotteryBarrageView extends FrameLayout {
    private LotteryBarrageItemView mAwardView1;
    private LotteryBarrageItemView mAwardView2;

    private final List<AwardBean.AwardInfo> mAwardList = new ArrayList<>();

    private ScrollHandler mScrollHandler = null;

    private ObjectAnimator mMoveAnimator1;
    private ObjectAnimator mMoveAnimator2;

    private int mListIndex = 0;

    private boolean mIsPause = false;

    public LotteryBarrageView(Context context) {
        super(context);
    }

    public LotteryBarrageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mAwardView1 = new LotteryBarrageItemView(context, attrs);
        mAwardView2 = new LotteryBarrageItemView(context, attrs);

        addView(mAwardView1);
        addView(mAwardView2);

        mScrollHandler = new ScrollHandler(this);

        LogUtil.e("LotteryBarrageView on create");
        LogUtil.e("LotteryBarrageView on create:" + DensityUtils.dp2px(30));
        initAnimation();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mIsPause) {
            return;
        }

        int nCount = getChildCount();

        LogUtil.e("LotteryBarrageView onMeasure : " + nCount);

        for (int i = 0; i < nCount; i++) {
            View view = getChildAt(i);
            measureChild(view, 10, 10);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (mIsPause) {
            return;
        }
        int topx = 4;

        int nCount = getChildCount();
        for (int i = 0; i < nCount; i++) {
            View v = getChildAt(i);
            int childHeight = v.getMeasuredHeight();
            int childWidth = v.getMeasuredWidth();
            LogUtil.e("LotteryBarrageView onLayout child view top:" + topx + " width:" + childWidth
                    + " height:" + childHeight + " p width:" + this.getWidth() + " p height:" + this.getHeight());
            v.layout(this.getWidth(), topx, this.getWidth() + childWidth, topx + childHeight);
            topx += childHeight;
        }
    }

    private void initAnimation() {
        mMoveAnimator1 = ObjectAnimator.ofFloat(mAwardView1, "translationX", 0);
        mMoveAnimator1.setDuration(2000);
        mMoveAnimator1.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                LogUtil.e("LotteryBarrageView mMoveAnimator1 end");
                mScrollHandler.sendEmptyMessageDelayed(10000, 0);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mMoveAnimator2 = ObjectAnimator.ofFloat(mAwardView2, "translationX", 0);
        mMoveAnimator2.setDuration(2000);
        mMoveAnimator2.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                LogUtil.e("LotteryBarrageView mMoveAnimator2 end");
                mScrollHandler.sendEmptyMessageDelayed(10000, 0);
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
//        initView();
    }

    public void pauseScroll() {
        mIsPause = true;
        if (mMoveAnimator1 != null) {
            mMoveAnimator1.pause();
        }
        if (mMoveAnimator2 != null) {
            mMoveAnimator2.pause();
        }
    }

    public void resumeScroll() {
        mIsPause = false;
        if (mMoveAnimator1 != null) {
            mMoveAnimator1.resume();
        }
        if (mMoveAnimator2 != null) {
            mMoveAnimator2.resume();
        }
    }

    public void stopScroll() {
        mIsPause = true;
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
        if (mMoveAnimator2 != null) {
            mMoveAnimator2.removeAllUpdateListeners();
            mMoveAnimator2.removeAllListeners();
            mMoveAnimator2.cancel();
            mMoveAnimator2 = null;
        }
    }

    private void initView() {
        mListIndex++;
        if (mListIndex < 0 || mListIndex >= mAwardList.size()) {
            mListIndex = 0;
        }

        AwardBean.AwardInfo awardInfo = mAwardList.get(mListIndex);
        mAwardView1.setUserAwardInfo(awardInfo.getAvatar(), awardInfo.getName(), awardInfo.getProduceName());

        int left = mAwardView1.getLeft();
        int right = mAwardView1.getRight();
        ValueAnimator valueAnimator = ValueAnimator.ofInt(this.getWidth() + mAwardView1.getMeasuredWidth());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                LogUtil.e("LotteryBarrageView pg:" + (int) animation.getAnimatedValue());
                mAwardView1.layout(left - (int) animation.getAnimatedValue(), mAwardView1.getTop()
                        , right - (int) animation.getAnimatedValue(), mAwardView1.getBottom());
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                valueAnimator.cancel();
                mScrollHandler.sendEmptyMessage(10000);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        valueAnimator.setDuration(4000);
        valueAnimator.setRepeatCount(1);
        valueAnimator.start();

/*        mAwardView1.setTranslationX(this.getWidth() + mAwardView1.getMeasuredWidth());
        mMoveAnimator1.start();
        mAwardView2.setUserAwardInfo(awardInfo.getAvatar(), awardInfo.getName(), awardInfo.getProduceName());
        mAwardView2.setTranslationX(this.getWidth() + mAwardView2.getMeasuredWidth());
        mMoveAnimator2.start();*/
    }

    private void hideView() {
        if (mIsPause) {
            return;
        }
        mScrollHandler.sendEmptyMessage(10001);
    }

    private static class ScrollHandler extends Handler {
        private final WeakReference<LotteryBarrageView> mBarrageView;

        public ScrollHandler(LotteryBarrageView barrageView) {
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
