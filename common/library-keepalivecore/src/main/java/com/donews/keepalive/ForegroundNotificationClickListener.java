package com.donews.keepalive;

import android.content.Context;
import android.content.Intent;

public interface ForegroundNotificationClickListener {

    /**
     * 通知点击回调方法
     *
     * @param context Context
     * @param intent  Intent
     */
    void foregroundNotificationClick(Context context, Intent intent);
}
