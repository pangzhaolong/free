package com.donews.front;


import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.donews.base.fragment.MvvmLazyLiveDataFragment;
import com.donews.front.adapter.FrontGoodsAdapter;
import com.donews.front.bean.LotteryCategoryBean;
import com.donews.front.databinding.FrontGiftFragmentBinding;
import com.donews.front.databinding.FrontNorFragmentBinding;
import com.donews.front.viewModel.GiftViewModel;
import com.donews.front.viewModel.NorViewModel;

public class NorFragment extends MvvmLazyLiveDataFragment<FrontNorFragmentBinding, NorViewModel> {

    private FrontGoodsAdapter mFrontGoodsAdapter;

    public NorFragment(LotteryCategoryBean.categoryBean categoryBean) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.front_nor_fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDataBinding.frontNorSrl.setEnabled(false);
        mDataBinding.frontNorLoadingLl.setVisibility(View.GONE);
        mDataBinding.frontNorRv.setVisibility(View.VISIBLE);
        mFrontGoodsAdapter = new FrontGoodsAdapter(this.getContext());
        mDataBinding.frontNorRv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.bottom = 10;
            }
        });
        mDataBinding.frontNorRv.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mDataBinding.frontNorRv.setAdapter(mFrontGoodsAdapter);

    }
}
