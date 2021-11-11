package com.module.lottery.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.module.lottery.adapter.ScrollListAdapter;
import com.module.lottery.utils.ScrollLinearLayoutManager;
import com.orhanobut.logger.Logger;

import java.lang.ref.WeakReference;

public class LotteryRecyclerView extends RecyclerView {
    CountdownHandler mCountdownHandler;
    private boolean mScroll;

    public LotteryRecyclerView(@NonNull Context context) {
        this(context, null);
    }

    public LotteryRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public LotteryRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //父层ViewGroup不要拦截点击事件
//        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }


    //设置自动滚动
    public void setAutomaticScroll(boolean scroll) {
        mScroll = scroll;
    }


    public void start() {
        if (mCountdownHandler == null) {
            mCountdownHandler = new CountdownHandler(this);
        }
        stop();
        if (mCountdownHandler != null && mScroll) {
            Message message = new Message();
            message.what = 1;
            mCountdownHandler.handleMessage(message);
        }
    }






    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        release();
    }

    public void release() {
        if (mCountdownHandler != null) {
            stop();
            mCountdownHandler = null;
        }
    }

    public void stop() {
        if (mCountdownHandler != null) {
            mCountdownHandler.removeMessages(1);
            mCountdownHandler.removeMessages(0);
            mCountdownHandler.removeCallbacksAndMessages(null);
        }
    }

    public static class CountdownHandler extends Handler {

        private WeakReference<LotteryRecyclerView> reference;   //

        CountdownHandler(LotteryRecyclerView context) {
            reference = new WeakReference(context);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (reference.get() != null && reference.get().getChildAt(0) != null) {
                        int currentPosition = ((RecyclerView.LayoutParams) reference.get().getChildAt(0).getLayoutParams()).getViewAdapterPosition();
                        currentPosition = currentPosition + 1;
                        ScrollLinearLayoutManager linearLayoutManager = (ScrollLinearLayoutManager) reference.get().getLayoutManager();
                        linearLayoutManager.smoothScrollToPosition(reference.get(), new State(), currentPosition);
                    }
                    Message message = new Message();
                    message.what = 1;
                    sendMessageDelayed(message, 2000);
                    break;
            }

        }
    }


}
