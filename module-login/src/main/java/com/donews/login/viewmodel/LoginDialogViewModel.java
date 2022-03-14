package com.donews.login.viewmodel;

import android.text.TextUtils;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;

import com.donews.base.viewmodel.BaseLiveDataViewModel;
import com.donews.common.contract.UserInfoBean;
import com.donews.login.model.LoginModel;
import com.donews.login.ui.fragments.BindPhoneDialogFragment;
import com.donews.share.WXHolderHelp;
import com.donews.utilslibrary.utils.BaseToast;
import com.donews.utilslibrary.utils.Validator;

/**
 * <p>
 * * 类描述:  model 与 ui 控制层
 * </p>
 * 手机绑定弹窗的相关Model层
 */
public class LoginDialogViewModel extends BaseLiveDataViewModel<LoginModel> {

    public BindPhoneDialogFragment dialogFragment;
    protected LoginModel mModel = createModel();

    @Override
    public LoginModel createModel() {
        return new LoginModel();
    }

    /**
     * 关联Dialog对象
     *
     * @param dialogFragment
     */
    public void setDialogFragment(BindPhoneDialogFragment dialogFragment) {
        this.dialogFragment = dialogFragment;
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
            BaseToast.makeToast(dialogFragment.getContext()).setToastLongText("请输入手机号").showToast();
            return true;
        }
        if (!Validator.isMobile(mobile)) {
            BaseToast.makeToast(dialogFragment.getContext()).setToastLongText("请输入正确的手机号").showToast();
            return true;
        }
        return false;
    }

    public void onBindPhone(String mobile, String vCode) {
        if (getOkMobile(mobile)) return;
        if (TextUtils.isEmpty(vCode)) {
            BaseToast.makeToast(dialogFragment.getContext()).setToastLongText("请输入验证码").showToast();
            return;
        }
        mModel.onBindPhoneDialog(mobile, vCode)
                .observe(dialogFragment, userInfoBean -> {
                    if(userInfoBean == null){
                        //出现了错误
                        dialogFragment.hideLoading();
                    }else{
                        BaseToast.makeToast(dialogFragment.getContext()).setToastLongText("绑定成功").showToast();
                        dialogFragment.dismiss();
                    }
                });
    }


}
