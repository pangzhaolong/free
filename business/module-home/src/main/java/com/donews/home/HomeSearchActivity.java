package com.donews.home;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.system.Os;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListAdapter;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.donews.base.activity.MvvmBaseLiveDataActivity;
import com.donews.base.viewmodel.BaseLiveDataViewModel;
import com.donews.home.adapter.SearchFragmentAdapter;
import com.donews.home.adapter.SearchSugAdapter;
import com.donews.home.databinding.HomeJddSearchSearchBinding;
import com.donews.home.viewModel.SearchViewModel;
import com.donews.utilslibrary.utils.LogUtil;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.gyf.immersionbar.ImmersionBar;

/**
 * <p> </p>
 * 作者： created by dw<br>
 * 日期： 2021/10/13 14:13<br>
 * 版本：V1.0<br>
 */
public class HomeSearchActivity extends MvvmBaseLiveDataActivity<HomeJddSearchSearchBinding, SearchViewModel> {

    private SearchFragmentAdapter mSearchFragmentAdapter;
    private SearchSugAdapter mSearchSugAdapter;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
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
        mDataBinding.homeSearchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equalsIgnoreCase("")) {
                    mDataBinding.homeSearchSuggestionRv.setVisibility(View.GONE);
                    mDataBinding.homeSearchPlatformLl.setVisibility(View.VISIBLE);
                    return;
                }
                LogUtil.e("xxxxxxx: " + s);
                mViewModel.getSearchData(s.toString()).observe((LifecycleOwner) mContext, searchSugBean -> {
                    if (searchSugBean.getList().size() <= 0) {
                        mDataBinding.homeSearchSuggestionRv.setVisibility(View.GONE);
                        mDataBinding.homeSearchPlatformLl.setVisibility(View.VISIBLE);
                    } else {
                        mDataBinding.homeSearchSuggestionRv.setVisibility(View.VISIBLE);
                        mDataBinding.homeSearchPlatformLl.setVisibility(View.GONE);
                    }
                    mSearchSugAdapter.refreshData(searchSugBean.getList());
                });
            }
        });

        mSearchSugAdapter = new SearchSugAdapter(this);
        mDataBinding.homeSearchSuggestionRv.setLayoutManager(new LinearLayoutManager(this));
        mDataBinding.homeSearchSuggestionRv.setAdapter(mSearchSugAdapter);
        mDataBinding.homeSearchSuggestionRv.setVisibility(View.GONE);
        mDataBinding.homeSearchPlatformLl.setVisibility(View.VISIBLE);
    }
}