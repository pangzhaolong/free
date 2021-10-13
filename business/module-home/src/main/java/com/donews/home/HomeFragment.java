package com.donews.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.donews.base.fragment.MvvmLazyLiveDataFragment;
import com.donews.common.router.RouterFragmentPath;
import com.donews.home.adapter.FragmentAdapter;
import com.donews.home.bean.HomeBean;
import com.donews.home.databinding.HomeFragmentBinding;
import com.donews.home.viewModel.HomeViewModel;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


/**
 * <p> </p>
 * 作者： created by honeylife<br>
 * 日期： 2020/11/10 18:13<br>
 * 版本：V1.0<br>
 */
@Route(path = RouterFragmentPath.Home.PAGER_HOME)
public class HomeFragment extends MvvmLazyLiveDataFragment<HomeFragmentBinding, HomeViewModel> {
    boolean isFirst = false;
    private FragmentAdapter mFragmentAdapter;
    private MutableLiveData<HomeBean> mHomeBean;

    @Override
    public int getLayoutId() {
        return R.layout.home_fragment;
    }


    @Override
    protected void onFragmentFirstVisible() {
        super.onFragmentFirstVisible();
        isFirst = true;
        // 获取网路数据
        mHomeBean = mViewModel.getNetHomeData();

        mHomeBean.observe(this, homeBean -> {
            // 获取数据
            if (homeBean == null) {
                // 处理接口出错的问题
                return;
            }
            // 处理正常的逻辑。
            if (mFragmentAdapter != null) {
                mFragmentAdapter.refreshData(homeBean.getList());
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mFragmentAdapter = new FragmentAdapter(this);

//        mDataBinding.homeCategoryVp2.setUserInputEnabled(false);
        mDataBinding.homeCategoryVp2.setAdapter(mFragmentAdapter);
        mDataBinding.homeCategoryTl.setTabMode(TabLayout.MODE_SCROLLABLE);
        TabLayoutMediator tab = new TabLayoutMediator(mDataBinding.homeCategoryTl, mDataBinding.homeCategoryVp2, (tab1, position) -> {
            if (position == 0) {
                tab1.setText("精品");
            } else {
                tab1.setText(mHomeBean.getValue().getList().get(position - 1).getCname());
            }
        });
        tab.attach();

        mDataBinding.homeSearchBar.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), HomeSearchActivity.class);
            startActivity(intent);
        });
        mDataBinding.homeJddHelp.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), HomeHelpActivity.class);
            startActivity(intent);
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
