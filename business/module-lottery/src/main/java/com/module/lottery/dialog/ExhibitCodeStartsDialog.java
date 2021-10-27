package com.module.lottery.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import com.module.lottery.model.LotteryModel;
import com.module.lottery.ui.BaseParams;
import com.module.lottery.viewModel.ExecuteLotteryViewModel;
import com.module_lottery.R;
import com.module_lottery.databinding.ExhibitCodeDialogLayoutBinding;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.Map;

import retrofit2.converter.gson.GsonConverterFactory;

//展示生成的抽奖码
public class ExhibitCodeStartsDialog extends BaseDialog<ExhibitCodeDialogLayoutBinding> {
    private Context context;
    private OnStateListener mOnFinishListener;
    private LotteryHandler mLotteryHandler = new LotteryHandler(this);
    private BaseLiveDataModel baseLiveDataModel;
    String mGoodsId;

    public ExhibitCodeStartsDialog(Context context, String goodsId) {
        super(context, R.style.dialogTransparent);//内容样式在这里引入
        this.context = context;
        mGoodsId = goodsId;
        baseLiveDataModel = new BaseLiveDataModel();
    }


    @Override
    public int setLayout() {
        return R.layout.exhibit_code_dialog_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Message mes = new Message();
        mes.what = 1;
        mLotteryHandler.sendMessageDelayed(mes, 3000);
        mDataBinding.closure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnFinishListener != null) {
                    mOnFinishListener.onFinish();
                }
            }
        });
        generateLotteryCode();
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
                            Toast.makeText(getContext(),"抽奖码获取失败",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onSuccess(GenerateCodeBean generateCode) {
                            if (generateCode != null) {
                                mDataBinding.setCodeBean(generateCode);
                            }
                        }
                    }));
        }
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
                        //广告跳转
                        reference.get().mOnFinishListener.onJumpAd();
                    }
                    break;
            }
        }
    }


}