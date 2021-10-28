package com.module.lottery.dialog;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.donews.base.model.BaseLiveDataModel;
import com.donews.network.EasyHttp;
import com.donews.network.cache.model.CacheMode;
import com.donews.network.callback.SimpleCallBack;
import com.donews.network.exception.ApiException;
import com.module.lottery.bean.ContactCustomerBean;
import com.module.lottery.bean.GenerateCodeBean;
import com.module.lottery.bean.LotteryCodeBean;
import com.module.lottery.model.LotteryModel;
import com.module.lottery.ui.BaseParams;
import com.module.lottery.viewModel.ExecuteLotteryViewModel;
import com.module_lottery.R;
import com.module_lottery.databinding.ExhibitCodeDialogLayoutBinding;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Random;

import retrofit2.converter.gson.GsonConverterFactory;

//展示生成的抽奖码
public class ExhibitCodeStartsDialog extends BaseDialog<ExhibitCodeDialogLayoutBinding> {
    private Context context;
    private OnStateListener mOnFinishListener;
    private LotteryHandler mLotteryHandler = new LotteryHandler(this);
    private BaseLiveDataModel baseLiveDataModel;
    String mGoodsId;
    LotteryCodeBean mLotteryCodeBean;
    int time = 3000;

    public ExhibitCodeStartsDialog(Context context, String goodsId, LotteryCodeBean lotteryCodeBean) {
        super(context, R.style.dialogTransparent);//内容样式在这里引入
        this.context = context;
        mGoodsId = goodsId;
        baseLiveDataModel = new BaseLiveDataModel();
        this.mLotteryCodeBean = lotteryCodeBean;
        //延迟一秒出现关闭按钮
        Message message = new Message();
        message.what = 2;
        mLotteryHandler.sendMessageDelayed(message, 1000);
    }


    @Override
    public int setLayout() {
        return R.layout.exhibit_code_dialog_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataBinding.closure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnFinishListener != null) {
                    //放在倒计时结束后再次打开
                    freed();
                    mOnFinishListener.onFinish();
                }
            }
        });
        generateLotteryCode();
        initView();


    }


    void initView() {
        if (mLotteryCodeBean.getCodes().size() >= 5) {
            mDataBinding.jsonAnimationLayout.setVisibility(View.GONE);
            mDataBinding.hintLayout.setVisibility(View.VISIBLE);
            automaticJump();

        } else {
            mDataBinding.jsonAnimationLayout.setVisibility(View.VISIBLE);
            mDataBinding.hintLayout.setVisibility(View.GONE);

            mDataBinding.jsonAnimation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    if (mOnFinishListener != null) {
                        mOnFinishListener.onLottery();
                    }
                }
            });
            mDataBinding.jsonAnimation.setImageAssetsFolder("images");
            mDataBinding.jsonAnimation.setAnimation("probability_button.json");
            mDataBinding.jsonAnimation.loop(true);
            mDataBinding.jsonAnimation.playAnimation();


            mDataBinding.jsonAnimationHand.setImageAssetsFolder("images");
            mDataBinding.jsonAnimationHand.setAnimation("hand.json");
            mDataBinding.jsonAnimationHand.loop(true);
            mDataBinding.jsonAnimationHand.playAnimation();

        }


    }


    private void automaticJump() {
        Message message = new Message();
        message.what = 1;
        mLotteryHandler.sendMessageDelayed(message, 1000);
    }


    private void freed() {
        mLotteryHandler.removeMessages(1);
        mLotteryHandler.removeCallbacksAndMessages(null);
        mLotteryHandler.removeMessages(0);
    }

    @Override
    public float setSize() {
        return 0.8f;
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
                                mDataBinding.setCodeBean(generateCode);
                                int schedule = 0;
                                //抽奖码生成成功回调
                                if (mLotteryCodeBean != null && mLotteryCodeBean.getCodes().size() != 0) {
                                    schedule = mLotteryCodeBean.getCodes().size();
                                    schedule = 50 / 5 * (schedule);
                                } else {
                                    schedule = 10;
                                }
//                                schedule=  generateRandomNumber(schedule);
                                startProgressBar(schedule);
                            }
                        }
                    }));
        }
    }

    //在特定区间产生随机数
    @SuppressLint("LongLogTag")
    private int generateRandomNumber(int endNumber) {
        endNumber=endNumber+10;
        Log.d("==============================$$$$$","endNumber "+endNumber+"");
        Random rand = new Random();
        int startNumber = (endNumber - 10);
        endNumber = (endNumber - 3);
        int number = (int) (startNumber + Math.random() * (endNumber - startNumber + 1));
        Log.d("==============================$$$$$","startNumber "+startNumber+" endNumber "+endNumber+"");

        return number;
    }

    //进度条设置动画
    public void startProgressBar(int progress) {
        Toast.makeText(getContext(), progress + "", Toast.LENGTH_SHORT).show();
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, progress);
        valueAnimator.setDuration(500);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                mDataBinding.includeProgressBar.progressBar.setProgress((int) value);
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (progress == 50) {
                    mDataBinding.giftBox.setVisibility(View.GONE);
                    mDataBinding.giftBoxOpen.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimator.start();

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

        void onJumpAd();

        void onLottery();
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
                        if (reference.get().time > 0) {
                            reference.get().mDataBinding.countdownHint.setText("恭喜！ 已解锁本商品最大中奖率" + "(" + reference.get().time / 1000 + ")");
                            reference.get().time = reference.get().time - 1000;
                            reference.get().automaticJump();
                        } else {
                            reference.get().mOnFinishListener.onFinish();
                        }
                    }
                    break;
                case 2:
                    if (reference.get() != null) {
                        reference.get().mDataBinding.closure.setVisibility(View.VISIBLE);
                    }
                    break;
            }
        }
    }


}