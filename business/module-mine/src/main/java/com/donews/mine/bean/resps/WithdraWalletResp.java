package com.donews.mine.bean.resps;

import com.google.gson.annotations.SerializedName;

/**
 * @author lcl
 * Date on 2021/10/26
 * Description:
 *  钱包详情。总额等
 */
public class WithdraWalletResp {
    @SerializedName("id")
    public String id;
    @SerializedName("total")
    public Double total;
}
