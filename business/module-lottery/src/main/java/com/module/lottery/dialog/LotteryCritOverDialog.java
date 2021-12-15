package com.module.lottery.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import androidx.annotation.NonNull;

import com.donews.base.utils.ToastUtil;
import com.donews.network.EasyHttp;
import com.donews.network.cache.model.CacheMode;
import com.donews.network.callback.SimpleCallBack;
import com.donews.network.exception.ApiException;
import com.module.lottery.bean.RecommendBean;
import com.module.lottery.model.LotteryModel;
import com.module.lottery.utils.ImageUtils;
import com.module.lottery.utils.RandomProbability;
import com.module_lottery.R;
import com.module_lottery.databinding.LotteryCritCommodityLayoutBinding;
import com.module_lottery.databinding.LotteryCritOverDialogLayoutBinding;

import java.lang.ref.WeakReference;

import io.reactivex.disposables.Disposable;

public class LotteryCritOverDialog extends BaseDialog<LotteryCritOverDialogLayoutBinding> implements DialogInterface.OnDismissListener {
    private CritOverHandler mCritOverHandler = new CritOverHandler(this);
    String mGoodsId;

    public LotteryCritOverDialog(String goodsId, Context context) {
        super(context, R.style.dialogTransparent);
        this.mGoodsId = goodsId;
    }

    @Override
    public int setLayout() {
        return R.layout.lottery_crit_over_dialog_layout;
    }

    @Override
    public float setSize() {
        return 0.85f;
    }
    //获取商品数据


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        //延迟一秒出现关闭按钮



        Message message = new Message();
        message.what = 1;
        mCritOverHandler.sendMessageDelayed(message, 1000);
        setOnDismissListener(this);
    }


    private void initView() {

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
            }
        }
    }


}
