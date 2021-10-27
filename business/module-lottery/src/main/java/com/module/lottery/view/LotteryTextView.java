package com.module.lottery.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.lang.ref.WeakReference;
import java.util.Random;

public class LotteryTextView extends androidx.appcompat.widget.AppCompatTextView {

    WeakReferenceHandler weakReferenceHandler = new WeakReferenceHandler(this);


    public LotteryTextView(Context context) {
        super(context);
    }

    public LotteryTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LotteryTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void start() {
        Random random = new Random();
        int ran = random.nextInt(10);
        Message message = new Message();
        message.obj = ran;
        message.what = 1;
        weakReferenceHandler.sendMessageDelayed(message, 20);

    }

    public void destroy() {
        if (weakReferenceHandler != null) {
            weakReferenceHandler.removeMessages(1);
            weakReferenceHandler.removeMessages(0);
            weakReferenceHandler.removeCallbacksAndMessages(null);
            weakReferenceHandler = null;
        }


    }

    private static class WeakReferenceHandler extends Handler {

        private WeakReference<LotteryTextView> reference;   //

        WeakReferenceHandler(LotteryTextView view) {
            reference = new WeakReference(view);
        }


        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (reference != null) {
                        reference.get().setText(msg.obj.toString());
                        reference.get().start();
                    }
                    break;
            }

        }
    }


}



