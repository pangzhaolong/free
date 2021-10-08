package com.donews.base.viewmodel;

import androidx.lifecycle.ViewModel;

import com.donews.base.model.BaseLiveDataModel;

/**
 * @author by SnowDragon
 * Date on 2020/12/23
 * Description:
 */
public abstract class BaseLiveDataViewModel<Model extends BaseLiveDataModel> extends ViewModel {
    protected Model mModel;

    public BaseLiveDataViewModel() {
        this.mModel = createModel();
    }

    /**
     * 创建Model
     *
     * @return Model
     */
    public abstract Model createModel();


    /**
     * 释放支援
     */
    @Override
    protected void onCleared() {
        super.onCleared();
        if (mModel != null) {
            mModel.unDisposable();
        }
    }
}
