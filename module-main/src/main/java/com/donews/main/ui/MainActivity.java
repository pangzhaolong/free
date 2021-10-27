package com.donews.main.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.dn.events.events.NavEvent;
import com.dn.sdk.business.loader.AdManager;
import com.dn.sdk.sdk.interfaces.listener.impl.SimpleInterstListener;
import com.dn.sdk.sdk.interfaces.listener.impl.SimpleRewardVideoListener;
import com.donews.base.activity.MvvmBaseLiveDataActivity;
import com.donews.base.base.AppStatusConstant;
import com.donews.base.base.AppStatusManager;
import com.donews.base.viewmodel.BaseLiveDataViewModel;
import com.donews.common.adapter.ScreenAutoAdapter;
import com.donews.common.router.RouterActivityPath;
import com.donews.common.router.RouterFragmentPath;
import com.donews.common.updatedialog.UpdateManager;
import com.donews.main.R;
import com.donews.main.adapter.MainPageAdapter;
import com.donews.main.common.CommonParams;
import com.donews.main.databinding.MainActivityMainBinding;
import com.donews.main.utils.ExitInterceptUtils;
import com.donews.utilslibrary.analysis.AnalysisHelp;
import com.donews.utilslibrary.analysis.AnalysisParam;
import com.donews.utilslibrary.analysis.AnalysisUtils;
import com.donews.utilslibrary.dot.Dot;
import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;
import com.orhanobut.logger.Logger;
import com.vmadalin.easypermissions.EasyPermissions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import me.majiajie.pagerbottomtabstrip.NavigationController;
import me.majiajie.pagerbottomtabstrip.listener.SimpleTabItemSelectedListener;

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
        EventBus.getDefault().register(this);
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
        int defaultColor = getResources().getColor(R.color.common_AEAEAE);

        mNavigationController = mDataBinding.bottomView.material()
                .addItem(R.drawable.main_home_checked, "首页", checkColor)
                .addItem(R.drawable.main_uesr_normal, "晒单", checkColor)
                .addItem(R.drawable.main_seckill_normal, "开奖", checkColor)
                .addItem(R.drawable.main_buy, "省钱购", checkColor)
                .addItem(R.drawable.main_uesr_normal, "我的", checkColor)
                .setDefaultColor(defaultColor)
                .enableAnimateLayoutChanges()
                .build();
        mNavigationController.showBottomLayout();
        mDataBinding.cvContentView.setOffscreenPageLimit(4);
        mDataBinding.cvContentView.setAdapter(adapter);
        mNavigationController.setupWithViewPager(mDataBinding.cvContentView);

        mDataBinding.cvContentView.setCurrentItem(position);
        mNavigationController.addSimpleTabItemSelectedListener(new SimpleTabItemSelectedListener() {
            @Override
            public void onSelected(int index, int old) {
                toggleStatusBar(index);
                mPosition = index;
            }
        });
        AppStatusManager.getInstance().setAppStatus(AppStatusConstant.STATUS_NORMAL);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetMessage(NavEvent navEvent) {
        if (navEvent.navIndex == 2) {
            mDataBinding.cvContentView.setCurrentItem(2);
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
                ImmersionBar.with(this)
                        .statusBarColor(R.color.main_color_bar)
                        .navigationBarColor(R.color.white)
                        .fitsSystemWindows(true)
                        .autoDarkModeEnable(true)
                        .init();
                AnalysisUtils.onEventEx(this, Dot.Btn_Home);
                break;
            case 1:
                AnalysisHelp.onEvent(this, AnalysisParam.TO_BENEFIT_BOTTOM_NAV);
                ImmersionBar.with(this)
                        .statusBarColor(R.color.white)
                        .navigationBarColor(R.color.white)
                        .fitsSystemWindows(true)
                        .autoDarkModeEnable(true)
                        .init();
                AnalysisUtils.onEventEx(this, Dot.Btn_ShowTime);
                break;
            case 2:
                AnalysisUtils.onEventEx(this, Dot.Btn_Lottery);
                ImmersionBar.with(this)
                        .statusBarColor(R.color.text_red)
                        .navigationBarColor(R.color.white)
                        .fitsSystemWindows(true)
                        .autoDarkModeEnable(true)
                        .init();
                break;
            case 3:
                AnalysisHelp.onEvent(this, AnalysisParam.TO_BENEFIT_BOTTOM_NAV);
                ImmersionBar.with(this)
                        .statusBarColor(R.color.white)
                        .navigationBarColor(R.color.white)
                        .fitsSystemWindows(true)
                        .autoDarkModeEnable(true)
                        .init();
                AnalysisUtils.onEventEx(this, Dot.Btn_SaveMoneyBuy);
                break;
            case 4:
                AnalysisHelp.onEvent(this, AnalysisParam.TO_BENEFIT_BOTTOM_NAV);
                ImmersionBar.with(this)
                        .statusBarColor(R.color.text_red)
                        .navigationBarColor(R.color.white)
                        .fitsSystemWindows(true)
                        .autoDarkModeEnable(true)
                        .init();
                AnalysisUtils.onEventEx(this, Dot.Btn_UserCenter);
                break;
            default:
        }
    }

    private void initFragment() {
        fragments = new ArrayList<>();
        //通过ARouter 获取其他组件提供的fragment

//        Fragment userFragment = (Fragment) ARouter.getInstance().build(RouterFragmentPath.User.PAGER_USER_SETTING)
//        .navigation();

        fragments.add(
                (Fragment) ARouter.getInstance()
                        .build(RouterFragmentPath.Front.PAGER_FRONT)
                        .navigation());
        fragments.add(RouterFragmentPath.Unboxing.getUnboxingFragment());
//        fragments.add(
//                (Fragment) ARouter.getInstance()
//                        .build(RouterFragmentPath.Spike.PAGER_SPIKE)
//                        .navigation());
        //开奖页面
        fragments.add(RouterFragmentPath.User.getMineOpenWinFragment(
                0, false, true));
        fragments.add((Fragment) ARouter.getInstance().build(RouterFragmentPath.Home.PAGER_HOME).navigation());
        fragments.add(
                (Fragment) ARouter.getInstance()
                        .build(RouterFragmentPath.User.PAGER_USER)
                        .navigation());
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
        ImmersionBar.with(this)
                .statusBarColor(R.color.main_color_bar)
                .navigationBarColor(R.color.white)
                .hideBar(BarHide.FLAG_HIDE_NAVIGATION_BAR)
                .fitsSystemWindows(true)
                .autoDarkModeEnable(true)
                .init();
        initFragment();
        initView(mPosition);
        // 获取数美deviceId之后，调用refresh接口刷新
        CommonParams.setNetWork();

        //此接口不需要
//        CommonParams.getCommonNetWork();

        UpdateManager.getInstance().checkUpdate(this, false);
    }

    @Override
    public void onBackPressed() {
        ExitInterceptUtils.INSTANCE.intercept(this);

//        ARouter.getInstance().build(RouterActivityPath.Rp.PAGE_RP).navigation();
    }

    @Override
    protected void onDestroy() {
        ImmersionBar.destroy(this, null);
        AppStatusManager.getInstance().setAppStatus(AppStatusConstant.STATUS_FORCE_KILLED);
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults,
                ExitInterceptUtils.INSTANCE.getRemindDialog());
    }
}