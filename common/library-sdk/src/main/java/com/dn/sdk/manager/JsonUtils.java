package com.dn.sdk.manager;

import com.dn.sdk.bean.CSJBean;
import com.dn.sdk.bean.GDTBean;
import com.dn.sdk.bean.IntegralOriginalBean;
import com.dn.sdk.utils.SdkLogUtils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author by SnowDragon
 * Date on 2021/3/29
 * Description:
 */
class JsonUtils {

    public static IntegralOriginalBean transFormIntegralBean(String jsonStr) {
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            return transFormIntegralBean(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static IntegralOriginalBean transFormIntegralBean(JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }
        Gson gson = new Gson();
        try {
            jsonObject.getJSONObject("open_ad_sdk_download_extra");

            CSJBean csjBean = gson.fromJson(jsonObject.toString(), CSJBean.class);
            SdkLogUtils.i(SdkLogUtils.TAG, csjBean.toString());

            if (csjBean.adInfo != null && csjBean.adInfo.metaData != null && csjBean.adInfo.metaData.app != null) {
                return new IntegralOriginalBean(1, csjBean);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        //有可能解析错误
        try {
            GDTBean gdtBean = gson.fromJson(jsonObject.toString(), GDTBean.class);
            if (gdtBean.ext != null) {
                return new IntegralOriginalBean(2, gdtBean);
            }
        } catch (Exception e) {
            SdkLogUtils.e(SdkLogUtils.TAG, e);
        }


        return null;

    }

}
