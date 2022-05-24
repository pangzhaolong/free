package com.donews.middle.centralDeploy;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.SPUtils;
import com.donews.common.BuildConfig;
import com.donews.middle.bean.globle.ABBean;
import com.donews.middle.bean.globle.OutherSwitchConfigBean;
import com.donews.middle.cache.GoodsCache;
import com.donews.middle.searchs.TopSearchConfig;
import com.donews.network.EasyHttp;
import com.donews.network.cache.model.CacheMode;
import com.donews.network.callback.SimpleCallBack;
import com.donews.network.exception.ApiException;
import com.donews.utilslibrary.utils.HttpConfigUtilsKt;
import com.donews.utilslibrary.utils.KeySharePreferences;
import com.donews.utilslibrary.utils.LogUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class OutherSwitchConfig {
    private OutherSwitchConfigBean bean;

    private static final int UPDATE_CONFIG_MSG = 11003;

    private static final class Holder {
        private static final OutherSwitchConfig s_abSwitchMgr = new OutherSwitchConfig();
    }

    private static final Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == UPDATE_CONFIG_MSG) {
                OutherSwitchConfig.Ins().update();
            }
        }
    };

    public static OutherSwitchConfig Ins() {
        return Holder.s_abSwitchMgr;
    }

    private OutherSwitchConfig() {
        if (bean == null) {
            bean = new OutherSwitchConfigBean();
        }
    }

    public OutherSwitchConfigBean getBean() {
        if (bean == null) {
            bean = new OutherSwitchConfigBean();
        }
        return bean;
    }

    public void setBean(OutherSwitchConfigBean bean) {
        this.bean = bean;
    }


    public void init() {
        if (mHandler != null) {
            mHandler.removeMessages(UPDATE_CONFIG_MSG);
            mHandler.sendEmptyMessage(UPDATE_CONFIG_MSG);
        }
        update();
    }

    private void update() {
        LogUtil.e("Outher Switch update");
        EasyHttp.get(HttpConfigUtilsKt.withConfigParams(BuildConfig.BASE_CONFIG_URL + BuildConfig.APP_IDENTIFICATION + "-" + "otherSwitch"
                + BuildConfig.BASE_RULE_URL, true))
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<OutherSwitchConfigBean>() {
                    @Override
                    public void onError(ApiException e) {
                        if (mHandler != null) {
                            if (mHandler.hasMessages(UPDATE_CONFIG_MSG)) {
                                mHandler.removeMessages(UPDATE_CONFIG_MSG);
                            }
                            mHandler.sendEmptyMessageDelayed(UPDATE_CONFIG_MSG, 20 * 1000);
                            try {
                                String benaJson = SPUtils.getInstance("outherSwitchFile")
                                        .getString("outherSwitch", "");
                                if (benaJson != null && benaJson.length() > 0) {
                                    OutherSwitchConfig.Ins().setBean(
                                            GsonUtils.fromJson(benaJson, OutherSwitchConfigBean.class));
                                }
                            } catch (Exception ee) {
                            }
                        }
                    }

                    @Override
                    public void onSuccess(OutherSwitchConfigBean abBean) {
                        OutherSwitchConfig.Ins().setBean(abBean);
                        if (mHandler != null) {
                            mHandler.sendEmptyMessageDelayed(UPDATE_CONFIG_MSG, OutherSwitchConfig.Ins().getBean().getRefreshInterval() * 1000L);
                        }
                        try {
                            SPUtils.getInstance("outherSwitchFile")
                                    .put("outherSwitch", GsonUtils.toJson(abBean));
                        } catch (Exception e) {
                        }
                    }
                });
    }

}
