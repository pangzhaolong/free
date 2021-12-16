package com.donews.middle.bean.front;

import com.donews.common.contract.BaseCustomViewModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LotteryGoodsBean extends BaseCustomViewModel {

    @SerializedName("list")
    private List<GoodsInfo> list;

    public List<GoodsInfo> getList() {
        return list;
    }

    public void setList(List<GoodsInfo> list) {
        this.list = list;
    }

    public static class GoodsInfo extends BaseCustomViewModel {
        //商品来源
        @SerializedName("id")
        private String id;
        //三方平台商品id
        @SerializedName("goods_id")
        private String goodsId;
        //商品标题
        @SerializedName("title")
        private String title;
        //商品主图
        @SerializedName("main_pic")
        private String mainPic;
        //商品原价
        @SerializedName("original_price")
        private float originalPrice;
        //参与状态 0未参与抽奖 1已参与，可继续抽 2抽奖码已满
        @SerializedName("lottery_status")
        private int lotteryStatus;
        //是否置顶
        @SerializedName("top")
        private boolean top;
        //中奖用户数
        @SerializedName("lucky_people")
        private int lucky_people;
        //标签 1 爆款推荐 2 今日热门 3 中奖最多
        @SerializedName("tag")
        private int tag;

        @SerializedName("critical_guid")
        private boolean critical_guid = false;

        @SerializedName("critical_show_hand")
        private boolean critical_show_hand = false;

        public boolean isCritical_show_hand() {
            return critical_show_hand;
        }

        public void setCritical_show_hand(boolean critical_show_hand) {
            this.critical_show_hand = critical_show_hand;
        }

        public boolean isCritical_guid() {
            return critical_guid;
        }

        public void setCritical_guid(boolean critical_guid) {
            this.critical_guid = critical_guid;
        }

        public int getTag() {
            return tag;
        }

        public void setTag(int tag) {
            this.tag = tag;
        }

        public boolean isTop() {
            return top;
        }

        public void setTop(boolean top) {
            this.top = top;
        }

        public int getLucky_people() {
            return lucky_people;
        }

        public void setLucky_people(int lucky_people) {
            this.lucky_people = lucky_people;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(String goodsId) {
            this.goodsId = goodsId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getMainPic() {
            return mainPic;
        }

        public void setMainPic(String mainPic) {
            this.mainPic = mainPic;
        }

        public float getOriginalPrice() {
            return originalPrice;
        }

        public void setOriginalPrice(float originalPrice) {
            this.originalPrice = originalPrice;
        }

        public int getLotteryStatus() {
            return lotteryStatus;
        }

        public void setLotteryStatus(int lotteryStatus) {
            this.lotteryStatus = lotteryStatus;
        }


        @Override
        public String toString() {
            return "GoodsInfo{" +
                    "id='" + id + '\'' +
                    ", goodsId='" + goodsId + '\'' +
                    ", title='" + title + '\'' +
                    ", mainPic='" + mainPic + '\'' +
                    ", originalPrice=" + originalPrice +
                    ", lotteryStatus=" + lotteryStatus +
                    ", top=" + top +
                    ", lucky_people=" + lucky_people +
                    ", tag=" + tag +
                    '}';
        }

    }
}
