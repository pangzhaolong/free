package com.donews.alive.api;

import com.donews.alive.BuildConfig;
import com.donews.utilslibrary.utils.JsonUtils;

/**
 * @author by SnowDragon
 * Date on 2020/12/9
 * Description:
 */
public class UrlCreator {
    protected static final String TAG = "UrlCreator";
    protected static final String BASE_URL = BuildConfig.BASE_CONFIG_URL;

    /**
     * Test environment
     */
    protected static final String OUT_AD_SUFFIX_DEV = BuildConfig.APP_IDENTIFICATION + "-ad_app_out-dev";

    /**
     * Product environment
     */
    protected static final String OUT_AD_SUFFIX_PROD = BuildConfig.APP_IDENTIFICATION + "-ad_app_out-prod";


    public String getUrl() {
        String suffix = (BuildConfig.DEBUG ? OUT_AD_SUFFIX_DEV : OUT_AD_SUFFIX_PROD);

        return BASE_URL + suffix + JsonUtils.getCommonJson(false);
    }

    /**
     * 引用外广告配置地址
     *
     * @return url
     */
    public String getOutAdConfigUrl() {
        String params = JsonUtils.getCommonJson(false);
        String suffix = BuildConfig.BASE_RULE_URL;
        return BASE_URL + suffix + params;
    }

}
