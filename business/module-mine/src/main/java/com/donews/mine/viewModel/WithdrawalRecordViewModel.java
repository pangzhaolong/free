package com.donews.mine.viewModel;

import android.view.View;
import android.widget.GridLayout;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;

import com.donews.base.viewmodel.BaseLiveDataViewModel;
import com.donews.mine.R;
import com.donews.mine.bean.resps.WithdrawConfigResp;
import com.donews.mine.bean.resps.WithdrawRecordResp;
import com.donews.mine.model.MineModel;

import java.util.List;

public class WithdrawalRecordViewModel extends BaseLiveDataViewModel<MineModel> {
    private FragmentActivity baseActivity;
    private ViewDataBinding viewDataBinding;

    public MutableLiveData<List<WithdrawRecordResp.RecordListDTO>> recordListLiveData =
            new MutableLiveData<>();

    public void setDataBinDing(ViewDataBinding dataBinding, FragmentActivity baseActivity) {
        this.viewDataBinding = dataBinding;
        this.baseActivity = baseActivity;
    }

    @Override
    public MineModel createModel() {
        return new MineModel();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    /**
     * 加载数据
     * @param offset
     * @param limit
     */
    public void loadRecordList(int offset,int limit){
        mModel.requestWithdrawRecord(recordListLiveData,offset,limit);
    }

}
