package com.donews.detail.viewmodel

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ImageSpan
import androidx.core.content.ContextCompat
import androidx.core.text.set
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.donews.base.base.BaseApplication
import com.donews.base.viewmodel.BaseLiveDataViewModel
import com.donews.detail.R
import com.donews.detail.bean.GoodsDetailInfo
import com.donews.detail.bean.PrivilegeLinkInfo
import com.donews.detail.repository.GoodsDetailRepository
import com.donews.network.callback.SimpleCallBack
import com.donews.network.exception.ApiException
import com.donews.network.result.LoadResult
import com.orhanobut.logger.Logger

/**
 * 商品详情ViewModel
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/12 14:49
 */
class GoodsDetailViewModel : BaseLiveDataViewModel<GoodsDetailRepository>() {

    private val _goodeDetailLiveData: MutableLiveData<GoodsDetailInfo> = MutableLiveData()
    val goodeDetailLiveData: LiveData<GoodsDetailInfo> = _goodeDetailLiveData

    private val _privilegeLinkLiveData: MutableLiveData<LoadResult<PrivilegeLinkInfo>> = MutableLiveData()
    val privilegeLinkLiveData: LiveData<LoadResult<PrivilegeLinkInfo>> = _privilegeLinkLiveData

    override fun createModel(): GoodsDetailRepository {
        return GoodsDetailRepository()
    }

    /** 获取商品详情 */
    fun getGoodsDetailInfo(id: String?, goodsId: String?) {
        mModel.queryGoodsDetailInfo(id, goodsId,
            object : SimpleCallBack<GoodsDetailInfo>() {
                override fun onError(e: ApiException?) {
                    Logger.e(e, "")
                }

                override fun onSuccess(t: GoodsDetailInfo?) {
                    t?.let {
                        _goodeDetailLiveData.postValue(it)
                    }
                }
            })
    }

    /** 获取高效链接 */
    fun getPrivilegeLink(goodsId: String, couponId: String) {
        mModel.queryPrivilegeLink(goodsId, couponId, object : SimpleCallBack<PrivilegeLinkInfo>() {
            override fun onError(e: ApiException?) {
                e?.let {
                    _privilegeLinkLiveData.value = LoadResult.Error(it)
                }?.run {
                    _privilegeLinkLiveData.value = LoadResult.Error(Throwable("未知错误"))
                }

            }

            override fun onSuccess(t: PrivilegeLinkInfo?) {
                t?.let {
                    _privilegeLinkLiveData.postValue(LoadResult.Success(t))
                }
            }
        })
        _privilegeLinkLiveData.value = LoadResult.Loading
    }
}