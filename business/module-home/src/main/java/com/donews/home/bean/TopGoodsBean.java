package com.donews.home.bean;

import androidx.annotation.NonNull;

import com.donews.common.contract.BaseCustomViewModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TopGoodsBean extends BaseCustomViewModel {


    @SerializedName("list")
    private List<goodsInfo> list;
    @SerializedName("page_id")
    private String page_id;

    public List<goodsInfo> getList() {
        return list;
    }

    public void setList(List<goodsInfo> list) {
        this.list = list;
    }

    public String getPage_id() {
        return page_id;
    }

    public void setPage_id(String page_id) {
        this.page_id = page_id;
    }

    @NonNull
    @Override
    public String toString() {
        return "TopGoodsBean{" +
                "list=" + list +
                ", pageId='" + page_id + '\'' +
                '}';
    }

    public static class goodsInfo {
        @SerializedName("id")
        private String id;
        @SerializedName("goods_id")
        private String goods_id;
        @SerializedName("item_link")
        private String item_link;
        @SerializedName("title")
        private String title;
        @SerializedName("dtitle")
        private String dtitle;
        @SerializedName("desc")
        private String desc;
        @SerializedName("cid")
        private String cid;
        @SerializedName("subcid")
        private List<String> subcid;
        @SerializedName("tbcid")
        private String tbcid;
        @SerializedName("main_pic")
        private String main_pic;
        @SerializedName("marketing_main_pic")
        private String marketingMainPic;
        @SerializedName("original_price")
        private Integer originalPrice;
        @SerializedName("actual_price")
        private Integer actualPrice;
        @SerializedName("discounts")
        private Integer discounts;
        @SerializedName("coupon_link")
        private String couponLink;
        @SerializedName("coupon_price")
        private Integer couponPrice;
        @SerializedName("month_sales")
        private Integer monthSales;
        @SerializedName("brand_id")
        private String brandId;
        @SerializedName("brand_name")
        private String brandName;
        @SerializedName("shop_type")
        private Integer shopType;
        @SerializedName("haitao")
        private Integer haitao;
        @SerializedName("seller_id")
        private String sellerId;
        @SerializedName("shop_name")
        private String shopName;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getGoods_id() {
            return goods_id;
        }

        public void setGoods_id(String goods_id) {
            this.goods_id = goods_id;
        }

        public String getItem_link() {
            return item_link;
        }

        public void setItem_link(String item_link) {
            this.item_link = item_link;
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

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
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

        public Integer getDiscounts() {
            return discounts;
        }

        public void setDiscounts(Integer discounts) {
            this.discounts = discounts;
        }

        public String getCouponLink() {
            return couponLink;
        }

        public void setCouponLink(String couponLink) {
            this.couponLink = couponLink;
        }

        public Integer getCouponPrice() {
            return couponPrice;
        }

        public void setCouponPrice(Integer couponPrice) {
            this.couponPrice = couponPrice;
        }

        public Integer getMonthSales() {
            return monthSales;
        }

        public void setMonthSales(Integer monthSales) {
            this.monthSales = monthSales;
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

        public Integer getShopType() {
            return shopType;
        }

        public void setShopType(Integer shopType) {
            this.shopType = shopType;
        }

        public Integer getHaitao() {
            return haitao;
        }

        public void setHaitao(Integer haitao) {
            this.haitao = haitao;
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
    }
}
