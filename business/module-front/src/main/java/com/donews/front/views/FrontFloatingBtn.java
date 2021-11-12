package com.donews.front.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.donews.front.R;

public class FrontFloatingBtn extends LinearLayout {

    private final CircleProgressBarView mCircleProgress;
    private final TextView mProgressTv;

    public FrontFloatingBtn(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        View view = LayoutInflater.from(context).inflate(R.layout.front_floating_item, this, true);
        mCircleProgress = view.findViewById(R.id.front_floating_progress);
        mProgressTv = view.findViewById(R.id.front_floating_tv);
    }

    @SuppressLint("SetTextI18n")
    public void setProgress(int progress) {
        mCircleProgress.setCurrentProgress(progress * 10);
        mProgressTv.setText(progress + "/10");
    }

}
