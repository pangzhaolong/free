package com.module.integral.ui;

import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.AppUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.dn.sdk.bean.integral.ProxyIntegral;
import com.dn.sdk.utils.IntegralComponent;
import com.donews.common.base.MvvmBaseLiveDataActivity;
import com.donews.common.router.RouterFragmentPath;
import com.example.module_integral.R;
import com.example.module_integral.databinding.IntegralWelfareNotTaskLayoutBinding;
import com.gyf.immersionbar.ImmersionBar;
import com.module.integral.viewModel.IntegralViewModel;

import java.util.List;

//限时福利
@Route(path = RouterFragmentPath.Integral.PAGER_INTEGRAL_NOT_TASK)
public class WelfareNotTaskActivity extends MvvmBaseLiveDataActivity<IntegralWelfareNotTaskLayoutBinding, IntegralViewModel> {

    //记录此时系统运行的时间（次留）
    private long secondStayStartTime = 0;
    //适配器
    private WelfareNotTaskAdapter adapter = new WelfareNotTaskAdapter(R.layout.item_welfare_item);

    @Override
    protected int getLayoutId() {
        return R.layout.integral_welfare_not_task_layout;
    }

    @Override
    public void initView() {
        ARouter.getInstance().inject(this);
        ImmersionBar.with(this)
                .statusBarColor(R.color.white)
                .navigationBarColor(R.color.black)
                .fitsSystemWindows(true)
                .autoDarkModeEnable(true)
                .init();
        mDataBinding.titleBar.setTitle("限时福利");
        mDataBinding.welfaeList.setLayoutManager(new LinearLayoutManager(this));
        addHeadView();
        mDataBinding.welfaeList.setAdapter(adapter);
        setData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //添加头视图
    private void addHeadView(){
        View view = LayoutInflater.from(this).inflate(R.layout.item_welfare_head,null,false);
        adapter.addHeaderView(view);
    }

    private void setData() {
        IntegralComponent.getInstance().getSecondStayTask(new IntegralComponent.ISecondStayTaskListener() {
            @Override
            public void onSecondStayTask(ProxyIntegral var1) {
                showBoxLayout(var1);
            }

            @Override
            public void onError(String var1) {

            }

            @Override
            public void onNoTask() {

            }
        });
    }

    //显示宝箱 次留任务
    private void showBoxLayout(ProxyIntegral integralBean) {
        if (AppUtils.isAppInstalled(integralBean.getPkName())) {
            mDataBinding.boxLayout.setVisibility(View.VISIBLE);
            Glide.with(WelfareNotTaskActivity.this).asDrawable().load(integralBean.getIcon()).into(mDataBinding.boxIcon);
            mDataBinding.boxLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //记录此时系统运行的时间（次留）
                    secondStayStartTime = SystemClock.elapsedRealtime();
                    jumpToApk(integralBean);
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 跳转打开apk
     */
    public void jumpToApk(ProxyIntegral integralBean) {
        if (integralBean != null) {
            IntegralComponent.getInstance().runIntegralApk(WelfareNotTaskActivity.this, integralBean);
        }
    }

    /**
     * 适配器
     */
    private class WelfareNotTaskAdapter extends BaseQuickAdapter<Object, BaseViewHolder> {

        public WelfareNotTaskAdapter(int layoutResId) {
            super(layoutResId);
        }

        public WelfareNotTaskAdapter(int layoutResId, @Nullable List<Object> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(@NonNull BaseViewHolder baseViewHolder, @Nullable Object o) {

        }
    }
}
