package com.dn.sdk.api;

import com.dn.sdk.BuildConfig;
import com.dn.sdk.utils.SdkLogUtils;
import com.donews.utilslibrary.utils.JsonUtils;
import com.donews.utilslibrary.utils.KeySharePreferences;
import com.donews.utilslibrary.utils.SPUtils;

/**
 * @author by SnowDragon
 * Date on 2020/12/9
 * Description:
 */
public class UrlCreator {
    protected static final String TAG = "UrlCreator";
    protected static final String BASE_URL = BuildConfig.BASE_CONFIG_URL;

    public static final String INTEGRAL_URL = BuildConfig.HTTP_AWARD + "wall/v1/appInfo";

    public static final String INTEGRAL_AWARD_URL = BuildConfig.HTTP_AWARD + "wall/v1/drawScore";

    public static final String INTEGRAL_APP_LIST = BuildConfig.HTTP_AWARD+"wall/v1/appList";
    public static final String INTEGRAL_APP_PAY = BuildConfig.HTTP_AWARD+"wall/v1/pay";


    /**
     * 应用内广告配置地址
     *
     * @return url
     */
    public String getAdConfigUrl() {
        String params = JsonUtils.getCommonJson(false);
        String tag = SPUtils.getInformain(KeySharePreferences.USER_TAG, null);
        if (tag != null) {
            params = params + "&" + tag;
        }
        SdkLogUtils.i(SdkLogUtils.TAG, " " + tag);
        return BASE_URL + BuildConfig.AD_CONFIG + params;
    }
}
