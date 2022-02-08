package com.donews.middle.views;

import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;


public class YywView extends androidx.appcompat.widget.AppCompatImageView {

    private final Context mContext;

    public final static int Model_Banner = 0;
    public final static int Model_WithDrawl = 1;
    private int mCurrentModel = Model_Banner;

    private List<FrontConfigBean.YywItem> mYywItemList = new ArrayList<>();

    private int mYYWIndex = 0;

    public YywView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        this.setVisibility(GONE);
    }

    private void setYywItem(FrontConfigBean.YywItem bannerItem) {
        Glide.with(this).load(bannerItem.getImg()).into(this);
        this.setOnClickListener(v -> {
            mYYWIndex++;
            GotoUtil.doAction(mContext, bannerItem.getAction()
                    , bannerItem.getTitle(), "front");
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
        refreshYywItem();
    }

    private void refreshYywItem() {
        try {
            int nSize = mYywItemList.size();
            if (mYYWIndex < 0 || mYYWIndex >= nSize) {
                if (nSize > 0) {
                    mYYWIndex = 0;
                } else {
                    return;
                }
            }
            this.setVisibility(VISIBLE);

            LogUtil.e("显示第" + mYYWIndex + "个Banner运营位信息");
            setYywItem(mYywItemList.get(mYYWIndex));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
