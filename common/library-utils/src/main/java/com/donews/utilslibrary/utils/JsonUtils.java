package com.donews.utilslibrary.utils;

import com.donews.utilslibrary.BuildConfig;

import org.json.JSONObject;

/**
 * <p> </p>
 * 作者： created by honeylife<br>
 * 日期： 2020/11/13 18:09<br>
 * 版本：V1.0<br>
 */
public class JsonUtils {
    /**
     * 获取公共的参数 登录需要的参数
     *
     * @param jsonObject
     * @return
     */
    public static JSONObject getCommonJson(JSONObject jsonObject) {
        try {
            JSONObject device = new JSONObject();
            device.put("imei", DeviceUtils.getDeviceId());
            device.put("idfa", "");
            device.put("androidId", DeviceUtils.getAndroidID());
            device.put("suuid", DeviceUtils.getMyUUID());
            device.put("mac", DeviceUtils.getMacAddress());
            device.put("os", "ANDROID");
            device.put("oaid", DeviceUtils.getOaid());
            device.put("smid", DeviceUtils.getShuMeiDeviceId());
            jsonObject.put("device", device);
            jsonObject.put("channel", DeviceUtils.getChannelName());
            jsonObject.put("versionCode", DeviceUtils.getAppVersionCode() + "");
            jsonObject.put("packageName", DeviceUtils.getPackage());
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * 郭威阳  充电小游戏需要的参数 一个字坑，
     * 每个人用一趟，真是烦
     *
     * @param jsonObject
     * @return
     */
    public static JSONObject getCommonJsonGame(JSONObject jsonObject) {
        try {
            JSONObject device = new JSONObject();
            device.put("imei", DeviceUtils.getDeviceId());
            device.put("idfa", "");
            device.put("android_id", DeviceUtils.getAndroidID());
            device.put("suuid", DeviceUtils.getMyUUID());
            device.put("mac", DeviceUtils.getMacAddress());
            device.put("os", 2);
            device.put("user_id", SPUtils.getInformain(KeySharePreferences.USER_ID, "0"));
            device.put("oaid", DeviceUtils.getOaid());
            device.put("channel", DeviceUtils.getChannelName());
            device.put("version_code", DeviceUtils.getAppVersionCode() + "");
            device.put("package_name", DeviceUtils.getPackage());
            jsonObject.put("config", device);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    /**
     * h5 连接要的参数 是否拼接token
     *
     * @return
     */
    public static String getCommonJson(boolean isToken) {
        StringBuilder device = new StringBuilder();
        try {
            device.append("?imei=").append(DeviceUtils.getDeviceId());
            device.append("&idfa=").append("");
            device.append("&android_id=").append(DeviceUtils.getAndroidID());
            device.append("&suuid=").append(DeviceUtils.getMyUUID());
            device.append("&mac=").append(DeviceUtils.getMacAddress());
            device.append("&os=").append(2);
            device.append("&user_id=").append(SPUtils.getInformain(KeySharePreferences.USER_ID, "0"));
            device.append("&oaid=").append(DeviceUtils.getOaid());
            device.append("&channel=").append(DeviceUtils.getChannelName());
            device.append("&version_code=").append(DeviceUtils.getAppVersionCode());
            device.append("&package_name=").append(DeviceUtils.getPackage());
            if (isToken) {
                device.append("&token=").append(AppInfo.getToken());
            }

        } catch (Throwable e) {
            e.printStackTrace();
        }
        return device.toString();
    }


    /**
     * h5 连接要的参数 是否拼接token h5 页面
     *
     * @return
     */
    public static String getCommonH5Json(boolean hasQuestionChar) {
        StringBuilder device = new StringBuilder();
        try {
            if (hasQuestionChar) {
                device.append("&imei=").append(DeviceUtils.getDeviceId());
            } else {
                device.append("?imei=").append(DeviceUtils.getDeviceId());
            }
            device.append("?imei=").append(DeviceUtils.getDeviceId());
            device.append("&idfa=").append("");
            device.append("&android_id=").append(DeviceUtils.getAndroidID());
            device.append("&mac=").append(DeviceUtils.getMacAddress());
            device.append("&os=").append(2);
            device.append("&uid=").append(SPUtils.getInformain(KeySharePreferences.USER_ID, "0"));
            device.append("&oaid=").append(DeviceUtils.getOaid());
            device.append("&channelId=").append("8"); //游戏都传8，郭崴阳要用
            device.append("&version=").append(DeviceUtils.getAppVersionCode());
            device.append("&isPlatform=true");
            device.append("&token=").append(AppInfo.getToken());


        } catch (Throwable e) {
            e.printStackTrace();
        }
        return device.toString();
    }


    public static JSONObject getUserJson(JSONObject jsonObject) {
        try {
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("uid", SPUtils.getInformain(KeySharePreferences.USER_ID, "0"));
            jsonObject1.put("accessToken", AppInfo.getToken());
            jsonObject1.put("channelId", 8);
            jsonObject1.put("isPlatform", true);
            jsonObject.put("user", jsonObject1);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    /**
     * h5链接跳过jenkins缓存
     *
     * @param url
     * @return
     */
    public static String H5url(String url) {
        if (DeviceUtils.getChannelName().contains(BuildConfig.APP_IDENTIFICATION) && url.contains("index.html#")) {
            return url.replace("index.html#", "index.html?nocache=1#");
        }
        return url;
    }
}
