package com.module.lottery.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.donews.base.model.BaseLiveDataModel;
import com.donews.base.utils.ToastUtil;
import com.donews.common.ad.business.monitor.LotteryAdCount;
import com.donews.middle.abswitch.ABSwitch;
import com.donews.network.EasyHttp;
import com.donews.network.cache.model.CacheMode;
import com.donews.network.callback.SimpleCallBack;
import com.donews.network.exception.ApiException;
import com.module.lottery.bean.CritCodeBean;
import com.module.lottery.bean.GenerateCodeBean;
import com.module.lottery.bean.RecommendBean;
import com.module.lottery.model.LotteryModel;
import com.module.lottery.ui.BaseParams;
import com.module.lottery.utils.ImageUtils;
import com.module.lottery.utils.LotteryAnimationUtils;
import com.module.lottery.utils.RandomProbability;
import com.module_lottery.R;
import com.module_lottery.databinding.LotteryCritCommodityLayoutBinding;
import com.module_lottery.databinding.LotteryCritOverDialogLayoutBinding;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.Map;

import io.reactivex.disposables.Disposable;

public class LotteryCritOverDialog extends BaseDialog<LotteryCritOverDialogLayoutBinding> implements DialogInterface.OnDismissListener {
    private CritOverHandler mCritOverHandler = new CritOverHandler(this);
    private String mGoodsId;
    private BaseLiveDataModel baseLiveDataModel;
    private OnStateListener mOnFinishListener;

    public LotteryCritOverDialog(String goodsId, Context context) {
        super(context, R.style.dialogTransparent);
        baseLiveDataModel = new BaseLiveDataModel();
        this.mGoodsId = goodsId;
    }

    @Override
    public int setLayout() {
        return R.layout.lottery_crit_over_dialog_layout;
    }

    @Override
    public float setDimAmount() {
        return 0.9f;
    }

    @Override
    public float setSize() {
        return 1.0f;
    }
    //获取商品数据


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //延迟一秒出现关闭按钮
        Message message = new Message();
        message.what = 1;
        mCritOverHandler.sendMessageDelayed(message, 1000);
        //延迟一秒出现关闭按钮
        Message msg = new Message();
        msg.what = 2;
        mCritOverHandler.sendMessageDelayed(msg, 3000);
        setOnDismissListener(this);


        Message animationMessage = new Message();
        animationMessage.what = 3;
        mCritOverHandler.sendMessageDelayed(animationMessage, 500);

    }

    private void multipleCode() {
        //生成多个抽奖码
        if (baseLiveDataModel != null && mGoodsId != null) {
            Map<String, String> params = BaseParams.getMap();
            params.put("goods_id", mGoodsId);
            getNeCritData(params, LotteryModel.LOTTERY_CRIT_CODE);
        }
    }

    private void initView() {
        mDataBinding.overView.setVisibility(View.VISIBLE);
        mDataBinding.overView.startAnimation(LotteryAnimationUtils.setTranslateAnimation(getContext()));
    }


    private void getNeCritData(Map<String, String> params, String url) {
        JSONObject json = new JSONObject(params);
        baseLiveDataModel.unDisposable();
        baseLiveDataModel.addDisposable(EasyHttp.post(url)
                .cacheMode(CacheMode.NO_CACHE)
                .upJson(json.toString())
                .execute(new SimpleCallBack<CritCodeBean>() {
                    @Override
                    public void onError(ApiException e) {
                        //广告跳转
                        if (mOnFinishListener != null) {
                            mOnFinishListener.onFinish();
                        }
                        Toast.makeText(getContext(), "抽奖码获取失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(CritCodeBean generateCode) {
                        if (generateCode != null) {
                            //抽奖统计
                            LotteryAdCount.INSTANCE.lotterySuccess();
                            if (mOnFinishListener != null) {
                                mOnFinishListener.onCritJump(generateCode);
                                mOnFinishListener.onFinish();
                            }
                        }
                    }
                }));


    }


    public void setStateListener(OnStateListener l) {
        mOnFinishListener = l;
    }

    public interface OnStateListener {
        /**
         * 此时可以关闭Activity了
         */
        void onFinish();

        void onCritJump(CritCodeBean critCodeBean);

    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        if (mCritOverHandler != null) {
            mCritOverHandler.removeMessages(0);
            mCritOverHandler.removeMessages(1);
            mCritOverHandler.removeCallbacksAndMessages(null);
        }
    }


    private static class CritOverHandler extends Handler {
        private WeakReference<LotteryCritOverDialog> reference;   //

        CritOverHandler(LotteryCritOverDialog context) {
            reference = new WeakReference(context);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (reference.get() != null) {
                    }
                    break;
                case 2:
                    if (reference.get() != null) {
                        reference.get().multipleCode();
                    }
                    break;

                case 3:
                    if (reference.get() != null) {
                        reference.get().initView();
                    }
                    break;

            }
        }
    }


}
