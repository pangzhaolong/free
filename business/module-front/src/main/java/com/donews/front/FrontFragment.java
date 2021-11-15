package com.donews.front;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.BarUtils;
import com.dn.events.events.WalletRefreshEvent;
import com.donews.common.base.MvvmLazyLiveDataFragment;
import com.donews.common.router.RouterActivityPath;
import com.donews.common.router.RouterFragmentPath;
import com.donews.front.adapter.FragmentAdapter;
import com.donews.front.databinding.FrontFragmentBinding;
import com.donews.front.dialog.ActivityRuleDialog;
import com.donews.front.viewModel.FrontViewModel;
import com.donews.middle.bean.WalletBean;
import com.donews.middle.bean.front.AwardBean;
import com.donews.middle.bean.front.LotteryCategoryBean;
import com.donews.middle.cache.GoodsCache;
import com.donews.middle.views.TabItem;
import com.donews.utilslibrary.utils.AppInfo;
import com.donews.utilslibrary.utils.DensityUtils;
import com.donews.utilslibrary.utils.KeySharePreferences;
import com.donews.utilslibrary.utils.LogUtil;
import com.donews.utilslibrary.utils.SPUtils;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.greenrobot.eventbus.EventBus;

import java.util.Timer;
import java.util.TimerTask;

@Route(path = RouterFragmentPath.Front.PAGER_FRONT)
public class FrontFragment extends MvvmLazyLiveDataFragment<FrontFragmentBinding, FrontViewModel>
        implements View.OnClickListener {

    private FragmentAdapter mFragmentAdapter;
    private LotteryCategoryBean mLotteryCategoryBean;

    private Animation mRotateAnimation;

    private Timer mRotateTimer = null;
    private TimerTask mRotateTask = null;

    ActivityRuleDialog mRuleDialog = null;

    private int mCurSelectPosition = 0;

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
                    tabItem.setTitle(mLotteryCategoryBean.getList().get(position).getName());
                });
        tab.attach();

        mDataBinding.frontCategoryTl.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                TabItem tabItem = (TabItem) tab.getCustomView();
                assert tabItem != null;
                tabItem.selected();
                mCurSelectPosition = tab.getPosition();
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

        loadCategoryData();
        loadLotteryRecord();
