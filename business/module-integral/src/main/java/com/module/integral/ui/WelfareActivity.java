package com.module.integral.ui;

import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toolbar;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.dn.sdk.bean.integral.IntegralStateListener;
import com.dn.sdk.bean.integral.ProxyIntegral;
import com.dn.sdk.utils.IntegralComponent;
import com.donews.common.router.RouterFragmentPath;
import com.example.module_integral.R;
import com.example.module_integral.databinding.IntegralWelfareLayoutBinding;
import com.module.integral.viewModel.IntegralViewModel;
import com.module.lottery.dialog.LessMaxDialog;

import java.lang.ref.WeakReference;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

//限时福利
@Route(path = RouterFragmentPath.Integral.PAGER_INTEGRAL)
public class WelfareActivity extends BaseActivity<IntegralWelfareLayoutBinding, IntegralViewModel> implements Toolbar.OnMenuItemClickListener {

    private static final int DELAY = 1000;

    @Autowired(name = "proxyIntegral")
    public ProxyIntegral proxyIntegral;
    private WelfareHandler mWelfareHandler = new WelfareHandler(this);
    //自动跳转的时间
    private int jumpTime = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.integral_welfare_layout;
    }

    @Override
    public void initView() {
        setSupportActionBar(mDataBinding.toolbar);
        ARouter.getInstance().inject(this);
        setData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void setData() {
        IntegralComponent.getInstance().getIntegral(new IntegralComponent.IntegralHttpCallBack() {
            @Override
            public void onSuccess(ProxyIntegral integralBean) {
                //判断app是否安装
                if (AppUtils.isAppInstalled(integralBean.getPkName())) {
                    refreshPageView(integralBean);
                } else {
                    mDataBinding.downloadBt.setText("立即下载");
                    mDataBinding.tipsLayout.setVisibility(View.GONE);
                }
                refreshPage(integralBean);
            }

            @Override
            public void onError(String var1) {

            }

            @Override
            public void onNoTask() {
                ToastUtils.showShort("无积分任务");
            }

        });
    }

    //开始自动跳转倒计时
    private void startAutomaticCountdown(ProxyIntegral integralBean) {
        jumpTime = 3;
        Message message = new Message();
        message.what = 1;
        message.obj = integralBean;
        mWelfareHandler.sendMessage(message);
    }


    private void refreshPageView(ProxyIntegral integralBean) {
        if (AppUtils.isAppInstalled(integralBean.getPkName())) {
            mDataBinding.downloadBt.setText("立即打开");
            Glide.with(WelfareActivity.this).asDrawable().load(integralBean.getIcon()).into(mDataBinding.apkTipsIcon);
            mDataBinding.apkTipsName.setText(integralBean.getAppName() + "安装成功啦");
            mDataBinding.apkTipsTime.setText("赶紧打开体验12S后自动跳转");
            //开始倒计时自动跳转
            startAutomaticCountdown(integralBean);
            mDataBinding.tipsLayout.setVisibility(View.VISIBLE);
            mDataBinding.tipsLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    jumpToApk(integralBean);
                }
            });

        }
    }


    /**
     * 跳转打开apk
     */
    public void jumpToApk(ProxyIntegral integralBean) {
        if (integralBean != null) {
            mWelfareHandler.removeMessages(0);
            mWelfareHandler.removeMessages(1);
            IntegralComponent.getInstance().runIntegralApk(WelfareActivity.this, integralBean);
        }
    }


    //刷新页面
    private void refreshPage(ProxyIntegral integralBean) {
        Glide.with(this).asDrawable().load(integralBean.getIcon()).into(mDataBinding.apkIcon);
        mDataBinding.apkName.setText(integralBean.getAppName());
        List<View> clickViews = new ArrayList<>();
        clickViews.add(mDataBinding.downloadBt);
        IntegralComponent.getInstance().setIntegralBindView(WelfareActivity.this, integralBean, mDataBinding.adView, clickViews, new IntegralStateListener() {
            @Override
            public void onAdShow() {

            }

            @Override
            public void onAdClick() {

            }

            @Override
            public void onStart() {

            }

            @Override
            public void onProgress(long l, long l1) {
                mDataBinding.downloadBt.post(new Runnable() {
                    @Override
                    public void run() {
                        if (l != 0) {
                            float value = (float) (Float.valueOf(l1) / Float.valueOf(l)) * 100f;
                            DecimalFormat df = new DecimalFormat("0.00");
                            df.setRoundingMode(RoundingMode.HALF_UP);
                            mDataBinding.downloadBt.setText("下载中 " + df.format(value) + "%");
                        }
                    }
                });

            }

            @Override
            public void onComplete() {
                mDataBinding.downloadBt.post(new Runnable() {
                    @Override
                    public void run() {
                        mDataBinding.downloadBt.setText("立即安装");
                    }
                });

            }

            @Override
            public void onInstalled() {
                refreshPageView(integralBean);
            }

            @Override
            public void onError(Throwable throwable) {

            }
        }, true);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }


    private static class WelfareHandler extends Handler {
        private WeakReference<WelfareActivity> reference;   //

        WelfareHandler(WelfareActivity context) {
            reference = new WeakReference(context);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (reference.get() != null && !reference.get().isDestroyed()) {
                        if (reference.get().jumpTime <= 0) {
                            reference.get().jumpTime = 0;
                            //倒计时结束，开始自动跳转
                            reference.get().jumpToApk((ProxyIntegral) (msg.obj));
                            reference.get().mDataBinding.tipsLayout.setVisibility(View.INVISIBLE);

                        } else {
                            //继续倒计时，更新UI
                            reference.get().jumpTime = reference.get().jumpTime -= 1;
                            reference.get().mDataBinding.apkTipsTime.setText("赶紧打开体验" + reference.get().jumpTime + "S后自动跳转");
                            Message message = new Message();
                            message.what = 1;
                            message.obj = msg.obj;
                            sendMessageDelayed(message, DELAY);
                        }
                    }
                    break;
            }
        }
    }


}
