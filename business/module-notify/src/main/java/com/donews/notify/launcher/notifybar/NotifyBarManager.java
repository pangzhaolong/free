package com.donews.notify.launcher.notifybar;

import static com.donews.utilslibrary.utils.KeySharePreferences.TIME_SERVICE;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.SPUtils;
import com.donews.base.base.BaseApplication;
import com.donews.common.BuildConfig;
import com.donews.network.EasyHttp;
import com.donews.network.cache.model.CacheMode;
import com.donews.network.callback.SimpleCallBack;
import com.donews.network.exception.ApiException;
import com.donews.notify.launcher.configs.Notify2ConfigManager;
import com.donews.notify.launcher.configs.baens.Notify2DataConfigBean;
import com.donews.notify.launcher.configs.baens.NotifyBarDataConfig;
import com.donews.notify.launcher.utils.NotifyLog;
import com.donews.utilslibrary.utils.JsonUtils;
import com.donews.utilslibrary.utils.LogUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * @author lcl
 * Date on 2022/3/8
 * Description:
 * 通知栏管理类
 */
public class NotifyBarManager {
    //更新配置通知栏模块的配置数据
    private static final int UPDATE_NOTIFY_BAR_CONFIG_MSG = 21003;
    //通知栏数据缓存的key
    private static String notifyBarCacheKey = "notify_bar_data_cache";
    //------------------------本地通知栏相关的key------------------------------
    //本地通知栏相关配置保存的配置文件
    private static String notifyAllowDayCountFile = "notify_bar_allowDayCount_file";
    //本地记录着通知当天打开的通知栏次数的配置(当日已经弹出通知栏的总次数)
    private static String notifyBarAllowDayCountKey = "notifyBarAllowDayCountKey_";
    //获取天数，当前生效的是哪一天的配置(因为隔天需要清除)
    private static String notifyBarAllowDayKey = "notifyBarAllowDayKey";
    //获取上一次显示的时间(上一次的显示时间)
    private static String notifyBarLastShowTimeKey = "notifyBarLastShowTimeKey";
    //记录上一次显示的样式id
    private static String notifyBarLastShowKey = "notifyBarLastShowIdKey";

    private static final class Holder {
        private static final NotifyBarManager s_notifyBarConfigMgr = new NotifyBarManager();
    }

