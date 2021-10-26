package com.donews.home.viewModel;

import androidx.lifecycle.MutableLiveData;

import com.donews.base.viewmodel.BaseLiveDataViewModel;
import com.donews.home.bean.HomeBean;
import com.donews.home.bean.SecKilBean;
import com.donews.home.bean.UserBean;
import com.donews.home.model.HomeModel;

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


    public MutableLiveData<HomeBean> getNetHomeData() {
        return mModel.getNetData();
    }

    public MutableLiveData<SecKilBean> getSecKilData() {
        return mModel.getSecKilData();
    }

    public MutableLiveData<UserBean> getUserList() {
        return mModel.getUserList();
    }
}
