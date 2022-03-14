package com.module.lottery.utils;

import android.content.Context;
import android.graphics.PointF;
import android.util.DisplayMetrics;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearSmoothScroller;

public class TopLinearSmoothScroller extends LinearSmoothScroller {
    public TopLinearSmoothScroller(Context context) {
        super(context);
    }
    @Override
    public int getVerticalSnapPreference() {
        return SNAP_TO_START;
    }

    @Nullable
    @Override
    public PointF computeScrollVectorForPosition(int targetPosition) {
        return super.computeScrollVectorForPosition(targetPosition);
    }

    @Override
    protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
        return super.calculateSpeedPerPixel(displayMetrics);
    }
}
