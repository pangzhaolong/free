package com.donews.mail.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.donews.detail.R
import com.donews.mail.adapter.MailPageackFragmentListAdapter
import com.donews.mail.beans.MailPackageTabItem
import com.donews.mail.entitys.resps.MailPackHomeListItemResp
import com.donews.mail.model.MailModel

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

    //数据代码
    private lateinit var datas: MutableList<MailPackHomeListItemResp>

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
        getListData()
    }

    private fun init() {
        addView(itemIncludeView)
        updateData()
    }

    //获取列表数据
    private fun getListData() {
        mModel
    }

    //更新数据
    private fun updateData() {
        if (!::listAdapter.isInitialized) {
            //还未初始化
            listAdapter = MailPageackFragmentListAdapter(
                0
            )
            vpRecycler.adapter = listAdapter
        }
        if (!::datas.isInitialized) {
            return //数据还未初始化
        }
        listAdapter.setNewData(datas)
    }
}