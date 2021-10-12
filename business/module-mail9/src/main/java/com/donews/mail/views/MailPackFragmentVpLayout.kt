package com.donews.mail.views

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.donews.detail.R
import com.donews.mail.adapter.MailPageackFragmentListAdapter
import com.donews.mail.beans.MailPackageTabItem
import com.donews.mail.entitys.resps.MailPackHomeListItemResp
import com.donews.mail.model.MailModel
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

    //当前模块的Model对象。
    private val mModel: MailModel by lazy {
        MailModel()
    }

    //当前对应的tab数据
    private lateinit var tabItem: MailPackageTabItem

    //列表适配器
    private lateinit var listAdapter: MailPageackFragmentListAdapter

    //数据订阅订阅对象
    private var listLiveData: MutableLiveData<MutableList<MailPackHomeListItemResp>?> =
        MutableLiveData()

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
        getListData(isRefresh = true, isLoadCache = true)
    }

    private fun init() {
        listLiveData.observe(context as FragmentActivity, {
            updateData()
        })
        addView(itemIncludeView)
        updateData()
    }

    private var listDataDisposable: Disposable? = null

    /**
     * 获取列表数据
     * @param isRefresh 是否为下拉加载，T:是，F:否
     * @param isLoadCache 是否允许加载缓存，T:允许，F:不允许
     */
    private fun getListData(isRefresh: Boolean, isLoadCache: Boolean = false) {
        if (::tabItem.isInitialized) {
            return //还未初始化
        }
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
        if (!::listAdapter.isInitialized) {
            //还未初始化
            listAdapter = MailPageackFragmentListAdapter(
                R.layout.mail_package_vp_list_item
            )
            vpRecycler.adapter = listAdapter
        }
        listLiveData.value?.apply {
            listAdapter.setNewData(this)
        }
    }
}