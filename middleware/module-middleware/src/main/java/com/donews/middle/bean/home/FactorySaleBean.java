package com.donews.middle.bean.home;

import com.donews.common.contract.BaseCustomViewModel;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class FactorySaleBean extends BaseCustomViewModel {
    public List<ListDTO> getList() {
        return list;
    }

    public void setList(List<ListDTO> list) {
        this.list = list;
    }

    @SerializedName("list")
    private List<ListDTO> list;

    public static class ListDTO implements Serializable {
        @SerializedName("id")
        private String id;
        @SerializedName("goods_id")
        private String goodsId;
        @SerializedName("item_link")
        private String itemLink;
        @SerializedName("title")
        private String title;
        @SerializedName("dtitle")
        private String dtitle;
        @SerializedName("original_price")
        private Integer originalPrice;
        @SerializedName("actual_price")
        private Integer actualPrice;
        @SerializedName("shop_type")
        private Integer shopType;
        @SerializedName("month_sales")
        private Integer monthSales;
        @SerializedName("two_hours_sales")
        private Integer twoHoursSales;
        @SerializedName("daily_sales")
        private Integer dailySales;
        @SerializedName("desc")
        private String desc;
        @SerializedName("coupon_link")
        private String couponLink;
        @SerializedName("coupon_end_time")
        private String couponEndTime;
        @SerializedName("coupon_start_time")
        private String couponStartTime;
        @SerializedName("coupon_price")
        private Integer couponPrice;
        @SerializedName("create_time")
        private String createTime;
        @SerializedName("main_pic")
        private String mainPic;
        @SerializedName("marketing_main_pic")
        private String marketingMainPic;
        @SerializedName("video")
        private String video;
        @SerializedName("seller_id")
        private String sellerId;
        @SerializedName("cid")
        private String cid;
        @SerializedName("tbcid")
        private String tbcid;
        @SerializedName("discounts")
        private Double discounts;
        @SerializedName("coupon_total_num")
        private Integer couponTotalNum;
        @SerializedName("activity_start_time")
        private String activityStartTime;
        @SerializedName("activity_end_time")
        private String activityEndTime;
        @SerializedName("shop_level")
        private Integer shopLevel;
        @SerializedName("brand_id")
        private String brandId;
        @SerializedName("brand_name")
        private String brandName;
        @SerializedName("hot_push")
        private Integer hotPush;
        @SerializedName("team_name")
        private String teamName;
        @SerializedName("quan_m_link")
        private Integer quanMLink;
        @SerializedName("hz_quan_over")
        private Integer hzQuanOver;
        @SerializedName("yunfeixian")
        private Integer yunfeixian;
        @SerializedName("tchaoshi")
        private Integer tchaoshi;
        @SerializedName("gold_sellers")
        private Integer goldSellers;
        @SerializedName("freeship_remote_district")
        private Integer freeshipRemoteDistrict;
        @SerializedName("live_price")
        private Integer livePrice;
        @SerializedName("sales_live")
        private Integer salesLive;
        @SerializedName("user_name")
        private String userName;
        @SerializedName("live_id")
        private String liveId;
        @SerializedName("activity_type")
        private Integer activityType;
        @SerializedName("brand")
        private Integer brand;
        @SerializedName("shop_name")
        private String shopName;
        @SerializedName("special_text")
        private List<?> specialText;

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

        public String getItemLink() {
            return itemLink;
        }

        public void setItemLink(String itemLink) {
            this.itemLink = itemLink;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDtitle() {
            return dtitle;
        }

        public void setDtitle(String dtitle) {
            this.dtitle = dtitle;
        }

        public Integer getOriginalPrice() {
            return originalPrice;
        }

        public void setOriginalPrice(Integer originalPrice) {
            this.originalPrice = originalPrice;
        }

        public Integer getActualPrice() {
            return actualPrice;
        }

        public void setActualPrice(Integer actualPrice) {
            this.actualPrice = actualPrice;
        }

        public Integer getShopType() {
            return shopType;
        }

        public void setShopType(Integer shopType) {
            this.shopType = shopType;
        }

        public Integer getMonthSales() {
            return monthSales;
        }

        public void setMonthSales(Integer monthSales) {
            this.monthSales = monthSales;
        }

        public Integer getTwoHoursSales() {
            return twoHoursSales;
        }

        public void setTwoHoursSales(Integer twoHoursSales) {
            this.twoHoursSales = twoHoursSales;
        }

        public Integer getDailySales() {
            return dailySales;
        }

        public void setDailySales(Integer dailySales) {
            this.dailySales = dailySales;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getCouponLink() {
            return couponLink;
        }

        public void setCouponLink(String couponLink) {
            this.couponLink = couponLink;
        }

        public String getCouponEndTime() {
            return couponEndTime;
        }

        public void setCouponEndTime(String couponEndTime) {
            this.couponEndTime = couponEndTime;
        }

        public String getCouponStartTime() {
            return couponStartTime;
        }

        public void setCouponStartTime(String couponStartTime) {
            this.couponStartTime = couponStartTime;
        }

        public Integer getCouponPrice() {
            return couponPrice;
        }

        public void setCouponPrice(Integer couponPrice) {
            this.couponPrice = couponPrice;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getMainPic() {
            return mainPic;
        }

        public void setMainPic(String mainPic) {
            this.mainPic = mainPic;
        }

        public String getMarketingMainPic() {
            return marketingMainPic;
        }

        public void setMarketingMainPic(String marketingMainPic) {
            this.marketingMainPic = marketingMainPic;
        }

        public String getVideo() {
            return video;
        }

        public void setVideo(String video) {
            this.video = video;
        }

        public String getSellerId() {
            return sellerId;
        }

        public void setSellerId(String sellerId) {
            this.sellerId = sellerId;
        }

        public String getCid() {
            return cid;
        }

        public void setCid(String cid) {
            this.cid = cid;
        }

        public String getTbcid() {
            return tbcid;
        }

        public void setTbcid(String tbcid) {
            this.tbcid = tbcid;
        }

        public Double getDiscounts() {
            return discounts;
        }

        public void setDiscounts(Double discounts) {
            this.discounts = discounts;
        }

        public Integer getCouponTotalNum() {
            return couponTotalNum;
        }

        public void setCouponTotalNum(Integer couponTotalNum) {
            this.couponTotalNum = couponTotalNum;
        }

        public String getActivityStartTime() {
            return activityStartTime;
        }

        public void setActivityStartTime(String activityStartTime) {
            this.activityStartTime = activityStartTime;
        }

        public String getActivityEndTime() {
            return activityEndTime;
        }

        public void setActivityEndTime(String activityEndTime) {
            this.activityEndTime = activityEndTime;
        }

        public Integer getShopLevel() {
            return shopLevel;
        }

        public void setShopLevel(Integer shopLevel) {
            this.shopLevel = shopLevel;
        }

        public String getBrandId() {
            return brandId;
        }

        public void setBrandId(String brandId) {
            this.brandId = brandId;
        }

        public String getBrandName() {
            return brandName;
        }

        public void setBrandName(String brandName) {
            this.brandName = brandName;
        }

        public Integer getHotPush() {
            return hotPush;
        }

        public void setHotPush(Integer hotPush) {
            this.hotPush = hotPush;
        }

        public String getTeamName() {
            return teamName;
        }

        public void setTeamName(String teamName) {
            this.teamName = teamName;
        }

        public Integer getQuanMLink() {
            return quanMLink;
        }

        public void setQuanMLink(Integer quanMLink) {
            this.quanMLink = quanMLink;
        }

        public Integer getHzQuanOver() {
            return hzQuanOver;
        }

        public void setHzQuanOver(Integer hzQuanOver) {
            this.hzQuanOver = hzQuanOver;
        }

        public Integer getYunfeixian() {
            return yunfeixian;
        }

        public void setYunfeixian(Integer yunfeixian) {
            this.yunfeixian = yunfeixian;
        }

        public Integer getTchaoshi() {
            return tchaoshi;
        }

        public void setTchaoshi(Integer tchaoshi) {
            this.tchaoshi = tchaoshi;
        }

        public Integer getGoldSellers() {
            return goldSellers;
        }

        public void setGoldSellers(Integer goldSellers) {
            this.goldSellers = goldSellers;
        }

        public Integer getFreeshipRemoteDistrict() {
            return freeshipRemoteDistrict;
        }

        public void setFreeshipRemoteDistrict(Integer freeshipRemoteDistrict) {
            this.freeshipRemoteDistrict = freeshipRemoteDistrict;
        }

        public Integer getLivePrice() {
            return livePrice;
        }

        public void setLivePrice(Integer livePrice) {
            this.livePrice = livePrice;
        }

        public Integer getSalesLive() {
            return salesLive;
        }

        public void setSalesLive(Integer salesLive) {
            this.salesLive = salesLive;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getLiveId() {
            return liveId;
        }

        public void setLiveId(String liveId) {
            this.liveId = liveId;
        }

        public Integer getActivityType() {
            return activityType;
        }

        public void setActivityType(Integer activityType) {
            this.activityType = activityType;
        }

        public Integer getBrand() {
            return brand;
        }

        public void setBrand(Integer brand) {
            this.brand = brand;
        }

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public List<?> getSpecialText() {
            return specialText;
        }

        public void setSpecialText(List<?> specialText) {
            this.specialText = specialText;
        }
    }
}
