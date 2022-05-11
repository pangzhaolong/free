package com.donews.task.vm

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.donews.base.viewmodel.BaseLiveDataViewModel
import com.donews.middle.bean.mine2.resp.UserAssetsResp
import com.donews.task.bean.BubbleReceiveInfo
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

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
    val goldCoinNum: ObservableField<String> = ObservableField<String>("0")

    /**
     * 账户活跃度数据
     */
    val activityNum: ObservableField<String> = ObservableField<String>("0")

    /**
     * 是否显示宝箱可领取Icon
     */
    val isShowIconCanGet : ObservableBoolean = ObservableBoolean(true)

    /**
     * 是否显示宝箱倒计时时间
     */
    val isShowBoxTimeView : ObservableBoolean = ObservableBoolean(false)

    //气泡领取
    private val mBubbleReceive: MutableLiveData<BubbleReceiveInfo> = MutableLiveData()
    val bubbleReceive: LiveData<BubbleReceiveInfo> = mBubbleReceive

    //气泡领取处理
    fun requestBubbleReceive(mId:Int,mType:String) {
        viewModelScope.launch {
            mModel.bubbleReceive(mId,mType).collect {
                it?.let {
                    mBubbleReceive.postValue(it)
                } ?: kotlin.run {
                    mBubbleReceive.postValue(null)
                }
            }
        }
    }

}