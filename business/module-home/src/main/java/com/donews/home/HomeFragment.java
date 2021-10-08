package com.donews.home;

import androidx.lifecycle.Observer;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.donews.base.fragment.MvvmLazyLiveDataFragment;
import com.donews.common.router.RouterFragmentPath;
import com.donews.home.bean.HomeBean;
import com.donews.home.databinding.HomeFragmentBinding;
import com.donews.home.viewModel.HomeViewModel;


/**
 * <p> </p>
 * 作者： created by honeylife<br>
 * 日期： 2020/11/10 18:13<br>
 * 版本：V1.0<br>
 */
@Route(path = RouterFragmentPath.Home.PAGER_HOME)
public class HomeFragment extends MvvmLazyLiveDataFragment<HomeFragmentBinding, HomeViewModel> {
    boolean isFirst = false;

    @Override
    public int getLayoutId() {
        return R.layout.home_fragment;
    }


    @Override
    protected void onFragmentFirstVisible() {
        super.onFragmentFirstVisible();
        isFirst = true;
        // 获取网路数据
        mViewModel.getNetHomeData().observe(this, homeBean -> {
            // 获取数据
            if (homeBean == null) {
                // 处理接口出错的问题
                return;
            }
            // 处理正常的逻辑。
        });
    }


    @Override
    public void onResume() {
        super.onResume();

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
