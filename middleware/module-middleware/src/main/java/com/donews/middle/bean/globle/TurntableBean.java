package com.donews.middle.bean.globle;

import android.graphics.Bitmap;

import com.donews.common.contract.BaseCustomViewModel;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class TurntableBean extends BaseCustomViewModel {

    @SerializedName("items")
    private List<ItemsDTO> items;

    public int getRefreshInterval() {
        return refreshInterval;
    }

    public void setRefreshInterval(int refreshInterval) {
        this.refreshInterval = refreshInterval;
    }

    @SerializedName("refreshInterval")
    private int refreshInterval = 30;
    public static class ItemsDTO implements Serializable {
        private Bitmap bitmap;
        @SerializedName("desc")
        private String desc;
        @SerializedName("icon")
        private Object icon;




        @SerializedName("item_icon")
        private Object item_icon;
        @SerializedName("id")
        private Integer id;
        @SerializedName("ratio")
        private Integer ratio;
        @SerializedName("reward")
        private RewardDTO reward;
        @SerializedName("title")
        private String title;
        public Object getItem_icon() {
            return item_icon;
        }

        public void setItem_icon(Object item_icon) {
            this.item_icon = item_icon;
        }
        public Bitmap getBitmap() {
            return bitmap;
        }

        public void setBitmap(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public Object getIcon() {
            return icon;
        }

        public void setIcon(Object icon) {
            this.icon = icon;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getRatio() {
            return ratio;
        }

        public void setRatio(Integer ratio) {
            this.ratio = ratio;
        }

        public RewardDTO getReward() {
            return reward;
        }

        public void setReward(RewardDTO reward) {
            this.reward = reward;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public static class RewardDTO implements Serializable {
            @SerializedName("coin_max")
            private Integer coinMax;
            @SerializedName("coin_min")
            private Integer coinMin;
            @SerializedName("type")
            private Integer type;

            public Integer getCoinMax() {
                return coinMax;
            }

            public void setCoinMax(Integer coinMax) {
                this.coinMax = coinMax;
            }

            public Integer getCoinMin() {
                return coinMin;
            }

            public void setCoinMin(Integer coinMin) {
                this.coinMin = coinMin;
            }

            public Integer getType() {
                return type;
            }

            public void setType(Integer type) {
                this.type = type;
            }


        }
    }

    public List<ItemsDTO> getItems() {
        return items;
    }

    public void setItems(List<ItemsDTO> items) {
        this.items = items;
    }

}
