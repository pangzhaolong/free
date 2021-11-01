package com.donews.pictures.viewmodel

import com.donews.base.model.BaseLiveDataModel
import com.donews.base.viewmodel.BaseLiveDataViewModel

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/11/1 19:40
 */
class BigImageViewModel : BaseLiveDataViewModel<BaseLiveDataModel>() {
    override fun createModel(): BaseLiveDataModel {
        return BaseLiveDataModel()
    }

}