//        loadAwardList();
        initSrl();

        mDataBinding.frontLotteryGotoLl.setOnClickListener(v -> ARouter.getInstance().build(RouterActivityPath.Web.PAGER_WEB_ACTIVITY)
                .withString("title", "中奖攻略")
                .withString("url", BuildConfig.WEB_BASE_URL)
                .navigation());
        mDataBinding.frontCashGetTv.setOnClickListener(v ->
                ARouter.getInstance()
                        .build(RouterActivityPath.Mine.PAGER_ACTIVITY_WITHDRAWAL)
                        .navigation());
        mDataBinding.frontRpHelpIv.setOnClickListener(v -> {
            if (mRuleDialog == null) {
                mRuleDialog = new ActivityRuleDialog(this.getContext());
                mRuleDialog.create();
            }
            mRuleDialog.show();
        });

        if (mRotateAnimation == null) {
            mRotateAnimation = new RotateAnimation(0, 8, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                    0.8f);
            mRotateAnimation.setInterpolator(new CycleInterpolator(2));
            mRotateAnimation.setRepeatMode(Animation.REVERSE);
            mRotateAnimation.setRepeatCount(1);
            mRotateAnimation.setDuration(400);
        }
        startTimer();

        scrollFloatBar();
    }

    private void startTimer() {
        if (mRotateTask == null) {
            mRotateTask = new TimerTask() {
                @Override
                public void run() {
                    FrontFragment.this.requireActivity().runOnUiThread(() -> mDataBinding.frontRpOpenFl5.startAnimation(mRotateAnimation));
                }
            };
        }
        if (mRotateTimer == null) {
            mRotateTimer = new Timer();
            mRotateTimer.schedule(mRotateTask, 2000, 5000);
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
    }

    private void initSrl() {
        mDataBinding.frontSrl.setEnableLoadMore(false);
        mDataBinding.frontSrl.setOnRefreshListener(refreshLayout -> {
            loadCategoryData();
//            loadAwardList();
            loadRpData();
            loadLotteryRecord();
            reloadNorData(mCurSelectPosition);
            mDataBinding.frontSrl.finishRefresh();
        });
    }

    private void reloadNorData(int position) {
        if (mFragmentAdapter != null) {
            mFragmentAdapter.reloadNorData(position);
        }
    }

    private void loadCategoryData() {
        mLotteryCategoryBean = GoodsCache.readGoodsBean(LotteryCategoryBean.class, "front");
        if (mLotteryCategoryBean != null && mLotteryCategoryBean.getList() != null) {
            mFragmentAdapter.refreshData(mLotteryCategoryBean.getList());
        }
        mViewModel.getNetData().observe(getViewLifecycleOwner(), categoryBean -> {
            if (categoryBean == null) {
                return;
            }

            mLotteryCategoryBean = categoryBean;
            mFragmentAdapter.refreshData(categoryBean.getList());
            GoodsCache.saveGoodsBean(categoryBean, "front");
        });
    }

    private void loadLotteryRecord() {
        mViewModel.getLotteryPeriod().observe(this.getViewLifecycleOwner(), lotteryOpenRecord -> {
            if (lotteryOpenRecord == null) {
                return;
            }
            showLotteryDate(lotteryOpenRecord.getPeriod());
            LogUtil.e("record:" + lotteryOpenRecord.getPeriod());
            loadLotteryDetail(lotteryOpenRecord.getPeriod());
        });
    }

    private void loadLotteryDetail(int period) {
        mViewModel.getLotteryDetail(period - 1).observe(getViewLifecycleOwner(), lotteryDetailBean -> {
            if (lotteryDetailBean == null) {
                LogUtil.e("record1: detail1:");
                return;
            }
            LogUtil.e("record:" + lotteryDetailBean.getPeriod() + " detail:" + lotteryDetailBean.getCode());
            showLotteryCode(lotteryDetailBean.getCode());
        });
    }

    private void showLotteryDate(int period) {
        mDataBinding.frontLotteryDate.setText(String.format(getString(R.string.front_lottery_date), period - 1));
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
    private void loadRpData() {
        if (!AppInfo.checkIsWXLogin()) {
            mDataBinding.frontRpIv1.setBackgroundResource(R.drawable.front_rp_wait);
            mDataBinding.frontRpIv2.setBackgroundResource(R.drawable.front_rp_wait);
            mDataBinding.frontRpIv3.setBackgroundResource(R.drawable.front_rp_wait);
            mDataBinding.frontRpIv4.setBackgroundResource(R.drawable.front_rp_wait);
            mDataBinding.frontRpIv5.setBackgroundResource(R.drawable.front_rp_gold);

            mDataBinding.frontRpOpenFl1.setOnClickListener(this);
            mDataBinding.frontRpOpenFl2.setOnClickListener(this);
            mDataBinding.frontRpOpenFl3.setOnClickListener(this);
            mDataBinding.frontRpOpenFl4.setOnClickListener(this);
            mDataBinding.frontRpOpenFl5.setOnClickListener(this);

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
            mDataBinding.frontRpTv1.setText("抽奖1次");
            mDataBinding.frontRpTv2.setText("抽奖3次");
            mDataBinding.frontRpTv3.setText("抽奖5次");
            mDataBinding.frontRpTv4.setText("抽奖7次");
            mDataBinding.frontRpTv5.setText("抽奖10次");
            startTimer();
            return;
        }

        WalletBean bean = GoodsCache.readGoodsBean(WalletBean.class, "front");
        if (bean != null && bean.getList() != null && bean.getList().size() == 5) {
            showRpData(bean);
        }

        mViewModel.getRpData().observe(this.getViewLifecycleOwner(), walletBean -> {
            if (walletBean == null || walletBean.getList() == null || walletBean.getList().size() != 5) {
                return;
            }

            showRpData(walletBean);
            GoodsCache.saveGoodsBean(walletBean, "front");
        });
    }

    private void loadAwardList() {
        AwardBean bean = GoodsCache.readGoodsBean(AwardBean.class, "front");
        if (bean != null && bean.getList() != null && bean.getList().size() == 5) {
            mDataBinding.frontBarrageView.refreshData(bean.getList());
        }

        mViewModel.getAwardList().observe(getViewLifecycleOwner(), awardBean -> {
            if (awardBean == null || awardBean.getList() == null) {
                return;
            }
            mDataBinding.frontBarrageView.refreshData(awardBean.getList());
            GoodsCache.saveGoodsBean(awardBean, "front");
        });
    }

    @SuppressLint("SetTextI18n")
    private void changeRpStatus(WalletBean.RpBean rpBean,
                                int topColor, int bottomColor, FrameLayout fl, TextView tv, ImageView iv) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) tv.getLayoutParams();
        fl.setTag(rpBean);
        fl.setOnClickListener(this);
        if (rpBean.getOpened()) {
            iv.setAlpha(1.0f);
            iv.setBackgroundResource(R.drawable.front_rp_oen);
            tv.setText("已开启");
            params.gravity = Gravity.CENTER;
            params.bottomMargin = DensityUtils.dp2px(10);
            tv.setLayoutParams(params);
            tv.setTextColor(topColor);
        } else {
            params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
            params.bottomMargin = DensityUtils.dp2px(6);
            tv.setLayoutParams(params);
            tv.setTextColor(bottomColor);
            if (rpBean.getHadLotteryTotal() == -1 || rpBean.getHadLotteryTotal() >= rpBean.getLotteryTotal()) {
                nCloseRpCounts += 1;
                iv.setAlpha(0.5f);
                tv.setText("待开启");
                iv.setBackgroundResource(R.drawable.front_rp_ready);
            } else if (rpBean.getHadLotteryTotal() < rpBean.getLotteryTotal()) {
                tv.setText("抽奖" + rpBean.getLotteryTotal() + "次");
                iv.setBackgroundResource(R.drawable.front_rp_wait);
            }
        }
    }

    int nCloseRpCounts = 0;

    @SuppressLint("SetTextI18n")
    private void showRpData(WalletBean walletBean) {
        WalletBean.RpBean rpBean = walletBean.getList().get(0);
        if (rpBean == null) {
            return;
        }

        int topColor = Color.parseColor("#764D38");
        int bottomColor = Color.parseColor("#FFF3D3");
        changeRpStatus(rpBean, topColor, bottomColor
                , mDataBinding.frontRpOpenFl1, mDataBinding.frontRpTv1, mDataBinding.frontRpIv1);
        rpBean = walletBean.getList().get(1);
        changeRpStatus(rpBean, topColor, bottomColor
                , mDataBinding.frontRpOpenFl2, mDataBinding.frontRpTv2, mDataBinding.frontRpIv2);
        rpBean = walletBean.getList().get(2);
        changeRpStatus(rpBean, topColor, bottomColor
                , mDataBinding.frontRpOpenFl3, mDataBinding.frontRpTv3, mDataBinding.frontRpIv3);
        rpBean = walletBean.getList().get(3);
        changeRpStatus(rpBean, topColor, bottomColor
                , mDataBinding.frontRpOpenFl4, mDataBinding.frontRpTv4, mDataBinding.frontRpIv4);

        rpBean = walletBean.getList().get(4);
        mDataBinding.frontRpOpenFl5.setTag(rpBean);
        mDataBinding.frontRpOpenFl5.setOnClickListener(this);
        if (rpBean.getOpened()) {
            stopTimer();
            mDataBinding.frontRpIv5.setAlpha(1.0f);
            mDataBinding.frontRpTv5.setText("已开启");
        } else {
            if (rpBean.getHadLotteryTotal() == -1 || rpBean.getHadLotteryTotal() >= rpBean.getLotteryTotal()) {
                nCloseRpCounts += 1;
                mDataBinding.frontRpIv5.setAlpha(0.5f);
                mDataBinding.frontRpTv5.setText("待开启");
            } else if (rpBean.getHadLotteryTotal() < rpBean.getLotteryTotal()) {
                mDataBinding.frontRpTv5.setText("抽奖" + rpBean.getLotteryTotal() + "次");
            }
        }

        mDataBinding.frontFloatingBtn.setProgress(rpBean.getHadLotteryTotal());

        SPUtils.setInformain(KeySharePreferences.CLOSE_RED_PACKAGE_COUNTS, nCloseRpCounts);
        if (rpBean.getHadLotteryTotal() == -1) {
            SPUtils.setInformain(KeySharePreferences.STEPS_TO_GOLD_RED_PACKAGE_COUNTS, 0);
        } else {
            SPUtils.setInformain(KeySharePreferences.STEPS_TO_GOLD_RED_PACKAGE_COUNTS, rpBean.getLotteryTotal() - rpBean.getHadLotteryTotal());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mDataBinding.frontBarrageView != null) {
            mDataBinding.frontBarrageView.resumeScroll();
        }

        loadRpData();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mDataBinding.frontBarrageView != null) {
            mDataBinding.frontBarrageView.pauseScroll();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mDataBinding.frontBarrageView.stopScroll();
        if (mFragmentAdapter != null) {
            mFragmentAdapter.clear();
        }
        if (mRotateAnimation != null) {
            mRotateAnimation.cancel();
            mRotateAnimation = null;
        }

        if (mRuleDialog != null) {
            mRuleDialog.dismiss();
            mRuleDialog = null;
        }

        stopTimer();
    }

    @Override
    public void onClick(View v) {
        if (!AppInfo.checkIsWXLogin()) {
            ARouter.getInstance()
                    .build(RouterActivityPath.User.PAGER_LOGIN)
                    .navigation();
            return;
        }

        WalletBean.RpBean rpBean = (WalletBean.RpBean) v.getTag();
        if (rpBean == null) {
            return;
        }

        if (rpBean.getOpened()) {
            Toast.makeText(this.getContext(), "这个红包已经开过了哦！", Toast.LENGTH_SHORT).show();
            return;
        }

        if (rpBean.getHadLotteryTotal() != -1 && rpBean.getHadLotteryTotal() < rpBean.getLotteryTotal()) {
            Toast.makeText(this.getContext(), "快去抽奖赚取开启红包次数吧！", Toast.LENGTH_SHORT).show();
            return;
        }

        if (rpBean.getHadLotteryTotal() == -1) {
            Toast.makeText(this.getContext(), "前面还有红包未开启哦！", Toast.LENGTH_SHORT).show();
            return;
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
            loadRpData();
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
}
