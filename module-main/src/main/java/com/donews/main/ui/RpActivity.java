package com.donews.main.ui;

import static com.donews.utilslibrary.utils.KeySharePreferences.NOTIFY_RANDOM_RED_AMOUNT;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Keep;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.dn.events.events.DoubleRpEvent;
import com.dn.events.events.LoginLodingStartStatus;
import com.dn.sdk.listener.rewardvideo.IAdRewardVideoListener;
import com.dn.sdk.listener.rewardvideo.SimpleRewardVideoListener;
import com.donews.base.utils.ToastUtil;
import com.donews.base.viewmodel.BaseLiveDataViewModel;
import com.donews.middle.adutils.RewardVideoAd;
import com.donews.common.base.MvvmBaseLiveDataActivity;
import com.donews.common.router.RouterActivityPath;
import com.donews.common.router.RouterFragmentPath;
import com.donews.main.BuildConfig;
import com.donews.main.R;
import com.donews.main.databinding.MainRpActivityBinding;
import com.donews.main.entitys.resps.ExitDialogRecommendGoods;
import com.donews.main.entitys.resps.ExitDialogRecommendGoodsResp;
import com.donews.middle.centralDeploy.ABSwitch;
import com.donews.middle.bean.WalletBean;
import com.donews.middle.bean.rp.PreRpBean;
import com.donews.middle.utils.LottieUtil;
import com.donews.network.EasyHttp;
import com.donews.network.cache.model.CacheMode;
import com.donews.network.callback.SimpleCallBack;
import com.donews.network.exception.ApiException;
import com.donews.utilslibrary.utils.AppInfo;
import com.donews.utilslibrary.utils.DeviceUtils;
import com.donews.utilslibrary.utils.HttpConfigUtilsKt;
import com.donews.utilslibrary.utils.KeySharePreferences;
import com.donews.utilslibrary.utils.SPUtils;
import com.donews.utilslibrary.utils.SoundHelp;
import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@Route(path = RouterActivityPath.Rp.PAGE_RP)
public class RpActivity extends MvvmBaseLiveDataActivity<MainRpActivityBinding, BaseLiveDataViewModel> {

    //此页面退出的。是否显示插屏广告
    public static boolean isShowInnerAd = false;

    private Context mContext;

    //    private Animation mScaleAnimation;
    private ExitDialogRecommendGoods mGoods;

    private CountDownTimer mCountDownTimer;

    private float mRestScore;
    private boolean mIsVerify = false;

