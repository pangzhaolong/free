package com.donews.mine.viewModel;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;

import com.blankj.utilcode.util.ConvertUtils;
import com.donews.base.utils.ToastUtil;
import com.donews.base.utils.glide.GlideUtils;
import com.donews.base.viewmodel.BaseLiveDataViewModel;
import com.donews.common.contract.ApplyUpdateBean;
import com.donews.mine.R;
import com.donews.mine.bean.CSBean;
import com.donews.mine.bean.emus.WinTypes;
import com.donews.mine.bean.resps.HistoryPeopleLotteryDetailResp;
import com.donews.mine.databinding.MineActivityWinningCodeBinding;
import com.donews.mine.model.MineModel;
import com.donews.mine.utils.TextWinUtils;
import com.donews.mine.views.CSSView;
import com.donews.utilslibrary.utils.DeviceUtils;
import com.donews.utilslibrary.utils.UrlUtils;

import java.util.ArrayList;
import java.util.List;

public class MineWinningCodeViewModel extends BaseLiveDataViewModel<MineModel> {
    public Context mContext;
    private MineActivityWinningCodeBinding viewDataBinding;

    /**
     * 中奖详情的结果参数
     */
    public MutableLiveData<HistoryPeopleLotteryDetailResp> detailLivData = new MutableLiveData<>();
    //城市区块的高度
    private int cidyLlHei = 0;

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

    /**
     * 获取数据
     *
     * @param period 期数
     */
    public void loadData(int period) {
        mModel.requestHistoryPeopleLootteryDetail(detailLivData, period);
    }

    /**
     * 更新其他数据
     *
     * @param view
     */
    public void updateData(View view) {
        char[] codeChid = detailLivData.getValue().code.toCharArray();
        //更新顶部数字
        ViewGroup numGP = view.findViewById(R.id.main_win_code_num_layout);
        for (int i = 0; i < numGP.getChildCount(); i++) {
            View numItemV = numGP.getChildAt(i);
            TextView numTv = numItemV.findViewById(R.id.icnl_mine_win_num);
            try {
                if (i < codeChid.length) {
                    numTv.setText(String.valueOf(codeChid[i]));
                }
            } catch (Exception e) {
            }
        }
        //更新城市数据
        CSSView cityV = view.findViewById(R.id.mine_win_code_city_v);
        List<CSBean> list = new ArrayList<>();
        for (HistoryPeopleLotteryDetailResp.SpeedsDTO speed : detailLivData.getValue().speeds) {
            list.add(new CSBean(speed.city,speed.speed));
        }
        cityV.refreshData(list);
        //城市区块操作
        LinearLayout cidyLl = view.findViewById(R.id.icnl_mine_win_city_ll);
        ImageView aollwIv = view.findViewById(R.id.mine_win_code_scan_allow);
        if(cidyLlHei <= 0){
            cidyLlHei = cidyLl.getHeight();
        }
        aollwIv.setOnClickListener(v -> {
            if(cidyLlHei <= 0){
                cidyLlHei = cidyLl.getHeight();
            }
            if(cidyLl.getVisibility() == View.VISIBLE){
                cidyLl.setVisibility(View.GONE);
                aollwIv.setRotation(0);
            }else{
                cidyLl.setVisibility(View.VISIBLE);
                aollwIv.setRotation(180);
            }
        });
    }

