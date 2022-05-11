package com.donews.middle.dialog;

import android.app.Activity;

import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.donews.base.fragmentdialog.AbstractFragmentDialog;

/**
 * @author lcl
 * Date on 2022/5/9
 * Description:
 * 使用Binding的基础FragmentDialog
 */
public abstract class BaseBindingFragmentDialog<B extends ViewDataBinding> extends AbstractFragmentDialog<B> {

    private int layoutId = 0;

    public BaseBindingFragmentDialog(int layoutId) {
        this.layoutId = layoutId;
    }


    /**
     * 布局文件id
     *
     * @return 返回布局文件Layout 的id
     */
    @Override
    protected final int getLayoutId() {
        return layoutId;
    }

    /**
     * 使用dataBinding 需要泛型传入，不使用dataBing，返回false
     *
     * @return true:使用dataBinding
     */
    @Override
    protected final boolean isUseDataBinding() {
        return true;
    }

    /**
     * 创建指定的ViewModel
     *
     * @param <M>
     * @param activity 生命周期组件。为了做数据共享
     * @param modelClazz
     */
    public <M extends ViewModel> M createViewModel(FragmentActivity activity, Class<M> modelClazz) {
        return new ViewModelProvider(activity).get(modelClazz);
    }

    /**
     * 创建指定的ViewModel
     *
     * @param <M>
     * @param modelClazz
     */
    public <M extends ViewModel> M createViewModel(Class<M> modelClazz) {
        return new ViewModelProvider(this).get(modelClazz);
    }
}
