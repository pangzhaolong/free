package com.donews.main.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.dn.sdk.bean.integral.IntegralStateListener;
import com.dn.sdk.bean.integral.ProxyIntegral;
import com.dn.sdk.utils.IntegralComponent;
import com.donews.main.R;
import com.donews.main.listener.RetentionTaskListener;
import com.donews.utilslibrary.utils.LogUtil;

import org.raphets.roundimageview.RoundImageView;

import java.util.ArrayList;
import java.util.List;

public class MainFloatingBtn extends FrameLayout implements IntegralComponent.ISecondStayTaskListener
        , IntegralComponent.IntegralHttpCallBack {

    private final Context mContext;
    private final RoundImageView mAppIcon;
    private final View mRootView;

    private RetentionTaskListener mListener;

    public MainFloatingBtn(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mContext = context;

        mRootView = LayoutInflater.from(context).inflate(R.layout.main_floating_rp, this, true);
        mAppIcon = mRootView.findViewById(R.id.main_rp_icon);

//        IntegralComponent.getInstance().getSecondStayTask(this);
//        IntegralComponent.getInstance().getIntegral(this);
    }

    public void reLoadTask() {
        IntegralComponent.getInstance().getSecondStayTask(this);
//        IntegralComponent.getInstance().getIntegralList(this);
    }

    public void setListener(RetentionTaskListener listener) {
        mListener = listener;
    }

    @Override
    public void onSecondStayTask(ProxyIntegral var1) {
        LogUtil.e("dn_integral onSecondStayTask 0");
        if (var1 == null) {
            return;
        }
        LogUtil.e("dn_integral onSecondStayTask 1");
        this.setVisibility(VISIBLE);
        List<View> views = new ArrayList<>();
        views.add(mRootView);
        Glide.with(mContext).load(var1.getIcon()).into(mAppIcon);
        IntegralComponent.getInstance().setIntegralBindView(mContext, var1
                , MainFloatingBtn.this, views, new IntegralStateListener() {
                    @Override
                    public void onAdShow() {
                        LogUtil.e("dn_integral onSecondStayTask onAdShow");
                    }

                    @Override
                    public void onAdClick() {
                        LogUtil.e("dn_integral onSecondStayTask onAdClick");
                    }

                    @Override
                    public void onStart() {
                        LogUtil.e("dn_integral onSecondStayTask onStart");
                    }

                    @Override
                    public void onProgress(long l, long l1) {
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.e("dn_integral onSecondStayTask onComplete");
                        mListener.onTaskClick(var1.getSourceRequestId(), var1.getWallRequestId());
                    }

                    @Override
                    public void onInstalled() {
                        LogUtil.e("dn_integral onSecondStayTask onInstalled");
                        mListener.onTaskClick(var1.getSourceRequestId(), var1.getWallRequestId());
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        LogUtil.e("dn_integral onSecondStayTask onError");
                    }
                }, true);
    }

    @Override
    public void onError(String var1) {
        this.setVisibility(GONE);
    }

    @Override
    public void onNoTask() {
        this.setVisibility(GONE);
    }

    @Override
    public void onSuccess(List<ProxyIntegral> var1) {
        if (var1 == null || var1.size() <= 0) {
            return;
        }
    }

    @Override
    public void onSuccess(ProxyIntegral var1) {
        LogUtil.e("dn_integral onSuccess 0");
        if (var1 == null) {
            return;
        }
        LogUtil.e("dn_integral onSuccess 1");
        this.setVisibility(VISIBLE);
        List<View> views = new ArrayList<>();
        views.add(mRootView);
        Glide.with(mContext).load(var1.getIcon()).into(mAppIcon);
        IntegralComponent.getInstance().setIntegralBindView(mContext, var1
                , MainFloatingBtn.this, views, new IntegralStateListener() {
                    @Override
                    public void onAdShow() {
                        LogUtil.e("dn_integral onSuccess onAdShow");
                    }

                    @Override
                    public void onAdClick() {
                        LogUtil.e("dn_integral onSuccess onAdClick");
                    }

                    @Override
                    public void onStart() {
                        LogUtil.e("dn_integral onSuccess onStart");
                    }

                    @Override
                    public void onProgress(long l, long l1) {
                        LogUtil.e("dn_integral onSuccess onProgress");
                    }

                    @Override
                    public void onComplete() {
                        LogUtil.e("dn_integral onSuccess onComplete");
                        mListener.onTaskClick(var1.getSourceRequestId(), var1.getWallRequestId());
                    }

                    @Override
                    public void onInstalled() {
                        LogUtil.e("dn_integral onSuccess onInstalled");
                        mListener.onTaskClick(var1.getSourceRequestId(), var1.getWallRequestId());
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        LogUtil.e("dn_integral onSuccess onError");
                    }
                }, true);
    }
}
