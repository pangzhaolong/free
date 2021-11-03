package com.donews.front.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;

import androidx.annotation.NonNull;

import com.donews.front.R;
import com.donews.front.databinding.FrontActivityRuleDialogBinding;

public class ActivityRuleDialog extends BaseDialog<FrontActivityRuleDialogBinding> {

    public ActivityRuleDialog(Context context) {
        super(context, R.style.dialogTransparent);
    }

    @Override
    public int setLayout() {
        return R.layout.front_activity_rule_dialog;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    public float setSize() {
        return 0.9f;
    }

    @SuppressLint("RestrictedApi")
    void initView() {
        mDataBinding.frontRuleDialogHtmlTv2.setText(Html.fromHtml(getContext().getString(R.string.front_rule_2text)));
        mDataBinding.frontRuleDialogHtmlTv3.setText(Html.fromHtml(getContext().getString(R.string.front_rule_3text)));

        mDataBinding.frontRuleKnownTv.setOnClickListener(v -> {
            dismiss();
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

}