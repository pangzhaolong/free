package com.donews.middle.bean.home;

import com.donews.common.contract.BaseCustomViewModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 兑换商品接口的返回结果
 */
public class HomeExchangeResp extends BaseCustomViewModel {
    //1 金币不足  2 活跃度不足  3 商品已兑完  4 兑换成功
    public int status;
    //达到条件还需要的值
    public int diff;
}
