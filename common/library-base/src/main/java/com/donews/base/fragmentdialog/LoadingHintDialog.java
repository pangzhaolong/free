package com.donews.base.fragmentdialog;

import android.text.TextUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.donews.base.R;
import com.donews.base.databinding.BaseLoadingDialogBinding;

import java.lang.reflect.Field;

/**
 * @author by SnowDragon
 * Date on 2020/12/16
 * Description:
 */
public class LoadingHintDialog extends AbstractFragmentDialog<BaseLoadingDialogBinding> {
    /**
     * 加载等待时的提示信息
     */
    private String description;
    private ShapeBuilder shapeBuilder;

    public LoadingHintDialog() {
        shapeBuilder = new ShapeBuilder();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.base_loading_dialog;
    }

    @Override
    protected void initView() {


        dataBinding.llBackground.setBackground(shapeBuilder.build(getContext()));

        //等待提示
        if (!TextUtils.isEmpty(description)) {
            dataBinding.tvLoadingMsg.setText(description);
        }

        //加载动画
        Animation operatingAnim = AnimationUtils.loadAnimation(getContext(), R.anim.base_dialog_loading_anim);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        dataBinding.loading.startAnimation(operatingAnim);


    }

    @Override
    protected boolean isUseDataBinding() {
        return true;
    }

    public LoadingHintDialog setDescription(String description) {
        this.description = description;
        return this;
    }

    /**
     * @param backgroundDim true:背景变暗
     * @return this
     */
    public LoadingHintDialog setBackgroundDim(boolean backgroundDim) {
        this.backgroundDim = backgroundDim;
        return this;
    }

    public LoadingHintDialog setBackGroundColor(int color) {
        shapeBuilder.setBackgroundColor(color);
        return this;
    }

    public LoadingHintDialog setBackGroundColor(String color) {
        shapeBuilder.setBackgroundColor(color);
        return this;
    }

    /**
     * @param radius 弧度，单位：dp
     * @return
     */
    public LoadingHintDialog setRadius(int radius) {
        shapeBuilder.setRadius(radius);
        return this;
    }

    /**
     * @param dismissOnBackPressed true:返回键消失
     * @return this
     */
    public LoadingHintDialog setDismissOnBackPressed(boolean dismissOnBackPressed) {
        this.dismissOnBackPressed = dismissOnBackPressed;
        return this;
    }

    /**
     * @param dismissOnTouchOutside true:点击视图外部弹窗消失
     * @return this
     */
    public LoadingHintDialog setDismissOnTouchOutside(boolean dismissOnTouchOutside) {
        this.dismissOnTouchOutside = dismissOnTouchOutside;
        return this;
    }

    public void showAllowingStateLoss(FragmentManager manager, String tag) {
        try {
            Field dismissed = DialogFragment.class.getDeclaredField("mDismissed");
            dismissed.setAccessible(true);
            dismissed.set(this, false);
            Field shown = DialogFragment.class.getDeclaredField("mShownByMe");
            shown.setAccessible(true);
            shown.set(this, true);

            FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, tag);
            ft.commitAllowingStateLoss();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
