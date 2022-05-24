package com.donews.middle.bean.globle;

import com.donews.common.contract.BaseCustomViewModel;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class OutherSwitchConfigBean extends BaseCustomViewModel {
    /**
     * 配置自动更新的间隔时间
     */
    @SerializedName("refreshInterval")
    private int refreshInterval = 30;
    /**
     * 首页插屏控制(首页各个tab的插屏是否开启的标志)
     * 注意：列表的开关顺序和首页底部的Tab顺序一一对应
     */
    @SerializedName("mainInterstitialSwitch")
    private List<Boolean> mainInterstitialSwitch = new ArrayList<>();

    public List<Boolean> getMainInterstitialSwitch() {
        return mainInterstitialSwitch;
    }

    public void setMainInterstitialSwitch(List<Boolean> mainInterstitialSwitch) {
        this.mainInterstitialSwitch = mainInterstitialSwitch;
    }

    public int getRefreshInterval() {
        return refreshInterval;
    }

    public void setRefreshInterval(int refreshInterval) {
        this.refreshInterval = refreshInterval;
    }

}
