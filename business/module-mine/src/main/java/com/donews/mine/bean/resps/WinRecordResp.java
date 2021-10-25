package com.donews.mine.bean.resps;

import com.donews.common.contract.BaseCustomViewModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author lcl
 * Date on 2021/10/21
 * Description:
 *  个人中奖记录
 */
public class WinRecordResp extends BaseCustomViewModel {

    @SerializedName("list")
    public List<ListDTO> list;
    @SerializedName("total")
    public String total;

    public static class ListDTO extends BaseCustomViewModel{
        @SerializedName("user_id")
        public String userId;
        @SerializedName("user_name")
        public String userName;
        @SerializedName("avatar")
        public String avatar;
        @SerializedName("code")
        public List<String> code;
        @SerializedName("win_bit")
        public List<Integer> winBit;
        @SerializedName("win_type")
        public String winType;
        @SerializedName("goods")
        public GoodsDTO goods;
        @SerializedName("received")
        public Boolean received;
        @SerializedName("open_code")
        public String openCode;
        @SerializedName("period")
        public int period;

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
