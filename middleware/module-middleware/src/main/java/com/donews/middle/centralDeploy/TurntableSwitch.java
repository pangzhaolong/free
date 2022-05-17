package com.donews.middle.centralDeploy;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import com.donews.common.BuildConfig;
import com.donews.middle.bean.globle.TurntableBean;
import com.donews.middle.cache.GoodsCache;
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

//大转盘的中台配置
public class TurntableSwitch {
    private TurntableBean mTurntableBean;

    private static final int UPDATE_CONFIG_MSG = 11003;

    private static final class Holder {
        private static final TurntableSwitch s_turntableSwitch = new TurntableSwitch();
    }

    private static final Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == UPDATE_CONFIG_MSG) {
                TurntableSwitch.Ins().update();
            }
        }
    };

    public static TurntableSwitch Ins() {
        return Holder.s_turntableSwitch;
    }

    private List<CallBack> mListenerList = new ArrayList();

    private TurntableSwitch() {
        if (mTurntableBean == null) {
            mTurntableBean = new TurntableBean();
        }
    }

    public TurntableBean getTurntableBean() {
        if (mTurntableBean == null) {
            mTurntableBean = new TurntableBean();
        }
        return mTurntableBean;
    }

    public void setAbBean(TurntableBean bean) {
        mTurntableBean = bean;
    }


    public void init() {
        if (mHandler != null) {
            mHandler.removeMessages(UPDATE_CONFIG_MSG);
            mHandler.sendEmptyMessage(UPDATE_CONFIG_MSG);
        }
    }

    private void update() {
        LogUtil.e("ABSwitch update");
        EasyHttp.get(HttpConfigUtilsKt.withConfigParams(BuildConfig.BASE_CONFIG_URL + BuildConfig.APP_IDENTIFICATION + "-" + "turntableConfig"
                + BuildConfig.BASE_RULE_URL, true))
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<TurntableBean>() {
                    @Override
                    public void onError(ApiException e) {
                        callOnFail();
                        if (mHandler != null) {
                            if (mHandler.hasMessages(UPDATE_CONFIG_MSG)) {
                                mHandler.removeMessages(UPDATE_CONFIG_MSG);
                            }
                            mHandler.sendEmptyMessageDelayed(UPDATE_CONFIG_MSG, 20 * 1000);
                        }
                    }

                    @Override
                    public void onSuccess(TurntableBean TurntableBean) {
                        TurntableSwitch.Ins().setAbBean(TurntableBean);
                        GoodsCache.saveGoodsBean(TurntableBean, "turntableSwitch");
                        callOnSuccess();
                        if (mHandler != null) {
                            mHandler.sendEmptyMessageDelayed(UPDATE_CONFIG_MSG, TurntableSwitch.Ins().getTurntableBean().getRefreshInterval() * 1000L);
                        }
                    }
                });
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
