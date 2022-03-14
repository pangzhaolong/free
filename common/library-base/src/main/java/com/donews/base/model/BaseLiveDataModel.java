package com.donews.base.model;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * @author by SnowDragon
 * Date on 2020/12/23
 * Description:
 */
public class BaseLiveDataModel {
    private CompositeDisposable mCompositeDisposable;

    public BaseLiveDataModel() {
    }

    public void addDisposable(Disposable disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);
    }

    public void unDisposable() {
        if (mCompositeDisposable != null && mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.clear();
        }
    }

}
