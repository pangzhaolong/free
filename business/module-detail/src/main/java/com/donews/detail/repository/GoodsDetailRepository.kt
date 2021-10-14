package com.donews.detail.repository

import androidx.lifecycle.MutableLiveData
import com.donews.base.model.BaseLiveDataModel
import com.donews.detail.bean.GoodsDetailInfo
import com.donews.network.EasyHttp
import com.donews.network.callback.SimpleCallBack

/**
 * 商品详情数据仓库
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/12 14:47
 */
class GoodsDetailRepository : BaseLiveDataModel() {

    /**
     * 查询商品详情信息
     */
    fun queryGoodsDetailInfo(id: String?, goodsId: String?, callBack: SimpleCallBack<GoodsDetailInfo>) {
        val urlCreator = UrlCreator()
        val getRequest = EasyHttp.get(urlCreator.getGoodsDetailApi())
        id?.let {
            getRequest.params("id", it)
        }
        goodsId?.let {
            getRequest.params("goods_id", id)
        }
        val disposable = getRequest.execute(callBack)
        addDisposable(disposable)
    }
}