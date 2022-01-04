package com.donews.front;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.BarUtils;
import com.bumptech.glide.Glide;
import com.dn.events.events.DoubleRpEvent;
import com.dn.events.events.FrontScrollEvent;
import com.dn.events.events.LoginLodingStartStatus;
import com.dn.events.events.RedPackageStatus;
import com.dn.events.events.WalletRefreshEvent;
import com.donews.common.base.MvvmLazyLiveDataFragment;
import com.donews.common.router.RouterActivityPath;
import com.donews.common.router.RouterFragmentPath;
import com.donews.front.adapter.BannerViewAdapter;
import com.donews.front.adapter.FragmentAdapter;
import com.donews.front.databinding.FrontFragmentBinding;
import com.donews.front.dialog.LotteryMore4RpDialog;
import com.donews.front.listener.FrontBannerClickListener;
import com.donews.front.viewModel.FrontViewModel;
import com.donews.middle.bean.WalletBean;
import com.donews.middle.bean.front.FrontConfigBean;
import com.donews.middle.bean.front.LotteryCategoryBean;
import com.donews.middle.bean.home.ServerTimeBean;
import com.donews.middle.cache.GoodsCache;
import com.donews.middle.dialog.ActivityRuleDialog;
import com.donews.middle.front.FrontConfigManager;
import com.donews.middle.go.GotoUtil;
import com.donews.middle.views.TabItem;
import com.donews.utilslibrary.analysis.AnalysisUtils;
import com.donews.utilslibrary.dot.Dot;
import com.donews.utilslibrary.utils.AppInfo;
import com.donews.utilslibrary.utils.DensityUtils;
import com.donews.utilslibrary.utils.KeySharePreferences;
import com.donews.utilslibrary.utils.LogUtil;
import com.donews.utilslibrary.utils.SPUtils;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.zhpan.indicator.enums.IndicatorStyle;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@Route(path = RouterFragmentPath.Front.PAGER_FRONT)
public class FrontFragment extends MvvmLazyLiveDataFragment<FrontFragmentBinding, FrontViewModel>
        implements View.OnClickListener, FrontBannerClickListener {

    private FragmentAdapter mFragmentAdapter;
    private LotteryCategoryBean mLotteryCategoryBean;

    private Animation mRotateAnimation;
    private Animation mScaleAnimation;

    private Timer mRotateTimer = null;
    private TimerTask mRotateTask = null;

    private Timer mScaleTimer = null;
    private TimerTask mScaleTask = null;
    private boolean mIsFragmentActive = false;

    private FrontHandler mFrontHandler = null;

    private ActivityRuleDialog mRuleDialog = null;
    //    private FirstGuidLotteryDialog mFirstGuidLotteryDialog = null;
    private LotteryMore4RpDialog mLotteryMore4RpDialog = null;

    private int mCurSelectPosition = 0;
    private boolean mFindFirstReadyRp = false;
    private long mPreClickRpTime = 0;
    private boolean mHasRefreshed = false;

    private WalletBean mWalletBean;

    private Context mContext;

    @Override
    public int getLayoutId() {
        return R.layout.front_fragment;
    }

    @Override
    protected void onFragmentFirstVisible() {
        super.onFragmentFirstVisible();
    }

    @SuppressLint("StringFormatMatches")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mContext = this.getContext();

        mCurSelectPosition = 0;

        mFragmentAdapter = new FragmentAdapter(this);
        mDataBinding.frontVp2.setAdapter(mFragmentAdapter);
        mDataBinding.frontCategoryTl.setTabMode(TabLayout.MODE_SCROLLABLE);

        TabLayoutMediator tab = new TabLayoutMediator(mDataBinding.frontCategoryTl, mDataBinding.frontVp2,
                (tab1, position) -> {
                    if (tab1.getCustomView() == null) {
                        tab1.setCustomView(new TabItem(mContext));
                    }
                    TabItem tabItem = (TabItem) tab1.getCustomView();
                    assert tabItem != null;
                    if (mLotteryCategoryBean == null || mLotteryCategoryBean.getList() == null) {
                        return;
                    }
                    LotteryCategoryBean.categoryBean bean = mLotteryCategoryBean.getList().get(position);
                    tabItem.setTitle(bean.getName());
                    if (!mHasRefreshed) {
                        if (bean.isSelected()) {
                            tabItem.selected();
                            mDataBinding.frontVp2.setCurrentItem(position, true);
                            mCurSelectPosition = position;
                        } else {
                            tabItem.unSelected();
                        }
                    }
                });
        tab.attach();

        mDataBinding.frontCategoryTl.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                TabItem tabItem = (TabItem) tab.getCustomView();
                assert tabItem != null;
                tabItem.selected();
                if (tab.getPosition() != mCurSelectPosition) {
                    mCurSelectPosition = tab.getPosition();
                    sendDotData();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                TabItem tabItem = (TabItem) tab.getCustomView();
                assert tabItem != null;
                tabItem.unSelected();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                TabItem tabItem = (TabItem) tab.getCustomView();
                assert tabItem != null;
                tabItem.selected();
            }
        });

        /*if (mRotateAnimation == null) {
            mRotateAnimation = new RotateAnimation(0, 8, Animation.RELATIVE_TO_SELF, 0f
                    , Animation.RELATIVE_TO_SELF, 1f);
            mRotateAnimation.setInterpolator(new CycleInterpolator(2));
            mRotateAnimation.setRepeatMode(Animation.REVERSE);
            mRotateAnimation.setRepeatCount(1);
            mRotateAnimation.setDuration(400);
        }*/
        /*if (mScaleAnimation == null) {
            mScaleAnimation = new ScaleAnimation(1.15f, 0.9f, 1.15f, 0.9f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            mScaleAnimation.setInterpolator(new LinearInterpolator());
            mScaleAnimation.setRepeatMode(Animation.REVERSE);
            mScaleAnimation.setRepeatCount(Animation.INFINITE);
            mScaleAnimation.setDuration(1000);
        }*/
        if (mScaleAnimation == null) {
            mScaleAnimation = new RotateAnimation(-5, 5, Animation.RELATIVE_TO_SELF, 0.5f
                    , Animation.RELATIVE_TO_SELF, 0.5f);
            mScaleAnimation.setInterpolator(new CycleInterpolator(2));
            mScaleAnimation.setRepeatMode(Animation.REVERSE);
            mScaleAnimation.setRepeatCount(1);
            mScaleAnimation.setDuration(400);
        }

        loadCategoryData();
        loadServerTime();
        if (FrontConfigManager.Ins() != null && FrontConfigManager.Ins().getConfigBean() != null && FrontConfigManager.Ins().getConfigBean().getLotteryWinner()) {
            loadLotteryWinnerList();
        }
        initSrl();

        mDataBinding.frontLotteryRuleBtnLl.setOnClickListener(v -> ARouter.getInstance().build(RouterActivityPath.Web.PAGER_WEB_ACTIVITY)
                .withString("title", "中奖攻略")
                .withString("url", BuildConfig.WEB_BASE_URL)
                .navigation());
        mDataBinding.frontLotteryGotoLl.setOnClickListener(v -> ARouter.getInstance().build(RouterActivityPath.Web.PAGER_WEB_ACTIVITY)
                .withString("title", "中奖攻略")
                .withString("url", BuildConfig.WEB_BASE_URL)
                .navigation());
        mDataBinding.frontLotteryRuleRl.setVisibility(View.GONE);

        startTimer();
        scrollFloatBar();
        EventBus.getDefault().register(this);

        mDataBinding.frontToTopTv.setOnClickListener(v -> {
            mFragmentAdapter.gotoTopPosition(mCurSelectPosition);
            mDataBinding.frontAppBarLayout.setExpanded(true, true);
            mDataBinding.frontToTopTv.setVisibility(View.GONE);
        });

        refreshLayout();
    }

    // 更新首页ui布局
    private void refreshLayout() {
        if (FrontConfigManager.Ins().getConfigBean() == null) {
            return;
        }

        if (!FrontConfigManager.Ins().getConfigBean().getRedPackage()
                && !FrontConfigManager.Ins().getConfigBean().getBanner()
                && !FrontConfigManager.Ins().getConfigBean().getTask()
                && FrontConfigManager.Ins().getConfigBean().getLotteryWinner()) {
            mDataBinding.frontLotteryRuleRl.setVisibility(View.VISIBLE);
            return;
        }

        mDataBinding.frontLotteryRuleRl.setVisibility(View.GONE);
        mDataBinding.frontRpRl.setVisibility(FrontConfigManager.Ins().getConfigBean().getRedPackage() ? View.VISIBLE : View.GONE);
        mDataBinding.frontGiftGroupBvp.setVisibility(FrontConfigManager.Ins().getConfigBean().getBanner() ? View.VISIBLE : View.GONE);
        mDataBinding.frontTaskGroupLl.setVisibility(FrontConfigManager.Ins().getConfigBean().getTask() ? View.VISIBLE : View.GONE);
        mDataBinding.frontLotteryWinnerCl.setVisibility(FrontConfigManager.Ins().getConfigBean().getLotteryWinner() ? View.VISIBLE : View.GONE);

        List<FrontConfigBean.TaskItem> taskItems = FrontConfigManager.Ins().getConfigBean().getTaskItems();
        if (taskItems != null && taskItems.size() == 4) {
            FrontConfigBean.TaskItem ti = FrontConfigManager.Ins().getConfigBean().getTaskItems().get(0);
            initTaskView(this.getContext(), ti, mDataBinding.frontTaskFl1, mDataBinding.frontTaskMixIv1, mDataBinding.frontTaskIv1, mDataBinding.frontTaskTv1, 1);
            ti = FrontConfigManager.Ins().getConfigBean().getTaskItems().get(1);
            initTaskView(this.getContext(), ti, mDataBinding.frontTaskFl2, mDataBinding.frontTaskMixIv2, mDataBinding.frontTaskIv2, mDataBinding.frontTaskTv2, 2);
            ti = FrontConfigManager.Ins().getConfigBean().getTaskItems().get(2);
            initTaskView(this.getContext(), ti, mDataBinding.frontTaskFl3, mDataBinding.frontTaskMixIv3, mDataBinding.frontTaskIv3, mDataBinding.frontTaskTv3, 3);
            ti = FrontConfigManager.Ins().getConfigBean().getTaskItems().get(3);
            initTaskView(this.getContext(), ti, mDataBinding.frontTaskFl4, mDataBinding.frontTaskMixIv4, mDataBinding.frontTaskIv4, mDataBinding.frontTaskTv4, 4);
            mDataBinding.frontTaskGroupLl.postInvalidate();
        } else {
            mDataBinding.frontTaskGroupLl.setVisibility(View.GONE);
        }

        List<FrontConfigBean.BannerItem> bannerItems = FrontConfigManager.Ins().getConfigBean().getBannerItems();
        if (bannerItems != null && bannerItems.size() > 0) {
            mDataBinding.frontGiftGroupBvp.setCanLoop(true)
                    .setIndicatorStyle(IndicatorStyle.ROUND_RECT)
                    .setAdapter(new BannerViewAdapter(getContext(), this))
                    /*.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                        @Override
                        public void onPageSelected(int position) {
                            super.onPageSelected(position);

                        }
                    })*/
                    .create(FrontConfigManager.Ins().getConfigBean().getBannerItems());
        } else {
            mDataBinding.frontGiftGroupBvp.setVisibility(View.GONE);
        }
    }

    private void initTaskView(Context context, FrontConfigBean.TaskItem ti, FrameLayout fl, ImageView mixIv, ImageView iv, TextView tv, int idx) {
        if (ti == null) {
            return;
        }
        fl.setOnClickListener(v -> GotoUtil.doAction(context, ti.getAction(), ti.getTitle(), "front"));
        if (ti.getModel() == 1) {
            mixIv.setVisibility(View.GONE);
            iv.setVisibility(View.VISIBLE);
            tv.setVisibility(View.VISIBLE);
            Glide.with(this).load(ti.getIcon()).into(iv);
            tv.setText(ti.getTitle());
        } else {
            iv.setVisibility(View.GONE);
            tv.setVisibility(View.GONE);
            mixIv.setVisibility(View.VISIBLE);
            Glide.with(this).load(ti.getIcon()).into(mixIv);
        }
    }

    private void sendDotData() {
        if (mLotteryCategoryBean == null || mLotteryCategoryBean.getList() == null || mLotteryCategoryBean.getList().size() <= 0) {
            return;
        }

        if (mCurSelectPosition < 0 || mCurSelectPosition > mLotteryCategoryBean.getList().size() - 1) {
            return;
        }

        LotteryCategoryBean.categoryBean bean = mLotteryCategoryBean.getList().get(mCurSelectPosition);
        if (bean == null) {
            return;
        }

        AnalysisUtils.onEventEx(mContext, Dot.But_Category_Click, bean.getName());
    }

    @Override
    public void onClick(int position) {
        if (position < 0 || position >= FrontConfigManager.Ins().getConfigBean().getBannerItems().size()) {
            return;
        }

        FrontConfigBean.BannerItem bi = FrontConfigManager.Ins().getConfigBean().getBannerItems().get(position);
        if (bi == null) {
            return;
        }

        GotoUtil.doAction(getContext(), bi.getAction(), bi.getTitle(), "front_banner");
    }

    private static class FrontHandler extends Handler {
        private final WeakReference<FrontFragment> fragmentWeakReference;

        public FrontHandler(FrontFragment fragment, Looper looper) {
            super(looper);
            fragmentWeakReference = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            FrontFragment fragment = fragmentWeakReference.get();
            if (fragment == null) {
                return;
            }
            switch (msg.what) {
                case 10001:
//                    fragment.startRotateAnim();
                    break;
                case 10002:
                    fragment.startScaleAnim();
                    break;

            }
        }
    }

    public void startRotateAnim() {
        try {
            if (mDataBinding.frontRpGold88 == null || !mDataBinding.frontRpGold88.isShown() || requireActivity().isFinishing()) {
                return;
            }
            mDataBinding.frontRpGold88.startAnimation(mRotateAnimation);
        } catch (Exception ignored) {
        }
    }

    private void startRpAnimator(WalletBean.RpBean rpBean, FrameLayout fl, int index) {
        if (rpBean == null) {
            return;
        }

        if (fl.getAnimation() != null) {
            fl.clearAnimation();
        }

        if (!rpBean.getOpened()) {
            if (rpBean.getHadLotteryTotal() == -1 || rpBean.getHadLotteryTotal() >= rpBean.getLotteryTotal()) {
                if (fl.getAnimation() == null && !mFindFirstReadyRpAnim) {
                    fl.startAnimation(mScaleAnimation);
                    mFindFirstReadyRpAnim = true;
                }
            } else if (rpBean.getHadLotteryTotal() < rpBean.getLotteryTotal()) {
                if (index == 1) {
                    if (fl.getAnimation() == null) {
                        fl.startAnimation(mScaleAnimation);
                    }
                }
            }
        }
    }

    private boolean mFindFirstReadyRpAnim = false;

    public void startScaleAnim() {
        if (!AppInfo.checkIsWXLogin()) {
            if (mDataBinding.frontRpOpenFl1.getAnimation() == null) {
                mDataBinding.frontRpOpenFl1.startAnimation(mScaleAnimation);
            }
            if (mDataBinding.frontRpOpenFl2.getAnimation() != null) {
                mDataBinding.frontRpOpenFl2.clearAnimation();
            }
            if (mDataBinding.frontRpOpenFl3.getAnimation() != null) {
                mDataBinding.frontRpOpenFl3.clearAnimation();
            }
            if (mDataBinding.frontRpOpenFl4.getAnimation() != null) {
                mDataBinding.frontRpOpenFl4.clearAnimation();
            }
            if (mDataBinding.frontRpOpenFl5.getAnimation() != null) {
                mDataBinding.frontRpOpenFl5.clearAnimation();
            }
            return;
        }
        mFindFirstReadyRpAnim = false;
        if (mWalletBean == null || mWalletBean.getList() == null || mWalletBean.getList().size() != 5) {
            return;
        }
        WalletBean.RpBean rpBean = mWalletBean.getList().get(0);
        startRpAnimator(rpBean, mDataBinding.frontRpOpenFl1, 1);
        rpBean = mWalletBean.getList().get(1);
        startRpAnimator(rpBean, mDataBinding.frontRpOpenFl2, 2);
        rpBean = mWalletBean.getList().get(2);
        startRpAnimator(rpBean, mDataBinding.frontRpOpenFl3, 3);
        rpBean = mWalletBean.getList().get(3);
        startRpAnimator(rpBean, mDataBinding.frontRpOpenFl4, 4);
        rpBean = mWalletBean.getList().get(4);
        startRpAnimator(rpBean, mDataBinding.frontRpOpenFl5, 5);
    }

    private void startTimer() {
        if (mFrontHandler == null) {
            mFrontHandler = new FrontHandler(this, this.requireContext().getMainLooper());
        }
        if (mRotateTask == null) {
            mRotateTask = new TimerTask() {
                @Override
                public void run() {
                    if (mFrontHandler != null && mIsFragmentActive) {
                        mFrontHandler.sendEmptyMessage(10001);
                    }
                }
            };
        }
        if (mRotateTimer == null) {
            mRotateTimer = new Timer();
            mRotateTimer.schedule(mRotateTask, 2000, 5000);
        }

        if (mScaleTask == null) {
            mScaleTask = new TimerTask() {
                @Override
                public void run() {
                    if (mFrontHandler != null && mIsFragmentActive) {
                        mFrontHandler.sendEmptyMessage(10002);
                    }
                }
            };
        }
        if (mScaleTimer == null) {
            mScaleTimer = new Timer();
            mScaleTimer.schedule(mScaleTask, 2000, 4000);
        }
    }

    private void stopTimer() {
        if (mRotateTimer != null) {
            mRotateTimer.cancel();
            mRotateTimer = null;
        }
        if (mRotateTask != null) {
            mRotateTask.cancel();
            mRotateTask = null;
        }
        if (mScaleTimer != null) {
            mScaleTimer.cancel();
            mScaleTimer = null;
        }
        if (mScaleTask != null) {
            mScaleTask.cancel();
            mScaleTask = null;
        }
        if (mFrontHandler != null) {
            mFrontHandler.removeCallbacksAndMessages(null);
            mFrontHandler = null;
        }
    }

    private void initSrl() {
        mDataBinding.frontSrl.setEnableLoadMore(false);
        mDataBinding.frontSrl.setOnRefreshListener(refreshLayout -> {
            refreshFront();
        });
    }

    private void refreshFront() {
        mHasRefreshed = true;
        refreshLayout();
        loadCategoryData();
        if (FrontConfigManager.Ins().getConfigBean().getRedPackage()) {
            loadRpData(false);
        }
        loadServerTime();
        reloadNorData(mCurSelectPosition);
        if (FrontConfigManager.Ins().getConfigBean().getLotteryWinner()) {
            loadLotteryWinnerList();
        }
        mDataBinding.frontSrl.finishRefresh();
    }

    private void reloadNorData(int position) {
        if (mFragmentAdapter != null) {
            mFragmentAdapter.reloadNorData(position);
        }
    }

    private void loadLotteryWinnerList() {
        mViewModel.getWinnerList().observe(this.getViewLifecycleOwner(), awardBean -> {
            if (awardBean == null || awardBean.getList() == null || awardBean.getList().size() <= 0) {
                return;
            }

            mDataBinding.frontLotteryCodesBarrageLbv.refreshData(awardBean.getList());
        });
    }

    private void loadCategoryData() {
        /*mLotteryCategoryBean = GoodsCache.readGoodsBean(LotteryCategoryBean.class, "front");
        if (mLotteryCategoryBean != null && mLotteryCategoryBean.getList() != null) {
            mFragmentAdapter.refreshData(mLotteryCategoryBean.getList());
        }
*/
        mViewModel.getNetData().observe(getViewLifecycleOwner(), categoryBean -> {
            if (categoryBean == null) {
                return;
            }

            mLotteryCategoryBean = categoryBean;

            mFragmentAdapter.refreshData(categoryBean.getList());
//            GoodsCache.saveGoodsBean(categoryBean, "front");
        });
    }

    private void loadServerTime() {
        mViewModel.getServerTime().observe(this.getViewLifecycleOwner(), serverTimeBean -> {
            if (serverTimeBean == null) {
                return;
            }
            checkLotteryRecord(serverTimeBean);
        });
    }

    private boolean mIsAfterTenClock = false;

    private void checkLotteryRecord(ServerTimeBean serverTimeBean) {
        String time = serverTimeBean.getNow();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(time) * 1000);
        LogUtil.e("Time:" + calendar.get(Calendar.YEAR) + " " + calendar.get(Calendar.MONTH) + " " + calendar.get(Calendar.DAY_OF_MONTH)
                + " " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND));
        mIsAfterTenClock = calendar.get(Calendar.HOUR_OF_DAY) >= 10;
        loadLotteryRecord();
    }

    private void loadLotteryRecord() {
        mViewModel.getLotteryPeriod().observe(this.getViewLifecycleOwner(), lotteryOpenRecord -> {
            if (lotteryOpenRecord == null) {
                return;
            }

            if (!mIsAfterTenClock) {
                showLotteryDateClosed(lotteryOpenRecord.getPeriod());
                return;
            }
            showLotteryDateOpend(lotteryOpenRecord.getPeriod());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            try {
                Date date = sdf.parse(lotteryOpenRecord.getPeriod() + "");
                Calendar c = Calendar.getInstance();
                assert date != null;
                c.setTime(date);
                c.add(Calendar.DAY_OF_MONTH, -1);
                String yesterday = sdf.format(c.getTime());
                loadLotteryDetail(Integer.parseInt(yesterday));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });
    }

    private void loadLotteryDetail(int period) {
        mViewModel.getLotteryDetail(period).observe(getViewLifecycleOwner(), lotteryDetailBean -> {
            if (lotteryDetailBean == null) {
                LogUtil.e("record1: detail1:");
                return;
            }
            LogUtil.e("record:" + lotteryDetailBean.getPeriod() + " detail:" + lotteryDetailBean.getCode());
            showLotteryCode(lotteryDetailBean.getCode());
        });
    }

    private void showLotteryDateClosed(int period) {
//        mDataBinding.frontLotteryDate.setText(String.format(getString(R.string.front_lottery_date), period - 1));
        mDataBinding.frontLotteryDate.setText(getString(R.string.front_lottery_date));
    }

    private void showLotteryDateOpend(int period) {
//        mDataBinding.frontLotteryDate.setText(String.format(getString(R.string.front_lottery_date_opened), period - 1));
        mDataBinding.frontLotteryDate.setText(R.string.front_lottery_date_opened);
    }

    private void showLotteryCode(String code) {
        if (code.length() < 7) {
            return;
        }
        mDataBinding.frontLotteryNumTv1.setText(code.substring(0, 1));
        mDataBinding.frontLotteryNumTv2.setText(code.substring(1, 2));
        mDataBinding.frontLotteryNumTv3.setText(code.substring(2, 3));
        mDataBinding.frontLotteryNumTv4.setText(code.substring(3, 4));
        mDataBinding.frontLotteryNumTv5.setText(code.substring(4, 5));
        mDataBinding.frontLotteryNumTv6.setText(code.substring(5, 6));
        mDataBinding.frontLotteryNumTv7.setText(code.substring(6, 7));
    }

    @SuppressLint("SetTextI18n")
    private void loadRpData(boolean autoGetRp) {
        mDataBinding.tomorrow01.setVisibility(View.GONE);
        mDataBinding.tomorrow02.setVisibility(View.GONE);
        mDataBinding.tomorrow03.setVisibility(View.GONE);
        mDataBinding.tomorrow04.setVisibility(View.GONE);
        mDataBinding.tomorrow05.setVisibility(View.GONE);

        if (!AppInfo.checkIsWXLogin()) {
            mDataBinding.frontRpIv1.setBackgroundResource(R.drawable.front_rp_ready);
            mDataBinding.frontRpIv2.setBackgroundResource(R.drawable.front_rp_ready);
            mDataBinding.frontRpIv3.setBackgroundResource(R.drawable.front_rp_ready);
            mDataBinding.frontRpIv4.setBackgroundResource(R.drawable.front_rp_ready);
            mDataBinding.frontRpIv5.setBackgroundResource(R.drawable.front_rp_gold);

            mDataBinding.frontRpOpenFl1.setOnClickListener(this);
            mDataBinding.frontRpOpenFl2.setOnClickListener(this);
            mDataBinding.frontRpOpenFl3.setOnClickListener(this);
            mDataBinding.frontRpOpenFl4.setOnClickListener(this);
            mDataBinding.frontRpOpenFl5.setOnClickListener(this);

            mDataBinding.frontRpOpenFl1.setAlpha(1f);
            mDataBinding.frontRpOpenFl2.setAlpha(1f);
            mDataBinding.frontRpOpenFl3.setAlpha(1f);
            mDataBinding.frontRpOpenFl4.setAlpha(1f);
            mDataBinding.frontRpOpenFl5.setAlpha(1f);

            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mDataBinding.frontRpTv1.getLayoutParams();
            params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
            params.bottomMargin = DensityUtils.dp2px(6);
            mDataBinding.frontRpTv1.setLayoutParams(params);
            mDataBinding.frontRpTv2.setLayoutParams(params);
            mDataBinding.frontRpTv3.setLayoutParams(params);
            mDataBinding.frontRpTv4.setLayoutParams(params);
            mDataBinding.frontRpTv5.setLayoutParams(params);
            int color = Color.parseColor("#FFF3D3");
            mDataBinding.frontRpTv1.setTextColor(color);
            mDataBinding.frontRpTv2.setTextColor(color);
            mDataBinding.frontRpTv3.setTextColor(color);
            mDataBinding.frontRpTv4.setTextColor(color);
            mDataBinding.frontRpTv1.setText("待开启");
            mDataBinding.frontRpTv2.setText("抽奖3次");
            mDataBinding.frontRpTv3.setText("抽奖5次");
            mDataBinding.frontRpTv4.setText("抽奖7次");
            mDataBinding.frontRpTv5.setText("抽奖10次");

            if (mDataBinding.frontRpOpenFl1.getAnimation() == null) {
                mDataBinding.frontRpOpenFl1.startAnimation(mScaleAnimation);
            }
            if (mDataBinding.frontRpOpenFl2.getAnimation() != null) {
                mDataBinding.frontRpOpenFl2.clearAnimation();
            }
            if (mDataBinding.frontRpOpenFl3.getAnimation() != null) {
                mDataBinding.frontRpOpenFl3.clearAnimation();
            }
            if (mDataBinding.frontRpOpenFl4.getAnimation() != null) {
                mDataBinding.frontRpOpenFl4.clearAnimation();
            }
            if (mDataBinding.frontRpOpenFl5.getAnimation() != null) {
                mDataBinding.frontRpOpenFl5.clearAnimation();
            }

            mDataBinding.frontRpProgress.setProgress(0);

            startTimer();

            EventBus.getDefault().post(new RedPackageStatus(0, 0));
            return;
        }

        if (!autoGetRp) {
            WalletBean bean = GoodsCache.readGoodsBean(WalletBean.class, "front");
            if (bean != null && bean.getList() != null && bean.getList().size() == 5) {
                showRpData(bean, false);
            }
        }

        mViewModel.getRpData().observe(this.getViewLifecycleOwner(), walletBean -> {
            if (walletBean == null || walletBean.getList() == null || walletBean.getList().size() != 5) {
                return;
            }

            showRpData(walletBean, autoGetRp);
            GoodsCache.saveGoodsBean(walletBean, "front");
        });
    }

    @SuppressLint("SetTextI18n")
    private void changeRpStatus(WalletBean.RpBean rpBean,
                                int topColor, int bottomColor,
                                FrameLayout fl, TextView tv,
                                TextView tvTomorrow, ImageView iv,
                                ImageView progressDoneIv, int index) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) tv.getLayoutParams();
        fl.setTag(rpBean);
        fl.setTag(R.id.tag_first, index);
        fl.setOnClickListener(this);
        if (fl.getAnimation() != null) {
            fl.clearAnimation();
        }

        if (rpBean.getOpened()) {
            iv.setAlpha(0.7f);
            iv.setBackgroundResource(R.drawable.front_rp_oen);
            tv.setText("已开");
            params.gravity = Gravity.CENTER;
            params.bottomMargin = DensityUtils.dp2px(16);
            tv.setLayoutParams(params);
            tv.setTextColor(topColor);
            progressDoneIv.setPadding(0, 0, 0, 0);
            progressDoneIv.setImageResource(R.drawable.front_rp_progress_done);
//            tvTomorrow.setVisibility(View.VISIBLE);
        } else {
            params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
            params.bottomMargin = DensityUtils.dp2px(6);
            tv.setLayoutParams(params);
            tv.setTextColor(bottomColor);
            if (rpBean.getHadLotteryTotal() == -1 || rpBean.getHadLotteryTotal() >= rpBean.getLotteryTotal()) {
                nCloseRpCounts += 1;
                tv.setText("待开启");
                iv.setBackgroundResource(R.drawable.front_rp_ready);
                progressDoneIv.setPadding(0, 0, 0, 0);
                progressDoneIv.setImageResource(R.drawable.front_rp_progress_done);
                if (fl.getAnimation() == null && !mFindFirstReadyRp) {
                    fl.startAnimation(mScaleAnimation);
                    mFindFirstReadyRp = true;
                }
            } else if (rpBean.getHadLotteryTotal() < rpBean.getLotteryTotal()) {
                if (index == 1) {
                    tv.setText("待开启");
                    progressDoneIv.setPadding(0, 0, 0, 0);
                    progressDoneIv.setImageResource(R.drawable.front_rp_progress_done);
                } else {
                    tv.setText("抽奖" + rpBean.getLotteryTotal() + "次");
                }
                iv.setBackgroundResource(R.drawable.front_rp_ready);
                if (index == 1) {
                    if (fl.getAnimation() == null) {
                        fl.startAnimation(mScaleAnimation);
                    }
                }
            }
        }
        fl.postInvalidate();
    }

    private void showTomorrowText() {
        mDataBinding.tomorrow01.setVisibility(View.VISIBLE);
        mDataBinding.tomorrow02.setVisibility(View.VISIBLE);
        mDataBinding.tomorrow03.setVisibility(View.VISIBLE);
        mDataBinding.tomorrow04.setVisibility(View.VISIBLE);
        mDataBinding.tomorrow05.setVisibility(View.VISIBLE);
        mDataBinding.frontRp88Ll.setVisibility(View.GONE);
        mDataBinding.frontRpGold88.setVisibility(View.GONE);
        mDataBinding.frontRpOpenFl1.setAlpha(0.7f);
        mDataBinding.frontRpOpenFl2.setAlpha(0.7f);
        mDataBinding.frontRpOpenFl3.setAlpha(0.7f);
        mDataBinding.frontRpOpenFl4.setAlpha(0.7f);
        mDataBinding.frontRpOpenFl5.setAlpha(0.7f);
    }

    int nCloseRpCounts = 0;

    @SuppressLint("SetTextI18n")
    private void showRpData(WalletBean walletBean, boolean autoGetRp) {
        WalletBean.RpBean rpBean = walletBean.getList().get(0);
        if (rpBean == null) {
            return;
        }

        mWalletBean = walletBean;

        SPUtils.setInformain(KeySharePreferences.FIRST_RP_IS_OPEN, rpBean.getOpened());

        nCloseRpCounts = 0;
        mFindFirstReadyRp = false;
        int topColor = Color.parseColor("#764D38");
        int bottomColor = Color.parseColor("#FFF3D3");
        changeRpStatus(rpBean, topColor, bottomColor
                , mDataBinding.frontRpOpenFl1, mDataBinding.frontRpTv1, mDataBinding.tomorrow01, mDataBinding.frontRpIv1, mDataBinding.frontRpProgressDoneIv1, 1);
        rpBean = walletBean.getList().get(1);
        changeRpStatus(rpBean, topColor, bottomColor
                , mDataBinding.frontRpOpenFl2, mDataBinding.frontRpTv2, mDataBinding.tomorrow02, mDataBinding.frontRpIv2, mDataBinding.frontRpProgressDoneIv2, 2);
        rpBean = walletBean.getList().get(2);
        changeRpStatus(rpBean, topColor, bottomColor
                , mDataBinding.frontRpOpenFl3, mDataBinding.frontRpTv3, mDataBinding.tomorrow03, mDataBinding.frontRpIv3, mDataBinding.frontRpProgressDoneIv3, 3);
        rpBean = walletBean.getList().get(3);
        changeRpStatus(rpBean, topColor, bottomColor
                , mDataBinding.frontRpOpenFl4, mDataBinding.frontRpTv4, mDataBinding.tomorrow04, mDataBinding.frontRpIv4, mDataBinding.frontRpProgressDoneIv4, 4);

        rpBean = walletBean.getList().get(4);
        mDataBinding.frontRpOpenFl5.setTag(rpBean);
        mDataBinding.frontRpOpenFl5.setTag(R.id.tag_first, 5);
        mDataBinding.frontRpOpenFl5.setOnClickListener(this);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mDataBinding.frontRpTv5.getLayoutParams();
        if (rpBean.getOpened()) {
            stopTimer();
            mDataBinding.frontRpIv5.setBackgroundResource(R.drawable.front_rp_gold_open);
            params.gravity = Gravity.CENTER;
            params.bottomMargin = DensityUtils.dp2px(16);
            mDataBinding.frontRpTv5.setLayoutParams(params);
            mDataBinding.frontRpTv5.setTextColor(topColor);
            mDataBinding.frontRpTv5.setText("已开");
            mDataBinding.frontRpProgressDoneIv5.setPadding(0, 0, 0, 0);
            mDataBinding.frontRpProgressDoneIv5.setImageResource(R.drawable.front_rp_progress_done);
            showTomorrowText();
            EventBus.getDefault().post(new RedPackageStatus(0, -2));
        } else {
            params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
            params.bottomMargin = DensityUtils.dp2px(6);
            mDataBinding.frontRpTv5.setLayoutParams(params);
            if (rpBean.getHadLotteryTotal() == -1 || rpBean.getHadLotteryTotal() >= rpBean.getLotteryTotal()) {
                nCloseRpCounts += 1;
                mDataBinding.frontRpTv5.setText("待开启");
                mDataBinding.frontRpProgressDoneIv5.setPadding(0, 0, 0, 0);
                mDataBinding.frontRpProgressDoneIv5.setImageResource(R.drawable.front_rp_progress_done);
            } else if (rpBean.getHadLotteryTotal() < rpBean.getLotteryTotal()) {
                mDataBinding.frontRpTv5.setText("抽奖" + rpBean.getLotteryTotal() + "次");
            }
            EventBus.getDefault().post(new RedPackageStatus(0, rpBean.getHadLotteryTotal()));
        }

        SPUtils.setInformain(KeySharePreferences.CLOSE_RED_PACKAGE_COUNTS, nCloseRpCounts);
        if (rpBean.getOpened()) {
            SPUtils.setInformain(KeySharePreferences.STEPS_TO_GOLD_RED_PACKAGE_COUNTS, 0);
            SPUtils.setInformain(KeySharePreferences.LOTTERY_COUNTS, rpBean.getLotteryTotal());
        } else {
            if (rpBean.getHadLotteryTotal() == -1) {
                SPUtils.setInformain(KeySharePreferences.STEPS_TO_GOLD_RED_PACKAGE_COUNTS, 0);
                SPUtils.setInformain(KeySharePreferences.LOTTERY_COUNTS, rpBean.getLotteryTotal());
                mDataBinding.frontRpProgress.setProgress(10);
            } else {
                SPUtils.setInformain(KeySharePreferences.STEPS_TO_GOLD_RED_PACKAGE_COUNTS, rpBean.getLotteryTotal() - rpBean.getHadLotteryTotal());
                SPUtils.setInformain(KeySharePreferences.LOTTERY_COUNTS, rpBean.getHadLotteryTotal());
                mDataBinding.frontRpProgress.setProgress(rpBean.getHadLotteryTotal());
            }
        }

        if (autoGetRp) {
            openRedPackage();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mDataBinding.frontLotteryCodesBarrageLbv != null) {
            mDataBinding.frontLotteryCodesBarrageLbv.resumeScroll();
        }

        if (mDataBinding.frontGiftGroupBvp != null) {
            mDataBinding.frontGiftGroupBvp.startLoop();
        }

        loadRpData(false);

        mIsFragmentActive = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mDataBinding.frontLotteryCodesBarrageLbv != null) {
            mDataBinding.frontLotteryCodesBarrageLbv.pauseScroll();
        }
        if (mDataBinding.frontGiftGroupBvp != null) {
            mDataBinding.frontGiftGroupBvp.stopLoop();
        }

        mIsFragmentActive = false;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

        stopTimer();

        EventBus.getDefault().unregister(this);

        if (mDataBinding.frontLotteryCodesBarrageLbv != null) {
            mDataBinding.frontLotteryCodesBarrageLbv.stopScroll();
        }
        if (mFragmentAdapter != null) {
            mFragmentAdapter.clear();
        }
        if (mRotateAnimation != null) {
            mRotateAnimation.cancel();
            mRotateAnimation = null;
        }
        if (mScaleAnimation != null) {
            mScaleAnimation.cancel();
            mScaleAnimation = null;
        }

        if (mRuleDialog != null) {
            if (mRuleDialog.isShowing()) {
                mRuleDialog.dismiss();
            }
            mRuleDialog = null;
        }
        if (mLotteryMore4RpDialog != null) {
            if (mLotteryMore4RpDialog.isShowing()) {
                mLotteryMore4RpDialog.dismiss();
            }
            mLotteryMore4RpDialog = null;
        }
    }

    boolean isDoubleClick() {
        long curClickTime = System.currentTimeMillis();
        if (curClickTime - mPreClickRpTime < 500) {
            mPreClickRpTime = curClickTime;
            return true;
        }

        mPreClickRpTime = curClickTime;
        return false;
    }

    private void openRedPackage() {
        if (isDoubleClick()) {
            return;
        }

        if (!AppInfo.checkIsWXLogin()) {
            ARouter.getInstance().build(RouterActivityPath.Rp.PAGE_RP)
                    .withString("from", "privilege")
                    .navigation();

            return;
        }

        if (mWalletBean == null || mWalletBean.getList() == null || mWalletBean.getList().size() <= 0) {
            return;
        }

        WalletBean.RpBean bean = mWalletBean.getList().get(0);
        if (!bean.getOpened()) {
            if (bean.getHadLotteryTotal() < bean.getLotteryTotal()) {
                ARouter.getInstance().build(RouterActivityPath.Rp.PAGE_RP)
                        .withString("from", "privilege")
                        .navigation();
                AnalysisUtils.onEventEx(mContext, Dot.But_Rp_Click, String.valueOf(1));
            } else {
                openRp();
            }
            return;
        }

        boolean allOpened = false;
        for (int i = 1; i < mWalletBean.getList().size(); i++) {
            WalletBean.RpBean bean1 = mWalletBean.getList().get(i);
            if (bean1 == null) {
                continue;
            }
            if (bean1.getOpened()) {
                if (i == mWalletBean.getList().size() - 1) {
                    allOpened = true;
                }
                continue;
            }
            if (bean1.getLotteryTotal() <= bean1.getHadLotteryTotal()) {
                AnalysisUtils.onEventEx(mContext, Dot.But_Rp_Click, String.valueOf(i + 1));
                openRp();
                break;
            }
            int nCounts = bean1.getLotteryTotal() - bean1.getHadLotteryTotal();
            if (nCounts > 0) {
                if (mLotteryMore4RpDialog == null) {
                    mLotteryMore4RpDialog = new LotteryMore4RpDialog(this.getContext(), this.requireActivity());
                }
                mLotteryMore4RpDialog.refreshCounts(nCounts);
                mLotteryMore4RpDialog.showEx();
                break;
            }
        }

        if (allOpened) {
            EventBus.getDefault().post(new DoubleRpEvent(3, 0f, "", ""));
        }
    }

    @Override
    public void onClick(View v) {
        openRedPackage();
    }

    private void openRp() {
        mViewModel.openRpData("", "").observe(this.getViewLifecycleOwner(), doubleRedPacketBean -> {
            if (doubleRedPacketBean == null) {
//                Toast.makeText(this.getContext(), "开启红包失败，请稍后再试或者反馈给我们，谢谢！", Toast.LENGTH_SHORT).show();
                return;
            }
            ARouter.getInstance().build(RouterActivityPath.Rp.PAGE_RP)
                    .withFloat("score", doubleRedPacketBean.getScore())
                    .withString("restId", doubleRedPacketBean.getRestId())
                    .navigation();
            loadRpData(false);
            EventBus.getDefault().post(new WalletRefreshEvent(0));
        });
    }

    int topBaseLinePx = 0;
    int topFloatBaseHei = 0;
    int appBarHei = 0;

    //滚动悬浮标题处理
    private void scrollFloatBar() {
        //处理滑动精选标题遮挡问题
        int statusHei = BarUtils.getStatusBarHeight();
        mDataBinding.frontAppBarLayout.addOnOffsetChangedListener((appBarLayout1, verticalOffset) -> {
            if (appBarHei == 0) {
                appBarHei = mDataBinding.frontAppBarLayout.getHeight();
                topFloatBaseHei = mDataBinding.frontCategoryTl.getPaddingTop();
                topBaseLinePx = (appBarHei - (mDataBinding.frontCategoryTl.getHeight() - topFloatBaseHei) - statusHei);
            }
            if (appBarHei == 0 || /*topFloatBaseHei == 0 ||*/ topBaseLinePx == 0) {
                return;
            }
            if (verticalOffset < -(topBaseLinePx)) {
                //需要改变了
                mDataBinding.frontCategoryTl.setPadding(
                        0,
                        topFloatBaseHei + Math.abs(topBaseLinePx + verticalOffset),
                        0,
                        mDataBinding.frontCategoryTl.getPaddingBottom());
            } else {
                //还没有到位置
                if (mDataBinding.frontCategoryTl.getPaddingTop() > topFloatBaseHei) {
                    mDataBinding.frontCategoryTl.setPadding(
                            0,
                            topFloatBaseHei - Math.abs(verticalOffset + topBaseLinePx),
                            0,
                            mDataBinding.frontCategoryTl.getPaddingBottom());
                } else if (mDataBinding.frontCategoryTl.getPaddingTop() < topFloatBaseHei) {
                    mDataBinding.frontCategoryTl.setPadding(
                            0,
                            topFloatBaseHei,
                            0,
                            mDataBinding.frontCategoryTl.getPaddingBottom());
                }
            }
        });
    }

    @Subscribe //用户登录状态变化
    public void loginStatusEvent(LoginLodingStartStatus event) {
        event.getLoginLoadingLiveData().observe(this, result -> {
            refreshFront();
            /*if (result != 1 && result != 2) {
                return;
            }
            if (event.getTag().equalsIgnoreCase("Front_Rp")) {
                loadRpData(true);
            } else {
                refreshFront();
            }*/
        });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetMessage(FrontScrollEvent event) {
        if (event.getEvent() != 0) {
            return;
        }

        mDataBinding.frontToTopTv.setVisibility(event.getStatus() == 1 ? View.VISIBLE : View.GONE);
    }
}
