package com.donews.mine;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.donews.base.fragment.MvvmLazyLiveDataFragment;
import com.donews.common.router.RouterFragmentPath;
import com.donews.mine.databinding.MineSettingFragmentBinding;
import com.donews.mine.viewModel.SettingFragmentViewModel;


/**
 * 作者： created by lcl<br>
 *  * 日期： 2021/10/10 9:13<br>
 *  * 版本：V1.0<br>
 */
@Route(path = RouterFragmentPath.User.PAGER_USER_SETTING)
public class SettingFragment extends MvvmLazyLiveDataFragment<MineSettingFragmentBinding, SettingFragmentViewModel> {


    @Override
    protected void onFragmentFirstVisible() {
        super.onFragmentFirstVisible();
        initView();
        mViewModel.lifecycleOwner = this;

    }


    private void initView() {
        mViewModel.setDataBinDing(mDataBinding, getBaseActivity());
    }


    @Override
    public int getLayoutId() {
        return R.layout.mine_setting_fragment;
    }


    @Override
    public void onResume() {
        super.onResume();
        onRefresh();
    }


    private void onRefresh(){
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
