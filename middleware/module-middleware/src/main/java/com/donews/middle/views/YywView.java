package com.donews.middle.views;

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
import com.donews.utilslibrary.utils.LogUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


public class YywView extends androidx.appcompat.widget.AppCompatImageView {

    private static final int MESSAGE_ID = 10001;
    private final Context mContext;

    public final static int Model_Banner = 0;
    public final static int Model_WithDrawl = 1;
    private int mCurrentModel = Model_Banner;

    private final YywHandler mYywHandler;

    private List<FrontConfigBean.YywItem> mYywItemList = new ArrayList<>();

    private int mYYWIndex = 0;

    public YywView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        this.setVisibility(GONE);

        mYywHandler = new YywHandler(Looper.getMainLooper(), this);
    }

    private void setYywItem(FrontConfigBean.YywItem bannerItem) {
        Glide.with(this).load(bannerItem.getImg()).into(this);
        this.setOnClickListener(v -> {
            GotoUtil.doAction(mContext, bannerItem.getAction(), bannerItem.getTitle(), "front");
            AnalysisUtils.onEventEx(mContext, Dot.BANNER_CLICK);
            refreshYywItem();
        });
    }

    public void refreshYyw(int model) {
        mCurrentModel = model;
        mYywItemList.clear();
        if (mCurrentModel == Model_Banner) {
            mYywItemList.addAll(FrontConfigManager.Ins().getConfigBean().getBannerItems());
        } else if (mCurrentModel == Model_WithDrawl) {
            mYywItemList.addAll(FrontConfigManager.Ins().getConfigBean().getWithDrawalItems());
        }

        if (mYywHandler != null) {
            mYywHandler.removeCallbacksAndMessages(null);
            mYywHandler.sendEmptyMessageDelayed(MESSAGE_ID, 5*1000);
        }

        refreshYywItem();
    }

    private void refreshYywItem() {
        try {
            mYYWIndex++;
            int nSize = mYywItemList.size();
            if (mYYWIndex < 0 || mYYWIndex >= nSize) {
                if (nSize > 0) {
                    mYYWIndex = 0;
                } else {
                    this.setVisibility(GONE);
                    return;
                }
            }
            this.setVisibility(VISIBLE);

            setYywItem(mYywItemList.get(mYYWIndex));

            if (mYywHandler != null) {
                mYywHandler.removeCallbacksAndMessages(null);
                mYywHandler.sendEmptyMessageDelayed(MESSAGE_ID, 5*1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
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
            if (msg.what == MESSAGE_ID) {
                YywView yywView = mYywView.get();
                if (yywView != null) {
                    yywView.refreshYywItem();
                }
            }
        }
    }
}
