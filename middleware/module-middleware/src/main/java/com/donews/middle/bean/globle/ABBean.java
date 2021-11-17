package com.donews.middle.bean.globle;

import com.google.gson.annotations.SerializedName;

public class ABBean {
    @SerializedName("openAB")
    private boolean openAB;
    private boolean openVideoToast = true;

    public boolean isOpenVideoToast() {
        return openVideoToast;
    }

    public void setOpenVideoToast(boolean openVideoToast) {
        this.openVideoToast = openVideoToast;
    }

    @Override
    public String toString() {
        return "ABBean{" +
                "ab='" + openAB + '\'' +
                '}';
    }

    public boolean isOpenAB() {
        return openAB;
    }

    public boolean getAb() {
        return openAB;
    }

    public void setAb(boolean openAB) {
        this.openAB = openAB;
    }
}
