package com.donews.notify.launcher.configs.baens;

import com.donews.common.contract.BaseCustomViewModel;
import com.google.gson.annotations.SerializedName;


/**
 * @author lcl
 * Date on 2021/10/21
 * Description:
 * 获取当前服务器时间
 */

public class CurrentServiceTime extends BaseCustomViewModel {
    @SerializedName("now")
    public String now;
}


