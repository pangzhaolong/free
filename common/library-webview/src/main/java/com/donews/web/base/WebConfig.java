package com.donews.web.base;

import android.app.Application;
import android.util.Log;

import com.donews.utilslibrary.utils.Utils;
import com.orhanobut.logger.Logger;
import com.tencent.smtt.export.external.TbsCoreSettings;
import com.tencent.smtt.sdk.QbSdk;

import java.util.HashMap;

/**
 * <p> </p>
 * 作者： created by honeylife<br>
 * 日期： 2020/11/16 14:30<br>
 * 版本：V1.0<br>
 */
public class WebConfig {

    public static void init(Application application) {
        try {
            if (Utils.getMainProcess(application)) {
                // 在调用TBS初始化、创建WebView之前进行如下配置
                HashMap map = new HashMap();
                map.put(TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER, true);
                map.put(TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE, true);
                QbSdk.initTbsSettings(map);

                //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
                QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
                    @Override
                    public void onViewInitFinished(boolean arg0) {
                        // TODO Auto-generated method stub
                        //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                        Log.d("app", " onViewInitFinished is " + arg0);
                    }

                    @Override
                    public void onCoreInitFinished() {
                        // TODO Auto-generated method stub
                    }
                };
                //x5内核初始化接口
                QbSdk.initX5Environment(application, cb);
            }
        } catch (Exception e) {
            Logger.e("init web config error:" + e.getMessage());
        }
    }
}
