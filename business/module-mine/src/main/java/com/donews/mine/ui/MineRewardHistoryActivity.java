package com.donews.mine.ui;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.donews.common.base.MvvmBaseLiveDataActivity;
import com.donews.common.router.RouterActivityPath;
import com.donews.mine.R;
import com.donews.mine.adapters.MineRewarHistoryAdapter;
import com.donews.mine.databinding.MineRewardHistoryActivityBinding;
import com.donews.mine.listener.RewardItemClickListener;
import com.donews.mine.viewModel.MineRewardHistoryViewModel;
import com.donews.utilslibrary.analysis.AnalysisUtils;
import com.donews.utilslibrary.dot.Dot;
import com.gyf.immersionbar.ImmersionBar;

/**
 * 往期开奖
 */
@Route(path = RouterActivityPath.Mine.PAGE_MINE_REWARD_HISTORY)
public class MineRewardHistoryActivity extends MvvmBaseLiveDataActivity<MineRewardHistoryActivityBinding, MineRewardHistoryViewModel> implements RewardItemClickListener {

    private MineRewarHistoryAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ImmersionBar.with(this)
                .statusBarColor(R.color.white)
                .navigationBarColor(R.color.white)
                .fitsSystemWindows(true)
                .autoDarkModeEnable(true)
                .init();
        AnalysisUtils.onEventEx(this, Dot.Page_LotteryHistory);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.mine_reward_history_activity;
    }

    @Override
    public void initView() {
        mDataBinding.mineRewardHistoryRv.setVisibility(View.GONE);
        mDataBinding.mineRewardHistoryLoadingTv.setVisibility(View.VISIBLE);

        mAdapter = new MineRewarHistoryAdapter(this, this);
        mDataBinding.mineRewardHistoryRv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.bottom = 20;
            }
        });
        mDataBinding.mineRewarHisBack.setOnClickListener(v->{
            finish();
        });
        mDataBinding.mineRewardHistoryRv.setLayoutManager(new LinearLayoutManager(this));
        mDataBinding.mineRewardHistoryRv.setAdapter(mAdapter);
        mViewModel.getHistoryData().observe(this, rewardHistoryBean -> {
            if (rewardHistoryBean == null || rewardHistoryBean.getList() == null) {
                return;
            }

            if (rewardHistoryBean.getList().size() <= 0) {
                Toast.makeText(this, "暂时无往期开奖码", Toast.LENGTH_SHORT).show();
                mDataBinding.mineRewardHistoryLoadingTv.setText("暂时无往期开奖码");
                return;
            }

            mAdapter.refreshData(rewardHistoryBean.getList());
            mDataBinding.mineRewardHistoryRv.setVisibility(View.VISIBLE);
            mDataBinding.mineRewardHistoryLoadingTv.setVisibility(View.GONE);
        });
    }

    @Override
    public void onClick(int period) {

    }
}