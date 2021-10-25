package com.donews.front;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.donews.base.fragment.MvvmLazyLiveDataFragment;
import com.donews.common.router.RouterActivityPath;
import com.donews.common.router.RouterFragmentPath;
import com.donews.front.adapter.FragmentAdapter;
import com.donews.front.bean.LotteryCategoryBean;
import com.donews.front.bean.WalletBean;
import com.donews.front.databinding.FrontFragmentBinding;
import com.donews.front.viewModel.FrontViewModel;
import com.donews.front.views.TabItem;
import com.donews.utilslibrary.utils.LogUtil;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

@Route(path = RouterFragmentPath.Front.PAGER_FRONT)
public class FrontFragment extends MvvmLazyLiveDataFragment<FrontFragmentBinding, FrontViewModel> implements View.OnClickListener {

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
                tab1.setCustomView(new TabItem(mContext));
                TabItem tabItem = (TabItem) tab1.getCustomView();
                assert tabItem != null;
                if (mLotteryCategoryBean == null) {
                    return;
                }
                if (mLotteryCategoryBean.getList() == null) {
                }
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


    private void loadRpData() {
        mViewModel.getRpData().observe(this.getViewLifecycleOwner(), walletBean -> {
            if (walletBean == null || walletBean.getList() == null || walletBean.getList().size() != 5) {
                return;
            }

            showRpData(walletBean);
        });
    }

