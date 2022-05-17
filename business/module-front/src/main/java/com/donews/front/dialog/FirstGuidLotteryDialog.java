package com.donews.front.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.donews.base.utils.ToastUtil;
import com.donews.common.router.RouterFragmentPath;
import com.donews.front.BuildConfig;
import com.donews.front.R;
import com.donews.front.databinding.FrontFirstGuidLotteryDialogBinding;
import com.donews.main.entitys.resps.ExitDialogRecommendGoods;
import com.donews.main.entitys.resps.ExitDialogRecommendGoodsResp;
import com.donews.middle.centralDeploy.ABSwitch;
import com.donews.middle.bean.rp.PreRpBean;
import com.donews.middle.dialog.BaseDialog;
import com.donews.network.EasyHttp;
import com.donews.network.cache.model.CacheMode;
import com.donews.network.callback.SimpleCallBack;
import com.donews.network.exception.ApiException;
import com.donews.utilslibrary.utils.HttpConfigUtilsKt;
import com.donews.utilslibrary.utils.KeySharePreferences;
import com.donews.utilslibrary.utils.SPUtils;
import com.donews.utilslibrary.utils.UrlUtils;

public class FirstGuidLotteryDialog extends BaseDialog<FrontFirstGuidLotteryDialogBinding> {

    private ExitDialogRecommendGoods mGoods;

    public FirstGuidLotteryDialog(Context context, Activity activity) {
        super(context, R.style.dialogTransparent);
        setOwnerActivity(activity);
        create();
    }

    @Override
    public int setLayout() {
        return R.layout.front_first_guid_lottery_dialog;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    public float setSize() {
        return 1f;
    }

    @SuppressLint("RestrictedApi")
    void initView() {
        mDataBinding.frontFirstGuidLotteryCloseIv.setOnClickListener(v -> {
            Activity activity = getOwnerActivity();
            if (activity != null && !activity.isFinishing()) {
                dismiss();
            }
        });

        mDataBinding.frontFirstGuidLotteryBtnTv.setOnClickListener(v -> {
            try {
                ARouter.getInstance()
                        .build(RouterFragmentPath.Lottery.PAGER_LOTTERY)
                        .withString("goods_id", mGoods.getGoodsId())
                        .withBoolean("start_lottery", ABSwitch.Ins().isOpenAutoLottery())
                        .withBoolean("privilege", true)
                        .navigation();
            } catch (Exception ignored) {
            }
            Activity activity = getOwnerActivity();
            if (activity != null && !activity.isFinishing()) {
                dismiss();
            }
        });
    }

    public void showEx() {
        requestPreRp();
//        requestGoodsInfo();
    }

    private void requestPreRp() {
        EasyHttp.post(HttpConfigUtilsKt.withConfigParams(BuildConfig.API_WALLET_URL + "v1/pre-red-packet", true))
                .cacheMode(CacheMode.NO_CACHE)
                .upJson("{}")
                .execute(new SimpleCallBack<PreRpBean>() {
                    @Override
                    public void onError(ApiException e) {

                    }

                    @SuppressLint("DefaultLocale")
                    @Override
                    public void onSuccess(PreRpBean bean) {
                        if (bean == null) {
                            mDataBinding.frontFirstLotteryMoneyTv.setText("");
                            ToastUtil.showShort(getContext(), "获取红包失败，请重试");
                            dismiss();
                        } else {
                            mDataBinding.frontFirstLotteryMoneyTv.setText(String.format("%.2f元", bean.getPre_score()));
                            SPUtils.setInformain(KeySharePreferences.FIRST_RP_OPEN_PRE_ID, bean.getPre_id());
                            SPUtils.setInformain(KeySharePreferences.FIRST_RP_OPEN_PRE_SCORE, bean.getPre_score());
                            requestGoodsInfo();
                        }
                    }
                });
    }

    private void requestGoodsInfo() {
        String url = HttpConfigUtilsKt.withConfigParams(BuildConfig.API_LOTTERY_URL + "v1/recommend-goods-list", true)
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
                            mDataBinding.frontFirstGuidLotteryMainSiv.setImageDrawable(resource);
                            show();
                            return false;
                        }
                    }).preload();
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

}