package com.donews.alive.launch;

import android.content.Context;
import android.content.Intent;

/**
 * @author by SnowDragon
 * Date on 2020/12/3
 * Description:
 */
abstract class ActionStart {

    /**
     * 通过Intent启动活动
     *
     * @param context context
     * @param intent  intent
     * @param ways    0
     */
    abstract void doAction(Context context, Intent intent, int ways);

    public long getDelay() {
        return 100L;
    }

}
