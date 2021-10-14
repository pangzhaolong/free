package com.donews.mail.views

import android.animation.Animator
import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.LinearLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.donews.detail.R
import com.donews.mail.adapter.MailPageackFragmentListAdapter
import com.donews.mail.beans.MailPackageTabItem
import com.donews.mail.entitys.resps.MailPackHomeListItemResp
import com.donews.mail.model.MailModel
import com.donews.mail.utils.ViewScrollBarUtil
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import io.reactivex.disposables.Disposable

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
    private val vpGoTop: FloatingActionButton by lazy {
        itemIncludeView.findViewById(R.id.vp_go_top)
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
    private var listLiveData: MutableLiveData<MutableList<MailPackHomeListItemResp>?> =
        MutableLiveData()

    //当前正在请求的控制对象
    private var listDataDisposable: Disposable? = null

    //是否允许显示悬浮去往顶部的按钮
    private val isAllowShowFloatGoTo = true

    //显示悬浮的回到顶部的最大距离、达到这个距离才会显示悬浮按钮
    private val showFloatGoToMaxPX = 30000

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

    /**
     * 绑定tab，将当前的列表和指定分类绑定
     * @param datas
     */
    fun bindDataList(tabItem: MailPackageTabItem) {
        this.tabItem = tabItem
        smRefesehLayout.setEnableRefresh(true)
        smRefesehLayout.setEnableLoadMore(true)
        smRefesehLayout.resetNoMoreData()
//        smRefesehLayout.setEnableAutoLoadMore(true)
        tabItemIsInitLoad = true
        vpGoTop.visibility = View.GONE
        listAdapter.setNewData(mutableListOf())
        smRefesehLayout.autoRefresh()
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
    fun goToTopBy() {
        vpRecycler.smoothScrollToPosition(0)
    }

    private fun init() {
        vpRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                buildScrollBar(dy)
                buildFloatGoToBut(dy)
            }
        })
        vpGoTop.setOnClickListener {
            if (vpRecycler.computeVerticalScrollOffset() > showFloatGoToMaxPX * 2) {
                goToTopBy()
            } else {
                goToTopAnim()
            }
        }
        smRefesehLayout.setOnRefreshListener {
            //设置下拉刷新
            if (tabItemIsInitLoad) {
                getListData(isRefresh = true, isLoadCache = true)
            } else {
                getListData(isRefresh = true, isLoadCache = false)
            }
            tabItemIsInitLoad = false
        }
        smRefesehLayout.setOnLoadMoreListener {
            //设置上拉加载更多
            vpRecycler.overScrollMode = View.OVER_SCROLL_IF_CONTENT_SCROLLS
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
        //配置网格
        vpRecycler.layoutManager = GridLayoutManager(context, 2)
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
                listLiveData.postValue(cacheList)
                return //存在缓存数据。直接使用
            }
        }
        if (listDataDisposable != null && listDataDisposable!!.isDisposed) {
            //上一个任务正在运行。那么结束上一个
            listDataDisposable?.dispose()
        }
        listDataDisposable = mModel.getMailHomeListData(isRefresh, tabItem, listLiveData)
    }

    //更新数据
    private fun updateData() {
        smRefesehLayout.closeHeaderOrFooter()
        if (!::listAdapter.isInitialized) {
            //还未初始化
            listAdapter = MailPageackFragmentListAdapter()
            vpRecycler.adapter = listAdapter
        }
        listLiveData.value?.apply {
            if (isRefresh) {
                if (this.isEmpty() || this.size < mModel.pageSize) {
                    smRefesehLayout.setNoMoreData(this.isEmpty())
                    smRefesehLayout.setEnableLoadMore(false)
                }
                listAdapter.setNewData(this)
            } else {
                if (this.isEmpty()) {
                    smRefesehLayout.setEnableLoadMore(false)
                }
                listAdapter.addData(this)
            }
        }
    }
}