    @Autowired
    String from;
    @Autowired
    float score;
    @Autowired
    String restId;
    @Autowired
    String preId;


    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);
        mContext = this;

        ARouter.getInstance().inject(this);
        SoundHelp.newInstance().init(this);
        SoundHelp.newInstance().onStart();

        if (isFromNotify()) {
            score = SPUtils.getInformain(NOTIFY_RANDOM_RED_AMOUNT, 0.5f);
            initNotifyRpGet();
        }
        mDataBinding.mainRpDlgCashTv.setText(String.format("%.2f", score));
        mDataBinding.mainRpGiveUp.setOnClickListener(v -> {
            if (!isFromPrivilege()) {
                SoundHelp.newInstance().onStart();
                finish();
            }
        });
        mDataBinding.mainRpCloseIv.setOnClickListener(v -> {
            if (!isFromNotify()) {
                isShowInnerAd = true;
            } else {
                EventBus.getDefault().post(new DoubleRpEvent(5
                        , score
                        , TextUtils.isEmpty(restId) ? "" : restId
                        , TextUtils.isEmpty(preId) ? "" : preId
                        , mRestScore));
            }
            finish();
        });
        mDataBinding.mainRpDouble.setOnClickListener(v -> {
            if (isFromPrivilege()) {
                if (AppInfo.checkIsWXLogin()) {
                    if (mGoods != null) {
                        ARouter.getInstance()
                                .build(RouterFragmentPath.Lottery.PAGER_LOTTERY)
                                .withString("goods_id", mGoods.getGoodsId())
                                .withBoolean("start_lottery", ABSwitch.Ins().isOpenAutoLottery())
                                .withBoolean("privilege", true)
                                .navigation();
                    }
                    finish();
                } else {
                    RouterActivityPath.LoginProvider.getLoginProvider()
                            .loginWX("Front_Rp", "首页>首个红包>登录领红包弹窗");
                }
            } else {
                doubleRp();
            }
        });
        /*if (mScaleAnimation == null) {
            mScaleAnimation = new ScaleAnimation(1.15f, 0.9f, 1.15f, 0.9f, Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            mScaleAnimation.setInterpolator(new LinearInterpolator());
            mScaleAnimation.setRepeatMode(Animation.REVERSE);
            mScaleAnimation.setRepeatCount(Animation.INFINITE);
            mScaleAnimation.setDuration(700);
            mDataBinding.mainRpDouble.startAnimation(mScaleAnimation);
        }*/

        if (isFromPrivilege()) {
            requestPreRp();
            if (AppInfo.checkIsWXLogin()) {
                mDataBinding.mainRpGiveUp.setText("将随机抽取一款奖品");
                mDataBinding.mainRpDoubleTv.setText("抽奖领红包");
                mDataBinding.mainRpPlayIv.setVisibility(View.GONE);
            } else {
                mDataBinding.mainRpGiveUp.setVisibility(View.INVISIBLE);
                mDataBinding.mainRpPlayIv.setVisibility(View.GONE);
                mDataBinding.mainRpDoubleTv.setText("登录领红包");
            }
        } else if (isFromNotify()) {
            mDataBinding.mainRpGiveUp.setText("任意商品抽奖,继续领红包！");
            mDataBinding.mainRpDoubleTv.setText("翻倍领取");
            mDataBinding.mainRpPlayIv.setVisibility(View.VISIBLE);
        }

        if (mCountDownTimer == null) {
            mCountDownTimer = new CountDownTimer(1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    try {
                        mDataBinding.mainRpCloseIv.setVisibility(View.VISIBLE);
                    } catch (Exception e) {

                    }
                }
            };
        }
        mCountDownTimer.start();
        LottieUtil.initLottieView(mDataBinding.mainRpLittleHandLav);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loginStatusEvent(LoginLodingStartStatus event) {
//        LogUtil.e(event.getTag() + "********************");
        if (!event.getTag().equalsIgnoreCase("Front_Rp")) {
            return;
        }

        MutableLiveData<Integer> mld = event.getLoginLoadingLiveData();
        mld.observe(this, d -> {
            if (d == 2) {
                checkRpData();
            } else if (d == -1) {
                ToastUtil.showShort(this, "微信登录失败，请重试!");
            }
        });
    }

    private void checkRpData() {
        showLoading("红包状态检查中...");
        mDataBinding.mainRpDouble.setClickable(false);
        EasyHttp.get(BuildConfig.API_WALLET_URL + "v1/red-packet")
                .cacheMode(CacheMode.NO_CACHE)
                .isShowToast(false)
                .execute(new SimpleCallBack<WalletBean>() {

                    @Override
                    public void onError(ApiException e) {
                        hideLoading();
                        mDataBinding.mainRpDouble.setClickable(true);
                    }

                    @Override
                    public void onSuccess(WalletBean walletBean) {
                        hideLoading();
                        mDataBinding.mainRpDouble.setClickable(true);

                        if (walletBean == null || walletBean.getList() == null || walletBean.getList().size() <= 0) {
                            finish();
                            return;
                        }

                        WalletBean.RpBean bean = walletBean.getList().get(0);
                        if (bean.getOpened()) {
                            ToastUtil.showShort(mContext, "奖励已发放");
                            finish();
                            return;
                        }

                        if (mGoods != null) {
                            ARouter.getInstance()
                                    .build(RouterFragmentPath.Lottery.PAGER_LOTTERY)
                                    .withString("goods_id", mGoods.getGoodsId())
                                    .withBoolean("start_lottery", ABSwitch.Ins().isOpenAutoLottery())
                                    .withBoolean("privilege", true)
                                    .navigation();
                        }
                        finish();
                    }
                });
    }

    @Override
    protected int getLayoutId() {
        ImmersionBar.with(this)
                .statusBarColor(R.color.transparent)
                .navigationBarColor(R.color.black)
                .fitsSystemWindows(true)
                .autoDarkModeEnable(true)
                .hideBar(BarHide.FLAG_HIDE_STATUS_BAR)
                .init();
        return R.layout.main_rp_activity;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void initView() {

    }

    private boolean isFromPrivilege() {
        return from != null && from.equalsIgnoreCase("privilege");
    }

    private boolean isFromNotify() {
        return !TextUtils.isEmpty(from) && from.equalsIgnoreCase("notify");
    }

    private void doubleRp() {
        showLoading("视频加载中...");
        IAdRewardVideoListener listener = new SimpleRewardVideoListener() {
            @Override
            public void onAdStartLoad() {
            }

            @Override
            public void onAdStatus(int code, @Nullable Object any) {
            }

            @Override
            public void onAdLoad() {
            }

            @Override
            public void onAdShow() {
                hideLoading();
            }

            @Override
            public void onAdVideoClick() {
            }

            @Override
            public void onRewardVerify(boolean result) {
                mIsVerify = result;
               /* if (result) {

                }*/
            }

            @Override
            public void onAdClose() {
                hideLoading();
                if (!mIsVerify) {
                    ToastUtil.showShort(mContext, "未完整看完视频,请重新观看获得奖励!");
                    return;
                }
                // 完整观看视频
                if (from != null && from.equalsIgnoreCase("wallTask")) {
                    EventBus.getDefault().post(new DoubleRpEvent(4, score, restId == null ? "" : restId, preId == null ? "" : preId, mRestScore));
                } else if (isFromNotify()) {
                    EventBus.getDefault().post(new DoubleRpEvent(6, score, restId == null ? "" : restId, preId == null ? "" : preId, mRestScore));
                }else {
                    EventBus.getDefault().post(new DoubleRpEvent(1, score, restId == null ? "" : restId, preId == null ? "" : preId, mRestScore));
                }
                finish();
            }

            @Override
            public void onVideoCached() {
            }

            @Override
            public void onVideoComplete() {
            }

            @Override
            public void onAdError(int code, @Nullable String errorMsg) {
                ToastUtil.showShort(mContext, "视频加载失败，点击翻倍领取重试");
                hideLoading();
            }
        };

        RewardVideoAd.INSTANCE.showPreloadRewardVideo(this, listener, false);
    }

    private void requestPreRp() {
        EasyHttp.post(HttpConfigUtilsKt.withConfigParams(BuildConfig.API_WALLET_URL + "v1/pre-red-packet", true))
                .cacheMode(CacheMode.NO_CACHE)
                .upJson("{\"suuid\":\"" + DeviceUtils.getMyUUID() + "\"}")
                .execute(new SimpleCallBack<PreRpBean>() {
                    @Override
                    public void onError(ApiException e) {
                    }

                    @SuppressLint("DefaultLocale")
                    @Override
                    public void onSuccess(PreRpBean bean) {
                        if (bean == null) {
                            mDataBinding.mainRpDlgCashTv.setText("");
                            ToastUtil.showShort(mContext, "获取红包失败，请重试");
                            finish();
                        } else {
                            mDataBinding.mainRpDlgCashTv.setText(String.format("%.2f", bean.getPre_score()));
                            SPUtils.setInformain(KeySharePreferences.FIRST_RP_OPEN_PRE_ID, bean.getPre_id());
                            SPUtils.setInformain(KeySharePreferences.FIRST_RP_OPEN_PRE_SCORE, bean.getPre_score());
                            requestGoodsInfo();
                        }
                    }
                });
    }

    @Keep
    public static class notifyRpBean {
        String rest_id;
        float rest_score;
    }

    //通知栏来的红包
    private void initNotifyRpGet() {
        EasyHttp.post(HttpConfigUtilsKt.withConfigParams(BuildConfig.API_WALLET_URL + "v1/landing-page-red-packet", true))
                .cacheMode(CacheMode.NO_CACHE)
                .upJson("{\"score\":" + score + "}")
                .execute(new SimpleCallBack<notifyRpBean>() {
                    @Override
                    public void onError(ApiException e) {
                    }

                    @SuppressLint("DefaultLocale")
                    @Override
                    public void onSuccess(notifyRpBean bean) {
                        if (bean == null) {
                            mDataBinding.mainRpDlgCashTv.setText("");
                            ToastUtil.showShort(mContext, "获取红包失败，请重试");
                            finish();
                        } else {
                            restId = bean.rest_id;
                            mRestScore = bean.rest_score;
                        }
                    }
                });
    }

    private void requestGoodsInfo() {
        String url = HttpConfigUtilsKt.withConfigParams(BuildConfig.API_LOTTERY_URL + "v1/recommend-goods-list", true)
                + "&limit=1&first=false";
        EasyHttp.get(url)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<ExitDialogRecommendGoodsResp>() {

                    @Override
                    public void onError(ApiException e) {
                        ToastUtil.showShort(mContext, "获取红包失败，请重试");
                        finish();
                    }

                    @Override
                    public void onSuccess(ExitDialogRecommendGoodsResp bean) {
                        if (bean == null || bean.getList() == null || bean.getList().size() <= 0) {
                            return;
                        }
                        mGoods = bean.getList().get(0);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }

        LottieUtil.cancelLottieView(mDataBinding.mainRpLittleHandLav);

        /*if (mDataBinding.mainRpDouble.getAnimation() != null) {
            mDataBinding.mainRpDouble.clearAnimation();
        }*/

        /*if (mScaleAnimation != null) {
            mScaleAnimation.cancel();
            mScaleAnimation = null;
        }*/

        SoundHelp.newInstance().onRelease();

        EventBus.getDefault().unregister(this);
    }
}