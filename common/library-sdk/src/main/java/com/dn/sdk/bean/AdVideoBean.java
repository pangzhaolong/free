package com.dn.sdk.bean;

import com.donews.b.main.info.DoNewsExpressDrawFeedAd;

/**
 * @author by SnowDragon
 * Date on 2020/11/25
 * Description:
 */
public class AdVideoBean {
    public int type = 0;
    public DoNewsExpressDrawFeedAd ad;
    public int videoId;
    public int ImgId;

    public AdVideoBean(int type, DoNewsExpressDrawFeedAd ad, int videoId, int imgId) {
        this.type = type;
        this.ad = ad;
        this.videoId = videoId;
        ImgId = imgId;
    }
}
