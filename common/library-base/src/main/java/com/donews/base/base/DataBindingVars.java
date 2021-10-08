package com.donews.base.base;

import android.util.SparseArray;

import androidx.annotation.NonNull;

/**
 * @author by SnowDragon
 * Date on 2020/12/23
 * Description:
 */
public class DataBindingVars {


    private SparseArray<Object> bindingParams = new SparseArray();



    public SparseArray<Object> getBindingParams() {
        return bindingParams;
    }

    public DataBindingVars addBindingParam(@NonNull Integer variableId,
                                           @NonNull Object object) {
        if (bindingParams.get(variableId) == null) {
            bindingParams.put(variableId, object);
        }
        return this;
    }
}
