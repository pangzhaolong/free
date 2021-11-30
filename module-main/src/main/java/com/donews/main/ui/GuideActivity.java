package com.donews.main.ui;


import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.VibrateUtils;
import com.dn.drouter.ARouteHelper;
import com.dn.events.events.LoginLodingStartStatus;
import com.dn.events.events.LoginUserStatus;
import com.donews.base.utils.ToastUtil;
import com.donews.base.viewmodel.BaseLiveDataViewModel;
import com.donews.common.base.MvvmBaseLiveDataActivity;
import com.donews.common.router.RouterActivityPath;
import com.donews.common.services.config.ServicesConfig;
import com.donews.main.R;
import com.donews.main.databinding.MainActivityGuideBinding;
import com.donews.middle.abswitch.ABSwitch;
import com.donews.utilslibrary.analysis.AnalysisUtils;
import com.donews.utilslibrary.dot.Dot;
import com.donews.utilslibrary.utils.AppInfo;
import com.donews.utilslibrary.utils.LogUtil;
import com.gyf.immersionbar.ImmersionBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * app 引导页面
 *
 * @author darryrzhoong
 */

@Route(path = RouterActivityPath.Main.PAGER_GUIDE_ACTIVITY)
public class GuideActivity
        extends MvvmBaseLiveDataActivity<MainActivityGuideBinding, BaseLiveDataViewModel> {

    //是否需要显示引导页
    private static boolean isLoadGuide = false;

    private long fastVibrateTime = 0;
    private String loginTag = "GuideActivity";

    public static void start(Context context) {
        isLoadGuide = SPUtils.getInstance().getBoolean("mainGuideIsShow", true);
        if (isLoadGuide && !ABSwitch.Ins().isOpenAB()) {
            //需要显示引导页，去往引导页
            ARouter.getInstance().build(RouterActivityPath.Main.PAGER_GUIDE_ACTIVITY)
                    .navigation(context);
        } else {
            //不显示引导页直接去首页
            MainActivity.start(context);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        ImmersionBar.with(this)
                .statusBarColor(R.color.transparent)
                .navigationBarColor(R.color.black)
                .fitsSystemWindows(false)
                .autoDarkModeEnable(true)
                .init();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.main_activity_guide;
    }

    @Subscribe
    public void loginLoading(LoginLodingStartStatus event) {
        if (!loginTag.equals(event.getTag())) {
            return;
        }
        event.getLoginLoadingLiveData().observe(this, result -> {
            if (result == 0) {
                showLoading();
            } else {
                hideLoading();
            }
        });
    }

    @Subscribe
    public void loginStatus(LoginUserStatus event) {
        if (event.getStatus() == 1 &&
                AppInfo.checkIsWXLogin()) {
            //执行跳过的逻辑
            mDataBinding.tvGotoGuide.performClick();
        }
    }

    @Override
    public void initView() {
        mDataBinding.llMainLogin.setEnabled(false);
        mDataBinding.tvGotoGuide.setOnClickListener(v -> {
            AnalysisUtils.onEventEx(this, Dot.But_Guide_Skip);
            MainActivity.start(this);
            isLoadGuide = false;
            SPUtils.getInstance().put("mainGuideIsShow", false);
            finish();
        });
        mDataBinding.rlWachatLoginFloat.setOnClickListener(v -> {
            //檢查是否勾选协议
            if (System.currentTimeMillis() - fastVibrateTime > 1500) {
                fastVibrateTime = System.currentTimeMillis();
                VibrateUtils.vibrate(100); //震动50毫秒
            }
            ToastUtil.showShort(this, "请先同意相关协议");
            mDataBinding.llBotDesc.clearAnimation();
            Animation anim = AnimationUtils.loadAnimation(this, R.anim.anim_not_select);
            mDataBinding.llBotDesc.startAnimation(anim);
        });
        mDataBinding.llMainLogin.setOnClickListener(v -> {
            AnalysisUtils.onEventEx(this, Dot.But_Guide_WX_Login);
            RouterActivityPath.LoginProvider.getLoginProvider()
                    .loginWX(loginTag);
        });
        mDataBinding.loginCkCheckTx.setOnClickListener(v -> {
            mDataBinding.loginCkCheck.performClick();
        });
        mDataBinding.loginCkCheckLayout.setOnClickListener(v -> {
            mDataBinding.loginCkCheck.performClick();
        });
        mDataBinding.loginCkCheck.setChecked(ABSwitch.Ins().isOpenAutoAgreeProtocol());
        mDataBinding.loginCkCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                mDataBinding.llMainLogin.setEnabled(true);
                mDataBinding.rlWachatLoginFloat.setVisibility(View.GONE);
            } else {
                mDataBinding.llMainLogin.setEnabled(false);
                mDataBinding.rlWachatLoginFloat.setVisibility(View.VISIBLE);
            }
        });
        mDataBinding.tvUserXy.setOnClickListener(v -> { //用户协议
            Bundle bundle = new Bundle();
            bundle.putString("url",
                    "http://ad-static-xg.tagtic.cn/wangzhuan/file/9e5f7a06cbf80a2186e3e34a70f0c360.html");
            bundle.putString("title", "用户协议");
            ARouteHelper.routeSkip(RouterActivityPath.Web.PAGER_WEB_ACTIVITY, bundle);
        });
        mDataBinding.tvYxXy.setOnClickListener(v -> { //隐私协议
            Bundle bundle = new Bundle();
            bundle.putString("url",
                    "http://ad-static-xg.tagtic.cn/wangzhuan/file/b7f18dcb857e80eab353cfb99c3f042e.html");
            bundle.putString("title", "隐私政策");
            ARouteHelper.routeSkip(RouterActivityPath.Web.PAGER_WEB_ACTIVITY, bundle);
        });

        //动画
        mDataBinding.guideCententAnim.setImageAssetsFolder("images");
        mDataBinding.guideCententAnim.setAnimation("main_guid_data.json");
        mDataBinding.guideCententAnim.loop(true);
        mDataBinding.guideCententAnim.playAnimation();
        //手指动画
        mDataBinding.guideFingerAnim.setImageAssetsFolder("images");
        mDataBinding.guideFingerAnim.setAnimation("lottery_finger.json");
        mDataBinding.guideFingerAnim.loop(true);
        mDataBinding.guideFingerAnim.playAnimation();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}