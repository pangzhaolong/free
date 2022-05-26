package com.donews.login.viewmodel;

import android.text.TextUtils;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;

import com.donews.base.base.BaseApplication;
import com.donews.base.utils.ToastUtil;
import com.donews.base.viewmodel.BaseLiveDataViewModel;
import com.donews.common.contract.UserInfoBean;
import com.donews.login.model.LoginModel;
import com.donews.share.WXHolderHelp;
import com.donews.utilslibrary.utils.BaseToast;
import com.donews.utilslibrary.utils.Validator;

/**
 * <p>
 * * 类描述:  model 与 ui 控制层
 * </p>
 * 作者： created by honeylife<br>
 * 日期： 2020/11/12 09:59<br>
 * 版本：V1.0<br>
 */
public class LoginViewModel extends BaseLiveDataViewModel<LoginModel> {

    public FragmentActivity mActivity;

    @Override
    public LoginModel createModel() {
        return new LoginModel();
    }

    /**
     * 获取验证码
     *
     * @return 手机号为空/不合法 true
     */
    public boolean getUserCode(String mobile) {
        if (getOkMobile(mobile)) {
            return true;
        } else {
            mModel.getUserCode(mobile);
            return false;
        }
    }

    /**
     * 检测手机号是否正确
     *
     * @param mobile 手机号
     * @return
     */
    private boolean getOkMobile(String mobile) {
        if (TextUtils.isEmpty(mobile)) {
            BaseToast.makeToast(mActivity).setToastLongText("请输入手机号").showToast();
            return true;
        }
        if (!Validator.isMobile(mobile)) {
            BaseToast.makeToast(mActivity).setToastLongText("请输入正确的手机号").showToast();
            return true;
        }
        return false;
    }

    public void onLogin(String mobile, String verCode) {
        if (getOkMobile(mobile)) return;
        if (TextUtils.isEmpty(verCode)) {
            BaseToast.makeToast(mActivity).setToastLongText("请输入验证码").showToast();
            return;
        }
        mModel.onLogin(mobile, verCode).observe(mActivity, userInfoBean -> mActivity.finish());

    }

    public void onWXLogin(int state, String code) {
        if (state == WXHolderHelp.STATE_LOGIN) { //微信登录
            mModel.onWXLogin(code);
        } else if (state == WXHolderHelp.STATE_BINDING) { // 微信绑定
            ToastUtil.showShort(BaseApplication.getInstance(),"未实现微信绑定逻辑。请实现...");
        }
    }

//    @Override
//    public void onLoadFinish(BaseModel model, BaseCustomViewModel data) {
//        getPageView().getLoginSuccess((UserInfoBean) data);
//    }


//    @Override
//    public void onComplete() {
//        getPageView().getUserCode();
//        BaseToast.makeToast(mActivity).setToastLongText("验证码发送成功").showToast();
//    }

    public void onBindPhone(String mobile, String vCode) {
        if (getOkMobile(mobile)) return;
        if (TextUtils.isEmpty(vCode)) {
            BaseToast.makeToast(mActivity).setToastLongText("请输入验证码").showToast();
            return;
        }
        mModel.onBindPhone(mobile, vCode).observe(mActivity, new Observer<UserInfoBean>() {
            @Override
            public void onChanged(UserInfoBean userInfoBean) {
                mActivity.finish();
            }
        });
    }


}
