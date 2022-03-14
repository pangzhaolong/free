package com.donews.home;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.donews.common.base.MvvmBaseLiveDataActivity;
import com.donews.common.router.RouterActivityPath;
import com.donews.home.adapter.PerfectGoodsAdapter;
import com.donews.home.adapter.SearchSugAdapter;
import com.donews.home.databinding.HomeWelfareActivityBinding;
import com.donews.home.listener.GoodsClickListener;
import com.donews.home.listener.SearchListener;
import com.donews.home.viewModel.WelfareViewModel;
import com.donews.middle.abswitch.OtherSwitch;
import com.donews.middle.bean.home.HomeGoodsBean;
import com.donews.middle.dialog.JumpThirdAppDialog;
import com.donews.middle.go.GotoUtil;
import com.donews.middle.listener.JumpThirdAppListener;
import com.gyf.immersionbar.ImmersionBar;

@Route(path = RouterActivityPath.Home.Welfare_Activity)
public class HomeWelfareActivity extends MvvmBaseLiveDataActivity<HomeWelfareActivityBinding, WelfareViewModel> implements GoodsClickListener,
        SearchListener {

    private PerfectGoodsAdapter mPerfectGoodsAdapter;
    private SearchSugAdapter mSearchSugAdapter;
    private int mPageId = 1;
    private int mSrc = 1;
    private String mCurKeyWord = "";

    @Autowired
    String from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);

        super.onCreate(savedInstanceState);

        ImmersionBar.with(this)
                .statusBarColor(R.color.white)
                .navigationBarColor(R.color.black)
                .fitsSystemWindows(true)
                .autoDarkModeEnable(true)
                .init();

        if (from.equalsIgnoreCase("tb")) {
            mDataBinding.homeWelfareLogo.setImageResource(R.drawable.home_logo_tb);
            mSrc = 1;
        } else if (from.equalsIgnoreCase("pdd")) {
            mDataBinding.homeWelfareLogo.setImageResource(R.drawable.home_logo_pdd);
            mSrc = 2;
        } else if (from.equalsIgnoreCase("jd")) {
            mDataBinding.homeWelfareLogo.setImageResource(R.drawable.home_logo_jd);
            mSrc = 3;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.home_welfare_activity;
    }

    @Override
    public void initView() {
        mDataBinding.homeWelfareLoadingStatusTv.setVisibility(View.VISIBLE);
        mDataBinding.homeWelfareGoodsRv.setVisibility(View.GONE);
        mPerfectGoodsAdapter = new PerfectGoodsAdapter(this, this);
        mDataBinding.homeWelfareGoodsRv.setLayoutManager(new LinearLayoutManager(this));
        mDataBinding.homeWelfareGoodsRv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = 32;
            }
        });
        mDataBinding.homeWelfareGoodsRv.setAdapter(mPerfectGoodsAdapter);

        loadRefreshList();

        mDataBinding.homeWelfareSrl.setEnableRefresh(false);
        mDataBinding.homeWelfareSrl.setOnLoadMoreListener(refreshLayout -> loadMoreList());

        mDataBinding.homeWelfareBack.setOnClickListener(v -> finish());

        mDataBinding.homeWelfareEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equalsIgnoreCase("")) {
                    return;
                }
                search(s.toString());
            }
        });

        mSearchSugAdapter = new SearchSugAdapter(this);
        mDataBinding.homeWelfareSearchRv.setLayoutManager(new LinearLayoutManager(this));
        mDataBinding.homeWelfareSearchRv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = 16;
            }
        });
        mDataBinding.homeWelfareSearchRv.setAdapter(mSearchSugAdapter);
    }

    private void loadRefreshList() {
        mViewModel.getPerfectGoodsData(from, 1).observe(this, homeGoodsBean -> {
            if (homeGoodsBean == null || homeGoodsBean.getList() == null || homeGoodsBean.getList().size() <= 0) {
                mDataBinding.homeWelfareLoadingStatusTv.setText("数据加载失败，点击重试.");
                mDataBinding.homeWelfareSrl.finishRefresh();
                return;
            }
            showPerfectGoodsData(homeGoodsBean, false);
        });
    }

    private void loadMoreList() {
        mPageId++;
        if (mCurKeyWord == null || mCurKeyWord.equalsIgnoreCase("")) {
            mViewModel.getPerfectGoodsData(from, mPageId).observe(this, perfectGoodsBean -> {
                mDataBinding.homeWelfareSrl.finishLoadMore();
                if (perfectGoodsBean == null || perfectGoodsBean.getList() == null || perfectGoodsBean.getList().size() <= 0) {
                    mPageId--;
                    mDataBinding.homeWelfareLoadingStatusTv.setText("数据加载失败，点击重试.");
                    mDataBinding.homeWelfareSrl.finishRefresh();
                    return;
                }
                showPerfectGoodsData(perfectGoodsBean, true);
            });
        } else {
            mViewModel.getSearchListData(mPageId, mCurKeyWord, mSrc).observe(this, homeGoodsBean -> {
                mDataBinding.homeWelfareSrl.finishLoadMore();
                if (homeGoodsBean == null || homeGoodsBean.getList() == null || homeGoodsBean.getList().size() <= 0) {
                    mDataBinding.homeWelfareLoadingStatusTv.setText("数据加载失败，点击重试.");
                    mPageId--;
                    return;
                }
                mPerfectGoodsAdapter.refreshData(homeGoodsBean.getList(), true);
            });
        }
    }

    private void showPerfectGoodsData(HomeGoodsBean homeGoodsBean, boolean isAdd) {
        mPerfectGoodsAdapter.refreshData(homeGoodsBean.getList(), isAdd);

        mDataBinding.homeWelfareGoodsRv.setVisibility(View.VISIBLE);
        mDataBinding.homeWelfareLoadingStatusTv.setVisibility(View.GONE);
        mDataBinding.homeWelfareSrl.finishRefresh();
        mDataBinding.homeWelfareSrl.finishLoadMore();
    }

    private void search(String keyWord) {
        mViewModel.getSearchData(keyWord).observe(this, searchSugBean -> {
            if (searchSugBean == null || searchSugBean.getList() == null || searchSugBean.getList().size() <= 0) {
                return;
            }

            mDataBinding.homeWelfareGoodsRv.setVisibility(View.GONE);
            mDataBinding.homeWelfareLoadingStatusTv.setVisibility(View.GONE);
            mDataBinding.homeWelfareSearchRv.setVisibility(View.VISIBLE);
            mSearchSugAdapter.refreshData(searchSugBean.getList());
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(String keyWord) {
        if (mCurKeyWord != null && mCurKeyWord.equalsIgnoreCase(keyWord)) {
            return;
        }

        mCurKeyWord = keyWord;

        mDataBinding.homeWelfareGoodsRv.setVisibility(View.GONE);
        mDataBinding.homeWelfareSearchRv.setVisibility(View.GONE);
        mDataBinding.homeWelfareLoadingStatusTv.setVisibility(View.VISIBLE);
        mPageId = 1;

        mViewModel.getSearchListData(mPageId, mCurKeyWord, mSrc).observe(this, homeGoodsBean -> {
            if (homeGoodsBean == null || homeGoodsBean.getList() == null || homeGoodsBean.getList().size() <= 0) {
                mDataBinding.homeWelfareLoadingStatusTv.setText("数据加载失败，点击重试.");
                return;
            }

            mDataBinding.homeWelfareGoodsRv.setVisibility(View.VISIBLE);
            mDataBinding.homeWelfareSearchRv.setVisibility(View.GONE);
            mDataBinding.homeWelfareLoadingStatusTv.setVisibility(View.GONE);
            mPerfectGoodsAdapter.refreshData(homeGoodsBean.getList(), false);
        });
    }

    @Override
    public void onClick(String goodsId, String materialId, String searchId, int src) {
        Context context = this;

        if (!OtherSwitch.Ins().isOpenJumpDlg()) {
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