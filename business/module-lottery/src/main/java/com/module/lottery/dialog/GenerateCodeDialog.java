package com.module.lottery.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.donews.base.model.BaseLiveDataModel;
import com.donews.network.EasyHttp;
import com.donews.network.cache.model.CacheMode;
import com.donews.network.callback.SimpleCallBack;
import com.donews.network.exception.ApiException;
import com.module.lottery.bean.GenerateCodeBean;
import com.module.lottery.model.LotteryModel;
import com.module.lottery.ui.BaseParams;
import com.module_lottery.R;
import com.module_lottery.databinding.GenerateDialogLayoutBinding;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.Map;

//生成抽奖码
public class GenerateCodeDialog extends BaseDialog<GenerateDialogLayoutBinding> {
    private Context context;
    private OnStateListener mOnFinishListener;
    private LotteryHandler mLotteryHandler = new LotteryHandler(this);
    private BaseLiveDataModel baseLiveDataModel;
    private String mGoodsId;
    public GenerateCodeDialog(Context context,String goodsId) {
        super(context, R.style.dialogTransparent);//内容样式在这里引入
        baseLiveDataModel = new BaseLiveDataModel();
        mGoodsId = goodsId;
        this.context = context;
    }

    @Override
    public int setLayout() {
        return R.layout.generate_dialog_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Message mes = new Message();
        mes.what = 1;
        mLotteryHandler.sendMessageDelayed(mes, 3000);
        initView();
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mDataBinding.lotteryText01.destroy();
                mDataBinding.lotteryText02.destroy();
                mDataBinding.lotteryText03.destroy();
                mDataBinding.lotteryText04.destroy();
                mDataBinding.lotteryText05.destroy();
                mDataBinding.lotteryText06.destroy();
                mDataBinding.lotteryText07.destroy();

            }
        });
    }


    //生成抽奖码
    public void generateLotteryCode() {
        if (baseLiveDataModel != null && mGoodsId != null) {
            Map<String, String> params = BaseParams.getMap();
            params.put("goods_id", mGoodsId);
            JSONObject json = new JSONObject(params);
            baseLiveDataModel.unDisposable();
            baseLiveDataModel.addDisposable(EasyHttp.post(LotteryModel.LOTTERY_GENERATE_CODE)
                    .cacheMode(CacheMode.NO_CACHE)
                    .upJson(json.toString())
                    .execute(new SimpleCallBack<GenerateCodeBean>() {
                        @Override
                        public void onError(ApiException e) {
                            Toast.makeText(getContext(), "抽奖码获取失败", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onSuccess(GenerateCodeBean generateCode) {
                            if (generateCode != null) {

                                //广告跳转
                                if(mOnFinishListener!=null){
                                    mOnFinishListener.onJump(generateCode);
                                }
//                                mDataBinding.setCodeBean(generateCode);
//                                int schedule = 0;
//                                //抽奖码生成成功回调
//                                if (mLotteryCodeBean != null && mLotteryCodeBean.getCodes().size() != 0) {
//                                    schedule = mLotteryCodeBean.getCodes().size();
//                                    schedule = 50 / 5 * (schedule);
//                                } else {
//                                    schedule = 10;
//                                }
////                                schedule=  generateRandomNumber(schedule);
//                                startProgressBar(schedule);
                            }
                        }
                    }));
        }
    }



    private  void initView(){
        mDataBinding.lotteryText01.start();
        mDataBinding.lotteryText02.start();
        mDataBinding.lotteryText03.start();
        mDataBinding.lotteryText04.start();
        mDataBinding.lotteryText05.start();
        mDataBinding.lotteryText06.start();
        mDataBinding.lotteryText07.start();
    }


    @Override
    public float setSize() {
        return 0.7f;
    }


    @Override
    public void setOnDismissListener(@Nullable OnDismissListener listener) {
        super.setOnDismissListener(listener);

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

        void onJump(GenerateCodeBean generateCodeBean);


    }

    private static class LotteryHandler extends Handler {
        private WeakReference<GenerateCodeDialog> reference;   //

        LotteryHandler(GenerateCodeDialog context) {
            reference = new WeakReference(context);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (reference.get() != null && reference.get().mOnFinishListener != null) {
                        reference.get(). generateLotteryCode();
                    }
                    break;
            }
        }
    }


}