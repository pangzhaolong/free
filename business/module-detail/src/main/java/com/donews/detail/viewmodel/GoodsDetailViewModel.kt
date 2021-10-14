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

    private val _goodeDetailLiveData: MutableLiveData<GoodsDetailInfo> = MutableLiveData()
    val goodeDetailLiveData: LiveData<GoodsDetailInfo> = _goodeDetailLiveData

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
                    Logger.d(t)
                    t?.let {
                        _goodeDetailLiveData.postValue(it)
                    }
                }
            })
    }
}