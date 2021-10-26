package com.donews.utilslibrary.analysis;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;


/**
 * @Author: honeylife
 * @CreateDate: 2020/10/14 15:58
 * @Description:
 */
public class AnalysisUtils {

    /**
     * @param mActivity
     * @param mMobName      友盟统计
     * @param nAnalysisName 大数据统计事件
     */
    public static void onEvent(Context mActivity, @NonNull String mMobName, @NonNull String nAnalysisName) {
        if (!TextUtils.isEmpty(mMobName)) {
            MobclickAgent.onEvent(mActivity, mMobName);
        }
        if (!TextUtils.isEmpty(nAnalysisName)) {
            AnalysisHelp.onEvent(mActivity, nAnalysisName);
        }
    }

    /**
     * 友盟带自定义参数上报
     *
     * @param mActivity
     * @param eventId   事件的id（友盟统计事件，大数据的使用相同的id)
     * @param params    上报的参数
     */
    public static void onEvent(
            Context mActivity,
            @NonNull String eventId,
            HashMap<String, Object> params) {
        if (!TextUtils.isEmpty(eventId)) {
            //上报友盟自定义参数
            if (params == null) {
                MobclickAgent.onEvent(mActivity, eventId);
            } else {
                MobclickAgent.onEventObject(mActivity, eventId, params);
            }
//            MobclickAgent.onEvent(mActivity, mMobName);
            //上报大数据平台带参数的
            if (params == null) {
                AnalysisHelp.onEvent(mActivity, eventId);
            } else {
                Object[] pa = params.values().toArray();
                String[] keys = new String[pa.length];
                params.keySet().toArray(keys);
                if (keys.length == 1) {
                    AnalysisHelp.onEvent(
                            mActivity,
                            eventId,
                            DIBuildUtils.getDIBuildStartIndex(keys[0]),
                            pa);
                } else {
                    //TODO 暂时还不支持多参数大数据自定义起点下标上报。故而后续遇到在处理
                    AnalysisHelp.onEvent(mActivity, eventId, params.values().toArray());
                }
            }
        }
    }

    /**
     * @param mActivity
     * @param nAnalysisName 大数据统计事件
     */
    public static void onEvent(Context mActivity, @NonNull String nAnalysisName) {
        onEvent(mActivity, "", nAnalysisName);
    }
}
