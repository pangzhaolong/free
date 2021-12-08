package com.donews.main.ui;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.dn.events.events.DoubleRpEvent;
import com.dn.events.events.LoginUserStatus;
import com.dn.events.events.RedPackageStatus;
import com.dn.events.events.WalletRefreshEvent;
import com.donews.base.base.AppManager;
import com.donews.base.base.AppStatusConstant;
import com.donews.base.base.AppStatusManager;
import com.donews.base.utils.ToastUtil;
import com.donews.common.ad.business.loader.AdManager;
import com.donews.common.ad.cache.AdVideoCacheUtils;
import com.donews.common.adapter.ScreenAutoAdapter;
import com.donews.common.base.MvvmBaseLiveDataActivity;
import com.donews.common.router.RouterActivityPath;
import com.donews.common.router.RouterFragmentPath;
import com.donews.common.updatedialog.UpdateManager;
import com.donews.common.updatedialog.UpdateReceiver;
import com.donews.main.BuildConfig;
import com.donews.main.R;
import com.donews.main.adapter.MainPageAdapter;
import com.donews.main.common.CommonParams;
import com.donews.main.databinding.MainActivityMainBinding;
import com.donews.main.dialog.AnAdditionalDialog;
import com.donews.main.dialog.DrawDialog;
import com.donews.main.dialog.EnterShowDialog;
import com.donews.main.dialog.FreePanicBuyingDialog;
import com.donews.main.utils.ExitInterceptUtils;
import com.donews.main.viewModel.MainViewModel;
import com.donews.main.views.CornerMarkUtils;
import com.donews.main.views.MainBottomTanItem;
import com.donews.middle.abswitch.ABSwitch;
import com.donews.utilslibrary.analysis.AnalysisHelp;
import com.donews.utilslibrary.analysis.AnalysisParam;
import com.donews.utilslibrary.analysis.AnalysisUtils;
import com.donews.utilslibrary.dot.Dot;
import com.donews.utilslibrary.utils.AppInfo;
import com.donews.utilslibrary.utils.DateManager;
import com.donews.utilslibrary.utils.HttpConfigUtilsKt;
import com.donews.utilslibrary.utils.KeySharePreferences;
import com.donews.utilslibrary.utils.LogUtil;
import com.donews.utilslibrary.utils.SPUtils;
import com.gyf.immersionbar.ImmersionBar;
import com.vmadalin.easypermissions.EasyPermissions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import me.majiajie.pagerbottomtabstrip.NavigationController;

/**
 * app 主页面
 *
 * @author darryrzhoong
 */

