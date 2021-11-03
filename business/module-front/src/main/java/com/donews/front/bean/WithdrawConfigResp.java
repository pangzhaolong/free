package com.donews.front.bean;

import com.donews.common.contract.BaseCustomViewModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author lcl
 * Date on 2021/10/26
 * Description:
 * 抽奖中心的配置
 */
public class WithdrawConfigResp extends BaseCustomViewModel {

    @SerializedName("list")
    public List<WithdrawListDTO> list;

    public static class WithdrawListDTO extends BaseCustomViewModel {
        @SerializedName("id")
        public Integer id;
        @SerializedName("money")
        public Double money;
        @SerializedName("tips")
        public String tips;
        @SerializedName("available")
        public Boolean available;
    }
}
