package com.donews.crashhandler.core;

import android.os.Message;

/**
 * 当activity生命周期内（黑屏影响）结束finish
 * 参考Cockroach
 * @author Swei
 * @date 2021/4/8 20:17
 * @since v1.0
 */
public interface IActivityLifecycleCrashHandler {

    void finishLaunchActivity(Message message);

    void finishResumeActivity(Message message);

    void finishPauseActivity(Message message);

    void finishStopActivity(Message message);

}
