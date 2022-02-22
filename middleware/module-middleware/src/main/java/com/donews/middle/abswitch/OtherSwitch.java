package com.donews.middle.abswitch;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import com.donews.common.BuildConfig;
import com.donews.middle.bean.globle.OtherBean;
import com.donews.middle.cache.GoodsCache;
import com.donews.network.EasyHttp;
import com.donews.network.cache.model.CacheMode;
import com.donews.network.callback.SimpleCallBack;
import com.donews.network.exception.ApiException;
import com.donews.utilslibrary.utils.HttpConfigUtilsKt;
import com.donews.utilslibrary.utils.LogUtil;
import com.donews.utilslibrary.utils.SPUtils;

import java.util.List;

public class OtherSwitch {
    private OtherBean mOtherBean;

    private static final int UPDATE_CONFIG_MSG = 11003;

    private static final class Holder {
        private static final OtherSwitch s_abSwitchMgr = new OtherSwitch();
    }

    private static final Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == UPDATE_CONFIG_MSG) {
                OtherSwitch.update();
            }
        }
    };

    public static OtherSwitch Ins() {
        return Holder.s_abSwitchMgr;
    }

    private OtherSwitch() {
        if (mOtherBean == null) {
            mOtherBean = new OtherBean();
        }
    }

    public OtherBean getAbBean() {
        if (mOtherBean == null) {
            mOtherBean = new OtherBean();
        }
        return mOtherBean;
    }

    public void setAbBean(OtherBean bean) {
        mOtherBean = bean;
    }


    public void init() {
        if (mHandler != null) {
            mHandler.sendEmptyMessage(UPDATE_CONFIG_MSG);
        } else {
            update();
        }
    }

    private static void update() {
        LogUtil.e("OtherSwitch update");
        EasyHttp.get(HttpConfigUtilsKt.withConfigParams(BuildConfig.BASE_CONFIG_URL + "ddyb-otherSwitch"
                + BuildConfig.BASE_RULE_URL, true))
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<OtherBean>() {
                    @Override
                    public void onError(ApiException e) {
                        if (mHandler != null) {
                            if (mHandler.hasMessages(UPDATE_CONFIG_MSG)) {
                                mHandler.removeMessages(UPDATE_CONFIG_MSG);
                            }
                            mHandler.sendEmptyMessageDelayed(UPDATE_CONFIG_MSG, 20 * 1000);
                        }
                    }

                    @Override
                    public void onSuccess(OtherBean otherBean) {
                        OtherSwitch.Ins().setAbBean(otherBean);
                        if (mHandler != null) {
                            mHandler.sendEmptyMessageDelayed(UPDATE_CONFIG_MSG, OtherSwitch.Ins().getAbBean().getRefreshInterval() * 1000L);
                        }
                    }
                });
    }

    public boolean isOpenVideoToast() {
        return mOtherBean.isOpenVideoToast();
    }
    public boolean isOpenOptionalCode() {
        return mOtherBean.isOpenOptionalCode();
    }

    public int getLotteryPriceShow() {
        return mOtherBean.getLotteryPriceShow();
    }

    public boolean isOpenAutoLottery() {
        return mOtherBean.isOpenAutoLottery();
    }

    public int isOpenHomeGuid() {
        return mOtherBean.getOpenHomeGuid();
    }

    public boolean isOpenAutoAgreeProtocol() {
        return mOtherBean.isOpenAutoAgreeProtocol();
    }

    public int getOpenAutoLotteryCount() {
        return mOtherBean.getOpenAutoLotteryCount();
    }

    public boolean isOpenAutoLotteryAfterLoginWxAtExitDialog() {
        return mOtherBean.isOpenAutoLotteryAfterLoginWxAtExitDialog();
    }

    public boolean isOpenAutoLotteryAfterLoginWx() {
        return mOtherBean.isOpenAutoLotteryAfterLoginWx();
    }

    public boolean isOpenGuidGif() {
        return mOtherBean.isOpenGuidGif();
    }

    public int getLotteryLine() {
        return mOtherBean.getLotteryLine();
    }

    public int getEnableOpenCritModelCount() {
        return mOtherBean.getEnableOpenCritModelCount();
    }

    public boolean isOpenCritModelByNewUser() {
        return mOtherBean.isOpenCritModelByNewUser();
    }

    public int getOpenCritModelByNewUserCount() {
        return mOtherBean.getOpenCritModelByNewUserCount();
    }

    public int getOpenCritModelByOldUserCount() {
        return mOtherBean.getOpenCritModelByOldUserCount();
    }

    public boolean isOpenScoreModelCrit() {
        return mOtherBean.isOpenScoreModelCrit();
    }

    public boolean getOpenCritModel() {
        return mOtherBean.isOpenCritModel();
    }

    public int getScoreTaskPlayTime() {
        return mOtherBean.getScoreTaskPlayTime();
    }

    public boolean isOpenScoreTask() {
        return mOtherBean.isOpenScoreTask();
    }

    public int getOpenScoreTaskMax() {
        return mOtherBean.getOpenScoreTaskMax();
    }

    public int getSelectNumberLocation() {
        return mOtherBean.getSelectNumberLocation();
    }

    public boolean isSkipSplashAd4NewUser() {
        return mOtherBean.isSkipSplashAd4NewUser();
    }


    public long getIntervalsTime() {
        return  mOtherBean.getIntervalsTime();
    }


    public boolean isOpenJumpDlg() {
        return mOtherBean.isOpenJumpDlg();
    }

    public boolean isApplicationShareJumpSwitch() {
        return  mOtherBean.isApplicationShareJumpSwitch();
    }
    public List<Integer> getOpenOptionalLocationList() {
        return mOtherBean.getOpenOptionalLocationList();
    }

    public boolean isApplicationBuyJumpSwitch() {
        return  mOtherBean.isApplicationBuyJumpSwitch();
    }


    public List<String> getApplicationShareJumpUrl() {
        return  mOtherBean.getApplicationShareJumpUrl();
    }
    public long getApplicationBuyJumpNumber() {
        return mOtherBean.getApplicationBuyJumpNumber();
    }

    public List<String> getApplicationBuyJumpUrl() {
        return  mOtherBean.getApplicationBuyJumpUrl();
    }

    public long getApplicationBuyDelayedJump() {
        return mOtherBean.getApplicationBuyDelayedJump();
    }

    public boolean isScreenUnlockJumpSwitch() {
        return mOtherBean.isScreenUnlockJumpSwitch();
    }

    public long getDelayedJump() {
        return mOtherBean.getDelayedJump();
    }

    public int getRevealNumber() {
        return mOtherBean.getRevealNumber();
    }

}
