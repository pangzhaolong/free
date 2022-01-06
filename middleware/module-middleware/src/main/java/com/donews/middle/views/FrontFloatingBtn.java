package com.donews.middle.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.donews.common.views.CircleProgressBarView;
import com.donews.middle.R;
import com.donews.middle.bean.front.FrontConfigBean;
import com.donews.middle.go.GotoUtil;


public class FrontFloatingBtn extends LinearLayout {

    public static final int CRITICAL_MODEL = 0;
    public static final int RP_MODEL = 1;
    public static final int YYW_MODEL = 2;

    private int mCurrModel = CRITICAL_MODEL;

    private Context mContext;

    private final LottieAnimationView mCriticalHitIv;
    private final LinearLayout mRpLl;
    private final CircleProgressBarView mCircleProgress;
    private final TextView mProgressTv;

    private final ImageView mYywImageView;

    public FrontFloatingBtn(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mContext = context;

        View view = LayoutInflater.from(context).inflate(R.layout.middle_floating_item, this, true);
        mCriticalHitIv = view.findViewById(R.id.common_critical_hit_iv);
        mRpLl = view.findViewById(R.id.common_rp_ll);
        mCircleProgress = view.findViewById(R.id.common_floating_progress);
        mProgressTv = view.findViewById(R.id.common_floating_tv);

        mYywImageView = view.findViewById(R.id.common_critical_yyw);

        refreshModel();
    }

    @SuppressLint("SetTextI18n")
    public void setProgress(int progress) {
        if (progress == -2) {
            mCircleProgress.setCurrentProgress(100);
            mProgressTv.setText("明日再来");
            return;
        }
        int steps = progress == -1 ? 10 : progress;
        mCircleProgress.setCurrentProgress(steps * 10);
        mProgressTv.setText(steps + "/10");
    }

    public void showCriticalBtn() {
        setModel(CRITICAL_MODEL);
    }

    public void setModel(int model) {
        mCurrModel = model;
        refreshModel();
    }

    private void refreshModel() {
        if (mCriticalHitIv.getAnimation() != null) {
            mCriticalHitIv.cancelAnimation();
            mCriticalHitIv.clearAnimation();
        }
        if (mCurrModel == RP_MODEL) {
            mCriticalHitIv.setVisibility(GONE);
            mYywImageView.setVisibility(GONE);
            mRpLl.setVisibility(VISIBLE);
        } else if (mCurrModel == CRITICAL_MODEL) {
            mCriticalHitIv.setVisibility(VISIBLE);
            mRpLl.setVisibility(GONE);
            mYywImageView.setVisibility(GONE);
            initLottie(mCriticalHitIv);
        } else if (mCurrModel == YYW_MODEL) {
            mCriticalHitIv.setVisibility(GONE);
            mRpLl.setVisibility(GONE);
            mYywImageView.setVisibility(VISIBLE);
        }
    }

    private void initLottie(LottieAnimationView view) {
        if ((view != null && !view.isAnimating())) {
            view.setImageAssetsFolder("images");
            view.clearAnimation();
            view.setAnimation("middle_critical_ani.json");
            view.loop(true);
            view.playAnimation();
        }
    }

    public void setYywInfo(FrontConfigBean.FloatingItem floatingItem) {
        setModel(YYW_MODEL);
        if (this.isShown()) {
            Glide.with(this).load(floatingItem.getImg()).into(mYywImageView);
        }
        this.setOnClickListener(v -> GotoUtil.doAction(mContext, floatingItem.getAction()
                , floatingItem.getTitle(), "front"));
    }
}
