package com.donews.home;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.GridLayoutManager;

import com.blankj.utilcode.util.KeyboardUtils;
import com.donews.base.utils.ToastUtil;
import com.donews.common.base.MvvmBaseLiveDataActivity;
import com.donews.home.adapter.HomeSearch2Adapter;
import com.donews.home.databinding.HomeSearch2SearchBinding;
import com.donews.home.dialogs.ExchangeGoodJbFragmentDialog;
import com.donews.home.listener.GoodsClickListener;
import com.donews.home.listener.SearchListener;
import com.donews.home.viewModel.NorViewModel;
import com.donews.home.viewModel.SearchViewModel;
import com.donews.home.views.SearchHistoryItem;
import com.donews.middle.bean.home.HomeExchangeReq;
import com.donews.middle.bean.home.HomeExchangeResp;
import com.donews.middle.bean.home.TmpSearchHistory;
import com.donews.middle.centralDeploy.ABSwitch;
import com.donews.middle.dialog.JumpThirdAppDialog;
import com.donews.middle.go.GotoUtil;
import com.donews.middle.listener.JumpThirdAppListener;
import com.donews.middle.searchs.TopSearchConfig;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;

/**
 * <p> </p>
 * 作者： created by dw<br>
 * 日期： 2022/5/13 14:13<br>
 * 版本：V1.0<br>
 * 心愿社的搜索页面。新版的搜索页面
 */
public class HomeSearch2Activity extends MvvmBaseLiveDataActivity<HomeSearch2SearchBinding, SearchViewModel> implements
        GoodsClickListener, SearchListener {

    private Context mContext;

    private String mPreKeyWord = "";
    private int mPageId = 0;
    private HomeSearch2Adapter adapter;
    private boolean isRefesh = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this)
                .statusBarColor(R.color.white)
                .navigationBarColor(R.color.black)
                .fitsSystemWindows(true)
                .autoDarkModeEnable(true)
                .init();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.home_search2_search;
    }

    @Override
    public void initView() {
        mContext = this;
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
                    return;
                }
//                search(s.toString());
            }
        });

        mDataBinding.homeSearchDo.setOnClickListener(v -> {
            hideSoftInput();
            isRefesh = true;
            search(mDataBinding.homeSearchEdit.getText().toString(), "1");
        });

        mDataBinding.homeSearchFlowLayout.setFlow(true);
        mDataBinding.homeSearchFlowLayout.setMaxRows(4);
        reloadSearchHistory();

        mDataBinding.homeSearchHistoryCleanTv.setOnClickListener(v -> {
            reloadSearchHistory();
        });

        adapter = new HomeSearch2Adapter(this, (item) -> {
            NorViewModel norViewModel = new NorViewModel();
            HomeExchangeReq req = new HomeExchangeReq();
            req.goods_id = item.goods_id;
            showLoading("兑换中");
            norViewModel.getExchanageGoodsData(req)
                    .observe(this, (homeExchangeResp -> {
                        hideLoading();
                        showExchangeDialog(homeExchangeResp);
                    }));
        });
        mDataBinding.homeSearchList.setLayoutManager(new GridLayoutManager(this, 2));
        mDataBinding.homeSearchList.setAdapter(adapter);

        mDataBinding.homeSearchRefresh.setEnableRefresh(false);
        mDataBinding.homeSearchRefresh.setOnLoadMoreListener(refreshLayout -> {
            isRefesh = false;
            search(mPreKeyWord, "" + Math.ceil(adapter.getData().size() / (20 * 1.0F)));
        });
    }

    //显示Dialog
    private void showExchangeDialog(HomeExchangeResp resp) {
        if (resp.status > 2) {
            if (resp.status == 4) {
                ToastUtil.showShort(this, "兑换成功!");
            } else {
                ToastUtil.showShort(this, "商品已兑完");
            }
            return;
        }
        if (resp.status < 0) {
            ToastUtil.showShort(this, "抱歉，状态错误");
            return;
        }
        int type = 0;
        if (resp.status == 2) {
            type = 1;
        }
        //显示弹窗
        ExchangeGoodJbFragmentDialog.getInstance(type, resp.diff)
                .show(getSupportFragmentManager(), ExchangeGoodJbFragmentDialog.class.getSimpleName());
    }

    private void search(String keyWord, String page_id) {
        String strKeyWord = keyWord.trim();
        if ("".equals(keyWord.trim())) {
            mDataBinding.homeSearchNotDataLl.setVisibility(View.GONE);
            mDataBinding.homeSearchHistoryTl.setVisibility(View.VISIBLE);
        } else {
            mDataBinding.homeSearchHistoryTl.setVisibility(View.GONE);
        }
        if (mPreKeyWord.equalsIgnoreCase(strKeyWord) || strKeyWord.equalsIgnoreCase("")) {
            return;
        }
        mViewModel.getSearchNewData(strKeyWord, page_id, 20)
                .observe((LifecycleOwner) mContext, searchSugBean -> {
                    mDataBinding.homeSearchRefresh.finishLoadMore();
                    if (isRefesh) {
                        mDataBinding.homeSearchRefresh.setNoMoreData(true);
                    }
                    if (searchSugBean == null || searchSugBean.getList() == null || searchSugBean.getList().size() <= 0) {
                        adapter.refreshData(new ArrayList<>(), isRefesh);
                        if (isRefesh) {
                            mDataBinding.homeSearchNotDataLl.setVisibility(View.VISIBLE);
                        } else {
                            mDataBinding.homeSearchNotDataLl.setVisibility(View.GONE);
                            mDataBinding.homeSearchRefresh.setNoMoreData(false);
                        }
                        return;
                    }
                    if (!isRefesh && searchSugBean.getList().size() < 20) {
                        mDataBinding.homeSearchRefresh.setNoMoreData(false);
                    }
                    mDataBinding.homeSearchNotDataLl.setVisibility(View.GONE);
                    adapter.refreshData(searchSugBean.getList(), isRefesh);
                });
        mPreKeyWord = strKeyWord;
    }

    private void reloadSearchHistory() {
        if (TopSearchConfig.Ins().getTopSearchList().size() <= 0) {
            mDataBinding.homeSearchHistoryTl.setVisibility(View.GONE);
        } else {
            mDataBinding.homeSearchHistoryTl.setVisibility(View.VISIBLE);
            mDataBinding.homeSearchFlowLayout.removeAllViews();
            for (int i = 0; i < TopSearchConfig.Ins().getTopSearchList().size(); i++) {
                mDataBinding.homeSearchFlowLayout.addView(new SearchHistoryItem(mContext,
                        TopSearchConfig.Ins().getTopSearchList().get(i), this));
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
            mDataBinding.homeSearchEdit.setText("");
            reloadSearchHistory();
            return;
        }

        if (mHistoryIndex < -1) {
            finish();
            return;
        }
    }

    @Override
    public void onClick(String keyWord) {
        mDataBinding.homeSearchEdit.setText(keyWord);
        mDataBinding.homeSearchEdit.setSelection(keyWord.length());
        isRefesh = true;
        search(mDataBinding.homeSearchEdit.getText().toString(), "1");
        hideSoftInput();
    }

    @Override
    protected void onPause() {
        super.onPause();
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

    //隐藏输入法
    private void hideSoftInput() {
        KeyboardUtils.hideSoftInput(mDataBinding.homeSearchEdit);
    }
}