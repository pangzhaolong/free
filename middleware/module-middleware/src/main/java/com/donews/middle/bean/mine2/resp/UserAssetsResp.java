package com.donews.middle.bean.mine2.resp;

import com.donews.common.contract.BaseCustomViewModel;

/**
 * @author lcl
 * Date on 2022/5/7
 * Description:
 * 积分和活跃度
 */
public class UserAssetsResp extends BaseCustomViewModel {
    /** 金币 */
    public int coin;
    /** 活跃积分 */
    public int active;
}

