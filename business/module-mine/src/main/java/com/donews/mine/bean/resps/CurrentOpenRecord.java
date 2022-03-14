package com.donews.mine.bean.resps;

import com.donews.common.contract.BaseCustomViewModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;


/**
 * @author lcl
 * Date on 2021/10/21
 * Description:
 * 当前开奖期数
 */

public class CurrentOpenRecord extends BaseCustomViewModel {
    @SerializedName("period")
    public int period;
}


