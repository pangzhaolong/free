package com.donews.home.viewModel;

import androidx.lifecycle.MutableLiveData;

import com.donews.base.viewmodel.BaseLiveDataViewModel;
import com.donews.home.model.WelfareModel;
import com.donews.middle.bean.home.HomeGoodsBean;
import com.donews.middle.bean.home.SearchSugBean;

/**
 * <p> </p>
 * 作者： created by honeylife<br>
 * 日期： 2020/12/7 10:59<br>
 * 版本：V1.0<br>
 */
public class WelfareViewModel extends BaseLiveDataViewModel<WelfareModel> {

    @Override
    public WelfareModel createModel() {
        return new WelfareModel();
    }

    public MutableLiveData<HomeGoodsBean> getPerfectGoodsData(String from, int pageId) {
        return mModel.getPerfectGoodsData(from, pageId);
    }

    public MutableLiveData<SearchSugBean> getSearchData(String keyWord) {
        return mModel.getSearchData(keyWord);
    }

    public MutableLiveData<HomeGoodsBean> getSearchListData(int pageId, String keyWord, int src) {
        return mModel.getSearchListData(pageId, keyWord, src);
    }
}
