package com.donews.mail.ui.fragments

import androidx.databinding.DataBindingUtil
import com.alibaba.android.arouter.facade.annotation.Route
import com.donews.base.fragment.MvvmLazyLiveDataFragment
import com.donews.common.router.RouterFragmentPath
import com.donews.detail.R
import com.donews.detail.databinding.MailPackageFragmentBinding
import com.donews.mail.adapter.MailPageackFragmentVPAdapter
import com.donews.mail.viewmodel.MailPackageFragmentViewModle
import com.google.android.material.tabs.TabLayoutMediator

/**
 * @author lcl
 * Date on 2021/10/12
 * Description:
 * 9.9包邮的Fragment
 */
@Route(path = RouterFragmentPath.Mail.PAGE_MAIL_PACKAGE)
class MailPackageFragment :
    MvvmLazyLiveDataFragment<MailPackageFragmentBinding, MailPackageFragmentViewModle>() {

    //viewPager 适配器
    val vpAdapter: MailPageackFragmentVPAdapter by lazy {
        MailPageackFragmentVPAdapter(mDataBinding.mailFrmVp)
    }

    override fun getLayoutId(): Int = R.layout.mail_package_fragment

    override fun onFragmentFirstVisible() {
        super.onFragmentFirstVisible()
        initView()
    }

    //初始化
    private fun initView() {
        //监听分类
        mViewModel.tabListLiveData.observe(this,{
            if(mDataBinding.mailFrmVp.adapter == null) {
                mDataBinding.mailFrmVp.adapter = vpAdapter
            }
            it?.apply {
                vpAdapter.setNewData(this)
            }
        })
        //绑定tabLayout和viewPager
        TabLayoutMediator(mDataBinding.mailFrmTab, mDataBinding.mailFrmVp) { tab, position ->
            tab.text = mViewModel.tabListLiveData.value?.get(position)?.name ?: "--"
        }.attach()
        //获取tab数据
        mViewModel.getTabDatas()
    }
}