package com.donews.main.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.dn.sdk.AdLoadManager;
import com.dn.sdk.bean.RequestInfo;
import com.dn.sdk.constant.AdIdConfig;
import com.donews.base.activity.MvvmBaseLiveDataActivity;
import com.donews.base.base.AppManager;
import com.donews.base.base.AppStatusConstant;
import com.donews.base.base.AppStatusManager;
import com.donews.base.storage.MmkvHelper;
import com.donews.base.viewmodel.BaseLiveDataViewModel;
import com.donews.common.adapter.ScreenAutoAdapter;
import com.donews.common.router.RouterActivityPath;
import com.donews.common.router.RouterFragmentPath;
import com.donews.common.updatedialog.UpdateManager;
import com.donews.main.R;
import com.donews.main.adapter.MainPageAdapter;
import com.donews.main.common.CommonParams;
import com.donews.main.databinding.MainActivityMainBinding;
import com.donews.utilslibrary.analysis.AnalysisHelp;
import com.donews.utilslibrary.analysis.AnalysisParam;
import com.donews.utilslibrary.analysis.AnalysisUtils;
import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;

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
    private long mInterval = 0; // 兩次返回鍵的间隔时间


    public static void start(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        ScreenAutoAdapter.match(this, 375.0f);
        super.onCreate(savedInstanceState);
    }


    private void initView(int position) {
        int checkColor = getResources().getColor(R.color.common_btn_color_sec);
        int defaultColor = getResources().getColor(R.color.common_AEAEAE);

        mNavigationController = mDataBinding.bottomView.material()
                .addItem(R.drawable.main_home_checked, "首页", checkColor)
                .addItem(R.drawable.main_mail9_normal, "9.9包邮", checkColor)
                .addItem(R.drawable.main_seckill_normal, "马上抢", checkColor)
////                .addItem(R.drawable.main_notify, "福利", checkColor)
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
            }
        });
        AppStatusManager.getInstance().setAppStatus(AppStatusConstant.STATUS_NORMAL);
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
                break;
            case 1:
                AnalysisHelp.onEvent(this, AnalysisParam.TO_BENEFIT_BOTTOM_NAV);
                ImmersionBar.with(this)
                        .statusBarColor(R.color.FF8030)
//                        .statusBarColor(R.color.transparent)
                        .navigationBarColor(R.color.white)
                        .fitsSystemWindows(true)
                        .autoDarkModeEnable(true)
                        .init();
                break;
            case 3:
                AnalysisHelp.onEvent(this, AnalysisParam.TO_BENEFIT_BOTTOM_NAV);
                ImmersionBar.with(this)
                        .statusBarColor(R.color.main_color_bar)
                        .navigationBarColor(R.color.white)
                        .fitsSystemWindows(true)
                        .autoDarkModeEnable(true)
                        .init();
                break;
            default:
        }
    }

    private void initFragment() {
        fragments = new ArrayList<>();
        //通过ARouter 获取其他组件提供的fragment
//        Fragment homeFragment = (Fragment) ARouter.getInstance().build(RouterFragmentPath.Home.PAGER_HOME).navigation();
//        Fragment userFragment = (Fragment) ARouter.getInstance().build(RouterFragmentPath.User.PAGER_USER_SETTING).navigation();

        fragments.add(
                (Fragment) ARouter.getInstance()
                        .build(RouterFragmentPath.Home.PAGER_HOME)
                        .navigation());
        fragments.add(
                (Fragment) ARouter.getInstance()
                        .build(RouterFragmentPath.Mail.PAGE_MAIL_PACKAGE)
                        .navigation());
        fragments.add(
                (Fragment) ARouter.getInstance()
                        .build(RouterFragmentPath.Spike.PAGER_SPIKE)
                        .navigation());
        fragments.add(
                (Fragment) ARouter.getInstance()
//                        .build(RouterFragmentPath.User.PAGER_USER)
                        .build(RouterFragmentPath.User.PAGER_USER_SETTING)
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
        initView(0);
        // 获取数美deviceId之后，调用refresh接口刷新
        CommonParams.setNetWork();
        CommonParams.getCommonNetWork();

        AdLoadManager.getInstance()
                .cacheRewardVideo(this, new RequestInfo(AdIdConfig.REWARD_VIDEO_ID), null);

        UpdateManager.getInstance().checkUpdate(this, false);
    }

    @Override
    public void onBackPressed() {
        long spent = System.currentTimeMillis() - mInterval;
        long mDiffTime = 2000;
        if (spent < mDiffTime) {
            // 程序关闭
            AppStatusManager.getInstance().setAppStatus(AppStatusConstant.STATUS_FORCE_KILLED);
            AnalysisUtils.onEvent(this, AnalysisParam.SHUTDOWN);
            AppManager.getInstance().AppExit();
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "再按一次退出！",
                    Toast.LENGTH_SHORT).show();
            mInterval = System.currentTimeMillis();
        }

    }

    @Override
    protected void onDestroy() {
        ImmersionBar.destroy(this, null);
        AppStatusManager.getInstance().setAppStatus(AppStatusConstant.STATUS_FORCE_KILLED);
        super.onDestroy();
    }
}