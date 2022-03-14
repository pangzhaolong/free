package com.donews.detail.adapter

import android.content.Context
import android.graphics.Paint
import android.text.SpannableString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.donews.base.widget.CenterImageSpan
import com.donews.utilslibrary.utils.DensityUtils
import com.donews.detail.R
import com.donews.detail.bean.DetailPicInfo
import com.donews.detail.bean.GoodsDetailInfo
import com.donews.detail.databinding.*
import com.donews.detail.ui.GoodsDetailActivity.EventListener
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
class GoodsDetailAdapter(
    val context: Context,
    val lifecycle: Lifecycle,
    val eventListener: EventListener,
    val goodsDetailInfo: GoodsDetailInfo
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_BANNER = 1
        const val TYPE_PRICE = 2
        const val TYPE_RECOMMEND = 3
        const val TYPE_SHOP_INFO = 4
        const val TYPE_DETAIL_PICS = 5

        const val POSITION_GOODS = 0
        const val POSITION_DETAIL_PICS = 4
    }

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
            0 -> return TYPE_BANNER
            //价格
            1 -> return TYPE_PRICE
            //推荐
            2 -> return TYPE_RECOMMEND
            //店铺信息
            3 -> return TYPE_SHOP_INFO
            //详情信息图片
            4 -> return TYPE_DETAIL_PICS
        }
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        when (viewType) {
            TYPE_BANNER -> {
                val dataBinding = DetailItemGoodsDetailBannerBinding.inflate(layoutInflater, parent, false)
                return BannerViewHolder(dataBinding)
            }
            TYPE_PRICE -> {
                val dataBinding = DetailItemGoodsDetailPriceBinding.inflate(layoutInflater, parent, false)
                return PriceViewHolder(dataBinding)
            }
            TYPE_RECOMMEND -> {
                val dataBinding = DetailItemGoodsDetailRecommendBinding.inflate(layoutInflater, parent, false)
                return RecommendViewHolder(dataBinding)
            }

            TYPE_SHOP_INFO -> {
                val dataBinding = DetailItemGoodsDetailShopInfoBinding.inflate(layoutInflater, parent, false)
                return ShopInfoViewHolder(dataBinding)
            }
            TYPE_DETAIL_PICS -> {
                val dataBinding = DetailItemGoodsDetailDetailBinding.inflate(layoutInflater, parent, false)
                return DetailInfoViewHolder(dataBinding)
            }
        }
        val dataBinding = DetailItemGoodsDetailBannerBinding.inflate(layoutInflater, parent, false)
        return BannerViewHolder(dataBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
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
        if (goodsDetailInfo.imgs.isBlank()) {
            bannerViewHolder.dataBinding.bvpBanner.visibility = View.GONE
            bannerViewHolder.dataBinding.ivMainPic.visibility = View.VISIBLE
            Glide.with(context)
                .load(goodsDetailInfo.mainPic)
                .placeholder(R.drawable.detail_img_placeholder)
                .centerCrop()
                .into(bannerViewHolder.dataBinding.ivMainPic)
        } else {
            val data: List<String> = goodsDetailInfo.imgs.split(",")
            bannerViewHolder.dataBinding.bvpBanner.visibility = View.VISIBLE
            bannerViewHolder.dataBinding.ivMainPic.visibility = View.GONE
            bannerViewHolder.dataBinding.bvpBanner.refreshData(data)
        }
    }
    //endregion

    //region 价格
    inner class PriceViewHolder(val dataBinding: DetailItemGoodsDetailPriceBinding) :
        RecyclerView.ViewHolder(dataBinding.root)

    private fun bindPriceView(priceViewHolder: PriceViewHolder, goodsDetailInfo: GoodsDetailInfo) {
        val dataBinding = priceViewHolder.dataBinding
        dataBinding.detailInfo = goodsDetailInfo
        dataBinding.eventListener = eventListener

        val hadCoupon = goodsDetailInfo.couponId.isNotBlank() && goodsDetailInfo.couponLink.isNotBlank()
        dataBinding.hasCoupon = hadCoupon

        dataBinding.tvTitle.text = getTitleString(goodsDetailInfo)

        if (hadCoupon) {
            //添加删除线
            dataBinding.tvOriginalPrice.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
            dataBinding.tvCouponDate.text = getCouponDateString(goodsDetailInfo)
        }
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

        var path = goodsDetailInfo.shopLogo
        //防止返回数据没有https前缀导致图片加载失败
        if (!path.startsWith("http")) {
            path = "https:$path"
        }
        Glide.with(shopInfoViewHolder.dataBinding.ivIcShop)
            .load(path)
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

                val screenWidth = DensityUtils.getScreenWidth()
                for (detailPic in detailPicInfos) {
                    val imageView = ImageView(context)
                    var width = LinearLayout.LayoutParams.MATCH_PARENT;
                    var height = LinearLayout.LayoutParams.WRAP_CONTENT
                    if (detailPic.width != 0) {
                        val scale = screenWidth * 1.0f / detailPic.width
                        width = screenWidth
                        height = (detailPic.height * scale).toInt()
                    }

                    val params = LinearLayout.LayoutParams(width, height)
                    detailInfoViewHolder.dataBinding.llImgs.addView(imageView, params)
                    Glide.with(context)
                        .load(detailPic.img)
                        .placeholder(R.drawable.detail_img_placeholder)
                        .into(imageView)
                }
            }
        } catch (e: JsonParseException) {
            Logger.d(e)
        }
    }
    //endregion
}