package com.donews.common.provider

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.alibaba.android.arouter.facade.template.IProvider
import com.donews.common.bean.PrivilegeLinkInfo
import com.donews.network.callback.SimpleCallBack
import io.reactivex.disposables.Disposable

/**
 * 商品详情提供的Provider
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/26 17:50
 */
interface IDetailProvider : IProvider {

    /**
     * 高效转链
     * @param goodsId String
     * @param couponId String
     * @param callBack SimpleCallBack<[ERROR : PrivilegeLinkInfo]>
     */
    fun queryPrivilegeLink(goodsId: String, couponId: String, callBack: SimpleCallBack<PrivilegeLinkInfo>): Disposable

    fun goToTaoBao(context: Context, link: String)

}