package com.donews.mine.ui;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.donews.common.base.MvvmBaseLiveDataActivity;
import com.donews.common.router.RouterActivityPath;
import com.donews.mine.R;
import com.donews.mine.databinding.MineActivityUserCancellationBinding;
import com.donews.mine.dialogs.UserCancellationWhyDialogFragment;
import com.donews.mine.viewModel.UserCancellationViewModel;
import com.gyf.immersionbar.ImmersionBar;

import org.greenrobot.eventbus.EventBus;

@Route(path = RouterActivityPath.Mine.PAGER_MINEUSER_CANCELLATION_ACTIVITY)
public class MineUserCancellationActivity extends
        MvvmBaseLiveDataActivity<MineActivityUserCancellationBinding, UserCancellationViewModel> {

    @Override
    protected int getLayoutId() {
        return R.layout.mine_activity_user_cancellation;
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

    public void initView() {
        mViewModel.mContext = this;
        mViewModel.setDataBinDing(mDataBinding);
        mDataBinding.titleBar.setTitle("注销账号");
        mDataBinding.tvCancellationTo.setOnClickListener(v -> {
            UserCancellationWhyDialogFragment df = (UserCancellationWhyDialogFragment) ARouter.getInstance()
                    .build(RouterActivityPath.Mine.DIALOG_USER_CANCELLATION_WHY_DIALOG_FRAGMENT)
                    .navigation();
            df.setCloseListener(isUnRegSuccess -> {
                if (isUnRegSuccess) {
                    finish();
                }
            });
            df.show(getSupportFragmentManager(), "user_cancellation");
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initData() {
    }
}