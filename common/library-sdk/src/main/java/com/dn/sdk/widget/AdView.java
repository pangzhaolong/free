package com.dn.sdk.widget;

import android.view.View;

import com.donews.b.main.info.DoNewsAdNativeData;

/**
 * @author by SnowDragon
 * Date on 2020/11/30
 * Description:
 */
public class AdView {
    /**
     * 广告视图
     */
    public View view;
    /**
     * 广告接口
     */
    public DoNewsAdNativeData newsAdNativeData;

    public AdView(View view, DoNewsAdNativeData newsAdNativeData) {
        this.view = view;
        this.newsAdNativeData = newsAdNativeData;
    }

    public void resume() {
        if (newsAdNativeData != null) {
            newsAdNativeData.resume();
        }
    }

    public void destroy() {
        if (newsAdNativeData != null) {
            newsAdNativeData.destroy();
        }
    }
}
