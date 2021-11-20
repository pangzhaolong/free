package com.donews.middle.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.donews.middle.R;


public class TabItem extends FrameLayout {

    private final TextView mTitleTv;
    private final View mBgView;

    public TabItem(@NonNull Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.middle_tab_item, this, true);
        mTitleTv = findViewById(R.id.middle_tab_item_title);
        mBgView = findViewById(R.id.middle_tab_item_bg);
    }

    public void setTitle(String title) {
        mTitleTv.setText(title);
        mBgView.setVisibility(GONE);
    }

    public void selected() {
        mTitleTv.setTextSize(18);
        mTitleTv.setTextColor(Color.parseColor("#F64A43"));
        mTitleTv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        mBgView.setVisibility(VISIBLE);
        Paint paint = mTitleTv.getPaint();
        float width = paint.measureText((String) mTitleTv.getText());
        LayoutParams layoutParams = (LayoutParams) mBgView.getLayoutParams();
        layoutParams.width = (int) width;
        mBgView.setLayoutParams(layoutParams);
    }

    public void unSelected() {
        mTitleTv.setTextSize(16);
        mTitleTv.setTextColor(Color.parseColor("#6D6D6D"));
        mTitleTv.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        mBgView.setVisibility(GONE);
    }

}
