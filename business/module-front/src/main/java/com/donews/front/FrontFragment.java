package com.donews.front;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.donews.base.fragment.MvvmLazyLiveDataFragment;
import com.donews.common.router.RouterFragmentPath;
import com.donews.front.adapter.FragmentAdapter;
import com.donews.front.bean.LotteryCategoryBean;
import com.donews.front.databinding.FrontFragmentBinding;
import com.donews.front.viewModel.FrontViewModel;
import com.donews.front.views.TabItem;
import com.donews.utilslibrary.utils.LogUtil;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

@Route(path = RouterFragmentPath.Front.PAGER_FRONT)
public class FrontFragment extends MvvmLazyLiveDataFragment<FrontFragmentBinding, FrontViewModel> {

    private FragmentAdapter mFragmentAdapter;
    private LotteryCategoryBean mLotteryCategoryBean;

    private Context mContext;

    @Override
    public int getLayoutId() {
        return R.layout.front_fragment;
    }

    @SuppressLint("StringFormatMatches")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mContext = this.getContext();

        Glide.with(this).load("https://wx4.sinaimg.cn/orj360/95936907ly8gdi6ssvmecj20u00u0q51.jpg").into(mDataBinding.frontGiftHeadIv);
        mDataBinding.frontGiftText.setText(Html.fromHtml(String.format(getString(R.string.front_gift_text),
                "x9527", "iPhone13")));

        mDataBinding.frontVScrollLl.startLoop();

        mFragmentAdapter = new FragmentAdapter(this);
        mDataBinding.frontVp2.setAdapter(mFragmentAdapter);
        mDataBinding.frontCategoryTl.setTabMode(TabLayout.MODE_SCROLLABLE);

        TabLayoutMediator tab = new TabLayoutMediator(mDataBinding.frontCategoryTl, mDataBinding.frontVp2, (tab1, position) -> {
            if (position == 0) {
                tab1.setCustomView(new TabItem(mContext));
                TabItem tabItem = (TabItem) tab1.getCustomView();
                assert tabItem != null;
                tabItem.setTitle("惊喜大奖");
                tabItem.selected();
            } else {
                LogUtil.e("xx: in" + position);
                tab1.setCustomView(new TabItem(mContext));
                TabItem tabItem = (TabItem) tab1.getCustomView();
                assert tabItem != null;
                if (mLotteryCategoryBean == null) {
                    LogUtil.e("xx: in 1" + position);
                    return;
                }
                if (mLotteryCategoryBean.getList() == null) {
                    LogUtil.e("xx: in 3" + position);
                }
                LogUtil.e("xx: out" + position + mLotteryCategoryBean.getList().get(position - 1).getName());
                tabItem.setTitle(mLotteryCategoryBean.getList().get(position - 1).getName());
            }
        });
        tab.attach();

        mDataBinding.frontCategoryTl.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                TabItem tabItem = (TabItem) tab.getCustomView();
                assert tabItem != null;
                tabItem.selected();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                TabItem tabItem = (TabItem) tab.getCustomView();
                assert tabItem != null;
                tabItem.unSelected();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mViewModel.getNetData().observe(getViewLifecycleOwner(), categoryBean -> {
            if (categoryBean == null) {
                return;
            }

            mLotteryCategoryBean = categoryBean;
            mFragmentAdapter.refreshData(categoryBean.getList());
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mDataBinding.frontVScrollLl.startLoop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mDataBinding.frontVScrollLl.stopLoop();
    }
}
