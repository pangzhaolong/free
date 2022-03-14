package com.module.lottery.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class OptionalLinearGradient extends LinearLayout {
    Paint mPaint;

    public OptionalLinearGradient(Context context) {
        this(context,null);
    }

    public OptionalLinearGradient(Context context, @Nullable AttributeSet attrs) {
        this(context,null,0);
    }

    public OptionalLinearGradient(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint=new Paint();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        LinearGradient linearGradient = new LinearGradient(0,0,getMeasuredWidth()/2,getMeasuredHeight()/2, Color.RED,Color.GREEN, Shader.TileMode.MIRROR);
        mPaint.setShader(linearGradient);
        canvas.drawRect(0,0,getMeasuredWidth(),getMeasuredHeight(),mPaint);

    }
}
