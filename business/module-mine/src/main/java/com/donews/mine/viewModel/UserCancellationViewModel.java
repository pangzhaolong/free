package com.donews.mine.viewModel;

import android.content.Context;

import com.donews.base.viewmodel.BaseLiveDataViewModel;
import com.donews.mine.databinding.MineActivityUserCancellationBinding;
import com.donews.mine.model.MineModel;

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
