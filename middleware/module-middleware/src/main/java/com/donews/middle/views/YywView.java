package com.donews.middle.views;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.donews.middle.bean.front.FrontConfigBean;
import com.donews.middle.front.FrontConfigManager;
import com.donews.middle.go.GotoUtil;
import com.donews.utilslibrary.analysis.AnalysisUtils;
import com.donews.utilslibrary.dot.Dot;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


public class YywView extends androidx.appcompat.widget.AppCompatImageView {

    private static final int MESSAGE_ID = 10001;
    private static final int MESSAGE_SWITCH_ID = 10002;
    private final Context mContext;

    private int mSwitchInterval = 10;
    private boolean mEnableYyw = false;
    public final static int Model_Banner = 0;
    public final static int Model_WithDrawl = 1;
    private int mCurrentModel = Model_Banner;

    private YywHandler mYywHandler;

    private List<FrontConfigBean.YywItem> mYywItemList = new ArrayList<>();

    private int mYYWIndex = 0;
    private String mFrom;

    public YywView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        this.setVisibility(GONE);

        mYywHandler = new YywHandler(Looper.getMainLooper(), this);
    }

    private void setYywItem(FrontConfigBean.YywItem bannerItem) {
        if (((Activity) mContext).isDestroyed() || ((Activity) mContext).isFinishing()) {
            return;
        }
        Glide.with(this).load(bannerItem.getImg()).into(this);
        this.requestLayout();
        this.setOnClickListener(v -> {
            GotoUtil.doAction(mContext, bannerItem.getAction(), bannerItem.getTitle());
            AnalysisUtils.onEventEx(mContext, Dot.BANNER_CLICK, mFrom + "-" + mYYWIndex);
            if (mYywHandler != null) {
                mYywHandler.removeMessages(MESSAGE_SWITCH_ID);
                mYywHandler.sendEmptyMessageDelayed(MESSAGE_SWITCH_ID, 1500);
            }
//            refreshYywItem();
        });
    }

    public void refreshYyw(int model) {
        mCurrentModel = model;
        mYywItemList.clear();
        if (mCurrentModel == Model_Banner) {
            mFrom = "Place_banner";
            mYywItemList.addAll(FrontConfigManager.Ins().getConfigBean().getBannerItems().getItems());
            mSwitchInterval = FrontConfigManager.Ins().getConfigBean().getBannerItems().getSwitchInterval();
            mEnableYyw = FrontConfigManager.Ins().getConfigBean().getBanner();
        } else if (mCurrentModel == Model_WithDrawl) {
            mFrom = "Place_withdrawal";
            mYywItemList.addAll(FrontConfigManager.Ins().getConfigBean().getWithDrawalItems().getItems());
            mSwitchInterval = FrontConfigManager.Ins().getConfigBean().getWithDrawalItems().getSwitchInterval();
            mEnableYyw = FrontConfigManager.Ins().getConfigBean().getWithDrawal();
        }

        if (!mEnableYyw) {
            this.setVisibility(GONE);
            return;
        }

        this.setVisibility(VISIBLE);

        if (mYywHandler != null) {
            mYywHandler.removeCallbacksAndMessages(null);
            mYywHandler.sendEmptyMessageDelayed(MESSAGE_ID, mSwitchInterval * 1000L);
        }

        refreshYywItem();
    }

    private void refreshYywItem() {
        try {
            int nSize = mYywItemList.size();
            if (mYYWIndex < 0 || mYYWIndex >= nSize) {
                if (nSize > 0) {
                    mYYWIndex = 0;
                } else {
                    this.setVisibility(GONE);
                    return;
                }
            }

            setYywItem(mYywItemList.get(mYYWIndex));

            if (mYywHandler != null) {
                mYywHandler.removeCallbacksAndMessages(null);
                mYywHandler.sendEmptyMessageDelayed(MESSAGE_ID, mSwitchInterval * 1000L);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mYywHandler != null) {
            mYywHandler.removeCallbacksAndMessages(null);
            mYywHandler = null;
        }
    }

    private static class YywHandler extends Handler {
        private final WeakReference<YywView> mYywView;

        public YywHandler(Looper looper, YywView yywView) {
            super(looper);
            mYywView = new WeakReference<>(yywView);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            YywView yywView = mYywView.get();
            if (yywView == null) {
                return;
            }
            if (msg.what == MESSAGE_ID || msg.what == MESSAGE_SWITCH_ID) {
               yywView.mYYWIndex++;
                yywView.refreshYywItem();
            }
        }
    }
}