    @SuppressLint("SetTextI18n")
    private void showRpData(WalletBean walletBean) {
        WalletBean.RpBean rpBean = walletBean.getList().get(0);
        mDataBinding.frontRpOpenFl1.setTag(rpBean);
        mDataBinding.frontRpOpenFl1.setOnClickListener(this);
        if (rpBean.getHadLotteryTotal() == -1) {
            mDataBinding.frontRpIv1.setAlpha(0.5f);
            mDataBinding.frontRpTv1.setText(rpBean.getLotteryTotal() + "/" + rpBean.getLotteryTotal());
        } else {
            mDataBinding.frontRpTv1.setText(rpBean.getHadLotteryTotal() + "/" + rpBean.getLotteryTotal());
        }
        if (!rpBean.getOpened()) {
            if (rpBean.getHadLotteryTotal() < rpBean.getLotteryTotal()) {
                mDataBinding.frontRpIv1.setBackgroundResource(R.drawable.front_rp_wait);
            } else {
                mDataBinding.frontRpIv1.setBackgroundResource(R.drawable.front_rp_close);
            }
        } else {
            mDataBinding.frontRpIv1.setBackgroundResource(R.drawable.front_rp_open);
        }
        rpBean = walletBean.getList().get(1);
        mDataBinding.frontRpOpenFl2.setTag(rpBean);
        mDataBinding.frontRpOpenFl2.setOnClickListener(this);
        if (rpBean.getHadLotteryTotal() == -1) {
            mDataBinding.frontRpIv2.setAlpha(0.5f);
            mDataBinding.frontRpTv2.setText(rpBean.getLotteryTotal() + "/" + rpBean.getLotteryTotal());
        } else {
            mDataBinding.frontRpTv2.setText(rpBean.getHadLotteryTotal() + "/" + rpBean.getLotteryTotal());
        }
        if (!rpBean.getOpened()) {
            if (rpBean.getHadLotteryTotal() != -1 && rpBean.getHadLotteryTotal() < rpBean.getLotteryTotal()) {
                mDataBinding.frontRpIv2.setBackgroundResource(R.drawable.front_rp_wait);
            } else {
                mDataBinding.frontRpIv2.setBackgroundResource(R.drawable.front_rp_close);
            }
        } else {
            mDataBinding.frontRpIv2.setBackgroundResource(R.drawable.front_rp_open);
        }
        rpBean = walletBean.getList().get(2);
        mDataBinding.frontRpOpenFl3.setTag(rpBean);
        mDataBinding.frontRpOpenFl3.setOnClickListener(this);
        if (rpBean.getHadLotteryTotal() == -1) {
            mDataBinding.frontRpIv3.setAlpha(0.5f);
            mDataBinding.frontRpTv3.setText(rpBean.getLotteryTotal() + "/" + rpBean.getLotteryTotal());
        } else {
            mDataBinding.frontRpTv3.setText(rpBean.getHadLotteryTotal() + "/" + rpBean.getLotteryTotal());
        }
        if (!rpBean.getOpened()) {
            if (rpBean.getHadLotteryTotal() != -1 && rpBean.getHadLotteryTotal() < rpBean.getLotteryTotal()) {
                mDataBinding.frontRpIv3.setBackgroundResource(R.drawable.front_rp_wait);
            } else {
                mDataBinding.frontRpIv3.setBackgroundResource(R.drawable.front_rp_close);
            }
        } else {
            mDataBinding.frontRpIv3.setBackgroundResource(R.drawable.front_rp_open);
        }
        rpBean = walletBean.getList().get(3);
        mDataBinding.frontRpOpenFl4.setTag(rpBean);
        mDataBinding.frontRpOpenFl4.setOnClickListener(this);
        if (rpBean.getHadLotteryTotal() == -1) {
            mDataBinding.frontRpIv4.setAlpha(0.5f);
            mDataBinding.frontRpTv4.setText(rpBean.getLotteryTotal() + "/" + rpBean.getLotteryTotal());
        } else {
            mDataBinding.frontRpTv4.setText(rpBean.getHadLotteryTotal() + "/" + rpBean.getLotteryTotal());
        }
        if (!rpBean.getOpened()) {
            if (rpBean.getHadLotteryTotal() != -1 && rpBean.getHadLotteryTotal() < rpBean.getLotteryTotal()) {
                mDataBinding.frontRpIv4.setBackgroundResource(R.drawable.front_rp_wait);
            } else {
                mDataBinding.frontRpIv4.setBackgroundResource(R.drawable.front_rp_close);
            }
        } else {
            mDataBinding.frontRpIv4.setBackgroundResource(R.drawable.front_rp_open);
        }
        rpBean = walletBean.getList().get(4);
        mDataBinding.frontRpOpenFl5.setTag(rpBean);
        mDataBinding.frontRpOpenFl5.setOnClickListener(this);
        if (rpBean.getHadLotteryTotal() == -1) {
            mDataBinding.frontRpIv5.setAlpha(0.5f);
            mDataBinding.frontRpTv5.setText(rpBean.getLotteryTotal() + "/" + rpBean.getLotteryTotal());
        } else {
            mDataBinding.frontRpTv5.setText(rpBean.getHadLotteryTotal() + "/" + rpBean.getLotteryTotal());
        }
        if (!rpBean.getOpened()) {
            if (rpBean.getHadLotteryTotal() != -1 && rpBean.getHadLotteryTotal() < rpBean.getLotteryTotal()) {
                mDataBinding.frontRpIv5.setBackgroundResource(R.drawable.front_rp_wait);
            } else {
                mDataBinding.frontRpIv5.setBackgroundResource(R.drawable.front_rp_close);
            }
        } else {
            mDataBinding.frontRpIv5.setBackgroundResource(R.drawable.front_rp_open);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        mDataBinding.frontVScrollLl.startLoop();
        loadRpData();
    }

    @Override
    public void onPause() {
        super.onPause();
//        mDataBinding.frontVScrollLl.stopLoop();
    }

    @Override
    public void onClick(View v) {
        WalletBean.RpBean rpBean = (WalletBean.RpBean) v.getTag();
        if (rpBean == null) {
            return;
        }

        if (rpBean.getOpened()) {
            Toast.makeText(this.getContext(), "这个红包未已经开过了哦！", Toast.LENGTH_SHORT).show();
            return;
        }

        if (rpBean.getHadLotteryTotal() != -1 && rpBean.getHadLotteryTotal() < rpBean.getLotteryTotal()) {
            Toast.makeText(this.getContext(), "快去抽奖赚取开启红包次数吧！", Toast.LENGTH_SHORT).show();
            return;
        }

        if (rpBean.getHadLotteryTotal() == -1) {
            Toast.makeText(this.getContext(), "前面还有红包未开启哦！", Toast.LENGTH_SHORT).show();
        }

        openRp();
    }

    private void openRp() {
        mViewModel.openRpData().observe(this.getViewLifecycleOwner(), redPacketBean -> {
            if (redPacketBean == null || redPacketBean.getAward() == null) {
                Toast.makeText(this.getContext(), "开启红包失败，请稍后再试或者反馈给我们，谢谢！", Toast.LENGTH_SHORT).show();
                return;
            }
            ARouter.getInstance().build(RouterActivityPath.Rp.PAGE_RP)
                    .withInt("type", redPacketBean.getAward().getType())
                    .withFloat("score", redPacketBean.getAward().getScore())
                    .navigation();
        });
    }
}
