package com.donews.unboxing.repository

import com.donews.base.model.BaseLiveDataModel
import com.donews.network.EasyHttp
import com.donews.network.callback.SimpleCallBack
import com.donews.unboxing.bean.UnboxingBean

/**
 * 晒单页数据仓库
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/20 17:46
 */
class UnboxingRepository : BaseLiveDataModel() {


    /**
     * 获取晒单页数据
     * @param callBack SimpleCallBack<UnboxingBean> 数据返回回调
     */
    fun getUnboxingData(callBack: SimpleCallBack<MutableList<UnboxingBean>>) {
        val unboxingUrlCreator = UnboxingUrlCreator()
        val disposable = EasyHttp.get(unboxingUrlCreator.getUnboxingDataUrl())
            .execute(callBack)
        addDisposable(disposable)
    }

}