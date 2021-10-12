package com.donews.mail.model

import androidx.lifecycle.MutableLiveData
import com.donews.base.model.BaseLiveDataModel
import com.donews.base.model.BaseModel
import com.donews.mail.entitys.resps.MailPackHomeListItemResp
import com.donews.network.EasyHttp
import com.donews.network.cache.model.CacheMode
import com.donews.network.callback.SimpleCallBack
import com.donews.network.exception.ApiException

/**
 * @author lcl
 * Date on 2021/10/12
 * Description:
 *  当前模块通用的ViewModel
 */
class MailModel: BaseLiveDataModel() {
//    /**
//     * 获取网路数据
//     *
//     * @return 返回 homeBean的数据
//     */
//    fun getNetData(): MutableLiveData<MutableList<MailPackHomeListItemResp>> {
//        val mutableLiveData: MutableLiveData<HomeBean?> = MutableLiveData<HomeBean?>()
//        addDisposable(EasyHttp.post("")
//            .upJson("")
//            .cacheMode(CacheMode.NO_CACHE)
//            .execute(object : SimpleCallBack<HomeBean?>() {
//                override fun onError(e: ApiException) {
//                    mutableLiveData.postValue(null)
//                }
//
//                override fun onSuccess(homeBean: HomeBean) {
//                    mutableLiveData.postValue(homeBean)
//                }
//            })
//        )
//        return mutableLiveData
//    }
}