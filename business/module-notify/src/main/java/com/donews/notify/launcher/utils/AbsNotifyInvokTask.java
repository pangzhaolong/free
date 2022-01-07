package com.donews.notify.launcher.utils;

import android.app.Activity;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.donews.notify.launcher.NotifyAnimationView;

import java.util.Map;

/**
 * @author lcl
 * Date on 2022/1/5
 * Description:
 * 对每种通知的处理接口，每种消息的处理对象必须是此接口的实现对象
 */
public abstract class AbsNotifyInvokTask {

    /**
     * 生命周期是否已经结束了，T:已结束，F:活跃中
     */
    protected boolean isDestroy = false;
    /**
     * 关联的生命周期组件
     */
    protected FragmentActivity activity;

    /**
     * 绑定当前类型的数据
     *
     * @param targetView   显示内容的容器
     * @param lastBindTask 再完成之后给上层一个改变视图的机会。再完成绑定之后回调方法
     */
    public abstract void bindTypeData(NotifyAnimationView targetView, Runnable lastBindTask);

    /**
     * 关联生命周期组件
     *
     * @param activity
     */
    public void attchActivity(FragmentActivity activity) {
        this.activity = activity;
        this.isDestroy = true;
        activity.getLifecycle().addObserver(new LifecycleObserver() {
            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            void onDestroy() {
                AbsNotifyInvokTask.this.onDestroy();
                AbsNotifyInvokTask.this.activity = null;
                AbsNotifyInvokTask.this.isDestroy = true;
            }
        });
    }

    /**
     * 表示生命周期已经结束
     */
    public void onDestroy() {
    }

    /**
     * 获取此处理器是否已经不处在生命周期活跃状态
     *
     * @return T:活跃状态，F:休眠状态。没有生命周期组件关联
     */
    public boolean getIsDestroy() {
        return isDestroy;
    }

    /**
     * 获取当前关联的生命周期组件
     *
     * @return
     */
    public FragmentActivity getAttchActivity() {
        return activity;
    }
}
