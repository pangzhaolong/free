package com.donews.mine.viewModel;

import android.content.Context;
import android.view.View;

import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.MutableLiveData;

import com.donews.base.popwindow.ConfirmPopupWindow;
import com.donews.base.utils.ToastUtil;
import com.donews.base.viewmodel.BaseLiveDataViewModel;
import com.donews.common.contract.ApplyUpdataBean;
import com.donews.common.contract.LoginHelp;
import com.donews.mine.model.SettingModel;
import com.donews.mine.ui.SettingActivity;
import com.donews.utilslibrary.utils.DeviceUtils;

public class SettingViewModel extends BaseLiveDataViewModel<SettingModel>{
    public Context mContext;
    private ViewDataBinding viewDataBinding;

    public void setDataBinDing(ViewDataBinding dataBinding) {
        this.viewDataBinding = dataBinding;
    }

    @Override
    public SettingModel createModel() {
        return new SettingModel();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }



//
//    @Override
//    public void onLoadFinish(BaseModel model, BaseCustomViewModel data) {
//        if (data instanceof CacheBean) {
//            viewDataBinding.setVariable(BR.cacheBean, data);
//        } else if (data instanceof SignBean) {
//            signBean = (SignBean) data;
//            viewDataBinding.setVariable(BR.signBean, data);
//        } else if (data instanceof ApplyUpdataBean) {
//            updateLogic((ApplyUpdataBean) data);
//        }
//    }

    public void updateLogic(ApplyUpdataBean applyUpdataBean) {
        if (applyUpdataBean.getVersion_code() <= DeviceUtils.getAppVersionCode()) {
            ToastUtil.show(mContext, "当前已是最新版本！");
        } else if (applyUpdataBean.getVersion_code() > DeviceUtils.getAppVersionCode()) {
        }
    }

    public void clearCache() {
        mModel.clearCache(mContext);
    }

    public void getCacheData() {
        mModel.getCacheData(mContext);
    }



}
