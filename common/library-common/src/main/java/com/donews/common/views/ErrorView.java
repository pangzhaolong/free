package com.donews.common.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.donews.common.R;


/**
 * @Author: honeylife
 * @CreateDate: 2020/4/20 9:57
 * @Description:
 */
public class ErrorView extends LinearLayout {
    private Button mRestartButton;
    private ISRestartCallBack mISRestartCallBack;

    public ErrorView(Context context) {
        this(context, null);
    }

    public ErrorView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ErrorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    protected int getLayoutId() {
        return R.layout.common_error_login;
    }

    protected void initView(final Context context) {
        LayoutInflater.from(context).inflate(getLayoutId(), this);
        mRestartButton = findViewById(R.id.restart_view);
        mRestartButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mISRestartCallBack == null) return;
                mISRestartCallBack.onRestartLoadUrl();
            }
        });
    }

    public void setISRestartCallBack(ISRestartCallBack mCallBack) {
        this.mISRestartCallBack = mCallBack;
    }


}
