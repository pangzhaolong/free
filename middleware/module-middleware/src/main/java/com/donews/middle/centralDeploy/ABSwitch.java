package com.donews.middle.centralDeploy;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import com.donews.common.BuildConfig;
import com.donews.middle.bean.globle.ABBean;
import com.donews.middle.cache.GoodsCache;
import com.donews.middle.searchs.TopSearchConfig;
import com.donews.network.EasyHttp;
import com.donews.network.cache.model.CacheMode;
import com.donews.network.callback.SimpleCallBack;
import com.donews.network.exception.ApiException;
import com.donews.utilslibrary.utils.HttpConfigUtilsKt;
import com.donews.utilslibrary.utils.KeySharePreferences;
import com.donews.utilslibrary.utils.LogUtil;
import com.donews.utilslibrary.utils.SPUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
                ABSwitch.Ins().update();
            }
        }
    };

    public static ABSwitch Ins() {
        return Holder.s_abSwitchMgr;
    }

    private List<CallBack> mListenerList = new ArrayList();

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
            mHandler.removeMessages(UPDATE_CONFIG_MSG);
            mHandler.sendEmptyMessage(UPDATE_CONFIG_MSG);
        }
        //关联更新热门搜索数据
        TopSearchConfig.update();
    }

    private void update() {
        LogUtil.e("ABSwitch update");
        EasyHttp.get(HttpConfigUtilsKt.withConfigParams(BuildConfig.BASE_CONFIG_URL + BuildConfig.APP_IDENTIFICATION + "-" + "abswitch"
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
                        callOnFail();
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
                        callOnSuccess();
                        SPUtils.setInformain(KeySharePreferences.KEY_SERVER_QQ_NUMBER, ABSwitch.Ins().getKfQQ());

                        if (mHandler != null) {
                            mHandler.sendEmptyMessageDelayed(UPDATE_CONFIG_MSG, ABSwitch.Ins().getAbBean().getRefreshInterval() * 1000L);
                        }
                    }
                });
    }


    public boolean isOpenAB() {
        if (BuildConfig.DEBUG) {
            return false;
        }
        return mAbBean.isOpenAB();
    }

    public boolean isOpenVideoToast() {
        return mAbBean.isOpenVideoToast();
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

    public boolean isSkipSplashAd4NewUser() {
        return mAbBean.isSkipSplashAd4NewUser();
    }


    public long getIntervalsTime() {
        return mAbBean.getIntervalsTime();
    }


    public boolean isApplicationShareJumpSwitch() {
        return mAbBean.isApplicationShareJumpSwitch();
    }

    public boolean isOpenJumpDlg() {
        return mAbBean.isOpenJumpDlg();
    }

    public boolean isApplicationBuyJumpSwitch() {
        return mAbBean.isApplicationBuyJumpSwitch();
    }


    public List<String> getApplicationShareJumpUrl() {
        return mAbBean.getApplicationShareJumpUrl();
    }

    public long getApplicationBuyJumpNumber() {
        return mAbBean.getApplicationBuyJumpNumber();
    }

    public List<String> getApplicationBuyJumpUrl() {
        return mAbBean.getApplicationBuyJumpUrl();
    }

    public long getApplicationBuyDelayedJump() {
        return mAbBean.getApplicationBuyDelayedJump();
    }

    public boolean isScreenUnlockJumpSwitch() {
        return mAbBean.isScreenUnlockJumpSwitch();
    }

    public long getDelayedJump() {
        return mAbBean.getDelayedJump();
    }

    public int getRevealNumber() {
        return mAbBean.getRevealNumber();
    }

    public boolean isShowInterstitialAdWhenOpenYyw() {
        return mAbBean.isShowInterstitialAdWhenOpenYyw();
    }

    public int getShowAppToParterCount() {
        return mAbBean.getShowAppToParterCount();
    }

    public String getKfQQ() {
        return mAbBean.getKfQQ();
    }

    public boolean isShowSplashScaleBtn() {
        return mAbBean.isShowSplashScaleBtn();
    }

    public boolean isInitDnSdkWhenApplicationLanuch() {
        return mAbBean.isInitDnSdkWhenApplicationLanuch();
    }

    public void addCallBack(CallBack callBack) {
        mListenerList.add(callBack);
        update();
    }

    private void callOnSuccess() {
        Iterator<CallBack> iterator = mListenerList.iterator();
        while (iterator.hasNext()) {
            CallBack next = (CallBack) iterator.next();
            next.onSuccess();
            iterator.remove();
        }
    }


    private void callOnFail() {
        Iterator<CallBack> iterator = mListenerList.iterator();
        while (iterator.hasNext()) {
            CallBack next = (CallBack) iterator.next();
            next.onFail();
            iterator.remove();
        }
    }

    public interface CallBack {
        void onSuccess();

        void onFail();
    }
}
