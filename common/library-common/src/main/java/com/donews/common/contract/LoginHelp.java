package com.donews.common.contract;

import android.text.TextUtils;

import com.donews.base.utils.GsonUtils;
import com.donews.utilslibrary.utils.AppStatusUtils;
import com.donews.utilslibrary.utils.KeySharePreferences;
import com.donews.utilslibrary.utils.SPUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
     *
     * @return T:未登录
     * F:已登录
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

    /**
     * 如果没有登录，用第一次打开app时间进行判断,否则使用注册时间进行判断
     * <p>
     * 判断用户注册时间是否大于指定时间
     *
     * @param time 单位小时，如48小时，这判断用户注册时间是否过去了48小时
     * @return true 用户注册时间已经大于 time时长
     */
    public boolean checkUserRegisterTime(int time) {
        long duration = time * 60 * 60 * 1000L;
        long installApp = AppStatusUtils.getAppInstallTime();
        return System.currentTimeMillis() - installApp >= duration;

        /*if (LoginHelp.getInstance().isLogin()) {            //未登陆
            long installApp = AppStatusUtils.getAppInstallTime();
            return System.currentTimeMillis() - installApp >= duration;
        } else {                                            //已登陆
            try {
                SimpleDateFormat mDataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
                String createAt = userInfoBean.getCreatedAt();
                Date registerDate = mDataFormat.parse(createAt);
                if (registerDate != null) {
                    return System.currentTimeMillis() - registerDate.getTime() >= duration;
                }
                return false;
            } catch (ParseException e) {
                e.printStackTrace();
                return false;
            }
        }*/
    }
}
