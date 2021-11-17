package com.donews.home.viewModel;

import androidx.lifecycle.MutableLiveData;

import com.donews.base.viewmodel.BaseLiveDataViewModel;
import com.donews.home.model.TbModel;
import com.donews.middle.bean.home.SearchGoodsBeanV2;
import com.donews.middle.bean.home.SearchResultTbBean;

/**
 * <p> </p>
 * 作者： created by dw<br>
 * 日期： 2020/12/7 10:59<br>
 * 版本：V1.0<br>
 */
public class TbViewModel extends BaseLiveDataViewModel<TbModel> {

    @Override
    public TbModel createModel() {
        return new TbModel();
    }


    public MutableLiveData<SearchGoodsBeanV2> getSearchResultData(String keyWord, int pageId, int src) {
        return mModel.getSearchResultData(keyWord, pageId, src);
    }

}
