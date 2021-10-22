package com.donews.front;


import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.donews.base.fragment.MvvmLazyLiveDataFragment;
import com.donews.front.adapter.GiftGoodsAdapter;
import com.donews.front.databinding.FrontGiftFragmentBinding;
import com.donews.front.viewModel.GiftViewModel;

public class GiftFragment extends MvvmLazyLiveDataFragment<FrontGiftFragmentBinding, GiftViewModel> {

    private GiftGoodsAdapter mFrontGoodsAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.front_gift_fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        mDataBinding.frontGiftRv.setNestedScrollingEnabled(false);

        mDataBinding.frontGiftSrl.setEnabled(false);
        mDataBinding.frontGiftLoadingLl.setVisibility(View.GONE);
        mDataBinding.frontGiftRv.setVisibility(View.VISIBLE);
        mFrontGoodsAdapter = new GiftGoodsAdapter(this.getContext());
        mDataBinding.frontGiftRv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.bottom = 10;
            }
        });
        mDataBinding.frontGiftRv.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mDataBinding.frontGiftRv.setAdapter(mFrontGoodsAdapter);

    }
}
