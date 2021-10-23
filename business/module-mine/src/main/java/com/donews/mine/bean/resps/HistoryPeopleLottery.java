package com.donews.mine.bean.resps;

import com.donews.common.contract.BaseCustomViewModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;


/**
 * @author lcl
 * Date on 2021/10/21
 * Description:
 * 个人参与历史
 */

public class HistoryPeopleLottery extends BaseCustomViewModel {
    @SerializedName("list")
    public List<Period> list;
    @SerializedName("total")
    public String total;

    public static class Period extends BaseCustomViewModel {
        @SerializedName("period")
        public int period;
        @SerializedName("opend")
        public boolean opend;
    }
}


