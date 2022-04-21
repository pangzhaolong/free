package com.donews.main.utils.integral.beans;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;

import com.dn.integral.jdd.integral.IntegralStateListener;
import com.dn.integral.jdd.integral.ProxyIntegral;
import com.donews.base.base.BaseApplication;
import com.donews.common.contract.BaseCustomViewModel;
import com.donews.main.utils.integral.MainIntegralTaskManager;
import com.donews.middle.utils.CommonUtils;

/**
 * @author lcl
 * Date on 2021/12/24
 * Description:
 * 管理对象中的任务业务实体
 */
public class MainIntegralTaskDataItem extends BaseCustomViewModel {
    //原始数据
    private ProxyIntegral srcData;
    //新增任务时候设置的原始的监听。也就是唯一一个全局始终伴随整个声明周期的监听
    private IntegralStateListener srcListener;
    //新增一个关联的监听。可以和原始的监听进行联动
    private IntegralStateListener attchSrcToNewListener;
    //需要新增一个关联监听的情况下对应的生命周期组件
    private Lifecycle lifecycle;
    //------------------ 原始监听的相关状态属性区域 -------------------
    //是否调用了onAdShow方法
    private boolean onAdShow = false;
    private boolean onAdClick = false;
    private boolean onStart = false;
    private boolean onProgress = false;
    private long onProgress_l = -1;
    private long onProgress_l1 = -1;
    private boolean onComplete = false;
    private boolean onInstalled = false;
    private boolean onError = false;
    private boolean onRewardVerify = false;
    private boolean onRewardVerifyError = false;
    private String onRewardVerifyErrorStr = null;
    private Throwable onErrorThrow = null;
    //-------------------------------------------------------------

    //当前任务是否已经失效(任务进行中但是已经失效了)，T:任务正常。F:任务已失效
    public boolean isFailure;

    public MainIntegralTaskDataItem(ProxyIntegral srcData) {
        this.srcData = srcData;
        //原始的监听
        this.srcListener = new IntegralStateListener() {
            @Override
            public void onAdShow() {
                onAdShow = true;
                if (attchSrcToNewListener != null) {
                    attchSrcToNewListener.onAdShow();
                }
            }

            @Override
            public void onAdClick() {
                onAdClick = true;
                if (attchSrcToNewListener != null) {
                    attchSrcToNewListener.onAdClick();
                }
            }

            @Override
            public void onStart() {
                onStart = true;
                if (attchSrcToNewListener != null) {
                    attchSrcToNewListener.onStart();
                }
            }

            @Override
            public void onProgress(long l, long l1) {
                onProgress = true;
                onProgress_l = l;
                onProgress_l1 = l1;
                if (attchSrcToNewListener != null) {
                    attchSrcToNewListener.onProgress(l, l1);
                }
            }

            @Override
            public void onComplete() {
                onComplete = true;
                if (attchSrcToNewListener != null) {
                    attchSrcToNewListener.onComplete();
                }
            }

            @Override
            public void onInstalled() {
                onInstalled = true;
                if (onRewardVerify || onRewardVerifyError) {
                    //如果后续逻辑已经触发。那么移除任务
                    MainIntegralTaskManager.removeTask(getTaskId());
                }
                if (attchSrcToNewListener != null) {
                    attchSrcToNewListener.onInstalled();
                }
            }

            @Override
            public void onError(Throwable throwable) {
                onError = true;
                onErrorThrow = throwable;
                MainIntegralTaskManager.removeTask(getTaskId());
                if (attchSrcToNewListener != null) {
                    attchSrcToNewListener.onError(throwable);
                }
            }

            @Override
            public void onRewardVerify() {
                onRewardVerify = true;
                //告知后台计时服务
                CommonUtils.startCritService(BaseApplication.getInstance(), srcData);
                if (attchSrcToNewListener != null) {
                    attchSrcToNewListener.onRewardVerify();
                }
                MainIntegralTaskManager.removeTask(getTaskId());
            }

            @Override
            public void onRewardVerifyError(String s) {
                MainIntegralTaskManager.removeTask(getTaskId());
                onRewardVerifyError = true;
                onRewardVerifyErrorStr = s;
                if (attchSrcToNewListener != null) {
                    attchSrcToNewListener.onRewardVerify();
                }
            }
        };
    }

    /**
     * 是否为等待安装状态
     *
     * @return
     */
    public boolean isWaitingInstall() {
        //已完成下载。但是未完成安装的
        return onComplete && !onInstalled;
    }

    /**
     * 获取任务id
     *
     * @return
     */
    public String getTaskId() {
        return getSrcData().getSourceRequestId();
    }

    /**
     * 获取原始的监听
     *
     * @return
     */
    public IntegralStateListener getSrcListener() {
        return srcListener;
    }

    /**
     * 检查指定任务和当前任务是否是同一个任务
     *
     * @param newTaskItem 新的任务
     * @return 原始的id
     */
    public boolean checkTaskIsRepeat(MainIntegralTaskDataItem newTaskItem) {
        return this.getTaskId().equals(newTaskItem.getTaskId());
    }

    /**
     * 获取原始数据
     *
     * @return
     */
    public ProxyIntegral getSrcData() {
        return srcData;
    }

    /**
     * 给已经存在的任务关联一个新的监听
     *
     * @param lifecycle             生命周期组件
     * @param attchSrcToNewListener 需要关联的监听
     * @param isInitAttch           是否初次关联(T:是，F:否)
     */
    public void setAttchNewListener(
            Lifecycle lifecycle, IntegralStateListener attchSrcToNewListener, boolean isInitAttch) {
        this.lifecycle = lifecycle;
        this.attchSrcToNewListener = attchSrcToNewListener;
        this.lifecycle.addObserver(
                (LifecycleEventObserver) (source, event) -> {
                    //新绑定的视图。和什么周期绑定
                    if (event.getTargetState() == Lifecycle.State.DESTROYED) {
                        this.attchSrcToNewListener = null;
                        this.lifecycle = null;
                    }
                });
        if (isInitAttch) {
            return; //初始添加。不进行状态同步。因为还没有
        }
        //开始节点。无所谓先后
        if (attchSrcToNewListener != null && onAdShow) {
            attchSrcToNewListener.onAdShow();
        }
        if (attchSrcToNewListener != null && onAdClick) {
            attchSrcToNewListener.onAdClick();
        }
        if (attchSrcToNewListener != null && onStart) {
            attchSrcToNewListener.onStart();
        }
        //中间节点
        if (attchSrcToNewListener != null && onProgress) {
            attchSrcToNewListener.onProgress(onProgress_l, onProgress_l1);
        }
        if (attchSrcToNewListener != null && onComplete) {
            attchSrcToNewListener.onComplete();
        }
        //保证两个终点事件在最后调用
        if (attchSrcToNewListener != null && onInstalled) {
            attchSrcToNewListener.onInstalled();
        }
        if (attchSrcToNewListener != null && onRewardVerify) {
            attchSrcToNewListener.onRewardVerify();
        }
        if (attchSrcToNewListener != null && onRewardVerifyError) {
            attchSrcToNewListener.onRewardVerifyError(onRewardVerifyErrorStr);
        }
        if (attchSrcToNewListener != null && onError) {
            attchSrcToNewListener.onError(onErrorThrow);
        }
    }
}
