package com.donews.mine.viewModel;

import android.view.View;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.SPUtils;
import com.donews.base.utils.GsonUtils;
import com.donews.base.utils.ToastUtil;
import com.donews.base.viewmodel.BaseLiveDataViewModel;
import com.donews.mine.R;
import com.donews.mine.bean.resps.WithdraWalletResp;
import com.donews.mine.bean.resps.WithdrawConfigResp;
import com.donews.mine.model.MineModel;

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

    //提现的结果
    public MutableLiveData<Integer> withdrawLivData =
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
     *
     * @param isNetLoad 是否网络加载
     */
    public void getLoadWithdrawData(boolean isNetLoad) {
        try {
            if (isNetLoad) {
                mModel.requestWithdrawCenterConfig(withdrawDataLivData);
                return;
            }
            String locJson = SPUtils.getInstance().getString("withdraw_config");
            WithdrawConfigResp queryBean = GsonUtils.fromLocalJson(locJson, WithdrawConfigResp.class);
            if (!(queryBean == null || queryBean.list == null || queryBean.list.isEmpty())) {
                //存在缓存先加载。再更新网络
                withdrawDataLivData.postValue(queryBean.list);
            }
            //刷新一次数据(但是下次才会生效)
            mModel.requestWithdrawCenterConfig(withdrawDataLivData);
        } catch (Exception err) {
            mModel.requestWithdrawCenterConfig(withdrawDataLivData);
        }
    }

    /**
     * 获取钱包详情
     */
    public void getLoadWithdraWalletDite() {
        mModel.requestWithdraWallet(withdrawDatilesLivData);
    }

    /**
     * 提现
     */
    public void requestWithdraw(GridLayout superLayout) {
        mModel.requestWithdra(withdrawLivData, getGridSelectViewItem(superLayout), baseActivity);
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
     * 添加网格数据
     *
     * @param gridLayout
     * @param submit     提交按钮
     * @param desc       描述
     */
    public void addGridDatas(@NonNull GridLayout gridLayout, TextView submit, TextView desc) {
        gridLayout.removeAllViews();
        if (withdrawDataLivData.getValue() == null) {
            return;
        }
        for (int i = 0; i < withdrawDataLivData.getValue().size(); i++) {
            getGridItemView(i, gridLayout, withdrawDataLivData.getValue().get(i), submit, desc);
        }
    }

    /**
     * 获取ItemView
     *
     * @param pos    当前的数据下标
     * @param item   当前数据
     * @param submit 提交按钮
     * @return
     */
    /**
     * 获取ItemView
     *
     * @param pos    当前的数据下标
     * @param item   当前数据
     * @param submit 提交按钮
     * @return
     */
    private View getGridItemView(
            int pos, GridLayout superLayout,
            WithdrawConfigResp.WithdrawListDTO item,
            TextView submit,
            TextView desc) {
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
        view.setOnClickListener(v -> {
            boolean isHostSelect = false; //是否自己被选中
            for (int i = 0; i < superLayout.getChildCount(); i++) {
                WithdrawConfigResp.WithdrawListDTO curItem =
                        (WithdrawConfigResp.WithdrawListDTO) superLayout.getChildAt(i)
                                .getTag(R.id.icnl_mine_withdraw_num);
                if ("1".equals(superLayout.getChildAt(i).getTag(R.id.icnl_mine_withdraw_fl).toString()) &&
                        superLayout.getChildAt(i) == v) {
                    isHostSelect = true;
                }
                superLayout.getChildAt(i).setTag(R.id.icnl_mine_withdraw_fl, "0");
                superLayout.getChildAt(i).setBackgroundResource(notSelectBgRes);
            }
            //检查是否点击的自己
            if (!isHostSelect) {
                submit.setEnabled(true);
                v.setTag(R.id.icnl_mine_withdraw_fl, "1");
                v.setBackgroundResource(selectBgRes);
            } else {
                v.setTag(R.id.icnl_mine_withdraw_fl, "0");
                v.setBackgroundResource(notSelectBgRes);
                submit.setEnabled(false);
            }
            if (withdrawDatilesLivData.getValue() == null) {
                ToastUtil.showShort(baseActivity, "钱包信息获取异常");
                submit.setEnabled(false);
                return; //钱包信息获取异常
            }
            if (withdrawDatilesLivData.getValue().total < item.money) {
                desc.setText("");
                submit.setEnabled(false);
                return; //余额不足
            }
            if (!item.available) {
                desc.setText(item.tips);
                submit.setEnabled(false);
            } else {
                desc.setText("");
                submit.setEnabled(!isHostSelect);
            }
        });
        //绑定数据
        TextView numTv = view.findViewById(R.id.icnl_mine_withdraw_num);
        TextView newUserZx = view.findViewById(R.id.icnl_mine_withdraw_new_uf);
        numTv.setText("" + item.money);
        //添加视图
        superLayout.addView(view);
        if (0.3 == item.money) {
            newUserZx.setVisibility(View.VISIBLE);
        } else {
            newUserZx.setVisibility(View.GONE);
        }
        return view;
    }
}
