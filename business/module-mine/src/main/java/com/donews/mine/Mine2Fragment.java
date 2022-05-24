package com.donews.mine;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
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
import com.donews.middle.IMainParams;
import com.donews.middle.adutils.InterstitialAd;
import com.donews.middle.adutils.InterstitialFullAd;
import com.donews.middle.adutils.adcontrol.AdControlManager;
import com.donews.middle.bean.mine2.resp.SignResp;
import com.donews.middle.centralDeploy.OutherSwitchConfig;
import com.donews.middle.front.FrontConfigManager;
import com.donews.middle.mainShare.vm.MainShareViewModel;
import com.donews.middle.viewmodel.BaseMiddleViewModel;
import com.donews.middle.views.TaskView;
import com.donews.mine.adapters.Mine2FragmentTaskAdapter;
import com.donews.mine.adapters.MineFragmentAdapter;
import com.donews.mine.bean.MineWithdraWallBean;
import com.donews.mine.bean.resps.RecommendGoodsResp;
import com.donews.mine.databinding.MineFragmentBinding;
import com.donews.mine.databinding.MineFragmentNewBinding;
import com.donews.mine.utils.TextViewNumberUtil;
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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


/**
 * <p> </p>
 * 作者： created by lcl<br>
 * 日期： 2021/10/18 18:13<br>
 * 版本：V1.0<br>
 */
@Route(path = RouterFragmentPath.User.PAGER_USER_NEW)
public class Mine2Fragment extends MvvmLazyLiveDataFragment<MineFragmentNewBinding, MineViewModel> {

    private Mine2FragmentTaskAdapter taskAdapter = null;
    private long fastUpdateTime = 0;

    private MainShareViewModel mShareVideModel;

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
                if (activity instanceof IMainParams &&
                        !OutherSwitchConfig.Ins().checkMainTabInterstitial(
                                ((IMainParams) activity).getThisFragmentCurrentPos(Mine2Fragment.this))) {
                    //后台设置当前Tab不允许加载插屏
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
        return R.layout.mine_fragment_new;
    }

    @Override
    protected boolean isActivityViewModel() {
        return true; //将当前ViewModel的生命周期扩大
    }

