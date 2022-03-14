package com.donews.utilslibrary.analysis;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.bytedance.applog.AppLog;
import com.bytedance.applog.InitConfig;
import com.bytedance.applog.util.UriConstants;
import com.dnstatistics.sdk.agent.DonewsAgent;
import com.dnstatistics.sdk.agent.DonewsConfigure;
import com.dnstatistics.sdk.entity.SexEnums;
import com.donews.utilslibrary.BuildConfig;
import com.donews.utilslibrary.utils.AppInfo;
import com.donews.utilslibrary.utils.DeviceUtils;
import com.donews.utilslibrary.utils.KeyConstant;
import com.donews.utilslibrary.utils.KeySharePreferences;
import com.donews.utilslibrary.utils.LogUtil;
import com.donews.utilslibrary.utils.SPUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.commonsdk.listener.OnGetOaidListener;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;


/**
 * @Author: honeylife
 * @CreateDate: 2020/10/13 15:05
 * @Description:
 */
public class AnalysisHelp {
    /**
     * 是否注册了大数据
     */
    public static boolean analysisRegister = false;

    public static void init(Application application) {
        if (LogUtil.allow) {
            DonewsConfigure.setLogEnabled(true);
        }
        DonewsConfigure.init(application, DeviceUtils.getChannelName(), KeyConstant.getANALYSIS_DATA());
        DonewsConfigure.setLogEnabled(BuildConfig.DEBUG);
        register(application);
        setAnalysisUMengRegister(application);
    }

    private static void setAnalysisUMengRegister(Application application) {
        UMConfigure.setLogEnabled(BuildConfig.DEBUG);
        UMConfigure.preInit(application, KeyConstant.getANALYSIS_U_MENG(), DeviceUtils.getChannelName());
        LogUtil.d("data=" + SPUtils.getInformain(KeySharePreferences.AGREEMENT, false));
        if (SPUtils.getInformain(KeySharePreferences.AGREEMENT, false)) {
            setAnalysisInitUmeng(application);
        }
    }

    public static void setAnalysisInitUmeng(Application application) {
        UMConfigure.init(application, KeyConstant.getANALYSIS_U_MENG(), DeviceUtils.getChannelName(), UMConfigure.DEVICE_TYPE_PHONE, null);
        UMConfigure.getOaid(application, new OnGetOaidListener() {
            @Override
            public void onGetOaid(String oaid) {
                SPUtils.setInformain(KeySharePreferences.OAID, !TextUtils.isEmpty(oaid) ? oaid : "");
                LogUtil.d("oaid====" + oaid);
            }
        });
        // 选用AUTO页面采集模式
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);
    }

    // register
    public static void register(Application application) {
        /* 穿山甲 用户行为sdk 初始化开始 */
        final InitConfig config = new InitConfig(BuildConfig.Applog_SDK_APPID, DeviceUtils.getChannelName()); // appid和渠道，appid如不清楚请联系客户成功经理，注意第二个参数 channel 不能为空

        config.setUriConfig(UriConstants.DEFAULT);//上报地址
        // 加密开关，SDK 5.5.1 及以上版本支持，false 为关闭加密，上线前建议设置为 true
        AppLog.setEncryptAndCompress(true);

        config.setAutoStart(true);
        AppLog.init(application, config);
        /* 初始化结束 */
        DonewsAgent.setOaId(DeviceUtils.getOaid());
        DonewsAgent.setExtDev(String.format("versionCode=%s", DeviceUtils.getAppVersionCode()));
        LogUtil.d("register 统计SDK");
        registerUserId();
    }

    /**
     * 传值给大数据
     */
    public static void registerUserId() {
        DonewsAgent.setUserInfo("", AppInfo.getUserId(), SexEnums.MAN, 0);
    }

    /**
     * 事件统计
     *
     * @param mActivity
     * @param eventName
     * @param params    ！！！重要！！！必须按顺序填写，区分传的是字符串还是整数，整数是数据要累加的，一般填字符串，比如成功和失败，点赞和评论的数目等
     */
    public static void onEvent(Context mActivity, @NonNull String eventName, Object... params) {
        Log.e("BIUtils", "event=" + eventName);
        Map<String, String> eventData = new HashMap<>();
        if (params != null && params.length > 0) {
            int dmsCount = 1;
            int dmnCount = 1;
            for (int i = 0; i < params.length; i++) {
                Object param = params[i];
                if (param != null) {
                    if (param instanceof String) {
                        eventData.put("dms" + dmsCount, (String) param);
                        dmsCount++;
                    } else if (param instanceof Integer || param instanceof Long) {
                        eventData.put("dmn" + dmnCount, String.valueOf(param));
                        dmnCount++;
                    }

                }
            }
        }
        eventData.put("dms" + 23, DeviceUtils.getShuMeiDeviceId());//数美id
        try {
            DonewsAgent.onEvent(mActivity, eventName, eventData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 事件统计
     *
     * @param mActivity
     * @param eventName
     */
    public static void onEvent(Context mActivity, @NonNull String eventName) {
        Log.e("BIUtils", "event=" + eventName);
        Map<String, String> eventData = new HashMap<>();
        DonewsAgent.setOaId(DeviceUtils.getOaid());
        eventData.put("dms" + 11, DeviceUtils.getMyUUID());// suuid
        eventData.put("dms" + 12, DeviceUtils.getOaid());//oaid
        eventData.put("dms" + 13, DeviceUtils.getAndroidID() + "");//androiid
        eventData.put("dms" + 14, AppInfo.getUserRegisterTime());
        eventData.put("dms" + 23, DeviceUtils.getShuMeiDeviceId());//数美id
        try {
            DonewsAgent.onEvent(mActivity, eventName, eventData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取应用的签名
     *
     * @param context 上下文
     */
    public static void getAppSign(Context context) {
        PackageManager pm = context.getPackageManager();
        String name = context.getPackageName();
        try {
            PackageInfo packinfo = pm.getPackageInfo(name, PackageManager.GET_SIGNATURES);
            String sign = packinfo.signatures[0].toCharsString();
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] md5sum = md5.digest(sign.getBytes());
            for (int n = 0; n < md5sum.length; n++) {
                if (md5sum[n] < 0) {
                    Log.i("签名", 256 + md5sum[n] + "");
                    continue;
                }
                Log.i("签名", "" + md5sum[n]);
            }
        } catch (Exception e) {

        }
    }


}