@Route(path = RouterActivityPath.Main.PAGER_MAIN)
public class MainActivity
        extends MvvmBaseLiveDataActivity<MainActivityMainBinding, MainViewModel> {

    private List<Fragment> fragments;

    private MainPageAdapter adapter;

    private NavigationController mNavigationController;

    private long mFirstClickBackTime = 0;
    /**
     * 初始选择tab
     */
    @Autowired(name = "position")
    int mPosition = 0;
    private CornerMarkUtils maskUtils = new CornerMarkUtils(this);

    private MainBottomTanItem lotteryItem;
    private ViewTreeObserver.OnGlobalLayoutListener layoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            mViewModel.checkMaskData(MainActivity.this);
            lotteryItem.getViewTreeObserver().removeOnGlobalLayoutListener(layoutListener);
        }
    };

    public static void start(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        ScreenAutoAdapter.match(this, 375.0f);
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this)
                .statusBarColor(R.color.transparent)
                .navigationBarColor(R.color.black)
                .fitsSystemWindows(false)
                .autoDarkModeEnable(true)
                .init();

        int nInAppCount = SPUtils.getInformain(KeySharePreferences.IS_FIRST_IN_APP, 0);
        nInAppCount++;
        SPUtils.setInformain(KeySharePreferences.IS_FIRST_IN_APP, nInAppCount);

        EventBus.getDefault().register(this);
        showDrawDialog();
        //检查浮标
        mViewModel.openWindFastNewPeriod.observe(this, result -> {
            if (result == null) {
                return;
            }
            if (result) {
                maskUtils.addMark(lotteryItem);
            } else {
                maskUtils.removeMark();
            }
        });
        if (!ABSwitch.Ins().isOpenAB()) {
            lotteryItem.getViewTreeObserver().addOnGlobalLayoutListener(layoutListener);
        }

        //预加载一个激励视频
        AdVideoCacheUtils.INSTANCE.cacheRewardVideo(this);
        //上报一个测试友盟多参数事件
        testUMMuliParams();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    //上报测试多参数事件
    private void testUMMuliParams() {
        if (BuildConfig.DEBUG) {
            HashMap<String, Object> para = new HashMap<>();
            String paramsValue = "testParams" + new Random().nextInt(10);
            para.put("source", paramsValue);
            AnalysisUtils.onEventEx(this, "TEST_ABCDEF", para);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginEvent(LoginUserStatus status) {
        if (status.getStatus() == 1 && AppInfo.checkIsWXLogin()) {
            //登录刷新广告id
            String url = HttpConfigUtilsKt.withConfigParams(BuildConfig.AD_ID_CONFIG, false);
            AdManager.INSTANCE.initAdIdConfig(url);
        }
    }


    /**
     * 显示开奖弹框
     */
    private void showDrawDialog() {
        if (ABSwitch.Ins().isOpenAB()) {
            return;
        }

        if (DateManager.getInstance().ifFirst(DateManager.SHOW_DIALOG_WHEN_LAUNCH)
                && SPUtils.getInformain(KeySharePreferences.SHOW_DIALOG_WHEN_LAUNCH, true)) {
            SPUtils.setInformain(KeySharePreferences.SHOW_DIALOG_WHEN_LAUNCH, true);
        }

        boolean logType = AppInfo.checkIsWXLogin();
        if (logType) {
            DrawDialog mDrawDialog = new DrawDialog();
            mDrawDialog.setEventListener(new DrawDialog.EventListener() {
                @Override
                public void switchPage() {
                    if (mNavigationController != null) {
                        mPosition = 2;
                        mNavigationController.setSelect(mPosition);
                    }
                }

                @Override
                public void dismiss() {
                    if (mDrawDialog.isAdded()) {
                        mDrawDialog.dismiss();
                    }
                    if (SPUtils.getInformain(KeySharePreferences.SHOW_DIALOG_WHEN_LAUNCH, true)) {
                        new EnterShowDialog(MainActivity.this).showEx();
                    }
                }

                @Override
                public void show() {
                    if (!MainActivity.this.isFinishing()) {
                        mDrawDialog.show(getSupportFragmentManager(), "DrawDialog");
                    }
                }
            });
            mDrawDialog.requestGoodsInfo(getApplicationContext());
        } else {
            FreePanicBuyingDialog mFreePanicBuyingDialog = new FreePanicBuyingDialog(MainActivity.this);
            mFreePanicBuyingDialog.setFinishListener(new FreePanicBuyingDialog.OnFinishListener() {
                @Override
                public void onDismiss() {
                    if (SPUtils.getInformain(KeySharePreferences.SHOW_DIALOG_WHEN_LAUNCH, true)) {
                        new EnterShowDialog(MainActivity.this).showEx();
                    }
                }

                @Override
                public void onShow() {
                    if (mFreePanicBuyingDialog != null && !MainActivity.this.isFinishing()) {
                        mFreePanicBuyingDialog.show();
                    }
                }
            });
            mFreePanicBuyingDialog.requestGoodsInfo(getApplicationContext());
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        ARouter.getInstance().inject(this);
        if (mNavigationController != null) {
            mNavigationController.setSelect(mPosition);
        }
    }

    private void initView(int position) {
        ARouter.getInstance().inject(this);
        int checkColor = getResources().getColor(R.color.common_btn_color_sec);
        int defaultColor = getResources().getColor(R.color.common_D5D5D5);

        if (!ABSwitch.Ins().isOpenAB()) {
            MainBottomTanItem homeItem = new MainBottomTanItem(this);
            homeItem.initialization("首页", R.drawable.main_home_checked, defaultColor, checkColor,
                    "main_bottom_tab_home.json");

            MainBottomTanItem showTimeItem = new MainBottomTanItem(this);
            showTimeItem.initialization("晒单", R.drawable.main_showtime, defaultColor, checkColor,
                    "main_bottom_tab_shaidan.json");

            lotteryItem = new MainBottomTanItem(this);
            lotteryItem.initialization("开奖", R.drawable.main_lottery, defaultColor, checkColor,
                    "main_bottom_tab_kaijiang.json");

            MainBottomTanItem buyItem = new MainBottomTanItem(this);
            buyItem.initialization("省钱购", R.drawable.main_buy, defaultColor, checkColor,
                    "main_bottom_tab_shengqiangou.json");

            MainBottomTanItem mineItem = new MainBottomTanItem(this);
            mineItem.initialization("我的", R.drawable.main_mine, defaultColor, checkColor, "main_bottom_tab_me.json");

            mNavigationController = mDataBinding.bottomView.custom()
                    .addItem(homeItem)
                    .addItem(showTimeItem)
                    .addItem(lotteryItem)
                    .addItem(buyItem)
                    .addItem(mineItem)
                    .enableAnimateLayoutChanges()
                    .build();
        } else {
            MainBottomTanItem homeItem = new MainBottomTanItem(this);
            homeItem.initialization("首页", R.drawable.main_home_checked, defaultColor, checkColor,
                    "main_bottom_tab_home.json");
            MainBottomTanItem lotteryItem = new MainBottomTanItem(this);
            lotteryItem.initialization("马上抢", R.drawable.main_lottery, defaultColor, checkColor,
                    "main_bottom_tab_kaijiang.json");

            MainBottomTanItem mineItem = new MainBottomTanItem(this);
            mineItem.initialization("设置", R.drawable.main_mine, defaultColor, checkColor, "main_bottom_tab_me.json");

            mNavigationController = mDataBinding.bottomView.custom()
                    .addItem(homeItem)
                    .addItem(lotteryItem)
                    .addItem(mineItem)
                    .enableAnimateLayoutChanges()
                    .build();
        }
        mNavigationController.showBottomLayout();
        mDataBinding.cvContentView.setOffscreenPageLimit(4);
        mDataBinding.cvContentView.setAdapter(adapter);
        mNavigationController.setupWithViewPager(mDataBinding.cvContentView);

        mDataBinding.cvContentView.setCurrentItem(position);
        mNavigationController.addSimpleTabItemSelectedListener((index, old) -> {
            toggleStatusBar(index);
            mPosition = index;
        });
        AppStatusManager.getInstance().setAppStatus(AppStatusConstant.STATUS_NORMAL);

        if (ABSwitch.Ins().isOpenAB()) {
            mDataBinding.mainFloatingBtn.setVisibility(View.GONE);
        } else {
            mDataBinding.mainFloatingBtn.setVisibility(View.VISIBLE);
            mDataBinding.mainFloatingBtn.setOnClickListener(v -> {
                toggleStatusBar(0);
                mDataBinding.cvContentView.setCurrentItem(0);
                mPosition = 0;
            });
        }

        int intoFrontCounts = SPUtils.getInformain(KeySharePreferences.INTO_FRONT_COUNTS, 0);
        if (!SPUtils.getInformain(KeySharePreferences.HAS_DO_INTO_FRONT, false)) {
            intoFrontCounts++;
            SPUtils.setInformain(KeySharePreferences.INTO_FRONT_COUNTS, intoFrontCounts);
        }

        if (ABSwitch.Ins().isOpenHomeGuid() != 0
                && SPUtils.getInformain(KeySharePreferences.INTO_FRONT_COUNTS, 0) >= ABSwitch.Ins().isOpenHomeGuid()
                && !SPUtils.getInformain(KeySharePreferences.HAS_DO_INTO_FRONT, false)) {
            mDataBinding.mainHomeGuidCl.setVisibility(View.VISIBLE);
            mDataBinding.mainHomeBtn.setOnClickListener(v -> {
                mDataBinding.mainHomeGuidCl.setVisibility(View.GONE);
                toggleStatusBar(3);
                mDataBinding.cvContentView.setCurrentItem(3);
                mPosition = 3;
            });
        } else {
            mDataBinding.mainHomeGuidCl.setVisibility(View.GONE);
        }
    }

    /**
     * 状态栏和导航栏刷新
     *
     * @param position
     */
    private void toggleStatusBar(int position) {
        switch (position) {
            case 0:
                AnalysisUtils.onEventEx(this, Dot.Page_Home);
                AnalysisUtils.onEventEx(this, Dot.Btn_Home);
                if (mViewModel.openWindFastNewPeriod.getValue() == null) {
                    //重新更新一次
                    mViewModel.checkMaskData(this);
                }
                break;
            case 1:
                AnalysisHelp.onEvent(this, AnalysisParam.TO_BENEFIT_BOTTOM_NAV);
                AnalysisUtils.onEventEx(this, Dot.Page_ShowTime);
                AnalysisUtils.onEventEx(this, Dot.Btn_ShowTime);
                break;
            case 2:
                AnalysisUtils.onEventEx(this, Dot.Page_Lottery);
                AnalysisUtils.onEventEx(this, Dot.Btn_Lottery);
                if (mViewModel.getFastOpenWindPeriod() != null) {
                    mViewModel.updateLocalOpenWindPeriod();
                }
                break;
            case 3:
                AnalysisHelp.onEvent(this, AnalysisParam.TO_BENEFIT_BOTTOM_NAV);
                AnalysisUtils.onEventEx(this, Dot.Page_SaveMoneyBuy);
                AnalysisUtils.onEventEx(this, Dot.Btn_SaveMoneyBuy);
                SPUtils.setInformain(KeySharePreferences.INTO_FRONT_COUNTS, 0);
                SPUtils.setInformain(KeySharePreferences.HAS_DO_INTO_FRONT, true);
                break;
            case 4:
                AnalysisHelp.onEvent(this, AnalysisParam.TO_BENEFIT_BOTTOM_NAV);
                AnalysisUtils.onEventEx(this, Dot.Page_UserCenter);
                AnalysisUtils.onEventEx(this, Dot.Btn_UserCenter);
                break;
            default:
        }
    }

    private void initFragment() {
        fragments = new ArrayList<>();
        //通过ARouter 获取其他组件提供的fragment
        if (ABSwitch.Ins().isOpenAB()) {
            fragments.add((Fragment) ARouter.getInstance().build(RouterFragmentPath.Home.PAGER_HOME).navigation());
            fragments.add((Fragment) ARouter.getInstance().build(RouterFragmentPath.Spike.PAGER_SPIKE).navigation());
            fragments.add(
                    (Fragment) ARouter.getInstance().build(RouterFragmentPath.User.PAGER_USER_SETTING).navigation());
        } else {
            fragments.add((Fragment) ARouter.getInstance()
                    .build(RouterFragmentPath.Front.PAGER_FRONT)
                    .navigation());
            fragments.add(RouterFragmentPath.Unboxing.getUnboxingFragment());
            fragments.add(RouterFragmentPath.User.getMineOpenWinFragment(
                    0, true, false, true, 1));
            fragments.add((Fragment) ARouter.getInstance().build(RouterFragmentPath.Home.PAGER_HOME).navigation());
            fragments.add((Fragment) ARouter.getInstance().build(RouterFragmentPath.User.PAGER_USER).navigation());
        }

        adapter = new MainPageAdapter(getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.setData(fragments);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.main_activity_main;
    }

    @Override
    public void initView() {
        int statusBarColor = R.color.main_color_bar;
        if (ABSwitch.Ins().isOpenAB()) {
            statusBarColor = R.color.white;
        }
        ImmersionBar.with(this)
                .statusBarColor(statusBarColor)
                .navigationBarColor(R.color.black)
                .fitsSystemWindows(true)
                .autoDarkModeEnable(true)
                .init();
        initFragment();
        initView(mPosition);
        // 获取数美deviceId之后，调用refresh接口刷新
        CommonParams.setNetWork();

        checkAppUpdate();
    }

    /**
     * 检测更新
     */
    private void checkAppUpdate() {
        UpdateManager.getInstance().checkUpdate(this, false);
        //定时检查更新
        UpdateReceiver updateReceiver = new UpdateReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_TIME_TICK);
        getApplication().registerReceiver(updateReceiver, intentFilter);
    }

    @Override
    public void onBackPressed() {
        if (!ABSwitch.Ins().isOpenAB()) {
            ExitInterceptUtils.INSTANCE.intercept(this);
        } else {
            long duration = System.currentTimeMillis() - mFirstClickBackTime;
            if (duration < 2000) {
                // 程序关闭
                AppStatusManager.getInstance().setAppStatus(AppStatusConstant.STATUS_FORCE_KILLED);
                AnalysisUtils.onEvent(this, AnalysisParam.SHUTDOWN);
                AppManager.getInstance().AppExit();
                finish();
            } else {
                Toast.makeText(this, "再按一次退出！", Toast.LENGTH_SHORT).show();
                mFirstClickBackTime = System.currentTimeMillis();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtil.e("MainActivity onStop");
    }

    @Override
    protected void onDestroy() {
        LogUtil.e("MainActivity onDestroy");

        ExitInterceptUtils.resetFinishBackStatus();
        ImmersionBar.destroy(this, null);
        AppStatusManager.getInstance().setAppStatus(AppStatusConstant.STATUS_FORCE_KILLED);
        EventBus.getDefault().unregister(this);
        if (fragments != null) {
            fragments.clear();
            fragments = null;
        }
        if (adapter != null) {
            adapter.clear();
            adapter = null;
        }

        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults,
                ExitInterceptUtils.INSTANCE.getRemindDialog());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetMessage(RedPackageStatus redPackageStatus) {
        if (redPackageStatus.getStatus() == 0) {
            mDataBinding.mainFloatingBtn.setProgress(redPackageStatus.getCounts());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDoubleRpEvent(DoubleRpEvent event) {
        if (event.getEvent() == 1) {
            //双倍领取红包ok
            postGotDoubleRp(event.getRestId());
        }
    }

    private void postGotDoubleRp(String restId) {
        mViewModel.postDoubleRp(restId).observe(this, doubleRedPacketBean -> {
            if (doubleRedPacketBean == null) {
                ToastUtil.showShort(MainActivity.this, "获取双倍奖励失败");
                return;
            }

            AnAdditionalDialog mDrawDialog = new AnAdditionalDialog(
                    String.valueOf(doubleRedPacketBean.getScore()), 4);
            mDrawDialog.setEventListener(() -> {
                if (mDrawDialog.isAdded()) {
                    mDrawDialog.dismiss();
                }
            });
            mDrawDialog.show(getSupportFragmentManager(), "AnAddDialog");
            EventBus.getDefault().post(new WalletRefreshEvent(0));
            AnalysisUtils.onEventEx(this, Dot.But_Rp_Double);
        });
    }
}