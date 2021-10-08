package com.donews.common.contract;

import android.text.TextUtils;

/**
 * <p> </p>
 * 作者： created by honeylife<br>
 * 日期： 2020/11/18 10:47<br>
 * 版本：V1.0<br>
 */
public class LoginHelp {
    private UserInfoBean userInfoBean;

    public static LoginHelp getInstance() {
        return Holder.instance;
    }

    private static final class Holder {
        private static LoginHelp instance = new LoginHelp();
    }

    public UserInfoBean getUserInfoBean() {
        return userInfoBean;
    }

    public void setUserInfoBean(UserInfoBean userInfoBean) {
        this.userInfoBean = userInfoBean;
    }

    public boolean isLogin() {
        return userInfoBean == null || TextUtils.isEmpty(userInfoBean.getId()) || userInfoBean.getId().equals("0");

    }
}
