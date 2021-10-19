package com.donews.mine.viewModel;

import android.content.Context;

import androidx.databinding.ViewDataBinding;

import com.donews.base.utils.ToastUtil;
import com.donews.base.viewmodel.BaseLiveDataViewModel;
import com.donews.common.contract.ApplyUpdateBean;
import com.donews.mine.model.MineModel;
import com.donews.mine.model.SettingModel;
import com.donews.utilslibrary.utils.DeviceUtils;

public class MineParticipateRecordViewModel extends BaseLiveDataViewModel<MineModel> {
    public Context mContext;
    private ViewDataBinding viewDataBinding;

    public void setDataBinDing(ViewDataBinding dataBinding) {
        this.viewDataBinding = dataBinding;
    }

    @Override
    public MineModel createModel() {
        return new MineModel();
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

    public void updateLogic(ApplyUpdateBean applyUpdateBean) {
        if (applyUpdateBean.getVersion_code() <= DeviceUtils.getAppVersionCode()) {
            ToastUtil.show(mContext, "当前已是最新版本！");
        } else if (applyUpdateBean.getVersion_code() > DeviceUtils.getAppVersionCode()) {
        }
    }

}
