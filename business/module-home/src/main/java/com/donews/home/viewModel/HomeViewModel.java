package com.donews.home.viewModel;

import androidx.lifecycle.MutableLiveData;

import com.donews.base.viewmodel.BaseLiveDataViewModel;
import com.donews.home.model.HomeModel;
import com.donews.middle.bean.home.FactorySaleBean;
import com.donews.middle.bean.home.HomeCategoryBean;
import com.donews.middle.bean.home.RealTimeBean;
import com.donews.middle.bean.home.SecKilBean;
import com.donews.middle.bean.home.TopIconsBean;
import com.donews.middle.bean.home.UserBean;

/**
 * <p> </p>
 * 作者： created by dw<br>
 * 日期： 2020/12/7 10:59<br>
 * 版本：V1.0<br>
 */
public class HomeViewModel extends BaseLiveDataViewModel<HomeModel> {

    @Override
    public HomeModel createModel() {
        return new HomeModel();
    }


    public MutableLiveData<HomeCategoryBean> getHomeCategoryBean() {
        return mModel.getHomeCategoryBean();
    }


    public MutableLiveData<RealTimeBean> getRankListData(int pageId) {
        return mModel.getRankListData(pageId);
    }


    public MutableLiveData<SecKilBean> getSecKilData() {
        return mModel.getSecKilData();
    }


    public MutableLiveData<FactorySaleBean> getFactorySale() {
        return mModel.getFactorySale();
    }


    public MutableLiveData<UserBean> getUserList() {
        return mModel.getUserList();
    }

    public MutableLiveData<TopIconsBean> getTopIcons() {
        return mModel.getTopIcons();
    }
}
