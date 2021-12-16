package com.donews.main.dialog.ext;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.donews.main.R;

/**
 * @author lcl
 * Date on 2021/12/13
 * Description:
 * 暴击福利(争取->暴击时刻新人专属、暴击时刻今日福利)使用同一个
 */
public class CritWelfareDialogFragment extends DialogFragment {

    private final static String TAG = "CritWelfareDialogFragment";

    /**
     * 确定按钮的监听
     */
    public interface OnSurListener {
        /**
         * 确定按钮的监听
         *
         * @param type    类型：0=新人专享，1=
         * @param curJd
         * @param totalJd
         */
        void onSur(int type, int curJd, int totalJd);
    }

    /**
     * 关闭按钮的监听
     */
    public interface OnCloseButListener {
        void close(CritWelfareDialogFragment dialog);
    }

    /**
     * 对话框关闭的监听。全局的关闭监听
     */
    public interface OnFinishDismissListener {
        void close(CritWelfareDialogFragment dialog);
    }

    /**
     * 类型
     * 0:新人专享
     * 1: 今日福利
     */
    public int type = 0;
    /**
     * 当前进度
     */
    public int currJd = 0;
    /**
     * 总的进度
     */
    public int totalJd = 1;

    TextView but;
    TextView desc;
    TextView name;
    TextView proTv;
    ImageView close;
    ImageView ivSuo;
    ProgressBar progress;

    private OnSurListener surListener;
    private OnCloseButListener closeListener;
    private OnFinishDismissListener dissListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //设置dialog的基本样式参数
        this.getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = this.getDialog().getWindow();
        //去掉dialog默认的padding
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //设置dialog的位置在底部
        lp.gravity = Gravity.BOTTOM;
        //设置dialog的动画
        lp.windowAnimations = R.style.Dialog_BottomToTopAnim;
        window.setAttributes(lp);
        window.setBackgroundDrawable(new ColorDrawable());

        View view = inflater.inflate(R.layout.main_crit_welfare_dialog_fragment, null);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().setCanceledOnTouchOutside(false);
        return view;
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
        but = getView().findViewById(R.id.main_crit_welfare_but);
        name = getView().findViewById(R.id.main_crit_welfare_name);
        desc = getView().findViewById(R.id.main_crit_welfare_desc);
        close = getView().findViewById(R.id.main_crit_welfare_close);
        ivSuo = getView().findViewById(R.id.main_crit_welfare_iv_suo);
        progress = getView().findViewById(R.id.main_crit_welfare_pro);
        proTv = getView().findViewById(R.id.main_crit_welfare_pro_tv);
        close.setOnClickListener(v -> {
            if (closeListener != null) {
                closeListener.close(this);
            }
            dismiss();
        });
        but.setOnClickListener(v -> {
            if (surListener != null) {
                surListener.onSur(type, currJd, totalJd);
            }
            dismiss();
        });
        getDialog().setOnDismissListener((DialogInterface.OnDismissListener) dialog -> {
            if (dissListener != null) {
                dissListener.close(this);
            }
        });
        updateUI();
    }

    public void setSurListener(OnSurListener surListener) {
        this.surListener = surListener;
    }

    public void setCloseListener(OnCloseButListener closeListener) {
        this.closeListener = closeListener;
    }

    public void setDissListener(OnFinishDismissListener dissListener) {
        this.dissListener = dissListener;
    }

    public void show(FragmentManager fragmentManager) {
        show(fragmentManager, this.toString());
    }

    //更新UI
    private void updateUI() {
        int proJd = (int) ((currJd / (totalJd * 1F)) * 100);
        progress.setProgress(Math.min(proJd, 100));
        progress.setSecondaryProgress(Math.min(proJd, 100));
        proTv.setText(currJd + "/" + totalJd);
        if (type == 0) {
            name.setText("新人专享特权");
        } else {
            name.setText("今日福利");
        }
        desc.setText(Html.fromHtml("抽奖 <font color='#D82D2A'>" + totalJd + "</font> 次即可解锁"));
        if (proJd >= 100) {
            ivSuo.setVisibility(View.GONE);
        } else {
            ivSuo.setVisibility(View.VISIBLE);
        }
    }

}
