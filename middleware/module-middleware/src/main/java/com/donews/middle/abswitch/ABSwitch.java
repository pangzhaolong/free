package com.donews.middle.abswitch;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import com.donews.common.BuildConfig;
import com.donews.middle.bean.globle.ABBean;
import com.donews.middle.cache.GoodsCache;
import com.donews.network.EasyHttp;
import com.donews.network.cache.model.CacheMode;
import com.donews.network.callback.SimpleCallBack;
import com.donews.network.exception.ApiException;
import com.donews.utilslibrary.utils.HttpConfigUtilsKt;
import com.donews.utilslibrary.utils.LogUtil;
import com.donews.utilslibrary.utils.SPUtils;

public class ABSwitch {
    private ABBean mAbBean;

    private static final int UPDATE_CONFIG_MSG = 11003;

    private static final class Holder {
        private static final ABSwitch s_abSwitchMgr = new ABSwitch();
    }

    private static final Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == UPDATE_CONFIG_MSG) {
                ABSwitch.update();
            }
        }
    };

    public static ABSwitch Ins() {
        return Holder.s_abSwitchMgr;
    }

    private ABSwitch() {
        if (mAbBean == null) {
            mAbBean = new ABBean();
            mAbBean.setOpenAB(SPUtils.getInformain("Is_Open_AB", true));
        }
    }

    public ABBean getAbBean() {
        if (mAbBean == null) {
            mAbBean = new ABBean();
        }
        return mAbBean;
    }

    public void setAbBean(ABBean bean) {
        mAbBean = bean;
    }


    public void init() {
        if (mHandler != null) {
            mHandler.sendEmptyMessage(UPDATE_CONFIG_MSG);
        } else {
            update();
        }
    }

    private static void update() {
        LogUtil.e("ABSwitch update");
        EasyHttp.get(HttpConfigUtilsKt.withConfigParams(BuildConfig.BASE_CONFIG_URL + "ddyb-abswitch"
                + BuildConfig.BASE_RULE_URL, true))
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<ABBean>() {
                    @Override
                    public void onError(ApiException e) {
                        if (!SPUtils.getInformain("Is_Open_AB", true)) {
                            ABSwitch.Ins().getAbBean().setOpenAB(false);
                        } else {
                            ABSwitch.Ins().getAbBean().setOpenAB(true);
                        }
                        if (mHandler != null) {
                            if (mHandler.hasMessages(UPDATE_CONFIG_MSG)) {
                                mHandler.removeMessages(UPDATE_CONFIG_MSG);
                            }
                            mHandler.sendEmptyMessageDelayed(UPDATE_CONFIG_MSG, 20 * 1000);
                        }
                    }

                    @Override
                    public void onSuccess(ABBean abBean) {
                        ABSwitch.Ins().setAbBean(abBean);
                        GoodsCache.saveGoodsBean(abBean, "abswitch");
                        if (!ABSwitch.Ins().getAbBean().isOpenAB()) {
                            SPUtils.setInformain("Is_Open_AB", false);
                        } else {
                            if (!SPUtils.getInformain("Is_Open_AB", true)) {
                                ABSwitch.Ins().getAbBean().setOpenAB(false);
                            }
                        }

                        if (mHandler != null) {
                            mHandler.sendEmptyMessageDelayed(UPDATE_CONFIG_MSG, ABSwitch.Ins().getAbBean().getRefreshInterval() * 1000L);
                        }
                    }
                });
    }


    public boolean isOpenAB() {
        return mAbBean.isOpenAB();
    }

    public boolean isOpenVideoToast() {
        return mAbBean.isOpenVideoToast();
    }
    public boolean isOpenOptionalCode() {
        return mAbBean.isOpenOptionalCode();
    }

    public int getLotteryPriceShow() {
        return mAbBean.getLotteryPriceShow();
    }


    public boolean isOpenAutoLottery() {
        return mAbBean.isOpenAutoLottery();
    }

    public int isOpenHomeGuid() {
        return mAbBean.getOpenHomeGuid();
    }

    public boolean isOpenAutoAgreeProtocol() {
        return mAbBean.isOpenAutoAgreeProtocol();
    }

    public int getOpenAutoLotteryCount() {
        return mAbBean.getOpenAutoLotteryCount();
    }

    public boolean isOpenAutoLotteryAfterLoginWxAtExitDialog() {
        return mAbBean.isOpenAutoLotteryAfterLoginWxAtExitDialog();
    }

    public boolean isOpenAutoLotteryAfterLoginWx() {
        return mAbBean.isOpenAutoLotteryAfterLoginWx();
    }

    public boolean isOpenGuidGif() {
        return mAbBean.isOpenGuidGif();
    }

    public int getLotteryLine() {
        return mAbBean.getLotteryLine();
    }

    public int getEnableOpenCritModelCount() {
        return mAbBean.getEnableOpenCritModelCount();
    }

    public boolean isOpenCritModelByNewUser() {
        return mAbBean.isOpenCritModelByNewUser();
    }

    public int getOpenCritModelByNewUserCount() {
        return mAbBean.getOpenCritModelByNewUserCount();
    }

    public int getOpenCritModelByOldUserCount() {
        return mAbBean.getOpenCritModelByOldUserCount();
    }

    public boolean isOpenScoreModelCrit() {
        return mAbBean.isOpenScoreModelCrit();
    }

    public boolean getOpenCritModel() {
        return mAbBean.isOpenCritModel();
    }

    public int getScoreTaskPlayTime() {
        return mAbBean.getScoreTaskPlayTime();
    }

    public boolean isOpenScoreTask() {
        return mAbBean.isOpenScoreTask();
    }

    public int getOpenScoreTaskMax() {
        return mAbBean.getOpenScoreTaskMax();
    }

    public int getSelectNumberLocation() {
        return mAbBean.getSelectNumberLocation();
    }

    public boolean isOpenJumpDlg() {
        return mAbBean.isOpenJumpDlg();
    }

    public boolean isOpenSkipSplashAd4NewUser() {
        return mAbBean.isOpenSkipSplashAd4NewUser();
    }
}
