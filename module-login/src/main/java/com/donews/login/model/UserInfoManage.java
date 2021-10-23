package com.donews.login.model;

import androidx.lifecycle.MutableLiveData;

import com.dn.drouter.ARouteHelper;
import com.dn.events.events.LoginUserStatus;
import com.dn.events.events.UserTelBindEvent;
import com.donews.base.utils.GsonUtils;
import com.donews.common.contract.DataBean;
import com.donews.common.contract.LoginHelp;
import com.donews.common.contract.UserInfoBean;
import com.donews.common.router.RouterActivityPath;
import com.donews.common.router.RouterFragmentPath;
import com.donews.common.services.config.ServicesConfig;
import com.donews.login.api.LoginApi;
import com.donews.network.EasyHttp;
import com.donews.network.cache.model.CacheMode;
import com.donews.network.callback.SimpleCallBack;
import com.donews.network.exception.ApiException;
import com.donews.network.model.HttpHeaders;
import com.donews.share.ISWXSuccessCallBack;
import com.donews.share.WXHolderHelp;
import com.donews.utilslibrary.analysis.AnalysisHelp;
import com.donews.utilslibrary.utils.AppInfo;
import com.donews.utilslibrary.utils.DeviceUtils;
import com.donews.utilslibrary.utils.JsonUtils;
import com.donews.utilslibrary.utils.KeySharePreferences;
import com.donews.utilslibrary.utils.LogUtil;
import com.donews.utilslibrary.utils.SPUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.disposables.Disposable;

/**
 * <p>  获取用户的信息</p>
 * 作者： created by honeylife<br>
 * 日期： 2020/11/13 17:46<br>
 * 版本：V1.0<br>
 */
