package com.dn.sdk.bean;

import android.text.TextUtils;

/**
 * @author by SnowDragon
 * Date on 2021/3/29
 * Description:
 */
public class IntegralOriginalBean {
    /**
     * 1:穿山甲，2 腾讯
     */
    public int type;
    public CSJBean csjBean;
    public GDTBean gdtBean;

    public IntegralOriginalBean(int type, CSJBean csjBean) {
        this.type = type;
        this.csjBean = csjBean;
    }

    public IntegralOriginalBean(int type, GDTBean gdtBean) {
        this.type = type;
        this.gdtBean = gdtBean;
    }

    public String getPackageName() {
        if (csjBean != null) {
            return csjBean.adInfo.metaData.app.package_name;
        }

        if (TextUtils.isEmpty(gdtBean.ext.packagename)) {
            return gdtBean.ext.pkg_name;
        }

        return gdtBean.ext.packagename;

    }

    public String getName() {
        if (csjBean != null) {
            return csjBean.adInfo.metaData.app.app_name;
        }
        return gdtBean.ext.appname;
    }

    public String geIconUrl() {
        if (csjBean != null) {
            return csjBean.adInfo.metaData.icon.url;
        }
        if (gdtBean == null) {
            return null;
        }
        if (TextUtils.isEmpty(gdtBean.ext.applogo)) {
            return gdtBean.corporateLogo;
        }
        return gdtBean.ext.applogo;
    }

    public String geCoverImage() {
        if (csjBean != null && csjBean.adInfo.metaData.cover_image != null) {
            return csjBean.adInfo.metaData.cover_image.url;
        }
        if (gdtBean == null) {
            return null;
        }

        if (gdtBean.screenshot_url_list != null && gdtBean.screenshot_url_list.size() > 0) {
            return gdtBean.screenshot_url_list.get(0);
        }

        return null;
    }

    public String getDownloadUrl() {
        if (csjBean != null) {
            return csjBean.adInfo.metaData.app.download_url;
        }
        if (gdtBean == null) {
            return null;
        }
        return gdtBean.ext.pkgurl;
    }

    public String getDeepLink() {
        if (csjBean != null && csjBean.adInfo.metaData.deep_link != null) {
            return csjBean.adInfo.metaData.deep_link.deeplink_url;
        }
        return null;
    }

    public boolean isApp() {
        if (csjBean != null && csjBean.adInfo != null && csjBean.adInfo.metaData != null && csjBean.adInfo.metaData.app != null) {
            return true;
        }

        if (gdtBean != null && gdtBean.ext != null) {
            return true;
        }


        return false;
    }

    public String getTxt() {
        if (csjBean != null) {
            return csjBean.adInfo.metaData.title;
        }

        if (gdtBean != null) {
            return gdtBean.txt;
        }
        return null;
    }

    public String getDesc() {
        if (csjBean != null) {
            return csjBean.adInfo.metaData.description;
        }
        if (gdtBean != null) {
            return gdtBean.desc;
        }
        return null;
    }
}
