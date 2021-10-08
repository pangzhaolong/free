package com.donews.utilslibrary.analysis;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.umeng.analytics.MobclickAgent;


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
     * @param mActivity
     * @param nAnalysisName 大数据统计事件
     */
    public static void onEvent(Context mActivity, @NonNull String nAnalysisName) {
        onEvent(mActivity, "", nAnalysisName);
    }
}
