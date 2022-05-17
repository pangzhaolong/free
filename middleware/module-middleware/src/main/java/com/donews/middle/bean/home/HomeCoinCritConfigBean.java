package com.donews.middle.bean.home;

import com.donews.common.contract.BaseCustomViewModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 看视频赚金币相关的配置
 */
public class HomeCoinCritConfigBean extends BaseCustomViewModel {
    // 是否开启超暴击模式
    @SerializedName("open")
    public boolean open;
    // 开启暴击还需要多少次广告
    @SerializedName("open_times")
    public int open_times;
    // 剩余暴击次数
    @SerializedName("rest_times")
    public int rest_times;
}
