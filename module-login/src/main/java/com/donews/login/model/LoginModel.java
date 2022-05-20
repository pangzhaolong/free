package com.donews.login.model;


import androidx.lifecycle.MutableLiveData;

import com.donews.base.model.BaseLiveDataModel;
import com.donews.base.model.BaseModel;
import com.donews.common.contract.UserInfoBean;
import com.donews.network.EasyHttp;
import com.donews.network.callback.SimpleCallBack;
import com.donews.network.exception.ApiException;
import com.donews.utilslibrary.utils.AppInfo;

import io.reactivex.disposables.Disposable;

/**
 * <p>  数据处理层 </p>
 * 作者： created by honeylife<br>
 * 日期： 2020/11/12 10:02<br>
 * 版本：V1.0<br>
 */
public class LoginModel extends BaseLiveDataModel {
    private Disposable disposable;

    /**
     * 获取验证码
     *
     * @param mobile 手机号
     */
    public void getUserCode(String mobile) {
        disposable = UserInfoManage.getUserCode(mobile);
    }


    /**
     * 手机号登录
     *
     * @param mobile  手机号
     * @param verCode 验证码
     */
    public MutableLiveData<UserInfoBean> onLogin(String mobile, String verCode) {
        return UserInfoManage.onLoadNetUserInfo(UserInfoManage.getNetDataStr(mobile, verCode));
    }


    /**
     * 微信登录
     *
     * @param code 微信的code
     */
    public MutableLiveData<UserInfoBean> onWXLogin(String code) {
        AppInfo.saveWXLoginCode(code);
        if (code != null && code.length() > 0) {
            //微信。都走绑定。而不再走登录
            return UserInfoManage.onLoadNetUserInfoWxBind(UserInfoManage.getNetDataStr(code), null, "微信登录页");
        }
        return UserInfoManage.onLoadNetUserInfo(UserInfoManage.getNetDataStr(code), null, "微信登录页");
    }

    /**
     * 微信绑定
     *
     * @param code 微信的code
     */
    public void onWXBindUser(String code) {
        UserInfoManage.onLoadBindUserInfo(UserInfoManage.getNetBindStr("", "", code));
    }

    /**
     * 手机号绑定
     *
     * @param mobile
     * @param verCode
     */
    public MutableLiveData<UserInfoBean> onBindPhone(String mobile, String verCode) {
        return UserInfoManage.onLoadBindUserInfo(UserInfoManage.getNetBindStr(mobile, verCode, ""));
    }

    /**
     * 手机号绑定(Dialog方式绑定)
     *
     * @param mobile
     * @param verCode
     */
    public MutableLiveData<UserInfoBean> onBindPhoneDialog(String mobile, String verCode) {
        return UserInfoManage.onLoadBindUserInfoDialog(UserInfoManage.getNetBindStr(mobile, verCode, ""));
    }


}
