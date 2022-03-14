package com.donews.main.dialog;

import static com.donews.middle.utils.CommonUtils.LOTTERY_FINGER;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.dn.drouter.ARouteHelper;
import com.dn.events.events.LoginLodingStartStatus;
import com.dn.events.events.LoginUserStatus;
import com.donews.common.router.RouterActivityPath;
import com.donews.common.router.RouterFragmentPath;
import com.donews.main.BuildConfig;
import com.donews.main.R;
import com.donews.main.databinding.MainEnterDialogLotteryBindingImpl;
import com.donews.main.entitys.resps.ExitDialogRecommendGoods;
import com.donews.main.entitys.resps.ExitDialogRecommendGoodsResp;
import com.donews.middle.abswitch.OtherSwitch;
import com.donews.network.EasyHttp;
import com.donews.network.cache.model.CacheMode;
import com.donews.network.callback.SimpleCallBack;
import com.donews.network.exception.ApiException;
import com.donews.utilslibrary.analysis.AnalysisUtils;
import com.donews.utilslibrary.dot.Dot;
import com.donews.utilslibrary.utils.AppInfo;
import com.donews.utilslibrary.utils.HttpConfigUtilsKt;
import com.donews.utilslibrary.utils.KeySharePreferences;
import com.donews.utilslibrary.utils.SPUtils;
import com.donews.utilslibrary.utils.UrlUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Random;

public class EnterShowDialog extends BaseDialog<MainEnterDialogLotteryBindingImpl> {

    private Context mContext;

    private ExitDialogRecommendGoods mGoods;

    private boolean mSelectedTips = false;

    private boolean mChangeOne = false;
    private boolean isSendCloseEvent = true;

    private final String LOGIN_TAG = "EnterShowDialog";

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

