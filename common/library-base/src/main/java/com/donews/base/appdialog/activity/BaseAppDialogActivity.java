package com.donews.base.appdialog.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @author by SnowDragon
 * Date on 2020/11/25
 * Description:
 */
public abstract class BaseAppDialogActivity extends AppCompatActivity {

    private static String mActivityJumpTag;        //activity跳转tag
    private static long mClickTime;                //activity跳转时间

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        setDataBinding();

        initView();
    }

    /**
     * 初始化视图
     */
    public abstract void initView();

    /**
     * 资源布局
     *
     * @return resLayoutId
     */
    public abstract void setDataBinding();


    /**
     * 检查是否重复跳转，不需要则重写方法并返回true
     */
    protected static boolean checkDoubleClick(Intent intent) {

        // 默认检查通过
        boolean result = true;
        // 标记对象
        String tag;
        if (intent.getComponent() != null) { // 显式跳转
            tag = intent.getComponent().getClassName();
        } else if (intent.getAction() != null) { // 隐式跳转
            tag = intent.getAction();
        } else {
            return true;
        }

        if (tag.equals(mActivityJumpTag) && mClickTime >= SystemClock.uptimeMillis() - 500) {
            // 检查不通过
            result = false;
        }

        // 记录启动标记和时间
        mActivityJumpTag = tag;
        mClickTime = SystemClock.uptimeMillis();
        return result;
    }
}
