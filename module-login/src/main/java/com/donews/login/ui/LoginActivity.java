package com.donews.login.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.VibrateUtils;
import com.dn.drouter.ARouteHelper;
import com.dn.events.events.LoginLodingStartStatus;
import com.donews.common.base.MvvmBaseLiveDataActivity;
import com.donews.base.utils.ToastUtil;
import com.donews.common.router.RouterActivityPath;
import com.donews.common.services.ILoginService;
import com.donews.common.services.config.ServicesConfig;
import com.donews.login.R;
import com.donews.login.databinding.LoginActivityBinding;
import com.donews.login.viewmodel.LoginViewModel;
import com.donews.middle.abswitch.ABSwitch;
import com.donews.share.ISWXSuccessCallBack;
import com.donews.share.WXHolderHelp;
import com.donews.utilslibrary.utils.LogUtil;
import com.gyf.immersionbar.ImmersionBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * <p> </p>
 * 作者： created by honeylife<br>
 * 日期： 2020/11/11 17:19<br>
 * 版本：V1.0<br>
 */
@Route(path = RouterActivityPath.User.PAGER_LOGIN)
public class LoginActivity extends MvvmBaseLiveDataActivity<LoginActivityBinding, LoginViewModel> implements ISWXSuccessCallBack {


    @Autowired(name = ServicesConfig.User.LONGING_SERVICE)
    ILoginService loginService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this)
                .statusBarColor(R.color.white)
                .navigationBarColor(R.color.black)
                .fitsSystemWindows(true)
                .autoDarkModeEnable(true)
                .init();
    }

    private long fastVibrateTime = 0;

    @Override
    public void initView() {
        EventBus.getDefault().register(this);
        mViewModel.mActivity = this;
        mDataBinding.titleBar
                .setTitle("登录");
        mDataBinding.rlMobileLogin.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, MobileActivity.class)));
        mDataBinding.rlWachatLogin.setOnClickListener(v -> onWxLogin());
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
        mDataBinding.llBotDesc.setOnClickListener(v -> {
            mDataBinding.loginCkCheck.setChecked(!mDataBinding.loginCkCheck.isChecked());
        });
        mDataBinding.loginCkCheck.setChecked(ABSwitch.Ins().isOpenAutoAgreeProtocol());
        mDataBinding.loginCkCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                mDataBinding.rlWachatLogin.setEnabled(true);
                mDataBinding.rlWachatLoginFloat.setVisibility(View.GONE);
            } else {
                mDataBinding.rlWachatLogin.setEnabled(false);
                mDataBinding.rlWachatLoginFloat.setVisibility(View.VISIBLE);
            }
        });
        mDataBinding.rlWachatLogin.setEnabled(false);
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
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe //用户登录状态变化
    public void loginStatusEvent(LoginLodingStartStatus event) {
        event.getLoginLoadingLiveData().observe(this, result -> {
            if (result == 1 || result == 2) {
                finish();
            } else if (result == -1) {
                hideLoading();
                ToastUtil.show(this, "登录失败");
            }
        });
    }

    private void onWxLogin() {
        WXHolderHelp.login(this);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.login_activity;
    }


    @Override
    public void onSuccess(int state, String code) {
        LogUtil.i("state和code的值" + state + "==" + code);
        showLoading();
        mViewModel.onWXLogin(state, code);
    }

    @Override
    public void onFailed(String msg) {

    }

}
