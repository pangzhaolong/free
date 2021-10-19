package com.donews.home.bean;

import com.donews.common.contract.BaseCustomViewModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SecKilBean extends BaseCustomViewModel {

    @SerializedName("ddq_time")
    private String ddqTime;
    @SerializedName("status")
    private Integer status;
    @SerializedName("goods_list")
    private List<goodsInfo> goodsList;
    @SerializedName("rounds_list")
    private List<roundInfo> roundsList;

    public String getDdqTime() {
        return ddqTime;
    }

    public void setDdqTime(String ddqTime) {
        this.ddqTime = ddqTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<goodsInfo> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<goodsInfo> goodsList) {
        this.goodsList = goodsList;
    }

    public List<roundInfo> getRoundsList() {
        return roundsList;
    }

    public void setRoundsList(List<roundInfo> roundsList) {
        this.roundsList = roundsList;
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
        @SerializedName("cid")
        private String cid;
        @SerializedName("subcid")
        private List<String> subcid;
        @SerializedName("ddq_desc")
        private String ddqDesc;
        @SerializedName("tbcid")
        private String tbcid;
        @SerializedName("main_pic")
        private String mainPic;
        @SerializedName("original_price")
        private float originalPrice;
        @SerializedName("actual_price")
        private float actualPrice;
        @SerializedName("discounts")
        private float discounts;
        @SerializedName("coupon_price")
        private float couponPrice;
        @SerializedName("month_sales")
        private Integer monthSales;
        @SerializedName("two_hours_sales")
        private Integer twoHoursSales;
        @SerializedName("daily_sales")
        private Integer dailySales;
        @SerializedName("brand")
        private Integer brand;
        @SerializedName("brand_id")
        private String brandId;
        @SerializedName("brand_name")
        private String brandName;
        @SerializedName("create_time")
        private String createTime;
        @SerializedName("activity_type")
        private Integer activityType;
        @SerializedName("activity_start_time")
        private String activityStartTime;
        @SerializedName("activity_end_time")
        private String activityEndTime;
        @SerializedName("seller_id")
        private String sellerId;
        @SerializedName("shop_name")
        private String shopName;
        @SerializedName("quan_m_link")
        private Integer quanMLink;
        @SerializedName("hz_quan_over")
        private Integer hzQuanOver;
        @SerializedName("yunfeixian")
        private Integer yunfeixian;
        @SerializedName("sales")
        private Integer sales;
        @SerializedName("coupon_total_num")
        private Integer couponTotalNum;
        @SerializedName("coupon_receive_num")
        private Integer couponReceiveNum;

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

        public String getDdqDesc() {
            return ddqDesc;
        }

        public void setDdqDesc(String ddqDesc) {
            this.ddqDesc = ddqDesc;
        }

        public String getTbcid() {
            return tbcid;
        }

        public void setTbcid(String tbcid) {
            this.tbcid = tbcid;
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

        public float getActualPrice() {
            return actualPrice;
        }

        public void setActualPrice(float actualPrice) {
            this.actualPrice = actualPrice;
        }

        public float getDiscounts() {
            return discounts;
        }

        public void setDiscounts(float discounts) {
            this.discounts = discounts;
        }

        public float getCouponPrice() {
            return couponPrice;
        }

        public void setCouponPrice(float couponPrice) {
            this.couponPrice = couponPrice;
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

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public Integer getActivityType() {
            return activityType;
        }

        public void setActivityType(Integer activityType) {
            this.activityType = activityType;
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

        public String getSellerId() {
            return sellerId;
        }

        public void setSellerId(String sellerId) {
            this.sellerId = sellerId;
        }

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
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

        public Integer getSales() {
            return sales;
        }

        public void setSales(Integer sales) {
            this.sales = sales;
        }

        public Integer getCouponTotalNum() {
            return couponTotalNum;
        }

        public void setCouponTotalNum(Integer couponTotalNum) {
            this.couponTotalNum = couponTotalNum;
        }

        public Integer getCouponReceiveNum() {
            return couponReceiveNum;
        }

        public void setCouponReceiveNum(Integer couponReceiveNum) {
            this.couponReceiveNum = couponReceiveNum;
        }
    }

    public static class roundInfo extends BaseCustomViewModel{
        @SerializedName("ddq_time")
        private String ddqTime;
        @SerializedName("status")
        private Integer status;
        @SerializedName("round")
        private String round;

        public String getDdqTime() {
            return ddqTime;
        }

        public void setDdqTime(String ddqTime) {
            this.ddqTime = ddqTime;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public String getRound() {
            return round;
        }

        public void setRound(String round) {
            this.round = round;
        }
    }
}
