package com.donews.mail.viewmodel

import androidx.lifecycle.MutableLiveData
import com.donews.base.viewmodel.BaseLiveDataViewModel
import com.donews.mail.beans.MailPackageTabItem
import com.donews.mail.model.MailModel

/**
 * @author lcl
 * Date on 2021/10/12
 * Description:
 * 9.9包邮的Fragment的ViewModel
 */
class MailPackageFragmentViewModle : BaseLiveDataViewModel<MailModel>() {

    //tabs的数据集合
    val tabListLiveData: MutableLiveData<MutableList<MailPackageTabItem>> =
        MutableLiveData(mutableListOf())

    override fun createModel(): MailModel {
        return MailModel()
    }

    /**
     * 获取tab数据
     */
    fun getTabDatas() {
        if (tabListLiveData.value == null) {
            val list = mutableListOf<MailPackageTabItem>()
            list.add(MailPackageTabItem(-1, "精选"))
            list.add(MailPackageTabItem(1, "5.9元区"))
            list.add(MailPackageTabItem(2, "9.9元区"))
            list.add(MailPackageTabItem(3, "19.9元区"))
            tabListLiveData.postValue(list)
        }
    }
}