package com.donews.common.views;

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

import com.donews.common.R;


public class FrontFloatingBtn extends LinearLayout {

    public static final int CRITICAL_MODEL = 0;
    public static final int RP_MODEL = 1;

    private int mCurrModel = 1;

    private final ImageView mCriticalHitIv;
    private final LinearLayout mRpLl;
    private final CircleProgressBarView mCircleProgress;
    private final TextView mProgressTv;

    public FrontFloatingBtn(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        View view = LayoutInflater.from(context).inflate(R.layout.common_floating_item, this, true);
        mCriticalHitIv = view.findViewById(R.id.common_critical_hit_iv);
        mRpLl = view.findViewById(R.id.common_rp_ll);
        mCircleProgress = view.findViewById(R.id.common_floating_progress);
        mProgressTv = view.findViewById(R.id.common_floating_tv);

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

    public void setModel(int model) {
        mCurrModel = model;
        refreshModel();
    }

    private void refreshModel() {
        if (mCurrModel == RP_MODEL) {
            mCriticalHitIv.setVisibility(GONE);
            mRpLl.setVisibility(VISIBLE);
        } else if (mCurrModel == CRITICAL_MODEL) {
            mCriticalHitIv.setVisibility(VISIBLE);
            mRpLl.setVisibility(GONE);
        }
    }
}
