package com.donews.home;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.donews.common.base.MvvmBaseLiveDataActivity;
import com.donews.home.adapter.BuysGoodsAdapter;
import com.donews.home.adapter.SearchFragmentAdapter;
import com.donews.home.adapter.SearchSugAdapter;
import com.donews.home.databinding.HomeJddSearchSearchBinding;
import com.donews.home.listener.GoodsClickListener;
import com.donews.home.listener.SearchListener;
import com.donews.home.viewModel.SearchViewModel;
import com.donews.home.views.SearchHistoryItem;
import com.donews.middle.abswitch.ABSwitch;
import com.donews.middle.bean.home.SearchHistory;
import com.donews.middle.bean.home.TmpSearchHistory;
import com.donews.middle.decoration.GridSpaceItemDecoration;
import com.donews.middle.dialog.JumpThirdAppDialog;
import com.donews.middle.go.GotoUtil;
import com.donews.middle.listener.JumpThirdAppListener;
import com.donews.middle.views.TabItemEx;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.gyf.immersionbar.ImmersionBar;

/**
 * <p> </p>
 * 作者： created by dw<br>
 * 日期： 2021/10/13 14:13<br>
 * 版本：V1.0<br>
 */
public class HomeSearchActivity extends MvvmBaseLiveDataActivity<HomeJddSearchSearchBinding, SearchViewModel> implements
        GoodsClickListener, SearchListener {

    private SearchFragmentAdapter mSearchFragmentAdapter;
    private SearchSugAdapter mSearchSugAdapter;
    private BuysGoodsAdapter mBuysGoodsAdapter;
    private Context mContext;

    private String mPreKeyWord = "";
    private int mPosition = 0;

    private int mPageId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this)
                .statusBarColor(R.color.white)
                .navigationBarColor(R.color.black)
                .fitsSystemWindows(true)
                .autoDarkModeEnable(true)
                .init();

        SearchHistory.Ins().read();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.home_jdd_search_search;
    }

    @Override
    public void initView() {
        mContext = this;

        mSearchFragmentAdapter = new SearchFragmentAdapter(this);
        mDataBinding.homeSearchPlatformVp2.setAdapter(mSearchFragmentAdapter);
        mDataBinding.homeSearchPlatformTl.setTabMode(TabLayout.MODE_SCROLLABLE);
        TabLayoutMediator tab = new TabLayoutMediator(mDataBinding.homeSearchPlatformTl, mDataBinding.homeSearchPlatformVp2, (tab1, position) -> {
            if (tab1.getCustomView() == null) {
                tab1.setCustomView(new TabItemEx(mContext));
            }
            TabItemEx itemEx = (TabItemEx) tab1.getCustomView();
            itemEx.init(position);
            if (position == 0) {
                itemEx.selected();
            }
        });
        tab.attach();

        mDataBinding.homeSearchPlatformTl.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                TabItemEx tabItem = (TabItemEx) tab.getCustomView();
                assert tabItem != null;
                tabItem.selected();
                mPosition = tab.getPosition();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                TabItemEx tabItem = (TabItemEx) tab.getCustomView();
                assert tabItem != null;
                tabItem.unSelected();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                TabItemEx tabItem = (TabItemEx) tab.getCustomView();
                assert tabItem != null;
                tabItem.selected();
                mPosition = tab.getPosition();
            }
        });
        mDataBinding.homeSearchBack.setOnClickListener(v -> onBackPressedEx());
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
                    mDataBinding.homeSearchFragmentsLl.setVisibility(View.INVISIBLE);
                    mDataBinding.homeSearchCl.setVisibility(View.VISIBLE);
                    return;
                }
                search(s.toString());
            }
        });


        mSearchSugAdapter = new SearchSugAdapter(this);
        mDataBinding.homeSearchSuggestionRv.setLayoutManager(new LinearLayoutManager(this));
        mDataBinding.homeSearchSuggestionRv.setAdapter(mSearchSugAdapter);
        mDataBinding.homeSearchSuggestionRv.setVisibility(View.GONE);
        mDataBinding.homeSearchFragmentsLl.setVisibility(View.INVISIBLE);
        mDataBinding.homeSearchCl.setVisibility(View.VISIBLE);

        mDataBinding.homeSearchDo.setOnClickListener(v -> {
            InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

            mDataBinding.homeSearchSuggestionRv.setVisibility(View.GONE);
            mDataBinding.homeSearchFragmentsLl.setVisibility(View.VISIBLE);
            mDataBinding.homeSearchCl.setVisibility(View.GONE);
            mSearchFragmentAdapter.search(mDataBinding.homeSearchEdit.getText().toString(), mPosition);
        });

        mDataBinding.homeSearchFlowLayout.setFlow(true);
        mDataBinding.homeSearchFlowLayout.setMaxRows(4);
        reloadSearchHistory();

        mDataBinding.homeSearchHistoryCleanTv.setOnClickListener(v -> {
            SearchHistory.Ins().clear();
            TmpSearchHistory.Ins().clear();
            SearchHistory.Ins().write("");
            reloadSearchHistory();
        });

        //大家都在买
        mBuysGoodsAdapter = new BuysGoodsAdapter(this, this);
        mDataBinding.homeSearchBuysRv.setLayoutManager(new GridLayoutManager(this, 2));
        mDataBinding.homeSearchBuysRv.addItemDecoration(new GridSpaceItemDecoration(2));
        mDataBinding.homeSearchBuysRv.setAdapter(mBuysGoodsAdapter);

        mDataBinding.homeSearchSrl.setEnableRefresh(false);
        mDataBinding.homeSearchSrl.setOnLoadMoreListener(refreshLayout -> loadBuysData());


        loadBuysData();
    }

    private void loadBuysData() {
        mPageId++;
        mViewModel.getBuysData(mPageId).observe(this, homeGoodsBean -> {
            mDataBinding.homeSearchSrl.finishLoadMore();
            if (homeGoodsBean == null || homeGoodsBean.getList() == null || homeGoodsBean.getList().size() <= 0) {
                mPageId--;
                return;
            }

            mBuysGoodsAdapter.refreshData(homeGoodsBean.getList(), mPageId == 1);
        });
    }

    private void search(String keyWord) {
        String strKeyWord = keyWord.trim();
        if (mPreKeyWord.equalsIgnoreCase(strKeyWord) || strKeyWord.equalsIgnoreCase("")) {
            return;
        }
        mViewModel.getSearchData(strKeyWord).observe((LifecycleOwner) mContext, searchSugBean -> {
            if (searchSugBean == null || searchSugBean.getList() == null || searchSugBean.getList().size() <= 0) {
                mDataBinding.homeSearchSuggestionRv.setVisibility(View.GONE);
                mDataBinding.homeSearchFragmentsLl.setVisibility(View.INVISIBLE);
                mDataBinding.homeSearchCl.setVisibility(View.VISIBLE);
                return;
            }

            mSearchSugAdapter.refreshData(searchSugBean.getList());
            mDataBinding.homeSearchSuggestionRv.setVisibility(View.VISIBLE);
            mDataBinding.homeSearchFragmentsLl.setVisibility(View.INVISIBLE);
            mDataBinding.homeSearchCl.setVisibility(View.GONE);
        });

        mPreKeyWord = strKeyWord;
    }

    private void reloadSearchHistory() {
        if (SearchHistory.Ins().getList().size() <= 0) {
            mDataBinding.homeSearchHistoryTl.setVisibility(View.GONE);
        } else {
            mDataBinding.homeSearchHistoryTl.setVisibility(View.VISIBLE);
            mDataBinding.homeSearchFlowLayout.removeAllViews();
            for (int i = 0; i < SearchHistory.Ins().getList().size(); i++) {
                mDataBinding.homeSearchFlowLayout.addView(new SearchHistoryItem(mContext, SearchHistory.Ins().getList().get(i), this));
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //搜索历史
        reloadSearchHistory();
    }

    @Override
    public void onBackPressed() {
        onBackPressedEx();
    }

    private int mHistoryIndex = -5;

    private void onBackPressedEx() {
        if (mHistoryIndex == -5) {
            mHistoryIndex = TmpSearchHistory.Ins().getList().size() - 1;
        }

        mHistoryIndex--;
        if (mHistoryIndex == -1) {
            mDataBinding.homeSearchHistoryTl.setVisibility(View.VISIBLE);
            mDataBinding.homeSearchSrl.setVisibility(View.VISIBLE);
            mDataBinding.homeSearchEdit.setText("");
            reloadSearchHistory();
            return;
        }

        if (mHistoryIndex < -1) {
            finish();
            return;
        }

//        mDataBinding.homeSearchEdit.setText(TmpSearchHistory.Ins().getList().get(mHistoryIndex));
        mSearchFragmentAdapter.showHistorySearch(TmpSearchHistory.Ins().getList().get(mHistoryIndex), mPosition);
    }

    @Override
    public void onClick(String keyWord) {
        mDataBinding.homeSearchSuggestionRv.setVisibility(View.GONE);
        mDataBinding.homeSearchFragmentsLl.setVisibility(View.VISIBLE);
        mDataBinding.homeSearchCl.setVisibility(View.GONE);

        mSearchFragmentAdapter.search(keyWord, mPosition);

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SearchHistory.Ins().write(SearchHistory.Ins().toString());
        SearchHistory.Ins().setCurKeyWord("");
//        SearchHistory.Ins().clear();
        TmpSearchHistory.Ins().clear();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onClick(String goodsId, String materialId, String searchId, int src) {
        Context context = this;

        if (!ABSwitch.Ins().isOpenJumpDlg()) {
            GotoUtil.requestPrivilegeLinkBean(context, goodsId, materialId, searchId, src);
            return;
        }

        new JumpThirdAppDialog(context, src, new JumpThirdAppListener() {
            @Override
            public void onClose() {

            }

            @Override
            public void onGo() {
                GotoUtil.requestPrivilegeLinkBean(context, goodsId, materialId, searchId, src);
            }
        }).show();

//        GotoUtil.requestPrivilegeLinkBean(this, goodsId, materialId, searchId, src);
    }
}