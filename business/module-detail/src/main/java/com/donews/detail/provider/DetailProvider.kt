package com.donews.detail.provider

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.alibaba.android.arouter.facade.annotation.Route
import com.donews.common.bean.PrivilegeLinkInfo
import com.donews.common.provider.IDetailProvider
import com.donews.common.router.RouterActivityPath
import com.donews.detail.repository.UrlCreator
import com.donews.network.EasyHttp
import com.donews.network.cache.model.CacheMode
import com.donews.network.callback.SimpleCallBack
import com.orhanobut.logger.Logger
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

    companion object {
        private const val PACKAGE_TB = "com.taobao.taobao"
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


    override fun goToTaoBao(context: Context, link: String) {
        if (link.isBlank()) {
            Logger.i("跳转淘宝:link null")
            Toast.makeText(context, "跳转错误", Toast.LENGTH_SHORT).show()
        } else {
            if (checkAppInstall(context, PACKAGE_TB)) {
                val strings = link.split("//")
                if (strings.isEmpty()) {
                    Logger.i("跳转淘宝:link  split //  is null")
                    return
                }
                val realLink = if (strings.size > 1) strings[1] else strings[0]
                val url = "taobao://$realLink"
                val uri: Uri = Uri.parse(url) // 商品地址
                val intent = Intent(Intent.ACTION_VIEW, uri)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
                context.startActivity(intent)
            } else {
                val url = link
                val uri: Uri = Uri.parse(url) // 商品地址
                val intent = Intent(Intent.ACTION_VIEW, uri)
                context.startActivity(intent)
            }
        }
    }

    private fun checkAppInstall(context: Context, packageName: String): Boolean {
        return try {
            context.packageManager.getPackageInfo(packageName, 0)
            true
        } catch (e: Exception) {
            false
        }
    }

}