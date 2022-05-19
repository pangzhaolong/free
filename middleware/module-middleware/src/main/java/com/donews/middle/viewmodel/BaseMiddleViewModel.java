package com.donews.middle.viewmodel;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.donews.middle.model.MiddleModel;

/**
 * @author lcl
 * Date on 2022/5/17
 * Description:
 */
public class BaseMiddleViewModel extends MiddleViewModel<MiddleModel> {

    /**
     * 全局单利的一个对象，再{@link #getBaseViewModel}的context参数使用Application既可获得当前对象
     */
    private static BaseMiddleViewModel _sBaseMiddleViewModel = new BaseMiddleViewModel();

    /**
     * 获取指定生命周期下的ViewModel
     *
     * @param owner
     * @return
     */
    public static BaseMiddleViewModel getBaseViewModel(ViewModelStoreOwner owner) {
        return new ViewModelProvider(owner).get(BaseMiddleViewModel.class);
    }

    /**
     * 获取指定生命周期下的ViewModel
     *
     * @return
     */
    public static BaseMiddleViewModel getBaseViewModel() {
        return _sBaseMiddleViewModel;
    }

    // 金币数量(个人中心的)
    public MutableLiveData<Integer> mine2JBCount = new MutableLiveData<>(0);
    // 积分数量(个人中心的)
    public MutableLiveData<Integer> mine2JFCount = new MutableLiveData<>(0);

    /**
     * 创建Model
     *
     * @return Model
     */
    @Override
    public MiddleModel createModel() {
        return new MiddleModel();
    }
}
