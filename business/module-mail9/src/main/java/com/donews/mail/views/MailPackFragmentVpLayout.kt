package com.donews.mail.views

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.Dimension.SP
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.ScreenUtils
import com.donews.mail.R
import com.donews.mail.adapter.MailPageackFragmentListAdapter
import com.donews.mail.beans.MailPackageFragmentListBean
import com.donews.mail.beans.MailPackageTabItem
import com.donews.mail.entitys.resps.MailPackHomeListItemResp
import com.donews.mail.model.MailModel
import com.donews.mail.utils.ViewScrollBarUtil
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout


/**
 * @author lcl
 * Date on 2021/10/12
 * Description:
 *  每个ViewPager页面的单项Item
 */
class MailPackFragmentVpLayout : LinearLayout {

    private val itemIncludeView: View by lazy {
        inflate(context, R.layout.mail_package_fragment_vp_page_include, null)
    }
    private val vpRecycler: RecyclerView by lazy {
        itemIncludeView.findViewById(R.id.vp_recycler)
    }
    private val smRefesehLayout: SmartRefreshLayout by lazy {
        itemIncludeView.findViewById(R.id.mail_sm_refresh)
    }
    private val smRefesehHeadLayout: ClassicsHeader by lazy {
        itemIncludeView.findViewById(R.id.mail_sm_refresh_head)
    }
    private val vpGoTop: FloatingActionButton by lazy {
        itemIncludeView.findViewById(R.id.vp_go_top)
    }
    private val topBg: View by lazy {
        val v: View = itemIncludeView.findViewById(R.id.mail_top_bg)
        val lp = v.layoutParams ?: ViewGroup.LayoutParams(
            (ScreenUtils.getScreenWidth() + ScreenUtils.getScreenWidth() * 0.15).toInt(),
            ConvertUtils.dp2px(100F)
        )
        v.layoutParams = lp
        v
    }

    //当前模块的Model对象。
    private val mModel: MailModel by lazy {
        MailModel()
    }

    //当前对应的tab数据
    private lateinit var tabItem: MailPackageTabItem

    //是否为当前类型的初次加载,T:当前类型的初次加载，F:非初次加载
    private var tabItemIsInitLoad: Boolean = false

    //本次的加载模式
    private var isRefresh: Boolean = false

    //列表适配器
    private lateinit var listAdapter: MailPageackFragmentListAdapter

    //数据订阅订阅对象
    private var listLiveData: MutableLiveData<MailPackageFragmentListBean> =
        MutableLiveData()

    //是否允许显示悬浮去往顶部的按钮
    private val isAllowShowFloatGoTo = true

    //显示悬浮的回到顶部的最大距离、达到这个距离才会显示悬浮按钮
    private val showFloatGoToMaxPX = 30000

