package com.donews.common;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import com.donews.base.utils.GsonUtils;
import com.donews.common.bean.AppGlobalConfigBean;
import com.donews.common.utils.CompareObjectUtils;
import com.donews.network.EasyHttp;
import com.donews.network.cache.model.CacheMode;
import com.donews.network.callback.SimpleCallBack;
import com.donews.network.exception.ApiException;
import com.donews.utilslibrary.utils.JsonUtils;
import com.donews.utilslibrary.utils.KeySharePreferences;
import com.donews.utilslibrary.utils.LogUtil;
import com.donews.utilslibrary.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by SnowDragon
 * Date on 2021/4/6
 * Description:
 */
public class NotifyLuncherConfigManager {
    private final List<AppGlobalConfigDataUpdateListener> mAppGlobalConfigDataUpdateListeners = new ArrayList<>();
    private AppGlobalConfigBean mAppGlobalConfigBean;

    private static Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            NotifyLuncherConfigManager.update();
        }
    };


    private NotifyLuncherConfigManager() {
    }


    private static final class Holder {
        private static final NotifyLuncherConfigManager sInstance = new NotifyLuncherConfigManager();
    }

    public static NotifyLuncherConfigManager getInstance() {
        return Holder.sInstance;
    }

    public static void update() {
        EasyHttp.get(BuildConfig.APP_NOTIFY_LUNCHER + JsonUtils.getCommonJson(false))
                .cacheMode(CacheMode.NO_CACHE)
//                .addInterceptor(new AppGlobalInterceptor())
                .isShowToast(BuildConfig.DEBUG)
                .execute(new SimpleCallBack<AppGlobalConfigBean>() {
                    @Override
                    public void onError(ApiException e) {
                        LogUtil.e(e.getCode() + e.getMessage());
                        if (mHandler != null) {
                            if (mHandler.hasMessages(0)) {
                                mHandler.removeMessages(0);
                            }
                            mHandler.sendEmptyMessageDelayed(0, getInstance().getAppGlobalConfigBean().refreshInterval * 1000L);
                        }
                    }

                    @Override
                    public void onSuccess(AppGlobalConfigBean bean) {
                        if (bean != null) {
                            SPUtils.setInformain(KeySharePreferences.APP_GLOBAL_CRASH_CONFIG, GsonUtils.toJson(bean));
                        }

                        if (mHandler != null) {
                            if (mHandler.hasMessages(0)) {
                                mHandler.removeMessages(0);
                            }
                        }

                        int refreshInterval = getInstance().getAppGlobalConfigBean().refreshInterval;
                        if (bean != null) {
                            refreshInterval = bean.refreshInterval;
                            NotifyLuncherConfigManager.getInstance().setAppGlobalConfigBean(bean);
                        }
                        if (mHandler != null) {
                            mHandler.sendEmptyMessageDelayed(0, refreshInterval * 1000L);
                        }
                    }
                });
    }

    public AppGlobalConfigBean getAppGlobalConfigBean(Context context) {
        SPUtils.init(context);
        return getAppGlobalConfigBean();
    }

    public AppGlobalConfigBean getAppGlobalConfigBean() {
        if (mAppGlobalConfigBean != null) {
            return mAppGlobalConfigBean;
        }
        try {
            String configJson = SPUtils.getInformain(KeySharePreferences.APP_GLOBAL_CRASH_CONFIG, null);
            if (configJson != null) {
                mAppGlobalConfigBean = GsonUtils.getLocalGson().fromJson(configJson, AppGlobalConfigBean.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mAppGlobalConfigBean == null) {
            mAppGlobalConfigBean = new AppGlobalConfigBean();
        }
        return mAppGlobalConfigBean;
    }

    public void addAppConfigDataUpdateListener(AppGlobalConfigDataUpdateListener listener) {
        if (listener != null) {
            mAppGlobalConfigDataUpdateListeners.add(listener);
        }
    }

    public void removeAppConfigDataUpdateListener(AppGlobalConfigDataUpdateListener listener) {
        mAppGlobalConfigDataUpdateListeners.remove(listener);
    }

    public interface AppGlobalConfigDataUpdateListener {
        /**
         * 数据不同，通知更新
         *
         * @param update 是否数据更新
         */
        void dataUpdate(boolean update);
    }

    private void setAppGlobalConfigBean(AppGlobalConfigBean configBean) {
        AppGlobalConfigBean curruntGlobalConfigBean = getAppGlobalConfigBean();
        boolean isUpdate = CompareObjectUtils.contrastObj(curruntGlobalConfigBean, configBean, AppGlobalConfigBean.class);
        this.mAppGlobalConfigBean = configBean;
        for (AppGlobalConfigDataUpdateListener listener : mAppGlobalConfigDataUpdateListeners) {
            if (listener != null) {
                try {
                    listener.dataUpdate(!isUpdate);
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        }
    }
}
