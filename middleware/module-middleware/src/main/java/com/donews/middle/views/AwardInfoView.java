package com.donews.middle.views;

import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.donews.middle.R;

public class AwardInfoView extends LinearLayout {
    private TextView mAwardTv;
    private Context mContext;
    private TextView mUserName_;

    public AwardInfoView(Context context) {
        super(context, null);
    }

    public AwardInfoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.middle_award_info, this, true);
        mAwardTv = findViewById(R.id.middle_text);
        mUserName_ = findViewById(R.id.user_name);
    }

    public void setUserAwardInfo(String name, String award) {
        if (name != null) {
            mUserName_.setText(name);
            mAwardTv.setText(award);
        }
    }

}
