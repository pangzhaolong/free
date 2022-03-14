/**
 * 额外获得奖励的弹窗
 *
 * @author hegai
 * @version v1.0
 * @date 2021/12/8
 */

package com.donews.mine.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BaseDialog<V extends ViewDataBinding> extends Dialog {
    protected V mDataBinding;
    private static final String LOTTERY_PS = "Lottery_BaseDialog";


    private SharedPreferences mSharedPreferences;

    public abstract int setLayout();

    public float setDimAmount() {


        return 0.8f;
    }


    public BaseDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSharedPreferences = getContext().getSharedPreferences(LOTTERY_PS, 0);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        mDataBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), setLayout(), null, true);
        setContentView(mDataBinding.getRoot());
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = getContext().getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * setSize());
        lp.dimAmount = setDimAmount();//外围遮罩透明度0.0f-1.0f
        dialogWindow.setAttributes(lp);
        dialogWindow.setGravity(Gravity.CENTER);//内围区域底部显示
//        //设置window背景，默认的背景会有Padding值，不能全屏。当然不一定要是透明，你可以设置其他背景，替换默认的背景即可。
//        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        //一定要在setContentView之后调用，否则无效
//        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

    }

    public void show(Context context) {
        if (context != null && context instanceof Activity && !((Activity) (context)).isFinishing() && !((Activity) (context)).isDestroyed()) {
            super.show();
        }
    }

    public void dismiss() {
        super.dismiss();
    }

    public SharedPreferences.Editor getEditor() {
        if (mSharedPreferences != null) {
            return mSharedPreferences.edit();
        }
        return null;
    }

    public SharedPreferences getSharedPreferences() {
        return mSharedPreferences;
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        return true;
    }


    public abstract float setSize();


    private CompositeDisposable mCompositeDisposable;

    public void addDisposable(Disposable disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);
    }

    public void unDisposable() {
        if (mCompositeDisposable != null && mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.clear();
        }
    }

}
