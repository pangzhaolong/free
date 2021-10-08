package com.donews.base.fragment;

import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;

import java.util.List;

/**
 * <p>
 * 扩展Fragment及Activity,使用add+show+hide模式下的Fragment的懒加载
 * 当调用 Fragment.showHideFragment ，确保已经先调用 Fragment.loadFragmentsFragment
 * 当调用 FragmentActivity.showHideFragment，确保已经先调用 FragmentActivity.loadFragmentsActivity
 * </p>
 * 作者： created by honeylife<br>
 * 日期： 2021/3/30 13:38<br>
 * 版本：V1.0<br>
 */
public class FragmentShowHideManager {
    /**
     * 加载根Fragment
     * 如果在fragment里面嵌套fragment，就调用此方法
     * @param containerViewId 布局id
     * @param rootFragment    根fragment
     * @param childFragmentManager    fragment的manger
     */
    public static void loadRootFragment(@IdRes int containerViewId, FragmentManager childFragmentManager, Fragment rootFragment) {
        loadFragmentsTransaction(containerViewId, 0, childFragmentManager, rootFragment);
    }

    /**
     * 加载同级的Fragment
     *  fragment里面嵌套好几个fragment的时候，要先调用这个方法
     * @param containerViewId 布局id
     * @param showPosition  默认显示的角标
     * @param fragments    加载的fragment
     * @param childFragmentManager  fragment的manger
     *
     */
    public static void loadFragmentsFragment(
            @IdRes int containerViewId,
           int showPosition,
            FragmentManager childFragmentManager,
            Fragment...fragments
    ) {
        loadFragmentsTransaction(containerViewId, showPosition, childFragmentManager,  fragments);
    }

    /**
     * 显示目标fragment，并隐藏其他fragment
     * fragment中调用
     * @param childFragmentManager manager
     * @param showFragment 需要显示的fragment
     */
    public static void showHideFragment(FragmentManager childFragmentManager,Fragment showFragment) {
        showHideFragmentTransaction(childFragmentManager, showFragment);
    }


    /**
     * 加载根Fragment
     * activity中嵌套fragment的时候，需要调用此方法
     * @param fragmentActivity  上下文
     * @param containerViewId 布局id
     * @param rootFragment  根fragment
     */
    public static void loadRootFragmentActivity(FragmentActivity fragmentActivity,@IdRes int containerViewId, Fragment rootFragment) {
        loadFragmentsTransaction(containerViewId, 0, fragmentActivity.getSupportFragmentManager(), rootFragment);
    }

    /**
     * 加载同级的Fragment
     *activity中嵌套多个fragment的时候，需要调用此方法
     * @param containerViewId 布局id
     * @param showPosition    默认显示的角标
     * @param fragments       加载的fragment
     */
    public static void loadFragmentsActivity( FragmentActivity fragmentActivity,
            @IdRes int containerViewId,
            int showPosition,
            Fragment... fragments
    ) {
        loadFragmentsTransaction(containerViewId, showPosition, fragmentActivity.getSupportFragmentManager(),  fragments);
    }

    /**
     * 显示目标fragment，并隐藏其他fragment
     * activity中调用
     * @param showFragment 需要显示的fragment
     */
    public static void showHideFragment(FragmentActivity fragmentActivity, Fragment showFragment) {
        showHideFragmentTransaction(fragmentActivity.getSupportFragmentManager(), showFragment);
    }

    /**
     * 使用add+show+hide模式加载fragment
     * <p>
     * 默认显示位置[showPosition]的Fragment，最大Lifecycle为Lifecycle.State.RESUMED
     * 其他隐藏的Fragment，最大Lifecycle为Lifecycle.State.STARTED
     *
     * @param containerViewId 容器id
     * @param showPosition    fragments
     * @param fragmentManager FragmentManager
     * @param fragments       控制显示的Fragments
     */
    public static void loadFragmentsTransaction(
            @IdRes int containerViewId,
            int showPosition,
            FragmentManager fragmentManager,
            Fragment... fragments
    ) {
        if (fragments.length > 0) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            for (int index = 0; index < fragments.length; index++) {
                Fragment fragment = fragments[index];
                fragmentTransaction.add(containerViewId, fragment, fragment.getTag());
                if (showPosition == index) {
                    fragmentTransaction.setMaxLifecycle(fragment, Lifecycle.State.RESUMED);
                } else {
                    fragmentTransaction.hide(fragment);
                    fragmentTransaction.setMaxLifecycle(fragment, Lifecycle.State.STARTED);
                }
            }
            fragmentTransaction.commit();
        } else {
            throw new IllegalStateException("fragments must not empty");
        }
    }


    /**
     * 显示需要显示的Fragment[showFragment]，并设置其最大Lifecycle为Lifecycle.State.RESUMED。
     * 同时隐藏其他Fragment,并设置最大Lifecycle为Lifecycle.State.STARTED
     *
     * @param fragmentManager
     * @param showFragment
     */
    public static void showHideFragmentTransaction(FragmentManager fragmentManager, Fragment showFragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.show(showFragment);
        fragmentTransaction.setMaxLifecycle(showFragment, Lifecycle.State.RESUMED);
        //获取其中所有的fragment,其他的fragment进行隐藏
        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment fragment : fragments) {
            if (fragment != showFragment) {
                fragmentTransaction.hide(fragment);
                fragmentTransaction.setMaxLifecycle(fragment, Lifecycle.State.STARTED);
            }
        }
    }


}
