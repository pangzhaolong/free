package com.dn.sdk.bean;

import com.dn.sdk.listener.AdVideoListener;

/**
 * @author by SnowDragon
 * Date on 2020/11/20
 * Description:
 */
public class AdInfo {

    public AdInfo(Reporter reporter){
        this.reporter = reporter;
    }

    private Reporter reporter;

    public Reporter getReporter() {
        return reporter;
    }

    public void setReporter(Reporter reporter) {
        this.reporter = reporter;
    }

    public static abstract class Reporter {
        AdVideoListener videoListener;

        public void renderVideoListener(AdVideoListener listener) {
            this.videoListener = listener;
        }

    }
}
