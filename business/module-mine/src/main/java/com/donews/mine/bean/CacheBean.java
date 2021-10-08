package com.donews.mine.bean;

import androidx.databinding.Bindable;

import com.donews.common.contract.BaseCustomViewModel;

/**
 * @auther ming
 * @date
 */
public class CacheBean extends BaseCustomViewModel {
    private String cacheValue;
    private String versionName;
    @Bindable
    public String getCacheValue() {
        return cacheValue;
    }

    public void setCacheValue(String cacheValue) {
        this.cacheValue = cacheValue;
        notifyChange();
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }
}
