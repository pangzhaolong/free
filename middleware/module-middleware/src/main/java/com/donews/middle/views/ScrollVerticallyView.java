package com.donews.middle.views;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;

import com.donews.middle.R;
import com.donews.middle.bean.front.AwardBean;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ScrollVerticallyView extends FrameLayout {

    List<AwardBean.AwardInfo> mListTitle = new ArrayList<>();
    LinearLayout mLayoutView;
    AnimatorSet animatorSet;
    TextView user_name;
    TextView middle_text;
     ScrollHandler scrollHandler=new ScrollHandler(ScrollVerticallyView.this);
     public ScrollVerticallyView(@NonNull Context context) {
        this(context, null);
    }

    public ScrollVerticallyView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollVerticallyView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.middle_award_info, this, true);
        mLayoutView = findViewById(R.id.layout_view);
        user_name = findViewById(R.id.user_name);
        middle_text = findViewById(R.id.middle_text);
        initAnimationSet();
    }


    public void setData(List<AwardBean.AwardInfo> listTitle) {
        if (listTitle != null) {
            mListTitle.addAll(listTitle);
        }
    }

    public void startAnimation() {
        if (animatorSet != null && !animatorSet.isRunning()) {
            scrollHandler.removeMessages(1);
            scrollHandler.sendEmptyMessage(1);
            animatorSet.removeAllListeners();
            animatorSet.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @SuppressLint("LongLogTag")
                @Override
                public void onAnimationEnd(Animator animation) {
                    Log.d("=wdadawdad===================", "515");
                    scrollHandler.removeMessages(1);
                    scrollHandler.sendEmptyMessage(1);
                    animatorSet.cancel();
                    animatorSet.start();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            //开始动画
            animatorSet.cancel();
            animatorSet.start();
        }
    }


    private void initAnimationSet() {
        if (animatorSet == null) {
            ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(mLayoutView, "alpha", 0.0f, 1f);
            objectAnimator1.setDuration(1500);
            ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(mLayoutView, "translationY", 100f, 0f);
            objectAnimator2.setDuration(1500);
            objectAnimator2.setInterpolator(new DecelerateInterpolator());

            ObjectAnimator objectAnimator3 = ObjectAnimator.ofFloat(mLayoutView, "translationY", 0f, -100f);
            objectAnimator3.setDuration(1500);
            objectAnimator3.setStartDelay(2000);
            objectAnimator3.setInterpolator(new AccelerateInterpolator());

            ObjectAnimator objectAnimator4 = ObjectAnimator.ofFloat(mLayoutView, "alpha", 1.0f, 0f);
            objectAnimator4.setDuration(1500);
            objectAnimator4.setStartDelay(2000);
            animatorSet = new AnimatorSet();
            animatorSet.play(objectAnimator1).with(objectAnimator2).before(objectAnimator3).before(objectAnimator4);
        }
    }


    private static class ScrollHandler extends Handler {
        private WeakReference<ScrollVerticallyView> reference;   //

        ScrollHandler(ScrollVerticallyView context) {
            reference = new WeakReference(context);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (reference.get() != null && reference.get().mLayoutView != null && reference.get().mListTitle.size() > 0) {
                        Random rand = new Random();
                        int n1 = rand.nextInt(reference.get().mListTitle.size());//返回值在范围[0,mListTitle.size()) 即[0,mListTitle.size()-1]
                        reference.get().user_name.setText(reference.get().mListTitle.get(n1).getName());
                        reference.get().middle_text.setText(reference.get().mListTitle.get(n1).getProduceName());
                    }
                    break;
            }
        }
    }
}
