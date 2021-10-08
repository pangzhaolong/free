package com.donews.login.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.donews.base.activity.MvvmBaseLiveDataActivity;
import com.donews.common.router.RouterActivityPath;
import com.donews.common.services.ILoginService;
import com.donews.common.services.config.ServicesConfig;
import com.donews.login.R;
import com.donews.login.databinding.LoginActivityBinding;
import com.donews.login.viewmodel.LoginViewModel;
import com.donews.share.ISWXSuccessCallBack;
import com.donews.share.WXHolderHelp;
import com.donews.utilslibrary.utils.LogUtil;

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
    }

    @Override
    public void initView() {
        mViewModel.mActivity = this;
        mDataBinding.rlMobileLogin.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, MobileActivity.class)));
        mDataBinding.rlWachatLogin.setOnClickListener(v -> onWxLogin());
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
        mViewModel.onWXLogin(state, code);
    }

    @Override
    public void onFailed(String msg) {

    }


}
