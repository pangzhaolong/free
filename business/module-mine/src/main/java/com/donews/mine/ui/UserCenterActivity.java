package com.donews.mine.ui;

import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.dn.drouter.ARouteHelper;
import com.dn.drouter.annotation.DNMethodRoute;
import com.donews.base.activity.MvvmBaseLiveDataActivity;
import com.donews.common.router.RouterActivityPath;
import com.donews.common.services.config.ServicesConfig;
import com.donews.mine.BR;
import com.donews.mine.R;
import com.donews.mine.databinding.MineUserCenterBinding;
import com.donews.mine.viewModel.UserInfoViewModel;
import com.donews.utilslibrary.utils.LogUtil;
import com.gyf.immersionbar.ImmersionBar;

public class UserCenterActivity extends MvvmBaseLiveDataActivity<MineUserCenterBinding, UserInfoViewModel> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this)
                .statusBarColor(R.color.white)
                .navigationBarColor(R.color.white)
                .fitsSystemWindows(true)
                .autoDarkModeEnable(true)
                .init();
        mDataBinding.titleBar.setTitle("个人中心");

    }


    private void bindWeChat() {
        mViewModel.bindWeChat();
    }

    private void goTOBindPhone() {
        ARouter.getInstance().build(RouterActivityPath.User.PAGER_BIND_PHONE).greenChannel().navigation(this);
    }

    /**
     * 获取数据
     */
    private void getUserIfoData() {
        mViewModel.getUserInfoData().observe(this, userInfoBean -> {
            if (mDataBinding != null && userInfoBean != null) {
                mDataBinding.setVariable(BR.userInfoBean, userInfoBean);
            }

        });
    }



    @Override
    protected int getLayoutId() {
        return R.layout.mine_user_center;
    }

    @Override
    public void initView() {
        ARouteHelper.bind(this);
        mDataBinding.rlUserinfoYaoqingCode.setVisibility(View.GONE);
//        viewDataBinding.rlUserinfoYaoqingCode.setOnClickListener(view -> goTOBindPhone());

        mDataBinding.rlUserinfoWachat.setOnClickListener(view -> bindWeChat());
        getUserIfoData();

    }

    @DNMethodRoute(ServicesConfig.User.LOGIN_SUCCESS)
    public void onWeChatLoading() {
        LogUtil.d("onWeChatLoading");
        getUserIfoData();
    }

    @Override
    protected void onDestroy() {
        ARouteHelper.unBind(this);
        super.onDestroy();

    }
}