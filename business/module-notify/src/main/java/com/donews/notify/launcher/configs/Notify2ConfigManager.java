package com.donews.notify.launcher.configs;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.SPUtils;
import com.donews.common.BuildConfig;
import com.donews.network.EasyHttp;
import com.donews.network.cache.model.CacheMode;
import com.donews.network.callback.SimpleCallBack;
import com.donews.network.exception.ApiException;
import com.donews.notify.launcher.configs.baens.Notify2DataConfigBean;
import com.donews.utilslibrary.utils.JsonUtils;
import com.donews.utilslibrary.utils.LogUtil;

public class Notify2ConfigManager {
    private static final int UPDATE_CONFIG_MSG = 21002;
    private static boolean isInit = false;
    private static String cacheKey = "notify_data_cache";
    private Notify2DataConfigBean mFrontConfigBean;

    private static final class Holder {
        private static final Notify2ConfigManager s_notifyConfigMgr = new Notify2ConfigManager();
    }

    public static Notify2ConfigManager Ins() {
        return Holder.s_notifyConfigMgr;
    }

    private Notify2ConfigManager() {
    }

    /**
     * 获取配置数据
     * @return
     */
    public Notify2DataConfigBean getNotifyConfigBean() {
        if (mFrontConfigBean == null) {
            String cache = SPUtils.getInstance(this.getClass().getSimpleName()).getString(cacheKey);
            if (cache == null || cache.equals("")) {
                mFrontConfigBean = new Notify2DataConfigBean();
            } else {
                try {
                    mFrontConfigBean = GsonUtils.fromJson(cache, Notify2DataConfigBean.class);
                } catch (Exception e) {
                    mFrontConfigBean = new Notify2DataConfigBean();
                    e.printStackTrace();
                }
            }
        }
        return mFrontConfigBean;
    }

    /**
     * 更新数据
     * @param bean
     */
    private void setNotifyConfigBean(Notify2DataConfigBean bean) {
        try {
            SPUtils.getInstance(this.getClass().getSimpleName()).put(cacheKey, GsonUtils.toJson(bean));
        } catch (Exception e) {
            SPUtils.getInstance(this.getClass().getSimpleName()).put(cacheKey, "");
            e.printStackTrace();
        }
        mFrontConfigBean = bean;
    }

    private static Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == UPDATE_CONFIG_MSG) {
                Notify2ConfigManager.update();
            }
        }
    };

    public void init() {
        if (isInit) {
            return;
        }
        isInit = true;
        update();
    }

    private static void update() {
        LogUtil.i("Notify2ConfigManager update");
        EasyHttp.get(BuildConfig.BASE_CONFIG_URL + "plus-notify-datas" + BuildConfig.BASE_RULE_URL +
                JsonUtils.getCommonJson(false))
                .cacheMode(CacheMode.NO_CACHE)
                .isShowToast(BuildConfig.DEBUG)
                .execute(new SimpleCallBack<Notify2DataConfigBean>() {
                    @Override
                    public void onError(ApiException e) {
                        LogUtil.e("Notify2ConfigManager" + e.getCode() + e.getMessage());
                        if (mHandler != null) {
                            if (mHandler.hasMessages(UPDATE_CONFIG_MSG)) {
                                mHandler.removeMessages(UPDATE_CONFIG_MSG);
                            }
                            mHandler.sendEmptyMessageDelayed(UPDATE_CONFIG_MSG, 15 * 1000);
                        }
                    }

                    @Override
                    public void onSuccess(Notify2DataConfigBean bean) {
                        LogUtil.i("Notify2ConfigManager update");

                        if (mHandler != null) {
                            if (mHandler.hasMessages(UPDATE_CONFIG_MSG)) {
                                mHandler.removeMessages(UPDATE_CONFIG_MSG);
                            }
                        }

                        Notify2ConfigManager.Ins().setNotifyConfigBean(bean);
                        if (mHandler != null) {
                            long stepTime = Notify2ConfigManager.Ins().getNotifyConfigBean().refreshInterval * 1000L;
                            if (stepTime <= 10 * 1000) {
                                stepTime = 10 * 1000;
                            }
                            mHandler.sendEmptyMessageDelayed(UPDATE_CONFIG_MSG, stepTime);
                        }
                    }
                });
    }
}
