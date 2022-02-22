package com.donews.mine.ui;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.ConvertUtils;
import com.donews.base.base.BaseApplication;
import com.donews.base.utils.ToastUtil;
import com.donews.common.base.MvvmBaseLiveDataActivity;
import com.donews.common.router.RouterActivityPath;
import com.donews.common.router.RouterFragmentPath;
import com.donews.middle.abswitch.OtherSwitch;
import com.donews.middle.bean.HighValueGoodsBean;
import com.donews.middle.cache.GoodsCache;
import com.donews.middle.request.RequestUtil;
import com.donews.mine.R;
import com.donews.mine.adapters.MineWinningRecordAdapter;
import com.donews.mine.bean.resps.WinRecordResp;
import com.donews.mine.databinding.MineActivityWinningRecordBinding;
import com.donews.mine.viewModel.MineWinningRecordViewModel;
import com.donews.utilslibrary.analysis.AnalysisUtils;
import com.donews.utilslibrary.dot.Dot;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

/**
 * 中奖记录
 */
@Route(path = RouterActivityPath.Mine.PAGER_WINNING_RECORD)
public class MineWinningRecordActivity extends
        MvvmBaseLiveDataActivity<MineActivityWinningRecordBinding, MineWinningRecordViewModel> {
    //适配器对象
    private MineWinningRecordAdapter adapter;
    private boolean isRefesh = false;

    @Override
    protected int getLayoutId() {
        return R.layout.mine_activity_winning_record;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this)
                .statusBarColor(R.color.white)
                .navigationBarColor(R.color.black)
                .fitsSystemWindows(true)
                .autoDarkModeEnable(true)
                .init();
        initView();
        AnalysisUtils.onEventEx(this, Dot.Page_AwardRecords);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initData();
    }

    public void initView() {
        adapter = new MineWinningRecordAdapter();
        adapter.setOnLoadMoreListener((page, pageSize) -> {
            if (mViewModel.winRecordLivData.getValue() == null ||
                    mViewModel.winRecordLivData.getValue().size() < adapter.pageSize) {
                adapter.loadMoreFinish(true, true);
            } else {
                isRefesh = false;
                mViewModel.loadDataList(page, pageSize);
            }
        });
        mDataBinding.mineWinRecodLayout.setRefeshOnListener(refreshLayout -> {
            isRefesh = true;
            refeshListData();
        });
        mDataBinding.mineWinRecodLayout.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        mDataBinding.mineWinRecodLayout.setAdapter(adapter);
        mDataBinding.mineWinRecodBack.setOnClickListener((v) -> {
            finish();
        });
        mDataBinding.mineWinRecodLayout.getStateLayout().setOnRetryClickListener(v -> {
            mDataBinding.mineWinRecodLayout.getRefeshLayout().autoRefresh();
        });
        mViewModel.winRecordLivData.observe(this, (items) -> {
            List<WinRecordResp.ListDTO> list;
            if (items == null) {
                list = new ArrayList<>();
            } else {
                list = items;
            }
            if (isRefesh) {
                mDataBinding.mineWinRecodLayout.setRefeshComplete();
                adapter.loadMoreFinish(true, items == null || items.isEmpty() || items.size() < adapter.pageSize);
                adapter.setNewData(list);
                if (items == null) {
                    mDataBinding.mineWinRecodLayout.getStateLayout().showError();
                    mDataBinding.mineWinRecodLayout.getStateLayout().findViewById(R.id.error_view)
                            .setOnClickListener(v -> {
                                mDataBinding.mineWinRecodLayout.getRefeshLayout().autoRefresh();
                            });
                } else if (list.isEmpty()) {
                    mDataBinding.mineWinRecodLayout.getStateLayout().showEmpty(
                            R.layout.incl_open_win_not_data, new ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT
                            )
                    );
                    View notDataView = mDataBinding.mineWinRecodLayout.getStateLayout().findViewById(R.id.mine_open_win_not_data_ll);
                    ImageView icon = mDataBinding.mineWinRecodLayout.getStateLayout().findViewById(R.id.mine_open_win_not_data_icon);
                    TextView tv = mDataBinding.mineWinRecodLayout.getStateLayout().findViewById(R.id.mine_open_win_not_data);
                    TextView but = mDataBinding.mineWinRecodLayout.getStateLayout().findViewById(R.id.mine_open_win_not_data_but);
                    tv.setText("一个奖都没有中\n你考虑过奖品的感受吗");
                    but.setText("立即抽奖");
                    but.setVisibility(View.VISIBLE);
                    but.setOnClickListener(v -> {
                        HighValueGoodsBean t = GoodsCache.readGoodsBean(HighValueGoodsBean.class, "exit");
                        if (t.getList() == null ||
                                t.getList().isEmpty()) {
                            ToastUtil.showShort(this, "商品获取失败。请重试");
                            RequestUtil.requestHighValueGoodsInfo();
                            return;
                        }
                        HighValueGoodsBean.GoodsInfo info = t.getList().get(0);
                        AnalysisUtils.onEventEx(BaseApplication.getInstance(),
                                Dot.But_Goto_Lottery, "中奖记录>没有中奖>立即抽奖");
                        ARouter.getInstance()
                                .build(RouterFragmentPath.Lottery.PAGER_LOTTERY)
                                .withString("goods_id", info.getGoodsId())
                                .withBoolean("start_lottery", OtherSwitch.Ins().isOpenAutoLottery())
                                .navigation();
                    });
                    notDataView.setPadding(notDataView.getPaddingLeft()
                            , notDataView.getPaddingTop() + ConvertUtils.dp2px(40F),
                            notDataView.getPaddingRight(),
                            notDataView.getPaddingBottom());
                    icon.setImageResource(R.drawable.mine_open_win_not_data_bg);
                } else {
                    mDataBinding.mineWinRecodLayout.getStateLayout().showContent();
                }
            } else {
                mDataBinding.mineWinRecodLayout.getStateLayout().showContent();
                adapter.loadMoreFinish(true, items.isEmpty());
                adapter.addData(list);
            }
        });
        mDataBinding.mineWinRecodLayout.getRefeshLayout().autoRefresh();
    }

    private void initData() {
    }

    //下拉刷新数据
    private void refeshListData() {
        mViewModel.loadDataList(1, adapter.pageSize);
    }
}