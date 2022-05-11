package com.donews.middle.bean.mine2.resp;

import com.donews.common.contract.BaseCustomViewModel;

import java.util.ArrayList;
import java.util.List;

import kotlinx.parcelize.Parcelize;

/**
 * @author lcl
 * Date on 2022/5/7
 * Description:
 * 积分和活跃度
 */
public class SignListResp extends BaseCustomViewModel {
    /**
     * 签到列表数据
     */
    public List<SignListItemResp> items = new ArrayList<>();

    /**
     * 每天的签到数据
     */
    public static class SignListItemResp extends BaseCustomViewModel{
        /**
         * 天数
         */
        public int day;
        /**
         * 标题
         */
        public String title;
        /**
         * 标题
         */
        public String desc;
        /**
         * 签到状态： 0：不能签到，1：可以签到，2：已经签到
         */
        public int status;
    }
}

