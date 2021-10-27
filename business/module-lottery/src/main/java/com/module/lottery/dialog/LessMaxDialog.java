package com.module.lottery.dialog;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import androidx.annotation.NonNull;
import com.module.lottery.bean.LotteryCodeBean;
import com.module_lottery.R;
import com.module_lottery.databinding.LessMaxDialogLayoutBinding;
//抽奖码小于6个
public class LessMaxDialog extends BaseDialog<LessMaxDialogLayoutBinding> {
    private Context mContext;
    LotteryCodeBean mLotteryCodeBean;
    public LessMaxDialog(Context context, LotteryCodeBean lotteryCodeBean) {
        super(context, R.style.dialogTransparent);//内容样式在这里引入
        mLotteryCodeBean=lotteryCodeBean;
        this.mContext = context;
    }

    @Override
    public int setLayout() {
        return R.layout.less_max_dialog_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    public float setSize() {
        return 0.7f;
    }

    @SuppressLint("RestrictedApi")
   void initView(){
       if(mLotteryCodeBean!=null){
           mDataBinding.number.setText((6-mLotteryCodeBean.getCodes().size())+"");
           mDataBinding.jsonAnimation.setImageAssetsFolder("images");
           mDataBinding.jsonAnimation.setAnimation("illustrate.json");
           mDataBinding.jsonAnimation.loop(true);
           mDataBinding.jsonAnimation.playAnimation();

       }
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