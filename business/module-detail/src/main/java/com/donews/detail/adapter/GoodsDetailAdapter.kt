package com.donews.detail.adapter

import android.content.Context
import android.graphics.Paint
import android.text.SpannableString
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.donews.base.widget.CenterImageSpan
import com.donews.common.utils.DensityUtils
import com.donews.detail.R
import com.donews.detail.bean.DetailPicInfo
import com.donews.detail.bean.GoodsDetailInfo
import com.donews.detail.databinding.*
import com.donews.detail.ui.GoodsDetailActivity
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.reflect.TypeToken
import com.orhanobut.logger.Logger

/**
 * 商品详情RecyclerViewAdapter
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/14 10:07
 */
class GoodsDetailAdapter(val context: Context, val lifecycle: Lifecycle, val goodsDetailInfo: GoodsDetailInfo) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun getItemCount(): Int {
        return if (goodsDetailInfo.detailPics.isBlank()) {
            4
        } else {
            5
        }
    }

    override fun getItemViewType(position: Int): Int {
        when (position) {
            //banner
            0 -> return 1
            //价格
            1 -> return 2
            //推荐
            2 -> return 3
            //店铺信息
            3 -> return 4
            //详情信息图片
            4 -> return 5
        }
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        when (viewType) {
            1 -> {
                val dataBinding = DetailItemGoodsDetailBannerBinding.inflate(layoutInflater, parent, false)
                return BannerViewHolder(dataBinding)
            }
            2 -> {
                val dataBinding = DetailItemGoodsDetailPriceBinding.inflate(layoutInflater, parent, false)
                return PriceViewHolder(dataBinding)
            }
            3 -> {
                val dataBinding = DetailItemGoodsDetailRecommendBinding.inflate(layoutInflater, parent, false)
                return RecommendViewHolder(dataBinding)
            }

            4 -> {
                val dataBinding = DetailItemGoodsDetailShopInfoBinding.inflate(layoutInflater, parent, false)
                return ShopInfoViewHolder(dataBinding)
            }
            5 -> {
                val dataBinding = DetailItemGoodsDetailDetailBinding.inflate(layoutInflater, parent, false)
                return DetailInfoViewHolder(dataBinding)
            }
        }
        val dataBinding = DetailItemGoodsDetailBannerBinding.inflate(layoutInflater, parent, false)
        return BannerViewHolder(dataBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = getItemViewType(position)
        when (viewType) {
            1 -> {
                val bannerViewHolder: BannerViewHolder = holder as BannerViewHolder
                bindBannerView(bannerViewHolder, goodsDetailInfo)
            }
            2 -> {
                val priceViewHolder: PriceViewHolder = holder as PriceViewHolder
                bindPriceView(priceViewHolder, goodsDetailInfo)
            }
            3 -> {
                val recommendViewHolder: RecommendViewHolder = holder as RecommendViewHolder
                bindRecommendView(recommendViewHolder, goodsDetailInfo)
            }

            4 -> {
                val shopInfoViewHolder: ShopInfoViewHolder = holder as ShopInfoViewHolder
                bindShopInfoView(shopInfoViewHolder, goodsDetailInfo)
            }
            5 -> {
                val detailInfoViewHolder: DetailInfoViewHolder = holder as DetailInfoViewHolder
                bindDetailInfoView(detailInfoViewHolder, goodsDetailInfo)
            }
        }
    }


    //region banner
    inner class BannerViewHolder(val dataBinding: DetailItemGoodsDetailBannerBinding) :
        RecyclerView.ViewHolder(dataBinding.root) {
        init {
            dataBinding.bvpBanner
                .setLifecycleRegistry(lifecycle)
                .setAdapter(GoodsBannerAdapter())
                .setCanLoop(false)
                .create()
        }
    }

    private fun bindBannerView(bannerViewHolder: BannerViewHolder, goodsDetailInfo: GoodsDetailInfo) {
        val data: List<String> = goodsDetailInfo.imgs.split(",")
        bannerViewHolder.dataBinding.bvpBanner.refreshData(data)
    }
    //endregion

    //region 价格
    inner class PriceViewHolder(val dataBinding: DetailItemGoodsDetailPriceBinding) :
        RecyclerView.ViewHolder(dataBinding.root)

    private fun bindPriceView(priceViewHolder: PriceViewHolder, goodsDetailInfo: GoodsDetailInfo) {
        val dataBinding = priceViewHolder.dataBinding
        dataBinding.detailInfo = goodsDetailInfo
        //添加删除线
        dataBinding.tvOriginalPrice.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
        dataBinding.tvTitle.text = getTitleString(goodsDetailInfo)
        dataBinding.tvCouponDate.text = getCouponDateString(goodsDetailInfo)
    }

    private fun getTitleString(goodsDetailInfo: GoodsDetailInfo): SpannableString {
        val span = "d "
        val resId = if (goodsDetailInfo.shopType == 1) R.drawable.detail_ic_tianmao else R.drawable.detail_ic_taobao
        val spannableString = SpannableString(span + goodsDetailInfo.title)

        val drawable = ContextCompat.getDrawable(context, resId)
        drawable?.let {
            //计算大小，使其和文字高度一般一致
            val height = context.resources.getDimension(R.dimen.detail_title) * 0.85f
            val width = height / it.intrinsicHeight * it.intrinsicWidth
            it.setBounds(0, 0, width.toInt(), height.toInt())
        }
        val imageSpan = CenterImageSpan(drawable!!)
        spannableString.setSpan(imageSpan, 0, 1, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
        return spannableString
    }

    private fun getCouponDateString(goodsDetailInfo: GoodsDetailInfo): String {
        val startTime = goodsDetailInfo.couponStartTime.substring(0, 10).replace("-", ".")
        val endTime = goodsDetailInfo.couponEndTime.substring(0, 10).replace("-", ".")
        return context.getString(R.string.detail_coupon_date, startTime, endTime)
    }
    //endregion

    //region 多多推荐
    inner class RecommendViewHolder(val dataBinding: DetailItemGoodsDetailRecommendBinding) :
        RecyclerView.ViewHolder(dataBinding.root)

    private fun bindRecommendView(recommendViewHolder: RecommendViewHolder, goodsDetailInfo: GoodsDetailInfo) {
        recommendViewHolder.dataBinding.detailInfo = goodsDetailInfo
    }
    //endregion

    //region shop 信息
    inner class ShopInfoViewHolder(val dataBinding: DetailItemGoodsDetailShopInfoBinding) :
        RecyclerView.ViewHolder(dataBinding.root) {
    }

    private fun bindShopInfoView(shopInfoViewHolder: ShopInfoViewHolder, goodsDetailInfo: GoodsDetailInfo) {
        shopInfoViewHolder.dataBinding.detailInfo = goodsDetailInfo
        Glide.with(shopInfoViewHolder.dataBinding.ivIcShop)
            .load(goodsDetailInfo.shopLogo)
            .into(shopInfoViewHolder.dataBinding.ivIcShop)
    }
    //endregion

    //region 详情 信息
    inner class DetailInfoViewHolder(val dataBinding: DetailItemGoodsDetailDetailBinding) :
        RecyclerView.ViewHolder(dataBinding.root) {
    }

    private fun bindDetailInfoView(detailInfoViewHolder: DetailInfoViewHolder, goodsDetailInfo: GoodsDetailInfo) {
        detailInfoViewHolder.dataBinding.detailInfo = goodsDetailInfo
        val detailPicInfo = goodsDetailInfo.detailPics
        val gson = Gson()
        try {
            val detailPicInfos =
                gson.fromJson<List<DetailPicInfo>>(detailPicInfo, object : TypeToken<List<DetailPicInfo>>() {}.type)
            detailInfoViewHolder.dataBinding.llImgs.removeAllViews()
            detailPicInfos?.let {
                for (detailPic in detailPicInfos) {
                    val imageView = ImageView(context)
                    val params = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    params.bottomMargin = DensityUtils.dip2px(10f)
                    detailInfoViewHolder.dataBinding.llImgs.addView(imageView, params)
                    Glide.with(context)
                        .load(detailPic.img)
                        .into(imageView)
                }
            }
        } catch (e: JsonParseException) {
            Logger.d(e)
        }
    }
    //endregion
}