package com.donews.middle.bean.home;

import com.donews.common.contract.BaseCustomViewModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TopIconsBean extends BaseCustomViewModel {

    @SerializedName("flag")
    private Boolean flag;
    @SerializedName("items")
    private List<Icon> items;

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public List<Icon> getItems() {
        return items;
    }

    public void setItems(List<Icon> items) {
        this.items = items;
    }

    public static class Icon extends BaseCustomViewModel {
        @SerializedName("icon")
        private String icon;
        @SerializedName("name")
        private String name;
        @SerializedName("url")
        private String url;
        @SerializedName("inner")
        private Integer inner;
        @SerializedName("type")
        private String type;

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public Integer getInner() {
            return inner;
        }

        public void setInner(Integer inner) {
            this.inner = inner;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
