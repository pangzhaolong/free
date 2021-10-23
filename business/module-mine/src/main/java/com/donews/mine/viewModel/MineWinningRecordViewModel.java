package com.donews.mine.viewModel;

import android.content.Context;

import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.MutableLiveData;

import com.donews.base.utils.ToastUtil;
import com.donews.base.viewmodel.BaseLiveDataViewModel;
import com.donews.common.contract.ApplyUpdateBean;
import com.donews.mine.bean.resps.WinRecordResp;
import com.donews.mine.model.MineModel;
import com.donews.utilslibrary.utils.DeviceUtils;

import java.util.List;

public class MineWinningRecordViewModel extends BaseLiveDataViewModel<MineModel> {
    public Context mContext;
    private ViewDataBinding viewDataBinding;

    //中奖记录的数据
    public MutableLiveData<List<WinRecordResp.ListDTO>> winRecordLivData = new MutableLiveData<>();

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

    /**
     * 加载列表数据
     * @param page
     * @param pageSize
     */
    public void loadDataList(int page, int pageSize) {
        mModel.requestWinRecord(winRecordLivData,page,pageSize);
    }

    public void updateLogic(ApplyUpdateBean applyUpdateBean) {
        if (applyUpdateBean.getVersion_code() <= DeviceUtils.getAppVersionCode()) {
            ToastUtil.show(mContext, "当前已是最新版本！");
        } else if (applyUpdateBean.getVersion_code() > DeviceUtils.getAppVersionCode()) {
        }
    }

}
