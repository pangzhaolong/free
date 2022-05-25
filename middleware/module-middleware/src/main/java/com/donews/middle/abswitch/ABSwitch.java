package com.donews.middle.abswitch;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import com.donews.common.BuildConfig;
import com.donews.common.contract.BaseCustomViewModel;
import com.donews.middle.bean.globle.OtherBean;
import com.donews.middle.cache.GoodsCache;
import com.donews.network.EasyHttp;
import com.donews.network.cache.model.CacheMode;
import com.donews.network.callback.SimpleCallBack;
import com.donews.network.exception.ApiException;
import com.donews.utilslibrary.utils.HttpConfigUtilsKt;
import com.donews.utilslibrary.utils.KeySharePreferences;
import com.donews.utilslibrary.utils.LogUtil;
import com.donews.utilslibrary.utils.SPUtils;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ABSwitch {
    private ABSwitchBean mAbSwitchBean;

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
        if (mAbSwitchBean == null) {
            mAbSwitchBean = new ABSwitchBean();
            mAbSwitchBean.setOpenAB(SPUtils.getInformain("Is_Open_AB", true));
        }
    }

    public ABSwitchBean getAbBean() {
        if (mAbSwitchBean == null) {
            mAbSwitchBean = new ABSwitchBean();
        }
        return mAbSwitchBean;
    }

    public void setAbBean(ABSwitchBean bean) {
        mAbSwitchBean = bean;
    }

    public boolean isOpenAB() {
        return mAbSwitchBean.isOpenAB();
    }


    public void init() {
        if (mHandler != null) {
            mHandler.sendEmptyMessage(UPDATE_CONFIG_MSG);
        } else {
            update();
        }
    }

    private void update() {
        LogUtil.e("ABSwitch update");
        EasyHttp.get(HttpConfigUtilsKt.withConfigParams(BuildConfig.BASE_CONFIG_URL +BuildConfig.APP_IDENTIFICATION+"-abswitch"
                + BuildConfig.BASE_RULE_URL, true))
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<ABSwitchBean>() {
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
                            mHandler.sendEmptyMessageDelayed(UPDATE_CONFIG_MSG, 20 * 1000L);
                        }
                    }

                    @Override
                    public void onSuccess(ABSwitchBean abSwitchBean) {
                        ABSwitch.Ins().setAbBean(abSwitchBean);
                        GoodsCache.saveGoodsBean(abSwitchBean, "abswitch");
                        if (!ABSwitch.Ins().getAbBean().isOpenAB()) {
                            SPUtils.setInformain("Is_Open_AB", false);
                        } else {
                            if (!SPUtils.getInformain("Is_Open_AB", true)) {
                                ABSwitch.Ins().getAbBean().setOpenAB(false);
                            }
                        }
                        callOnSuccess();

                        if (mHandler != null) {
                            mHandler.sendEmptyMessageDelayed(UPDATE_CONFIG_MSG, ABSwitch.Ins().getAbBean().getRefreshInterval() * 1000L);
                        }
                    }
                });
    }

    public static class ABSwitchBean extends BaseCustomViewModel {
        @SerializedName("openAB")
        private boolean openAB;
        @SerializedName("refreshInterval")
        private int refreshInterval = 30;

        public int getRefreshInterval() {
            return refreshInterval;
        }

        public boolean isOpenAB() {
            return openAB;
        }

        public void setOpenAB(boolean openAB) {
            this.openAB = openAB;
        }

        @Override
        public String toString() {
            return "ABBean{" +
                    "ab='" + openAB + '\'' +
                    '}';
        }
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
