package com.donews.home.viewModel;

import androidx.lifecycle.MutableLiveData;

import com.donews.base.viewmodel.BaseLiveDataViewModel;
import com.donews.home.bean.DataBean;
import com.donews.home.bean.SearchResultTbBean;
import com.donews.home.bean.TopGoodsBean;
import com.donews.home.model.TbModel;
import com.donews.home.model.TopModel;

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


    public MutableLiveData<SearchResultTbBean> getSearchResultData(String keyWord) {
        return mModel.getSearchResultData(keyWord);
    }

}
