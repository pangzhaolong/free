package com.donews.home.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.donews.base.fragment.MvvmLazyLiveDataFragment;
import com.donews.home.R;
import com.donews.home.adapter.FragmentAdapter;
import com.donews.home.adapter.FragmentCategoryAdapter;
import com.donews.home.adapter.NorGoodsAdapter;
import com.donews.home.bean.HomeBean;
import com.donews.home.databinding.HomeFragmentNorBinding;
import com.donews.home.viewModel.NorViewModel;
import com.donews.utilslibrary.utils.LogUtil;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class NorFragment extends MvvmLazyLiveDataFragment<HomeFragmentNorBinding, NorViewModel> {

    private final HomeBean.CategoryItem mCategoryItem;
    private NorGoodsAdapter mNorGoodsAdapter;

    public NorFragment(HomeBean.CategoryItem categoryItem) {
        mCategoryItem = categoryItem;
    }

    @Override
    public int getLayoutId() {
        return R.layout.home_fragment_nor;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNorGoodsAdapter = new NorGoodsAdapter(this.getContext());
        mDataBinding.homeNorGoodsRv.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mDataBinding.homeNorGoodsRv.setAdapter(mNorGoodsAdapter);

        mViewModel.getNorGoodsData(mCategoryItem.getCid()).observe(getViewLifecycleOwner(), norGoodsBean -> {
            if (norGoodsBean == null) {
                return;
            }

            mNorGoodsAdapter.refreshData(norGoodsBean.getList());
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.e("NorFragment onDestroy");
    }
}
