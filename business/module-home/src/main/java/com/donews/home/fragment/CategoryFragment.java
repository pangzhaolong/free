package com.donews.home.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.donews.base.fragment.MvvmLazyLiveDataFragment;
import com.donews.home.R;
import com.donews.home.databinding.HomeFragmentCategoryBinding;
import com.donews.home.viewModel.CatViewModel;

public class CategoryFragment extends MvvmLazyLiveDataFragment<HomeFragmentCategoryBinding, CatViewModel> {

    public CategoryFragment(int index) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.home_fragment_category;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}
