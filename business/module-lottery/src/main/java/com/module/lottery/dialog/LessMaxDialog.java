package com.module.lottery.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.module_lottery.R;

//抽奖码小于6个
public class LessMaxDialog extends BaseDialog implements View.OnClickListener{
    private Context context;

    public LessMaxDialog(Context context) {
        super(context, R.style.dialogTransparent);//内容样式在这里引入

        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.less_max_dialog_layout);

    }

    @Override
    public void onClick(View view) {
    }
    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if(mOnFinishListener!=null){
            mOnFinishListener.onFinish();
        }
        return super.onKeyDown(keyCode, event);
    }
    NoDrawDialog.OnFinishListener mOnFinishListener;


    public void setFinishListener(NoDrawDialog.OnFinishListener l) {
        mOnFinishListener=l;
    }
    public interface OnFinishListener {
        /**
         * 此时可以关闭Activity了
         *
         */
        void onFinish( );
    }
}