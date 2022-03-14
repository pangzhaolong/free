package com.donews.login.ui.fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.donews.base.fragmentdialog.LoadingHintDialog;
import com.donews.common.router.RouterFragmentPath;
import com.donews.login.R;
import com.donews.login.viewmodel.LoginDialogViewModel;

/**
 * @author lcl
 * Date on 2021/10/22
 * Description:
 * 绑定手机号的弹窗(弹窗方式绑定手机号)
 */
@Route(path = RouterFragmentPath.Login.PAGER_BIND_PHONE_DIALOG_FRAGMENT)
public class BindPhoneDialogFragment extends DialogFragment {

    LoginDialogViewModel mViewModel;
//    @Autowired(name = ServicesConfig.User.LONGING_SERVICE)
//    ILoginService loginService;

    int timer = 60;

    private boolean isSelected = true;

    EditText editVerificationCode;
    EditText editMobileCode;
    RelativeLayout rlMobileLogin;
    TextView tvLoginText;
    TextView btnVerificationCode;
    TextView tvVerificationCode;
    ImageView closeIcon;
    LinearLayout closeDialogIcon;

    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1001:
                    tvVerificationCode.setVisibility(View.INVISIBLE);
                    btnVerificationCode.setVisibility(View.VISIBLE);
                    timer--;
                    handler.sendEmptyMessageDelayed(1002, 1000);
                    break;
                case 1002:
                    if (timer == 0) {
                        btnVerificationCode.setText(String.format("%s", "60 s"));
                        btnVerificationCode.setVisibility(View.VISIBLE);
                        tvVerificationCode.setVisibility(View.VISIBLE);
                        btnVerificationCode.setVisibility(View.GONE);
                    } else {
                        btnVerificationCode.setText(String.format("%s", timer));
                        timer--;
                        handler.sendEmptyMessageDelayed(1002, 1000);
                    }
                    break;
                default:
            }
        }
    };

    private LoadingHintDialog loadingHintDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new LoginDialogViewModel();
        mViewModel.setDialogFragment(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.login_bind_phone_dialog_fragment, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editVerificationCode = getView().findViewById(R.id.edit_verification_code);
        editMobileCode = getView().findViewById(R.id.edit_mobile_code);
        rlMobileLogin = getView().findViewById(R.id.rl_mobile_login);
        tvLoginText = getView().findViewById(R.id.tv_next_bind);
        btnVerificationCode = getView().findViewById(R.id.btn_verification_code);
        tvVerificationCode = getView().findViewById(R.id.tv_verification_code);
        closeIcon = getView().findViewById(R.id.iv_login_close_edit);
        closeDialogIcon = getView().findViewById(R.id.iv_login_close);
        intList();
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams windowParams = window.getAttributes();
//        windowParams.dimAmount = 0.0f;//Dialog外边框透明
//        window.setLayout(-1, -2); //高度自适应，宽度全屏
        windowParams.gravity = Gravity.CENTER; //在顶部显示
        windowParams.width = (int) (ScreenUtils.getScreenWidth() * 0.78F);
//        windowParams.windowAnimations = R.style.TopDialogAnimation;
        window.setAttributes(windowParams);

        //设置背景半透明
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //getDialog().setCancelable(false);//这个会屏蔽掉返回键
        getDialog().setCanceledOnTouchOutside(false);
    }

    //监听事件
    private void intList() {
        closeDialogIcon.setOnClickListener(v -> {
            dismiss();
        });
        closeIcon.setOnClickListener(v -> onClearView());
        // 获取验证码
        tvVerificationCode
                .setOnClickListener(v -> getUserCode());
        rlMobileLogin.setOnClickListener(v -> {
            loadingHintDialog = new LoadingHintDialog();
            loadingHintDialog.setDismissOnBackPressed(true)
                    .setDescription("提交中...")
                    .show(getChildFragmentManager(), "BindPhone");

            mViewModel.onBindPhone(
                    getEditTextStr(getView().findViewById(R.id.edit_mobile_code)),
                    getEditTextStr(getView().findViewById(R.id.edit_verification_code)));
        });

        editVerificationCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count != 0 && !TextUtils.isEmpty(getEditTextStr(editMobileCode))) {
                    rlMobileLogin.setClickable(true);
                    tvLoginText.setTextColor(Color.parseColor("#FFFFFF"));
                    rlMobileLogin.setBackgroundResource(R.drawable.mobile_login_selected_bg);
                } else {
                    rlMobileLogin.setClickable(false);
                    rlMobileLogin.setBackgroundResource(R.drawable.mobile_login_bg);
                    tvLoginText.setTextColor(Color.parseColor("#AEAEAE"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void onClearView() {
        editMobileCode.setText("");
        editVerificationCode.setText("");
    }


    public String getEditTextStr(EditText editText) {
        return editText.getText().toString().trim();
    }

    public void hideLoading() {
        if (loadingHintDialog != null) {
            loadingHintDialog.disMissDialog();
        }
    }

    //获取验证码
    public void getUserCode() {
        boolean isNull = mViewModel.getUserCode(getEditTextStr(editMobileCode));
        if (isNull) {
            return;
        }
        timer = 60;
        handler.sendEmptyMessage(1001);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeMessages(1001);
            handler.removeMessages(1002);
            handler = null;
        }
    }
}
