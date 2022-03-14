package com.donews.main.dialog.views

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.PointF
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.IntRange
import androidx.core.animation.doOnEnd
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.donews.base.utils.glide.GlideUtils
import com.donews.main.R
import com.donews.middle.bean.HighValueGoodsBean
import com.donews.utilslibrary.utils.UrlUtils


/**
 * @author lcl
 * Date on 2021/11/20
 * Description:
 *  滚动抽奖的动画,左右滚动你抽奖
 */
class WindGoodsScrollView : RecyclerView {
    var curWid: Int = 0
    var curHei: Int = 0
    private val scrollAdapter = SeepAdapter()
    private val scrollLayoutManager: SeepLineManager by lazy {
        SeepLineManager(context)
    }
    private var finishCall:(()->Unit)? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (curHei != h) {
            curHei = h
        }
        if (curWid != w) {
            curWid = w
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        layoutManager = scrollLayoutManager
        scrollLayoutManager.orientation = HORIZONTAL
        adapter = scrollAdapter
    }

    override fun onInterceptTouchEvent(e: MotionEvent?): Boolean {
        return true
    }

    override fun onTouchEvent(e: MotionEvent?): Boolean {
        return true
    }

    /**
     * 设置数据
     * @param datas 数据源
     */
    fun setDatas(datas: MutableList<HighValueGoodsBean.GoodsInfo>) {
        scrollAdapter.setNewData(datas)
    }

    /**
     * 开始滚动
     * @param tagetPos 滚动的位置
     * @param finishCall 抽奖完成的回调
     */
    fun startScroll(tagetPos: Int = 0,finishCall:()->Unit) {
        //默认向后滚动20页
        this.finishCall = finishCall
        if (tagetPos <= 0 && scrollAdapter.data.size > 0) {
            smoothScrollToPosition(Int.MAX_VALUE)
        } else {
            smoothScrollToPosition(tagetPos)
        }
    }

    /**
     * 适配器
     */
    open inner class SeepAdapter : RecyclerView.Adapter<MViewHoder>() {

        val data: MutableList<HighValueGoodsBean.GoodsInfo> = mutableListOf()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MViewHoder {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.incl_win_goods_scroll_item, parent, false)
            return MViewHoder(itemView)
        }

        override fun onBindViewHolder(holder: MViewHoder, position: Int) {
            GlideUtils.loadImageView(
                this@WindGoodsScrollView.context,
                UrlUtils.formatHeadUrlPrefix(getItem(position).mainPic),
                holder.iconView
            )
        }

        override fun getItemCount(): Int {
            if (data.size > 0) {
                return Int.MAX_VALUE
            }
            return data.size
        }

        override fun getItemId(position: Int): Long {
            return (position % data.size).toLong()
        }

        fun getItem(pos: Int): HighValueGoodsBean.GoodsInfo {
            return data[getItemId(pos).toInt()]
        }

        fun setNewData(datas: MutableList<HighValueGoodsBean.GoodsInfo>) {
            data.clear()
            data.addAll(datas)
            notifyDataSetChanged()
        }
    }

    //ViewHodler
    class MViewHoder : ViewHolder {
        val iconView: ImageView

        constructor(itemView: View) : super(itemView) {
            iconView = itemView.findViewById(R.id.icnl_item_iv)
        }
    }

    /**
     * 速度控制的Manager
     */
    inner class SeepLineManager : LinearLayoutManager {
        private var contxt: Context? = null

        constructor(context: Context?) : super(context) {
            this.contxt = contxt
        }

        constructor(context: Context?, orientation: Int, reverseLayout: Boolean) : super(
            context,
            orientation,
            reverseLayout
        ) {
            this.contxt = contxt
        }

        constructor(
            context: Context?,
            attrs: AttributeSet?,
            defStyleAttr: Int,
            defStyleRes: Int
        ) : super(context, attrs, defStyleAttr, defStyleRes) {
            this.contxt = contxt
        }

        private var speedRatio = 0.000F //滚动控制因子
        private var speedAnim: ValueAnimator? = null
        private var scrollCountPx = 0L //滑动的总距离

        override fun scrollHorizontallyBy(dx: Int, recycler: Recycler?, state: State?): Int {
            val a = super.scrollHorizontallyBy(
                ((speedRatio * dx).toInt()),
                recycler,
                state
            ) //屏蔽之后无滑动效果，证明滑动的效果就是由这个函数实现
            scrollCountPx += a
            if (a == (speedRatio * dx).toInt()) {
                return dx
            }
            return a
        }

        override fun smoothScrollToPosition(
            recyclerView: RecyclerView,
            state: State?,
            position: Int
        ) {
            scrollCountPx = 0
            speedAnim?.cancel()
            speedAnim = ValueAnimator.ofFloat(0.001F, 1F, 0.0095F)
            speedAnim?.addUpdateListener {
                speedRatio = it.animatedValue as Float
            }
            speedAnim?.duration = 2000
            speedAnim?.doOnEnd {
                this@WindGoodsScrollView.scrollToPosition(0)
                finishCall?.invoke()
            }
            speedAnim?.start()
            super.smoothScrollToPosition(recyclerView, state, position)
        }
    }
}