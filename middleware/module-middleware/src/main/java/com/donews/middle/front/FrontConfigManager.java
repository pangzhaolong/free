package com.donews.middle.front;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import com.donews.common.BuildConfig;
import com.donews.middle.api.MiddleApi;
import com.donews.middle.bean.front.FrontConfigBean;
import com.donews.network.EasyHttp;
import com.donews.network.cache.model.CacheMode;
import com.donews.network.callback.SimpleCallBack;
import com.donews.network.exception.ApiException;
import com.donews.utilslibrary.utils.JsonUtils;
import com.donews.utilslibrary.utils.LogUtil;

public class FrontConfigManager {
    private static final int UPDATE_CONFIG_MSG = 11001;
    private FrontConfigBean mFrontConfigBean;

    private static final class Holder {
        private static final FrontConfigManager s_frontConfigMgr = new FrontConfigManager();
    }

    public static FrontConfigManager Ins() {
        return Holder.s_frontConfigMgr;
    }

    private FrontConfigManager() {
    }

    public FrontConfigBean getConfigBean() {
        if (mFrontConfigBean == null) {
            mFrontConfigBean = new FrontConfigBean();
        }
        return mFrontConfigBean;
    }

    public void setFrontConfigBean(FrontConfigBean bean) {
        mFrontConfigBean = bean;
    }

    private static final Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == UPDATE_CONFIG_MSG) {
                FrontConfigManager.update();
            }
        }
    };

    public void init() {
        if (mHandler != null) {
            mHandler.sendEmptyMessage(UPDATE_CONFIG_MSG);
        } else {
            update();
        }
    }

    private static void update() {
        LogUtil.i("FrontConfigManager update");
        EasyHttp.get(MiddleApi.frontConfigUrl + JsonUtils.getCommonJson(false))
                .cacheMode(CacheMode.NO_CACHE)
                .isShowToast(BuildConfig.DEBUG)
                .execute(new SimpleCallBack<FrontConfigBean>() {
                    @Override
                    public void onError(ApiException e) {
                        LogUtil.e("FrontConfigManager" + e.getCode() + e.getMessage());
                        if (mHandler != null) {
                            if (mHandler.hasMessages(UPDATE_CONFIG_MSG)) {
                                mHandler.removeMessages(UPDATE_CONFIG_MSG);
                            }
                            mHandler.sendEmptyMessageDelayed(UPDATE_CONFIG_MSG, 20 * 1000);
                        }
                    }

                    @Override
                    public void onSuccess(FrontConfigBean bean) {
                        LogUtil.i("FrontConfigManager update");

                        if (mHandler != null) {
                            if (mHandler.hasMessages(UPDATE_CONFIG_MSG)) {
                                mHandler.removeMessages(UPDATE_CONFIG_MSG);
                            }
                        }

                        FrontConfigManager.Ins().setFrontConfigBean(bean);
                        if (mHandler != null) {
                            mHandler.sendEmptyMessageDelayed(UPDATE_CONFIG_MSG, FrontConfigManager.Ins().getConfigBean().getRefreshInterval() * 1000L);
                        }
                    }
                });
    }
}
