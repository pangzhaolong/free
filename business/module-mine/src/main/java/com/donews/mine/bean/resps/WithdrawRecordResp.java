package com.donews.mine.bean.resps;

import com.donews.common.contract.BaseCustomViewModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author lcl
 * Date on 2021/10/26
 * Description:
 * 提现记录 = 积分列表
 */
public class WithdrawRecordResp extends BaseCustomViewModel {

    @SerializedName("list")
    public List<RecordListDTO> list;

    public static class RecordListDTO extends BaseCustomViewModel {
        @SerializedName("id")
        public String id;
        @SerializedName("score")
        public Double score;
        @SerializedName("comment")
        public String comment;
        @SerializedName("time")
        public String time;
        @SerializedName("unit")
        public String unit;
    }
}
