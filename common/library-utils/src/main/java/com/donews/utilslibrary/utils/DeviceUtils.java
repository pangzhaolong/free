package com.donews.utilslibrary.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.dnstatistics.sdk.agent.DonewsAgent;
import com.donews.utilslibrary.BuildConfig;
import com.donews.utilslibrary.base.UtilsConfig;
import com.ishumei.smantifraud.SmAntiFraud;
import com.meituan.android.walle.ChannelInfo;
import com.meituan.android.walle.WalleChannelReader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.UUID;

import static android.content.Context.BATTERY_SERVICE;

/**
 * @Author: honeylife
 * @CreateDate: 2020/10/28 15:24
 * @Description: 获取设备的数据
 */
public class DeviceUtils {
    @Deprecated
    public static int dip2px(Context context, float pxValue) {
        return dip2px(pxValue);
    }

    public static int dip2px(float pxValue) {
        return Math.round(pxValue * UtilsConfig.getApplication().getResources().getDisplayMetrics()
                .density);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * android_id 获取
     */
    public static String getAndroidID() {
        try {
            String ANDROID_ID = Settings.System.getString(UtilsConfig.getApplication().getContentResolver(),
                    Settings.System.ANDROID_ID);
            return ANDROID_ID;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 真实的DeviceId
     */
    @SuppressLint("HardwareIds")
    public static String getDeviceId() {
        try {
            TelephonyManager tm = (TelephonyManager) UtilsConfig.getApplication().getSystemService(
                    Context.TELEPHONY_SERVICE);
            String deviceId = "";
            if (tm == null)
                return "";
            deviceId = tm.getDeviceId();
            return deviceId;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    /**
     * 获取版本名
     *
     * @return 当前应用的版本名称
     */
    public static String getVersionName() {
        try {
            PackageManager manager = UtilsConfig.getApplication().getPackageManager();
            PackageInfo info = manager.getPackageInfo(UtilsConfig.getApplication().getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            return "2.0";
        }
    }

    /**
     * 获取版本号。
     *
     * @return 应用版本号
     */
    public static int getAppVersionCode() {
        try {
            PackageManager manager = UtilsConfig.getApplication().getPackageManager();
            PackageInfo info = manager.getPackageInfo(UtilsConfig.getApplication().getPackageName(), 0);
            if (null != info) {
                return info.versionCode;
            }
        } catch (Exception e) {
            return -1;
        }
        return -1;
    }

    /**
     * 得到包名
     */
    public static String getPackage() {
        try {
            PackageManager manager = UtilsConfig.getApplication().getPackageManager();
            PackageInfo info = manager.getPackageInfo(UtilsConfig.getApplication().getPackageName(), 0);
            String packagename = info.packageName;
            LogUtil.i(packagename);
            return packagename;
        } catch (Exception e) {
            return "2.0";
        }
    }

    public static String getMacAddress() {
        String macAddress = "";
        Context context = null;
        try {
            Application application = UtilsConfig.getApplication();
            if (application == null) {
                return macAddress;
            }
            context = application.getApplicationContext();
            if (context == null) {
                return macAddress;
            }
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            if (wifiManager == null) {
                return macAddress;
            }
            WifiInfo info = wifiManager.getConnectionInfo();
            if (info == null) {
                return macAddress;
            }
            macAddress = info.getMacAddress();
            return macAddress;
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return "";
    }

    public static boolean shouldHideStatusBar() {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT;
    }

    public static boolean hideStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            return true;
        } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            return true;
        } else {
            return false;
        }
    }

    public static int getStatusBarHeight(Context context) {

        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, sbar = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
        }
        return sbar;
    }

    // 获得数美的deviceID
    public static String getShuMeiDeviceId() {
        String deviceId = "";
        try {
            deviceId = SmAntiFraud.getDeviceId();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return deviceId;
    }

    // suuid获取,保存在sp中，不然每次返回都是不一样的suuid
    public static String getMyUUID() {
        String key = "SUUID";
        String appSuuid = SPUtils.getInformain(key, "");
        if (TextUtils.isEmpty(appSuuid.trim())) {
            String suuid = DonewsAgent.obtainSuuid(UtilsConfig.getApplication());
            SPUtils.setInformain(key, suuid);
            appSuuid = suuid;
        }
        return appSuuid;
    }

    public static String getChannelName() {
        ChannelInfo channelInfo = WalleChannelReader.getChannelInfo(UtilsConfig.getApplication());
        String channel;
        if (channelInfo != null) {
            channel = channelInfo.getChannel();
            Map<String, String> extraInfo = channelInfo.getExtraInfo();
        } else {
            channel = WalleChannelReader.getChannel(UtilsConfig.getApplication());
        }
        return !TextUtils.isEmpty(channel) ? channel : BuildConfig.APP_IDENTIFICATION;

    }

    public static String getOaid() {
        return SPUtils.getInformain(KeySharePreferences.OAID, "");
    }


    /**
     * @return 额外信息中获取邀请码
     */
    public static String getInvCode() {
        // 或者也可以直接根据key获取
        String value = WalleChannelReader.get(UtilsConfig.getApplication(), "invite_code");
        return !TextUtils.isEmpty(value) ? value : "";
    }

    /**
     * 获取到 设备的id
     *
     * @param context
     * @return
     */
    public static String getDeviceIds(Context context) {
        final int targetSdkVersion = context.getApplicationInfo().targetSdkVersion;
        if (targetSdkVersion > Build.VERSION_CODES.P && Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            return getUniqueID(context);
        } else {
            return getTelId(context);
        }
    }

    @SuppressLint("HardwareIds")
    private static String getTelId(Context context) {
        final TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return "";
        }
        return manager.getDeviceId();
    }

    private static String getUniqueID(Context context) {
        String id = null;
        final String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        if (!TextUtils.isEmpty(androidId) && !"9774d56d682e549c".equals(androidId)) {
            try {
                UUID uuid = UUID.nameUUIDFromBytes(androidId.getBytes("utf8"));
                id = uuid.toString();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        if (TextUtils.isEmpty(id)) {
            id = getUUID(context);
        }

        return TextUtils.isEmpty(id) ? UUID.randomUUID().toString() : id;
    }

    private static String getUUID(Context context) {
        String serial = null;

        String m_szDevIDShort = "35" +
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +

                ((null != Build.CPU_ABI) ? Build.CPU_ABI.length() : 0) % 10 +

                Build.DEVICE.length() % 10 + Build.DISPLAY.length() % 10 +

                Build.HOST.length() % 10 + Build.ID.length() % 10 +

                Build.MANUFACTURER.length() % 10 + Build.MODEL.length() % 10 +

                Build.PRODUCT.length() % 10 + Build.TAGS.length() % 10 +

                Build.TYPE.length() % 10 + Build.USER.length() % 10; //13 位

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (ActivityCompat.checkSelfPermission(context,
                            Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        serial = "serial"; // 随便一个初始化
                    }
                    serial = android.os.Build.getSerial();
                } else {
                    serial = Build.SERIAL;
                }
                //API>=9 使用serial号
                return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
            } catch (Exception exception) {
                serial = "serial"; // 随便一个初始化
            }
        } else {
            serial = android.os.Build.UNKNOWN; // 随便一个初始化
        }

        //使用硬件信息拼凑出来的15位号码
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static int getBattery() {
        try {
            BatteryManager batteryManager = (BatteryManager) UtilsConfig.getApplication().getSystemService(
                    BATTERY_SERVICE);
            return batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        } catch (Exception e) {
            LogUtil.e(e);
        }
        return 0;
    }

    /***
     * 获取手机的宽度，
     * @param activity
     * @return px值
     */
    public static int getWidthPixels(Activity activity) {
        int widthPixels = 0;
        if (activity != null && !activity.isFinishing()) {
            DisplayMetrics outMetrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
            widthPixels = outMetrics.widthPixels;
        }
        return widthPixels;
    }

    /***
     * 获取手机的高度，
     * @param activity
     * @return px值
     */
    public static int getHeightPixels(Activity activity) {
        int heightPixels = 0;
        if (activity != null && !activity.isFinishing()) {
            DisplayMetrics outMetrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
            heightPixels = outMetrics.heightPixels;
        }
        return heightPixels;
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

}
