package com.donews.main.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.alibaba.android.arouter.launcher.ARouter;
import com.dn.events.events.DoubleRpEvent;
import com.dn.events.events.LoginLodingStartStatus;
import com.dn.sdk.listener.IAdRewardVideoListener;
import com.donews.base.fragmentdialog.AbstractFragmentDialog;
import com.donews.base.fragmentdialog.LoadingHintDialog;
import com.donews.base.utils.ToastUtil;
import com.donews.common.ad.cache.AdVideoCacheUtils;
import com.donews.common.router.RouterActivityPath;
import com.donews.common.router.RouterFragmentPath;
import com.donews.main.BuildConfig;
import com.donews.main.R;
import com.donews.main.databinding.MainRpDialogLayoutExBindingImpl;
import com.donews.main.entitys.resps.ExitDialogRecommendGoods;
import com.donews.main.entitys.resps.ExitDialogRecommendGoodsResp;
import com.donews.middle.abswitch.ABSwitch;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainRpDialog extends AbstractFragmentDialog<MainRpDialogLayoutExBindingImpl> {

    //此页面退出的。是否显示插屏广告
    public static boolean isShowInnerAd = false;

    private Context mContext;
    private ExitDialogRecommendGoods mGoods;
    private CountDownTimer mCountDownTimer;
    private float mRestScore;
    private boolean mIsVerify = false;

    private String mFrom;
    private float mScore;
    private String mRestId;
    private String mPreId;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;

        EventBus.getDefault().register(this);

        SoundHelp.newInstance().init(context);
        SoundHelp.newInstance().onStart();
    }

    public MainRpDialog(String from, float score, String restId, String preId) {
        mFrom = from;
        mScore = score;
        mRestId = restId;
        mPreId = preId;
    }
