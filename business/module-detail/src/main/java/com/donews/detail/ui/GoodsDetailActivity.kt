package com.donews.detail.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Paint
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
import android.content.ClipData
import android.content.ClipboardManager
import android.net.Uri
import android.view.MotionEvent
import android.widget.Toast
import com.donews.detail.bean.GoodsDetailInfo
import com.donews.detail.bean.PrivilegeLinkInfo
import com.donews.network.result.LoadResult


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

        /** 淘宝app包名 */
        private const val PACKAGE_TB = "com.taobao.taobao"
        private const val APP_PATH_TB_DETAIL = "com.taobao.tao.detail.activity.DetailActivity"
    }

    private var id: String? = null
    private var goodsId: String? = null

    private var totalScrollerY: Int = 0
    private var currentScrollY = 0
    private lateinit var goodsDetailAdapter: GoodsDetailAdapter

    private var recyclerViewScrollState: Int = RecyclerView.SCROLL_STATE_IDLE
    private var tabTile = arrayListOf<String>()

    //判读是否是recyclerView主动引起的滑动，true- 是，false- 否，由tablayout引起的
    private var isRecyclerScroll: Boolean = false

    //记录上一次位置，防止在同一内容块里滑动 重复定位到tablayout
    private var lastPos: Int = 0;

    //用于recyclerView滑动到指定的位置
    private var canScroll: Boolean = false
    private var scrollToPosition: Int = 0


    override fun getLayoutId(): Int {
        return R.layout.detail_activity_goods_detail
    }

    @SuppressLint("ClickableViewAccessibility")
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
        mDataBinding.tvOriginalPrice.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG


        mDataBinding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val pos = tab.position
                isRecyclerScroll = false
                if (pos == 1) {
                    moveToPosition(5)
                } else {
                    moveToPosition(0)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }

        })
