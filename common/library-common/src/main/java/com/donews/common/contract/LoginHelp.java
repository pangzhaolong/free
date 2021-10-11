package com.donews.common.contract;

import android.text.TextUtils;

import com.donews.base.utils.GsonUtils;
import com.donews.utilslibrary.utils.KeySharePreferences;
import com.donews.utilslibrary.utils.SPUtils;

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
        checkUserInfoBeanSp();
        return userInfoBean;
    }

    public void setUserInfoBean(UserInfoBean userInfoBean) {
        checkUserInfoBeanSp();
        this.userInfoBean = userInfoBean;
    }

    /**
     * 检查是否登录
     * @return
     *  T:未登录
     *  F:已登录
     */
    public boolean isLogin() {
        checkUserInfoBeanSp();
        return userInfoBean == null || TextUtils.isEmpty(userInfoBean.getId()) || userInfoBean.getId().equals("0");

    }

    //检查用户存储对象
    private void checkUserInfoBeanSp() {
        try {
            if (userInfoBean != null) {
                return;
            }
            String spUserInfo = SPUtils.getInformain(KeySharePreferences.USER_INFO, "");
            if (spUserInfo.isEmpty()) {
                return;
            }
            userInfoBean = GsonUtils.fromLocalJson(spUserInfo, UserInfoBean.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
