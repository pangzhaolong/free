package com.donews.alive.config;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author by SnowDragon
 * Date on 2020/12/9
 * Description:
 */

@IntDef({AppOutType.WIFI, AppOutType.LOCK,
        AppOutType.APP_OUT, AppOutType.CHARGE, AppOutType.INSTALL_OR_REMOVE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AppOutType {

    int WIFI = 101;
    int LOCK = 102;
    int APP_OUT = 103;
    int CHARGE = 104;
    int INSTALL_OR_REMOVE = 105;


}

