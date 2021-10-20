package com.donews.mine.ui;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.donews.base.activity.MvvmBaseLiveDataActivity;
import com.donews.common.router.RouterActivityPath;
import com.donews.mine.R;
import com.donews.mine.adapters.MineParticipateRecordAdapter;
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
        mDataBinding.mineParRecodLayout.getRefeshLayout().autoRefresh();
    }

    private List<Object> list = new ArrayList<>();

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
        //添加head的数据
        for (int i = 0; i < 3; i++) {
            View itemView = View.inflate(this, adapterHeadItemRes, null);
            recordListVP.addView(itemView,
                    new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }

    }

    Handler h = new Handler();

    //下拉刷新数据
    private void refeshListData() {
        h.postDelayed(() -> {
            mDataBinding.mineParRecodLayout.setRefeshComplete();
            list.clear();
            for (int i = 0; i < 50; i++) {
                list.add("" + i);
            }
            adapter.setNewData(list);
        }, 1000);
    }

    //上拉加载更多
    private void loadMoreListData() {
        h.postDelayed(() -> {
            for (int i = 0; i < 10; i++) {
                list.add("" + i);
            }
            adapter.loadMoreFinish(true, false);
            adapter.addData(list);
        }, 1000);
    }
}