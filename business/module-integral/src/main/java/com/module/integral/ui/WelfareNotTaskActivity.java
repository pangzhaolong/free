package com.module.integral.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.AppUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.dn.drouter.ARouteHelper;
import com.dn.sdk.bean.integral.ProxyIntegral;
import com.dn.sdk.utils.IntegralComponent;
import com.donews.base.utils.ToastUtil;
import com.donews.base.utils.glide.GlideUtils;
import com.donews.common.base.MvvmBaseLiveDataActivity;
import com.donews.common.router.RouterActivityPath;
import com.donews.common.router.RouterFragmentPath;
import com.donews.middle.dialog.ActivityRuleDialog;
import com.donews.middle.dialog.IntegralRuleDialog;
import com.donews.utilslibrary.utils.JsonUtils;
import com.module.integral.down.DownApkUtil;
import com.donews.network.BuildConfig;
import com.donews.network.EasyHttp;
import com.donews.network.cache.model.CacheMode;
import com.donews.network.callback.SimpleCallBack;
import com.donews.network.exception.ApiException;
import com.example.module_integral.R;
import com.example.module_integral.databinding.IntegralWelfareNotTaskLayoutBinding;
import com.gyf.immersionbar.ImmersionBar;
import com.module.integral.bean.AppWallBean;
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
    IntegralRuleDialog mActivityRuleDialog;
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
        mDataBinding.rule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mActivityRuleDialog == null) {
                    mActivityRuleDialog = new IntegralRuleDialog(WelfareNotTaskActivity.this);
                }
                mActivityRuleDialog.show();
            }
        });
        setData();
        getTaskList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mActivityRuleDialog != null && mActivityRuleDialog.isShowing()) {
            mActivityRuleDialog.dismiss();
        }
    }

    //添加头视图
    private void addHeadView() {
        View view = LayoutInflater.from(this).inflate(R.layout.item_welfare_head, null, false);
        adapter.addHeaderView(view);
    }

    private void setData() {
//        IntegralComponent.getInstance().getSecondStayTask(new IntegralComponent.ISecondStayTaskListener() {
//            @Override
//            public void onSecondStayTask(ProxyIntegral var1) {
//                showBoxLayout(var1);
//            }
//
//            @Override
//            public void onError(String var1) {
//
//            }
//
//            @Override
//            public void onNoTask() {
//
//            }
//        });
    }

    //显示宝箱 次留任务
//    private void showBoxLayout(ProxyIntegral integralBean) {
//        if (AppUtils.isAppInstalled(integralBean.getPkName())) {
//            mDataBinding.boxLayout.setVisibility(View.VISIBLE);
//            Glide.with(WelfareNotTaskActivity.this).asDrawable().load(integralBean.getIcon()).into(mDataBinding.boxIcon);
//            mDataBinding.boxLayout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //记录此时系统运行的时间（次留）
//                    secondStayStartTime = SystemClock.elapsedRealtime();
//                    jumpToApk(integralBean);
//                }
//            });
//        }
//    }

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

    //获取任务
    private void getTaskList() {
        EasyHttp.get(BuildConfig.BASE_CONFIG_URL + "plus-appsWall" + com.donews.common.BuildConfig.BASE_RULE_URL
                + JsonUtils.getCommonJson(false))
                .cacheMode(CacheMode.NO_CACHE)
                .isShowToast(false)
                .execute(new SimpleCallBack<AppWallBean>() {
                    @Override
                    public void onError(ApiException e) {
                        ToastUtil.showShort(WelfareNotTaskActivity.this, "获取任务异常");
                    }

                    @Override
                    public void onSuccess(AppWallBean appWallBean) {
                        if (appWallBean.apps != null) {
                            adapter.setNewData(appWallBean.apps);
                        }
                    }
                });
    }

    /**
     * 适配器
     */
    private class WelfareNotTaskAdapter extends BaseQuickAdapter<AppWallBean.AppWallBeanItem, BaseViewHolder> {

        DownApkUtil downApkUtil = new DownApkUtil();

        public WelfareNotTaskAdapter(int layoutResId) {
            super(layoutResId);
        }

        public WelfareNotTaskAdapter(int layoutResId, @Nullable List<AppWallBean.AppWallBeanItem> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(@NonNull BaseViewHolder baseViewHolder, @Nullable AppWallBean.AppWallBeanItem item) {
            ImageView icon = baseViewHolder.findView(R.id.iv_down_icon);
            TextView down = baseViewHolder.findView(R.id.tv_down);
            GlideUtils.loadImageView(WelfareNotTaskActivity.this, item.icon, icon);
            baseViewHolder.setText(R.id.tv_down_title, item.title)
                    .setText(R.id.tv_down_desc, item.desc);

            if ("install".equals(item.action)) {
                if (AppUtils.isAppInstalled(item.pkgName)) {
                    down.setText("打开");
                } else if (downApkUtil.checkIsDownApk(item.linkUrl)) {
                    down.setText("安装");
                } else {
                    down.setText("下载");
                }
            } else {
                down.setText("查看");
            }
            down.setOnClickListener(v -> {
                if ("install".equals(item.action)) {
                    if (AppUtils.isAppInstalled(item.pkgName)) {
                        AppUtils.launchApp(item.pkgName);
                    } else {
                        if (downApkUtil.checkIsDownApk(item.linkUrl)) {
                            AppUtils.installApp(downApkUtil.getDownApkFile(item.linkUrl));
                            return;
                        }
//                        showLoading("准备中");
//                        Glide.with(WelfareNotTaskActivity.this)
//                                .asBitmap()
//                                .load(item.icon)
//                                .into(new SimpleTarget<Bitmap>() {
//                                    @Override
//                                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                                        hideLoading();
//                                    }
//                                });
                        downApkUtil.downApk(item.title, item.linkUrl, null, WelfareNotTaskActivity.this.getLifecycle(),
                                (downApkCallBeanBean, integer) -> {
                                    if (integer < 0) {
                                        down.setText("重试");
                                    } else {
                                        if (integer < 100) {
                                            down.setText("下载.." + integer + "%");
                                        } else {
                                            down.setText("安装");
                                        }
                                    }
                                    return null;
                                }
                        );
                    }
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("url", item.linkUrl);
                    bundle.putString("title", item.title);
                    ARouteHelper.routeSkip(RouterActivityPath.Web.PAGER_WEB_ACTIVITY, bundle);
                }
            });
        }
    }
}
