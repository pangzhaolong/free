package com.dn.sdk.widget;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.dn.sdk.R;
import com.dn.sdk.widget.progressbtn.ProgressButton;

/**
 * @author by SnowDragon
 * Date on 2021/3/29
 * Description:
 */
public class IntegralView extends FrameLayout {

    public IntegralView(Context context) {
        this(context, null);

    }

    public IntegralView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IntegralView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        setOrientation(VERTICAL);
//        setPadding(0, DensityUtils.dip2px(8), 0, DensityUtils.dip2px(8));
//        init(context);
        LayoutInflater.from(context).inflate(R.layout.sdk_intergal_ad, this);

        progressButton = findViewById(R.id.pro_btn);
        ivLogo = findViewById(R.id.iv_app_logo);
        tvAppName = findViewById(R.id.tv_app_name);
        tvHint = findViewById(R.id.tv_hint);

    }

    TextView tvAppName;
    ImageView ivLogo;
    ProgressButton progressButton;
    TextView tvHint;

    public void setAppName(String appName) {
        tvAppName.setText(appName);
    }

    public void setAward(String award) {
        if (TextUtils.isEmpty(award)) {
            award = "0.3";
        }
        String content = String.format("试用APP30秒，赚取%s元", award);
        int index1 = content.indexOf("30");
        int index2 = content.indexOf(award);
        SpannableString span = new SpannableString(content);
        span.setSpan(new ForegroundColorSpan(0XFFFF5A00), index1, index1 + 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new ForegroundColorSpan(0XFFFF5A00), index2, index2 + award.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvHint.setText(span);
    }

    public ImageView getIvLogo() {
        return ivLogo;
    }

    public ProgressButton getTextBtn() {
        return progressButton;
    }

    public TextView getTvAppName() {
        return tvAppName;
    }

    private void init(Context context) {
        //奖励
//        tvAward = new TextView(context);
//        tvAward.setTextColor(0xff000000);
//        tvAward.getPaint().setFakeBoldText(true);
//        tvAward.setTextSize(18);
//        tvAward.setText("听一首歌，赚0.3元");
//        LinearLayout.LayoutParams paramsAward = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        paramsAward.gravity = Gravity.CENTER_HORIZONTAL;
//        tvAward.setLayoutParams(paramsAward);
//
//        addView(tvAward);

        //提示
//        tvHint = new TextView(context);
//        tvHint.setTextColor(Color.GRAY);
//        tvHint.getPaint().setFakeBoldText(true);
//        tvHint.setTextSize(18);
//        LinearLayout.LayoutParams paramsHint = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        paramsHint.topMargin = 30;
//        paramsHint.gravity = Gravity.CENTER_HORIZONTAL;
//        tvHint.setLayoutParams(paramsHint);
//        tvHint.setTextSize(18);
//        tvHint.setText("下载达人养猪场");
//        addView(tvHint);


        //下载
    /*
                <com.xiaochen.progressroundbutton.AnimDownloadProgressButton
        android:focusableInTouchMode="true"
        android:focusable="true"
        app:progressbtn_text_size="25"
        android:id="@+id/anim_btn"
        android:layout_width="match_parent"
        android:layout_height="40dp"
        android:textColor="@android:color/black"
        android:background="@drawable/bg_button"
        app:progressbtn_text_color="@android:color/black"
        app:progressbtn_background_color="@android:color/holo_blue_dark"
        app:progressbtn_background_second_color="@android:color/darker_gray"/>*/
//
//        tvBtn = new ProgressButton(context);
//        tvBtn.setTextColor(Color.RED);
//        tvBtn.getPaint().setFakeBoldText(true);
//        tvBtn.setTextSize(18);
//
//        LinearLayout.LayoutParams paramBtn = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        paramBtn.gravity = Gravity.CENTER_HORIZONTAL;
//        paramBtn.topMargin = 30;
//        paramBtn.width = DensityUtils.dip2px(135);
//        paramBtn.height = DensityUtils.dip2px(43);
//        tvBtn.setLayoutParams(paramBtn);
//        tvBtn.setBackgroundResource(R.drawable.sdk_integral_ad_btn);
//        tvBtn.setProgressBtnBackgroundColor(0xffff4444);
//        tvBtn.setProgressBtnBackgroundSecondColor(0xffffffff);
//        tvBtn.setTextColor(Color.BLACK);
//        tvBtn.setTextCoverColor(Color.BLACK);
//        tvBtn.setCurrentText("下载");
//        tvBtn.setGravity(Gravity.CENTER);
//        tvBtn.setTextSize(getResources().getDimension(R.dimen.integral_ad_btn_font_seize));
//
//        addView(tvBtn);
    }

//    public void setAward(String awardContent) {
//        if (tvAward != null) {
//            tvAward.setText(awardContent);
//        }
//    }
//
//    public void setHint(String hint) {
//        if (tvHint != null) {
//            tvHint.setText(hint);
//        }
//    }
//
//    public void setBtnText(String btnStr) {
//        if (tvBtn != null) {
//            tvBtn.setText(btnStr);
//        }
//    }


}
