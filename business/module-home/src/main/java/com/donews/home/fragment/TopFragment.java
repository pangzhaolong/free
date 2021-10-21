package com.donews.home.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.dn.events.events.NavEvent;
import com.donews.base.fragment.MvvmLazyLiveDataFragment;
import com.donews.common.router.RouterActivityPath;
import com.donews.common.router.RouterFragmentPath;
import com.donews.home.R;
import com.donews.home.adapter.GridAdapter;
import com.donews.home.adapter.TopBannerViewAdapter;
import com.donews.home.adapter.TopGoodsAdapter;
import com.donews.home.bean.BannerBean;
import com.donews.home.bean.DataBean;
import com.donews.home.bean.RealTimeBean;
import com.donews.home.bean.SecKilBean;
import com.donews.home.bean.TopGoodsBean;
import com.donews.home.cache.GoodsCache;
import com.donews.home.databinding.HomeFragmentTopBinding;
import com.donews.home.listener.GoodsDetailListener;
import com.donews.home.viewModel.TopViewModel;
import com.donews.utilslibrary.utils.LogUtil;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

public class TopFragment extends MvvmLazyLiveDataFragment<HomeFragmentTopBinding, TopViewModel> implements GoodsDetailListener {

    private GridAdapter mGridAdapter;
    private TopGoodsAdapter mTopGoodsAdapter;

    private String mCurDdqTime = "";
    private String mNextDdqTime = "";
    private String mCurTime = "";
    private String mNextTime = "";

    private TimerHandler mTimerHandler;

    private DataBean mDataBean = null;

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

        mTimerHandler = new TimerHandler(Looper.getMainLooper(), this);

        mTopGoodsAdapter = new TopGoodsAdapter(this.getContext(), this);
        mGridAdapter = new GridAdapter(this.getContext());
        mDataBinding.homeTopBannerViewPager.setCanLoop(true);
        LogUtil.e("TopFragment onViewCreated");

        mDataBean = GoodsCache.readGoodsBean(DataBean.class, "");
        if (mDataBean != null && mDataBean.getBannners() != null && mDataBean.getBannners().size() > 0) {
            LogUtil.e("TopFragment tmpDataBean in :" + mDataBean);
//            mDataBinding.homeTopBannerViewPager.refreshData(mDataBean.getBannners());
            mGridAdapter.refreshData(mDataBean.getSpecial_category());
        }

        mViewModel.getTopBannerData().observe(getViewLifecycleOwner(), dataBean -> {
            // 获取数据
            if (dataBean == null) {
                // 处理接口出错的问题
                return;
            }
            mDataBean = dataBean;
//            mDataBinding.homeTopBannerViewPager.refreshData(dataBean.getBannners());
            mGridAdapter.refreshData(dataBean.getSpecial_category());
            GoodsCache.saveGoodsBean(dataBean, "");
        });

        TopGoodsBean goodsBean = GoodsCache.readGoodsBean(TopGoodsBean.class, "");
        if (goodsBean != null && goodsBean.getList() != null && goodsBean.getList().size() > 0) {
            LogUtil.e("TopFragment goodsBean in :" + goodsBean);
            mTopGoodsAdapter.refreshData(goodsBean.getList());
        }

        mViewModel.getTopGoodsData().observe(getViewLifecycleOwner(), topGoodsBean -> {
            if (topGoodsBean == null) {
                return;
            }

            mTopGoodsAdapter.refreshData(topGoodsBean.getList());
            GoodsCache.saveGoodsBean(topGoodsBean, "");
        });

        RealTimeBean tempRealTimeBean = GoodsCache.readGoodsBean(RealTimeBean.class, "top");
        showRealTimeBean(tempRealTimeBean);

        mViewModel.getRealTimeData().observe(getViewLifecycleOwner(), realTimeBean -> {
            showRealTimeBean(realTimeBean);
            GoodsCache.saveGoodsBean(realTimeBean, "top");
        });

        loadSecKilData();

        mDataBinding.homeTopItem2Rl.setOnClickListener(v -> EventBus.getDefault().post(new NavEvent(2)));
        mDataBinding.homeTopItem1Rl.setOnClickListener(v -> ARouter.getInstance().build(RouterActivityPath.RealTime.REALTIME_DETAIL).navigation());

        mDataBinding.homeColumnGv.setAdapter(mGridAdapter);

