package com.donews.mine;

import android.annotation.SuppressLint;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.donews.base.fragment.MvvmLazyLiveDataFragment;
import com.donews.common.router.RouterActivityPath;
import com.donews.common.router.RouterFragmentPath;
import com.donews.mine.adapters.MineFragmentAdapter;
import com.donews.mine.databinding.MineFragmentBinding;
import com.donews.mine.viewModel.MineViewModel;

import java.util.ArrayList;
import java.util.List;


/**
 * <p> </p>
 * 作者： created by lcl<br>
 * 日期： 2021/10/18 18:13<br>
 * 版本：V1.0<br>
 */
@Route(path = RouterFragmentPath.User.PAGER_USER)
public class MineFragment extends MvvmLazyLiveDataFragment<MineFragmentBinding, MineViewModel> {

    MineFragmentAdapter adapter;

    @Override
    public int getLayoutId() {
        return R.layout.mine_fragment;
    }

    @Override
    protected void onFragmentFirstVisible() {
        super.onFragmentFirstVisible();
        initView();
        mViewModel.lifecycleOwner = this;
    }

    private List<Object> list = new ArrayList<>();

    @SuppressLint("WrongConstant")
    private void initView() {
        mViewModel.setDataBinDing(mDataBinding, getBaseActivity());
        getView().findViewById(R.id.mine_me_add_reco).setOnClickListener((v) -> {
            //参与记录
            ARouter.getInstance()
                    .build(RouterActivityPath.Mine.PAGER_PARTICIPATE_RECORD)
                    .navigation();
        });
        getView().findViewById(R.id.mine_me_win_reco).setOnClickListener((v) -> {
            //中奖记录
            ARouter.getInstance()
                    .build(RouterActivityPath.Mine.PAGER_WINNING_RECORD)
                    .navigation();
        });
        adapter = new MineFragmentAdapter();
        adapter.setOnLoadMoreListener((page, pageSize) -> {
            loadMoreListData();
        });
        mDataBinding.mineMeListLayout.setRefeshOnListener(refreshLayout -> {
            refeshListData();
        });
        mDataBinding.mineMeListLayout.attchAppBarLayout(mDataBinding.mineMeApptBar);
        mDataBinding.mineMeListLayout.getRecyclerView().setLayoutManager(
                new GridLayoutManager(getActivity(), 2));
        mDataBinding.mineMeListLayout.getRecyclerView().setAdapter(adapter);
        mDataBinding.mineMeListLayout.getRefeshLayout().autoRefresh();

//        mDataBinding.mineCcsView.refreshData();
    }

    @Override
    public void onResume() {
        super.onResume();
        onRefresh();
        adapter.setNewData(list);
    }

    private void onRefresh() {
//        mViewModel.getQuery().observe(this, queryBean -> {
//            if (queryBean == null) {
//                return;
//            }
//            mDataBinding.setVariable(BR.query, queryBean);
//        });
//        if (LoginHelp.getInstance().getUserInfoBean() != null) {
//            mDataBinding.setVariable(BR.user, LoginHelp.getInstance().getUserInfoBean());
//        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    Handler h = new Handler();

    //下拉刷新数据
    private void refeshListData() {
        h.postDelayed(() -> {
            mDataBinding.mineMeListLayout.setRefeshComplete();
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
