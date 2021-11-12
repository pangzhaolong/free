package com.donews.middle.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.donews.middle.R;


public class TabItemEx extends FrameLayout {

    private final TextView mTitleTv;
    private final ImageView mLogoView;

    public TabItemEx(@NonNull Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.middle_tab_item_ex, this, true);
        mTitleTv = findViewById(R.id.middle_tab_item_title);
        mLogoView = findViewById(R.id.middle_tab_item_logo);
    }

    public void init(int position) {
        switch (position) {
            case 0:
                mLogoView.setBackgroundResource(R.drawable.middle_item_tb);
                mTitleTv.setText("淘宝");
                break;
            case 1:
                mLogoView.setBackgroundResource(R.drawable.middle_item_pdd);
                mTitleTv.setText("拼多多");
                break;
            case 2:
                mLogoView.setBackgroundResource(R.drawable.middle_item_jd);
                mTitleTv.setText("京东");
                break;
        }
    }

    public void selected() {
//        mTitleTv.setTextSize(18);
        mTitleTv.setTextColor(Color.parseColor("#2C2C2C"));
        mTitleTv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        mLogoView.setVisibility(VISIBLE);
        /*Paint paint = mTitleTv.getPaint();
        float width = paint.measureText((String) mTitleTv.getText());
        LayoutParams layoutParams = (LayoutParams) mBgView.getLayoutParams();
        layoutParams.width = (int) width;
        mBgView.setLayoutParams(layoutParams);*/
    }

    public void unSelected() {
//        mTitleTv.setTextSize(16);
        mTitleTv.setTextColor(Color.parseColor("#6D6D6D"));
        mTitleTv.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        mLogoView.setVisibility(GONE);
    }

}
