package com.dn.sdk.count;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author by SnowDragon
 * Date on 2020/11/20
 * Description:
 */


@StringDef({TrackType.AD_CLICK, TrackType.AD_SHOW, TrackType.AD_LOAD_ERROR, TrackType.AD_CLOSE, TrackType.AD_SKIP})
@Retention(RetentionPolicy.RUNTIME)
public @interface TrackType {
    String AD_CLICK = "click";
    String AD_SHOW = "show";
    String AD_LOAD_ERROR = "load_error";
    String AD_CLOSE = "close";
    String AD_SKIP = "skip";
}
