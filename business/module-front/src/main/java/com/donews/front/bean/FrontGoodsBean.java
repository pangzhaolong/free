package com.donews.front.bean;

import com.donews.common.contract.BaseCustomViewModel;

public class FrontGoodsBean extends BaseCustomViewModel {
    private String mainPic;
    private String title;
    private float price;
    private String doWhat;

    public FrontGoodsBean(String mainPic,
                          String title,
                          float price,
                          String doWhat) {
        this.mainPic = mainPic;
        this.title = title;
        this.price = price;
        this.doWhat = doWhat;
    }

    public String getMainPic() {
        return mainPic;
    }

    public void setMainPic(String mainPic) {
        this.mainPic = mainPic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getDoWhat() {
        return doWhat;
    }

    public void setDoWhat(String doWhat) {
        this.doWhat = doWhat;
    }
}
