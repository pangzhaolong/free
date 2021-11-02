package com.module.lottery.behavior;

import android.content.Context;
import android.graphics.Color;
import android.provider.CalendarContract;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

import com.module_lottery.R;
import com.orhanobut.logger.Logger;

public class progressBehavior extends CoordinatorLayout.Behavior {

    int mProgressMarginStart;

    public progressBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        //读取dimen配置参数
        mProgressMarginStart = context.getResources().getDimensionPixelSize(R.dimen.margin_start);


    }


    @Override
    public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull View dependency) {
        return dependency instanceof ImageView;
    }

    @Override
    public boolean onDependentViewChanged(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull View dependency) {
        try {
            int offset = dependency.getLeft();
            //判断下面的三角形位移的距离是否大于等于child 的中间位置
            //获取child的中间位置
            int centerLocal = child.getLeft() + mProgressMarginStart;
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
            int maxValue = parent.getWidth();
//        Logger.d("########## maxValue" +   maxValue + "  child.getRight() "+child.getRight());
            if (offset >= centerLocal && child.getRight() < maxValue) {
                layoutParams.leftMargin = offset - mProgressMarginStart;
                child.setLayoutParams(layoutParams);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }
}
