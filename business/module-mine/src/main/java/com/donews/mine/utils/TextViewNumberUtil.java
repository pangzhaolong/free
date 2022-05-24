package com.donews.mine.utils;

import android.animation.Animator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.widget.TextView;

import java.text.DecimalFormat;

/**
 * @author lcl
 * Date on 2022/5/23
 * Description:
 * textView 数子增长动画工具
 */
public class TextViewNumberUtil {

    /**
     * 添加TextView的增长动画
     *
     * @param tv         TextView
     * @param startValue 初始开始的值
     * @param endValue   最终显示的值
     */
    public static void addTextViewAddAnim(TextView tv, double startValue, double endValue) {
        double newStartValue = startValue;
        if (tv.getTag() != null && tv.getTag() instanceof ValueAnimator) {
            //取消原有动画
            ((ValueAnimator) tv.getTag()).cancel();
            newStartValue = Double.parseDouble(tv.getText().toString());
        }
        TextViewEvaluator evaluator = new TextViewEvaluator(newStartValue, endValue);
        ValueAnimator animator = ValueAnimator.ofObject(evaluator, tv);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                tv.setTag(null);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                tv.setTag(null);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        tv.setTag(animator);
        //动画时间
        animator.setDuration(800);
        animator.start();
    }

    //核心类
    static class TextViewEvaluator implements TypeEvaluator<TextView> {
        private double value = 0;
        private double initValue = 0;

        TextViewEvaluator(double initValue, double value) {
            this.value = value;
            this.initValue = initValue;
        }

        @Override
        public TextView evaluate(float fraction, TextView startValue, TextView textView) {
            //样式具体改变 (自定义)
            TextView tv = (TextView) textView;
            DecimalFormat df = new DecimalFormat("#0");
            tv.setText(df.format(initValue + (value * fraction)));
            return startValue;
        }

    }
}
