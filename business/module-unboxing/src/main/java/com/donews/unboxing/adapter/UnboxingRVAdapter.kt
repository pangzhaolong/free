package com.donews.unboxing.adapter

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.donews.common.decoration.GridItemDecoration
import com.donews.common.utils.DensityUtils
import com.donews.unboxing.R
import com.donews.unboxing.bean.UnboxingBean
import com.donews.unboxing.databinding.UnboxingItemUnboxingBinding
import com.donews.utilslibrary.utils.AppInfo
import com.tencent.mmkv.MMKV

/**
 * 晒单页RecyclerView Adapter
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/20 19:21
 */
class UnboxingRVAdapter(layoutResId: Int) : BaseQuickAdapter<UnboxingBean, BaseViewHolder>(layoutResId) {

    private val mmkv: MMKV = MMKV.mmkvWithID("unBoxingLikeData_" + AppInfo.getToken())!!

    init {
        addChildClickViewIds(R.id.btn_lottery)
    }

    override fun onItemViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int) {
        super.onItemViewHolderCreated(viewHolder, viewType)
        DataBindingUtil.bind<UnboxingItemUnboxingBinding>(viewHolder.itemView)
    }

    override fun convert(helper: BaseViewHolder, item: UnboxingBean?) {
        item?.let { bean ->
            val dataBinding: UnboxingItemUnboxingBinding = helper.getBinding()!!
            dataBinding.unboxingBean = bean


            val picAdapter = UnboxingPicAdapter(R.layout.unboxing_item_pic)

            dataBinding.rvPics.apply {
                setHasFixedSize(true)
                isNestedScrollingEnabled = false
                setItemViewCacheSize(10)
                layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
                addItemDecoration(GridItemDecoration(3, DensityUtils.dip2px(2f)))
                adapter = picAdapter
            }
            picAdapter.setNewData(bean.images as MutableList<String>?)

            val codeString = bean.code
            val resultCodeString = bean.openCode
            if (codeString == resultCodeString) {
                dataBinding.ivIcFree.setImageResource(R.drawable.unboxing_ic_win)
                dataBinding.tvWinningNumber.text = codeString
            } else {
                dataBinding.ivIcFree.setImageResource(R.drawable.unboxing_ic_similarity)
                val builder = SpannableStringBuilder(codeString)
                if (resultCodeString.isBlank()) {
                    builder.setSpan(
                        ForegroundColorSpan(Color.parseColor("#2C2C2C")),
                        codeString.length - 1,
                        codeString.length,
                        Spannable.SPAN_INCLUSIVE_EXCLUSIVE
                    )
                } else {
                    val length = codeString.length.coerceAtMost(resultCodeString.length)
                    for (index in 0 until length) {
                        if (codeString[index] != resultCodeString[index]) {
                            builder.setSpan(
                                ForegroundColorSpan(Color.parseColor("#2C2C2C")),
                                index, index + 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE
                            )
                        }
                    }
                    dataBinding.tvWinningNumber.text = builder
                }
            }

            val clickListener = View.OnClickListener {
                val zan = mmkv.decodeBool(item.id.toString(), false)
                if (!zan) {
                    mmkv.encode(item.id.toString(), true)
                    dataBinding.zan = true
                } else {
                    Toast.makeText(context, "您已经点过赞了！", Toast.LENGTH_SHORT).show()
                }
            }

            dataBinding.ivIcLike.setOnClickListener(clickListener)
            dataBinding.tvLikeNumber.setOnClickListener(clickListener)

            dataBinding.zan = mmkv.decodeBool(item.id.toString(), false)

        }
    }
}