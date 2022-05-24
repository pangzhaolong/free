package com.donews.lotterypage.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.doing.spike.R
import com.doing.spike.bean.CombinationSpikeBean
import com.doing.spike.bean.SpikeBean.GoodsListDTO
import com.doing.spike.bean.SpikeBean.RoundsListDTO
import com.doing.spike.databinding.SpikeContextItemBinding
import com.donews.lotterypage.base.LotteryPageBean
import com.donews.lotterypage.databinding.LotteryPageContentItemBinding
import com.donews.utilslibrary.utils.UrlUtils


public class ContentAdapter(context: Context) :
    RecyclerView.Adapter<ContentAdapter.ContentHolder?>() {
    private var mLayoutId = 0
    private var mLotteryPageBean: LotteryPageBean? = null
    private val mContext: Context = context.applicationContext
    private var mListener: OnItemClickListener? = null
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ContentHolder {
        val spikeContextItemBinding: LotteryPageContentItemBinding =
            DataBindingUtil.inflate(LayoutInflater.from(parent.context), mLayoutId, parent, false)
        return ContentHolder(spikeContextItemBinding)
    }

    fun getLayout(layoutId: Int) {
        mLayoutId = layoutId
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ContentHolder, position: Int) {
        val goodsListDTO = mLotteryPageBean!!.list[position]
        val roundedCorners = RoundedCorners(5)
        val options = RequestOptions.bitmapTransform(roundedCorners)
        var imageUrl = mLotteryPageBean!!.list[position].mainPic
        imageUrl = UrlUtils.formatUrlPrefix(imageUrl)
        Glide.with(mContext).load(imageUrl).apply(options)
            .into(holder.mSpikeContextItemBinding.picture)
        holder.mSpikeContextItemBinding.nameLab.text = mLotteryPageBean!!.list[position].title
        var valuePrice = mLotteryPageBean!!.list[position]?.displayPrice
        holder.itemView.setOnClickListener {
            if (mListener != null) {
                mListener!!.onItemClick(goodsListDTO)
            }
        }
        holder.mSpikeContextItemBinding.price.text = ""+mLotteryPageBean!!.list.get(position).displayPrice

    }

    override fun getItemCount(): Int {
        return if (mLotteryPageBean != null && mLotteryPageBean!!.list != null) mLotteryPageBean!!.list.size else 0
    }

    fun setLotteryPageBean(bean: LotteryPageBean?) {
        mLotteryPageBean = bean
    }

    class ContentHolder(var mSpikeContextItemBinding: LotteryPageContentItemBinding) :
        RecyclerView.ViewHolder(mSpikeContextItemBinding.root)

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mListener = listener
    }

    public interface OnItemClickListener {
        fun onItemClick(goodsListDTO: LotteryPageBean.ListDTO?)
    }


}
