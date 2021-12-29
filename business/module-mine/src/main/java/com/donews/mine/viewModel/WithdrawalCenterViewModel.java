package com.donews.mine.viewModel;

import static androidx.annotation.Dimension.SP;

import android.util.TypedValue;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.SPUtils;
import com.dn.sdk.bean.integral.ProxyIntegral;
import com.dn.sdk.utils.IntegralComponent;
import com.donews.base.utils.GsonUtils;
import com.donews.base.utils.ToastUtil;
import com.donews.base.viewmodel.BaseLiveDataViewModel;
import com.donews.middle.bean.HighValueGoodsBean;
import com.donews.middle.bean.front.WinningRotationBean;
import com.donews.middle.cache.GoodsCache;
import com.donews.middle.request.RequestUtil;
import com.donews.mine.R;
import com.donews.mine.bean.resps.WithdraWalletResp;
import com.donews.mine.bean.resps.WithdrawConfigResp;
import com.donews.mine.model.MineModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class WithdrawalCenterViewModel extends BaseLiveDataViewModel<MineModel> {
    private FragmentActivity baseActivity;
    private ViewDataBinding viewDataBinding;

    //是否存在积分任务
    public boolean isExitesIntegralTask = false;

    //提现中心的配置，金额列表
    public MutableLiveData<List<WithdrawConfigResp.WithdrawListDTO>> withdrawDataLivData =
            new MutableLiveData<>();

    //钱包详情
    public MutableLiveData<WithdraWalletResp> withdrawDatilesLivData =
            new MutableLiveData<>();

    /**
     * 提现页面的地步横向滚动数据
     * null = 获取错误
     */
    public MutableLiveData<WinningRotationBean> awardScrollDataLiveData =
            new MutableLiveData<>(null);

    //提现的结果
    public MutableLiveData<Integer> withdrawLivData =
            new MutableLiveData<>();

    //当前请求的操作对象
    public WithdrawConfigResp.WithdrawListDTO withdrawSelectDto = null;

    //是否正在提现中
    public boolean isWithdrawLoading = false;

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
     * 获取地步滚动的通知
     */
    public void getWiningRotation(){
        mModel.getWiningRotation(awardScrollDataLiveData);
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
     * 更新积分墙任务
     */
    public void updateIntegralTask() {
        //获取积分墙任务
        IntegralComponent.getInstance().getIntegral(new IntegralComponent.IntegralHttpCallBack() {
            @Override
            public void onSuccess(ProxyIntegral integralBean) {
                isExitesIntegralTask = true;
                getLoadWithdraWalletDite();
                getLoadWithdrawData(false);
            }

            @Override
            public void onError(String var1) {
                isExitesIntegralTask = false;
                getLoadWithdraWalletDite();
                getLoadWithdrawData(false);
            }

            @Override
            public void onNoTask() {
                isExitesIntegralTask = false;
                getLoadWithdraWalletDite();
                getLoadWithdrawData(false);
            }
        });
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
        if (isWithdrawLoading) {
            return;
        }
        if (getGridSelectViewItem(superLayout) == null) {
            ToastUtil.showShort(superLayout.getContext(), "请选择提金额");
            return;
        }
        isWithdrawLoading = true;
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
     * 获取一个商品
     *
     * @return
     */
    public HighValueGoodsBean.GoodsInfo getGoodInfo() {
        HighValueGoodsBean bean = GoodsCache.readGoodsBean(HighValueGoodsBean.class, "exit");
        if (bean.getList() != null && bean.getList().size() > 0) {
            RequestUtil.requestHighValueGoodsInfo();
            return bean.getList().get(0);
        } else {
            RequestUtil.requestHighValueGoodsInfo();
            return null;
        }
    }

    /**
     * 检查本地是否有商品
     *
     * @return T:存在，F:不存在
     */
    public Boolean checkGoodInfo() {
        HighValueGoodsBean bean = GoodsCache.readGoodsBean(HighValueGoodsBean.class, "exit");
        if (bean == null) {
            RequestUtil.requestHighValueGoodsInfo();
        }
        return bean != null;
    }


    /**
     * 添加网格数据
     *
     * @param gridLayout
     * @param submit     提交按钮
     * @param descll     描述的容器
     */
    public void addGridDatas(@NonNull GridLayout gridLayout, TextView submit, ConstraintLayout descll) {
        gridLayout.removeAllViews();
        if (withdrawDataLivData.getValue() == null) {
            return;
        }
        //筛选一次数据
        List<WithdrawConfigResp.WithdrawListDTO> newAddList = new ArrayList<>();
        for (int i = 0; i < withdrawDataLivData.getValue().size(); i++) {
            WithdrawConfigResp.WithdrawListDTO item = withdrawDataLivData.getValue().get(i);
            if(item.external){ //判断是否为积分任务项目
                //判断是否为随机项目
                boolean isRomanItem = item.money <= 0;
                if(!(isRomanItem && isExitesIntegralTask)){
                    continue; //是随机金额项目。但是当前没有任务,所以需要隐藏随机金额项
                }
            }
            newAddList.add(item);
        }
        for (int i = 0; i < newAddList.size(); i++) {
            getGridItemView(i, gridLayout, newAddList.get(i), submit, descll);
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
            ConstraintLayout descLL) {
        int notClickBgRes = R.drawable.mine_withdrawal_momy_item_enable_bg;
        int notSelectBgRes = R.drawable.ad_shape_min_bg;
        int selectBgRes = R.drawable.mine_withdrawal_momy_item_bg;
        //构建UI
        int col = pos % 3; //列
        View view = View.inflate(baseActivity, R.layout.incl_mine_withdrawal_number, null);
        GridLayout.LayoutParams glp = new GridLayout.LayoutParams();
        glp.columnSpec = GridLayout.spec(col, 1, 3F);
        glp.height = ConvertUtils.dp2px(83);
        glp.width = ConvertUtils.dp2px(103);
        view.setLayoutParams(glp);
        view.setTag(R.id.icnl_mine_withdraw_num, item); //绑定数据
        view.setTag(R.id.icnl_mine_withdraw_fl, "0"); //设置未选中默认
        //组件
        TextView numTv = view.findViewById(R.id.icnl_mine_withdraw_num);
        TextView numTvFlg = view.findViewById(R.id.icnl_mine_withdraw_fl);
        TextView newUserZx = view.findViewById(R.id.icnl_mine_withdraw_new_uf);
        ImageView userBFlg = view.findViewById(R.id.icnl_mine_withdraw_jb);
        TextView desc = descLL.findViewById(R.id.mine_draw_desc);
        view.setOnClickListener(v -> {
            submit.setText("立即提现");
            //情况内容区域的点击
            descLL.setOnClickListener(null);
            if (isWithdrawLoading) {
                ToastUtil.showShort(v.getContext(), "正在提现中,请稍后重试");
                return;//提现中。不允许点击
            }
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
                superLayout.getChildAt(i)
                        .findViewById(R.id.icnl_mine_withdraw_new_uf)
                        .setVisibility(View.GONE);
                superLayout.getChildAt(i)
                        .findViewById(R.id.icnl_mine_withdraw_jb)
                        .setVisibility(View.GONE);
            }
            //检查是否点击的自己
            if (!isHostSelect) {
                newUserZx.setVisibility(View.VISIBLE);
                userBFlg.setVisibility(View.VISIBLE);
                submit.setEnabled(true);
                v.setTag(R.id.icnl_mine_withdraw_fl, "1");
                v.setBackgroundResource(selectBgRes);
                withdrawSelectDto = (WithdrawConfigResp.WithdrawListDTO) v.getTag(R.id.icnl_mine_withdraw_num);
            } else {
                newUserZx.setVisibility(View.GONE);
                userBFlg.setVisibility(View.GONE);
                v.setTag(R.id.icnl_mine_withdraw_fl, "0");
                v.setBackgroundResource(notSelectBgRes);
                submit.setEnabled(false);
                withdrawSelectDto = null;
            }
            newUserZx.setBackgroundResource(R.drawable.mine_new_user_rw_bg);
            userBFlg.setImageResource(R.drawable.mine_tx_b_jb_red);
            newUserZx.setText("任务");
            if (!item.external) {
                //如果是正常项目的话。才走正常的逻辑判断
                if (withdrawDatilesLivData.getValue() == null) {
                    ToastUtil.showShort(baseActivity, "钱包信息获取异常");
                    submit.setEnabled(false);
                    return; //钱包信息获取异常
                }
                if (withdrawDatilesLivData.getValue().total < item.money) {
                    desc.setText("余额不足");
                    descLL.setVisibility(View.VISIBLE);
                    if (checkGoodInfo()) {
                        submit.setEnabled(true);
                        submit.setText("抽奖开红包");
                    } else {
                        submit.setEnabled(false);
                        submit.setText("立即提现");
                    }
                    return; //余额不足
                }
                if (!item.available) {
                    desc.setText(item.tips);
                    descLL.setVisibility(View.VISIBLE);
                    submit.setEnabled(false);
                } else {
                    newUserZx.setBackgroundResource(R.drawable.mine_new_user_zx_bg);
                    userBFlg.setImageResource(R.drawable.mine_tx_b_jb);
                    newUserZx.setText("已解锁");
                    desc.setText("");
                    descLL.setVisibility(View.INVISIBLE);
                    submit.setEnabled(!isHostSelect);
                }
            } else {
                //随机金额项点击
                descLL.setVisibility(View.INVISIBLE);
                submit.setEnabled(true);
                submit.setText("抽奖开红包");
                submit.setEnabled(!isHostSelect);
            }
        });
        //绑定数据
        if (item.external) {
            numTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            numTv.setText("随机金额");
            numTvFlg.setText("");
        } else {
            numTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
            numTv.setText("" + item.money);
            numTvFlg.setText("元");
        }
        if (pos == 0) {
            //默认选中第一个
            view.performClick();
        }
        //添加视图
        superLayout.addView(view);
        return view;
    }
}
