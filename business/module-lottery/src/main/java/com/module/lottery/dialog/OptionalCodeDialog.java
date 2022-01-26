/**
 * 额外获得奖励的弹窗
 *
 * @author hegai
 * @version v1.0
 * @date 2021/12/8
 */

package com.module.lottery.dialog;

import static com.donews.middle.utils.CommonUtils.LOTTERY_FINGER;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.NonNull;

import com.donews.base.utils.ToastUtil;
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
import com.module_lottery.databinding.OptionalCodeDialogBinding;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;

//自选码dialog
public class OptionalCodeDialog extends BaseDialog<OptionalCodeDialogBinding> {
    private Context mContext;

    private LotteryHandler mLotteryHandler = new LotteryHandler(this);

    private OnFinishListener mOnFinishListener;
    private boolean isSendCloseEvent = true;
    private String mGoodsId;
    public OptionalCodeDialog(Context context, String goodsId ) {
        super(context, R.style.dialogTransparent);//内容样式在这里引入
        this.mContext = context;
        this.mGoodsId = goodsId;
        //延迟一秒出现关闭按钮
        Message message = new Message();
        message.what = 1;
        mLotteryHandler.sendMessageDelayed(message, 1000);
    }

    @Override
    public int setLayout() {
        return R.layout.optional_code_dialog;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    public float setSize() {
        return 0.9f;
    }

    @SuppressLint("RestrictedApi")
    void initView() {
        mDataBinding.jsonHand.setImageAssetsFolder("images");
        mDataBinding.jsonHand.setAnimation(LOTTERY_FINGER);
        mDataBinding.jsonHand.loop(true);
        mDataBinding.jsonHand.playAnimation();
        List<String> data = new ArrayList<String>();
        for (int i = 0; i < 10; i++) {
            data.add(i + "");
        }
        mDataBinding.minutePv01.setData(data);
        mDataBinding.minutePv02.setData(data);
        mDataBinding.minutePv03.setData(data);
        mDataBinding.minutePv04.setData(data);
        mDataBinding.minutePv05.setData(data);
        mDataBinding.minutePv06.setData(data);
        mDataBinding.minutePv07.setData(data);
        setOnDismissListener((d) -> {
            if (isSendCloseEvent) {
                AnalysisUtils.onEventEx(mContext, Dot.Lottery_Increase_ChancesDialog_Close);
            }
        });
        mDataBinding.closure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mDataBinding.lotteryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSendCloseEvent = false;
                AnalysisUtils.onEventEx(mContext, Dot.Lottery_Increase_ChancesDialog_Continue);
                if (mOnFinishListener != null) {
                    StringBuffer buffer = new StringBuffer();
                    buffer.append(mDataBinding.minutePv01.getPerformSelectValue() + "");
                    buffer.append(mDataBinding.minutePv02.getPerformSelectValue() + "");
                    buffer.append(mDataBinding.minutePv03.getPerformSelectValue() + "");
                    buffer.append(mDataBinding.minutePv04.getPerformSelectValue() + "");
                    buffer.append(mDataBinding.minutePv05.getPerformSelectValue() + "");
                    buffer.append(mDataBinding.minutePv06.getPerformSelectValue() + "");
                    buffer.append(mDataBinding.minutePv07.getPerformSelectValue() + "");
                    mOnFinishListener.onReturnSelectValue(buffer.toString());
                }
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {

        return true;
    }


    public void setFinishListener(OnFinishListener l) {
        mOnFinishListener = l;
    }

    public interface OnFinishListener {
        void onReturnSelectValue(String value);
        void onError();
    }


    private static class LotteryHandler extends Handler {
        private WeakReference<OptionalCodeDialog> reference;   //

        LotteryHandler(OptionalCodeDialog context) {
            reference = new WeakReference(context);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (reference.get() != null) {
                        reference.get().mDataBinding.closure.setVisibility(View.VISIBLE);
                    }
                    break;
            }
        }
    }


}