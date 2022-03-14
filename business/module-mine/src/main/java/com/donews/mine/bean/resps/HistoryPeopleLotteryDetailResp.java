package com.donews.mine.bean.resps;

import com.donews.common.contract.BaseCustomViewModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author lcl
 * Date on 2021/10/21
 * Description:
 * 个人中心的。开奖详情接口实体
 */
public class HistoryPeopleLotteryDetailResp extends BaseCustomViewModel {

    @SerializedName("period")
    public Integer period;
    @SerializedName("code")
    public String code;
    @SerializedName("speeds")
    public List<SpeedsDTO> speeds;
    /** 中奖名单 */
    @SerializedName("winer")
    public List<WinerDTO> winer;
    /** 我得参与记录 */
    @SerializedName("record")
    public List<WinerDTO> record;
    /** 我的中奖 */
    @SerializedName("my_win")
    public List<WinerDTO> myWin;

    public static class SpeedsDTO extends BaseCustomViewModel{
        @SerializedName("city")
        public String city;
        @SerializedName("speed")
        public String speed;
    }

    public static class WinerDTO extends BaseCustomViewModel{
        @SerializedName("user_id")
        public String userId;
        @SerializedName("user_name")
        public String userName;
        @SerializedName("avatar")
        public String avatar;
        @SerializedName("code")
        public List<String> code;
        @SerializedName("win_type")
        public String winType;
        @SerializedName("goods")
        public GoodsDTO goods;
        @SerializedName("received")
        public Boolean received;
        @SerializedName("open_code")
        public String openCode;

        public static class GoodsDTO extends BaseCustomViewModel{
            @SerializedName("id")
            public String id;
            @SerializedName("title")
            public String title;
            @SerializedName("image")
            public String image;
            @SerializedName("price")
            public Integer price;
        }
    }

}
