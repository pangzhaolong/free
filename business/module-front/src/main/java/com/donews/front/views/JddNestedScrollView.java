package com.donews.front.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

public class JddNestedScrollView extends NestedScrollView {
    private boolean mIsNeedScroll = true;

    public JddNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            return mIsNeedScroll;
        }
        return super.onInterceptTouchEvent(ev);
    }

    public void setNeedScroll(boolean isNeedScroll) {
        mIsNeedScroll = isNeedScroll;
    }
}
