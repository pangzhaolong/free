package com.donews.main.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.airbnb.lottie.LottieAnimationView;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.donews.common.router.RouterFragmentPath;
import com.donews.main.BuildConfig;
import com.donews.main.R;
import com.donews.main.databinding.MainEnterDialogLotteryBindingImpl;
import com.donews.main.entitys.resps.ExitDialogRecommendGoods;
import com.donews.main.entitys.resps.ExitDialogRecommendGoodsResp;
import com.donews.middle.abswitch.ABSwitch;
import com.donews.network.EasyHttp;
import com.donews.network.cache.model.CacheMode;
import com.donews.network.callback.SimpleCallBack;
import com.donews.network.exception.ApiException;
import com.donews.utilslibrary.utils.KeySharePreferences;
import com.donews.utilslibrary.utils.SPUtils;
import com.donews.utilslibrary.utils.UrlUtils;

import java.util.Random;

public class EnterShowDialog extends BaseDialog<MainEnterDialogLotteryBindingImpl> {

    private Context mContext;

    private ExitDialogRecommendGoods mGoods;

    private boolean mIsNoTipsShow = false;

    public EnterShowDialog(Context context) {
        super(context, R.style.dialogTransparent);
        mContext = context;
        create();
    }

    @Override
    public int setLayout() {
        return R.layout.main_enter_dialog_lottery;
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

    @SuppressLint({"RestrictedApi", "SetTextI18n", "DefaultLocale"})
    void initView() {
        mDataBinding.ivClose.setOnClickListener(v -> dismiss());
        mDataBinding.btnNext.setOnClickListener(v -> {
            mDataBinding.tvProbability1.setText(randLucky());
            requestGoodsInfo(false);
        });

        mDataBinding.btnLottery.setOnClickListener(v -> {

            ARouter.getInstance()
                    .build(RouterFragmentPath.Lottery.PAGER_LOTTERY)
                    .withString("goods_id", mGoods.getGoodsId())
                    .withBoolean("start_lottery", ABSwitch.Ins().isOpenAutoLottery())
                    .navigation();
            dismiss();
        });

        mDataBinding.tvProbability1.setText(randLucky());

        if (!SPUtils.getInformain(KeySharePreferences.SHOW_DIALOG_WHEN_LAUNCH, true)) {
            mDataBinding.mainEnterClickLl.setVisibility(View.INVISIBLE);
        } else {
            mDataBinding.mainEnterClickLl.setVisibility(View.VISIBLE);
        }

        mDataBinding.mainEnterClickLl.setOnClickListener(v -> {
            mIsNoTipsShow = !mIsNoTipsShow;
            if (mIsNoTipsShow) {
                mDataBinding.mainEnterClick.setBackgroundResource(R.drawable.main_enter_radio_bg_select);
                SPUtils.setInformain(KeySharePreferences.SHOW_DIALOG_WHEN_LAUNCH, false);
            } else {
                mDataBinding.mainEnterClick.setBackgroundResource(R.drawable.main_enter_radio_bg_unselect);
                SPUtils.setInformain(KeySharePreferences.SHOW_DIALOG_WHEN_LAUNCH, true);
            }
        });

        initLottie(mDataBinding.mainEnterDialogLottie, "lottery_finger.json");

//        requestGoodsInfo();
    }

    public void showEx() {
        requestGoodsInfo(true);
    }

    @SuppressLint("DefaultLocale")
    private String randLucky() {
        float rand = 80 + 20 * (new Random().nextFloat());
        if (rand >= 99.9f) {
            rand = 99.7f;
        }
        return String.format("%.1f", rand) + "%";
    }

    private void initLottie(LottieAnimationView view, String json) {
        if (view != null && !view.isAnimating()) {
            view.setImageAssetsFolder("images");
            view.setAnimation(json);
            view.loop(true);
            view.playAnimation();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    private void requestGoodsInfo(boolean isFirstIn) {
        EasyHttp.get(BuildConfig.API_LOTTERY_URL + "v1/recommend-goods-list?limit=1&first"+SPUtils.getInformain(KeySharePreferences.IS_FIRST_IN_APP, "true"))
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<ExitDialogRecommendGoodsResp>() {

                    @Override
                    public void onError(ApiException e) {
                        if (isFirstIn) {
                            dismiss();
                        }
                    }

                    @Override
                    public void onSuccess(ExitDialogRecommendGoodsResp bean) {
                        showInfo(bean, isFirstIn);
                    }
                });
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void showInfo(ExitDialogRecommendGoodsResp bean, boolean isFirstIn) {
        if (bean == null || bean.getList().size() <= 0) {
            dismiss();
            return;
        }
        mGoods = bean.getList().get(0);
        Glide.with(mContext)
                .load(UrlUtils.formatUrlPrefix(mGoods.getMainPic()))
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .addListener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        if (isFirstIn) {
                            dismiss();
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        mDataBinding.ivGoodsPic.setImageDrawable(resource);
                        show();
                        return false;
                    }
                }).preload();
//        Glide.with(mContext).load(UrlUtils.formatUrlPrefix(mGoods.getMainPic())).into(mDataBinding.ivGoodsPic);
        mDataBinding.tvGoodsTitle.setText(mGoods.getTitle());
        mDataBinding.tvActualPrice.setText(String.format("￥%.0f", mGoods.getDisplayPrice()));
        mDataBinding.tvOriginalPrice.setText("￥" + mGoods.getOriginalPrice());
        mDataBinding.tvOriginalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        if (mGoods.getTotalPeople() > 10000) {
            mDataBinding.tvBuyNumber.setText("累计" + mGoods.getTotalPeople() / 10000 + "万人参与抢购");
        } else {
            mDataBinding.tvBuyNumber.setText("累计" + mGoods.getTotalPeople() + "人参与抢购");
        }

        SPUtils.setInformain(KeySharePreferences.IS_FIRST_IN_APP, "false");
    }
}