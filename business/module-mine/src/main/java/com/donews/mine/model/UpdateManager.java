package com.donews.mine.model;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;

/**
 * @auther ming
 * @date$
 */
class UpdateManager {
    private Context mActivity;
    private Handler mHandler;

    public UpdateManager() {

    }


    public UpdateManager(Activity activity) {
        this.mActivity = activity;
//        mHandler = new MyHandler(this);

    }

    public UpdateManager(Activity activity, Handler handler) {
        this.mActivity = activity;
        this.mHandler = handler;

    }
}
