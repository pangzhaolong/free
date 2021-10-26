package com.donews.mine.viewModel;

import android.content.Context;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;

import com.blankj.utilcode.util.ConvertUtils;
import com.donews.base.utils.ToastUtil;
import com.donews.base.viewmodel.BaseLiveDataViewModel;
import com.donews.common.contract.ApplyUpdateBean;
import com.donews.common.contract.LoginHelp;
import com.donews.mine.R;
import com.donews.mine.bean.resps.WithdraWalletResp;
import com.donews.mine.bean.resps.WithdrawConfigResp;
import com.donews.mine.model.MineModel;
import com.donews.mine.model.SettingModel;
import com.donews.utilslibrary.utils.DeviceUtils;

import java.util.List;

public class WithdrawalCenterViewModel extends BaseLiveDataViewModel<MineModel> {
    private FragmentActivity baseActivity;
    private ViewDataBinding viewDataBinding;

    //提现中心的配置，金额列表
    public MutableLiveData<List<WithdrawConfigResp.WithdrawListDTO>> withdrawDataLivData =
            new MutableLiveData<>();

    //钱包详情
    public MutableLiveData<WithdraWalletResp> withdrawDatilesLivData =
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
     * 获取提现中心的配置
     */
    public void getLoadWithdrawData() {
        mModel.requestWithdrawCenterConfig(withdrawDataLivData);
    }

    /**
     * 获取钱包详情
     */
    public void getLoadWithdraWalletDite() {
        mModel.requestWithdraWallet(withdrawDatilesLivData);
    }

    /**
     * 添加网格数据
     *
     * @param gridLayout
     */
    public void addGridDatas(@NonNull GridLayout gridLayout) {
        gridLayout.removeAllViews();
        if (withdrawDataLivData.getValue() == null) {
            return;
        }
        for (int i = 0; i < withdrawDataLivData.getValue().size(); i++) {
            getGridItemView(i, gridLayout, withdrawDataLivData.getValue().get(i));
        }
    }

    /**
     * 获取当前容器下选中的项目所对应的数据
     *
     * @param superLayout grid容器
     * @return
     */
    public WithdrawConfigResp.WithdrawListDTO getGridSelectViewItem(GridLayout superLayout) {
        for (int i = 0; i < superLayout.getChildCount(); i++) {
            if ("1".equals(superLayout.getChildAt(i).getTag(R.id.icnl_mine_withdraw_fl).toString())) {
                return (WithdrawConfigResp.WithdrawListDTO) superLayout.getChildAt(i)
                        .getTag(R.id.icnl_mine_withdraw_num);
            }
        }
        return null; //没有选中项
    }

    /**
     * 获取ItemView
     *
     * @param pos  当前的数据下标
     * @param item 当前数据
     * @return
     */
    private View getGridItemView(int pos, GridLayout superLayout, WithdrawConfigResp.WithdrawListDTO item) {
        int notClickBgRes = R.drawable.mine_withdrawal_momy_item_enable_bg;
        int notSelectBgRes = R.drawable.ad_shape_min_bg;
        int selectBgRes = R.drawable.mine_withdrawal_momy_item_bg;
        //构建UI
        int col = pos % 3; //列
        View view = View.inflate(baseActivity, R.layout.incl_mine_withdrawal_number, null);
        GridLayout.LayoutParams glp = new GridLayout.LayoutParams();
        glp.columnSpec = GridLayout.spec(col, 1, 3F);
        glp.height = ConvertUtils.dp2px(83);
        view.setLayoutParams(glp);
        view.setTag(R.id.icnl_mine_withdraw_num, item); //绑定数据
        view.setTag(R.id.icnl_mine_withdraw_fl, "0"); //设置未选中默认
        if (item.available) {
            view.setOnClickListener(v -> {
                if (!item.available) {
                    ToastUtil.showShort(v.getContext(), item.tips);
                    return; //不可提现
                }
                boolean isHostSelect = false; //是否自己被选中
                for (int i = 0; i < superLayout.getChildCount(); i++) {
                    WithdrawConfigResp.WithdrawListDTO curItem =
                            (WithdrawConfigResp.WithdrawListDTO) superLayout.getChildAt(i)
                                    .getTag(R.id.icnl_mine_withdraw_num);
                    if (!curItem.available) {
                        continue; //跳过不可提现部分
                    }
                    if ("1".equals(superLayout.getChildAt(i).getTag(R.id.icnl_mine_withdraw_fl).toString())) {
                        isHostSelect = true;
                    }
                    superLayout.getChildAt(i).setTag(R.id.icnl_mine_withdraw_fl, "0");
                    superLayout.getChildAt(i).setBackgroundResource(notSelectBgRes);
                }
                if (!isHostSelect) {
                    v.setTag(R.id.icnl_mine_withdraw_fl, "1");
                    v.setBackgroundResource(selectBgRes);
                }else{
                    v.setTag(R.id.icnl_mine_withdraw_fl, "0");
                    v.setBackgroundResource(notSelectBgRes);
                }
            });
        } else {
            view.setBackgroundResource(notClickBgRes);
            view.setOnClickListener(v -> {
                ToastUtil.showShort(v.getContext(), item.tips);
            });
        }
        //绑定数据
        TextView numTv = view.findViewById(R.id.icnl_mine_withdraw_num);
        numTv.setText("" + item.money);
        //添加视图
        superLayout.addView(view);
        return view;
    }
}
