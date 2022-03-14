package com.donews.middle.bean.home;

import com.donews.common.contract.BaseCustomViewModel;
import com.google.gson.annotations.SerializedName;

public class PrivilegeLinkBean extends BaseCustomViewModel {
    @SerializedName("url")
    private String url;
    @SerializedName("tpwd")
    private String tpwd;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTpwd() {
        return tpwd;
    }

    public void setTpwd(String tpwd) {
        this.tpwd = tpwd;
    }
}
