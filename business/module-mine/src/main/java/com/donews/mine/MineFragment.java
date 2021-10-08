package com.donews.mine;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.donews.base.fragment.MvvmLazyLiveDataFragment;
import com.donews.common.contract.LoginHelp;
import com.donews.common.router.RouterFragmentPath;
import com.donews.mine.databinding.MineFragmentBinding;
import com.donews.mine.viewModel.MineViewModel;


/**
 * <p> </p>
 * 作者： created by honeylife<br>
 * 日期： 2020/11/10 18:13<br>
 * 版本：V1.0<br>
 */
@Route(path = RouterFragmentPath.User.PAGER_USER)
public class MineFragment extends MvvmLazyLiveDataFragment<MineFragmentBinding, MineViewModel> {


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
        return R.layout.mine_fragment;
    }


    @Override
    public void onResume() {
        super.onResume();
        onRefresh();
    }


    private void onRefresh(){
        mViewModel.getQuery().observe(this, queryBean -> {
            if (queryBean == null) {
                return;
            }
            mDataBinding.setVariable(BR.query, queryBean);
        });
        if (LoginHelp.getInstance().getUserInfoBean() != null) {
            mDataBinding.setVariable(BR.user, LoginHelp.getInstance().getUserInfoBean());
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
