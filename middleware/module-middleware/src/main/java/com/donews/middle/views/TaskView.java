package com.donews.middle.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

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


public class TaskView extends LinearLayout {

    private final Context mContext;

    public final static int Place_Front = 0;
    public final static int Place_Mine = 1;
    private int mCurrentModel = Place_Front;

    private List<FrontConfigBean.SubItems> mYywItemList = new ArrayList<>();

    private int mYYWIndex = 0;
    private int mTaskGroup = 1;
    private final ImageView[] mImageViews = new ImageView[4];
    private final LayoutParams mLayoutParams1 = new LayoutParams(0, LayoutParams.MATCH_PARENT, 1);
    private final LayoutParams mLayoutParams2 = new LayoutParams(0, LayoutParams.MATCH_PARENT, 2);


    public TaskView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        mImageViews[0] = new ImageView(this.mContext);
        mImageViews[1] = new ImageView(this.mContext);
        mImageViews[2] = new ImageView(this.mContext);
        mImageViews[3] = new ImageView(this.mContext);
        for (ImageView iv : mImageViews) {
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
        }
    }

    public void refreshYyw(int model) {
        mCurrentModel = model;
        mYywItemList.clear();
        if (mCurrentModel == Place_Front) {
            mYywItemList.addAll(FrontConfigManager.Ins().getConfigBean().getFrontTask().getItems());
            mTaskGroup = FrontConfigManager.Ins().getConfigBean().getFrontTask().getTaskGroup();
        } else if (mCurrentModel == Place_Mine) {
            mYywItemList.addAll(FrontConfigManager.Ins().getConfigBean().getMineTask().getItems());
            mTaskGroup = FrontConfigManager.Ins().getConfigBean().getMineTask().getTaskGroup();
        }

        initViews();

        refreshYywItem();
    }

    private void initViews() {
        this.removeAllViews();
        for (ImageView iv : mImageViews) {
            iv.setLayoutParams(mLayoutParams1);
        }
        switch (mTaskGroup) {
            case 0:
                this.setWeightSum(4);
                this.addView(mImageViews[0]);
                this.addView(mImageViews[1]);
                this.addView(mImageViews[2]);
                this.addView(mImageViews[3]);
                break;
            case 3:
                this.setWeightSum(4);
                mImageViews[0].setLayoutParams(mLayoutParams2);
                this.addView(mImageViews[0]);
                this.addView(mImageViews[1]);
                this.addView(mImageViews[2]);
                break;
            case 4:
                this.setWeightSum(4);
                this.addView(mImageViews[0]);
                this.addView(mImageViews[1]);
                mImageViews[2].setLayoutParams(mLayoutParams2);
                this.addView(mImageViews[2]);
                break;
            case 1:
                this.setWeightSum(2);
                this.addView(mImageViews[0]);
                this.addView(mImageViews[1]);
                break;
            case 2:
                this.setWeightSum(1);
                this.addView(mImageViews[0]);
                break;
        }
        this.measure(getMeasuredWidth(), getMeasuredHeight());
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
            setYywItem();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setYywItem() {
        switch (mTaskGroup) {
            case 0:
                Glide.with(this).load(mYywItemList.get(0).getSubItems().get(mYYWIndex).getImg()).into(mImageViews[0]);
                Glide.with(this).load(mYywItemList.get(1).getSubItems().get(mYYWIndex).getImg()).into(mImageViews[1]);
                Glide.with(this).load(mYywItemList.get(2).getSubItems().get(mYYWIndex).getImg()).into(mImageViews[2]);
                Glide.with(this).load(mYywItemList.get(3).getSubItems().get(mYYWIndex).getImg()).into(mImageViews[3]);
                break;
            case 1:
                Glide.with(this).load(mYywItemList.get(0).getSubItems().get(mYYWIndex).getImg()).into(mImageViews[0]);
                Glide.with(this).load(mYywItemList.get(1).getSubItems().get(mYYWIndex).getImg()).into(mImageViews[1]);
                break;
            case 2:
                Glide.with(this).load(mYywItemList.get(0).getSubItems().get(mYYWIndex).getImg()).into(mImageViews[0]);
                break;
            case 3:
            case 4:
                Glide.with(this).load(mYywItemList.get(0).getSubItems().get(mYYWIndex).getImg()).into(mImageViews[0]);
                Glide.with(this).load(mYywItemList.get(1).getSubItems().get(mYYWIndex).getImg()).into(mImageViews[1]);
                Glide.with(this).load(mYywItemList.get(2).getSubItems().get(mYYWIndex).getImg()).into(mImageViews[2]);
                break;
        }
        this.setOnClickListener(v -> {
            GotoUtil.doAction(mContext, mYywItemList.get(0).getSubItems().get(mYYWIndex).getAction()
                    , mYywItemList.get(0).getSubItems().get(mYYWIndex).getTitle(), "front");
            AnalysisUtils.onEventEx(mContext, Dot.BANNER_CLICK);
            refreshYywItem();
        });
    }
}
