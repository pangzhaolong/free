package com.donews.mine.viewModel;

import android.content.Context;

import androidx.databinding.ViewDataBinding;

import com.donews.base.utils.ToastUtil;
import com.donews.base.viewmodel.BaseLiveDataViewModel;
import com.donews.common.contract.ApplyUpdateBean;
import com.donews.mine.databinding.MineActivityUserCancellationBinding;
import com.donews.mine.model.MineModel;
import com.donews.mine.model.SettingModel;
import com.donews.utilslibrary.utils.DeviceUtils;

public class UserCancellationViewModel extends BaseLiveDataViewModel<MineModel> {
    public Context mContext;
    private MineActivityUserCancellationBinding viewDataBinding;

    public void setDataBinDing(MineActivityUserCancellationBinding dataBinding) {
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
}
