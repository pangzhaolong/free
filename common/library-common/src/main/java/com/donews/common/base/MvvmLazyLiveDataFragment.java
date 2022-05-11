package com.donews.common.base;

import android.content.Context;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.donews.base.base.DataBindingVars;
import com.donews.base.fragmentdialog.LoadingHintDialog;
import com.donews.base.utils.ClassUtil;
import com.donews.base.viewmodel.BaseLiveDataViewModel;

import io.reactivex.subjects.BehaviorSubject;

/**
 * 应用模块:fragment
 * <p>
 * 类描述: 配置懒加载的fragment(支持fragment嵌套懒加载)
 * 如果是ViewPager中嵌套fragment，记得必须将behavior的值传FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
 * FragmentPagerAdapter中的setPrimaryItem中已经设置了lifecycle的state状态。
 * <p>
 * <p>
 * 作者： created by honeylife<br>
 * 日期：2020-01-28
 */
public abstract class MvvmLazyLiveDataFragment<V extends ViewDataBinding, VM extends BaseLiveDataViewModel>
        extends Fragment {
    protected V mDataBinding;

    protected VM mViewModel;
    BehaviorSubject<Lifecycle.Event> behaviorSubject = BehaviorSubject.create();

    protected LoadingHintDialog loadingHintDialog;

    protected String mFragmentTag = this.getClass().getSimpleName();
    protected static final String TAG = "MvvmLazyLiveDataFragment";

    /**
     * Fragment生命周期 onAttach -> onCreate -> onCreatedView -> onActivityCreated
     * -> onStart -> onResume -> onPause -> onStop -> onDestroyView -> onDestroy
     * -> onDetach 对于 ViewPager + Fragment 的实现我们需要关注的几个生命周期有： onCreatedView +
     * onActivityCreated + onResume + onPause + onDestroyView
     */

    protected View rootView = null;

    /**
     * 是否第一次可见
     */
    protected boolean mIsFirstVisible = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void showLoading() {
        if (loadingHintDialog != null) {
            hideLoading();
        }
        loadingHintDialog = new LoadingHintDialog();
        loadingHintDialog.setDismissOnBackPressed(false)
                .setDescription("提交中...")
                .show(getChildFragmentManager(), "user_cancellation");
    }

    protected void showLoading(String msg) {
        if (loadingHintDialog != null) {
            hideLoading();
        }
        loadingHintDialog = new LoadingHintDialog();
        loadingHintDialog.setDismissOnBackPressed(false)
                .setDescription(msg)
                .show(getChildFragmentManager(), "user_cancellation");
    }

    protected void hideLoading() {
        if (loadingHintDialog != null) {
            loadingHintDialog.disMissDialog();
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        if (null == rootView) {
            mDataBinding =
                    DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
            mDataBinding.setLifecycleOwner(this);
            rootView = mDataBinding.getRoot();

        }
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //初始化ViewModel
        initViewModel();

        //binding 对象、model
        bindingBars();

    }

    /**
     * 第一次可见,根据业务进行初始化操作
     */
    protected void onFragmentFirstVisible() {

    }


    private void bindingBars() {
        //添加绑定对象，可以为null
        DataBindingVars dataBindingVars = getBindingVariable();
        if (dataBindingVars != null) {
            SparseArray<Object> bindingParams = dataBindingVars.getBindingParams();

            for (int i = 0, length = bindingParams.size(); i < length; i++) {
                mDataBinding.setVariable(bindingParams.keyAt(i), bindingParams.valueAt(i));
            }
        }
    }

    /**
     * 获取ViewModel
     */
    protected void initViewModel() {
        Class<VM> vmClass = ClassUtil.getViewModel(this);
        if (vmClass == null) {
            return;
        }
        mViewModel = getFragmentScopeViewModel(vmClass);
    }

    protected <T extends ViewModel> T getFragmentScopeViewModel(@NonNull Class<T> modelClass) {
        if (isActivityViewModel()) {
            return new ViewModelProvider(getActivity()).get(modelClass);
        }
        return new ViewModelProvider(this).get(modelClass);
    }

    /**
     * 是否返回Activity生命周期的ViewModel，让ViewModel和所依赖的Activity生命周期等同长度。为了做Fragment之间的数据共享
     *
     * @return T:是，F:只是自身生命周期等同
     */
    protected boolean isActivityViewModel() {
        return false;
    }

    protected FragmentActivity getBaseActivity() {
        return getActivity();
    }


    @Override
    public void onResume() {
        super.onResume();
        if (mIsFirstVisible && !isHidden()) {
            onFragmentFirstVisible();
            mIsFirstVisible = false;
        }

    }


    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        mIsFirstVisible = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @LayoutRes
    public abstract int getLayoutId();

    /**
     * databingding 绑定对象
     */
    public DataBindingVars getBindingVariable() {
        return null;
    }


}
