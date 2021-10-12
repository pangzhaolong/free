package com.donews.detail.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.donews.base.activity.MvvmBaseLiveDataActivity
import com.donews.detail.R
import com.donews.detail.databinding.DetailActivityGoodsDetailBinding
import com.donews.detail.viewmodel.GoodsDetailViewModel
import com.orhanobut.logger.Logger

/**
 * 商品详情页
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/11 17:25
 */
class GoodsDetailActivity : MvvmBaseLiveDataActivity<DetailActivityGoodsDetailBinding, GoodsDetailViewModel>() {

    companion object {

        private const val PARAMS_ID = "params_id"
        private const val PARAMS_GOODS_ID = "params_goods_id"

        @JvmStatic
        fun start(context: Context, id: String?, goodsId: String?) {
            val starter = Intent(context, GoodsDetailActivity::class.java)
            id?.let {
                starter.putExtra(PARAMS_ID, id)
            }
            goodsId?.let {
                starter.putExtra(PARAMS_GOODS_ID, goodsId)
            }
            context.startActivity(starter)
        }
    }

    private var id: String? = null
    private var goodsId: String? = null

    override fun getLayoutId(): Int {
        return R.layout.detail_activity_goods_detail
    }

    override fun initView() {
        intent?.let {
            id = it.getStringExtra(PARAMS_ID)
            goodsId = it.getStringExtra(PARAMS_GOODS_ID)
        }
        if (id.isNullOrBlank() && goodsId.isNullOrBlank()) {
            Logger.d("id and goodsId is null or blank")
        }
        mViewModel.getGoodsDetailInfo(id, goodsId)
    }
}