        mDataBinding.homeGoodProductRv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.bottom = 16;
                outRect.left = 5;
                outRect.right = 5;
            }
        });
        mDataBinding.homeGoodProductRv.setLayoutManager(new GridLayoutManager(this.getContext(), 2));
        mDataBinding.homeGoodProductRv.setAdapter(mTopGoodsAdapter);

        mDataBinding.homeTopShowSrl.setOnRefreshListener(() -> new Handler().postDelayed(() -> mDataBinding.homeTopShowSrl.setRefreshing(false), 1000));

        /*mDataBinding.homeTopBannerViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position < 0 || position >= mDataBean.getBannners().size()) {
                    return;
                }
                BannerBean bannerBean = mDataBean.getBannners().get(position);
                if (bannerBean == null) {
                    return;
                }

                if (!bannerBean.getLink().equalsIgnoreCase("")) {

                }

            }
        });*/
    }

    private void showRealTimeBean(RealTimeBean realTimeBean) {
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
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void loadSecKilData() {
        SecKilBean tmpSecKilBean = GoodsCache.readGoodsBean(SecKilBean.class, "top");
        showSecKilBean(tmpSecKilBean);

        mViewModel.getSecKilData().observe(getViewLifecycleOwner(), secKilBean -> {
            showSecKilBean(secKilBean);
            GoodsCache.saveGoodsBean(secKilBean, "top");
        });
    }

    private void showSecKilBean(SecKilBean secKilBean) {
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

        doSomeThing();

        mDataBinding.homeTopTjLl.setVisibility(View.VISIBLE);
        Glide.with(this).load(secKilBean.getGoodsList().get(0).getMainPic()).into(mDataBinding.homeTopTj2ItemIv1);
        mDataBinding.homeTopTj2ItemTv1.setText(secKilBean.getGoodsList().get(0).getDtitle());
        mDataBinding.homeTopTj2ItemPriceTv1.setText("" + secKilBean.getGoodsList().get(0).getActualPrice());
        ///
        Glide.with(this).load(secKilBean.getGoodsList().get(1).getMainPic()).into(mDataBinding.homeTopTj2ItemIv2);

        mDataBinding.homeTopTj2ItemTv2.setText(secKilBean.getGoodsList().get(1).getDtitle());
        mDataBinding.homeTopTj2ItemPriceTv2.setText("" + secKilBean.getGoodsList().get(0).getActualPrice());
    }

    @SuppressLint({"SimpleDateFormat", "SetTextI18n"})
    private void doSomeThing() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(Objects.requireNonNull(format.parse(mCurDdqTime)));
        } catch (ParseException ignored) {

        }

        if (!mCurTime.equalsIgnoreCase("")) {
            String[] time = mCurTime.split(":");
            if (time.length == 2) {
                mDataBinding.homeTopItem2TimeTv.setText(time[0] + "点场");
                mDataBinding.homeTopItem2CountdownTv.setText("正在抢购");
            }
        }

        if (calendar.getTimeInMillis() - c.getTimeInMillis() < 30 * 60 * 1000) {
            if (mCurTime.equalsIgnoreCase("")) {
                mDataBinding.homeTopItem2TimeTv.setText("");
            } else {
                String[] time = mCurTime.split(":");
                if (time.length < 2) {
                    mDataBinding.homeTopItem2TimeTv.setText("");
                } else {
                    mDataBinding.homeTopItem2TimeTv.setText(time[0] + "点场");
                    mDataBinding.homeTopItem2CountdownTv.setText("正在抢购");
                }
            }
        } else {
            if (mNextDdqTime.equalsIgnoreCase("")) {
                mDataBinding.homeTopItem2CountdownTv.setText("");
            } else {
                try {
                    c.setTime(Objects.requireNonNull(format.parse(mNextDdqTime)));
                } catch (ParseException ignored) {

                }
                long delta = calendar.getTimeInMillis() - c.getTimeInMillis();
                if (delta < 0 && delta > -30 * 60 * 1000) {
                    //发送1秒消息
                    if (!mNextTime.equalsIgnoreCase("")) {
                        String[] time = mNextTime.split(":");
                        if (time.length == 2) {
                            mDataBinding.homeTopItem2TimeTv.setText(time[0] + "点场");
                            mDataBinding.homeTopItem2CountdownTv.setText("倒计时...");
                            mTimerHandler.sendEmptyMessage(10001);
                        }
                    }
                }
            }
        }
    }

    private static class TimerHandler extends Handler {

        private final WeakReference<TopFragment> mTopFragment;

        public TimerHandler(Looper looper, TopFragment fragment) {
            super(looper);
            mTopFragment = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 10001) {
                checkTime();
            }
        }

        @SuppressLint("SimpleDateFormat")
        private void checkTime() {
            TopFragment fragment = mTopFragment.get();
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar c = Calendar.getInstance();
            c.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            try {
                c.setTime(Objects.requireNonNull(format.parse(fragment.mNextDdqTime)));
            } catch (ParseException ignored) {
            }

            long delta = c.getTimeInMillis() - calendar.getTimeInMillis();
            if (delta < 0) {
                fragment.mDataBinding.homeTopItem2CountdownTv.setText("正在抢购");
                return;
            }

            SimpleDateFormat newFormat = new SimpleDateFormat("mm:ss");
            String strTime = newFormat.format(new Date(delta));

            fragment.mDataBinding.homeTopItem2CountdownTv.setText(strTime);

            fragment.mTimerHandler.sendEmptyMessageDelayed(10001, 1000);
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

        if (mTimerHandler != null) {
            mTimerHandler.removeCallbacksAndMessages(null);
            mTimerHandler = null;
        }
    }

    @Override
    public void onClick(String id, String goodsId) {
        ARouter.getInstance().build(RouterActivityPath.GoodsDetail.GOODS_DETAIL)
                .withString("params_id", id)
                .withString("params_goods_id", goodsId)
                .navigation();
    }
}
