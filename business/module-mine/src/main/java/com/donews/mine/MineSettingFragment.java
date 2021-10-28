package com.donews.mine;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.NetworkUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.dn.events.events.LoginLodingStartStatus;
import com.dn.events.events.LoginUserStatus;
import com.donews.base.fragment.MvvmLazyLiveDataFragment;
import com.donews.base.fragmentdialog.LoadingHintDialog;
import com.donews.base.utils.ToastUtil;
import com.donews.common.contract.LoginHelp;
import com.donews.common.contract.UserInfoBean;
import com.donews.common.router.RouterActivityPath;
import com.donews.common.router.RouterFragmentPath;
import com.donews.mine.common.CommonParams;
import com.donews.mine.databinding.MineSettingFragmentBinding;
import com.donews.mine.dialogs.ShareToDialogFragment;
import com.donews.mine.dialogs.UserCancellationWhyDialogFragment;
import com.donews.mine.viewModel.SettingFragmentViewModel;
import com.donews.utilslibrary.utils.AppInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;


/**
 * 作者： created by lcl<br>
 * * 日期： 2021/10/10 9:13<br>
 * * 版本：V1.0<br>
 */
@Route(path = RouterFragmentPath.User.PAGER_USER_SETTING)
public class MineSettingFragment extends
        MvvmLazyLiveDataFragment<MineSettingFragmentBinding, SettingFragmentViewModel> {


    @Override
    protected void onFragmentFirstVisible() {
        super.onFragmentFirstVisible();
        initView();
        mViewModel.lifecycleOwner = this;
        mViewModel.updateUIFlg.observe(this, result -> {
            bindViewText();
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.mine_setting_fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        bindViewText();
        onRefresh();
    }

    private void onRefresh() {
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe
    public void loginStatus(LoginUserStatus event) {
        updateUI();
    }

    @Subscribe //用户登录状态变化
    public void loginStatusEvent(LoginLodingStartStatus event) {
        event.getLoginLoadingLiveData().observe(this, result -> {
            if (result == 1 || result == 2) {
                AppInfo.exitLogin();
                //切换为设备登录
                ToastUtil.show(getBaseActivity(), "执行成功");
                hideLoading();
            } else if (result == -1) {
                ToastUtil.show(getBaseActivity(), "操作异常");
            }
        });
    }

    private void initView() {
        EventBus.getDefault().register(this);
        mViewModel.setDataBinDing(mDataBinding, getBaseActivity());
        mDataBinding.tvExitLogin.setOnClickListener(v -> {
            if (!NetworkUtils.isAvailableByPing()) {
                ToastUtil.show(getBaseActivity(), "请检查网络连接");
                return;
            }
            showLoading();
            AppInfo.exitWXLogin();
            CommonParams.setNetWorkExitOrUnReg();
        });
        updateUI();
    }

    //更新UI
    private void updateUI() {
        UserInfoBean uf = LoginHelp.getInstance().getUserInfoBean();
        TextView loginBut = getView().findViewById(R.id.tv_exit_login);
        if (uf == null ||
                !AppInfo.checkIsWXLogin()) { //未登录
            loginBut.setVisibility(View.GONE);
        } else { //已登录
            loginBut.setVisibility(View.VISIBLE);
        }
    }

    //绑定视图和文字
    private void bindViewText() {
        //列表数据绑定
        int itemViewCount = 0;
        for (int i = 0; i < mDataBinding.setListLayout.getChildCount(); i++) {
            View item = mDataBinding.setListLayout.getChildAt(i);
            if (item.getTag() == null || item.getTag().toString().isEmpty()) {
                continue;
            }
            TextView tvTitle = item.findViewById(R.id.tv_name);
            TextView tvDesc = item.findViewById(R.id.tv_right_desc);
            tvTitle.setText(mViewModel.getItemTitleName(itemViewCount));
            tvDesc.setText(mViewModel.getItemDescText(itemViewCount));
            mViewModel.addItemClick(item, itemViewCount);
            itemViewCount++;
        }
    }
}
