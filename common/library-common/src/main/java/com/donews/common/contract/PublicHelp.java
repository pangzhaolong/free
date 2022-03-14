package com.donews.common.contract;

/**
 * <p> </p>
 * 作者： created by honeylife<br>
 * 日期： 2020/12/09 10:47<br>
 * 版本：V1.0<br>
 */
public class PublicHelp {
    private PublicConfigBean publicConfigBean;

    public static PublicHelp getInstance() {
        return Holder.instance;
    }

    private static final class Holder {
        private static PublicHelp instance = new PublicHelp();
    }

    public PublicConfigBean getPublicConfigBean() {
        return publicConfigBean;
    }

    public void setPublicConfigBean(PublicConfigBean publicConfigBean) {
        this.publicConfigBean = publicConfigBean;
    }
}
