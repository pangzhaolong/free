package com.donews.mine.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.dn.events.events.LoginLodingStartStatus;
import com.dn.events.events.LoginUserStatus;
import com.dn.events.events.UserUnRegisteredEvent;
import com.donews.base.fragmentdialog.LoadingHintDialog;
import com.donews.base.utils.ToastUtil;
import com.donews.common.router.RouterActivityPath;
import com.donews.mine.R;
import com.donews.mine.common.CommonParams;
import com.donews.utilslibrary.analysis.AnalysisHelp;
import com.donews.utilslibrary.dot.Dot;
import com.donews.utilslibrary.utils.AppInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * @author lcl
 * Date on 2021/10/22
 * Description:
 * 用户注销原因的弹窗
 */
@Route(path = RouterActivityPath.Mine.DIALOG_USER_CANCELLATION_WHY_DIALOG_FRAGMENT)
public class UserCancellationWhyDialogFragment extends DialogFragment {

    /**
     * 关闭的监听
     */
    public interface OnCloseListener {
        /**
         * 关闭监听
         *
         * @param isUnRegSuccess 是否注销成功,T:成功注销，F:没有注销完成
         */
        void close(boolean isUnRegSuccess);
    }

    private LoadingHintDialog loadingHintDialog;

    private RelativeLayout selectContent;
    private LinearLayout selectResult;

    private RadioGroup radGroup;
    private RadioButton rada;
    private RadioButton radb;
    private RadioButton radc;
    private LinearLayout radaLL;
    private LinearLayout radbLL;
    private LinearLayout radcLL;
    private TextView submit;
    private View close;

    //是否显示为结果UI
    private boolean isShowResultUI = false;
    //显示为结果UI时候的计数数字
    private int resultUICount = 5;
    private Handler handler = new Handler();

    //关闭的监听
    private OnCloseListener closeListener;

    /**
     * 设置对结果的监听。就是是否完成注销的监听
     *
     * @param listener
     */
    public void setCloseListener(OnCloseListener listener) {
        this.closeListener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.mine_user_cancellation_why_dialog_fragment, null);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        return dialog;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        selectContent = view.findViewById(R.id.mine_content_select);
        selectResult = view.findViewById(R.id.mine_content_result);

        close = view.findViewById(R.id.iv_close);
        radGroup = view.findViewById(R.id.rg_rad);
        rada = view.findViewById(R.id.ll_select_a_but);
        radb = view.findViewById(R.id.ll_select_b_but);
        radc = view.findViewById(R.id.ll_select_c_but);
        radaLL = view.findViewById(R.id.ll_select_a);
        radbLL = view.findViewById(R.id.ll_select_b);
        radcLL = view.findViewById(R.id.ll_select_c);
        submit = view.findViewById(R.id.tv_submit);
        close.setOnClickListener(v -> {
            dismiss();
        });
        submit.setOnClickListener(v -> {
            if (isShowResultUI) {
                handler.removeCallbacksAndMessages(getActivity());
                handler.removeCallbacksAndMessages(this);
                dismiss();
            } else {
                //显示为结果视图
                if (!NetworkUtils.isAvailableByPing()) {
                    ToastUtil.show(getActivity(), "请检查网络连接");
                    return;
                }
                loadingHintDialog = new LoadingHintDialog();
                loadingHintDialog.setDismissOnBackPressed(false)
                        .setDescription("提交中...")
                        .show(getChildFragmentManager(), "user_cancellation");
                isSendUnReg = true;
                AppInfo.exitWXLogin();
                CommonParams.setNetWorkExitOrUnReg();
            }
        });
        radaLL.setOnClickListener((v) -> {
            rada.setChecked(true);
        });
        radbLL.setOnClickListener((v) -> {
            radb.setChecked(true);
        });
        radcLL.setOnClickListener((v) -> {
            radc.setChecked(true);
        });
        rada.setOnCheckedChangeListener((buttonView, isChecked) -> {
            submit.setEnabled(true);
            if (isChecked) {
                radb.setChecked(false);
                radc.setChecked(false);
            }
        });
        radb.setOnCheckedChangeListener((buttonView, isChecked) -> {
            submit.setEnabled(true);
            if (isChecked) {
                rada.setChecked(false);
                radc.setChecked(false);
            }
        });
        radc.setOnCheckedChangeListener((buttonView, isChecked) -> {
            submit.setEnabled(true);
            if (isChecked) {
                rada.setChecked(false);
                radb.setChecked(false);
            }
        });
        submit.setEnabled(false);
    }

    private boolean isSendUnReg = false;
    private LoginLodingStartStatus event;

    @Subscribe //用户登录状态变化
    public void loginStatusEvent(LoginLodingStartStatus event) {
        if(isSendUnReg && this.event == null) {
            this.event = event;
            event.getLoginLoadingLiveData().observe(this, result -> {
                if (result == 1 || result == 2) {
                    AppInfo.exitLogin();
                    submitFinish();
                } else if (result == -1) {
                    ToastUtil.show(getActivity(), "账户注销异常");
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        cliseDialog();
        super.onDestroy();
    }

    @Override
    public void dismiss() {
        super.dismiss();
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

    //关闭弹窗
    private void cliseDialog() {
        if (isShowResultUI) {
            EventBus.getDefault().post(new UserUnRegisteredEvent());
            closeListener.close(true);
        } else {
            closeListener.close(false);
        }
    }

    //提交完成
    private void submitFinish() {
        hideLoading();
        resultUICount = 5;
        goResultUI();
    }

    //显示为结果UI
    private void goResultUI() {
        isShowResultUI = true;
        selectContent.setVisibility(View.GONE);
        selectResult.setVisibility(View.VISIBLE);
        submit.setText("我知道了(" + resultUICount + "S)");
        handler.removeCallbacksAndMessages(getActivity());
        handler.removeCallbacksAndMessages(this);
        handler.postDelayed(() -> {
            if (resultUICount <= 0) {
                dismiss();
                return; //已经为最后了。关闭弹窗
            }
            resultUICount--;
            goResultUI();
        }, 1000);
    }

    public void hideLoading() {
        if (loadingHintDialog != null) {
            loadingHintDialog.disMissDialog();
        }
    }
}
