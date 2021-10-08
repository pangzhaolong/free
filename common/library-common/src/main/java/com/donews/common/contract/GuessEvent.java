package com.donews.common.contract;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author by SnowDragon
 * Date on 2021/1/4
 * Description:
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({GuessEvent.BTN_GIVE_UP, GuessEvent.BTN_GO_ON_CHALLENGE, GuessEvent.BTN_EARN_EXTRA_REWARD,
        GuessEvent.BTN_START_NEXT, GuessEvent.BTN_GET_ENERGY, GuessEvent.BTN_GET_REWARD
        , GuessEvent.ERROR_THREE_TIMES_LOOK_VIDEO, GuessEvent.LOOK_VIDEO_GET_ENERGY,
        GuessEvent.PAGE_GUESS_RIGHT, GuessEvent.PAGE_NO_ENERGY
        , GuessEvent.PAGE_GUESS_RIGHT_FINAL, GuessEvent.VIDEO_LOAD_ERROR_REFRESH})
public @interface GuessEvent {


    //放弃领取
    int BTN_GIVE_UP = 0;
    /**
     * 继续挑战
     */
    int BTN_GO_ON_CHALLENGE = 1;
    /**
     * 领取额外奖励
     */
    int BTN_EARN_EXTRA_REWARD = 2;
    /**
     * 开启下一轮
     */
    int BTN_START_NEXT = 3;
    /**
     * 领取体力
     */
    int BTN_GET_ENERGY = 4;

    /**
     * 领取奖励
     */
    int BTN_GET_REWARD = 5;


    //看视频 猜错三次看视频
    int ERROR_THREE_TIMES_LOOK_VIDEO = 6;
    //看视频 领体力
    int LOOK_VIDEO_GET_ENERGY = 7;

    //猜对
    int PAGE_GUESS_RIGHT = 8;
    //最后一题
    int PAGE_GUESS_RIGHT_FINAL = 9;

    //没有体力
    int PAGE_NO_ENERGY = 10;

    //视频加载视频刷新题
    int VIDEO_LOAD_ERROR_REFRESH = 11;

}
