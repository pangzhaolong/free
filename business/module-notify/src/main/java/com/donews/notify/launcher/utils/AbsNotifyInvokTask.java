package com.donews.notify.launcher.utils;

import android.app.Activity;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.donews.notify.launcher.NotifyAnimationView;
import com.donews.notify.launcher.configs.baens.Notify2DataConfigBean;

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
     * item的点击操作
     *
     * @param targetView 点击的视图
     * @param uiTemplat  点击对应的数据
     * @return 是否消费掉这个点击动作，T:已消费,终止流程，F:不消费。继续上层逻辑
     */
    public abstract boolean itemClick(NotifyAnimationView targetView, Notify2DataConfigBean.UiTemplat uiTemplat);

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
