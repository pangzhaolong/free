package com.donews.mine.views;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.donews.mine.R;
import com.donews.mine.bean.CSBean;

public class CCSItemView extends FrameLayout {
    private TextView mCityTv;
    private TextView mSpeedTv;

    public CCSItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.mine_ccs_item, this, true);
        mCityTv = this.findViewById(R.id.mine_ccs_city);
        mSpeedTv = this.findViewById(R.id.mine_ccs_speed);
    }

    public void setCityAndSpeed(CSBean csBean) {
        mCityTv.setText(csBean.city);
        SpannableString spannableString = new SpannableString(csBean.speed);
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#E9423E"));
        spannableString.setSpan(foregroundColorSpan, spannableString.length()-1, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        mSpeedTv.setText(spannableString);
//        invalidate();
    }
}
