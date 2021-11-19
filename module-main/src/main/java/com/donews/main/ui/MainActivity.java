package com.donews.main.ui;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.dn.events.events.RedPackageStatus;
import com.donews.base.base.AppManager;
import com.donews.base.base.AppStatusConstant;
import com.donews.base.base.AppStatusManager;
import com.donews.base.viewmodel.BaseLiveDataViewModel;
import com.donews.common.adapter.ScreenAutoAdapter;
import com.donews.common.base.MvvmBaseLiveDataActivity;
import com.donews.common.router.RouterActivityPath;
import com.donews.common.router.RouterFragmentPath;
import com.donews.common.updatedialog.UpdateManager;
import com.donews.common.updatedialog.UpdateReceiver;
import com.donews.main.R;
import com.donews.main.adapter.MainPageAdapter;
import com.donews.main.common.CommonParams;
import com.donews.main.databinding.MainActivityMainBinding;
import com.donews.main.dialog.DrawDialog;
import com.donews.main.dialog.EnterShowDialog;
import com.donews.main.dialog.FreePanicBuyingDialog;
import com.donews.main.utils.ExitInterceptUtils;
import com.donews.main.views.MainBottomTanItem;
import com.donews.middle.abswitch.ABSwitch;
import com.donews.utilslibrary.analysis.AnalysisHelp;
import com.donews.utilslibrary.analysis.AnalysisParam;
import com.donews.utilslibrary.analysis.AnalysisUtils;
import com.donews.utilslibrary.dot.Dot;
import com.donews.utilslibrary.utils.AppInfo;
import com.donews.utilslibrary.utils.KeySharePreferences;
import com.donews.utilslibrary.utils.SPUtils;
import com.gyf.immersionbar.ImmersionBar;
import com.vmadalin.easypermissions.EasyPermissions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import me.majiajie.pagerbottomtabstrip.NavigationController;

/**
 * app 主页面
 *
 * @author darryrzhoong
 */

@Route(path = RouterActivityPath.Main.PAGER_MAIN)
public class MainActivity
        extends MvvmBaseLiveDataActivity<MainActivityMainBinding, BaseLiveDataViewModel> {

    private List<Fragment> fragments;

    private MainPageAdapter adapter;

    private NavigationController mNavigationController;

    private EnterShowDialog mEnterShowDialog;

    private long mFirstClickBackTime = 0;
    /**
     * 初始选择tab
     */
    @Autowired(name = "position")
    int mPosition = 0;

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
        EventBus.getDefault().register(this);
        showDrawDialog();
    }

    /**
     * 显示开奖弹框
     */
    private void showDrawDialog() {
        if (ABSwitch.Ins().isOpenAB()) {
            return;
        }
        if (AppInfo.checkIsWXLogin()) {
            if (SPUtils.getInformain(KeySharePreferences.SHOW_DIALOG_WHEN_LAUNCH, true)) {
                if ((mEnterShowDialog != null) && (!mEnterShowDialog.isShowing())) {
                    mEnterShowDialog.show();
                }
            }
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
                }

                @Override
                public void show() {
                    mDrawDialog.show(getSupportFragmentManager(), "DrawDialog");
                }
            });
            mDrawDialog.requestGoodsInfo(getApplicationContext());
        } else {
            FreePanicBuyingDialog mFreePanicBuyingDialog = new FreePanicBuyingDialog(MainActivity.this);
            mFreePanicBuyingDialog.setFinishListener(new FreePanicBuyingDialog.OnFinishListener() {
                @Override
                public void onDismiss() {
                    if (mFreePanicBuyingDialog != null) {
                        new EnterShowDialog(MainActivity.this).show();
                    }
                }

                @Override
                public void onShow() {
                    if (mFreePanicBuyingDialog != null) {
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

            MainBottomTanItem lotteryItem = new MainBottomTanItem(this);
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

        mDataBinding.mainFloatingBtn.setOnClickListener(v -> {
            toggleStatusBar(0);
            mDataBinding.cvContentView.setCurrentItem(0);
            mPosition = 0;
        });

        mEnterShowDialog = new EnterShowDialog(this);
        mEnterShowDialog.create();
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
                break;
            case 1:
                AnalysisHelp.onEvent(this, AnalysisParam.TO_BENEFIT_BOTTOM_NAV);
                AnalysisUtils.onEventEx(this, Dot.Page_ShowTime);
                AnalysisUtils.onEventEx(this, Dot.Btn_ShowTime);
                break;
            case 2:
                AnalysisUtils.onEventEx(this, Dot.Page_Lottery);
                AnalysisUtils.onEventEx(this, Dot.Btn_Lottery);
                break;
            case 3:
                AnalysisHelp.onEvent(this, AnalysisParam.TO_BENEFIT_BOTTOM_NAV);
                AnalysisUtils.onEventEx(this, Dot.Page_SaveMoneyBuy);
                AnalysisUtils.onEventEx(this, Dot.Btn_SaveMoneyBuy);
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
            fragments.add(RouterFragmentPath.User.getMineOpenWinFragment(0, false, true));
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
    protected void onDestroy() {
        ImmersionBar.destroy(this, null);
        AppStatusManager.getInstance().setAppStatus(AppStatusConstant.STATUS_FORCE_KILLED);
        EventBus.getDefault().unregister(this);
        if (mEnterShowDialog != null) {
            mEnterShowDialog.dismiss();
            mEnterShowDialog = null;
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
}