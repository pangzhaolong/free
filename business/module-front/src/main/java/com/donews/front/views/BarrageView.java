package com.donews.front.views;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.donews.front.R;
import com.donews.front.bean.AwardBean;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class BarrageView extends LinearLayout {
    private UserAwardInfoView mAwardView1;
    private UserAwardInfoView mAwardView2;

    private final List<AwardBean> mAwardList = new ArrayList<>();

    private ScrollHandler mScrollHandler = null;

    public BarrageView(Context context) {
        super(context);
    }

    public BarrageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mAwardView1 = new UserAwardInfoView(context, attrs);
        mAwardView2 = new UserAwardInfoView(context, attrs);

        addView(mAwardView1);
        addView(mAwardView2);

        mScrollHandler = new ScrollHandler(this);

        refreshData();
    }

    public void refreshData() {
        initAwardList();

        mAwardView1.setUserAwardInfo(mAwardList.get(0).avatar, mAwardList.get(0).name, mAwardList.get(0).award);
        mAwardView2.setUserAwardInfo(mAwardList.get(1).avatar, mAwardList.get(1).name, mAwardList.get(1).award);
//        mAwardView2.setTranslationX(mAwardView2.getX()+100);

        startScroll();
    }

    public void startScroll() {
        mScrollHandler.sendEmptyMessageDelayed(10000, 200);
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
        mAwardView1.setTranslationX(mAwardView1.getX()-10);
//        mAwardView2.setTranslationX(mAwardView2.getX()-10);
        mScrollHandler.sendEmptyMessageDelayed(10000, 100);
    }

    private static class ScrollHandler extends Handler {
        private WeakReference<BarrageView> mBarrageView;

        public ScrollHandler(BarrageView barrageView) {
            super();
            mBarrageView = new WeakReference<>(barrageView);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 10000) {
                mBarrageView.get().scrollViews();
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
