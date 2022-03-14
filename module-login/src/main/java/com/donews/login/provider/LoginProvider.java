package com.donews.login.provider;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.template.IProvider;
import com.donews.common.contract.UserInfoBean;
import com.donews.common.services.config.ServicesConfig;
import com.donews.login.model.ISUserInfoCallBack;
import com.donews.login.model.UserInfoManage;
import com.donews.utilslibrary.utils.LogUtil;

import org.json.JSONObject;

/**
 * <p> </p>
 * 作者： created by honeylife<br>
 * 日期： 2020/11/17 17:04<br>
 * 版本：V1.0<br>
 */
@Route(path = ServicesConfig.User.LONGING_SERVICE)
public class LoginProvider implements IProvider {

    private UserInfoBean mUserInfoBean;

    @Override
    public void init(Context context) {

    }

    /**
     * 获去用户信息
     */
    public void getUserInfo() {
        /**
         * {
         *   "user_name": "string",
         *   "head_img": "string",
         *   "gender": "GENDER_UNKNOWN",
         *   "birthday": "string",
         *   "third_party_id": "string"
         * }
         */

        String data = "";
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_name", "");
            jsonObject.put("head_img", "");
            jsonObject.put("gender", "");
            jsonObject.put("birthday", "");
            jsonObject.put("third_party_id", "");
            data = jsonObject.toString();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        LogUtil.d(data);
        UserInfoManage.getUserOtherInfo("0", new ISUserInfoCallBack() {
            @Override
            public void setUserInfo(UserInfoBean userInfoBean) {
                mUserInfoBean = userInfoBean;
                LogUtil.d(userInfoBean.toString());
                getUser();
            }
        });
    }

    /**
     * 刷新token
     */
    public void getRefreshToken() {
        UserInfoManage.onRefresh();
    }

    /**
     * 首页登录 login
     */
    public void getLogin() {
        UserInfoManage.onLoadNetUserInfo(UserInfoManage.getNetDataStr("", ""));
    }

    /**
     * 首页 预注册 pre_register
     */
    public void preRegister() {
        UserInfoManage.onPreRegister();
    }

    /**
     * 首页登录 login(微信登录)
     *
     * @param wxCode 微信的code
     */
    public void getLoginWx(String wxCode) {
        if (wxCode == null || "".equals(wxCode)) {
            //没有微信相关登录信息。那么直接设备登录。走原始逻辑
//            getLogin();
            preRegister();
        } else {
            //走微信登录
            UserInfoManage.onLoadNetUserInfo(UserInfoManage.getNetDataStr(wxCode), null, "微信登录页");
        }
    }

    public UserInfoBean getUser() {
        return mUserInfoBean;
    }

    public void weChatBind() {
        UserInfoManage.weChatBind();
    }

}
