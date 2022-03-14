package com.donews.main.utils.integral;

import androidx.lifecycle.Lifecycle;

import com.dn.sdk.bean.integral.IntegralStateListener;
import com.dn.sdk.bean.integral.ProxyIntegral;
import com.donews.main.utils.integral.beans.MainIntegralTaskDataItem;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lcl
 * Date on 2021/12/24
 * Description:
 * 首页模块中的。积分墙任务管理模块
 */
public class MainIntegralTaskManager {

    //构建一个线程安全的任务集合
    private static Map<String, MainIntegralTaskDataItem> taskMap = Collections.synchronizedMap(new HashMap<>());

    /**
     * 开始一个任务或者是下载一个监听，允许重复添加(如果已经存在的话。那么就会制作关联操作)
     *
     * @param lifecycle    生命周期组件
     * @param integralBean 开始的任务模式
     * @param onListener   需要关联的监听
     * @return 返回当前任务的原始监听, null：表示添加失败
     *  注意：实际上这个才是真正绑定给SDK的兼容。onListener 只是被返回的监听代理的一个监听而已
     */
    public synchronized static IntegralStateListener addOrAttchTask(
            Lifecycle lifecycle, ProxyIntegral integralBean, IntegralStateListener onListener) {
        MainIntegralTaskDataItem taskDataItem = new MainIntegralTaskDataItem(integralBean);
        if (getCheckTaskIsExties(integralBean)) {
            //已经存在任务
            MainIntegralTaskDataItem oldData = taskMap.get(taskDataItem.getTaskId());
            if (oldData != null) {
                oldData.setAttchNewListener(lifecycle, onListener, false);
                return oldData.getSrcListener();
            }
            return null;//理论上应该走不动到这里(但是为了防止万一)
        }
        //新增一个任务
        taskMap.put(taskDataItem.getTaskId(), taskDataItem);
        taskDataItem.setAttchNewListener(lifecycle, onListener, true);
        return taskDataItem.getSrcListener();
    }

    /**
     * 移除指定任务
     *
     * @param taskId 任务id
     * @return 返回的当前被移除的对象
     */
    public synchronized static MainIntegralTaskDataItem removeTask(String taskId) {
        return taskMap.remove(taskId);
    }

    /**
     * 获取指定类型的任务。
     *
     * @param srcData 任务id
     * @return null:任务不存在，否则返回任务
     */
    public synchronized static MainIntegralTaskDataItem getItemTask(ProxyIntegral srcData) {
        return taskMap.get(srcData.getSourceRequestId());
    }

    /**
     * 获取指定任务是否存在
     *
     * @return
     */
    public static boolean getCheckTaskIsExties(ProxyIntegral integralBean) {
        return taskMap.containsKey(integralBean.getSourceRequestId());
    }

}
