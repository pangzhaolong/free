package com.donews.notify.launcher.configs;

import static com.donews.utilslibrary.utils.KeySharePreferences.TIME_SERVICE;

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
import com.donews.notify.launcher.configs.baens.CurrentServiceTime;
import com.donews.notify.launcher.configs.baens.Notify2DataConfigBean;
import com.donews.notify.launcher.configs.baens.NotifyBarDataConfig;
import com.donews.notify.launcher.notifybar.NotifyBarManager;
import com.donews.notify.launcher.utils.AppNotifyForegroundUtils;
import com.donews.utilslibrary.utils.JsonUtils;
import com.donews.utilslibrary.utils.LogUtil;

import cn.cd.dn.sdk.models.utils.DNServiceTimeManager;

public class Notify2ConfigManager {
    //更新配置桌面通知模块的配置数据
    private static final int UPDATE_CONFIG_MSG = 21002;
    private static boolean isInit = false;
    private static String cacheKey = "notify_data_cache";
    //桌面通知相关配置数据对象
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
     * 获取桌面通知配置数据
     *
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
     * 设置或者更新桌面通知模块数据
     *
     * @param bean
     */
    private void setNotifyConfigBean(Notify2DataConfigBean bean) {
        try {
            if (bean.notifyConfigs != null) {
                for (Notify2DataConfigBean.NotifyItemConfig notifyConfig : bean.notifyConfigs) {
                    if (notifyConfig.uiTemplate != null) {
                        for (Notify2DataConfigBean.UiTemplat uiTemplat : notifyConfig.uiTemplate) {
                            //解析标签数据
                            uiTemplat.notifyTypeId = notifyConfig.id;
                            uiTemplat.buildFixTag();
                        }
                    }
                }
            }
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
        //更新桌面通知配置数据
        update();
        //注册后台通知监听
        AppNotifyForegroundUtils.startForegroundCheck();
        //初始化通知栏配置数据
        NotifyBarManager.init();
    }

    //获取服务器时间
    private static void getServiceTime() {
        long time = DNServiceTimeManager.Companion.getIns().getServiceTime();
        if (time <= 0) {
            long curTime = System.currentTimeMillis() / 1000;
            long saveTime = com.donews.utilslibrary.utils.SPUtils.getLongInformain(TIME_SERVICE, 0L);
            if (curTime < saveTime) {
                return; //放弃保存。因为时间不可能往前走。
            }
            com.donews.utilslibrary.utils.SPUtils.setInformain(TIME_SERVICE, curTime);
        } else {
            long curTime = time / 1000;
            long saveTime = com.donews.utilslibrary.utils.SPUtils.getLongInformain(TIME_SERVICE, 0L);
            if (curTime < saveTime) {
                return; //放弃保存。因为时间不可能往前走。
            }
            com.donews.utilslibrary.utils.SPUtils.setInformain(TIME_SERVICE, curTime);
        }
//        EasyHttp.get("https://lottery.xg.tagtic.cn/lottery/v1/get-now-time")
//                .cacheMode(CacheMode.NO_CACHE)
//                .execute(new SimpleCallBack<CurrentServiceTime>() {
//                    @Override
//                    public void onError(ApiException e) {
//                        long curTime = System.currentTimeMillis();
//                        long saveTime = com.donews.utilslibrary.utils.SPUtils.getLongInformain(TIME_SERVICE, 0L);
//                        if (curTime < saveTime) {
//                            return; //放弃保存。因为时间不可能往前走。
//                        }
//                        com.donews.utilslibrary.utils.SPUtils.setInformain(TIME_SERVICE, curTime);
//                    }
//
//                    @Override
//                    public void onSuccess(CurrentServiceTime queryBean) {
//                        long curTime = System.currentTimeMillis();
//                        long saveTime = com.donews.utilslibrary.utils.SPUtils.getLongInformain(TIME_SERVICE, 0L);
//                        if (queryBean == null || queryBean.now == null || queryBean.now.isEmpty()) {
//                            if (curTime < saveTime) {
//                                return; //放弃保存。因为时间不可能往前走。
//                            }
//                            com.donews.utilslibrary.utils.SPUtils.setInformain(TIME_SERVICE, curTime);
//                        } else {
//                            com.donews.utilslibrary.utils.SPUtils.setInformain(TIME_SERVICE,
//                                    Long.parseLong(queryBean.now));
//                        }
//                    }
//                });
    }

    private static void update() {
        getServiceTime();
        LogUtil.i("Notify2ConfigManager update");
        EasyHttp.get(BuildConfig.BASE_CONFIG_URL + com.donews.common.BuildConfig.APP_IDENTIFICATION + "-" + "notify-datas" + BuildConfig.BASE_RULE_URL +
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
