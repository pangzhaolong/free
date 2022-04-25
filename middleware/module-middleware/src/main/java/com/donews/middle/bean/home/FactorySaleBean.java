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
        private float originalPrice;
        @SerializedName("actual_price")
        private float actualPrice;
        @SerializedName("shop_type")
        private float shopType;
        @SerializedName("month_sales")
        private float monthSales;
        @SerializedName("two_hours_sales")
        private float twoHoursSales;
        @SerializedName("daily_sales")
        private float dailySales;
        @SerializedName("desc")
        private String desc;
        @SerializedName("coupon_link")
        private String couponLink;
        @SerializedName("coupon_end_time")
        private String couponEndTime;
        @SerializedName("coupon_start_time")
        private String couponStartTime;
        @SerializedName("coupon_price")
        private float couponPrice;
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
        private float couponTotalNum;
        @SerializedName("activity_start_time")
        private String activityStartTime;
        @SerializedName("activity_end_time")
        private String activityEndTime;
        @SerializedName("shop_level")
        private float shopLevel;
        @SerializedName("brand_id")
        private String brandId;
        @SerializedName("brand_name")
        private String brandName;
        @SerializedName("hot_push")
        private float hotPush;
        @SerializedName("team_name")
        private String teamName;
        @SerializedName("quan_m_link")
        private float quanMLink;
        @SerializedName("hz_quan_over")
        private float hzQuanOver;
        @SerializedName("yunfeixian")
        private float yunfeixian;
        @SerializedName("tchaoshi")
        private float tchaoshi;
        @SerializedName("gold_sellers")
        private float goldSellers;

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

        public float getOriginalPrice() {
            return originalPrice;
        }

        public void setOriginalPrice(float originalPrice) {
            this.originalPrice = originalPrice;
        }

        public float getActualPrice() {
            return actualPrice;
        }

        public void setActualPrice(float actualPrice) {
            this.actualPrice = actualPrice;
        }

        public float getShopType() {
            return shopType;
        }

        public void setShopType(float shopType) {
            this.shopType = shopType;
        }

        public float getMonthSales() {
            return monthSales;
        }

        public void setMonthSales(float monthSales) {
            this.monthSales = monthSales;
        }

        public float getTwoHoursSales() {
            return twoHoursSales;
        }

        public void setTwoHoursSales(float twoHoursSales) {
            this.twoHoursSales = twoHoursSales;
        }

        public float getDailySales() {
            return dailySales;
        }

        public void setDailySales(float dailySales) {
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

        public float getCouponPrice() {
            return couponPrice;
        }

        public void setCouponPrice(float couponPrice) {
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

        public float getCouponTotalNum() {
            return couponTotalNum;
        }

        public void setCouponTotalNum(float couponTotalNum) {
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

        public float getShopLevel() {
            return shopLevel;
        }

        public void setShopLevel(float shopLevel) {
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

        public float getHotPush() {
            return hotPush;
        }

        public void setHotPush(float hotPush) {
            this.hotPush = hotPush;
        }

        public String getTeamName() {
            return teamName;
        }

        public void setTeamName(String teamName) {
            this.teamName = teamName;
        }

        public float getQuanMLink() {
            return quanMLink;
        }

        public void setQuanMLink(float quanMLink) {
            this.quanMLink = quanMLink;
        }

        public float getHzQuanOver() {
            return hzQuanOver;
        }

        public void setHzQuanOver(float hzQuanOver) {
            this.hzQuanOver = hzQuanOver;
        }

        public float getYunfeixian() {
            return yunfeixian;
        }

        public void setYunfeixian(float yunfeixian) {
            this.yunfeixian = yunfeixian;
        }

        public float getTchaoshi() {
            return tchaoshi;
        }

        public void setTchaoshi(float tchaoshi) {
            this.tchaoshi = tchaoshi;
        }

        public float getGoldSellers() {
            return goldSellers;
        }

        public void setGoldSellers(float goldSellers) {
            this.goldSellers = goldSellers;
        }

        public float getFreeshipRemoteDistrict() {
            return freeshipRemoteDistrict;
        }

        public void setFreeshipRemoteDistrict(float freeshipRemoteDistrict) {
            this.freeshipRemoteDistrict = freeshipRemoteDistrict;
        }

        public float getLivePrice() {
            return livePrice;
        }

        public void setLivePrice(float livePrice) {
            this.livePrice = livePrice;
        }

        public float getSalesLive() {
            return salesLive;
        }

        public void setSalesLive(float salesLive) {
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

        public float getActivityType() {
            return activityType;
        }

        public void setActivityType(float activityType) {
            this.activityType = activityType;
        }

        public float getBrand() {
            return brand;
        }

        public void setBrand(float brand) {
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

        @SerializedName("freeship_remote_district")
        private float freeshipRemoteDistrict;
        @SerializedName("live_price")
        private float livePrice;
        @SerializedName("sales_live")
        private float salesLive;
        @SerializedName("user_name")
        private String userName;
        @SerializedName("live_id")
        private String liveId;
        @SerializedName("activity_type")
        private float activityType;
        @SerializedName("brand")
        private float brand;
        @SerializedName("shop_name")
        private String shopName;
        @SerializedName("special_text")
        private List<?> specialText;

    }
}
