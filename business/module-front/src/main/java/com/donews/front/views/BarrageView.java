package com.donews.front.views;

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


    private final List<AwardBean> mAwardList = new ArrayList<>();

    private ScrollHandler mScrollHandler = null;

    private float mDefaultXPos = 0;

    private int mListIndex = 0;

    public BarrageView(Context context) {
        super(context);
    }

    public BarrageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mAwardView1 = new UserAwardInfoView(context, attrs);

        addView(mAwardView1);

        mScrollHandler = new ScrollHandler(this);

        refreshData();
    }

    public void refreshData() {
        initAwardList();

        if (mAwardList.size() <= 0) {
            return;
        }
        AwardBean awardBean = mAwardList.get(0);
        mAwardView1.setUserAwardInfo(awardBean.avatar, awardBean.name, awardBean.award);
        mDefaultXPos = mAwardView1.getX();
        mAwardView1.setTranslationX(mDefaultXPos + 900);
        startScroll();
    }

    public void startScroll() {
        mScrollHandler.sendEmptyMessageDelayed(10000, 50);
    }

    public void pauseScroll() {

    }

    public void resumeScroll() {

    }

    public void stopScroll() {
        if (mScrollHandler != null) {
            mScrollHandler.removeCallbacksAndMessages(null);
            mScrollHandler = null;
        }
    }

    public void scrollViews() {
        mAwardView1.setTranslationX(mAwardView1.getX() - 10);
        if (mAwardView1.getX() <= 40) {
            mScrollHandler.sendEmptyMessageDelayed(10001, 2000);
            return;
        }
        mScrollHandler.sendEmptyMessageDelayed(10000, 50);
    }

    public void reInitView() {
        mListIndex++;
        if (mListIndex < 0 || mListIndex >= mAwardList.size()) {
            mListIndex = 0;
        }

        AwardBean awardBean = mAwardList.get(mListIndex);
        mAwardView1.setUserAwardInfo(awardBean.avatar, awardBean.name, awardBean.award);
        mAwardView1.setTranslationX(mDefaultXPos + 500);
        mScrollHandler.sendEmptyMessageDelayed(10000, 50);
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
                mBarrageView.get().scrollViews();
            } else if (msg.what == 10001) {
                mBarrageView.get().reInitView();
            }
        }
    }

    private void initAwardList() {
        mAwardList.add(new AwardBean(
                "https://wx4.sinaimg.cn/orj360/95936907ly8gdi6ssvmecj20u00u0q51.jpg"
                , "测试0", "iPhone13"));
        mAwardList.add(new AwardBean(
                "https://wx4.sinaimg.cn/orj360/95936907ly8gdi6ssvmecj20u00u0q51.jpg"
                , "测试1", "iPhone13"));
        mAwardList.add(new AwardBean(
                "https://wx4.sinaimg.cn/orj360/95936907ly8gdi6ssvmecj20u00u0q51.jpg"
                , "测试2", "iPhone13"));
        mAwardList.add(new AwardBean(
                "https://wx4.sinaimg.cn/orj360/95936907ly8gdi6ssvmecj20u00u0q51.jpg"
                , "测试3", "iPhone13"));
        mAwardList.add(new AwardBean(
                "https://wx4.sinaimg.cn/orj360/95936907ly8gdi6ssvmecj20u00u0q51.jpg"
                , "测试4", "iPhone13"));
        mAwardList.add(new AwardBean(
                "https://wx4.sinaimg.cn/orj360/95936907ly8gdi6ssvmecj20u00u0q51.jpg"
                , "测试5", "iPhone13"));
        mAwardList.add(new AwardBean(
                "https://wx4.sinaimg.cn/orj360/95936907ly8gdi6ssvmecj20u00u0q51.jpg"
                , "测试6", "iPhone13"));
    }
}
