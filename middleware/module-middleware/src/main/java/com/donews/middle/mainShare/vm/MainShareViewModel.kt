package com.donews.middle.mainShare.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.donews.base.viewmodel.BaseLiveDataViewModel
import com.donews.middle.bean.mine2.resp.UserAssetsResp
import com.donews.middle.mainShare.bean.TaskBubbleInfo
import com.donews.middle.mainShare.repository.MainShareRepository
import com.donews.middle.viewmodel.BaseMiddleViewModel
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
    fun requestAdReport(mId: Int, mType: String) {
        viewModelScope.launch {
            mModel.adReport(mId, mType).collect {
                it?.let {
                    if (mType in arrayOf("share", "collect")) {
                        BaseMiddleViewModel.getBaseViewModel()
                            .getDailyTasks(null)
                    }
                    adReport.postValue(it)
                } ?: kotlin.run {
                    adReport.postValue(null)
                }
            }
        }
    }

    companion object {
        //转盘
        const val TYPE_TURNTABLE = "turntable"
        const val ID_TURNTABLE = 0

        //签到
        const val TYPE_SIGN = "sign"
        const val ID_SIGN = 1

        //抽奖
        const val TYPE_LOTTERY = "lottery"
        const val ID_LOTTERY = 2

        //分享
        const val TYPE_SHARE = "share"
        const val ID_SHARE = 3

        //集卡
        const val TYPE_COLLECT = "collect"
        const val ID_COLLECT = 4

        //视频
        const val TYPE_VIDEO = "video"
        const val ID_VIDEO = 5

        //宝箱
        const val TYPE_GIFT_BOX = "giftbox"
        const val ID_GIFT_BOX = 6
    }

}