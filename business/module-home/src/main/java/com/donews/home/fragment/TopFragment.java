package com.donews.home.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.donews.base.fragment.MvvmLazyLiveDataFragment;
import com.donews.common.router.RouterActivityPath;
import com.donews.common.router.RouterFragmentPath;
import com.donews.home.R;
import com.donews.home.adapter.GridAdapter;
import com.donews.home.adapter.TopBannerViewAdapter;
import com.donews.home.adapter.TopGoodsAdapter;
import com.donews.home.bean.TopBannerBean;
import com.donews.home.databinding.HomeFragmentTopBinding;
import com.donews.home.listener.GoodsDetailListener;
import com.donews.home.viewModel.TopViewModel;
import com.donews.utilslibrary.utils.LogUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class TopFragment extends MvvmLazyLiveDataFragment<HomeFragmentTopBinding, TopViewModel> implements GoodsDetailListener {


    private GridAdapter mGridAdapter;
    private TopGoodsAdapter mTopGoodsAdapter;

    private String mCurDdqTime = "";
    private String mNextDdqTime = "";
    private String mCurTime = "";
    private String mNextTime = "";

    @Override
    public int getLayoutId() {
        return R.layout.home_fragment_top;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint({"SetTextI18n", "CheckResult", "DefaultLocale"})
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDataBinding.homeTopBannerViewPager
                .setLifecycleRegistry(getLifecycle())
                .setAdapter(new TopBannerViewAdapter(this.getContext())).create();

        mDataBinding.homeTopBannerViewPager.setCanLoop(true);
        LogUtil.e("TopFragment onViewCreated");
        mViewModel.getTopBannerData().observe(getViewLifecycleOwner(), dataBean -> {
            // 获取数据
            if (dataBean == null) {
                // 处理接口出错的问题
                return;
            }
            mDataBinding.homeTopBannerViewPager.refreshData(dataBean.getBannners());
            mGridAdapter.refreshData(dataBean.getSpecial_category());
            // 处理正常的逻辑。
        });

        mViewModel.getTopGoodsData().observe(getViewLifecycleOwner(), topGoodsBean -> {
            if (topGoodsBean == null) {
                return;
            }

            mTopGoodsAdapter.refreshData(topGoodsBean.getList());
        });

        mViewModel.getRealTimeData().observe(getViewLifecycleOwner(), realTimeBean -> {
            if (realTimeBean == null || realTimeBean.getList() == null || realTimeBean.getList().size() < 2) {
                mDataBinding.homeTopTjLl.setVisibility(View.GONE);
                return;
            }
            mDataBinding.homeTopTjLl.setVisibility(View.VISIBLE);
            Glide.with(this).load(realTimeBean.getList().get(0).getMainPic()).into(mDataBinding.homeTopTj1ItemIv1);
            float sales = realTimeBean.getList().get(0).getTwoHoursSales();
            if (sales > 10000) {
                sales /= 10000;
                mDataBinding.homeTopTj1ItemTv1.setText("2小时抢" + String.format("%.1f", sales) + "万");
            } else {
                mDataBinding.homeTopTj1ItemTv1.setText("2小时抢" + sales);
            }
            mDataBinding.homeTopTj1ItemPriceTv1.setText(realTimeBean.getList().get(0).getActualPrice() + "");
            ///
            Glide.with(this).load(realTimeBean.getList().get(1).getMainPic()).into(mDataBinding.homeTopTj1ItemIv2);
            sales = realTimeBean.getList().get(1).getTwoHoursSales();
            if (sales > 10000) {
                sales /= 10000;
                mDataBinding.homeTopTj1ItemTv2.setText("2小时抢" + String.format("%.1f", sales) + "万");
            } else {
                mDataBinding.homeTopTj1ItemTv2.setText("2小时抢" + sales);
            }
            mDataBinding.homeTopTj1ItemPriceTv2.setText(realTimeBean.getList().get(1).getActualPrice() + "");
        });

        loadSecKilData();

        mDataBinding.homeTopItem2Rl.setOnClickListener(v -> ARouter.getInstance().build(RouterFragmentPath.Spike.PAGER_SPIKE).navigation());

        mGridAdapter = new GridAdapter(this.getContext());
        mDataBinding.homeColumnGv.setAdapter(mGridAdapter);

        mTopGoodsAdapter = new TopGoodsAdapter(this.getContext(), this);
        mDataBinding.homeGoodProductRv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
//                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom = 20;
                outRect.left = 10;
                outRect.right = 10;
            }
        });
        mDataBinding.homeGoodProductRv.setLayoutManager(new GridLayoutManager(this.getContext(), 2));
        mDataBinding.homeGoodProductRv.setAdapter(mTopGoodsAdapter);
    }

    @SuppressLint("DefaultLocale")
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void loadSecKilData() {
        mViewModel.getSecKilData().observe(getViewLifecycleOwner(), secKilBean -> {
            if (secKilBean == null || secKilBean.getGoodsList() == null || secKilBean.getRoundsList().size() <= 0
                    || secKilBean.getGoodsList().size() < 2) {
                mDataBinding.homeTopTjLl.setVisibility(View.GONE);
                return;
            }

            mCurDdqTime = secKilBean.getDdqTime();
            int index = -1;
            for (int i = 0; i < secKilBean.getRoundsList().size(); i++) {
                if (secKilBean.getRoundsList().get(i).getDdqTime().equalsIgnoreCase(mCurDdqTime)) {
                    index = i;
                    break;
                }
            }
            if (index < 0) {
                return;
            }

            mCurTime = secKilBean.getRoundsList().get(index).getRound();
            if (index < secKilBean.getRoundsList().size() - 1) {
                mNextDdqTime = secKilBean.getRoundsList().get(index + 1).getDdqTime();
                mNextTime = secKilBean.getRoundsList().get(index + 1).getRound();
            }


            mDataBinding.homeTopTjLl.setVisibility(View.VISIBLE);
            Glide.with(this).load(secKilBean.getGoodsList().get(0).getMainPic()).into(mDataBinding.homeTopTj2ItemIv1);
            mDataBinding.homeTopTj2ItemTv1.setText(secKilBean.getGoodsList().get(0).getDtitle());
            mDataBinding.homeTopTj2ItemPriceTv1.setText(String.format("%f", secKilBean.getGoodsList().get(0).getActualPrice()));
            ///
            Glide.with(this).load(secKilBean.getGoodsList().get(1).getMainPic()).into(mDataBinding.homeTopTj2ItemIv2);

            mDataBinding.homeTopTj2ItemTv2.setText(secKilBean.getGoodsList().get(1).getDtitle());
            mDataBinding.homeTopTj2ItemPriceTv2.setText(String.format("%f", secKilBean.getGoodsList().get(0).getActualPrice()));


        });
    }

    @SuppressLint("SimpleDateFormat")
    private void doSomeThing() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(Objects.requireNonNull(format.parse(mCurDdqTime)));
        } catch (ParseException ignored) {

        }
        if (calendar.getTimeInMillis() - c.getTimeInMillis() < 30 * 60 * 1000) {
            if (mCurTime.equalsIgnoreCase("")) {
                mDataBinding.homeTopItem2TimeTv.setText("");
            } else {
                String[] time = mCurTime.split(":");
                if (time.length < 2) {
                    mDataBinding.homeTopItem2TimeTv.setText("");
                } else {
                    mDataBinding.homeTopItem2TimeTv.setText(time[0] + "场");
                    mDataBinding.homeTopItem2CountdownTv.setText("正在抢购");
                }
            }
        } else {
            if (mNextDdqTime.equalsIgnoreCase("")) {
                mDataBinding.homeTopItem2CountdownTv.setText("");
            } else {
                try {
                    c.setTime(Objects.requireNonNull(format.parse(mCurDdqTime)));
                } catch (ParseException ignored) {

                }
                long delta = calendar.getTimeInMillis() - c.getTimeInMillis();
                if (delta < 0 && delta > -30 * 60 * 1000) {
                    //发送1秒消息
                }
            }
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        LogUtil.e("TopFragment onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.e("TopFragment onCreate");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtil.e("TopFragment onDestroyView");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogUtil.e("TopFragment onDetach");
    }

    @Override
    public void onResume() {
        super.onResume();
        mDataBinding.homeTopBannerViewPager.startLoop();
        LogUtil.e("TopFragment onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        mDataBinding.homeTopBannerViewPager.stopLoop();
        LogUtil.e("TopFragment onPause");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        LogUtil.e("TopFragment onDestroy");
    }

    @Override
    public void onClick(String id, String goodsId) {
        ARouter.getInstance().build(RouterActivityPath.GoodsDetail.GOODS_DETAIL)
                .withString("params_id", id)
                .withString("params_goods_id", goodsId)
                .navigation();
    }
}
