package com.dn.sdk.lib.load;

import android.app.Activity;

import com.dn.sdk.api.AdConfigSupply;
import com.dn.sdk.bean.AdConfigBean;
import com.dn.sdk.bean.RequestInfo;
import com.dn.sdk.constant.AdType;
import com.dn.sdk.lib.SdkManager;
import com.dn.sdk.listener.IAdNewsFeedListener;
import com.dn.sdk.utils.SdkLogUtils;
import com.dn.sdk.widget.AdView;

import java.util.LinkedList;
import java.util.List;

/**
 * @author by SnowDragon
 * Date on 2021/1/19
 * Description:
 */
public class LoadCusRender {
    LinkedList<AdConfigBean.AdID> adIdS;

    private final Activity activity;
    private final RequestInfo requestInfo;
    private final IAdNewsFeedListener callBack;
    int layoutId;

    public LoadCusRender(Activity activity, RequestInfo requestInfo, int layoutId, IAdNewsFeedListener callBack) {
        this.activity = activity;
        this.requestInfo = requestInfo;
        this.callBack = callBack;
        this.layoutId = layoutId;
        requestInfo.adType = AdType.NEWS_FEED_CUSTOM_RENDER;
    }

    public void loadCusRender() {
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
                .loadNewsFeedCustomRender(activity, requestInfo, layoutId, new IAdNewsFeedListener() {

                    @Override
                    public void success(List<AdView> viewList) {
                        if (callBack != null) {
                            callBack.success(viewList);
                        }
                    }

                    @Override
                    public void onError(String errorMsg) {
                        requestInfo.usePassId = false;
                        loadAd();
                    }
                });


    }
}
