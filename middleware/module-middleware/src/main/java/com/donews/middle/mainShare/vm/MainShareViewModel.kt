package com.donews.middle.mainShare.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.donews.base.viewmodel.BaseLiveDataViewModel
import com.donews.middle.bean.mine2.resp.UserAssetsResp
import com.donews.middle.mainShare.bean.TaskBubbleInfo
import com.donews.middle.mainShare.repository.MainShareRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 *  make in st
 *  on 2022/5/11 11:30
 *  主页面ViewModel,用于main和Fragment之间共享数据
 */
class MainShareViewModel : BaseLiveDataViewModel<MainShareRepository>() {

    override fun createModel(): MainShareRepository {
        return MainShareRepository()
    }

    //获取用户幸运值和活跃度
    private val mUserAssets: MutableLiveData<UserAssetsResp> = MutableLiveData()
    @JvmField
    val userAssets: LiveData<UserAssetsResp> = mUserAssets

    //任务气泡列表
    private val mTaskBubbles: MutableLiveData<TaskBubbleInfo> = MutableLiveData()

    val taskBubbles: LiveData<TaskBubbleInfo> = mTaskBubbles

    val adReport: MutableLiveData<Any> = MutableLiveData()

    //获取用户幸运值和活跃度
    fun requestUserAssets() {
        viewModelScope.launch {
            mModel.getUserAssets().collect {
                it?.let {
                    mUserAssets.postValue(it)
                } ?: kotlin.run {
                    mUserAssets.postValue(null)
                }
            }
        }
    }

    //获取任务气泡列表
    fun requestTaskBubbles() {
        viewModelScope.launch {
            mModel.getTaskBubbles().collect {
                it?.let {
                    mTaskBubbles.postValue(it)
                } ?: kotlin.run {
                    mTaskBubbles.postValue(null)
                }
            }
        }
    }

    //看广告上报
    fun requestAdReport(mId:Int,mType:String) {
        viewModelScope.launch {
            mModel.adReport(mId,mType).collect {
                it?.let {
                    adReport.postValue(it)
                } ?: kotlin.run {
                    adReport.postValue(null)
                }
            }
        }
    }

}