    //列表的布局管理器
    private lateinit var layoutManager: RecyclerView.LayoutManager

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        init()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
    }

    /**
     * 绑定tab，将当前的列表和指定分类绑定
     * @param isAnimLoad 是否显示加载动画
     * @param tabItem 当前对应的分类
     */
    fun bindDataList(isAnimLoad: Boolean, tabItem: MailPackageTabItem) {
        this.tabItem = tabItem
        smRefesehLayout.setEnableRefresh(true)
        smRefesehLayout.setEnableLoadMore(false)
        smRefesehLayout.resetNoMoreData()
        listAdapter.loadMoreModule?.isEnableLoadMore = true
        listAdapter.loadMoreModule?.loadMoreComplete()
//        smRefesehLayout.setEnableAutoLoadMore(true)
        vpGoTop.visibility = View.GONE
        listAdapter.setNewData(mutableListOf())
        tabItemIsInitLoad = true
        if (isAnimLoad) {
            smRefesehLayout.autoRefresh()
        } else {
            refreshLoadData()
        }
    }

    /**
     * 回到列表的顶部（带动画的）
     */
    fun goToTopAnim() {
        vpRecycler.smoothScrollToPosition(0)
    }

    /**
     * 回到列表的顶部
     */
    fun goToTopBy(pos: Int = 0) {
        vpRecycler.scrollToPosition(pos)
    }

    /**
     * 设置网络模式
     */
    fun setGridLayoutManager() {
        layoutManager = GridLayoutManager(context, 2)
        vpRecycler.layoutManager = layoutManager
        //更新adapter
        listAdapter = MailPageackFragmentListAdapter()
        vpRecycler.adapter = listAdapter
        listAdapter.loadMoreModule?.setOnLoadMoreListener {
            //加载更多
            getListData(isRefresh = false, isLoadCache = false)
        }
        smRefesehLayout.autoRefresh()
    }

    /**
     * 设置为列表模式(为后期扩展预留)
     */
    fun setLinearLayoutManager() {
        layoutManager = LinearLayoutManager(context)
        vpRecycler.layoutManager = layoutManager
        //更新Adapter
        listAdapter = MailPageackFragmentListAdapter()
        vpRecycler.adapter = listAdapter
        listAdapter.loadMoreModule?.setOnLoadMoreListener {
            //加载更多
            getListData(isRefresh = false, isLoadCache = false)
        }
        smRefesehLayout.autoRefresh()
    }

    //初始加载数据
    private fun init() {
        vpRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                buildScrollBar(dy)
                buildFloatGoToBut(dy)
                scrollTopBg(dy)
            }
        })
        vpGoTop.setOnClickListener {
            goToTopBy(12) //移动到 10
            goToTopAnim() //然后以动画的方式移动到顶部
        }
        //设置下拉刷新区域的属性
        smRefesehHeadLayout.setAccentColorId(R.color.FFFF990)
        smRefesehHeadLayout.setTextSizeTime(SP,13F)
        //刷新容器
        smRefesehLayout.setOnRefreshListener {
            refreshLoadData()
        }
        smRefesehLayout.setOnLoadMoreListener {
            //设置上拉加载更多
            getListData(isRefresh = false, isLoadCache = false)
        }
        //订阅数据
        listLiveData.observe(context as FragmentActivity, {
            updateData()
        })
        val lp = itemIncludeView.layoutParams ?: ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT,
        )
        addView(itemIncludeView, lp)

        updateData()
    }

    //构建滚动条
    private fun buildScrollBar(dy: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (dy > 0) { //上滚动
                ViewScrollBarUtil.setViewScrollBarStyle(vpRecycler, false)
            } else if (dy < 0) { //下滚动
                ViewScrollBarUtil.setViewScrollBarStyle(vpRecycler, true)
            }
        }
    }

    //构建滑动过程中的。是否显示悬浮的回到顶部的按钮
    private fun buildFloatGoToBut(dy: Int) {
        if (vpRecycler.computeVerticalScrollOffset() > showFloatGoToMaxPX) {
            //显示回顶部(滑动距离大于指定值。显示悬浮回顶部按钮)
            if (isAllowShowFloatGoTo) {
                if (dy > 5) { //继续下滚动内容。用户不太想回到顶部。所以将回调顶部按钮隐藏
                    if (vpGoTop.visibility != View.GONE) {
                        vpGoTop.visibility = View.GONE
                    }
                } else if (dy < -10) { //向上滚动内容了。那么判定用户有想回到顶部的意图,显示会顶部按钮
                    if (vpGoTop.visibility != View.VISIBLE) {
                        vpGoTop.visibility = View.VISIBLE
                    }
                }
            }
        } else {
            //隐藏
            if (isAllowShowFloatGoTo && vpGoTop.visibility != View.GONE) {
                vpGoTop.visibility = View.GONE
            }
        }
    }

    //刷新加载数据
    private fun refreshLoadData() {
        //设置下拉刷新
        if (tabItemIsInitLoad) {
            getListData(isRefresh = true, isLoadCache = true)
        } else {
            getListData(isRefresh = true, isLoadCache = false)
        }
        listAdapter.loadMoreModule?.loadMoreComplete()
        listAdapter.loadMoreModule?.isEnableLoadMore = false
        tabItemIsInitLoad = false
    }

    /**
     * 获取列表数据
     * @param isRefresh 是否为下拉加载，T:是，F:否
     * @param isLoadCache 是否允许加载缓存，T:允许，F:不允许
     */
    private fun getListData(isRefresh: Boolean, isLoadCache: Boolean = false) {
        if (!::tabItem.isInitialized) {
            return //还未初始化
        }
        this.isRefresh = isRefresh
        if (isLoadCache) {
            //允许加载缓存
            val cacheList: MutableList<MailPackHomeListItemResp>? =
                mModel.getMailHomeListCacheData(tabItem)
            if (cacheList?.isNotEmpty() == true) {
                listLiveData.postValue(MailPackageFragmentListBean(tabItem, cacheList))
                return //存在缓存数据。直接使用
            }
        }
        mModel.getMailHomeListData(isRefresh, tabItem, listLiveData)
    }

    //更新数据
    private fun updateData() {
        smRefesehLayout.closeHeaderOrFooter()
        if (!::listAdapter.isInitialized) {
            setGridLayoutManager()
        }
        //数据更新通知。更新数据操作
        listLiveData.value?.apply {
            listAdapter.loadMoreModule?.isEnableLoadMore = true
            if (!this@MailPackFragmentVpLayout::tabItem.isInitialized) {
                return@apply //类型还未初始化。无效操作
            }
            if (this@MailPackFragmentVpLayout.tabItem.type != this.tabItem.type) {
                return@apply //通知的数据不是当前的对应的类型。放弃更新
            }
            if (this.updateList == null) {
                if (!isRefresh) {
                    listAdapter.loadMoreModule?.loadMoreFail()
                }
                return@apply
            }
            if (isRefresh) {
                if (this.updateList.isEmpty() || this.updateList.size < mModel.pageSize) {
                    smRefesehLayout.setNoMoreData(this.updateList.isEmpty())
                    smRefesehLayout.setEnableLoadMore(false)
                }
                listAdapter.setNewData(this.updateList)
            } else {
                if (this.updateList.isEmpty()) {
                    listAdapter.loadMoreModule?.loadMoreEnd()
                    smRefesehLayout.setEnableLoadMore(false)
                } else {
                    listAdapter.loadMoreModule?.loadMoreComplete()
                }
                listAdapter.addData(this.updateList)
            }
        }
    }

    //检查是否滑动到了顶部
    private fun getScrollIsTop(): Boolean {
        val lm = vpRecycler.layoutManager ?: return true
        when (vpRecycler.layoutManager) {
            is LinearLayoutManager -> {

            }
            is GridLayoutManager -> {

            }
            is StaggeredGridLayoutManager -> {

            }
        }
        return true
    }

    //滑动距离
    private var scrollPx = 0
    //滑动顶部顶部背景
    private fun scrollTopBg(dy: Int) {
        scrollPx = vpRecycler.computeVerticalScrollOffset()
        if ((topBg.y == 0F && scrollPx == 0) ||
            topBg.y < -topBg.height && scrollPx > topBg.height
        ) {
            return //不在可滑动范围了。终止滑动,防止过度无效调用
        }
        topBg.y = (-vpRecycler.computeVerticalScrollOffset()).toFloat()
        Log.e("this", "滑动中：$dy,${topBg.y}")
    }
}