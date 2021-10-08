package com.dn.sdk.lib.load;

import android.app.Activity;

import com.dn.sdk.api.AdConfigSupply;
import com.dn.sdk.bean.AdConfigBean;
import com.dn.sdk.bean.RequestInfo;
import com.dn.sdk.constant.AdType;
import com.dn.sdk.lib.SdkManager;
import com.dn.sdk.listener.IAdCallBack;
import com.dn.sdk.utils.SdkLogUtils;

import java.util.LinkedList;

/**
 * @author by SnowDragon
 * Date on 2021/1/19
 * Description:
 */
public class LoadBanner {
    LinkedList<AdConfigBean.AdID> adIdS;

    private final Activity activity;
    private final RequestInfo requestInfo;
    private final IAdCallBack callBack;

    public LoadBanner(Activity activity, RequestInfo requestInfo, IAdCallBack callBack) {
        this.activity = activity;
        this.requestInfo = requestInfo;
        this.callBack = callBack;
        requestInfo.adType = AdType.BANNER;
    }

    public void loadBanner() {
        adIdS = AdConfigSupply.getInstance().getAdIdList(requestInfo.adType);
        SdkLogUtils.i(SdkLogUtils.TAG, "");
        loadAd();
    }

    private void loadAd() {
        if (adIdS.isEmpty()) {
            if (callBack != null) {
                callBack.onError("加载失败");
            }
            return;
        }
        AdConfigBean.AdID adID = adIdS.poll();
        AdConfigSupply.getInstance().wrapperRequestInfo(adID, requestInfo);

        SdkManager.getInstance().getAdController(requestInfo.getSdkType())
                .loadBanner(activity, requestInfo, new IAdCallBack() {
                    @Override
                    public void onError(String error) {
                        requestInfo.usePassId = false;
                        loadAd();
                    }

                    @Override
                    public void onShow() {
                        if (callBack != null) {
                            callBack.onShow();
                        }
                    }

                    @Override
                    public void onClose() {
                        if (callBack != null) {
                            callBack.onClose();
                        }
                    }
                });

    }
}
