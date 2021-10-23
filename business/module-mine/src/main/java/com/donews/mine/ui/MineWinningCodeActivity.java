package com.donews.mine.ui;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.donews.base.activity.MvvmBaseLiveDataActivity;
import com.donews.base.utils.ToastUtil;
import com.donews.common.router.RouterActivityPath;
import com.donews.mine.R;
import com.donews.mine.adapters.MineParticipateRecordAdapter;
import com.donews.mine.adapters.MineWinningCodeAdapter;
import com.donews.mine.databinding.MineActivityWinningCodeBinding;
import com.donews.mine.viewModel.MineWinningCodeViewModel;
import com.gyf.immersionbar.ImmersionBar;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 个人中心的开奖码
 */
@Route(path = RouterActivityPath.Mine.PAGER_MINE_WINNING_CODE_ACTIVITY)
public class MineWinningCodeActivity extends
        MvvmBaseLiveDataActivity<MineActivityWinningCodeBinding, MineWinningCodeViewModel> {

    @Autowired(name = "period")
    public int period;

    private int headRes = R.layout.mine_activity_winning_code_list_head;
    //adapter的headView
    private ViewGroup adapterHead = null;
    MineWinningCodeAdapter adapter;
    private List<Object> list = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.mine_activity_winning_code;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this)
                .statusBarColor(R.color.transparent)
                .navigationBarColor(R.color.white)
                .fitsSystemWindows(false)
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
        ARouter.getInstance().inject(this);
        mViewModel.setDataBinDing(mDataBinding, this);
        adapterHead = (ViewGroup) View.inflate(this, headRes, null);
        adapterHead.findViewById(R.id.mine_win_code_sele_rules).setOnClickListener((v) -> {
            ToastUtil.show(this, "中奖规则查看");
        });
        adapter = new MineWinningCodeAdapter();
        //设置没有更多数据
        adapter.getLoadMoreModule().loadMoreEnd();
        adapter.setOnLoadMoreListener((page, pageSize) -> {
            adapter.loadMoreFinish(true, true);
        });
        mDataBinding.mainWinCodeRefresh.setOnRefreshListener(refreshLayout -> {
            mViewModel.loadData(period);
        });
        addListHead();
        mDataBinding.mineWinCodeList.setLayoutManager(new LinearLayoutManager(this));
        mDataBinding.mineWinCodeList.setAdapter(adapter);
        mDataBinding.mainWinCodeTitleBack.setOnClickListener((v) -> {
            finish();
        });
        mViewModel.detailLivData.observe(this, (data) -> {
            mDataBinding.mainWinCodeRefresh.finishRefresh();
            adapter.setNewData(list);
            updateUI();
        });
        mDataBinding.mainWinCodeRefresh.autoRefresh();
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
    }

    //更新UI
    private void updateUI() {
        mDataBinding.mainWinCodeTitleName.setText(period + "期");
        if (mViewModel.detailLivData.getValue() == null) {
            return;
        }
        mViewModel.updateData( //更新其他数据
                adapterHead);
        mViewModel.addSelectGoods( //添加中奖商品
                adapterHead);
        mViewModel.addAddToGoods( //添加参与商品
                adapterHead, true);
        mViewModel.addSelectToNames( //添加中奖名单
                adapterHead);
    }
}