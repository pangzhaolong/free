package com.donews.mine.ui;

import static com.donews.common.router.RouterActivityPath.Mine.PAGER_MINE_USER_INFO_ACTIVITY;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.dn.drouter.ARouteHelper;
import com.dn.events.events.LoginUserStatus;
import com.donews.base.utils.ToastUtil;
import com.donews.base.utils.glide.GlideUtils;
import com.donews.base.viewmodel.BaseLiveDataViewModel;
import com.donews.common.adapter.ScreenAutoAdapter;
import com.donews.common.base.MvvmBaseLiveDataActivity;
import com.donews.common.contract.LoginHelp;
import com.donews.common.contract.UserInfoBean;
import com.donews.common.contract.WeChatBean;
import com.donews.common.router.RouterActivityPath;
import com.donews.mine.R;
import com.donews.mine.databinding.MineActivityUserInfoBinding;
import com.donews.utilslibrary.utils.AppInfo;
import com.donews.utilslibrary.utils.KeySharePreferences;
import com.donews.utilslibrary.utils.SPUtils;
import com.gyf.immersionbar.ImmersionBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * <p>
 * 用户信息页面
 * <p>
 */
@Route(path = PAGER_MINE_USER_INFO_ACTIVITY)
public class UserInfoActivity extends MvvmBaseLiveDataActivity<MineActivityUserInfoBinding, BaseLiveDataViewModel> {

    private int mBDCounts = 0;

    @Override
    protected int getLayoutId() {
        ScreenAutoAdapter.match(this, 375.0f);
        ImmersionBar.with(this)
                .statusBarColor(R.color.mine_f6f9fb)
                .navigationBarColor(R.color.black)
                .fitsSystemWindows(true)
                .autoDarkModeEnable(true)
                .init();

        return R.layout.mine_activity_user_info;
    }

    @Override
    public void initView() {
        EventBus.getDefault().register(this);
        WeChatBean wxBean = LoginHelp.getInstance().getUserInfoBean().getWechatExtra();
        GlideUtils.loadImageView(this, wxBean.getHeadimgurl(), mDataBinding.userinfoHead);
        mDataBinding.userinfoId.setText(LoginHelp.getInstance().getUserInfoBean().getId());
        mDataBinding.userinfoName.setText(wxBean.getNickName());
        mDataBinding.userinfoExit.setOnClickListener((v) -> {
            AppInfo.exitWXLogin();
            finish();
        });
        mDataBinding.userinfoZx.setOnClickListener((v) -> {
            UserInfoBean uf = LoginHelp.getInstance().getUserInfoBean();
            if (uf == null ||
                    !AppInfo.checkIsWXLogin()) { //未登录
                ToastUtil.show(this, "你还未登陆,请先登录!");
                return;
            }
            ARouter.getInstance()
                    .build(RouterActivityPath.Mine.PAGER_MINEUSER_CANCELLATION_ACTIVITY)
                    .navigation();
        });
    }

    @Subscribe
    public void loginStatus(LoginUserStatus event) {
        finish();
    }

    private void lxkf() {
        String qqNumber = SPUtils.getInformain(KeySharePreferences.KEY_SERVER_QQ_NUMBER, "");

        if (checkApkExist(this, "com.tencent.mobileqq") && !qqNumber.equalsIgnoreCase("")) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("mqqwpa://im/chat?chat_type=wpa&uin=" + qqNumber + "&version=1")));
        } else {
            Bundle bundle = new Bundle();
            bundle.putString("url",
                    "https://recharge-web.xg.tagtic.cn/free/index.html#/customer");
            bundle.putString("title", "客服");
            ARouteHelper.routeSkip(RouterActivityPath.Web.PAGER_WEB_ACTIVITY, bundle);
        }
    }

    private boolean checkApkExist(Context context, String packageName) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName,
                    PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        mBDCounts = 0;
    }
}
