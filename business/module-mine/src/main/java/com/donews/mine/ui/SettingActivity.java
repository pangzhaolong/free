package com.donews.mine.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.donews.base.activity.MvvmBaseLiveDataActivity;
import com.donews.base.popwindow.ConfirmPopupWindow;
import com.donews.common.contract.LoginHelp;
import com.donews.common.router.RouterActivityPath;
import com.donews.common.updatedialog.UpdateManager;
import com.donews.mine.BuildConfig;
import com.donews.mine.R;
import com.donews.mine.backdoor.BackDoorActivity;
import com.donews.mine.databinding.MineActivitySettingBinding;
import com.donews.mine.viewModel.SettingViewModel;
import com.gyf.immersionbar.ImmersionBar;

public class SettingActivity extends MvvmBaseLiveDataActivity<MineActivitySettingBinding, SettingViewModel>{


    @Override
    protected int getLayoutId() {
        return R.layout.mine_activity_setting;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this)
                .statusBarColor(R.color.white)
                .navigationBarColor(R.color.white)
                .fitsSystemWindows(true)
                .autoDarkModeEnable(true)
                .init(); 
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initData();
    }

    private void initData() {
        getCacheData();
    }


    private void getCacheData() {
        mViewModel.getCacheData();
    }

    public void initView() {
        mViewModel.mContext=this;
        mDataBinding.titleBar.setTitle("设置");
        mDataBinding.rlSettingUserInfo.setOnClickListener(view -> goToUserCenter());
        mDataBinding.rlSettingAboutMe.setOnClickListener(view -> goToAboutMo());
        mDataBinding.rlSettingClearCache.setOnClickListener(view -> clearCache());
        mDataBinding.rlSettingCheckUpdate.setOnClickListener(view -> applyUpdata());
        mDataBinding.llBackDoor.setOnClickListener(view -> backDoorClick());

        mDataBinding.tvExitLoginText.setVisibility(View.GONE);
//        if (LoginHelp.getInstance().isLogin()) {
//            mDataBinding.tvExitLoginText.setVisibility(View.GONE);
//        } else {
//            mDataBinding.tvExitLoginText.setVisibility(View.VISIBLE);
//            mDataBinding.tvExitLoginText.setOnClickListener(view -> dropOut());
//        }
    }

    /**
     * 退出登录
     */
    private void dropOut() {
        ConfirmPopupWindow confirmPopupWindow = new ConfirmPopupWindow(this);
        confirmPopupWindow.show();
        confirmPopupWindow.setTitleText("确认退出登录吗？").setOkOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmPopupWindow.hide();
                //接触绑定
//                UserInfoManage.getUserUnBind(true, true, true);
                mDataBinding.tvExitLoginText.setVisibility(View.GONE);
            }

        }).setCancelOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmPopupWindow.hide();
            }
        });
    }

    /**
     * 清除缓存
     */
    private void clearCache() {
        ConfirmPopupWindow confirmPopupWindow = new ConfirmPopupWindow(this);
        confirmPopupWindow.show();
        confirmPopupWindow.setTitleText("确定清除缓存？").setOkOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmPopupWindow.hide();
                mViewModel.clearCache();
            }

        }).setCancelOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmPopupWindow.hide();
            }
        });

    }

    /**
     * 跳转关于我们页面
     */
    private void goToAboutMo() {
        ARouter.getInstance().build(RouterActivityPath.Web.PAGER_WEB_ACTIVITY)
                .withString("title", "关于我们")
                .withString("url", BuildConfig.HTTP_H5 + "aboutUs")
                .navigation(this);

    }

    /**
     * 跳转个人中心
     */
    protected void goToUserCenter() {
        if (goToLoginPage()) return;
        startActivity(new Intent(this, UserCenterActivity.class));
    }

    /**
     * 判断未登录跳转登录
     *
     * @return true 未登录
     */
    private boolean goToLoginPage() {
        if (LoginHelp.getInstance().isLogin()) {
            ARouter.getInstance().build(RouterActivityPath.User.PAGER_LOGIN).greenChannel().navigation(SettingActivity.this);
            return true;
        }
        return false;
    }

    /**
     * 检查更新
     */
    private void applyUpdata() {
        UpdateManager.getInstance().checkUpdate(this);
    }



    private int mbackDoorCount = 0;

    @Override
    protected void onResume() {
        super.onResume();
        mbackDoorCount = 0;
    }

    public void backDoorClick() {
        mbackDoorCount++;
        if (mbackDoorCount == 10) {
            startActivity(new Intent(this, BackDoorActivity.class));
        }
    }
}