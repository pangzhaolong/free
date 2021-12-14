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
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.donews.main.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lcl
 * Date on 2021/12/13
 * Description:
 * 下载App的模块
 */
public class CritDownAppDialogFragment extends DialogFragment {

    private final static String TAG = "CritWelfareDialogFragment";

    /**
     * 确定按钮的监听
     */
    public interface OnItemClickListener {
        /**
         * 确定按钮的监听
         *
         * @param item 点击的Item数据
         */
        void onSur(ItemData item);
    }

    /**
     * 关闭按钮的监听
     */
    public interface OnCloseButListener {
        void close(CritDownAppDialogFragment dialog);
    }

    /**
     * 对话框关闭的监听。全局的关闭监听
     */
    public interface OnFinishDismissListener {
        void close(CritDownAppDialogFragment dialog);
    }

    private OnItemClickListener surListener;
    private OnCloseButListener closeListener;
    private OnFinishDismissListener dissListener;
    //每个item视图id
    private int itemLayoutRes = R.layout.incl_main_crit_down_app_dialog_item;
    //数据列表
    private List<ItemData> dataList = new ArrayList();
    //关闭按钮
    private ImageView close;
    //滚动的视图
    private ScrollView slView;
    //列表显示的容器
    private LinearLayout contentListll;
    //内容区最大高度。为屏幕高度的一半
    private int contentMaxHei = (int) (ScreenUtils.getScreenHeight() * 0.5F);
    private ViewTreeObserver.OnGlobalLayoutListener globallListener;
    //item的高度
    private int itemHei = -1;

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
        View view = inflater.inflate(R.layout.main_crit_down_app_dialog_fragment, null);
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
        slView = getView().findViewById(R.id.sl_view);
        contentListll = getView().findViewById(R.id.main_crit_down_item_layout);
        close = getView().findViewById(R.id.main_crit_down_close);
        close.setOnClickListener(v -> {
            if (closeListener != null) {
                closeListener.close(this);
            }
            dismiss();
        });
        getDialog().setOnDismissListener((DialogInterface.OnDismissListener) dialog -> {
            if (dissListener != null) {
                dissListener.close(this);
            }
        });
        for (int i = 0; i < 3; i++) {
            dataList.add(new ItemData());
        }
        updateData();
    }

    public void setSurListener(OnItemClickListener surListener) {
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
    private void updateData() {
        contentListll.removeAllViews();
        for (ItemData itemData : dataList) {
            View itemView = LayoutInflater.from(getContext()).inflate(itemLayoutRes, null);
            contentListll.addView(itemView);
            if(itemHei <= 0){
                buildSlHei(itemView);
                break;
            }
            //可以开始绑定数据了
            bindData(itemData,itemView);
        }

        //计算视图高度
        ViewGroup.LayoutParams lp = slView.getLayoutParams();
        if (dataList.size() * itemHei > contentMaxHei) {
            lp.height = contentMaxHei;
        } else {
            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
        slView.setLayoutParams(lp);
    }

    //调整列表滚动容器的高度
    private void buildSlHei(View view) {
        //计算其中一个item的高度
        if(itemHei >= 0){
            return; //正在计算中,取消重复计算
        }
        if (globallListener == null) {
            itemHei = 0;
            globallListener = () -> {
                view.getViewTreeObserver().removeOnGlobalLayoutListener(globallListener);
                itemHei = view.getHeight();
                updateData();
            };
            view.getViewTreeObserver().addOnGlobalLayoutListener(globallListener);
        }
    }

    //绑定数据
    private void bindData(ItemData data,View view){

    }

    /**
     * 每项对应的数据
     */
    public static class ItemData {

    }

}