        EventBus.getDefault().register(this);

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
            isSendCloseEvent = false;
            if (AppInfo.checkIsWXLogin()) {
                AnalysisUtils.onEventEx(mContext, Dot.But_Hme_Recommend_Login_Next);
            } else {
                AnalysisUtils.onEventEx(mContext, Dot.But_Hme_Recommend_Not_Login_Next);
            }
            mDataBinding.tvProbability1.setText(randLucky());
            mChangeOne = true;
            requestGoodsInfo(false);
        });

        mDataBinding.btnLottery.setOnClickListener(v -> {
            isSendCloseEvent = false;
            if (AppInfo.checkIsWXLogin()) {
                AnalysisUtils.onEventEx(mContext, Dot.But_Hme_Recommend_Login_Snap);

                ARouter.getInstance()
                        .build(RouterFragmentPath.Lottery.PAGER_LOTTERY)
                        .withString("goods_id", mGoods.getGoodsId())
                        .withBoolean("start_lottery", OtherSwitch.Ins().isOpenAutoLottery())
                        .navigation();

                dismiss();
            } else {
                AnalysisUtils.onEventEx(mContext, Dot.But_Hme_Recommend_Not_Login_Snap);

                RouterActivityPath.LoginProvider.getLoginProvider()
                        .loginWX(LOGIN_TAG, "首页>热门商品推荐>未登录弹窗");
            }
        });

        mDataBinding.tvProbability1.setText(randLucky());

        if (SPUtils.getInformain(KeySharePreferences.SHOW_DIALOG_WHEN_LAUNCH, true)) {
            mDataBinding.mainEnterClickLl.setVisibility(View.VISIBLE);
            mDataBinding.mainEnterClick.setBackgroundResource(R.drawable.main_enter_radio_bg_unselect);
        } else {
            mDataBinding.mainEnterClickLl.setVisibility(View.INVISIBLE);
        }

        mDataBinding.mainEnterNoTipsToday.setOnClickListener(v -> {
            mSelectedTips = !mSelectedTips;
            if (mSelectedTips) {
                mDataBinding.mainEnterClick.setBackgroundResource(R.drawable.main_enter_radio_bg_select);
                SPUtils.setInformain(KeySharePreferences.SHOW_DIALOG_WHEN_LAUNCH, false);
            } else {
                mDataBinding.mainEnterClick.setBackgroundResource(R.drawable.main_enter_radio_bg_unselect);
                SPUtils.setInformain(KeySharePreferences.SHOW_DIALOG_WHEN_LAUNCH, true);
            }
        });

        initLottie(mDataBinding.mainEnterDialogLottie, LOTTERY_FINGER);
        setOnDismissListener(dialog -> {
            if (isSendCloseEvent) {
                if (AppInfo.checkIsWXLogin()) {
                    AnalysisUtils.onEventEx(mContext, Dot.But_Hme_Recommend_Login_Close);
                } else {
                    AnalysisUtils.onEventEx(mContext, Dot.But_Hme_Recommend_Not_Login_Close);
                }
            }
        });

        if (AppInfo.checkIsWXLogin()) {
            mDataBinding.mainEnterNoTipsToday.setVisibility(View.VISIBLE);
            mDataBinding.mainEnterAgreeProtocol.setVisibility(View.GONE);
            mDataBinding.btnLottery.setText(R.string.main_exit_lottery);
        } else {
            mDataBinding.mainEnterNoTipsToday.setVisibility(View.GONE);
            mDataBinding.mainEnterAgreeProtocol.setVisibility(View.VISIBLE);
            boolean protocol = getSharedPreferences().getBoolean("Free", false) ||
                    OtherSwitch.Ins().isOpenAutoAgreeProtocol();
            mDataBinding.mainEnterCheckBox.setChecked(protocol);
            mDataBinding.btnLottery.setText("登录抢购");
        }

        mDataBinding.mainEnterUserProtocol.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("url", BuildConfig.USER_PROTOCOL);
            bundle.putString("title", "用户协议");
            ARouteHelper.routeSkip(RouterActivityPath.Web.PAGER_WEB_ACTIVITY, bundle);
        });
        mDataBinding.mainEnterPrivacyProtocol.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("url", BuildConfig.PRIVATE_POLICY_URL);
            bundle.putString("title", "隐私政策");
            ARouteHelper.routeSkip(RouterActivityPath.Web.PAGER_WEB_ACTIVITY, bundle);
        });
        mDataBinding.mainEnterAgreeProtocol.setOnClickListener(v -> {
            if (AppInfo.checkIsWXLogin()) {
                mDataBinding.mainEnterCheckBox.setChecked(!mDataBinding.mainEnterCheckBox.isChecked());
                getSharedPreferences().getBoolean("Free", mDataBinding.mainEnterCheckBox.isChecked());
            }
        });
    }

    public void showEx() {
        requestGoodsInfo(true);
    }

    public void dismissEx() {
        dismiss();
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
        String url = "";
        if (mChangeOne) {
            url = HttpConfigUtilsKt.withConfigParams(BuildConfig.API_LOTTERY_URL + "v1/recommend-goods-list", true)
                    + "&limit=1&first=false";
        } else {
            if (SPUtils.getInformain(KeySharePreferences.IS_FIRST_IN_APP, 0) == 1) {
                url = HttpConfigUtilsKt.withConfigParams(BuildConfig.API_LOTTERY_URL + "v1/recommend-goods-list", true)
                        + "&limit=1&first=true";
            } else {
                url = HttpConfigUtilsKt.withConfigParams(BuildConfig.API_LOTTERY_URL + "v1/recommend-goods-list", true)
                        + "&limit=1&first=false";
            }
        }
        EasyHttp.get(url)
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
        if (mContext != null && mContext instanceof Activity && !((Activity) mContext).isDestroyed()) {
            RoundedCorners roundedCorners = new RoundedCorners(25);
            RequestOptions requestOptions = RequestOptions.bitmapTransform(roundedCorners);
            Glide.with(mContext)
                    .load(UrlUtils.formatUrlPrefix(mGoods.getMainPic()))
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .apply(requestOptions)
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
        }
//        Glide.with(mContext).load(UrlUtils.formatUrlPrefix(mGoods.getMainPic())).into(mDataBinding.ivGoodsPic);
        mDataBinding.tvGoodsTitle.setText(mGoods.getTitle());
        mDataBinding.tvActualPrice.setText(String.format("￥%d", OtherSwitch.Ins().getLotteryPriceShow()));
//        mDataBinding.tvActualPrice.setText(String.format("￥%.0f", mGoods.getDisplayPrice()));
        mDataBinding.tvOriginalPrice.setText("￥" + mGoods.getOriginalPrice());
        mDataBinding.tvOriginalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        if (mGoods.getTotalPeople() > 10000) {
            mDataBinding.tvBuyNumber.setText("累计" + mGoods.getTotalPeople() / 10000 + "万人参与抢购");
        } else {
            mDataBinding.tvBuyNumber.setText("累计" + mGoods.getTotalPeople() + "人参与抢购");
        }
    }

    private boolean mIsSelf = false;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loginStatusEvent(LoginLodingStartStatus event) {
        if (!event.getTag().equalsIgnoreCase(LOGIN_TAG)) {
            return;
        }
        mIsSelf = true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loginUserStatus(LoginUserStatus userStatus) {
        if (userStatus.getStatus() == 1) {
            if (!mIsSelf || mGoods == null) {
                return;
            }

            try {
                ARouter.getInstance()
                        .build(RouterFragmentPath.Lottery.PAGER_LOTTERY)
                        .withString("goods_id", mGoods.getGoodsId())
                        .withBoolean("start_lottery", OtherSwitch.Ins().isOpenAutoLottery())
                        .navigation();
            } catch (Exception ignored) {
            }
            dismiss();
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        EventBus.getDefault().unregister(this);
    }
}