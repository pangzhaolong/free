package com.donews.mine.viewModel;

import android.content.Context;

import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;

import com.donews.base.utils.ToastUtil;
import com.donews.base.viewmodel.BaseLiveDataViewModel;
import com.donews.mine.databinding.MineSettingFragmentBinding;
import com.donews.mine.model.SettingModel;
import com.donews.utilslibrary.utils.DeviceUtils;

public class SettingFragmentViewModel extends BaseLiveDataViewModel<SettingModel>{
    private ViewDataBinding viewDataBinding;
    public LifecycleOwner lifecycleOwner;
    private FragmentActivity baseActivity;

    public void setDataBinDing(MineSettingFragmentBinding dataBinding, FragmentActivity baseActivity) {
        this.viewDataBinding = dataBinding;
        this.baseActivity = baseActivity;
        dataBinding.setListener(this);
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

    public void clearCache() {
        mModel.clearCache(baseActivity);
    }

    public void getCacheData() {
        mModel.getCacheData(baseActivity);
    }



}
