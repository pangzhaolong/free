package com.donews.notify.launcher;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.donews.base.base.AppManager;
import com.donews.base.base.BaseApplication;
import com.donews.base.storage.MmkvHelper;
import com.donews.base.utils.ToastUtil;
import com.donews.common.BuildConfig;
import com.donews.common.NotifyLuncherConfigManager;
import com.donews.notify.launcher.utils.NotifyLog;
import com.donews.utilslibrary.utils.AppStatusUtils;
import com.donews.utilslibrary.utils.KeySharePreferences;

import java.text.SimpleDateFormat;
import java.util.Date;


public class NotifyScreenDelegate {
    private static final String KEY_NOTIFYOPEN_TIME = KeySharePreferences.KEY_NOTIFYOPEN_TIME;
    private static final String KEY_NOTIFYCOUNT_LIMIT = KeySharePreferences.KEY_NOTIFYCOUNT_LIMIT;
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final long HOUR = 60 * 60 * 1000;
    private static final long DAY = HOUR * 24;
    private final Handler mHandler = new Handler(Looper.getMainLooper());

    private boolean isLoaded;
    private boolean isOpen;

    private static Runnable mShowNotifyRunnable = null;
    //是否已经锁屏了
    public static boolean isLockScreen = false;

    /**
     * 解决多次解锁时，间隙，cd时间默认为10秒内不出
     */
    private static long mIntervalLockShowTime = 10 * 1000;
    //最后一次显示的时间
    private static long mLastShowTime = 0;

    /**
     * 网络变换
     */
    private int mCurruntCount = 0;