/*

    @Override
    public int setLayout() {
        return R.layout.main_rp_dialog_layout;
    }

    @Override
    public float setSize() {
        return 0.9f;
    }
*/

    @Override
    protected int getLayoutId() {
        return R.layout.main_rp_dialog_layout_ex;
    }


    protected void initView() {
        if (isFromNotify()) {
            initNotifyRpGet();
        }
        dataBinding.mainRpDlgCashTv.setText(String.format("%.2f", mScore));
        dataBinding.mainRpGiveUp.setOnClickListener(v -> {
            if (!isFromPrivilege()) {
                SoundHelp.newInstance().onStart();
                dismissEx();
            }
        });
        dataBinding.mainRpCloseIv.setOnClickListener(v -> {
            if (!isFromNotify()) {
                isShowInnerAd = true;
            } else {
                EventBus.getDefault().post(new DoubleRpEvent(5
                        , mScore
                        , TextUtils.isEmpty(mRestId) ? "" : mRestId
                        , TextUtils.isEmpty(mPreId) ? "" : mPreId
                        , mRestScore));
            }
            dismissEx();
        });
        dataBinding.mainRpDouble.setOnClickListener(v -> {
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
                    dismissEx();
                } else {
                    RouterActivityPath.LoginProvider.getLoginProvider()
                            .loginWX("Front_Rp", "首页>首个红包>登录领红包弹窗");
                }
            } else {
                doubleRp();
            }
        });

        if (isFromPrivilege()) {
            requestPreRp();
            if (AppInfo.checkIsWXLogin()) {
                dataBinding.mainRpGiveUp.setText("将随机抽取一款奖品");
                dataBinding.mainRpDoubleTv.setText("抽奖领红包");
                dataBinding.mainRpPlayIv.setVisibility(View.GONE);
            } else {
                dataBinding.mainRpGiveUp.setVisibility(View.INVISIBLE);
                dataBinding.mainRpPlayIv.setVisibility(View.GONE);
                dataBinding.mainRpDoubleTv.setText("登录领红包");
            }
        } else if (isFromNotify()) {
            dataBinding.mainRpGiveUp.setText("任意商品抽奖,继续领红包！");
            dataBinding.mainRpDoubleTv.setText("翻倍领取");
            dataBinding.mainRpPlayIv.setVisibility(View.VISIBLE);
        }

        if (mCountDownTimer == null) {
            mCountDownTimer = new CountDownTimer(1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    try {
                        dataBinding.mainRpCloseIv.setVisibility(View.VISIBLE);
                    } catch (Exception e) {

                    }
                }
            };
        }
        mCountDownTimer.start();
        LottieUtil.initLottieView(dataBinding.mainRpLittleHandLav);
    }

    @Override
    protected boolean isUseDataBinding() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loginStatusEvent(LoginLodingStartStatus event) {
//        LogUtil.e(event.getTag() + "********************");
        if (!event.getTag().equalsIgnoreCase("Front_Rp")) {
            return;
        }

        MutableLiveData<Integer> mld = event.getLoginLoadingLiveData();
        mld.observe(this.getViewLifecycleOwner(), d -> {
            if (d == 2) {
                checkRpData();
            } else if (d == -1) {
                ToastUtil.showShort(mContext, "微信登录失败，请重试!");
            }
        });
    }

    private void checkRpData() {
        showLoading("红包状态检查中...");
        dataBinding.mainRpDouble.setClickable(false);
        EasyHttp.get(BuildConfig.API_WALLET_URL + "v1/red-packet")
                .cacheMode(CacheMode.NO_CACHE)
                .isShowToast(false)
                .execute(new SimpleCallBack<WalletBean>() {

                    @Override
                    public void onError(ApiException e) {
                        hideLoading();
                        dataBinding.mainRpDouble.setClickable(true);
                    }

                    @Override
                    public void onSuccess(WalletBean walletBean) {
                        hideLoading();
                        dataBinding.mainRpDouble.setClickable(true);

                        if (walletBean == null || walletBean.getList() == null || walletBean.getList().size() <= 0) {
                            dismissEx();
                            return;
                        }

                        WalletBean.RpBean bean = walletBean.getList().get(0);
                        if (bean.getOpened()) {
                            ToastUtil.showShort(mContext, "奖励已发放");
                            dismissEx();
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
                        dismissEx();
                    }
                });
    }


    private boolean isFromPrivilege() {
        return !TextUtils.isEmpty(mFrom) && mFrom.equalsIgnoreCase("privilege");
    }

    private boolean isFromNotify() {
        return !TextUtils.isEmpty(mFrom) && mFrom.equalsIgnoreCase("notify");
    }

    private void doubleRp() {
        showLoading("视频加载中...");
        IAdRewardVideoListener listener = new IAdRewardVideoListener() {
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
                if (mFrom != null && mFrom.equalsIgnoreCase("wallTask")) {
                    EventBus.getDefault().post(new DoubleRpEvent(4, mScore, mRestId == null ? "" : mRestId, mPreId == null ? "" : mPreId, mRestScore));
                } else if (isFromNotify()) {
                    EventBus.getDefault().post(new DoubleRpEvent(6, mScore, mRestId == null ? "" : mRestId, mPreId == null ? "" : mPreId, mRestScore));
                } else {
                    EventBus.getDefault().post(new DoubleRpEvent(1, mScore, mRestId == null ? "" : mRestId, mPreId == null ? "" : mPreId, mRestScore));
                }
                dismissEx();
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

        AdVideoCacheUtils.INSTANCE.showRewardVideo(listener);
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
                            dataBinding.mainRpDlgCashTv.setText("");
                            ToastUtil.showShort(mContext, "获取红包失败，请重试");
                            dismissEx();
                        } else {
                            dataBinding.mainRpDlgCashTv.setText(String.format("%.2f", bean.getPre_score()));
                            SPUtils.setInformain(KeySharePreferences.FIRST_RP_OPEN_PRE_ID, bean.getPre_id());
                            SPUtils.setInformain(KeySharePreferences.FIRST_RP_OPEN_PRE_SCORE, bean.getPre_score());
                            requestGoodsInfo();
                        }
                    }
                });
    }

    @Keep
    private static class notifyRpBean {
        String rest_id;
        float rest_score;
    }

    //通知栏来的红包
    private void initNotifyRpGet() {
        EasyHttp.post(HttpConfigUtilsKt.withConfigParams(BuildConfig.API_WALLET_URL + "v1/landing-page-red-packet", true))
                .cacheMode(CacheMode.NO_CACHE)
                .upJson("{\"score\":" + mScore + "}")
                .execute(new SimpleCallBack<notifyRpBean>() {
                    @Override
                    public void onError(ApiException e) {
                    }

                    @SuppressLint("DefaultLocale")
                    @Override
                    public void onSuccess(notifyRpBean bean) {
                        if (bean == null) {
                            dataBinding.mainRpDlgCashTv.setText("");
                            ToastUtil.showShort(mContext, "获取红包失败，请重试");
                            dismissEx();
                        } else {
                            mRestId = bean.rest_id;
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
                        dismissEx();
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


    protected LoadingHintDialog loadingHintDialog;

    public void showLoading() {
        if (loadingHintDialog != null) {
            hideLoading();
        }
        loadingHintDialog = new LoadingHintDialog();
        loadingHintDialog.setDismissOnBackPressed(false)
                .setDescription("提交中...")
                .show(this.getParentFragmentManager(), "user_cancellation");
    }

    public void showLoading(String msg) {
        if (loadingHintDialog != null) {
            hideLoading();
        }
        loadingHintDialog = new LoadingHintDialog();
        loadingHintDialog.setDismissOnBackPressed(false)
                .setDescription(msg)
                .showAllowingStateLoss(this.getParentFragmentManager(), "user_cancellation");
    }

    public void hideLoading() {
        if (loadingHintDialog != null) {
            loadingHintDialog.disMissDialog();
        }
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }

        LottieUtil.cancelLottieView(dataBinding.mainRpLittleHandLav);

        SoundHelp.newInstance().onRelease();

        EventBus.getDefault().unregister(this);
    }

    private void dismissEx() {
        new Handler().postDelayed(()-> dismiss(), 200);
    }
/*
    @Override
    public void dismiss() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }

        LottieUtil.cancelLottieView(dataBinding.mainRpLittleHandLav);

        SoundHelp.newInstance().onRelease();

        EventBus.getDefault().unregister(this);

        super.dismiss();
    }*/
}
