package com.donews.middle.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.donews.middle.R;
import com.google.android.material.imageview.ShapeableImageView;

public class LotteryBarrageItemView extends LinearLayout {
    private final ShapeableImageView mAvatarIv;
    private final TextView mAwardTv;
    private final Context mContext;

    private boolean mIsIdle = true;

    public boolean isIdle() {
        return mIsIdle;
    }

    public void setIdle(boolean isIdle) {
        this.mIsIdle = isIdle;
    }

    public LotteryBarrageItemView(Context context) {
        super(context);
        mContext = context;

        LayoutInflater.from(context).inflate(R.layout.middle_lottery_barrage_item, this, true);
        mAvatarIv = findViewById(R.id.middle_gift_head_iv);
        mAwardTv = findViewById(R.id.middle_gift_text);
    }

    public LotteryBarrageItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mContext = context;

        LayoutInflater.from(context).inflate(R.layout.middle_lottery_barrage_item, this, true);
        mAvatarIv = findViewById(R.id.middle_gift_head_iv);
        mAwardTv = findViewById(R.id.middle_gift_text);
    }

    @SuppressLint("SetTextI18n")
    public void setUserAwardInfo(String url, String name, String award) {
        if (url == null || url.equalsIgnoreCase("")) {
            mAwardTv.setText(name + award);
        } else {
            Glide.with(mContext).load(url).into(mAvatarIv);
            mAwardTv.setText(award);
        }
    }
}
