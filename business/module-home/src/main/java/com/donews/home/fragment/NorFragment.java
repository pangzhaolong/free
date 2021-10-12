package com.donews.home.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.donews.base.fragment.MvvmLazyLiveDataFragment;
import com.donews.home.R;
import com.donews.home.adapter.FragmentAdapter;
import com.donews.home.adapter.FragmentCategoryAdapter;
import com.donews.home.bean.HomeBean;
import com.donews.home.databinding.HomeFragmentNorBinding;
import com.donews.home.viewModel.NorViewModel;
import com.donews.utilslibrary.utils.LogUtil;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class NorFragment extends MvvmLazyLiveDataFragment<HomeFragmentNorBinding, NorViewModel> {

    public NorFragment(HomeBean.CategoryItem categoryItem) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.home_fragment_nor;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*mDataBinding.homeCategoryVp2.setAdapter(new FragmentCategoryAdapter(this));
        TabLayoutMediator tab = new TabLayoutMediator(mDataBinding.homeCategoryTl, mDataBinding.homeCategoryVp2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        tab.setText("人气");
                        break;
                    case 1:
                        tab.setText("最新");
                        break;
                    case 2:
                        tab.setText("销量");
                        break;
                    case 3:
                        tab.setText("价格");
                        break;
                }
            }
        });
        tab.attach();*/
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.e("NorFragment onDestroy");
    }
}
