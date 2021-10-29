package com.donews.common.base;

import android.os.Bundle;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.donews.common.ad.business.monitor.PageMonitor;
import com.donews.base.base.DataBindingVars;
import com.donews.base.fragmentdialog.LoadingHintDialog;
import com.donews.common.base.popwindow.BasePopupWindow;
import com.donews.base.utils.ClassUtil;
import com.donews.base.viewmodel.BaseLiveDataViewModel;

import java.util.ArrayList;

/**
 * 应用模块: activity
 * <p>
 * 类描述: activity抽象基类
 * <p>
 * <p>
 * 作者： created by honeylife<br>
 * 日期：2020-01-27
 */
public abstract class MvvmBaseLiveDataActivity<V extends ViewDataBinding, VM extends BaseLiveDataViewModel> extends AppCompatActivity {

    protected VM mViewModel;

    protected V mDataBinding;


    private ViewGroup rootView;
    private ArrayList<BasePopupWindow> basePopupWindows = new ArrayList<>();
    protected LoadingHintDialog loadingHintDialog;

    public final void addPDPopupWindow(BasePopupWindow basePopupWindow) {
        basePopupWindows.add(basePopupWindow);
    }

    public ViewGroup getRootView() {
        return rootView;
    }


    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        rootView = (FrameLayout) findViewById(android.R.id.content);
    }

    protected void showLoading(){
        if(loadingHintDialog != null){
            hideLoading();
        }
        loadingHintDialog = new LoadingHintDialog();
        loadingHintDialog.setDismissOnBackPressed(false)
                .setDescription("提交中...")
                .show(getSupportFragmentManager(), "user_cancellation");
    }

    protected void showLoading(String msg){
        if(loadingHintDialog != null){
            hideLoading();
        }
        loadingHintDialog = new LoadingHintDialog();
        loadingHintDialog.setDismissOnBackPressed(false)
                .setDescription(msg)
                .show(getSupportFragmentManager(), "user_cancellation");
    }

    protected void hideLoading(){
        if(loadingHintDialog != null){
            loadingHintDialog.disMissDialog();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViewModel();
        performDataBinding();
        initView();
        new PageMonitor().attach(this);
    }

    private void initViewModel() {
        Class<VM> viewModelClass = ClassUtil.getViewModel(this);
        if (viewModelClass == null) {
            return;
        }
        mViewModel = getActivityScopeViewModel(viewModelClass);

    }

    /**
     * layoutId
     *
     * @return int
     */
    protected abstract int getLayoutId();

    /**
     * 初始化视图
     */
    public abstract void initView();

    /**
     * 添加绑定对象，可以为null
     *
     * @return 绑定id，对象集合
     */
    protected DataBindingVars geDataBindingVars() {
        return null;
    }

    ;

    private void performDataBinding() {
        mDataBinding = DataBindingUtil.setContentView(this, getLayoutId());
        mDataBinding.setLifecycleOwner(this);

        //添加绑定对象
        DataBindingVars dataBindingVars = geDataBindingVars();
        if (dataBindingVars != null) {

            SparseArray<Object> bindingParams = dataBindingVars.getBindingParams();

            for (int i = 0, length = bindingParams.size(); i < length; i++) {
                mDataBinding.setVariable(bindingParams.keyAt(i), bindingParams.valueAt(i));
            }
        }
    }

    /**
     * 返回ViewModel
     *
     * @param modelClass ViewModel.class
     * @param <T>        extend ViewModel
     * @return ViewModel
     */
    protected <T extends ViewModel> T getActivityScopeViewModel(@NonNull Class<T> modelClass) {
        return new ViewModelProvider(this).get(modelClass);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    @Override
    protected void onResume() {
        super.onResume();
        for (int i = basePopupWindows.size() - 1; i >= 0; i--) {
            basePopupWindows.get(i).onResume();
        }
    }

    protected boolean checkBaseActivity(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            for (int i = basePopupWindows.size() - 1; i >= 0; i--) {
                BasePopupWindow basePopupWindow = basePopupWindows.get(i);
                if (basePopupWindow.isShown() && basePopupWindow.backPressDismiss()) {
                    basePopupWindow.hide();
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (checkBaseActivity(keyCode, event)) {
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

}
