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
public class LessMaxDialog extends Dialog implements View.OnClickListener{
    private Context context;

    public LessMaxDialog(Context context) {
        super(context, R.style.dialogTransparent);//内容样式在这里引入

        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.less_max_dialog_layout);


        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.7); // 宽度设置为屏幕宽度的80%
        //lp.dimAmount=0.0f;//外围遮罩透明度0.0f-1.0f
        dialogWindow.setAttributes(lp);
        dialogWindow.setGravity(Gravity.CENTER);//内围区域底部显示

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