    /**
     * 添加中奖商品数据到视图
     *
     * @param view 视图对象
     */
    @SuppressLint("SetTextI18n")
    public void addSelectGoods(View view) {
        TextView statusName = view.findViewById(R.id.mine_win_code_win_desc_name);
        ViewGroup vGroup = view.findViewById(R.id.mine_win_code_win_desc_good_ll);
        vGroup.removeAllViews();
        if (detailLivData.getValue().myWin == null ||
                detailLivData.getValue().myWin.isEmpty()) {
            statusName.setText("很遗憾,你未中奖");
            View childView = View.inflate(view.getContext(), R.layout.incl_open_win_not_data, null);
            TextView desc = childView.findViewById(R.id.mine_open_win_not_data);
            TextView but = childView.findViewById(R.id.mine_open_win_not_data_but);
            desc.setText("抽奖多的人\n当然容易免单呀");
            but.setOnClickListener(v -> {
                ToastUtil.show(view.getContext(), "参与更多抽奖>>>");
            });
            //添加未中奖视图
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            vGroup.addView(childView, lp);
            return;
        }
        statusName.setText("恭喜你,获得大奖");
        for (int i = 0; i < detailLivData.getValue().myWin.size(); i++) {
            HistoryPeopleLotteryDetailResp.WinerDTO item = detailLivData.getValue().myWin.get(i);
            View childView = View.inflate(view.getContext(), R.layout.incl_mine_winning_record_list_good, null);
            //更新视图
            TextView seal = childView.findViewById(R.id.mine_win_item_seal);
            ImageView icon = childView.findViewById(R.id.mine_win_item_good_icon);
            TextView name = childView.findViewById(R.id.mine_win_item_good_name);
            TextView picre = childView.findViewById(R.id.mine_win_item_good_pic);
            TextView code = childView.findViewById(R.id.mine_win_item_snap_number);
            TextView goTo = childView.findViewById(R.id.mine_win_item_goto);
            //开始绑定数据
            seal.setVisibility(View.VISIBLE);
            if (WinTypes.Alike.type.equals(item.winType)) {
                seal.setText(WinTypes.Alike.name);
            } else if (WinTypes.Equal.type.equals(item.winType)) {
                seal.setText(WinTypes.Equal.name);
            } else {
                seal.setVisibility(View.GONE);
                seal.setText(WinTypes.None.name);
            }
            name.setText(item.goods.title);
            picre.setText("" + item.goods.price);
            GlideUtils.loadImageView(view.getContext(), UrlUtils.formatUrlPrefix(item.goods.image), icon);
            code.setText(Html.fromHtml(
                    TextWinUtils.drawOldText(detailLivData.getValue().code, item.code)));
            goTo.setText("去领奖");
            goTo.setOnClickListener((v) -> {
                ToastUtil.show(view.getContext(), "前往去领奖页面");
            });
            //添加视图
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.topMargin = ConvertUtils.dp2px(10);
            vGroup.addView(childView, lp);
        }
    }

