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
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.donews.base.fragment.MvvmLazyLiveDataFragment;
import com.donews.base.utils.ToastUtil;
import com.donews.common.contract.LoginHelp;
import com.donews.common.contract.UserInfoBean;
import com.donews.common.router.RouterActivityPath;
import com.donews.common.router.RouterFragmentPath;
import com.donews.mine.databinding.MineSettingFragmentBinding;
import com.donews.mine.dialogs.ShareToDialogFragment;
import com.donews.mine.dialogs.UserCancellationWhyDialogFragment;
import com.donews.mine.viewModel.SettingFragmentViewModel;
import com.donews.utilslibrary.utils.AppInfo;

import java.io.File;


/**
 * 作者： created by lcl<br>
 * * 日期： 2021/10/10 9:13<br>
 * * 版本：V1.0<br>
 */
@Route(path = RouterFragmentPath.User.PAGER_USER_SETTING)
public class MineSettingFragment extends MvvmLazyLiveDataFragment<MineSettingFragmentBinding, SettingFragmentViewModel> {


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
        super.onDestroy();
    }

    private void initView() {
        mViewModel.setDataBinDing(mDataBinding, getBaseActivity());
        mDataBinding.tvExitLogin.setOnClickListener(v -> {
            ToastUtil.show(getBaseActivity(), "退出登录按钮");
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
