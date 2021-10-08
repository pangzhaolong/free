package com.donews.mine.viewModel;

import android.content.Context;

import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.MutableLiveData;

import com.donews.base.viewmodel.BaseLiveDataViewModel;
import com.donews.common.contract.LoginHelp;
import com.donews.common.contract.UserInfoBean;
import com.donews.mine.model.UserInfoModel;

public class UserInfoViewModel extends BaseLiveDataViewModel<UserInfoModel> {
    private Context context;
    private ViewDataBinding viewDataBinding;


    public MutableLiveData<UserInfoBean> getUserInfoData() {
        MutableLiveData<UserInfoBean> liveData = new MutableLiveData<>();
        if (!LoginHelp.getInstance().isLogin()) {
            UserInfoBean userInfoBean = LoginHelp.getInstance().getUserInfoBean();
            if (userInfoBean != null) {
                liveData.postValue(userInfoBean);

            }
        }
        return liveData;
    }

    public void setDataBinDing(ViewDataBinding dataBinding) {
        this.viewDataBinding = dataBinding;
    }


    public void bindWeChat() {
        mModel.bindWeChat();
    }

    @Override
    public UserInfoModel createModel() {
        return new UserInfoModel();
    }
}
