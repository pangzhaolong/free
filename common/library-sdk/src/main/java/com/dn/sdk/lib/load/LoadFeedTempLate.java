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
public class LoadFeedTempLate {
    LinkedList<AdConfigBean.AdID> adIdS;

    private final Activity activity;
    private final RequestInfo requestInfo;
    private final IAdCallBack callBack;
    private boolean cacheAd = false;

    public LoadFeedTempLate(Activity activity, RequestInfo requestInfo, IAdCallBack callBack) {
        this(activity, requestInfo, callBack, false);
    }

    public LoadFeedTempLate(Activity activity, RequestInfo requestInfo, IAdCallBack callBack, boolean cacheAd) {
        this.activity = activity;
        this.requestInfo = requestInfo;
        this.callBack = callBack;
        requestInfo.adType = AdType.NEWS_FEED_TEMPLATE;
        this.cacheAd = cacheAd;
    }

    public void loadFeedTemplate() {
        adIdS = AdConfigSupply.getInstance().getAdIdList(requestInfo.adType);
        SdkLogUtils.i(SdkLogUtils.TAG, "");
        loadAd();
    }

    private void loadAd() {
        SdkLogUtils.i(SdkLogUtils.TAG, "--waterfall LoadFeedTempLate");
        if (adIdS.isEmpty()) {
            if (callBack != null) {
                callBack.onError("加载失败");
            }
            return;
        }
        AdConfigBean.AdID adID = adIdS.poll();
        AdConfigSupply.getInstance().wrapperRequestInfo(adID, requestInfo);

        SdkManager.getInstance().getAdController(requestInfo.getSdkType())
                .loadNewsFeedTemplate(activity, requestInfo, cacheAd, new IAdCallBack() {
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
