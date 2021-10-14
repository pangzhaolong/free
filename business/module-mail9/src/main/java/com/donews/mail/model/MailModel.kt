package com.donews.mail.model

import androidx.lifecycle.MutableLiveData
import com.donews.base.model.BaseLiveDataModel
import com.donews.mail.beans.MailPackageTabItem
import com.donews.mail.entitys.resps.MailPackHomeListItemResp
import com.donews.mail.entitys.resps.MailPackHomeListResp
import com.donews.network.BuildConfig
import com.donews.network.EasyHttp
import com.donews.network.cache.model.CacheMode
import com.donews.network.callback.SimpleCallBack
import com.donews.network.exception.ApiException
import io.reactivex.disposables.Disposable
import kotlin.math.ceil

/**
 * @author lcl
 * Date on 2021/10/12
 * Description:
 *  当前模块通用的ViewModel
 */
class MailModel : BaseLiveDataModel() {

    companion object{
        /** 9.9包邮页面的列表接口地址(每个tab的列表数据接口地址) */
        val API_NINE_OP_GOODS_LIST = "${BuildConfig.BASE_URL}v1/nine-op-goods-list"
    }

    /** 加载更多数据时候的分页大小：40 */
    val pageSize = 40

    /**
     * [getMailHomeListData]接口的缓存数据
     */
    private val mailHomeListCacheData: MutableMap<Int, MutableList<MailPackHomeListItemResp>?> =
        mutableMapOf()

    /**
     * 获取指定tab下的缓存数据
     * @param tabItem tab类型
     * @return 缓存的数据
     */
    fun getMailHomeListCacheData(tabItem: MailPackageTabItem): MutableList<MailPackHomeListItemResp>? {
        return mailHomeListCacheData[tabItem.type]
    }

    /**
     * 获取9.9包邮的顶部指定某个tab的数据(带了缓存功能)
     *
     * @param isRefresh 是否为下拉刷新(就是是否为从头加载)，T:是，F:否
     * @param tabItem 当前加载的Tab信息
     * @param mutableLiveData 接受通知的数据。获得结果之后会更新此对象数据
     * @return 返回本次请求的控制对象
     */
    fun getMailHomeListData(
        isRefresh: Boolean,
        tabItem: MailPackageTabItem,
        mutableLiveData: MutableLiveData<MutableList<MailPackHomeListItemResp>?>
    ): Disposable? {
        var pageId = 1
        val cacheData = mailHomeListCacheData[tabItem.type]
        if (!isRefresh && cacheData?.isNotEmpty() == true) {
            //有数据。并且为加载更多模式。那么计算页码
            pageId += ceil(cacheData.size / pageSize * 1.0).toInt()
        }
        val disp: Disposable = EasyHttp.get(API_NINE_OP_GOODS_LIST)
            .params(
                mutableMapOf(
                    Pair("page_id", "$pageId"),
                    Pair("page_size", "$pageSize"),
                    Pair("nine_cid", "${tabItem.type}")
                )
            )
//            .upJson("") //up系列方法添加参数
            .cacheMode(CacheMode.NO_CACHE)
            .execute(object : SimpleCallBack<MailPackHomeListResp?>() {
                override fun onError(e: ApiException) {
                    mutableLiveData.postValue(null)
                }

                override fun onSuccess(bean: MailPackHomeListResp?) {
                    //添加缓存
                    if (isRefresh) {
                        mailHomeListCacheData[tabItem.type] = bean?.list
                    } else {
                        if (mailHomeListCacheData[tabItem.type] != null) {
                            if (bean?.list?.isNotEmpty() == true) {
                                mailHomeListCacheData[tabItem.type]!!.addAll(bean!!.list)
                            }
                        } else {
                            mailHomeListCacheData[tabItem.type] = bean?.list
                        }
                    }
                    //通知数据
                    mutableLiveData.postValue(bean?.list)
                }
            })
        addDisposable(disp)
        return disp
    }
}