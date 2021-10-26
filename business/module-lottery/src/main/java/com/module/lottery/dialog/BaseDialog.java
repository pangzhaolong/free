package com.module.lottery.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.module.lottery.model.LotteryModel;
import com.module_lottery.R;

public  abstract class BaseDialog<V extends ViewDataBinding> extends Dialog {
    protected V mDataBinding;
    public abstract int setLayout();
    public BaseDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataBinding= DataBindingUtil.inflate(LayoutInflater.from(getContext()),  setLayout(),null,true);
        setContentView(mDataBinding.getRoot());
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = getContext().getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * setSize());
        //lp.dimAmount=0.0f;//外围遮罩透明度0.0f-1.0f
        dialogWindow.setAttributes(lp);
        dialogWindow.setGravity(Gravity.CENTER);//内围区域底部显示
//        //设置window背景，默认的背景会有Padding值，不能全屏。当然不一定要是透明，你可以设置其他背景，替换默认的背景即可。
//        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        //一定要在setContentView之后调用，否则无效
//        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

    }

    public abstract float setSize();

}
