package com.module.lottery.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.donews.base.utils.ToastUtil;
import com.donews.common.router.RouterFragmentPath;
import com.donews.middle.dialog.BaseDialog;
import com.donews.middle.utils.JsonValueListUtils;
import com.donews.network.EasyHttp;
import com.donews.network.cache.model.CacheMode;
import com.donews.network.callback.SimpleCallBack;
import com.donews.network.exception.ApiException;
import com.module.lottery.bean.RecommendBean;
import com.module.lottery.model.LotteryModel;
import com.module.lottery.utils.ClickDoubleUtil;
import com.module.lottery.utils.ImageUtils;
import com.module.lottery.utils.RandomProbability;
import com.module_lottery.R;
import com.module_lottery.databinding.LotteryCritCommodityLayoutBinding;

import java.lang.ref.WeakReference;

import io.reactivex.disposables.Disposable;

public class LotteryCritCommodityDialog extends BaseDialog<LotteryCritCommodityLayoutBinding> implements DialogInterface.OnDismissListener {
    private CritCommodityHandler mCritCommodityHandler = new CritCommodityHandler(this);
    String mGoodsId;
    RecommendBean mRecommendBean;

    public LotteryCritCommodityDialog(String goodsId, Context context) {

        super(context, R.style.dialogTransparent);
        this.mGoodsId = goodsId;
    }

    @Override
    public int setLayout() {
        return R.layout.lottery_crit_commodity_layout;
    }

    @Override
    public float setSize() {
        return 0.85f;
    }
    //获取商品数据


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestNetData();
        initView();
        //延迟一秒出现关闭按钮
        Message message = new Message();
        message.what = 1;
        mCritCommodityHandler.sendMessageDelayed(message, 1000);
        setOnDismissListener(this);
    }


    private void initView() {

        mDataBinding.jsonHand.setImageAssetsFolder("images");
        mDataBinding.jsonHand.setAnimation(JsonValueListUtils.LOTTERY_FINGER);
        mDataBinding.jsonHand.loop(true);
        mDataBinding.jsonHand.playAnimation();
        mDataBinding.closure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mDataBinding.jumpBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ClickDoubleUtil.isFastClick()) {
                    if (mRecommendBean != null) {
//                        ARouter.getInstance()
//                                .build(RouterFragmentPath.Lottery.PAGER_LOTTERY).withString("goods_id", mRecommendBean.getGoodsId()).withString("action", "newAction")
//                                .navigation();
//                        if (isShowing()) {
//                            dismiss();
//                        }
                    }
                }
            }
        });
    }


    private void refreshPage(RecommendBean recommendBean) {
//        ImageUtils.setImage(getOwnerActivity(), mDataBinding.picture, recommendBean.getMainPic(), 1);
//        mDataBinding.title.setText(recommendBean.getTitle() + "");
//        mDataBinding.price.setText("¥ "+recommendBean.getDisplayPrice() + "");
//        mDataBinding.originalPrice.setText("¥ "+recommendBean.getOriginalPrice() + "");
//        mDataBinding.originalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
//        mDataBinding.numberPeople.setText("累计" + recommendBean.getTotalPeople() + "人参与抽奖");
//        mDataBinding.randomNumber.setText(RandomProbability.Companion.getRandomNumber() + "%");
    }

    private void requestNetData() {
        this.unDisposable();
        Disposable disposable = EasyHttp.get(LotteryModel.LOTTERY_CRIT_COMMODITY).params("goods_id", mGoodsId)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<RecommendBean>() {
                    @Override
                    public void onError(ApiException e) {

                        ToastUtil.show(getContext(), e.getMessage());
                    }

                    @Override
                    public void onSuccess(RecommendBean recommendBean) {
                        if (recommendBean != null) {
                            mRecommendBean = recommendBean;
                            refreshPage(recommendBean);
                        }
                    }
                });
        this.addDisposable(disposable);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (mCritCommodityHandler != null) {
            mCritCommodityHandler.removeMessages(0);
            mCritCommodityHandler.removeMessages(1);
            mCritCommodityHandler.removeCallbacksAndMessages(null);
        }
    }


    private static class CritCommodityHandler extends Handler {
        private WeakReference<LotteryCritCommodityDialog> reference;   //

        CritCommodityHandler(LotteryCritCommodityDialog context) {
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
