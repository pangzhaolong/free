package com.module.integral.ui;

import android.view.MenuItem;
import android.view.View;
import android.widget.Toolbar;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.AppUtils;
import com.bumptech.glide.Glide;
import com.dn.sdk.bean.integral.IntegralStateListener;
import com.dn.sdk.bean.integral.ProxyIntegral;
import com.dn.sdk.utils.IntegralComponent;
import com.donews.common.router.RouterFragmentPath;
import com.example.module_integral.R;
import com.example.module_integral.databinding.IntegralWelfareLayoutBinding;
import com.module.integral.viewModel.IntegralViewModel;

import java.util.ArrayList;
import java.util.List;

//限时福利
@Route(path = RouterFragmentPath.Integral.PAGER_INTEGRAL)
public class WelfareActivity extends BaseActivity<IntegralWelfareLayoutBinding, IntegralViewModel> implements Toolbar.OnMenuItemClickListener {

    @Autowired(name = "proxyIntegral")
    public ProxyIntegral proxyIntegral;

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
        ARouter.getInstance().destroy();

    }

    private void setData() {
        IntegralComponent.getInstance().getIntegral(new IntegralComponent.IntegralHttpCallBack() {
            @Override
            public void onSuccess(ProxyIntegral integralBean) {
                //判断app是否安装
                if (AppUtils.isAppInstalled(integralBean.getPkName())) {
                    mDataBinding.downloadBt.setText("立即打开");
                } else {
                    mDataBinding.downloadBt.setText("立即下载");
                }
                refreshPage(integralBean);
            }

            @Override
            public void onError(String var1) {

            }

            @Override
            public void onNoTask() {

            }

        });


    }

    //刷新页面
    private void refreshPage(ProxyIntegral integralBean) {
        Glide.with(this).asDrawable().load(integralBean.getIcon()).into(mDataBinding.apkIcon);
        mDataBinding.apkName.setText(integralBean.getPkName());
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
                        mDataBinding.downloadBt.setText("下载中 " + (l1 / l) * 100 + "%");
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

            }

            @Override
            public void onError(Throwable throwable) {

            }
        });

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

}