public class UserInfoManage {
    /**
     * 获取用户自己的信息
     *
     * @param name
     */
    public static Disposable updateUserSelfInfo(String name, ISUserInfoCallBack callBack) {
        return EasyHttp.post(LoginApi.INFO)
                .upJson(name)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<UserInfoBean>() {
                    @Override
                    public void onError(ApiException e) {
                        LogUtil.i(e.getCode() + e.getMessage() + "");
                    }

                    @Override
                    public void onSuccess(UserInfoBean userInfoBean) {
                        LogUtil.i(userInfoBean.toString());
                        if (callBack != null) {
                            callBack.setUserInfo(userInfoBean);
                        }
                    }
                });
    }

    /**
     * 获取他人的信息
     *
     * @param userId 用户的id
     */
    public static Disposable getUserOtherInfo(String userId, ISUserInfoCallBack callBack) {
        return EasyHttp.get(String.format(LoginApi.INFO_USER_ID, userId))
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<UserInfoBean>() {
                    @Override
                    public void onError(ApiException e) {
                        LogUtil.i(e.getCode() + e.getMessage() + "");
                    }

                    @Override
                    public void onSuccess(UserInfoBean userInfoBean) {
                        LogUtil.i(userInfoBean.toString());
                        if (callBack != null) {
                            callBack.setUserInfo(userInfoBean);
                        }
//                        if (userId.equals("0")) {
//                            LogUtil.d("Token" + userInfoBean.getToken());
//                            LoginHelp.getInstance().setUserInfoBean(userInfoBean);
//                            EventBus.getDefault().post(new LoginEvent());
//                        }

                    }
                });
    }

    /**
     * 获取验证码
     *
     * @param mobile 手机号
     */
    public static Disposable getUserCode(String mobile) {
        return EasyHttp.get(LoginApi.CODE)
                .params("mobile", mobile)
                .params("packageName", DeviceUtils.getPackage())
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
//                        loadFail(e.getMessage(), isRefresh);
                        LogUtil.i(e.getCode() + e.getMessage() + "");
                    }

                    @Override
                    public void onSuccess(String s) {
//                        parseJson(s);
                        LogUtil.i(s);
                    }

                    @Override
                    public void onCompleted() {
                        super.onCompleted();
//                        onCompleted();
                    }
                });
    }


    /**
     * 解绑用户信息
     *
     * @param mobile 手机号
     * @param wechat 微信信息
     * @param taobao 淘宝信息
     */
    public static Disposable getUserUnBind(boolean mobile, boolean wechat, boolean taobao) {
        String data = "";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobile", mobile);
            jsonObject.put("wechat", wechat);
            jsonObject.put("taobao", taobao);
            data = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return EasyHttp.post(LoginApi.UNBIND)
                .upJson(data)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<UserInfoBean>() {
                    @Override
                    public void onError(ApiException e) {
                        LogUtil.i(e.getCode() + e.getMessage() + "");
                    }

                    @Override
                    public void onSuccess(UserInfoBean userInfoBean) {
                        LogUtil.i(userInfoBean.toString());
                    }
                });
    }


    /**
     * 微信登录使用
     *
     * @param code 微信需要的code
     * @return
     */
    public static String getNetDataStr(String code) {
        return getNetDataStr("", "", code);

    }

    public static String getNetDataStr(String mobile, String verCode) {
        return getNetDataStr(mobile, verCode, "");
    }

    /**
     * 获取请求登录的参数数据
     *
     * @param mobile  手机号
     * @param verCode 验证码
     * @param code    微信的code值
     * @return
     */
    private static String getNetDataStr(String mobile, String verCode, String code) {
        String data = "";
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("mobile", mobile);
            jsonObject.put("code", verCode);
            jsonObject.put("wechat", code);
            JsonUtils.getCommonJson(jsonObject);
            data = jsonObject.toString();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return data;
    }


    /**
     * 绑定手机号的数据
     *
     * @param mobile  手机号
     * @param verCode 验证码
     * @param code    微信的code值
     * @return
     */
    public static String getNetBindStr(String mobile, String verCode, String code) {
        String data = "";
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("mobile", mobile);
            jsonObject.put("code", verCode);
            jsonObject.put("wechat", code);
            jsonObject.put("openId", "");
            jsonObject.put("mergeInfo", true);
            data = jsonObject.toString();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return data;
    }


    public static void weChatBind() {
        WXHolderHelp.onBind(WXHolderHelp.STATE_BINDING, new ISWXSuccessCallBack() {
            @Override
            public void onSuccess(int state, String code) {
                onLoadBindUserInfo(getNetBindStr("", "", code));
            }

            @Override
            public void onFailed(String msg) {

            }
        });
    }


    /**
     * 用户绑定接口
     *
     * @param data 请求的参数
     */
    public static MutableLiveData<UserInfoBean> onLoadBindUserInfo(String data) {
        MutableLiveData<UserInfoBean> mutableLiveData = new MutableLiveData<UserInfoBean>();
        EasyHttp.post(LoginApi.BIND)
                .upJson(data)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<UserInfoBean>() {
                    @Override
                    public void onError(ApiException e) {
                        LogUtil.i(e.getCode() + e.getMessage() + "");

                    }

                    @Override
                    public void onSuccess(UserInfoBean userInfoBean) {
                        LogUtil.i(userInfoBean.toString());
                        setHttpToken(userInfoBean);
                        mutableLiveData.postValue(userInfoBean);
                        ARouteHelper.build(ServicesConfig.User.LOGIN_SUCCESS).invoke();
                        ARouteHelper.invoke(RouterActivityPath.ClassPath.WEB_VIEW_OBJ_ACTIVITY_JAVASCRIPT
                                , "onReloadUrl");

                    }
                });

        return mutableLiveData;
    }

    /**
     * 用户绑定手机接口,Dialog方式绑定
     *
     * @param data 请求的参数
     * @return 如果通知为空表示错误
     */
    public static MutableLiveData<UserInfoBean> onLoadBindUserInfoDialog(String data) {
        MutableLiveData<UserInfoBean> mutableLiveData = new MutableLiveData<>();
        EasyHttp.post(LoginApi.BIND)
                .upJson(data)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<UserInfoBean>() {
                    @Override
                    public void onError(ApiException e) {
                        LogUtil.i(e.getCode() + e.getMessage() + "");
                        mutableLiveData.postValue(null);
                    }

                    @Override
                    public void onSuccess(UserInfoBean userInfoBean) {
                        EventBus.getDefault().post(new UserTelBindEvent());
                        LogUtil.i(userInfoBean.toString());
                        setHttpToken(userInfoBean);
                        mutableLiveData.postValue(userInfoBean);
                        ARouteHelper.build(ServicesConfig.User.LOGIN_SUCCESS).invoke();
                        ARouteHelper.invoke(RouterActivityPath.ClassPath.WEB_VIEW_OBJ_ACTIVITY_JAVASCRIPT
                                , "onReloadUrl");

                    }
                });

        return mutableLiveData;
    }


    /**
     * 登录的接口
     *
     * @param data 请求的参数
     */
    public static MutableLiveData<UserInfoBean> onLoadNetUserInfo(String data) {
        MutableLiveData<UserInfoBean> mutableLiveData = new MutableLiveData<UserInfoBean>();
        EasyHttp.post(LoginApi.LOGIN)
                .upJson(data)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<UserInfoBean>() {
                    @Override
                    public void onError(ApiException e) {
                        EventBus.getDefault().post(new LoginUserStatus(-1));
                        LogUtil.i(e.getCode() + e.getMessage() + "");
                    }

                    @Override
                    public void onSuccess(UserInfoBean userInfoBean) {
                        if (userInfoBean == null) {
                            EventBus.getDefault().post(new LoginUserStatus(0));
                            return;
                        }
                        setHttpToken(userInfoBean);
                        LogUtil.i(userInfoBean.toString());
                        mutableLiveData.postValue(userInfoBean);
                        refreshUserTag(userInfoBean);
                        EventBus.getDefault().post(new LoginUserStatus(1));
                    }
                });

        return mutableLiveData;
    }

    /**
     * 获取用户TAG
     *
     * @param userInfoBean Userbean
     */
    private static void refreshUserTag(UserInfoBean userInfoBean) {
        EasyHttp.get(LoginApi.USER_TAG)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<DataBean>() {
                    @Override
                    public void onError(ApiException e) {

                    }

                    @Override
                    public void onSuccess(DataBean dataBean) {
                        SPUtils.setInformain(KeySharePreferences.USER_TAG, dataBean.data);
                        ARouteHelper.build(RouterFragmentPath.MethodPath.AD_LOAD_MANAGER_REFRESH_AD_CONFIG)
                                .invoke();

                    }
                });

    }

    private static String getUserTagParams(UserInfoBean userInfoBean) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", userInfoBean.getToken());
            jsonObject.put("uid", userInfoBean.getId());
            jsonObject.put("device_id", DeviceUtils.getDeviceId());
            jsonObject.put("suuid", DeviceUtils.getMyUUID());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    private static void setHttpToken(UserInfoBean userInfoBean) {
        LogUtil.d("Token" + userInfoBean.getToken());
        LoginHelp.getInstance().setUserInfoBean(userInfoBean);
        SPUtils.setInformain(KeySharePreferences.USER_INFO, GsonUtils.toJson(userInfoBean));
        SPUtils.setInformain(KeySharePreferences.TOKEN, userInfoBean.getToken());
        SPUtils.setInformain(KeySharePreferences.USER_ID, userInfoBean.getId());
        AnalysisHelp.registerUserId();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.put(HttpHeaders.HEAD_AUTHORIZATION, AppInfo.getToken(userInfoBean.getToken()));
        EasyHttp.getInstance().addCommonHeaders(httpHeaders);
    }

    public static void clearHttpToken() {
        LoginHelp.getInstance().setUserInfoBean(new UserInfoBean());
        SPUtils.remove(KeySharePreferences.TOKEN);
        EasyHttp.getInstance().addCommonHeaders(null);
    }

    /**
     * 刷新token的数据
     **/
    public static Disposable onRefresh() {
        JSONObject jsonObject = new JSONObject();
        JsonUtils.getCommonJson(jsonObject);
        String data = jsonObject.toString();
        return EasyHttp.post(LoginApi.REFRESH)
                .upJson(data)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<UserInfoBean>() {
                    @Override
                    public void onError(ApiException e) {
                        EventBus.getDefault().post(new LoginUserStatus(-2));
                        LogUtil.i(e.getCode() + e.getMessage() + "");
                    }

                    @Override
                    public void onSuccess(UserInfoBean userInfoBean) {

                        LogUtil.i(userInfoBean.toString());
                        setHttpToken(userInfoBean);

                        refreshUserTag(userInfoBean);
                        EventBus.getDefault().post(new LoginUserStatus(1));
                    }
                });
    }


}
