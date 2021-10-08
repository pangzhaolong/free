package com.dn.sdk.listener;

import com.donews.b.main.info.DoNewsExpressDrawFeedAd;

import java.util.List;

/**
 * @author by SnowDragon
 * Date on 2020/11/25
 * Description:
 */
public interface IAdDrawFeedListener {
    void renderSuccess(List<DoNewsExpressDrawFeedAd> doNewsDrawFeedAds);
}
