package com.donews.common.lifecycle;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

/**
 * 可以监控整个应用的生命周期,是否处于前台后台
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/4/6
 */
public abstract class ApplicationObserver implements LifecycleObserver {
    /**
     * 注册此监听
     */
   public void register() {
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    /**
     * 注销此监听
     */
    public void unregister() {
        ProcessLifecycleOwner.get().getLifecycle().removeObserver(this);
    }

    /**
     * ON_CREATE 在应用程序的整个生命周期中只会被调用一次
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreate() {
        onAppCreate();
    }

    /**
     * 应用程序出现到前台时调用
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    void onStart() {
        onAppStart();
    }

    /**
     * 应用程序出现到前台时调用(会有一定延迟)
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    void onResume() {
        onAppResume();
    }

    /**
     * 应用程序退出到后台时调用(会有一定延迟)
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    void onPause() {
        onAppPause();
    }

    /**
     * 应用程序退出到后台时调用
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    void onStop() {
        onAppStop();
    }

    /**
     * 永远不会被调用到，系统不会分发调用ON_DESTROY事件
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void onDestroy() {

    }


    public  abstract void onAppCreate();

    public abstract void onAppStart();

    public abstract void onAppResume();

    public abstract void onAppPause();

    public abstract void onAppStop();

}
