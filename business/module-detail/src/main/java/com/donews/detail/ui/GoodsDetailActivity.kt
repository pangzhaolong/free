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

    private var totalScrollerY: Int = 0
    private var currentScrollY = 0
    private lateinit var goodsDetailAdapter: GoodsDetailAdapter

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

        val statusBarHeight = ImmersionBar.getStatusBarHeight(this)
        val layoutParams: ConstraintLayout.LayoutParams =
            mDataBinding.viewBgStatusBar.layoutParams as ConstraintLayout.LayoutParams
        layoutParams.height = statusBarHeight
        mDataBinding.viewBgStatusBar.layoutParams = layoutParams

        for (index in TAB_TITLE.indices) {
            val tab = mDataBinding.tabLayout.newTab().apply {
                text = TAB_TITLE[index]
            }
            mDataBinding.tabLayout.addTab(tab)
        }

//        mDataBinding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
//            override fun onTabSelected(tab: TabLayout.Tab?) {
//                val layoutManager: LinearLayoutManager = mDataBinding.rvContent.layoutManager as LinearLayoutManager
//                if (mDataBinding.tabLayout.selectedTabPosition == 0) {
//                    layoutManager.scrollToPositionWithOffset(0, 0)
//                } else {
//                    layoutManager.scrollToPositionWithOffset(4, 0)
//                }
//            }
//
//            override fun onTabUnselected(tab: TabLayout.Tab?) {
//
//            }
//
//            override fun onTabReselected(tab: TabLayout.Tab?) {
//
//            }
//
//        })


        refreshToolBar()

        mDataBinding.rvContent.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                currentScrollY += dy
                refreshToolBar()

                val layoutManager: LinearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                val position = layoutManager.findFirstVisibleItemPosition()
                val viewType = goodsDetailAdapter.getItemViewType(position)
                Logger.d(viewType)
                if (viewType == 5) {
                    mDataBinding.tabLayout.getTabAt(1)?.select()
                } else {
                    mDataBinding.tabLayout.getTabAt(0)?.select()
                }
            }
        })


        mViewModel.goodeDetailLiveData.observe(this,
            {
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
}