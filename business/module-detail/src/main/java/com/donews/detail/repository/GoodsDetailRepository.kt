package com.donews.detail.repository

import com.alibaba.android.arouter.facade.annotation.Autowired
import com.donews.base.model.BaseLiveDataModel
import com.donews.detail.bean.GoodsDetailInfo
import com.donews.common.bean.PrivilegeLinkInfo
import com.donews.common.provider.IDetailProvider
import com.donews.common.router.RouterActivityPath
import com.donews.network.EasyHttp
import com.donews.network.cache.model.CacheMode
import com.donews.network.callback.SimpleCallBack

/**
 * 商品详情数据仓库
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/12 14:47
 */
class GoodsDetailRepository : BaseLiveDataModel() {


    @Autowired(name = RouterActivityPath.GoodsDetail.GOODS_DETAIL_PROVIDER)
    @JvmField
    internal var detailProvider: IDetailProvider? = null

    /**
     * 查询商品详情信息
     */
    fun queryGoodsDetailInfo(id: String?, goodsId: String?, callBack: SimpleCallBack<GoodsDetailInfo>) {
        val urlCreator = UrlCreator()
        val getRequest = EasyHttp.get(urlCreator.getGoodsDetailApi())
            .cacheMode(CacheMode.NO_CACHE)
        id?.let {
            getRequest.params("id", it)
        }
        goodsId?.let {
            getRequest.params("goods_id", goodsId)
        }
        val disposable = getRequest.execute(callBack)
        addDisposable(disposable)
    }

    /**
     * 查询商品转链
     */
    fun queryPrivilegeLink(goodsId: String, couponId: String, callBack: SimpleCallBack<PrivilegeLinkInfo>) {
        val urlCreator = UrlCreator()
        val disposable = EasyHttp.get(urlCreator.getPrivilegeLinkApi())
            .cacheMode(CacheMode.NO_CACHE)
            .params("goods_id", goodsId)
            .params("coupon_id", couponId)
            .execute(callBack)
        addDisposable(disposable)
    }
}