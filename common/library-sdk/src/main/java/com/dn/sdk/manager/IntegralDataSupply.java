package com.dn.sdk.manager;

import android.util.Base64;
import android.util.Log;

import com.dn.sdk.bean.IntegralBean;
import com.dn.sdk.bean.IntegralOriginalBean;
import com.dn.sdk.bean.RequestInfo;
import com.dn.sdk.constant.AdType;
import com.dn.sdk.lib.SDKType;
import com.dn.sdk.utils.SdkLogUtils;
import com.donews.base.utils.CrashHandlerUtil;
import com.donews.common.AppGlobalConfigManager;
import com.donews.utilslibrary.analysis.AnalysisHelp;
import com.donews.utilslibrary.base.UtilsConfig;

import java.util.HashMap;

/**
 * @author by SnowDragon
 * Date on 2021/3/29
 * Description:
 */
public class IntegralDataSupply {

    private IntegralDataSupply() {
    }
    private static final class Holder{
        private static final IntegralDataSupply sInstance = new IntegralDataSupply();
    }
    public static IntegralDataSupply getInstance() {
        return Holder.sInstance;
    }

    private final HashMap<String, IntegralOriginalBean> integralMap = new HashMap<>();

    /**
     * @param reqId 本地广告数据缓存，用于不同时段，上报数据
     * @return
     */
    public IntegralOriginalBean getIntegralOriginalBean(String reqId) {
        return integralMap.get(reqId);
    }

    public void addIntegralData(String reqId, String materialData) {
        String decodeData = new String(Base64.decode(materialData, android.util.Base64.DEFAULT));

        //如果返回的广告素材是app 下载类，保存数据
        IntegralOriginalBean originalBean = JsonUtils.transFormIntegralBean(decodeData);
        Log.i("CrashHandlerUtil","decodeData "+decodeData);
        CrashHandlerUtil.getInstance().writeLocalFile(decodeData, UtilsConfig.getApplication());
        if (originalBean != null) {
            integralMap.put(reqId, originalBean);
        }


    }

    private IntegralBean.DataBean integralBean;

    /**
     * 服务端返回的积分墙列表
     *
     * @return integralBean
     */
    public IntegralBean.DataBean getServerIntegralBean() {
        return integralBean;
    }

    public void setIntegralBean(IntegralBean.DataBean integralBean) {
        this.integralBean = integralBean;
    }

    private RequestInfo requestInfo;

    /**
     * @return 广告配置
     */
    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }

    /**
     * 大数据上报
     *
     * @param eventName 事件名称
     */
    public void appActivateReport(IntegralBean.DataBean bean) {
        appActivateReport("appActivate", bean);
    }

    public void appActivateReport(String eventName, IntegralBean.DataBean bean) {
        if (bean == null) {
            return;
        }

        AnalysisHelp.onEvent(UtilsConfig.getApplication(),
                eventName, "", "", "", "", bean.pkg, bean.name);

    }
}
