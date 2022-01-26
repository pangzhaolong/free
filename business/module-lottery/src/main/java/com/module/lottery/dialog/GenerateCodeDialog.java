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
import com.donews.middle.abswitch.ABSwitch;
import com.donews.network.EasyHttp;
import com.donews.network.cache.model.CacheMode;
import com.donews.network.callback.SimpleCallBack;
import com.donews.network.exception.ApiException;
import com.donews.utilslibrary.analysis.AnalysisUtils;
import com.donews.utilslibrary.dot.Dot;
import com.donews.utilslibrary.utils.AppInfo;
import com.module.lottery.bean.CritCodeBean;
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
    private String mOptionalCode;

    public GenerateCodeDialog(Context context, String goodsId, String optionalCode) {
        super(context, R.style.dialogTransparent);//内容样式在这里引入
        baseLiveDataModel = new BaseLiveDataModel();
        mGoodsId = goodsId;
        mOptionalCode = optionalCode;
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

        //生成抽奖码分为两种情况 本地生成  和服务器生成
        boolean logType = AppInfo.checkIsWXLogin();
        if (ABSwitch.Ins().getLotteryLine() == 1 && !logType) {
            mLotteryHandler.sendMessageDelayed(mes, 2000);
        } else {
            mLotteryHandler.sendMessageDelayed(mes, 800);
        }
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
        //生成抽奖码分为两种情况 本地生成  和服务器生成
        boolean logType = AppInfo.checkIsWXLogin();
        if (ABSwitch.Ins().getLotteryLine() == 1 && !logType) {
            //本地生成
            if (mOnFinishListener != null) {
                mOnFinishListener.onExclusiveBulletFrame();
            }
        } else {
            singleCode();
        }
    }

    private void singleCode() {

        //生成一个抽奖码
        if (baseLiveDataModel != null && mGoodsId != null) {
            Map<String, String> params = BaseParams.getMap();
            if (mOptionalCode != null) {
                params.put("cus_code", mOptionalCode);
            }
            params.put("goods_id", mGoodsId);
            getNetData(params, LotteryModel.LOTTERY_GENERATE_CODE);
        }
    }


    private void getNetData(Map<String, String> params, String url) {
        JSONObject json = new JSONObject(params);
        baseLiveDataModel.unDisposable();
        baseLiveDataModel.addDisposable(EasyHttp.post(url)
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
                        AnalysisUtils.onEventEx(getContext(), Dot.PAY_FAIL);
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


        void onExclusiveBulletFrame();
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