    private Runnable getShowNotifyRunnable(Context context) {
        if (mShowNotifyRunnable == null) {
            mShowNotifyRunnable = () -> {
                Log.i(NotifyInitProvider.TAG, "NotifyScreenDelegate excute Runnable");
                NotifyActionActivity.destroy();
                if (canOpen(context) && isLoaded) {
                    NotifyActivity.actionStart(context);
                    isOpen = true;
                }
            };
        }
        return mShowNotifyRunnable;
    }

    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.i(NotifyInitProvider.TAG, action);
        switch (action) {
            case Intent.ACTION_SCREEN_ON:
                isLockScreen = false;
                /* 不做业务逻辑处理，只是为了辅助后续的解锁页面弹出,提升解锁弹出的成功率 */
                if (canShowNotify()) {
                    NotifyActionActivity.actionStart(context);
                } else {
                    Log.w(NotifyInitProvider.TAG, action + " can't show");
                }
                break;
            case Intent.ACTION_USER_PRESENT:
                isLockScreen = false;
                NotifyLog.log("解锁成功。开始检查显示弹否");
                NotifyActionActivity.destroy();
                if (canShowNotify() && canShowAct() && canShowFastClick()) {
                    if (BuildConfig.DEBUG) {
                        mHandler.post(() -> {
                            ToastUtil.showShort(BaseApplication.getInstance(), "(解锁)条件通过。开始显示通知");
                        });
                    }
                    tryLoadNewImg(context);
                    long delayTime = NotifyLuncherConfigManager.getInstance().getAppGlobalConfigBean().notifyDelayShowTime;
                    mLastShowTime = System.currentTimeMillis();
                    Log.w(NotifyInitProvider.TAG, action + " show , delayTime=" + delayTime);
                    NotifyLog.log("检查通知通过。发起延迟通知");
                    mHandler.postDelayed(getShowNotifyRunnable(context), delayTime);
                } else {
                    if (BuildConfig.DEBUG) {
                        mHandler.post(() -> {
                            ToastUtil.show(BaseApplication.getInstance(), "(解锁)条件检查未通过a=" + canShowNotify() + ",b=" + canShowAct() + ",c=" + canShowFastClick());
                        });
                    }
                    Log.w(NotifyInitProvider.TAG, action + " can't show");
                }
                break;

            case Intent.ACTION_SCREEN_OFF:
                isLockScreen = true;
                //  锁屏时注意关闭解锁和亮屏弹出来的透明页面，只是为了体验好些。
                NotifyActionActivity.destroy();
                NotifyActivity.destroy();
                break;
            case ConnectivityManager.CONNECTIVITY_ACTION://网络状态发生变化
                if (mCurruntCount > 2) {
                    NotifyActionActivity.destroy();
                    if (canShowNotify() && canShowAct()) {
                        tryLoadNewImg(context);
                        long delayTime = NotifyLuncherConfigManager.getInstance().getAppGlobalConfigBean().notifyDelayShowTime;
                        Log.w(NotifyInitProvider.TAG, action + " show , delayTime=" + delayTime);
                        mHandler.postDelayed(getShowNotifyRunnable(context), delayTime);
                    } else {
                        Log.w(NotifyInitProvider.TAG, action + " can't show");
                    }
                }
                Log.i(NotifyInitProvider.TAG, action + ",currunt num = " + mCurruntCount);
                mCurruntCount++;
                break;
        }
    }

    /**
     * 显示通知
     *
     * @param context 上下文
     */
    public void showNotify(Context context) {
        NotifyLog.log("(后台)开始显示桌面通知");
        NotifyActionActivity.destroy();
        if (canShowNotify() && canShowAct() && canShowFastClick()) {
            if (BuildConfig.DEBUG) {
                mHandler.post(() -> {
                    ToastUtil.showShort(BaseApplication.getInstance(), "条件检查通过。后台计时通知");
                });
            }
            NotifyLog.logNotToast("(后台)条件通过");
            long delayTime = NotifyLuncherConfigManager.getInstance().getAppGlobalConfigBean().notifyDelayShowTime;
            mLastShowTime = System.currentTimeMillis();
            Log.w(NotifyInitProvider.TAG, " show , delayTime=" + delayTime);
            isLoaded = true;
            NotifyLog.logNotToast("(后台)已发送延迟显示");
            mHandler.postDelayed(getShowNotifyRunnable(context), delayTime);
            //加载图片
            tryLoadNewImg2(context);
//            if (url == null || url.isEmpty()) {
//                long delayTime = NotifyLuncherConfigManager.getInstance().getAppGlobalConfigBean().notifyDelayShowTime;
//                mLastShowTime = System.currentTimeMillis();
//                Log.w(NotifyInitProvider.TAG, " show , delayTime=" + delayTime);
//                isLoaded = true;
//                Log.e("notifyDes","(后台)已发送延迟显示");
//                mHandler.postDelayed(getShowNotifyRunnable(context), delayTime);
//            } else {
//                //加载网络图片。然后显示
//                Log.e("notifyDes","(后台)加载图片显示");
//                tryLoadNewImg2(context);
//            }
        } else {
            NotifyLog.log("(后台)条件检查未通过a=" + canShowNotify() + ",b=" + canShowAct() + ",c=" + canShowFastClick());
        }
    }

    //网络图片加载
    private void tryLoadNewImg2(Context context) {
        String url = NotifyLuncherConfigManager.getInstance().getAppGlobalConfigBean().notifyLauncherImg;
        Glide.with(context).asBitmap().load(url).into(new CustomTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                Log.d(NotifyInitProvider.TAG, "tryLoadNewImg success , url = " + url);
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {
                Log.d(NotifyInitProvider.TAG, "tryLoadNewImg onLoadCleared , url = " + url);
            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                super.onLoadFailed(errorDrawable);
                Log.d(NotifyInitProvider.TAG, "tryLoadNewImg onLoadFailed , url = " + url);
            }
        });

    }

    private void tryLoadNewImg(Context context) {
        isLoaded = false;
        isOpen = false;
        String url = NotifyLuncherConfigManager.getInstance().getAppGlobalConfigBean().notifyLauncherImg;
        Glide.with(context).asBitmap().load(url).into(new CustomTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                isLoaded = true;
                if (!isOpen) {
                    getShowNotifyRunnable(context);
                }
                Log.d(NotifyInitProvider.TAG, "tryLoadNewImg success , url = " + url);
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {
                isLoaded = true;
                Log.d(NotifyInitProvider.TAG, "tryLoadNewImg onLoadCleared , url = " + url);
            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {

                super.onLoadFailed(errorDrawable);
                isLoaded = true;
                Log.d(NotifyInitProvider.TAG, "tryLoadNewImg onLoadFailed , url = " + url);
            }
        });

    }

    private boolean canOpen(Context context) {
        boolean alwaysShow = NotifyLuncherConfigManager.getInstance().getAppGlobalConfigBean().notifyAlwaysShow;
        boolean showCount = showCountLimit(context);
        if (alwaysShow && showCount) {
            return true;
        } else {
            if (!MmkvHelper.isInit()) {
                MmkvHelper.init(context);
            }
            // 初次没写入则有可能为0
            long lastOpenTime = MmkvHelper.getInstance().getMmkv().decodeLong(KEY_NOTIFYOPEN_TIME, 1);
            long todayZero = getTodayZeroTime();
            boolean result = (todayZero > lastOpenTime) && lastOpenTime > 0;
            Log.w(NotifyInitProvider.TAG, "lastOpenTime result = " + result);
            result &= showCount;

            Log.w(NotifyInitProvider.TAG, "final result = " + result);
            return result;
        }
    }

    private boolean showCountLimit(Context context) {
        long notifyShowMaxCount = NotifyLuncherConfigManager.getInstance().getAppGlobalConfigBean().notifyShowMaxCount;
        long curruntNotifyCount = getTodayShowCount(context);
        Log.w(NotifyInitProvider.TAG, "curruntNotifyCount = " + curruntNotifyCount + "，notifyShowMaxCount = " + notifyShowMaxCount);
        return curruntNotifyCount < notifyShowMaxCount;
    }

    /**
     * 时间对的上
     */
    private boolean canShowNotify() {
        //时间，比如12代表12点
        int time1 = NotifyLuncherConfigManager.getInstance().getAppGlobalConfigBean().notifyTimeStart1;
        int time2 = NotifyLuncherConfigManager.getInstance().getAppGlobalConfigBean().notifyTimeStart2;
        Log.w(NotifyInitProvider.TAG, "canShowNotify time1 = " + time1);
        Log.w(NotifyInitProvider.TAG, "canShowNotify time2 = " + time2);

        //增加新用户延迟判断isOutDelayTime4NewUser
        boolean result = isOutDelayTime4NewUser() && (isRangeTime(time1) || isRangeTime(time2));

        long now = System.currentTimeMillis();
        boolean canShow = (now - mLastShowTime > mIntervalLockShowTime);

        Log.w(NotifyInitProvider.TAG, "result = " + result + ",canShow = " + canShow);
        result = result && canShow;
        return result;
    }

    private boolean canShowAct() {
        Log.w(NotifyInitProvider.TAG, "App  Activity Stack Size: " + AppManager.getInstance().getActivitySize());
        return AppManager.getInstance().getActivitySize() <= 0 ||
                !AppUtils.isAppForeground();
    }

    //检查是否距离上一次点击进入应用达到了足够值,T:已达到间隔时间，F:还未达到间隔时间
    private boolean canShowFastClick() {
        int po = NotifyLuncherConfigManager.getInstance().getAppGlobalConfigBean().notifyClickLastOpenInterval * 1000;
        long lastClickTime = SPUtils.getInstance().getLong("notifyClickLastOpenInterval", 0);
        return System.currentTimeMillis() - lastClickTime > po;
    }

    private boolean isRangeTime(int time) {
        //范围
        int startTime1 = time - 2;
        int endTime1 = time + 2;
        if (startTime1 <= 0) {
            startTime1 = 0;
        }
        long todayZero = getTodayZeroTime();
        long todayStartTime1 = todayZero + HOUR * startTime1;
        long todayEndTime1 = todayZero + HOUR * endTime1;
        long now = System.currentTimeMillis();

        return now >= todayStartTime1 && now <= todayEndTime1;
    }

    //是否超过新用户延迟时间
    private boolean isOutDelayTime4NewUser() {
        long delayTime = NotifyLuncherConfigManager.getInstance().getAppGlobalConfigBean().notifyDelay4NewUser * 1000L;
        if (System.currentTimeMillis() - AppStatusUtils.getAppInstallTime() > delayTime) {
            Log.w(NotifyInitProvider.TAG, "out delay time 4 new user: true");
            return true;
        }

        Log.w(NotifyInitProvider.TAG, "out delay time 4 new user: false");
        return false;
    }

    public long getTodayZeroTime() {
        long currentTimestamps = System.currentTimeMillis();
        String today = SIMPLE_DATE_FORMAT.format(new Date());
        try {
            return SIMPLE_DATE_FORMAT.parse(today).getTime();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        //加上8小时的毫秒数是因为毫秒时间戳是从北京时间1970年01月01日08时00分00秒开始算的
        return currentTimestamps - (currentTimestamps + HOUR * 8) % DAY;
    }

    public static void putTodayShowCount(Context context) {
        if (!MmkvHelper.isInit()) {
            MmkvHelper.init(context);
        }
        String today = SIMPLE_DATE_FORMAT.format(new Date());

        long curruntNotifyCount = MmkvHelper.getInstance().getMmkv().decodeLong(today + "_" + KEY_NOTIFYCOUNT_LIMIT, 0);
        curruntNotifyCount += 1;

        Log.w(NotifyInitProvider.TAG, "putTodayShowCount =" + curruntNotifyCount);
        MmkvHelper.getInstance().getMmkv().encode(today + "_" + KEY_NOTIFYCOUNT_LIMIT, curruntNotifyCount);
    }

    public static long getTodayShowCount(Context context) {
        if (!MmkvHelper.isInit()) {
            MmkvHelper.init(context);
        }
        String today = SIMPLE_DATE_FORMAT.format(new Date());

        return MmkvHelper.getInstance().getMmkv().decodeLong(today + "_" + KEY_NOTIFYCOUNT_LIMIT, 0);
    }


}
