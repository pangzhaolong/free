package com.donews.detail.provider

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.facade.model.RouteMeta
import com.donews.common.bean.PrivilegeLinkInfo
import com.donews.common.provider.IDetailProvider
import com.donews.common.router.RouterActivityPath
import com.donews.detail.repository.UrlCreator
import com.donews.network.EasyHttp
import com.donews.network.cache.model.CacheMode
import com.donews.network.callback.SimpleCallBack
import io.reactivex.disposables.Disposable

/**
 * IDetailProvider 实现
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/26 17:53
 */
@Route(path = RouterActivityPath.GoodsDetail.GOODS_DETAIL_PROVIDER)
class DetailProvider : IDetailProvider {
    override fun init(context: Context?) {

    }

    override fun queryPrivilegeLink(
        goodsId: String,
        couponId: String,
        callBack: SimpleCallBack<PrivilegeLinkInfo>
    ): Disposable {
        val urlCreator = UrlCreator()
        return EasyHttp.get(urlCreator.getPrivilegeLinkApi())
            .cacheMode(CacheMode.NO_CACHE)
            .params("goods_id", goodsId)
            .params("coupon_id", couponId)
            .execute(callBack)
    }


}