package com.donews.login.ui;

import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.donews.common.base.MvvmBaseLiveDataActivity;
import com.donews.common.router.RouterActivityPath;
import com.donews.common.services.ILoginService;
import com.donews.common.services.config.ServicesConfig;
import com.donews.login.R;
import com.donews.login.databinding.LoginMobileActivityBinding;
import com.donews.login.viewmodel.LoginViewModel;

/**
 * <p>手机号登录 s</p>
 * 作者： created by honeylife<br>
 * 日期： 2020/11/11 16:16<br>
 * 版本：V1.0<br>
 */
@Route(path = RouterActivityPath.User.PAGER_MOBILE)
public class MobileActivity extends MvvmBaseLiveDataActivity<LoginMobileActivityBinding, LoginViewModel> {


    @Autowired(name = ServicesConfig.User.LONGING_SERVICE)
    ILoginService loginService;
    private int timer = 60;
    private boolean isSelected = true;
    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1001:
                    mDataBinding.tvVerificationCode.setVisibility(View.INVISIBLE);
                    mDataBinding.btnVerificationCode.setVisibility(View.VISIBLE);
                    timer--;
                    handler.sendEmptyMessageDelayed(1002, 1000);
                    break;
                case 1002:
                    if (timer == 0) {
                        mDataBinding.btnVerificationCode.setText(String.format("%s", "60 s"));
                        mDataBinding.tvVerificationCode.setVisibility(View.VISIBLE);
                        mDataBinding.btnVerificationCode.setVisibility(View.GONE);
                    } else {
                        mDataBinding.btnVerificationCode.setText(String.format("%s", timer));
                        timer--;
                        handler.sendEmptyMessageDelayed(1002, 1000);
                    }
                    break;
            }
        }
    };

    @Override
    public void initView() {
        intList();
    }

    //监听事件
    private void intList() {
        mViewModel.mActivity = this;
        mDataBinding.ivLoginCloseEdit.setOnClickListener(v -> onClearView());
        // 获取验证码
        mDataBinding.tvVerificationCode.setOnClickListener(v -> getUserCode());
        mDataBinding.rlMobileLogin.setOnClickListener(v ->
                mViewModel.onLogin(getEditTextStr(mDataBinding.editMobileCode),
                        getEditTextStr(mDataBinding.editVerificationCode)));

        mDataBinding.editVerificationCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count != 0 && !TextUtils.isEmpty(getEditTextStr(mDataBinding.editMobileCode))) {
                    mDataBinding.rlMobileLogin.setClickable(true);
                    mDataBinding.tvLoginText.setTextColor(Color.parseColor("#FFFFFF"));
                    mDataBinding.rlMobileLogin.setBackgroundResource(R.drawable.mobile_login_selected_bg);
                } else {
                    mDataBinding.rlMobileLogin.setClickable(false);
                    mDataBinding.rlMobileLogin.setBackgroundResource(R.drawable.mobile_login_bg);
                    mDataBinding.tvLoginText.setTextColor(Color.parseColor("#AEAEAE"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void onClearView() {
        mDataBinding.editMobileCode.setText("");
        mDataBinding.editVerificationCode.setText("");
    }


    public String getEditTextStr(EditText editText) {
        return editText.getText().toString().trim();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.login_mobile_activity;
    }


    //获取验证码
    public void getUserCode() {
        boolean isNull = mViewModel.getUserCode(getEditTextStr(mDataBinding.editMobileCode));
        if (isNull) return;
        timer = 60;
        handler.sendEmptyMessage(1001);
    }


    @Override
    protected void onDestroy() {
        if (handler != null) {
            handler.removeMessages(1001);
            handler.removeMessages(1002);
            handler = null;
        }

        super.onDestroy();
    }

}
