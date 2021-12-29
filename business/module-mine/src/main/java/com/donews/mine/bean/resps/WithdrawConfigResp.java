package com.donews.mine.bean.resps;

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
        /**
         * 是否可提现
         * T:允许，F:不可提现
         */
        @SerializedName("available")
        public Boolean available = false;
        /**
         * 是否为随机金额:
         * T:随机金额项(金额>0:表示任务奖励，<=0:随机金额)
         * F:正常提现项目
         */
        @SerializedName("external")
        public Boolean external = false;
    }
}
