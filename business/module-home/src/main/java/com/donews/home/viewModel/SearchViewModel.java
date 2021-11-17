package com.donews.home.viewModel;

import androidx.lifecycle.MutableLiveData;

import com.donews.base.viewmodel.BaseLiveDataViewModel;
import com.donews.home.model.SearchModel;
import com.donews.middle.bean.home.HomeGoodsBean;
import com.donews.middle.bean.home.SearchSugBean;

/**
 * <p> </p>
 * 作者： created by honeylife<br>
 * 日期： 2020/12/7 10:59<br>
 * 版本：V1.0<br>
 */
public class SearchViewModel extends BaseLiveDataViewModel<SearchModel> {

    @Override
    public SearchModel createModel() {
        return new SearchModel();
    }

    public MutableLiveData<SearchSugBean> getSearchData(String search) {
        return mModel.getSearchData(search);
    }

    public MutableLiveData<HomeGoodsBean> getBuysData(int pageId) {
        return mModel.getBuysData(pageId);
    }
}
