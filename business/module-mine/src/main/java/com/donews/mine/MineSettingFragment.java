package com.donews.mine;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.NetworkUtils;
import com.dn.events.events.LoginLodingStartStatus;
import com.dn.events.events.LoginUserStatus;
import com.donews.base.utils.ToastUtil;
import com.donews.common.base.MvvmLazyLiveDataFragment;
import com.donews.common.contract.LoginHelp;
import com.donews.common.contract.UserInfoBean;
import com.donews.common.router.RouterFragmentPath;
import com.donews.jpush.utils.JPushSwitch;
import com.donews.middle.abswitch.ABSwitch;
import com.donews.mine.common.CommonParams;
import com.donews.mine.databinding.MineSettingFragmentBinding;
import com.donews.mine.viewModel.SettingFragmentViewModel;
import com.donews.utilslibrary.analysis.AnalysisUtils;
import com.donews.utilslibrary.dot.Dot;
import com.donews.utilslibrary.utils.AppInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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
        AnalysisUtils.onEventEx(getActivity(), Dot.Page_Setting);
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
            AnalysisUtils.onEventEx(getActivity(), Dot.Btn_Logout);
            showLoading();
            AppInfo.exitWXLogin();
            CommonParams.setNetWorkExitOrUnReg();
        });
        updateUI();

        if (ABSwitch.Ins().isOpenAB()) {
            mDataBinding.mineSettingTitle.setVisibility(View.VISIBLE);
            mDataBinding.tvExitLogin.setVisibility(View.GONE);
            mDataBinding.setZxzh.setVisibility(View.GONE);
            mDataBinding.setFxapp.setVisibility(View.GONE);
        }
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
            Switch swControl = item.findViewById(R.id.sw_control);
            TextView tvTitle = item.findViewById(R.id.tv_name);
            TextView tvDesc = item.findViewById(R.id.tv_right_desc);
            tvTitle.setText(mViewModel.getItemTitleName(itemViewCount));
            if (swControl != null) {
                boolean type = JPushSwitch.getSwitchType(getContext());
                swControl.setChecked(!type);
                swControl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            ToastUtil.show(getContext(), "消息通知已开启");
                            JPushSwitch.resumePush(getContext());
                        } else {
                            ToastUtil.show(getContext(), "消息通知已关闭");
                            JPushSwitch.stopPush(getContext());
                        }
                    }
                });

            }

            tvDesc.setText(mViewModel.getItemDescText(itemViewCount));
            mViewModel.addItemClick(item, itemViewCount);
            itemViewCount++;
        }
    }
}
