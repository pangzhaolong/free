package com.donews.home.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.donews.base.utils.ToastUtil;
import com.donews.common.base.MvvmBaseLiveDataActivity;
import com.donews.common.base.MvvmLazyLiveDataFragment;
import com.donews.home.R;
import com.donews.home.adapter.ExchangeFragmentTabGoodsAdapter;
import com.donews.home.databinding.HomeExchangeGoodNotDialogBinding;
import com.donews.home.databinding.HomeFragmentExchangeTabBinding;
import com.donews.home.dialogs.ExchangeGoodJbFragmentDialog;
import com.donews.home.viewModel.NorViewModel;
import com.donews.middle.bean.home.HomeCategory2Bean;
import com.donews.middle.bean.home.HomeCategoryBean;
import com.donews.middle.bean.home.HomeExchangeGoodsBean;
import com.donews.middle.bean.home.HomeExchangeReq;
import com.donews.middle.bean.home.HomeExchangeResp;
import com.donews.middle.bean.home.HomeGoodsBean;
import com.donews.middle.bean.home.SearchRespBean;
import com.donews.middle.cache.GoodsCache;
import com.donews.middle.decoration.GridSpaceItemDecoration;

/**
 * 首页 -> 每个Tab所对应的Fragment
 */
public class ExchangeTabFragment extends MvvmLazyLiveDataFragment<HomeFragmentExchangeTabBinding, NorViewModel>
        implements ExchangeFragmentTabGoodsAdapter.GoodsClickListener {

    private HomeCategory2Bean.Category2Item mCategoryItem;
    private ExchangeFragmentTabGoodsAdapter mNorGoodsAdapter;

    private int mPageId = 0;
    private RecyclerView.ItemDecoration mItemDecoration;

    public ExchangeTabFragment(HomeCategory2Bean.Category2Item categoryItem) {
        mCategoryItem = categoryItem;
    }

    @Override
    public int getLayoutId() {
        return R.layout.home_fragment_exchange_tab;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPageId = 0;
        mDataBinding.homeNorSrl.setVisibility(View.GONE);
        mDataBinding.homeNorLoadingLl.setVisibility(View.VISIBLE);

        mNorGoodsAdapter = new ExchangeFragmentTabGoodsAdapter(this.getContext(), this);

        int nItemDecorationCount = mDataBinding.homeNorGoodsRv.getItemDecorationCount();
        for (int i = 0; i < nItemDecorationCount; i++) {
            mDataBinding.homeNorGoodsRv.removeItemDecorationAt(i);
        }
        mItemDecoration = new GridSpaceItemDecoration(2);
        mDataBinding.homeNorGoodsRv.addItemDecoration(mItemDecoration);
        mDataBinding.homeNorGoodsRv.setLayoutManager(new GridLayoutManager(this.getContext(), 2));
        mDataBinding.homeNorGoodsRv.setAdapter(mNorGoodsAdapter);

        if (mCategoryItem != null) {
            HomeExchangeGoodsBean tmpHomeGoodsBean = GoodsCache.readGoodsBean(HomeGoodsBean.class, mCategoryItem.category_id);
            showNorGoodsBean(tmpHomeGoodsBean);
        }

        loadMoreData();
        initSrl();
    }

    /**
     * 对话按钮的点击
     *
     * @param item 商品信息
     */
    @Override
    public void onExchanageClick(SearchRespBean.SearchRespItemBean item) {
        HomeExchangeReq req = new HomeExchangeReq();
        req.goods_id = item.goods_id;
        showLoading("兑换中");
        mViewModel.getExchanageGoodsData(req)
                .observe(this, (homeExchangeResp -> {
                    hideLoading();
                    if (homeExchangeResp == null) {
                        ToastUtil.showShort(getActivity(), "数据获取失败");
                        return;
                    }
                    showExchangeDialog(homeExchangeResp);
                }));
    }

    //显示Dialog
    private void showExchangeDialog(HomeExchangeResp resp) {
        if (resp.status > 2) {
            if (resp.status == 4) {
                ToastUtil.showShort(getContext(), "兑换成功!");
            } else {
                ToastUtil.showShort(getContext(), "商品已兑完");
            }
            return;
        }
        if (resp.status < 0) {
            ToastUtil.showShort(getContext(), "抱歉，状态错误");
            return;
        }
        int type = 0;
        if (resp.status == 2) {
            type = 1;
        }
        //显示弹窗
        ExchangeGoodJbFragmentDialog.getInstance(type, resp.diff)
                .show(getChildFragmentManager(), ExchangeGoodJbFragmentDialog.class.getSimpleName());
    }

    private void initSrl() {
        mDataBinding.homeNorSrl.setEnableRefresh(false);
        mDataBinding.homeNorSrl.setEnableAutoLoadMore(false);
        mDataBinding.homeNorSrl.setOnLoadMoreListener(refreshLayout -> loadMoreData());
    }

    private void loadMoreData() {
        if (mCategoryItem == null) {
            return;
        }
        mPageId++;
        mViewModel.getExchanageGoodsData(mCategoryItem.category_id, mPageId).observe(getViewLifecycleOwner(), homeGoodsBean -> {
            mDataBinding.homeNorSrl.finishLoadMore();

            if (homeGoodsBean == null || homeGoodsBean.getList() == null || homeGoodsBean.getList().size() <= 0) {
                mPageId--;
                return;
            }
            showNorGoodsBean(homeGoodsBean);
        });
    }

    private void showNorGoodsBean(HomeExchangeGoodsBean homeGoodsBean) {
        if (homeGoodsBean == null || homeGoodsBean.getList() == null || homeGoodsBean.getList().size() <= 0) {
            return;
        }

        mNorGoodsAdapter.refreshData(homeGoodsBean.getList(), mPageId == 1);

        if (mCategoryItem != null) {
            GoodsCache.saveGoodsBean(homeGoodsBean, mCategoryItem.category_id);
        }
        mDataBinding.homeNorLoadingLl.setVisibility(View.GONE);
        mDataBinding.homeNorSrl.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mItemDecoration = null;
    }
}
