package com.donews.utilslibrary.base;

import android.app.Application;

import com.donews.utilslibrary.analysis.AnalysisHelp;
import com.donews.utilslibrary.utils.KeySharePreferences;
import com.donews.utilslibrary.utils.LogUtil;
import com.donews.utilslibrary.utils.SPUtils;


/**
 * @Author: honeylife
 * @CreateDate: 2020/10/28 15:28
 * @Description:
 */
public class UtilsConfig {

    private static Application application;

    // 初始化，获取值
    public static void init(Application mApplication) {
        application = mApplication;
        LogUtil.d("UtilConfig初始化了");
        SPUtils.init(KeySharePreferences.SP_KEY, mApplication);
        AnalysisHelp.init(mApplication);
    }

    public static Application getApplication() {
        if (application == null) {
            application = getApplicationByReflection();
        }
        return application;
    }

    private static Application getApplicationByReflection() {

        try {
            Application application = (Application) Class.forName("android.app.ActivityThread").getMethod("currentApplication").invoke(null, (Object[]) null);
            if (application != null) {
                return application;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Application application = (Application) Class.forName("android.app.AppGlobals")
                    .getMethod("getInitialApplication").invoke(null, (Object[]) null);
            if (application != null) {
                return application;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