    @Override
    protected void onFragmentFirstVisible() {
        super.onFragmentFirstVisible();

        mShareVideModel = new ViewModelProvider(requireActivity()).get(MainShareViewModel.class);
        SignResp it = new SignResp();
        it.coin = 1500;
        mViewModel.mineSignResult.postValue(it);
        mViewModel.mineSignDounleResult.postValue(it);

        mDataBinding.setViewModel(mViewModel);
        mDataBinding.setThiz(this);
        mViewModel.lifecycleOwner = this;
        initView();
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //手机号绑定成功
    public void bindTel(UserTelBindEvent event) {
        updateUIData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //用户登录状态变化
    public void loginStatusEvent(LoginUserStatus event) {
        if (event.getStatus() == 1) {
            loadData();
        }
        updateUIData();
    }

    @Override
    public void onResume() {
        super.onResume();
        onRefresh();
    }

    //检查是否有DialogFragmnet显示。T:有，F:没有
    private void isFragmentDialogShow(Runnable finishCall) {
        try {
            boolean isFragmentDialogShow = false;
            List<Fragment> list = getBaseActivity().getSupportFragmentManager().getFragments();
            DialogFragment dialogFragment = null;
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) instanceof DialogFragment) {
                    if (!list.get(i).isHidden()) {
                        dialogFragment = (DialogFragment) list.get(i);
                        isFragmentDialogShow = true;
                        break;
                    }
                }
            }
            if (!isFragmentDialogShow) {
                finishCall.run();
                return;
            }
            Message dissMessage = null;
            try {
                if (dialogFragment == null ||
                        dialogFragment.getDialog() == null) {
                    finishCall.run();
                    return;
                }
                //获取原始的监听
                Field field = dialogFragment.getDialog().getClass().getDeclaredField("mDismissMessage");
                field.setAccessible(true);
                dissMessage = (Message) field.get(dialogFragment);
            } catch (Exception e) {
            } finally {
                Message finalDissMessage = dissMessage;
                if (finalDissMessage == null) {
                    finishCall.run();
                } else {
                    dialogFragment.getDialog().setOnDismissListener(dialog -> {
                        //执行原始的关闭事件
                        Message.obtain(finalDissMessage).sendToTarget();
                    });
                }
            }
        } catch (Exception e) {
            finishCall.run();
        }
    }

    private void onRefresh() {
        if (BaseMiddleViewModel.getBaseViewModel().mine2DailyTask.getValue() != null &&
                System.currentTimeMillis() - fastUpdateTime < 5 * 60 * 1000) {
            return; //如果加载成功之后。才会进行时间计算。否则只要切换就会加载
        }
        mViewModel.getDailyTasks();
        mViewModel.getsignList();
        mViewModel.getUserAssets();
        fastUpdateTime = System.currentTimeMillis();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    //去往设置页面
    public void gotoSetting() {
        ARouter.getInstance()
                .build(RouterActivityPath.Mine.PAGER_ACTIVITY_SETTING)
                .navigation();
    }

    //去往登录页面
    public void gotoLogin() {
        //去往登录页面
        ARouter.getInstance()
                .build(RouterActivityPath.User.PAGER_LOGIN)
                .navigation();
        //go to
//        ARouter.getInstance()
//                .build(RouterActivityPath.Mine.PAGER_MINE_WINNING_CODE_ACTIVITY)
//                .navigation();
    }

    //是否存在金币播放任务了已经
    private boolean isExietisJBAnimTask = false;
    //是否存在金币播放任务了已经
    private boolean isExietisJFAnimTask = false;

    @SuppressLint("WrongConstant")
    private void initView() {
        //设置运营位
        if (FrontConfigManager.Ins().getConfigBean().getTask()) {
            mDataBinding.mine2YywLl.setVisibility(View.VISIBLE);
            mDataBinding.mine2Yyw.refreshYyw(TaskView.Place_Mine);
        } else {
            mDataBinding.mine2YywLl.setVisibility(View.GONE);
        }

        taskAdapter = new Mine2FragmentTaskAdapter(getBaseActivity(), mViewModel);
        mDataBinding.mine2TaskList.setAdapter(taskAdapter);
        mViewModel.mine2RefeshDataLive.observe(this, (result) -> {
            if (result) {
                loadData(); //刷新数据
                syncActivitiesModel(); //同步刷新
            }
        });
        BaseMiddleViewModel.getBaseViewModel().mine2DailyTask.observe(this, (result) -> {
            if (result == null) {
                taskAdapter.setNewData(new ArrayList<>());
            } else {
                taskAdapter.setNewData(result);
            }
        });
        loadData();

        // 订阅外部模块金币等数据变化
        mShareVideModel.userAssets.observe(this, (item) -> {
            //活动模块同步
            loadData();
        });
        //金币
        mViewModel.getMine2JBCount().observe(this, jb -> {
            isExietisJBAnimTask = true;
            isFragmentDialogShow(() -> {
                try {
                    isExietisJBAnimTask = false;
                    double startDouble = Double.parseDouble(mDataBinding.mine2JbCount.getText().toString());
                    TextViewNumberUtil.addTextViewAddAnim(mDataBinding.mine2JbCount, startDouble, jb);
                } catch (Exception e) {
                    mDataBinding.mine2JbCount.setText("" + jb);
                }
            });
        });
        //积分
        mViewModel.getMine2JFCount().observe(this, jf -> {
            isExietisJFAnimTask = true;
            isFragmentDialogShow(() -> {
                try {
                    isExietisJFAnimTask = true;
                    double startDouble = Double.parseDouble(mDataBinding.mine2JfCount.getText().toString());
                    TextViewNumberUtil.addTextViewAddAnim(mDataBinding.mine2JfCount, startDouble, jf);
                } catch (Exception e) {
                    mDataBinding.mine2JfCount.setText("" + jf);
                }
            });
        });
    }

    private void loadData() {
        //加载今日任务数据
        mViewModel.getDailyTasks();
        mViewModel.getsignList();
        mViewModel.getUserAssets();
    }

    private void syncActivitiesModel() {
        //同步更新活动模块数据
        mShareVideModel.requestTaskBubbles();
    }

    //更新UI数据
    private void updateUIData() {
        if (AppInfo.checkIsWXLogin()) {
            mViewModel.mine2UserHead.postValue(LoginHelp.getInstance().getUserInfoBean().getWechatExtra().getHeadimgurl());
            mViewModel.mine2UserName.postValue(LoginHelp.getInstance().getUserInfoBean().getWechatExtra().getNickName());
        } else {
            mViewModel.mine2UserHead.postValue("");
            mViewModel.mine2UserName.postValue("未登录");
        }
    }
}
