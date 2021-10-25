package com.donews.mine;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.util.Log;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.dn.events.events.LoginUserStatus;
import com.dn.events.events.UserTelBindEvent;
import com.donews.base.fragment.MvvmLazyLiveDataFragment;
import com.donews.common.router.RouterActivityPath;
import com.donews.common.router.RouterFragmentPath;
import com.donews.mine.adapters.MineFragmentAdapter;
import com.donews.mine.bean.resps.RecommendGoodsResp;
import com.donews.mine.databinding.MineFragmentBinding;
import com.donews.mine.viewModel.MineViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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
    private boolean isRefresh = false;

    @Override
    public int getLayoutId() {
        EventBus.getDefault().register(this);
        return R.layout.mine_fragment;
    }

    @Override
    protected void onFragmentFirstVisible() {
        super.onFragmentFirstVisible();
        initView();
        mViewModel.lifecycleOwner = this;
    }

    @Subscribe //手机号绑定成功
    public void bindTel(UserTelBindEvent event) {
        checkTelBind();
    }

    @Subscribe //用户登录状态变化
    public void bindTel(LoginUserStatus event) {
        checkTelBind();
    }

    @Override
    public void onResume() {
        super.onResume();
        onRefresh();
    }

    private void onRefresh() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @SuppressLint("WrongConstant")
    private void initView() {
        mViewModel.setDataBinDing(mDataBinding, getBaseActivity());
        getView().findViewById(R.id.iv_user_logo).setOnClickListener((v) -> {
            //登录
            ARouter.getInstance()
                    .build(RouterActivityPath.User.PAGER_LOGIN)
                    .navigation();
        });
        getView().findViewById(R.id.rl_top_bar_seting).setOnClickListener((v) -> {
            //设置
            ARouter.getInstance()
                    .build(RouterActivityPath.Mine.PAGER_ACTIVITY_SETTING)
                    .navigation();
        });
        getView().findViewById(R.id.rl_top_bar_bind).setOnClickListener((v) -> {
            //绑定手机号
            DialogFragment bindTelDialogFragment = (DialogFragment) ARouter.getInstance()
                    .build(RouterFragmentPath.Login.PAGER_BIND_PHONE_DIALOG_FRAGMENT).navigation();
            bindTelDialogFragment.show(
                    getActivity().getSupportFragmentManager(), "bind_tel");
        });
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
            mDataBinding.mineMeListLayout.getRefeshLayout().finishRefresh();
        });
        mDataBinding.mineMeListLayout.getRefeshLayout().autoRefresh();

//        mDataBinding.mineCcsView.refreshData();
    }

    //检查手机号绑定
    public void checkTelBind() {

    }

    //下拉刷新数据
    private void refeshListData() {
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
