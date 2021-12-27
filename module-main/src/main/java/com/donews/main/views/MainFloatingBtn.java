package com.donews.main.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.dn.sdk.bean.integral.ProxyIntegral;
import com.dn.sdk.utils.IntegralComponent;
import com.donews.main.R;

import org.raphets.roundimageview.RoundImageView;

import java.util.ArrayList;
import java.util.List;

public class MainFloatingBtn extends FrameLayout implements IntegralComponent.ISecondStayTaskListener {

    private final Context mContext;
    private final RoundImageView mAppIcon;
    private final View mRootView;

    public MainFloatingBtn(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mContext = context;

        mRootView = LayoutInflater.from(context).inflate(R.layout.main_floating_rp, this, true);
        mAppIcon = mRootView.findViewById(R.id.main_rp_icon);

        IntegralComponent.getInstance().getSecondStayTask(this);
    }

    @Override
    public void onSecondStayTask(ProxyIntegral var1) {
        if (var1 == null) {
            return;
        }
        List<View> views = new ArrayList<>();
        views.add(mRootView);
        Glide.with(mContext).load(var1.getIcon()).into(mAppIcon);
        IntegralComponent.getInstance().setIntegralBindView(mContext, var1
                , MainFloatingBtn.this, views, null, true);
    }

    @Override
    public void onError(String var1) {
        this.setVisibility(GONE);
    }

    @Override
    public void onNoTask() {
        this.setVisibility(GONE);
    }
}
