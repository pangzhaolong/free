package com.donews.middle.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.donews.middle.R;


@SuppressLint("ViewConstructor")
public class CountDownViewNew extends LinearLayout {

    private final TextView mH0, mH1, mM0, mM1, mS0, mS1;

    private CountDownTimer mNewUserGiftCountDownTimer = null;
    private long mDurationTime = 0;

    public CountDownViewNew(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        View view = LayoutInflater.from(context).inflate(R.layout.middle_count_down_layout, this, true);
        mH0 = view.findViewById(R.id.count_down_h0);
        mH1 = view.findViewById(R.id.count_down_h1);
        mM0 = view.findViewById(R.id.count_down_m0);
        mM1 = view.findViewById(R.id.count_down_m1);
        mS0 = view.findViewById(R.id.count_down_s0);
        mS1 = view.findViewById(R.id.count_down_s1);

//        updateCountDownTime();
    }

    public void updateCountDownTime(long durationTime) {
        if (mNewUserGiftCountDownTimer != null) {
            mNewUserGiftCountDownTimer.cancel();
            mNewUserGiftCountDownTimer = null;
        }
        mDurationTime = durationTime;

        mNewUserGiftCountDownTimer = new CountDownTimer(mDurationTime, 1000) {
            @SuppressLint("DefaultLocale")
            @Override
            public void onTick(long millisUntilFinished) {
                mDurationTime -= 1000;
                long h = (mDurationTime % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
                long m = (mDurationTime % (1000 * 60 * 60)) / (1000 * 60);
                long s = (mDurationTime % (1000 * 60)) / 1000;
                mH0.setText(String.valueOf(h / 10));
                mH1.setText(String.valueOf(h % 10));
                mM0.setText(String.valueOf(m / 10));
                mM1.setText(String.valueOf(m % 10));
                mS0.setText(String.valueOf(s / 10));
                mS1.setText(String.valueOf(s % 10));
            }

            @Override
            public void onFinish() {
                if (mNewUserGiftCountDownTimer != null) {
                    mNewUserGiftCountDownTimer.cancel();
                    mNewUserGiftCountDownTimer = null;
                }
            }
        };
        mNewUserGiftCountDownTimer.start();
    }

    public void clear() {
        if (mNewUserGiftCountDownTimer != null) {
            mNewUserGiftCountDownTimer.cancel();
            mNewUserGiftCountDownTimer = null;
        }
    }
}
