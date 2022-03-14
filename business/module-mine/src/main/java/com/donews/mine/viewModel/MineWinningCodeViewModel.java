package com.donews.mine.viewModel;

import android.content.Context;

import androidx.fragment.app.FragmentActivity;
import com.donews.base.viewmodel.BaseLiveDataViewModel;
import com.donews.mine.databinding.MineActivityWinningCodeBinding;
import com.donews.mine.model.MineModel;

public class MineWinningCodeViewModel extends BaseLiveDataViewModel<MineModel> {
    public Context mContext;
    private MineActivityWinningCodeBinding viewDataBinding;

    public void setDataBinDing(MineActivityWinningCodeBinding dataBinding, FragmentActivity act) {
        this.viewDataBinding = dataBinding;
        this.mContext = act;
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