    /**
     * 添加参与商品到容器
     *
     * @param view      视图对象
     * @param isInitAdd 是否初始添加，如果是则只添加3个默认，T:初始加载，F:加载更多
     */
    public void addAddToGoods(View view, boolean isInitAdd) {
        ViewGroup vGroup = view.findViewById(R.id.mine_win_code_win_add_good_ll);
        ViewGroup moreLoadView = view.findViewById(R.id.mine_win_code_win_add_good_more);
        List<HistoryPeopleLotteryDetailResp.WinerDTO> currentAddRecord = new ArrayList<>();
        int initLoadSize = 4;
        if (isInitAdd) {
            if (detailLivData.getValue().record != null &&
                    detailLivData.getValue().record.size() > initLoadSize) {
                for (int i = 0; i < initLoadSize; i++) {
                    currentAddRecord.add(detailLivData.getValue().record.get(i));
                }
                moreLoadView.setVisibility(View.VISIBLE);
            } else {
                if (detailLivData.getValue().record != null) {
                    currentAddRecord.addAll(detailLivData.getValue().record);
                }
                moreLoadView.setVisibility(View.GONE);
            }
            vGroup.removeAllViews();
        } else {
            if (detailLivData.getValue().record != null) {
                for (int i = initLoadSize; i < detailLivData.getValue().record.size(); i++) {
                    currentAddRecord.add(detailLivData.getValue().record.get(i));
                }
            }
            moreLoadView.setVisibility(View.GONE);
        }
        if (detailLivData.getValue().record == null ||
                detailLivData.getValue().record.isEmpty()) {
            return;
        }
        for (int i = 0; i < currentAddRecord.size(); i++) {
            HistoryPeopleLotteryDetailResp.WinerDTO item = currentAddRecord.get(i);
            View childView = View.inflate(view.getContext(), R.layout.incl_mine_winning_record_list_good, null);
            //更新视图
            TextView seal = childView.findViewById(R.id.mine_win_item_seal);
            ImageView icon = childView.findViewById(R.id.mine_win_item_good_icon);
            TextView name = childView.findViewById(R.id.mine_win_item_good_name);
            TextView picre = childView.findViewById(R.id.mine_win_item_good_pic);
            TextView code = childView.findViewById(R.id.mine_win_item_snap_number);
            TextView goTo = childView.findViewById(R.id.mine_win_item_goto);
            //开始绑定数据
            if (WinTypes.Alike.type.equals(item.winType)) {
                seal.setText(WinTypes.Alike.name);
            } else if (WinTypes.Equal.type.equals(item.winType)) {
                seal.setText(WinTypes.Equal.name);
            } else {
                seal.setVisibility(View.GONE);
                seal.setText(WinTypes.None.name);
            }
            name.setText(item.goods.title);
            picre.setText("" + item.goods.price);
            GlideUtils.loadImageView(view.getContext(), UrlUtils.formatUrlPrefix(item.goods.image), icon);
            code.setText(Html.fromHtml(
                    TextWinUtils.drawOldText(detailLivData.getValue().code, item.code)));
            goTo.setText("继续抽奖");
            goTo.setOnClickListener((v) -> {
                ToastUtil.show(view.getContext(), "去往继续抽奖页面");
            });
            //添加视图
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.topMargin = ConvertUtils.dp2px(10);
            vGroup.addView(childView, lp);
        }
    }

    /**
     * 添加中奖名单到容器
     *
     * @param view 视图对象
     */
    public void addSelectToNames(View view) {
        ViewGroup vGroup = view.findViewById(R.id.mine_win_code_sele_good_ll);
        vGroup.removeAllViews();
        if (detailLivData.getValue().winer == null) {
            return;
        }
        for (HistoryPeopleLotteryDetailResp.WinerDTO item : detailLivData.getValue().winer) {
            View childView = View.inflate(view.getContext(), R.layout.incl_mine_win_code_sele_item, null);
            TextView seal = childView.findViewById(R.id.main_win_code_sele_item_seal);
            ImageView headImg = childView.findViewById(R.id.main_win_code_sele_item_icon);
            TextView userName = childView.findViewById(R.id.main_win_code_sele_item_name);
            TextView winCode = childView.findViewById(R.id.main_win_code_sele_item_code);
            ImageView goodIcon = childView.findViewById(R.id.main_win_code_sele_good_icon);
            TextView goodName = childView.findViewById(R.id.main_win_code_sele_good_name);
            TextView goodPice = childView.findViewById(R.id.main_win_code_sele_good_pic);
            TextView goTo = childView.findViewById(R.id.main_win_code_sele_good_goto);
            //开始绑定数据
            if (WinTypes.Alike.type.equals(item.winType)) {
                seal.setText(WinTypes.Alike.name);
            } else if (WinTypes.Equal.type.equals(item.winType)) {
                seal.setText(WinTypes.Equal.name);
            } else {
                seal.setText(WinTypes.None.name);
            }
            GlideUtils.loadImageView(view.getContext(), UrlUtils.formatUrlPrefix(item.avatar), headImg);
            userName.setText(item.userName);
            winCode.setText(Html.fromHtml(
                    TextWinUtils.drawOldText(detailLivData.getValue().code, item.code)));
            GlideUtils.loadImageView(view.getContext(), UrlUtils.formatUrlPrefix(item.goods.image), goodIcon);
            goodName.setText(item.goods.title);
            goodPice.setText("" + item.goods.price);
            goTo.setOnClickListener((v) -> {
                ToastUtil.show(view.getContext(), "去往立即抢购");
            });
            //添加视图
            vGroup.addView(childView, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
    }
}
