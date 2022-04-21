package com.donews.notify.launcher.notifybar;

import android.app.Notification;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.blankj.utilcode.util.AppUtils;
import com.chat_hook.HookMethodCall;
import com.chat_hook.HookMethodCallParams;
import com.chat_hook.HookMethodHelper;
import com.chat_hook.HookMethodParams;
import com.donews.base.base.BaseApplication;
import com.donews.notify.launcher.configs.baens.NotifyBarDataConfig;
import com.donews.notify.launcher.notifybar.listeners.OnNotificationBuiderCreateListener;
import com.donews.notify.launcher.notifybar.uistyle.IUIStyleShow;
import com.donews.notify.launcher.notifybar.uistyle.UIStyle0ShowImpl;
import com.donews.notify.launcher.notifybar.uistyle.UIStyle1ShowImpl;
import com.donews.notify.launcher.notifybar.utils.CollectionInvokUtil;
import com.donews.notify.launcher.utils.NotifyLog;
import com.donews.utilslibrary.analysis.AnalysisUtils;
import com.donews.utilslibrary.dot.Dot;
import com.ycbjie.notificationlib.NotificationUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author lcl
 * Date on 2022/3/9
 * Description:
 * 通知栏显示控制管理类
 */
public class NotifyBarShowManager {

    //通知的渠道ID
    private static String channelId = null;
    private static boolean isAddBuilderHook = false;

    /**
     * 各种UI显示的处理对象
     */
    private static Map<Integer, IUIStyleShow> uiStyleInvoks = new HashMap<Integer, IUIStyleShow>() {
        {
            put(0, new UIStyle0ShowImpl());
            put(1, new UIStyle1ShowImpl());
        }
    };
    //当前显示生效的UI样式
    private static IUIStyleShow currentInvokSytle = null;

    /**
     * 发送通知栏通知
     */
    public synchronized static void sendNotifyBar() {
        addBuilderHook();
        NotifyBarDataConfig.NotifyBarUIDataConfig item = getShowNotifyBarData();
        if (item == null) {
            NotifyLog.logBar("没有通知内容显示。终止操作~~");
            return;
        }
        IUIStyleShow uiInvok = uiStyleInvoks.get(item.uiType);
        if (uiInvok == null) {
            currentInvokSytle = null;
            NotifyLog.logBar("UI类型[" + item.uiType + "],暂时无此类型的处理方式。终止操作~~");
            return;
        }
        currentInvokSytle = uiInvok;
        AnalysisUtils.onEventEx(BaseApplication.getInstance(),
                Dot.Notify_Bar_Show, "样式->" + item.uiType);
        uiInvok.showNotify(BaseApplication.getInstance(), item);
    }

    /**
     * 获取通知的渠道ID
     *
     * @return
     */
    public static String getChannelId() {
        if (channelId == null) {
            channelId = BaseApplication.getInstance().getPackageName();
        }
        return channelId + "_desktop";
    }

    /**
     * 获取通知的渠道名称
     *
     * @return
     */
    public static String getChannelName() {
        return "专属优惠活动";
    }

    /**
     * 获取指定数据的NotifyId，获取通知栏的通知ID
     *
     * @param uiType 通知栏样式
     * @return
     */
    public static int getNotifyId(int uiType) {
        NotifyBarDataConfig configy = NotifyBarManager.Ins().getNotifyBarConfigBean();
        if (configy.notifyShowModel == 0) {
            return (int) System.currentTimeMillis();
        } else {
            return uiType;
        }
    }

    /**
     * 添加通知的 Builder 的拦截
     */
    private static void addBuilderHook() {
        if (isAddBuilderHook) {
            return;
        }
        isAddBuilderHook = true;
        Class<?> hookClass = NotificationUtils.class;
        try {
            HookMethodHelper.INSTANCE.addHookMethod(new HookMethodParams(
                    hookClass, "getNotificationCompat", new Object[]{String.class, String.class, Integer.TYPE}, new HookMethodCall() {
                @Override
                public void beforeHookedMethod(@Nullable HookMethodCallParams hookMethodCallParams) {
                }

                @Override
                public void afterHookedMethod(@Nullable HookMethodCallParams hookMethodCallParams) {
                    try {
                        if (currentInvokSytle != null &&
                                hookMethodCallParams.getResult() instanceof NotificationCompat.Builder) {
                            currentInvokSytle.buildCompatBuider(hookMethodCallParams.getThisObject().toString(),
                                    (NotificationCompat.Builder) hookMethodCallParams.getResult());
                            NotifyLog.logBar("已经对Builder参数的修改回调精选了调用(getNotificationCompat)");
                        }
                    } catch (Exception e) {
                        NotifyLog.logBar("处理对Builder参数设置出现异常：" + e);
                    } catch (Error err) {
                        NotifyLog.logBar("处理对Builder参数设置出现错误：" + err);
                    }
                }
            }));
            HookMethodHelper.INSTANCE.addHookMethod(new HookMethodParams(
                    hookClass, "getNotificationV4", new Object[]{String.class, String.class, Integer.TYPE}, new HookMethodCall() {
                @Override
                public void beforeHookedMethod(@Nullable HookMethodCallParams hookMethodCallParams) {
                }

                @Override
                public void afterHookedMethod(@Nullable HookMethodCallParams hookMethodCallParams) {
                    try {
                        if (currentInvokSytle != null &&
                                hookMethodCallParams.getResult() instanceof Notification.Builder) {
                            currentInvokSytle.buildBuider(hookMethodCallParams.getThisObject().toString(),
                                    (Notification.Builder) hookMethodCallParams.getResult());
                            NotifyLog.logBar("已经对Builder参数的修改回调精选了调用(getNotificationV4)");
                        }
                    } catch (Exception e) {
                        NotifyLog.logBar("处理对Builder参数设置出现异常：" + e);
                    } catch (Error err) {
                        NotifyLog.logBar("处理对Builder参数设置出现错误：" + err);
                    }
                }
            }));
        } catch (Exception e) {
            NotifyLog.logBar("添加对Builder参数设置出现异常：" + e);
        } catch (Error err) {
            NotifyLog.logBar("添加对Builder参数设置出现错误：" + err);
        }
    }

    /**
     * 获取需要显示的通知栏数据
     *
     * @return null 没有数据
     */
    private static NotifyBarDataConfig.NotifyBarUIDataConfig getShowNotifyBarData() {
        try {
            return CollectionInvokUtil.getFinalShowStyle(CollectionInvokUtil.getShowUIStyles());
        } catch (Exception e) {
            NotifyLog.logBar("获取UI样式失败。请检查中台配置~~：e=" + e);
            e.printStackTrace();
            return null;
        }
    }

}
