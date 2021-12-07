package com.module.lottery.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.donews.base.model.BaseLiveDataModel;
import com.donews.common.ad.business.monitor.LotteryAdCount;
import com.donews.network.EasyHttp;
import com.donews.network.cache.model.CacheMode;
import com.donews.network.callback.SimpleCallBack;
import com.donews.network.exception.ApiException;
import com.donews.utilslibrary.analysis.AnalysisUtils;
import com.donews.utilslibrary.dot.Dot;
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

    public GenerateCodeDialog(Context context, String goodsId) {
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
        mLotteryHandler.sendMessageDelayed(mes, 800);
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
                            //广告跳转
                            if (mOnFinishListener != null) {
                                mOnFinishListener.onFinish();
                            }
                            Toast.makeText(getContext(), "抽奖码获取失败", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onSuccess(GenerateCodeBean generateCode) {
                            if (generateCode != null) {
                                //抽奖统计
                                LotteryAdCount.INSTANCE.lotterySuccess();
                                if (mOnFinishListener != null) {
                                    mOnFinishListener.onJump(generateCode);
                                }
                                AnalysisUtils.onEventEx(getContext(), Dot.PAY_SUCC);
                            } else {
                                AnalysisUtils.onEventEx(getContext(), Dot.PAY_FAIL);
                            }
                        }
                    }));
        }
    }


    private void initView() {
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
                        reference.get().generateLotteryCode();
                    }
                    break;
            }
        }
    }


}