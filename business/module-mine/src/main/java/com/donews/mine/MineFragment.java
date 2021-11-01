package com.donews.mine;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.dn.events.events.LoginUserStatus;
import com.dn.events.events.UserTelBindEvent;
import com.dn.events.events.WalletRefreshEvent;
import com.donews.base.fragment.MvvmLazyLiveDataFragment;
import com.donews.common.base.MvvmLazyLiveDataFragment;
import com.donews.base.utils.glide.GlideUtils;
import com.donews.common.contract.LoginHelp;
import com.donews.common.contract.UserInfoBean;
import com.donews.common.router.RouterActivityPath;
import com.donews.common.router.RouterFragmentPath;
import com.donews.mine.adapters.MineFragmentAdapter;
import com.donews.mine.databinding.MineFragmentBinding;
import com.donews.mine.viewModel.MineViewModel;
import com.donews.utilslibrary.utils.AppInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;


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

    @Subscribe(threadMode = ThreadMode.MAIN) //手机号绑定成功
    public void bindTel(UserTelBindEvent event) {
        updateUIData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //用户登录状态变化
    public void loginStatusEvent(LoginUserStatus event) {
        updateUIData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetMessage(WalletRefreshEvent navEvent) {
        if (navEvent.navIndex == 0) {
            mViewModel.getLoadWithdrawData();
        }else if(navEvent.navIndex == 1){
            mViewModel.getLoadWithdrawData();
        }
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
        mDataBinding.mineMeListLayout.getRefeshLayout().setEnableRefresh(false);
        mViewModel.setDataBinDing(mDataBinding, getBaseActivity());
        getView().findViewById(R.id.tv_userinfo_name).setOnClickListener((v) -> {
            getView().findViewById(R.id.iv_user_logo).performClick();
        });
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
        getView().findViewById(R.id.mine_me_money_num_ll).setOnClickListener((v) -> {
            //提现
            ARouter.getInstance()
                    .build(RouterActivityPath.Mine.PAGER_ACTIVITY_WITHDRAWAL)
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
        mDataBinding.mineFrmRefesh.setOnRefreshListener(refreshLayout -> {
            refeshListData();
        });
        mDataBinding.mineMeListLayout.attchAppBarLayout(mDataBinding.mineMeApptBar);
        mDataBinding.mineMeListLayout.getRecyclerView().setLayoutManager(
                new GridLayoutManager(getActivity(), 2));
        mDataBinding.mineMeListLayout.getRecyclerView().setAdapter(adapter);
        mViewModel.withdrawDatilesLivData.observe(this, item -> {
            if(item != null){
                TextView tv = getView().findViewById(R.id.mine_me_money_num);
                tv.setText(""+item.total);
            }
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
            mDataBinding.mineFrmRefesh.finishRefresh();
        });
        mDataBinding.mineMeListLayout.getRecyclerView()
                .addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                        if (recyclerView.computeVerticalScrollOffset() <= 0) {
                            mDataBinding.mineFrmRefesh.setEnabled(true);
                        } else {
                            mDataBinding.mineFrmRefesh.setEnabled(false);
                        }
                    }
                });
        updateUIData();
        mDataBinding.mineFrmRefesh.autoRefresh();
//        mDataBinding.mineCcsView.refreshData();
    }

    //更新UI数据
    private void updateUIData() {
        checkLogin();
        checkTelBind();
        UserInfoBean uf = LoginHelp.getInstance().getUserInfoBean();
        ImageView headIcon = getView().findViewById(R.id.iv_user_logo);
        TextView userName = getView().findViewById(R.id.tv_userinfo_name);
        if (uf == null ||
                !AppInfo.checkIsWXLogin()) { //未登录
            headIcon.setImageResource(R.drawable.mine_not_login_user_head);
            userName.setText("立即登录");
        } else { //已登录
            GlideUtils.loadImageViewLoading(getActivity(), uf.getHeadImg(),
                    headIcon, R.drawable.mine_not_login_user_head,
                    R.drawable.mine_not_login_user_head);
            userName.setText(uf.getUserName());
        }
    }

    //检查登录逻辑
    private void checkLogin() {
        ImageView loginIcon = getView().findViewById(R.id.iv_user_logo);
        TextView userName = getView().findViewById(R.id.tv_userinfo_name);
        if (AppInfo.checkIsWXLogin()) {
            loginIcon.setEnabled(false);
            userName.setEnabled(false);
        } else {
            loginIcon.setEnabled(true);
            userName.setEnabled(true);
        }
    }

    //检查手机号绑定
    public void checkTelBind() {
        UserInfoBean userInfo = LoginHelp.getInstance().getUserInfoBean();
        TextView bindTv = getView().findViewById(R.id.rl_top_bar_bind);
        if (userInfo == null) {
            bindTv.setVisibility(View.GONE);
            return; //未登录
        } else {
            if (AppInfo.checkIsWXLogin()) {
                bindTv.setVisibility(View.VISIBLE);
            } else {
                //还未登录(没有实际的登录)
                bindTv.setVisibility(View.GONE);
            }
        }
        String mobile = userInfo.getMobile();
        if (mobile != null &&
                mobile.length() > 0) {
            //已绑定
            bindTv.setText("已绑定");
            bindTv.setEnabled(false);
        } else {
            bindTv.setEnabled(true);
        }
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
