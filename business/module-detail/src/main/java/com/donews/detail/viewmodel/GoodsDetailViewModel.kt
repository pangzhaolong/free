package com.donews.detail.viewmodel

import com.donews.base.viewmodel.BaseLiveDataViewModel
import com.donews.detail.bean.GoodsDetailInfo
import com.donews.detail.repository.GoodsDetailRepository
import com.donews.network.callback.SimpleCallBack
import com.donews.network.exception.ApiException
import com.orhanobut.logger.Logger

/**
 * 商品详情ViewModel
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/12 14:49
 */
class GoodsDetailViewModel : BaseLiveDataViewModel<GoodsDetailRepository>() {


    override fun createModel(): GoodsDetailRepository {
        return GoodsDetailRepository()
    }


    fun getGoodsDetailInfo(id: String?, goodsId: String?) {
        mModel.queryGoodsDetailInfo(id, goodsId,
            object : SimpleCallBack<GoodsDetailInfo>() {
                override fun onError(e: ApiException?) {
                    Logger.e(e, "")
                }

                override fun onSuccess(t: GoodsDetailInfo?) {
                    if (t == null) {
                        Logger.d("请求为null")
                    } else {
                        Logger.d(t)
                    }
                }
            })
    }
}