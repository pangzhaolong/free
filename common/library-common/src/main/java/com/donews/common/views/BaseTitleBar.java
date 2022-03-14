package com.donews.common.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.donews.common.R;


/**
 * @Author: honeylife
 * @CreateDate: 2020/8/6
 * @Description: 统一的titleBar
 */
public class BaseTitleBar extends RelativeLayout {
    protected TextView mTitleTextView;
    protected View translateHeader;
    private TextView mBackButton;
    private TextView mOkButton;
    private RelativeLayout mRelativeLayout;
    private ImageView mBackImage;

    public BaseTitleBar(Context context) {
        super(context);
        initView(context);
    }

    public BaseTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    protected int getLayoutId() {
        return R.layout.common_base_title_bar;
    }

    protected void initView(final Context context) {
        LayoutInflater.from(context).inflate(getLayoutId(), this);
        translateHeader = findViewById(R.id.translate_header);
        mRelativeLayout = (RelativeLayout) findViewById(R.id.title_bar_root);
        mBackButton = (TextView) findViewById(R.id.title_bar_left);
        mBackImage = ((ImageView) findViewById(R.id.title_bar_back));
        mOkButton = (TextView) findViewById(R.id.title_bar_right);
        mTitleTextView = (TextView) findViewById(R.id.title_bar_title);
        mBackImage.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (context instanceof Activity) {
                    ((Activity) context).finish();
                }
            }
        });

    }

    public void show(boolean show) {
//        mBackButton.setVisibility(show ? mBackButton.getVisibility() : GONE);
        mBackImage.setVisibility(show ? mBackImage.getVisibility() : GONE);
        mTitleTextView.setVisibility(show ? mTitleTextView.getVisibility() : GONE);
        mOkButton.setVisibility(show ? mOkButton.getVisibility() : GONE);
    }

    public View getBackGroundView() {
        return mRelativeLayout;
    }

    public void setBackTextOnClick(OnClickListener onClickListener) {
        mBackButton.setOnClickListener(onClickListener);
    }

    public void setBackOnClick(OnClickListener onClickListener) {
        mBackImage.setOnClickListener(onClickListener);
    }

    public void setBackImageView(int resId) {
        mBackImage.setImageResource(resId);
    }

    public void setSubmitOnClick(OnClickListener onClickListener) {
        mOkButton.setOnClickListener(onClickListener);
    }

    public void setSubmitTextColor(int color) {
        mOkButton.setTextColor(color);
    }


    public void setTitle(CharSequence charSequence) {
        mTitleTextView.setText(charSequence);
    }

    public void setTitleTextColor(String color) {
        mTitleTextView.setTextColor(Color.parseColor(color));
    }

    public void setSubmitButtonTextSize(float size) {

    }


    public void setTitle(int resId) {
        mTitleTextView.setText(getContext().getText(resId));
    }

    public TextView getTitleView() {
        return mTitleTextView;
    }

    public TextView getSubmitButtonText() {
        return mOkButton;
    }

    public void setSubmitButtonText(CharSequence charSequence) {
        mOkButton.setText(charSequence);
    }

    public void setSubmitButtonText(int resId) {
        mOkButton.setText(getContext().getText(resId));
    }

    public void setBackButtonText(CharSequence charSequence) {
        mBackButton.setText(charSequence);
    }

    public TextView getSubmitButton() {
        return mOkButton;
    }

    public TextView getBackButton() {
        return mBackButton;
    }


    public ImageView getBackButtonButton() {
        return mBackImage;
    }

    public void hideSubmitButton() {
        mOkButton.setVisibility(GONE);
    }

    public void hideBackButtonText() {
        mBackButton.setVisibility(GONE);
    }

    public void hideBackButton() {
        mBackImage.setVisibility(GONE);
    }

    public void hideView() {
        mRelativeLayout.setVisibility(GONE);
    }

    public void showView() {
        mRelativeLayout.setVisibility(VISIBLE);
    }

    public void showSubmitButton() {
        mOkButton.setVisibility(VISIBLE);
    }


    public void showBackButtonText() {
        mBackButton.setVisibility(VISIBLE);
    }

    public void showBackButton() {
        mBackImage.setVisibility(VISIBLE);
    }

    public void setTitleBarBackgroundColor(String resId) {
        mRelativeLayout.setBackgroundColor(Color.parseColor(resId));
    }

    public void settranslateHeaderColor(int resId) {
        translateHeader.setBackgroundColor(resId);
    }

    public void setViewVisible(boolean show) {
        mRelativeLayout.setVisibility(show ? VISIBLE : GONE);
    }

    public void anim(float percent) {
//        getBackGroundView().setBackgroundColor(ColorUtils.evaluate(percent, 0x00ffffff,
//                0xff1fbdaf));
//        mTitleTextView.setTextColor(ColorUtils.evaluate(percent, 0x00ffffff, COLOR_TITLE));
//        mTitleTextView.setBackgroundResource(R.drawable.icon_search_image);
    }
}