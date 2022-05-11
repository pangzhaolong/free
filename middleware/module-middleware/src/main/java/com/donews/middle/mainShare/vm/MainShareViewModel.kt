package com.donews.middle.mainShare.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.donews.base.viewmodel.BaseLiveDataViewModel
import com.donews.middle.bean.mine2.resp.UserAssetsResp
import com.donews.middle.mainShare.repository.MainShareRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 *  make in st
 *  on 2022/5/11 11:30
 *  主页面ViewModel,用于main和Fragment之间共享数据
 */
class MainShareViewModel: BaseLiveDataViewModel<MainShareRepository>() {

    override fun createModel(): MainShareRepository {
        return MainShareRepository()
    }

    //获取用户幸运值和活跃度
    private val mUserAssets: MutableLiveData<UserAssetsResp> = MutableLiveData()
    val userAssets: LiveData<UserAssetsResp> = mUserAssets

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

}