package com.donews.utilslibrary.utils;

import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.widget.Toast;

import java.lang.reflect.Field;

public class BaseToast {
    private static BaseToast mAppToast;
    private final Toast mToast;


    private static Field sField_TN;
    private static Field sField_TN_Handler;

    static {
        try {
            sField_TN = Toast.class.getDeclaredField("mTN");
            sField_TN.setAccessible(true);
            sField_TN_Handler = sField_TN.getType().getDeclaredField("mHandler");
            sField_TN_Handler.setAccessible(true);
        } catch (Exception e) {
        }
    }

    private static void hook(Toast toast) {
        try {
            Object tn = sField_TN.get(toast);
            Handler preHandler = (Handler) sField_TN_Handler.get(tn);
            sField_TN_Handler.set(tn, new SafelyHandlerWarpper(preHandler));
        } catch (Exception e) {
        }
    }


    public BaseToast(Context context) {
        mToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
    }

    public static BaseToast makeToast(Context context) {
        mAppToast = new BaseToast(context);
        return mAppToast;
    }

    public BaseToast setToastText(int resId) {
        mToast.setText(resId);
        return this;
    }

    public BaseToast setToastText(CharSequence charSequence) {
        mToast.setText(charSequence);
        return setToastShortDuration();
    }

    public BaseToast setToastLongText(CharSequence charSequence) {
        mToast.setText(charSequence);
        return setToastLongDuration();
    }

    public BaseToast setToastLongDuration() {
        mToast.setDuration(Toast.LENGTH_LONG);
        mToast.setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL, 0, 0);
        return this;
    }

    public BaseToast setToastShortDuration() {
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL, 0, 0);
        return this;
    }

    public void showToast() {
        hook(mToast);
        mToast.show();
    }



}
