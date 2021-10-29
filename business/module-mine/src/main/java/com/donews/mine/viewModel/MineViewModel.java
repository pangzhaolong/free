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
        mModel.requestWithdraWallet(withdrawDatilesLivData);
    }

    /**
     * 加载精选推荐
     * @param limit 需要加载多少条数据
     */
    public void loadRecommendGoods(int limit){
        mModel.requestRecommendGoodsList(recommendGoodsLiveData,limit);
    }
}
