package com.donews.home.viewModel;

import androidx.lifecycle.MutableLiveData;

import com.donews.base.viewmodel.BaseLiveDataViewModel;
import com.donews.home.model.HomeModel;
import com.donews.middle.bean.home.HomeCategoryBean;
import com.donews.middle.bean.home.SecKilBean;
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


    public MutableLiveData<HomeCategoryBean> getNetHomeData() {
        return mModel.getNetData();
    }

    public MutableLiveData<SecKilBean> getSecKilData() {
        return mModel.getSecKilData();
    }

    public MutableLiveData<UserBean> getUserList() {
        return mModel.getUserList();
    }
}
