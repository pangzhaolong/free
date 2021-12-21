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
import com.google.android.material.imageview.ShapeableImageView;

public class LotteryBarrageItemView extends LinearLayout {
    private final ShapeableImageView mAvatarIv;
    private final TextView mAwardTv;
    private final Context mContext;

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

        LayoutInflater.from(context).inflate(R.layout.middle_award_info, this, true);
        mAvatarIv = findViewById(R.id.middle_gift_head_iv);
        mAwardTv = findViewById(R.id.middle_gift_text);
    }

    public void setUserAwardInfo(String url, String name, String award) {
        Glide.with(mContext).load(url).into(mAvatarIv);
        String awardInfo = String.format(mContext.getString(R.string.middle_lottery_barrage_item_text), award);
        mAwardTv.setText(Html.fromHtml(awardInfo));
    }

}
