package com.donews.home.bean;

import com.donews.common.contract.BaseCustomViewModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchResultTbBean extends BaseCustomViewModel {

    @SerializedName("list")
    private List<goodsInfo> list;
    @SerializedName("page_id")
    private String pageId;

    public List<goodsInfo> getList() {
        return list;
    }

    public void setList(List<goodsInfo> list) {
        this.list = list;
    }

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    public static class goodsInfo extends BaseCustomViewModel{
        @SerializedName("id")
        private String id;
        @SerializedName("goods_id")
        private String goodsId;
        @SerializedName("title")
        private String title;
        @SerializedName("dtitle")
        private String dtitle;
        @SerializedName("original_price")
        private float originalPrice;
        @SerializedName("actual_price")
        private float actualPrice;
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
        @SerializedName("coupon_price")
        private float couponPrice;
        @SerializedName("activity_type")
        private Integer activityType;
        @SerializedName("create_time")
        private String createTime;
        @SerializedName("main_pic")
        private String mainPic;
        @SerializedName("marketing_main_pic")
        private String marketingMainPic;
        @SerializedName("cid")
        private String cid;
        @SerializedName("subcid")
        private List<String> subcid;
        @SerializedName("tbcid")
        private String tbcid;
        @SerializedName("discounts")
        private float discounts;
        @SerializedName("activity_start_time")
        private String activityStartTime;
        @SerializedName("activity_end_time")
        private String activityEndTime;
        @SerializedName("shop_name")
        private String shopName;
        @SerializedName("brand")
        private Integer brand;
        @SerializedName("brand_id")
        private String brandId;
        @SerializedName("brand_name")
        private String brandName;
        @SerializedName("quan_m_link")
        private Integer quanMLink;
        @SerializedName("hz_quan_over")
        private Integer hzQuanOver;
        @SerializedName("yunfeixian")
        private Integer yunfeixian;

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

        public float getCouponPrice() {
            return couponPrice;
        }

        public void setCouponPrice(float couponPrice) {
            this.couponPrice = couponPrice;
        }

        public Integer getActivityType() {
            return activityType;
        }

        public void setActivityType(Integer activityType) {
            this.activityType = activityType;
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

        public String getCid() {
            return cid;
        }

        public void setCid(String cid) {
            this.cid = cid;
        }

        public List<String> getSubcid() {
            return subcid;
        }

        public void setSubcid(List<String> subcid) {
            this.subcid = subcid;
        }

        public String getTbcid() {
            return tbcid;
        }

        public void setTbcid(String tbcid) {
            this.tbcid = tbcid;
        }

        public float getDiscounts() {
            return discounts;
        }

        public void setDiscounts(float discounts) {
            this.discounts = discounts;
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

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public Integer getBrand() {
            return brand;
        }

        public void setBrand(Integer brand) {
            this.brand = brand;
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
    }
}
