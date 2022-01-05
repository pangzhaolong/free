package com.donews.front.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.donews.common.router.RouterFragmentPath;
import com.donews.front.BuildConfig;
import com.donews.front.R;
import com.donews.front.databinding.FrontLotteryMore4RpDialogBinding;
import com.donews.front.utils.AnimationUtils;
import com.donews.main.entitys.resps.ExitDialogRecommendGoods;
import com.donews.main.entitys.resps.ExitDialogRecommendGoodsResp;
import com.donews.middle.abswitch.ABSwitch;
import com.donews.middle.dialog.BaseDialog;
import com.donews.network.EasyHttp;
import com.donews.network.cache.model.CacheMode;
import com.donews.network.callback.SimpleCallBack;
import com.donews.network.exception.ApiException;
import com.donews.utilslibrary.utils.HttpConfigUtilsKt;
import com.donews.utilslibrary.utils.UrlUtils;

public class LotteryMore4RpDialog extends BaseDialog<FrontLotteryMore4RpDialogBinding> {

    private ExitDialogRecommendGoods mGoods;

    private int mNeedLotteryCounts = 0;

    public LotteryMore4RpDialog(Context context, Activity activity) {
        super(context, R.style.dialogTransparent);
        setOwnerActivity(activity);
        create();
    }

    public void refreshCounts(int nCounts) {
        mNeedLotteryCounts = nCounts;
        mDataBinding.frontLotteryMore4RpCountsTv.setText(String.valueOf(mNeedLotteryCounts));
    }

    @Override
    public int setLayout() {
        return R.layout.front_lottery_more_4_rp_dialog;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    public float setSize() {
        return 0.85f;
    }

    @SuppressLint("RestrictedApi")
    void initView() {
        mDataBinding.frontLotteryMore4RpCloseIv.setOnClickListener(v -> {
            Activity activity = getOwnerActivity();
            if (activity != null && !activity.isFinishing()) {
                dismiss();
            }
        });
        mDataBinding.frontLotteryMore4RpChange1Tv.setOnClickListener(v -> {
            requestGoodsInfo();
        });

        mDataBinding.frontLotteryMore4RpLotteryTv.setOnClickListener(v -> {
            try {
                ARouter.getInstance()
                        .build(RouterFragmentPath.Lottery.PAGER_LOTTERY)
                        .withString("goods_id", mGoods.getGoodsId())
                        .withBoolean("start_lottery", ABSwitch.Ins().isOpenAutoLottery())
                        .navigation();
            } catch (Exception ignored) {
            }
            Activity activity = getOwnerActivity();
            if (activity != null && !activity.isFinishing()) {
                dismiss();
            }
        });

        mDataBinding.frontLotteryMore4RpLotteryTv.setAnimation(AnimationUtils.setScaleAnimation(1000));

        mDataBinding.frontLotteryMore4RpCountsTv.setText(String.valueOf(mNeedLotteryCounts));
    }

    public void showEx() {
        requestGoodsInfo();
    }

    private void requestGoodsInfo() {
        String url = "";
        url = HttpConfigUtilsKt.withConfigParams(BuildConfig.API_LOTTERY_URL + "v1/recommend-goods-list", true)
                + "&limit=1&first=false";

        EasyHttp.get(url)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<ExitDialogRecommendGoodsResp>() {

                    @Override
                    public void onError(ApiException e) {
                        dismiss();
                    }

                    @Override
                    public void onSuccess(ExitDialogRecommendGoodsResp bean) {
                        showInfo(bean);
                    }
                });
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void showInfo(ExitDialogRecommendGoodsResp bean) {
        if (bean == null || bean.getList().size() <= 0) {
            dismiss();
            return;
        }
        mGoods = bean.getList().get(0);
        if (this.getContext() != null && !getOwnerActivity().isFinishing()) {
            Glide.with(this.getContext())
                    .load(UrlUtils.formatUrlPrefix(mGoods.getMainPic()))
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .addListener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            dismiss();
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            mDataBinding.frontLotteryMore4RpMainSiv.setImageDrawable(resource);
                            show();
                            return false;
                        }
                    }).preload();
        }

        mDataBinding.frontLotteryMore4RpDescTv.setText(mGoods.getTitle());
        mDataBinding.frontLotteryMore4RpCouponPriceTv.setText(String.format("￥%.0f", mGoods.getDisplayPrice()));
        mDataBinding.frontLotteryMore4RpOriginPriceTv.setText(String.format("￥%.0f", mGoods.getOriginalPrice()));
        mDataBinding.frontLotteryMore4RpOriginPriceTv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        if (mGoods.getTotalPeople() > 10000) {
            mDataBinding.frontLotteryMore4RpInfoTv.setText(String.format("累计%.02f万人参与抢购", mGoods.getTotalPeople() / 10000f));
        } else {
            mDataBinding.frontLotteryMore4RpInfoTv.setText(String.format("累计%d人参与抢购", mGoods.getTotalPeople()));
        }
    }
}