package com.donews.detail.ui

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.donews.base.activity.MvvmBaseLiveDataActivity
import com.donews.common.router.RouterActivityPath
import com.donews.common.utils.DensityUtils
import com.donews.detail.R
import com.donews.detail.adapter.GoodsDetailAdapter
import com.donews.detail.databinding.DetailActivityGoodsDetailBinding
import com.donews.detail.viewmodel.GoodsDetailViewModel
import com.google.android.material.tabs.TabLayout
import com.gyf.immersionbar.BarHide
import com.gyf.immersionbar.ImmersionBar
import com.orhanobut.logger.Logger

/**
 * 商品详情页
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/11 17:25
 */
@Route(path = RouterActivityPath.GoodsDetail.GOODS_DETAIL)
class GoodsDetailActivity : MvvmBaseLiveDataActivity<DetailActivityGoodsDetailBinding, GoodsDetailViewModel>() {

    companion object {

        private const val PARAMS_ID = "params_id"
        private const val PARAMS_GOODS_ID = "params_goods_id"
        private val TAB_TITLE = arrayOf("商品", "详情")
    }

    private var id: String? = null
    private var goodsId: String? = null

    private var totalScrollerY: Int = 0
    private var currentScrollY = 0
    private lateinit var goodsDetailAdapter: GoodsDetailAdapter

    private var recyclerViewScrollState: Int = RecyclerView.SCROLL_STATE_IDLE
    private var tabLayoutScrollRecyclerView: Boolean = false

    private var tabTile = arrayListOf<String>()

    override fun getLayoutId(): Int {
        return R.layout.detail_activity_goods_detail
    }

    override fun initView() {
        ImmersionBar.with(this)
            .navigationBarColor(android.R.color.transparent)
            .statusBarColor(R.color.detail_status_bar_color)
            .hideBar(BarHide.FLAG_HIDE_NAVIGATION_BAR)
            .autoDarkModeEnable(false)
            .fitsSystemWindows(false)
            .init()

        refreshToolBar()

        val statusBarHeight = ImmersionBar.getStatusBarHeight(this)
        val layoutParams: ConstraintLayout.LayoutParams =
            mDataBinding.viewBgStatusBar.layoutParams as ConstraintLayout.LayoutParams
        layoutParams.height = statusBarHeight
        mDataBinding.viewBgStatusBar.layoutParams = layoutParams

        mDataBinding.eventListener = EventListener()

        mDataBinding.rvContent.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                currentScrollY += dy
                refreshToolBar()
                if (recyclerViewScrollState != RecyclerView.SCROLL_STATE_IDLE) {
                    val layoutManager: LinearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val position = layoutManager.findFirstVisibleItemPosition()
                    val viewType = goodsDetailAdapter.getItemViewType(position)
                    if (viewType == 5) {
                        mDataBinding.tabLayout.setScrollPosition(1, 0f, false)
                    } else {
                        mDataBinding.tabLayout.setScrollPosition(0, 0f, false)
                    }
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                recyclerViewScrollState = newState
            }
        })

        mViewModel.goodeDetailLiveData.observe(this,
            {

                if (it.detailPics.isBlank()) {
                    tabTile.add(TAB_TITLE[0])
                } else {
                    tabTile.add(TAB_TITLE[0])
                    tabTile.add(TAB_TITLE[1])
                }
                for (index in tabTile.indices) {
                    val tab = mDataBinding.tabLayout.newTab().apply {
                        text = TAB_TITLE[index]
                    }
                    mDataBinding.tabLayout.addTab(tab)
                }

                goodsDetailAdapter = GoodsDetailAdapter(this, lifecycle, it)
                mDataBinding.rvContent.adapter = goodsDetailAdapter
            })
        requestDetailInfo()
    }

    private fun requestDetailInfo() {
        intent?.let {
            id = it.getStringExtra(PARAMS_ID)
            goodsId = it.getStringExtra(PARAMS_GOODS_ID)
        }
        if (id.isNullOrBlank() && goodsId.isNullOrBlank()) {
            Logger.d("id and goodsId is null or blank")
        }
        mViewModel.getGoodsDetailInfo(id, goodsId)
    }

    private fun refreshToolBar() {
        if (totalScrollerY == 0) {
            totalScrollerY = (DensityUtils.getScreenWidth() / 4f * 2).toInt()
        }

        val halfToolbarScrollY = totalScrollerY / 2
        var progress: Int
        when {
            currentScrollY <= halfToolbarScrollY -> {
                progress = (currentScrollY * 100f / halfToolbarScrollY).toInt()
                mDataBinding.tabLayout.visibility = View.GONE
                mDataBinding.imgBtnBackBlack.visibility = View.GONE
                mDataBinding.ivToolbarBg.visibility = View.GONE
                val alpha = (1 - progress / 100f)
                mDataBinding.ivBgBtnBack.let {
                    it.visibility = View.VISIBLE
                    it.alpha = alpha
                }
                mDataBinding.imgBtnBackWhite.let {
                    it.visibility = View.VISIBLE
                    it.alpha = alpha
                }
            }
            currentScrollY <= totalScrollerY -> {
                progress = ((currentScrollY - halfToolbarScrollY) * 100f / halfToolbarScrollY).toInt()
                mDataBinding.ivBgBtnBack.visibility = View.GONE
                mDataBinding.imgBtnBackWhite.visibility = View.GONE

                val alpha = progress / 100f
                mDataBinding.imgBtnBackBlack.let {
                    it.visibility = View.VISIBLE
                    it.alpha = alpha
                }
                mDataBinding.tabLayout.let {
                    it.visibility = View.VISIBLE
                    it.alpha = alpha
                }
                mDataBinding.ivToolbarBg.let {
                    it.visibility = View.VISIBLE
                    it.alpha = alpha
                }
            }
            else -> {
                mDataBinding.ivBgBtnBack.visibility = View.GONE
                mDataBinding.imgBtnBackWhite.visibility = View.GONE
                mDataBinding.imgBtnBackBlack.let {
                    it.visibility = View.VISIBLE
                    it.alpha = 1f
                }
                mDataBinding.tabLayout.let {
                    it.visibility = View.VISIBLE
                    it.alpha = 1f
                }
                mDataBinding.ivToolbarBg.let {
                    it.visibility = View.VISIBLE
                    it.alpha = 1f
                }
            }
        }

        progress = (currentScrollY * 100f / totalScrollerY).toInt()
        val alpha = progress / 100f
        mDataBinding.viewBgStatusBar.alpha = alpha
    }


    inner class EventListener {
        fun clickBack(view: View) {
            this@GoodsDetailActivity.finish()
        }
    }
}