//        mDataBinding.rvContent.setOnTouchListener { _, event ->
//            //当滑动由recyclerView触发时，isRecyclerScroll 置true
//            if (event.action == MotionEvent.ACTION_DOWN) {
//                isRecyclerScroll = true
//            }
//            false
//        }
        mDataBinding.rvContent.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                currentScrollY += dy
                refreshToolBar()
                if (isRecyclerScroll) {
                    //第一个可见的view的位置，即tablayou需定位的位置
                    val manager: LinearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val position = manager.findFirstVisibleItemPosition();
                    if (lastPos != position) {
                        mDataBinding.tabLayout.setScrollPosition(position, 0f, true);
                    }
                    lastPos = position
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                recyclerViewScrollState = newState
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    isRecyclerScroll = true
                }
                if (canScroll) {
                    canScroll = false
                    moveToPosition(scrollToPosition)
                }
            }
        })

        mViewModel.goodeDetailLiveData.observe(this, {

            //根据内容适配tab
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
            //绑定数据
            mDataBinding.detailInfo = it
            goodsDetailAdapter = GoodsDetailAdapter(this, lifecycle, EventListener(), it)
            mDataBinding.rvContent.adapter = goodsDetailAdapter

            //预先请求转链
            requestPrivilegeLink(it)
        })
        requestDetailInfo()
    }

    /**  请求商品信息*/
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

    /** 请求转链接口 */
    private fun requestPrivilegeLink(detailInfo: GoodsDetailInfo) {
        mViewModel.getPrivilegeLink(detailInfo.goodsId, detailInfo.couponId)
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

    private fun moveToPosition(position: Int) {
        // 第一个可见的view的位置
        val layoutManager: LinearLayoutManager = mDataBinding.rvContent.layoutManager as LinearLayoutManager
        val firstItem = layoutManager.findFirstVisibleItemPosition();
        // 最后一个可见的view的位置
        val lastItem = layoutManager.findLastVisibleItemPosition();
        when {
            position <= firstItem -> {
                // 如果跳转位置firstItem 之前(滑出屏幕的情况)，就smoothScrollToPosition可以直接跳转，
                mDataBinding.rvContent.smoothScrollToPosition(position);
            }
            position <= lastItem -> {
                // 跳转位置在firstItem 之后，lastItem 之间（显示在当前屏幕），smoothScrollBy来滑动到指定位置
                val top = mDataBinding.rvContent.getChildAt(position - firstItem).top
                mDataBinding.rvContent.smoothScrollBy(0, top);
            }
            else -> {
                // 如果要跳转的位置在lastItem 之后，则先调用smoothScrollToPosition将要跳转的位置滚动到可见位置
                // 再通过onScrollStateChanged控制再次调用当前moveToPosition方法，执行上一个判断中的方法
                mDataBinding.rvContent.smoothScrollToPosition(position);
                scrollToPosition = position;
                canScroll = true
            }
        }
    }


    inner class EventListener {
        fun clickBack(view: View) {
            this@GoodsDetailActivity.finish()
        }

        fun clickCopy(view: View) {
            when (val loadResult = mViewModel.privilegeLinkLiveData.value) {
                is LoadResult.Loading -> {
                    Toast.makeText(this@GoodsDetailActivity, "数据请求中，清稍后", Toast.LENGTH_SHORT).show()
                }
                is LoadResult.Error -> {
                    Toast.makeText(this@GoodsDetailActivity, "复制口令失败", Toast.LENGTH_SHORT).show()
                }
                is LoadResult.Success -> {
                    val info = loadResult.data
                    if (info != null) {
                        val cm: ClipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                        val content = info.tpwd
                        // 创建普通字符型ClipData
                        val mClipData = ClipData.newPlainText("Label", content)
                        // 将ClipData内容放到系统剪贴板里。
                        cm.setPrimaryClip(mClipData)
                        Toast.makeText(this@GoodsDetailActivity, "复制口令成功", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@GoodsDetailActivity, "复制口令失败", Toast.LENGTH_SHORT).show()
                    }
                }
                else -> {
                    Toast.makeText(this@GoodsDetailActivity, "复制口令失败", Toast.LENGTH_SHORT).show()
                }
            }
        }

        fun clickBuy(view: View) {
            when (val loadResult = mViewModel.privilegeLinkLiveData.value) {
                is LoadResult.Loading -> {
                    Toast.makeText(this@GoodsDetailActivity, "数据请求中，清稍后", Toast.LENGTH_SHORT).show()
                }
                is LoadResult.Error -> {
                    Toast.makeText(this@GoodsDetailActivity, "获取链接失败", Toast.LENGTH_SHORT).show()
                }
                is LoadResult.Success -> {
                    val info = loadResult.data
                    if (info != null) {
                        if (checkAppInstall(this@GoodsDetailActivity, PACKAGE_TB)) {
                            val url = "taobao://" + info.shortUrl.split("//")[1]
                            val uri: Uri = Uri.parse(url) // 商品地址
                            val intent = Intent(Intent.ACTION_VIEW, uri)
                            startActivity(intent)
                        } else {
                            val url = info.shortUrl
                            val uri: Uri = Uri.parse(url) // 商品地址
                            val intent = Intent(Intent.ACTION_VIEW, uri)
                            startActivity(intent)
                        }
                    } else {
                        Toast.makeText(this@GoodsDetailActivity, "获取链接失败", Toast.LENGTH_SHORT).show()
                    }
                }
                else -> {
                    Toast.makeText(this@GoodsDetailActivity, "获取链接失败", Toast.LENGTH_SHORT).show()
                }
            }
        }

        fun clickGetCoupon(view: View) {
            when (val loadResult = mViewModel.privilegeLinkLiveData.value) {
                is LoadResult.Loading -> {
                    Toast.makeText(this@GoodsDetailActivity, "数据请求中，清稍后", Toast.LENGTH_SHORT).show()
                }
                is LoadResult.Error -> {
                    Toast.makeText(this@GoodsDetailActivity, "获取链接失败", Toast.LENGTH_SHORT).show()
                }
                is LoadResult.Success -> {
                    val info = loadResult.data
                    if (info != null) {
                        if (checkAppInstall(this@GoodsDetailActivity, PACKAGE_TB)) {
                            val url = "taobao://" + info.shortUrl.split("//")[1]
                            val uri: Uri = Uri.parse(url) // 商品地址
                            val intent = Intent(Intent.ACTION_VIEW, uri)
                            startActivity(intent)
                        } else {
                            val url = info.shortUrl
                            val uri: Uri = Uri.parse(url) // 商品地址
                            val intent = Intent(Intent.ACTION_VIEW, uri)
                            startActivity(intent)
                        }
                    } else {
                        Toast.makeText(this@GoodsDetailActivity, "获取链接失败", Toast.LENGTH_SHORT).show()
                    }
                }
                else -> {
                    Toast.makeText(this@GoodsDetailActivity, "获取链接失败", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun checkAppInstall(context: Context, packageName: String): Boolean {
        return try {
            context.packageManager.getPackageInfo(packageName, 0)
            true
        } catch (e: Exception) {
            false
        }
    }
}