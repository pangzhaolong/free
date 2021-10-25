package com.donews.mine.ui;

import static com.donews.common.router.RouterActivityPath.Mine.PAGER_MINE_WINNING_CODE_ACTIVITY;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.SPUtils;
import com.donews.base.activity.MvvmBaseLiveDataActivity;
import com.donews.common.router.RouterActivityPath;
import com.donews.mine.R;
import com.donews.mine.adapters.MineParticipateRecordAdapter;
import com.donews.mine.bean.resps.HistoryPeopleLottery;
import com.donews.mine.databinding.MineActivityParticipateRecordBinding;
import com.donews.mine.viewModel.MineParticipateRecordViewModel;
import com.gyf.immersionbar.ImmersionBar;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 参与记录
 */
@Route(path = RouterActivityPath.Mine.PAGER_PARTICIPATE_RECORD)
public class MineParticipateRecordActivity extends
        MvvmBaseLiveDataActivity<MineActivityParticipateRecordBinding, MineParticipateRecordViewModel> {

    //adapter的headView
    private ViewGroup adapterHead = null;
    //head中的每项数据的列表
    private int adapterHeadItemRes = R.layout.mine_participate_record_list_head_item;
    //适配器对象
    private MineParticipateRecordAdapter adapter;
    private boolean isRefresh = false;

    @Override
    protected int getLayoutId() {
        return R.layout.mine_activity_participate_record;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this)
                .statusBarColor(R.color.mine_f6f9fb)
                .navigationBarColor(R.color.white)
                .fitsSystemWindows(true)
                .autoDarkModeEnable(true)
                .init();
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initData();
    }

    public void initView() {
        mViewModel.setDataBinDing(mDataBinding);
        adapterHead = (ViewGroup) View.inflate(this, R.layout.mine_participate_record_list_head, null);
        adapter = new MineParticipateRecordAdapter();
        adapter.setOnLoadMoreListener((page, pageSize) -> {
            loadMoreListData();
        });
        mDataBinding.mineParRecodLayout.setRefeshOnListener(refreshLayout -> {
            refeshListData();
        });
        addListHead();
        mDataBinding.mineParRecodLayout.setLayoutManager(new GridLayoutManager(this, 2));
        mDataBinding.mineParRecodLayout.setAdapter(adapter);
        mDataBinding.mineParRecodBack.setOnClickListener((v) -> {
            finish();
        });
        mViewModel.peopleHistoryLiveData.observe(this, (items) -> {
            mDataBinding.mineParRecodLayout.setRefeshComplete();
            addListHeadData();
        });
        mViewModel.recommendGoodsLiveData.observe(this, listDTOS -> {
            if (isRefresh) {
                adapter.refeshFinish();
            }
            if (listDTOS == null || listDTOS.isEmpty()) {
                adapter.setNewData(new ArrayList<>());
            } else {
                adapter.setNewData(listDTOS);
            }
            adapter.loadMoreFinish(true, false);
            mDataBinding.mineParRecodLayout.getRefeshLayout().finishRefresh();
        });
        mDataBinding.mineParRecodLayout.getRefeshLayout().autoRefresh();
    }

    private void initData() {
    }

    //添加列表的记录头
    private void addListHead() {
        ViewGroup.LayoutParams lp = adapterHead.getLayoutParams();
        if (lp == null) {
            lp = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        adapterHead.setLayoutParams(lp);
        adapter.addHeaderView(adapterHead);
        //模拟更是记录数据
        addListHeadData();
    }

    //添加列表的数据
    private void addListHeadData() {
        //存放往期记录的容器
        ViewGroup recordListVP = adapterHead.findViewById(R.id.mine_par_record_layout);
        if (recordListVP.getChildCount() != 0) {
            recordListVP.removeAllViews();
        }
        if (mViewModel.peopleHistoryLiveData.getValue() == null ||
                mViewModel.peopleHistoryLiveData.getValue().isEmpty()) {
            recordListVP.removeAllViews();
            return;
        }
        if (mViewModel.peopleHistoryLiveData.getValue() == null ||
                mViewModel.peopleHistoryLiveData.getValue().isEmpty()) {
            adapterHead.findViewById(R.id.mine_open_win_not_data_ll).setVisibility(View.VISIBLE);
            return;
        }
        adapterHead.findViewById(R.id.mine_open_win_not_data_ll).setVisibility(View.GONE);
        //添加head的数据
        for (HistoryPeopleLottery.Period period : mViewModel.peopleHistoryLiveData.getValue()) {
            View itemView = getHeadItemView(period);
            recordListVP.addView(itemView,
                    new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            //设置文字
            TextView name = itemView.findViewById(R.id.mine_par_rec_ls_head_name);
            name.setText(period.period + "期");
        }
    }

    //获取head的头每项数据
    private View getHeadItemView(HistoryPeopleLottery.Period period) {
        View mItemView = View.inflate(this, adapterHeadItemRes, null);
        ImageView icon = mItemView.findViewById(R.id.mine_par_rec_ls_head_icon);
        TextView showFlg = mItemView.findViewById(R.id.mine_par_rec_ls_head_flg);
        if (period.opend) {
            //已经开奖
            icon.setVisibility(View.VISIBLE);
            icon.setImageResource(R.drawable.mine_win_open);
        } else {
            icon.setVisibility(View.INVISIBLE);
            //未开奖
        }
        if (SPUtils.getInstance().getInt("" + period.period, 0) == 0) {
            //未查看
            showFlg.setVisibility(View.VISIBLE);
            showFlg.setText("未查看");
        } else {
            //已查看
            showFlg.setVisibility(View.INVISIBLE);
            showFlg.setText("");
        }
        mItemView.setOnClickListener((v) -> {
            SPUtils.getInstance().put("" + period.period, 1);
            ARouter.getInstance().build(PAGER_MINE_WINNING_CODE_ACTIVITY)
                    .withInt("period", period.period)
                    .navigation();
            //直接将当前数据项目设置为已查看状态
            showFlg.setVisibility(View.INVISIBLE);
            showFlg.setText("");
        });
        return mItemView;
    }

    //下拉刷新数据
    private void refeshListData() {
        mViewModel.loadHistoryPeopleLottery();
        adapter.loadMoreFinish(true, true);
        isRefresh = true;
        adapter.refeshStart();
        mViewModel.loadRecommendGoods(25);
    }

    //上拉加载更多
    private void loadMoreListData() {
        isRefresh = false;
        mViewModel.loadRecommendGoods(adapter.getData().size() + 15);
    }

}