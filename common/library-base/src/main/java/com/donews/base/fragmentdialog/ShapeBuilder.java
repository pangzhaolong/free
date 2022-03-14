package com.donews.base.fragmentdialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.TypedValue;

/**
 * @author by SnowDragon
 * Date on 2020/12/16
 * Description:
 */
public class ShapeBuilder {
    private int cornerRadius;
    private int bkColor;
    private Context mContext;
    private static final int DEFAULT_BACKGROUND = 0XFF333333;

    public Drawable build(Context context) {
        this.mContext = context;
        final GradientDrawable bg = new GradientDrawable();
        // bg.setStroke(lineWidth,lineColor,dashWidth,dashGap);
        bg.setCornerRadius(cornerRadius);
        bg.setColor(bkColor == 0 ? DEFAULT_BACKGROUND : bkColor);
        return bg;
    }

    public ShapeBuilder setRadius(float cornerRadius) {
        this.cornerRadius = dp2px(mContext, cornerRadius);
        return this;
    }

    public ShapeBuilder setBackgroundColor(int color) {
        bkColor = color;
        return this;
    }

    public ShapeBuilder setBackgroundColor(String color) {
        bkColor = Color.parseColor(color);
        return this;
    }


    /**
     * dp转px
     *
     * @param context
     * @param dpVal
     * @return pxVal
     */
    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }


}
