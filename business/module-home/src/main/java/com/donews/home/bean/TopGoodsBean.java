package com.donews.home.bean;

import androidx.annotation.NonNull;

import com.donews.common.contract.BaseCustomViewModel;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
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

    public static class goodsInfo extends BaseCustomViewModel {
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
        private String marketing_main_pic;
        @SerializedName("original_price")
        private float original_price;
        @SerializedName("actual_price")
        private float actual_price;
        @SerializedName("discounts")
        private float discounts;
        @SerializedName("coupon_link")
        private String coupon_link;
        @SerializedName("coupon_price")
        private float coupon_price;
        @SerializedName("month_sales")
        private int month_sales;
        @SerializedName("brand_id")
        private String brand_id;
        @SerializedName("brand_name")
        private String brand_name;
        @SerializedName("shop_type")
        private int shop_type;
        @SerializedName("haitao")
        private int haitao;
        @SerializedName("seller_id")
        private String seller_id;
        @SerializedName("shop_name")
        private String shop_name;

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

        public String getMain_pic() {
            return main_pic;
        }

        public void setMain_pic(String main_pic) {
            this.main_pic = main_pic;
        }

        public String getMarketing_main_pic() {
            return marketing_main_pic;
        }

        public void setMarketing_main_pic(String marketing_main_pic) {
            this.marketing_main_pic = marketing_main_pic;
        }

        public float getOriginal_price() {
            return original_price;
        }

        public void setOriginal_price(float original_price) {
            this.original_price = original_price;
        }

        public float getActual_price() {
            return actual_price;
        }

        public void setActual_price(float actual_price) {
            this.actual_price = actual_price;
        }

        public float getDiscounts() {
            return discounts;
        }

        public void setDiscounts(float discounts) {
            this.discounts = discounts;
        }

        public String getCoupon_link() {
            return coupon_link;
        }

        public void setCoupon_link(String coupon_link) {
            this.coupon_link = coupon_link;
        }

        public float getCoupon_price() {
            return coupon_price;
        }

        public void setCoupon_price(float coupon_price) {
            this.coupon_price = coupon_price;
        }

        public int getMonth_sales() {
            return month_sales;
        }

        public void setMonth_sales(int month_sales) {
            this.month_sales = month_sales;
        }

        public String getBrand_id() {
            return brand_id;
        }

        public void setBrand_id(String brand_id) {
            this.brand_id = brand_id;
        }

        public String getBrand_name() {
            return brand_name;
        }

        public void setBrand_name(String brand_name) {
            this.brand_name = brand_name;
        }

        public int getShop_type() {
            return shop_type;
        }

        public void setShop_type(int shop_type) {
            this.shop_type = shop_type;
        }

        public int getHaitao() {
            return haitao;
        }

        public void setHaitao(int haitao) {
            this.haitao = haitao;
        }

        public String getSeller_id() {
            return seller_id;
        }

        public void setSeller_id(String seller_id) {
            this.seller_id = seller_id;
        }

        public String getShop_name() {
            return shop_name;
        }

        public void setShop_name(String shop_name) {
            this.shop_name = shop_name;
        }
    }
}
