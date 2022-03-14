package com.donews.common.lifecycle;

import android.os.Handler;

import com.donews.common.download.DownloadHelper;
import com.donews.utilslibrary.utils.AppStatusUtils;

/**
 * 空实现
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/4/6
 */
public class SimpleApplicationObServer extends ApplicationObserver {


    @Override
    public void onAppCreate() {

    }

    @Override
    public void onAppStart() {
        AppStatusUtils.setAppForceGround(true);
    }

    @Override
    public void onAppResume() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                DownloadHelper.installApp();
            }
        }, 5000);
    }

    @Override
    public void onAppPause() {

    }

    @Override
    public void onAppStop() {
        AppStatusUtils.setAppForceGround(false);
    }
}
