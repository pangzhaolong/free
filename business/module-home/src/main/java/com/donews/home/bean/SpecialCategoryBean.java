package com.donews.home.bean;

import com.donews.common.contract.BaseCustomViewModel;

public class SpecialCategoryBean extends BaseCustomViewModel {
    String title;
    String img;
    String c_type;
    String url;
    int in;

    public int getIn() {
        return in;
    }

    public String getC_type() {
        return c_type;
    }

    public String getImg() {
        return img;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public void setC_type(String c_type) {
        this.c_type = c_type;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setIn(int in) {
        this.in = in;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "SpecialCategoryBean{" +
                "title='" + title + '\'' +
                ", img='" + img + '\'' +
                ", c_type='" + c_type + '\'' +
                ", url='" + url + '\'' +
                ", in=" + in +
                '}';
    }
}
