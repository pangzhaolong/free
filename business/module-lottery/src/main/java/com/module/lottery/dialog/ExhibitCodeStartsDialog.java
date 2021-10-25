package com.module.lottery.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.module_lottery.R;

import java.lang.ref.WeakReference;

//展示生成的抽奖码
public class ExhibitCodeStartsDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private OnStateListener mOnFinishListener;
    private LotteryHandler mLotteryHandler = new LotteryHandler(this);

    public ExhibitCodeStartsDialog(Context context) {
        super(context, R.style.dialogTransparent);//内容样式在这里引入
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exhibit_code_dialog_layout);
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.7); // 宽度设置为屏幕宽度的80%
        //lp.dimAmount=0.0f;//外围遮罩透明度0.0f-1.0f
        dialogWindow.setAttributes(lp);
        dialogWindow.setGravity(Gravity.CENTER);//内围区域底部显示
        Message mes = new Message();
        mes.what = 1;
        mLotteryHandler.sendMessageDelayed(mes, 3000);
    }


    @Override
    public void setOnDismissListener(@Nullable OnDismissListener listener) {
        super.setOnDismissListener(listener);


    }

    @Override
    public void onClick(View view) {
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if (mOnFinishListener != null) {
            mOnFinishListener.onFinish();
        }
        return super.onKeyDown(keyCode, event);
    }


    public void setStateListener(OnStateListener l) {
        mOnFinishListener = l;
    }

    public interface OnStateListener {
        /**
         * 此时可以关闭Activity了
         */
        void onFinish();

        void onJumpAd();


    }

    private static class LotteryHandler extends Handler {
        private WeakReference<ExhibitCodeStartsDialog> reference;   //

        LotteryHandler(ExhibitCodeStartsDialog context) {
            reference = new WeakReference(context);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (reference.get() != null && reference.get().mOnFinishListener != null) {
                        //广告跳转
                        reference.get().mOnFinishListener.onJumpAd();
                    }
                    break;
            }
        }
    }


}