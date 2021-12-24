package com.donews.middle.viewmodel;


import androidx.lifecycle.ViewModel;

import com.donews.base.model.BaseLiveDataModel;
import com.donews.base.viewmodel.BaseLiveDataViewModel;

/**
 * @author by SnowDragon
 * Date on 2020/12/23
 * Description:
 */
public abstract class MiddleViewModel<Model extends BaseLiveDataModel> extends BaseLiveDataViewModel {
    protected Model mModel;

    public MiddleViewModel() {
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

