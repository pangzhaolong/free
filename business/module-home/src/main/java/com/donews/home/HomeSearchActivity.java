package com.donews.home;

import android.os.Build;
import android.os.Bundle;
import android.system.Os;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListAdapter;

import androidx.annotation.RequiresApi;

import com.donews.base.activity.MvvmBaseLiveDataActivity;
import com.donews.base.viewmodel.BaseLiveDataViewModel;
import com.donews.home.adapter.SearchFragmentAdapter;
import com.donews.home.databinding.HomeJddSearchSearchBinding;
import com.donews.utilslibrary.utils.LogUtil;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.gyf.immersionbar.ImmersionBar;

public class HomeSearchActivity extends MvvmBaseLiveDataActivity<HomeJddSearchSearchBinding, BaseLiveDataViewModel> {

    private SearchFragmentAdapter mSearchFragmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.home_jdd_search_search;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void initView() {
        ImmersionBar.with(this)
                .statusBarColor(R.color.home_color_bar)
                .navigationBarColor(R.color.white)
                .fitsSystemWindows(true)
                .autoDarkModeEnable(true)
                .init();

        mSearchFragmentAdapter = new SearchFragmentAdapter(this);
        mDataBinding.homeSearchPlatformVp2.setAdapter(mSearchFragmentAdapter);
        mDataBinding.homeSearchPlatformTl.setTabMode(TabLayout.MODE_SCROLLABLE);
        TabLayoutMediator tab = new TabLayoutMediator(mDataBinding.homeSearchPlatformTl, mDataBinding.homeSearchPlatformVp2, (tab1, position) -> {
            if (position == 0) {
                tab1.setText("淘宝");
            }
        });
        tab.attach();
        mDataBinding.homeSearchBack.setOnClickListener(v -> finish());
        mDataBinding.homeSearchEdit.setDefaultFocusHighlightEnabled(false);
        mDataBinding.homeSearchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                LogUtil.e("xxxxxxx: " + s);
            }
        });
    }
}