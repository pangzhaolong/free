package com.donews.task.vm

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.donews.base.viewmodel.BaseLiveDataViewModel

/**
 *  make in st
 *  on 2022/5/7 10:39
 */
class TaskViewModel: BaseLiveDataViewModel<TaskRepository>() {

    override fun createModel(): TaskRepository {
        return TaskRepository()
    }

    /**
     * 账户金币数据
     */
    val goldCoinNum: ObservableField<String> = ObservableField<String>("0.00")

    /**
     * 账户活跃度数据
     */
    val activityNum: ObservableField<String> = ObservableField<String>("0.00")

    /**
     * 是否显示宝箱可领取Icon
     */
    val isShowIconCanGet : ObservableBoolean = ObservableBoolean(true)

    /**
     * 是否显示宝箱倒计时时间
     */
    val isShowBoxTimeView : ObservableBoolean = ObservableBoolean(false)

}