    /**
     * 时间更新的广播
     */
    private static BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(intent.ACTION_TIME_TICK)) {
                //时间更新了。需要开始检查是否弹出通知了
                NotifyLog.logBar("时间更新了。开始进行通知栏逻辑判断..");
                NotifyBarShowManager.sendNotifyBar();
            }
        }
    };

    private static Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == UPDATE_NOTIFY_BAR_CONFIG_MSG) {
                updateNotifyBarConfig();
            }
        }
    };
    //是否初始化过的标记
    private static boolean isInit = false;
    //通知栏配置相关的数据对象
    private NotifyBarDataConfig mNotifyBarBean;

    private NotifyBarManager() {
    }

    /**
     * 初始化
     */
    public synchronized static void init() {
        if (isInit) {
            return;
        }
        isInit = true;
        //注册时间的广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        BaseApplication.getInstance().registerReceiver(broadcastReceiver, filter);
        //更新通知栏通知数据
        updateNotifyBarConfig();
        NotifyLog.logBar("模块已经初始化完成了~~~~");
    }

    /**
     * 单利获取当前管理对象
     *
     * @return
     */
    public static NotifyBarManager Ins() {
        return NotifyBarManager.Holder.s_notifyBarConfigMgr;
    }

    /**
     * 获取通知栏相关配置数据
     *
     * @return
     */
    public NotifyBarDataConfig getNotifyBarConfigBean() {
        if (mNotifyBarBean == null) {
            String cache = SPUtils.getInstance(this.getClass().getSimpleName()).getString(notifyBarCacheKey);
            if (cache == null || cache.equals("")) {
                mNotifyBarBean = new NotifyBarDataConfig();
            } else {
                try {
                    mNotifyBarBean = GsonUtils.fromJson(cache, NotifyBarDataConfig.class);
                } catch (Exception e) {
                    mNotifyBarBean = new NotifyBarDataConfig();
                    e.printStackTrace();
                }
            }
        }
        return mNotifyBarBean;
    }

    /**
     * 获取上一次显示的样式ID
     *
     * @return
     */
    public String getLastShowId() {
        return com.blankj.utilcode.util.SPUtils.getInstance(notifyAllowDayCountFile)
                .getString(notifyBarLastShowKey, "");
    }

    /**
     * 保存指定id为上一次显示的样式id
     *
     * @param id
     */
    public void setLastShowId(String id) {
        com.blankj.utilcode.util.SPUtils.getInstance(notifyAllowDayCountFile)
                .put(notifyBarLastShowKey, id);
        setLastShowTime();
    }

    /**
     * 更新上一次显示的时间
     */
    public void setLastShowTime() {
        //更新上一次的显示时间
        com.blankj.utilcode.util.SPUtils.getInstance(notifyAllowDayCountFile)
                .put(notifyBarLastShowTimeKey, System.currentTimeMillis());
    }

    /**
     * 获取上一次显示的时间
     */
    public long getLastShowTime() {
        //更新上一次的显示时间
        return com.blankj.utilcode.util.SPUtils.getInstance(notifyAllowDayCountFile)
                .getLong(notifyBarLastShowTimeKey, 0L);
    }

    /**
     * 获取当前天显示的总次数
     *
     * @param typeId    需要查询的分类
     * @param isAutoAdd 是否自动 +1
     * @return
     */
    public int getCurrentDayShowCount(int typeId, boolean isAutoAdd) {
        //当前的时间
        long currentTime = com.donews.utilslibrary.utils.SPUtils.getLongInformain(TIME_SERVICE, 0L) * 1000;
        //获取当前生效的日期(格式:yyyyMMdd)
        String localAllowDay = com.blankj.utilcode.util.SPUtils.getInstance(notifyAllowDayCountFile)
                .getString(notifyBarAllowDayKey);
        //检查是否超过了一天。超过需要清除本地的存储
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
        String currentTimeDay = sf.format(new Date(currentTime));
        int resultValue = com.blankj.utilcode.util.SPUtils.getInstance(notifyAllowDayCountFile)
                .getInt(notifyBarAllowDayCountKey + typeId, 0);
        if (localAllowDay != null && localAllowDay.length() > 0) {
            try {
                if (!currentTimeDay.equals(sf.format(sf.parse(localAllowDay)))) {
                    //已经超过一天了。更新配置
                    com.blankj.utilcode.util.SPUtils.getInstance(notifyAllowDayCountFile)
                            .put(notifyBarAllowDayKey, currentTimeDay);
                    com.blankj.utilcode.util.SPUtils.getInstance(notifyAllowDayCountFile)
                            .put(notifyBarAllowDayCountKey + typeId, 0);
                    com.blankj.utilcode.util.SPUtils.getInstance(notifyAllowDayCountFile)
                            .put(notifyBarLastShowTimeKey, 0L);
                    resultValue = 0;
                    localAllowDay = currentTimeDay;
                } else {
                    //没有超过一天。那么直接读取本地的配置信息
                    resultValue = com.blankj.utilcode.util.SPUtils.getInstance(notifyAllowDayCountFile)
                            .getInt(notifyBarAllowDayCountKey + typeId, 0);
                }
            } catch (ParseException e) {
                com.blankj.utilcode.util.SPUtils.getInstance(notifyAllowDayCountFile)
                        .put(notifyBarAllowDayKey, currentTimeDay);
                localAllowDay = currentTimeDay;
            }
        } else {
            com.blankj.utilcode.util.SPUtils.getInstance(notifyAllowDayCountFile)
                    .put(notifyBarAllowDayKey, currentTimeDay);
            localAllowDay = currentTimeDay;
        }
        if (isAutoAdd) {
            //更新当前的总数
            com.blankj.utilcode.util.SPUtils.getInstance(notifyAllowDayCountFile)
                    .put(notifyBarAllowDayCountKey + typeId, resultValue + 1);
        }
        return resultValue;
    }

    /**
     * 设置或者更新通知栏通知配置数据
     *
     * @param bean
     */
    private void setNotifyBarConfigBean(NotifyBarDataConfig bean) {
        try {
            SPUtils.getInstance(this.getClass().getSimpleName()).put(notifyBarCacheKey, GsonUtils.toJson(bean));
        } catch (Exception e) {
            SPUtils.getInstance(this.getClass().getSimpleName()).put(notifyBarCacheKey, "");
            e.printStackTrace();
        }
        mNotifyBarBean = bean;
        NotifyLog.logBar("配置数据已更新:" + mNotifyBarBean);
    }

    //更新通知栏通知配置数据
    private static void updateNotifyBarConfig() {
        NotifyLog.logBar("开始加载或者更新配置数据~~~");
        EasyHttp.get(BuildConfig.BASE_CONFIG_URL  + com.donews.common.BuildConfig.APP_IDENTIFICATION+"-"+ "notify-bar-datas" + BuildConfig.BASE_RULE_URL +
                JsonUtils.getCommonJson(false))
                .cacheMode(CacheMode.NO_CACHE)
                .isShowToast(BuildConfig.DEBUG)
                .execute(new SimpleCallBack<NotifyBarDataConfig>() {
                    @Override
                    public void onError(ApiException e) {
                        NotifyLog.logBar("配置数据获取异常：" + e.getCode() + "," + e.getMessage());
                        if (mHandler != null) {
                            if (mHandler.hasMessages(UPDATE_NOTIFY_BAR_CONFIG_MSG)) {
                                mHandler.removeMessages(UPDATE_NOTIFY_BAR_CONFIG_MSG);
                            }
                            mHandler.sendEmptyMessageDelayed(UPDATE_NOTIFY_BAR_CONFIG_MSG, 15 * 1000);
                        }
                    }

                    @Override
                    public void onSuccess(NotifyBarDataConfig bean) {
                        LogUtil.i("NotifyBar Manager data");

                        if (mHandler != null) {
                            if (mHandler.hasMessages(UPDATE_NOTIFY_BAR_CONFIG_MSG)) {
                                mHandler.removeMessages(UPDATE_NOTIFY_BAR_CONFIG_MSG);
                            }
                        }

                        NotifyBarManager.Ins().setNotifyBarConfigBean(bean);
                        if (mHandler != null) {
                            long stepTime = NotifyBarManager.Ins().getNotifyBarConfigBean().refreshInterval * 1000L;
                            if (stepTime <= 10 * 1000) {
                                stepTime = 10 * 1000;
                            }
                            mHandler.sendEmptyMessageDelayed(UPDATE_NOTIFY_BAR_CONFIG_MSG, stepTime);
                        }
                    }
                });
    }

}
