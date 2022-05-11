package com.donews.middle.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * @author lcl
 * Date on 2022/5/9
 * Description:
 * 带有灰色功能的容器视图
 */
public class GrayConstraintlayout extends ConstraintLayout {

    private Paint mPaint = new Paint();

    public GrayConstraintlayout(@NonNull Context context) {
        super(context);
    }

    public GrayConstraintlayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GrayConstraintlayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public GrayConstraintlayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (!enabled) {
            ColorMatrix cm = new ColorMatrix();
            cm.setSaturation(0);
            mPaint.setColorFilter(new ColorMatrixColorFilter(cm));
        } else {
            mPaint = new Paint();
        }
        invalidate();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.saveLayer(null, mPaint, Canvas.ALL_SAVE_FLAG);
        super.dispatchDraw(canvas);
        canvas.restore();
    }


    @Override
    public void draw(Canvas canvas) {
        canvas.saveLayer(null, mPaint, Canvas.ALL_SAVE_FLAG);
        super.draw(canvas);
        canvas.restore();
    }
}
