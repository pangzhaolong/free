package com.donews.front;


import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.dn.events.events.FrontScrollEvent;
import com.dn.events.events.LotteryStatusEvent;
import com.donews.common.base.MvvmLazyLiveDataFragment;
import com.donews.common.router.RouterFragmentPath;
import com.donews.front.adapter.FrontGoodsAdapter;
import com.donews.front.databinding.FrontNorFragmentBinding;
import com.donews.front.listener.FrontClickListener;
import com.donews.front.viewModel.NorViewModel;
import com.donews.middle.bean.front.LotteryCategoryBean;
import com.donews.middle.bean.front.LotteryGoodsBean;
import com.donews.middle.cache.GoodsCache;
import com.donews.middle.decoration.GridSpaceItemDecoration;
import com.donews.utilslibrary.analysis.AnalysisUtils;
import com.donews.utilslibrary.dot.Dot;
import com.donews.utilslibrary.utils.KeySharePreferences;
import com.donews.utilslibrary.utils.SPUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class FrontGoodsFragment extends MvvmLazyLiveDataFragment<FrontNorFragmentBinding, NorViewModel> implements FrontClickListener {

    private FrontGoodsAdapter mNorGoodsAdapter;
    private LotteryCategoryBean.categoryBean mCategoryBean;
    private int mPageId = 0;
    private RecyclerView.ItemDecoration mItemDecoration;
    private int mRvScrollLength = 0;

    private int mCurrentPosition = 0;

    public FrontGoodsFragment() {

    }

    public FrontGoodsFragment(LotteryCategoryBean.categoryBean categoryBean) {
        mCategoryBean = categoryBean;
    }

    @Override
    public int getLayoutId() {
        return R.layout.front_nor_fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPageId = 0;

        mDataBinding.frontNorLoadingLl.setVisibility(View.VISIBLE);
        mDataBinding.frontNorRv.setVisibility(View.GONE);
        mNorGoodsAdapter = new FrontGoodsAdapter(this.getContext(), this);

        int nItemDecorationCount = mDataBinding.frontNorRv.getItemDecorationCount();
        for (int i = 0; i < nItemDecorationCount; i++) {
            mDataBinding.frontNorRv.removeItemDecorationAt(i);
        }

        if (mCategoryBean != null && mCategoryBean.getCols() == 1) {
            mItemDecoration = new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                    outRect.top = 32;
                }
            };
            mDataBinding.frontNorRv.addItemDecoration(mItemDecoration);
            mDataBinding.frontNorRv.setLayoutManager(new LinearLayoutManager(this.getContext()));
            mNorGoodsAdapter.setCols(1);
        } else {
            mItemDecoration = new GridSpaceItemDecoration(2);
            mDataBinding.frontNorRv.addItemDecoration(mItemDecoration);
            mDataBinding.frontNorRv.setLayoutManager(new GridLayoutManager(this.getContext(), 2));
            mNorGoodsAdapter.setCols(2);
        }
        mDataBinding.frontNorRv.setAdapter(mNorGoodsAdapter);

        mDataBinding.frontLoadingStatusTv.setOnClickListener(v -> loadNorData());

        if (mCategoryBean != null) {
            LotteryGoodsBean lotteryGoodsBean = GoodsCache.readGoodsBean(LotteryGoodsBean.class, "home_" + mCategoryBean.getCategoryId());
            showNorData(lotteryGoodsBean, true);
        }
        loadNorData();

        initSrl();

        EventBus.getDefault().register(this);
        mDataBinding.frontNorRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mRvScrollLength += dy;
                checkScrollDy();
            }
        });

        checkScrollDy();
    }

    private void checkScrollDy() {
        if (mRvScrollLength >= 5000) {
            EventBus.getDefault().post(new FrontScrollEvent(0, 1));
        } else {
            EventBus.getDefault().post(new FrontScrollEvent(0, 0));
        }
    }

    private void initSrl() {
        mDataBinding.frontNorSrl.setEnableRefresh(false);
        mDataBinding.frontNorSrl.setEnableAutoLoadMore(false);
        mDataBinding.frontNorSrl.setOnLoadMoreListener(refreshLayout -> {
            loadNorData();
            mDataBinding.frontNorSrl.finishLoadMore();
        });
    }

    public void reloadNorData(int position) {
        mCurrentPosition = position;
        mPageId = 0;
        loadNorData();
    }

    public void gotoTopPosition() {
        mRvScrollLength = 0;
        mDataBinding.frontNorRv.scrollToPosition(0);
        EventBus.getDefault().post(new FrontScrollEvent(0, 0));
    }

    private void loadNorData() {
        if (mDataBinding.frontLoadingStatusTv != null) {
            mDataBinding.frontLoadingStatusTv.setText("数据加载中...");
        }
        if (mCategoryBean == null) {
            return;
        }
        mPageId++;
        mViewModel.getNetData(mCategoryBean.getCategoryId(), mPageId).observe(getViewLifecycleOwner(), norGoodsBean -> {
            if (norGoodsBean == null || norGoodsBean.getList() == null || norGoodsBean.getList().size() <= 0) {
                mPageId--;
                if (mDataBinding.frontLoadingStatusTv != null) {
                    mDataBinding.frontLoadingStatusTv.setText("加载数据失败，点击重新加载");
                }
                mDataBinding.frontNorSrl.finishLoadMoreWithNoMoreData();
                return;
            }

            showNorData(norGoodsBean, false);
            GoodsCache.saveGoodsBean(norGoodsBean, "home_" + mCategoryBean.getCategoryId());
        });
    }

    private void showNorData(LotteryGoodsBean lotteryGoodsBean, boolean check) {
        if (check) {
            if (lotteryGoodsBean == null || lotteryGoodsBean.getList() == null || lotteryGoodsBean.getList().size() <= 0) {
                return;
            }
        }

        mNorGoodsAdapter.refreshData(lotteryGoodsBean.getList(), mPageId == 1
                , mCurrentPosition == 0 && SPUtils.getInformain(KeySharePreferences.IS_FIRST_IN_APP, 0) == 1);
        mDataBinding.frontNorLoadingLl.setVisibility(View.GONE);
        mDataBinding.frontNorRv.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(int position, String goodsId) {
        ARouter.getInstance()
                .build(RouterFragmentPath.Lottery.PAGER_LOTTERY)
                .withBoolean("needLotteryEvent", true)
                .withInt("position", position)
                .withString("goods_id", goodsId)
                .navigation();
        if (mCategoryBean == null) {
            return;
        }
        AnalysisUtils.onEventEx(this.getContext(), Dot.But_Goto_Lottery, mCategoryBean.getName());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(LotteryStatusEvent event) {
        if (event.object == null) {
            return;
        }

        List<String> list = (List<String>) event.object;
        int lotteryStatus = 0;
        if (list.size() <= 0) {
            lotteryStatus = 0;
        } else if (list.size() < 6) {
            lotteryStatus = 1;
        } else {
            lotteryStatus = 2;
        }
        mNorGoodsAdapter.refreshItem(event.position, event.goodsId, lotteryStatus);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mItemDecoration = null;
        if (mNorGoodsAdapter != null) {
            mNorGoodsAdapter.clear();
        }

        EventBus.getDefault().unregister(this);
    }
}
