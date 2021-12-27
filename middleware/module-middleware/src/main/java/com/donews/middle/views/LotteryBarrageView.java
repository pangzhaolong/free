package com.donews.middle.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.donews.middle.bean.front.AwardBean;
import com.donews.utilslibrary.utils.LogUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class LotteryBarrageView extends FrameLayout {
    private final int ITEM_NUMS = 6;
    private final static int MSG_ID = 10001;
    private final LotteryBarrageItemView[] mLotteryBarrageViews = new LotteryBarrageItemView[ITEM_NUMS];
    private final List<AwardBean.AwardInfo> mAwardList = new ArrayList<>();

    private ScrollHandler mScrollHandler = null;

    private int mListIndex = 0;
    private boolean mIsPause = false;

    public LotteryBarrageView(Context context) {
        super(context);
    }

    public LotteryBarrageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        for (int i = 0; i < ITEM_NUMS; i++) {
            mLotteryBarrageViews[i] = new LotteryBarrageItemView(context, attrs);
            mLotteryBarrageViews[i].setVisibility(INVISIBLE);
            addView(mLotteryBarrageViews[i]);
        }

        mScrollHandler = new ScrollHandler(this);

        LogUtil.e("LotteryBarrageView on create");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mIsPause) {
            return;
        }

        int nCount = getChildCount();
        for (int i = 0; i < nCount; i++) {
            View view = getChildAt(i);
            measureChild(view, 0, 0);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (mIsPause) {
            return;
        }
        int topx = 0;
        int nCount = getChildCount();
        for (int i = 0; i < nCount; i++) {
            View v = getChildAt(i);
            int childHeight = v.getMeasuredHeight();
            int childWidth = v.getMeasuredWidth();
            if (i % 2 == 1) {
                v.layout(this.getWidth(), topx + childHeight, this.getWidth() + childWidth, topx + 2 * childHeight);
            } else {
                v.layout(this.getWidth(), topx, this.getWidth() + childWidth, topx + childHeight);
            }
        }
    }

    public void refreshData(List<AwardBean.AwardInfo> list) {
        mAwardList.clear();
        mAwardList.addAll(list);

        if (mAwardList.size() <= 0) {
            return;
        }
        if (mScrollHandler != null) {
            if (mScrollHandler.hasMessages(MSG_ID)) {
                mScrollHandler.removeMessages(MSG_ID);
            }
        }
        startScroll();
    }

    public void startScroll() {
        initView();
    }

    public void pauseScroll() {
        mIsPause = true;
    }

    public void resumeScroll() {
        mIsPause = false;
    }

    public void stopScroll() {
        mIsPause = true;
        if (mScrollHandler != null) {
            mScrollHandler.removeCallbacksAndMessages(null);
            mScrollHandler = null;
        }
        for (int i = 0; i < mTAs.length; i++) {
            if (mTAs[i] != null) {
                mTAs[i].cancel();
                mTAs[i] = null;
            }
        }

        for (int i = 0; i < mLotteryBarrageViews.length; i++) {
            if (mLotteryBarrageViews[i] != null) {
                mLotteryBarrageViews[i] = null;
            }
        }
    }

    private TranslateAnimation[] mTAs = new TranslateAnimation[6];

    private int getIdleTransAnimatorIdx() {
        for (int idx = 0; idx < mTAs.length; idx++) {
            if (mTAs[idx] == null) {
                return idx;
            }
        }
        return -1;
    }

    private int getIdleItemView() {
        for (int i = 0; i < mLotteryBarrageViews.length; i++) {
            if (mLotteryBarrageViews[i].isIdle()) {
                return i;
            }
        }
        return -1;
    }

    private void initView() {
        mListIndex++;
        if (mListIndex < 0 || mListIndex >= mAwardList.size()) {
            mListIndex = 0;
        }

        int idleViewIdx = getIdleItemView();
        if (idleViewIdx < 0) {
            mScrollHandler.sendEmptyMessageDelayed(MSG_ID, 1000);
            return;
        }

        int idleTransIdx = getIdleTransAnimatorIdx();
        if (idleTransIdx < 0) {
            mScrollHandler.sendEmptyMessageDelayed(MSG_ID, 1000);
            return;
        }

        gogogogogo(idleTransIdx, idleViewIdx, mListIndex);
    }

    @SuppressLint("Recycle")
    private void gogogogogo(int transIdx, int viewIdx, int infoIdx) {
//        LogUtil.e("LotteryBarrageView gogogo:" + transIdx + " vid:" + viewIdx + " infoIdx:" + infoIdx);
        AwardBean.AwardInfo awardInfo = mAwardList.get(infoIdx);

        int viewGroupWidth = this.getWidth();
        mLotteryBarrageViews[viewIdx].setIdle(false);
        mLotteryBarrageViews[viewIdx].setVisibility(View.VISIBLE);
        mLotteryBarrageViews[viewIdx].setUserAwardInfo(awardInfo.getAvatar(), awardInfo.getName(), awardInfo.getProduceName());
        mLotteryBarrageViews[viewIdx].measure(0, 0);
        mLotteryBarrageViews[viewIdx].clearAnimation();
        mTAs[transIdx] = new TranslateAnimation(0,
                -viewGroupWidth - mLotteryBarrageViews[viewIdx].getMeasuredWidth(), 0, 0);
        mTAs[transIdx].setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mLotteryBarrageViews[viewIdx].setVisibility(INVISIBLE);
                mLotteryBarrageViews[viewIdx].setIdle(true);
                mTAs[transIdx].cancel();
                mTAs[transIdx] = null;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        mTAs[transIdx].setDuration(5000);
        mTAs[transIdx].setRepeatCount(0);
        mLotteryBarrageViews[viewIdx].startAnimation(mTAs[transIdx]);

        mScrollHandler.sendEmptyMessageDelayed(MSG_ID, 2000);
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
            if (msg.what == MSG_ID) {
                mBarrageView.get().initView();
            }
        }
    }
}
