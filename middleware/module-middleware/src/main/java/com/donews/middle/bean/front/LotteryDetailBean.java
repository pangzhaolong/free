package com.donews.middle.bean.front;

import com.donews.common.contract.BaseCustomViewModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LotteryDetailBean extends BaseCustomViewModel {

    @SerializedName("period")
    private Integer period;
    @SerializedName("code")
    private String code;
    @SerializedName("speeds")
    private List<SpeedsDTO> speeds;
    @SerializedName("winer")
    private List<WinerDTO> winer;

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<SpeedsDTO> getSpeeds() {
        return speeds;
    }

    public void setSpeeds(List<SpeedsDTO> speeds) {
        this.speeds = speeds;
    }

    public List<WinerDTO> getWiner() {
        return winer;
    }

    public void setWiner(List<WinerDTO> winer) {
        this.winer = winer;
    }

    public static class SpeedsDTO extends BaseCustomViewModel {
        @SerializedName("city")
        private String city;
        @SerializedName("speed")
        private String speed;

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getSpeed() {
            return speed;
        }

        public void setSpeed(String speed) {
            this.speed = speed;
        }
    }

    public static class WinerDTO extends BaseCustomViewModel {
        @SerializedName("user_id")
        private String userId;
        @SerializedName("user_name")
        private String userName;
        @SerializedName("avatar")
        private String avatar;
        @SerializedName("code")
        private List<String> code;
        @SerializedName("win_type")
        private String winType;
        @SerializedName("goods")
        private GoodsDTO goods;
        @SerializedName("received")
        private Boolean received;
        @SerializedName("open_code")
        private String openCode;
        @SerializedName("period")
        private Integer period;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public List<String> getCode() {
            return code;
        }

        public void setCode(List<String> code) {
            this.code = code;
        }

        public String getWinType() {
            return winType;
        }

        public void setWinType(String winType) {
            this.winType = winType;
        }

        public GoodsDTO getGoods() {
            return goods;
        }

        public void setGoods(GoodsDTO goods) {
            this.goods = goods;
        }

        public Boolean getReceived() {
            return received;
        }

        public void setReceived(Boolean received) {
            this.received = received;
        }

        public String getOpenCode() {
            return openCode;
        }

        public void setOpenCode(String openCode) {
            this.openCode = openCode;
        }

        public Integer getPeriod() {
            return period;
        }

        public void setPeriod(Integer period) {
            this.period = period;
        }

        public static class GoodsDTO extends BaseCustomViewModel {
            @SerializedName("id")
            private String id;
            @SerializedName("title")
            private String title;
            @SerializedName("image")
            private String image;
            @SerializedName("price")
            private Integer price;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public Integer getPrice() {
                return price;
            }

            public void setPrice(Integer price) {
                this.price = price;
            }
        }
    }
}
