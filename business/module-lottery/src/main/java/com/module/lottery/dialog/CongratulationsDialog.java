package com.module.lottery.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.alibaba.android.arouter.launcher.ARouter;
import com.donews.common.router.RouterFragmentPath;
import com.donews.main.entitys.resps.ExitDialogRecommendGoods;
import com.donews.main.entitys.resps.ExitDialogRecommendGoodsResp;
import com.donews.main.entitys.resps.ExitInterceptConfig;
import com.donews.main.utils.ExitInterceptUtils;
import com.donews.network.EasyHttp;
import com.donews.network.cache.model.CacheMode;
import com.donews.network.exception.ApiException;
import com.module.lottery.bean.RecommendBean;
import com.module.lottery.model.LotteryModel;
import com.module.lottery.utils.ClickDoubleUtil;
import com.module.lottery.utils.ImageUtils;
import com.module.lottery.utils.RandomProbability;
import com.module_lottery.R;
import com.module_lottery.databinding.CongratulationsDialogLayoutBinding;
import com.module_lottery.databinding.NoDrawDialogLayoutBinding;

//满足6个恭喜dialog
public class CongratulationsDialog extends BaseDialog<CongratulationsDialogLayoutBinding> {
    private Context mContext;
    int limitNumber = 1;


    public CongratulationsDialog(Context context) {
        super(context, R.style.dialogTransparent);//内容样式在这里引入
        this.mContext = context;
    }

    //保存记录当前展示的商品
    RecommendBean mRecommendBean;


    @Override
    public int setLayout() {
        return R.layout.congratulations_dialog_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestGoodsInfo("");
        mDataBinding.replace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRecommendBean != null && ClickDoubleUtil.isFastClick()) {
                    requestGoodsInfo(mRecommendBean.getGoodsId());
                }
            }
        });

        mDataBinding.jumpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRecommendBean != null) {
                    dismiss();
                    ARouter.getInstance()
                            .build(RouterFragmentPath.Lottery.PAGER_LOTTERY).withString("goods_id", mRecommendBean.getGoodsId()).withString("action", "newAction")
                            .navigation();
                }
            }
        });

        mDataBinding.closure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (mOnFinishListener != null) {
                    mOnFinishListener.onFinish();
                }
            }
        });
    }

    @Override
    public float setSize() {
        return 0.8f;
    }


    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if (mOnFinishListener != null) {
            mOnFinishListener.onFinish();
        }
        return super.onKeyDown(keyCode, event);
    }

    OnFinishListener mOnFinishListener;

    private void requestGoodsInfo(String goods_id) {
        EasyHttp.get(LotteryModel.LOTTERY_RECOMMEND_CODE).cacheMode(CacheMode.NO_CACHE)
                .params("goods_id", goods_id + "").execute(new com.donews.network.callback.SimpleCallBack<RecommendBean>() {
            @Override
            public void onError(ApiException e) {

            }

            @Override
            public void onSuccess(RecommendBean recommendBean) {
                if (mDataBinding != null && recommendBean != null) {
                    mRecommendBean = recommendBean;
                    if (mRecommendBean != null) {
                        ImageUtils.setImage(mContext, mDataBinding.commodity, mRecommendBean.getMainPic(), 5);
                        mDataBinding.price.setText("¥ " + mRecommendBean.getDisplayPrice());
                        mDataBinding.originalPrice.setText("¥ " + mRecommendBean.getOriginalPrice());
                        mDataBinding.titleName.setText(mRecommendBean.getTitle());
                        mDataBinding.randomNumber.setText(RandomProbability.Companion.getRandomNumber() + "%");
                        mDataBinding.originalPrice.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG);

                    }
                }
            }
        });
    }


    public void setFinishListener(OnFinishListener l) {
        mOnFinishListener = l;
    }

    public interface OnFinishListener {
        /**
         * 此时可以关闭Activity了
         */
        void onFinish();
    }
}