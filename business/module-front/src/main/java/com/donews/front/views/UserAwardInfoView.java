package com.donews.front.views;

import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.donews.front.R;

public class UserAwardInfoView extends LinearLayout {
    private final ImageView mAvatarIv;
    private final TextView mAwardTv;
    private final Context mContext;

    public UserAwardInfoView(Context context) {
        super(context);
        mContext = context;

        LayoutInflater.from(context).inflate(R.layout.front_user_award_info, this, true);
        mAvatarIv = findViewById(R.id.front_gift_head_iv);
        mAwardTv = findViewById(R.id.front_gift_text);
    }

    public UserAwardInfoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mContext = context;

        LayoutInflater.from(context).inflate(R.layout.front_user_award_info, this, true);
        mAvatarIv = findViewById(R.id.front_gift_head_iv);
        mAwardTv = findViewById(R.id.front_gift_text);
    }

    public void setUserAwardInfo(String url, String name, String award) {
        Glide.with(mContext).load(url).into(mAvatarIv);
        String awardInfo = String.format(mContext.getString(R.string.front_gift_text), name, award);
        mAwardTv.setText(Html.fromHtml(awardInfo));
    }

}
