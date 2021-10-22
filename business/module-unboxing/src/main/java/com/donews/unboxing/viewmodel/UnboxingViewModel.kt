package com.donews.unboxing.viewmodel

import com.donews.base.viewmodel.BaseLiveDataViewModel
import com.donews.unboxing.bean.UnBoxingResp
import com.donews.unboxing.bean.UnboxingBean
import com.donews.unboxing.repository.UnboxingRepository
import com.donews.unboxing.smartrefreshlayout.interfaces.ListPagingInterface
import com.donews.unboxing.smartrefreshlayout.interfaces.impl.UnboxingListPagingInterfaceImpl

/**
 *  晒单页 ViewModel
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/20 17:47
 */
class UnboxingViewModel : BaseLiveDataViewModel<UnboxingRepository>(),
    ListPagingInterface<UnBoxingResp, UnboxingBean> by UnboxingListPagingInterfaceImpl(UnboxingRepository()) {

    override fun createModel(): UnboxingRepository {
        return UnboxingRepository()
    }
}