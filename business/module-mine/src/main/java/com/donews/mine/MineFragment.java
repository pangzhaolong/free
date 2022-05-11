package com.donews.mine;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.SPUtils;
import com.dn.drouter.ARouteHelper;
import com.dn.events.events.LoginUserStatus;
import com.dn.events.events.LotteryStatusEvent;
import com.dn.events.events.UserTelBindEvent;
import com.dn.events.events.WalletRefreshEvent;
import com.dn.sdk.AdCustomError;
import com.dn.sdk.listener.interstitial.SimpleInterstitialFullListener;
import com.dn.sdk.listener.interstitial.SimpleInterstitialListener;
import com.donews.base.utils.GsonUtils;
import com.donews.base.utils.ToastUtil;
import com.donews.base.utils.glide.GlideUtils;
import com.donews.common.base.MvvmLazyLiveDataFragment;
import com.donews.common.contract.LoginHelp;
import com.donews.common.contract.UserInfoBean;
import com.donews.common.router.RouterActivityPath;
import com.donews.common.router.RouterFragmentPath;
import com.donews.middle.adutils.InterstitialAd;
import com.donews.middle.adutils.InterstitialFullAd;
import com.donews.middle.adutils.adcontrol.AdControlManager;
import com.donews.middle.views.TaskView;
import com.donews.mine.adapters.MineFragmentAdapter;
import com.donews.mine.bean.MineWithdraWallBean;
import com.donews.mine.bean.resps.RecommendGoodsResp;
import com.donews.mine.databinding.MineFragmentBinding;
import com.donews.mine.viewModel.MineViewModel;
import com.donews.mine.views.operating.MineOperatingPosView;
import com.donews.network.BuildConfig;
import com.donews.network.EasyHttp;
import com.donews.network.cache.model.CacheMode;
import com.donews.network.callback.SimpleCallBack;
import com.donews.network.exception.ApiException;
import com.donews.utilslibrary.analysis.AnalysisUtils;
import com.donews.utilslibrary.dot.Dot;
import com.donews.utilslibrary.utils.AppInfo;
import com.donews.utilslibrary.utils.JsonUtils;
import com.donews.yfsdk.check.InterstitialAdCheck;
import com.donews.yfsdk.moniter.PageMonitor;
import com.donews.yfsdk.monitor.InterstitialFullAdCheck;
import com.donews.yfsdk.monitor.PageMoniterCheck;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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

    //运营位的配置信息的文件名称
    public static final String mineYYWCacheFile = "mineYYWCacheFile";
    //运营位的配置缓存的key
    public static final String mineYYWCache = "mineYYWCache";

    MineFragmentAdapter adapter;
    private boolean isRefresh = false;

    private TaskView mTaskView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new PageMonitor().attach(this, new PageMonitor.PageListener() {
            @NonNull
            @Override
            public AdCustomError checkShowAd() {
                if (AdControlManager.INSTANCE.getAdControlBean().getUseInstlFullWhenSwitch()) {
                    return InterstitialFullAdCheck.INSTANCE.isEnable();
                } else {
                    return InterstitialAdCheck.INSTANCE.isEnable();
                }
            }

            @Override
            public int getIdleTime() {
                return AdControlManager.INSTANCE.getAdControlBean().getNoOperationDuration();
            }

            @Override
            public void showAd() {
                Activity activity = requireActivity();
                if (activity == null || activity.isFinishing()) {
                    return;
                }
                if (!AdControlManager.INSTANCE.getAdControlBean().getUseInstlFullWhenSwitch()) {
                    InterstitialAd.INSTANCE.showAd(activity, new SimpleInterstitialListener() {
                        @Override
                        public void onAdError(int code, String errorMsg) {
                            super.onAdError(code, errorMsg);
                            Logger.d("晒单页插屏加载广告错误---- code = $code ,msg =  $errorMsg ");
                        }

                        @Override
                        public void onAdClosed() {
                            super.onAdClosed();
                            PageMoniterCheck.INSTANCE.showAdSuccess("mine_fragment");
                        }
                    });
                } else {
                    InterstitialFullAd.INSTANCE.showAd(activity, new SimpleInterstitialFullListener() {
                        @Override
                        public void onAdError(int errorCode, String errprMsg) {
                            super.onAdError(errorCode, errprMsg);
                            Logger.d("晒单页插全屏加载广告错误---- code = $errorCode ,msg =  $errprMsg ");
                        }

                        @Override
                        public void onAdClose() {
                            super.onAdClose();
                            PageMoniterCheck.INSTANCE.showAdSuccess("mine_fragment");
                        }
                    });
                }
            }
        });
    }

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
        if (!AppInfo.checkIsWXLogin()) {
            mViewModel.withdrawDatilesLivData.postValue(null);
        } else {
            mViewModel.getLoadWithdrawData();
        }
        updateUIData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetMessage(WalletRefreshEvent navEvent) {
        if (navEvent.navIndex == 0) {
            mViewModel.getLoadWithdrawData();
        } else if (navEvent.navIndex == 1) {
            mViewModel.getLoadWithdrawData();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void lotteryStatusEvent(LotteryStatusEvent event) {
        if (event.object == null || event.goodsId == null) {
            return;
        }
        List<String> list = (List<String>) event.object;
        int lotteryStatus = 0;
        if (list.size() <= 0) {
            lotteryStatus = 0;
        } else if (list.size() < 6) {
            lotteryStatus = 1;
        } else {
            lotteryStatus = 2;
        }
        if (adapter != null) {
            RecommendGoodsResp.ListDTO item = adapter.getItem(event.position);
            if (item != null) {
                if (!event.goodsId.equals(item.goodsId)) {
                    return;
                }
                item.lotteryStatus = (lotteryStatus);
                adapter.notifyItemChanged(event.position);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        onRefresh();

//        ARouter.getInstance()
//                .build(RouterFragmentPath.Integral.PAGER_INTEGRAL_NOT_TASK)
//                .navigation();
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
        getView().findViewById(R.id.iv_top_bar_kf).setOnClickListener((v) -> {
            AnalysisUtils.onEventEx(getActivity(), Dot.Page_ContactService);
            Bundle bundle = new Bundle();
            bundle.putString("url",
                    "https://recharge-web.xg.tagtic.cn/jdd/index.html#/customer");
            bundle.putString("title", "客服");
            ARouteHelper.routeSkip(RouterActivityPath.Web.PAGER_WEB_ACTIVITY, bundle);
        });
        getView().findViewById(R.id.tv_userinfo_name).setOnClickListener((v) -> {
            if (!AppInfo.checkIsWXLogin()) {
                ARouter.getInstance()
                        .build(RouterActivityPath.User.PAGER_LOGIN)
                        .navigation();
            }
        });
        getView().findViewById(R.id.iv_user_logo).setOnClickListener((v) -> {
            if (!AppInfo.checkIsWXLogin()) {
                //登录
                ARouter.getInstance()
                        .build(RouterActivityPath.User.PAGER_LOGIN)
                        .navigation();
                return;
            }
            //用户信息
            ARouter.getInstance()
                    .build(RouterActivityPath.Mine.PAGER_MINE_USER_INFO_ACTIVITY)
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
            if (!checkIsLogin()) {
                getView().findViewById(R.id.iv_user_logo).performClick();
                return;
            }
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
            if (!checkIsLogin()) {
                getView().findViewById(R.id.iv_user_logo).performClick();
                return;
            }
            ARouter.getInstance()
                    .build(RouterActivityPath.Mine.PAGER_PARTICIPATE_RECORD)
                    .navigation();
        });
        getView().findViewById(R.id.mine_me_win_reco).setOnClickListener((v) -> {
            //中奖记录
            if (!checkIsLogin()) {
                getView().findViewById(R.id.iv_user_logo).performClick();
                return;
            }
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
            TextView tv = getView().findViewById(R.id.mine_me_money_num);
            if (item != null) {
                tv.setText("" + item.total);
            } else {
                tv.setText("0");
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
        scrollFloatBar();
//        mDataBinding.mineCcsView.refreshData();

        mTaskView = getView().findViewById(R.id.mine_me_k_operating_ex);
        mTaskView.refreshYyw(TaskView.Place_Mine);
    }

    private MineWithdraWallBean mineWithdraWallBean = null;

    //模拟设置运营位
    private void setYYW() {
        String localJson = SPUtils.getInstance(mineYYWCacheFile).getString(mineYYWCache, "");
        if (localJson.length() > 0) {
            mineWithdraWallBean = GsonUtils.fromLocalJson(localJson, MineWithdraWallBean.class);
            updateYYWData();
        }
        EasyHttp.get(BuildConfig.BASE_CONFIG_URL  + com.donews.common.BuildConfig.APP_IDENTIFICATION+"-"+ "mineWithdrawal" + com.donews.common.BuildConfig.BASE_RULE_URL
                + JsonUtils.getCommonJson(false))
                .cacheMode(CacheMode.NO_CACHE)
                .isShowToast(false)
                .execute(new SimpleCallBack<MineWithdraWallBean>() {
                    @Override
                    public void onError(ApiException e) {
                        ToastUtil.showShort(getActivity(), "获取数据异常");
                    }

                    @Override
                    public void onSuccess(MineWithdraWallBean appWallBean) {
                        if (appWallBean != null) {
                            if (mineWithdraWallBean == null) {
                                mineWithdraWallBean = appWallBean;
                                updateYYWData();
                            }
                            SPUtils.getInstance(mineYYWCacheFile).put(mineYYWCache, GsonUtils.toJson(appWallBean));
                        }
                    }
                });
    }

    //更新运营位数据
    private void updateYYWData() {
        if (mineWithdraWallBean == null || !mineWithdraWallBean.mine) {
            return;
        }
        MineOperatingPosView vpOperation = getView().findViewById(R.id.mine_me_k_operating);
        List<MineOperatingPosView.IOperatingData> list = new ArrayList<>();
        if (mineWithdraWallBean.mineItems != null && mineWithdraWallBean.mineItems.size() > 0) {
            list.addAll(mineWithdraWallBean.mineItems);
        }
        vpOperation.setDatas(list);
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
        vpHei = 0; //让滚动位置重新计算一遍。防止视图变化引起的位置变化计算不及时导致界面滑动错误
    }

    //检查是否已经登录了，T:已登录，F:未登录
    private boolean checkIsLogin() {
        UserInfoBean uf = LoginHelp.getInstance().getUserInfoBean();
        return uf != null &&
                AppInfo.checkIsWXLogin();
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
        bindTv.setVisibility(View.GONE);
        if (userInfo == null) {
//            bindTv.setVisibility(View.GONE);
            return; //未登录
        } else {
            if (AppInfo.checkIsWXLogin()) {
//                bindTv.setVisibility(View.VISIBLE);
            } else {
                //还未登录(没有实际的登录)
//                bindTv.setVisibility(View.GONE);
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
//        setYYW();
        if (mTaskView != null) {
            mTaskView.refreshYyw(TaskView.Place_Mine);
        }
        if (checkIsLogin()) {
            mViewModel.getLoadWithdrawData();
        } else {
            mViewModel.withdrawDatilesLivData.postValue(null);
        }
    }

    //上拉加载更多
    private void loadMoreListData() {
        isRefresh = false;
        mViewModel.loadRecommendGoods(adapter.getData().size() + 15);
    }

    int topBaseLinePx = 0;
    int topFloatBaseHei = 0;
    int appBarHei = 0;
    int vpHei = 0;
    int selectBarBaseHei = 0;
    View vpView = null;

    //滚动悬浮标题处理
    private void scrollFloatBar() {
        //处理滑动精选标题遮挡问题
        vpView = getView().findViewById(R.id.mine_me_k_operating);
        int statusHei = BarUtils.getStatusBarHeight();
        mDataBinding.mineMeApptBar.addOnOffsetChangedListener((appBarLayout1, verticalOffset) -> {
            if (selectBarBaseHei == 0) {
                selectBarBaseHei = mDataBinding.mineMeSelectBar.getHeight();
            }
            if (topFloatBaseHei == 0) {
                topFloatBaseHei = mDataBinding.mineMeSelectBar.getPaddingTop();
            }
            //vp调整过位置了。那么重新就散基础距离等相关数据
            if (vpHei != vpView.getHeight()) {
                vpHei = vpView.getHeight();
                appBarHei = mDataBinding.mineMeApptBar.getHeight();
                topBaseLinePx = (appBarHei - (selectBarBaseHei - topFloatBaseHei) - statusHei);
            }
            if (appBarHei == 0 || topFloatBaseHei == 0 || topBaseLinePx == 0) {
                return;
            }
            if (verticalOffset < -(topBaseLinePx)) {
                //需要改变了
                mDataBinding.mineMeSelectBar.setPadding(
                        0,
                        topFloatBaseHei + Math.abs(topBaseLinePx + verticalOffset),
                        0,
                        mDataBinding.mineMeSelectBar.getPaddingBottom());
            } else if (verticalOffset > -(topBaseLinePx)) {
                //还没有到位置
                if (mDataBinding.mineMeSelectBar.getPaddingTop() > topFloatBaseHei) {
                    mDataBinding.mineMeSelectBar.setPadding(
                            0,
                            topFloatBaseHei - Math.abs(verticalOffset + topBaseLinePx),
                            0,
                            mDataBinding.mineMeSelectBar.getPaddingBottom());
                } else if (mDataBinding.mineMeSelectBar.getPaddingTop() < topFloatBaseHei) {
                    mDataBinding.mineMeSelectBar.setPadding(
                            0,
                            topFloatBaseHei,
                            0,
                            mDataBinding.mineMeSelectBar.getPaddingBottom());
                }
            }
        });
    }
}
