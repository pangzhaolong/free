package com.donews.notify.launcher.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.Nullable;

/**
 * @author lcl
 * Date on 2022/3/15
 * Description:
 */
@SuppressLint("AppCompatCustomView")
public class ExtImageView extends ImageView {
    public ExtImageView(Context context) {
        super(context);
    }

    public ExtImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ExtImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ExtImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
