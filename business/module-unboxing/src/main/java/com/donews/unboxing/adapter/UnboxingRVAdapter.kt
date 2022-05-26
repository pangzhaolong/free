package com.donews.unboxing.adapter

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.android.arouter.launcher.ARouter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.donews.base.utils.ToastUtil
import com.donews.common.decoration.GridSpacingItemDecoration
import com.donews.common.router.RouterActivityPath
import com.donews.middle.views.TaskView
import com.donews.unboxing.R
import com.donews.unboxing.bean.UnboxingBean
import com.donews.unboxing.databinding.UnboxingItemUnboxingBinding
import com.donews.utilslibrary.utils.AppInfo
import com.donews.utilslibrary.utils.DensityUtils
import com.tencent.mmkv.MMKV
import java.util.*

/**
 * 晒单页RecyclerView Adapter
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/20 19:21
 */
class UnboxingRVAdapter(layoutResId: Int) : BaseQuickAdapter<UnboxingBean, BaseViewHolder>(layoutResId) {

    private var mmkv: MMKV = MMKV.mmkvWithID("unBoxingLikeData_" + AppInfo.getUserId(), MMKV.MULTI_PROCESS_MODE)!!

    init {
        addChildClickViewIds(R.id.cl_goods_info)
        addChildClickViewIds(R.id.btn_lottery)
    }

    override fun onItemViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int) {
        super.onItemViewHolderCreated(viewHolder, viewType)
        DataBindingUtil.bind<UnboxingItemUnboxingBinding>(viewHolder.itemView)
    }

    override fun convert(holder: BaseViewHolder, item: UnboxingBean) {
        item?.let { bean ->
            val dataBinding: UnboxingItemUnboxingBinding = holder.getBinding()!!
            dataBinding.unboxingBean = bean

            if (holder.layoutPosition == 0) {
                dataBinding.yywTaskView.visibility = View.VISIBLE;
                dataBinding.yywTaskView.refreshYyw(TaskView.Place_Show_Msg)
            }

            val picAdapter = UnboxingPicAdapter(R.layout.unboxing_item_pic)
            picAdapter.setOnItemClickListener { adapter, _, position ->
                RouterActivityPath.Pictures.toBigImageActivity(adapter.data as ArrayList<String>?, position)
            }

            dataBinding.rvPics.apply {
                setHasFixedSize(true)
                isNestedScrollingEnabled = false
                setItemViewCacheSize(10)
                isFocusableInTouchMode = false
                layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)

                val num = (0 until itemDecorationCount).reversed()
                for (index in num) {
                    removeItemDecorationAt(index)
                }

                addItemDecoration(GridSpacingItemDecoration(3, DensityUtils.dip2px(8f)))
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
                if (!AppInfo.checkIsWXLogin()) {
                    ARouter.getInstance()
                            .build(RouterActivityPath.User.PAGER_LOGIN)
                            .navigation()
                    return@OnClickListener
                }
                val key = bean.id.toString()
                val zan = mmkv.decodeBool(key, false)
                if (!zan) {
                    mmkv.encode(key, true)
                    dataBinding.zan = true
                } else {
                    ToastUtil.show(context, "您已经点过赞了！")
                }
            }

            dataBinding.ivIcLike.setOnClickListener(clickListener)
            dataBinding.tvLikeNumber.setOnClickListener(clickListener)

            dataBinding.zan = mmkv.decodeBool(item.id.toString(), false)

        }
    }
}