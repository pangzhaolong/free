package com.donews.common.contract;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author by SnowDragon
 * Date on 2021/1/8
 * Description:
 */
@IntDef({})
@Retention(RetentionPolicy.RUNTIME)
public @interface GuessType {

    //猜词-one
    int GUESS_WORD = 0;

    //猜成语
    int GUESS_IDIOM = 1;


}
