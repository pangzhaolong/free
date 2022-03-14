package com.module.lottery.utils;

import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ScrollLinearLayoutManager extends LinearLayoutManager {
    private float MILLISECONDS_PER_INCH = 1.3f;
    private Context mContext;

    public ScrollLinearLayoutManager(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        TopLinearSmoothScroller topLinearSmoothScroller = new TopLinearSmoothScroller(mContext) {
            @Nullable
            @Override
            public PointF computeScrollVectorForPosition(int targetPosition) {
                return ScrollLinearLayoutManager.this
                        .computeScrollVectorForPosition(targetPosition);
            }

            @Override
            protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                return MILLISECONDS_PER_INCH / displayMetrics.density;
            }
        };
        //定位到顶部
        topLinearSmoothScroller.setTargetPosition(position);
        startSmoothScroll(topLinearSmoothScroller);
    }
}
