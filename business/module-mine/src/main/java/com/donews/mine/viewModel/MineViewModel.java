package com.donews.mine.viewModel;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.SPUtils;
import com.donews.base.utils.GsonUtils;
import com.donews.base.viewmodel.BaseLiveDataViewModel;
import com.donews.common.contract.LoginHelp;
import com.donews.common.router.RouterActivityPath;
import com.donews.mine.Api.MineHttpApi;
import com.donews.mine.bean.QueryBean;
import com.donews.mine.bean.resps.RecommendGoodsResp;
import com.donews.mine.bean.resps.WithdraWalletResp;
import com.donews.mine.bean.resps.WithdrawConfigResp;
import com.donews.mine.databinding.MineFragmentBinding;
import com.donews.mine.model.MineModel;
import com.donews.mine.ui.InvitationCodeActivity;
import com.donews.mine.ui.SettingActivity;
import com.donews.mine.ui.UserCenterActivity;
import com.donews.utilslibrary.utils.BaseToast;
import com.donews.utilslibrary.utils.LogUtil;

import java.util.List;

/**
 * <p> </p>
 * 作者： created by honeylife<br>
 * 日期： 2020/11/16 15:30<br>
 * 版本：V1.0<br>
 */
public class MineViewModel extends BaseLiveDataViewModel<MineModel> {


    public LifecycleOwner lifecycleOwner;
    private MineFragmentBinding dataBinding;
    private FragmentActivity baseActivity;

    public MutableLiveData<List<RecommendGoodsResp.ListDTO>> recommendGoodsLiveData= new MutableLiveData<>();

    //钱包详情
    public MutableLiveData<WithdraWalletResp> withdrawDatilesLivData =
            new MutableLiveData<>();


    @Override
    public MineModel createModel() {
        return new MineModel();
    }

    public void setDataBinDing(MineFragmentBinding dataBinding, FragmentActivity baseActivity) {
        this.dataBinding = dataBinding;
        this.baseActivity = baseActivity;
    }

    /**
     * 钱包详情
     */
    public void getLoadWithdrawData() {
        String locJson = SPUtils.getInstance().getString("withdraw_detail");
        try {
            WithdraWalletResp queryBean = GsonUtils.fromLocalJson(locJson, WithdraWalletResp.class);
            if (queryBean == null) {
                withdrawDatilesLivData.postValue(queryBean);
            }
        } catch (Exception err) {
        }
        mModel.requestWithdraWallet(withdrawDatilesLivData);
        mModel.requestWithdrawCenterConfig(null);
    }

    /**
     * 加载精选推荐
     * @param limit 需要加载多少条数据
     */
    public void loadRecommendGoods(int limit){
        mModel.requestRecommendGoodsList(recommendGoodsLiveData,limit);
    }
}
