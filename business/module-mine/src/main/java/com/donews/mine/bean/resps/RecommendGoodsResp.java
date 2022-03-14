package com.donews.mine.bean.resps;

import com.donews.common.contract.BaseCustomViewModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author lcl
 * Date on 2021/10/22
 * Description:
 * 精选推荐的列表接口实体
 */
public class RecommendGoodsResp extends BaseCustomViewModel {

    @SerializedName("list")
    public List<ListDTO> list;

    public static class ListDTO extends BaseCustomViewModel{
        @SerializedName("title")
        public String title;
        @SerializedName("main_pic")
        public String mainPic;
        @SerializedName("original_price")
        public Integer originalPrice;
        @SerializedName("total_people")
        public Integer totalPeople;
        @SerializedName("id")
        public String id;
        @SerializedName("goods_id")
        public String goodsId;
        @SerializedName("winning_rates")
        public float winningRates;
        // 参与状态 0未参与抽奖 1已参与，可继续抽 2抽奖码已满
        @SerializedName("lottery_status")
        public int lotteryStatus;
    }
}
