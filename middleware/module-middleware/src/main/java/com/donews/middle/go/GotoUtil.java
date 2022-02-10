package com.donews.middle.go;

import static com.donews.utilslibrary.utils.KeySharePreferences.CURRENT_SCORE_TASK_COUNT;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.alibaba.android.arouter.launcher.ARouter;
import com.dn.drouter.ARouteHelper;
import com.dn.sdk.bean.integral.ProxyIntegral;
import com.dn.sdk.utils.IntegralComponent;
import com.donews.base.BuildConfig;
import com.donews.base.utils.ToastUtil;
import com.donews.common.contract.BaseCustomViewModel;
import com.donews.common.router.RouterActivityPath;
import com.donews.common.router.RouterFragmentPath;
import com.donews.middle.abswitch.ABSwitch;
import com.donews.middle.api.MiddleApi;
import com.donews.middle.bean.TaskActionBean;
import com.donews.middle.bean.home.PrivilegeLinkBean;
import com.donews.network.EasyHttp;
import com.donews.network.cache.model.CacheMode;
import com.donews.network.callback.SimpleCallBack;
import com.donews.network.exception.ApiException;
import com.donews.utilslibrary.analysis.AnalysisUtils;
import com.donews.utilslibrary.dot.Dot;
import com.donews.utilslibrary.utils.SPUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class GotoUtil {

    private static final String PACKAGE_TB = "com.taobao.taobao";
    private static final String PACKAGE_PDD = "com.xunmeng.pinduoduo";
    private static final String PACKAGE_JD = "com.jingdong.app.mall";
    private static final String PACKAGE_ELM = "me.ele";

    /**
     * 获取商品转链
     *
     * @return
     */
    @SuppressLint("DefaultLocale")
    public static void requestPrivilegeLinkBean(Context context, String goodsId, String materialId, String searchId, int src) {
        EasyHttp.get(String.format(MiddleApi.privilegeLinkUrl, goodsId, materialId, searchId, src))
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<PrivilegeLinkBean>() {
                    @Override
                    public void onError(ApiException e) {

                    }

                    @Override
                    public void onSuccess(PrivilegeLinkBean privilegeLinkBean) {
                        if (privilegeLinkBean == null) {
                            return;
                        }

                        gotoApp(context, privilegeLinkBean, src);
                    }
                });
    }

    private static boolean checkAppInstall(Context context, String packageName) {
        try {
            context.getPackageManager().getPackageInfo(packageName, 0);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static void gotoApp(Context context, PrivilegeLinkBean privilegeLinkBean, int src) {
        if (src == 1) {
            if (checkAppInstall(context, PACKAGE_TB)) {
                String url = "taobao://" + privilegeLinkBean.getUrl().split("//")[1];
                Uri uri = Uri.parse(url); // 商品地址
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
            } else {
                String url = privilegeLinkBean.getUrl();
                Uri uri = Uri.parse(url); // 商品地址
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
            }
        } else if (src == 2) {
            if (checkAppInstall(context, PACKAGE_PDD)) {
                String url = "pinduoduo://" + privilegeLinkBean.getUrl().split("//")[1];
                Uri uri = Uri.parse(url); // 商品地址
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
            } else {
                String url = privilegeLinkBean.getUrl();
                Uri uri = Uri.parse(url); // 商品地址
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
            }
        } else if (src == 3) {
            if (checkAppInstall(context, PACKAGE_JD)) {
                String url = "openapp.jdmobile://" + privilegeLinkBean.getUrl().split("//")[1];
                Uri uri = Uri.parse(url); // 商品地址
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
            } else {
                String url = privilegeLinkBean.getUrl();
                Uri uri = Uri.parse(url); // 商品地址
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
            }
        }
    }

    public static void gotoElem(Context context, String urlPath) {
        if (checkAppInstall(context, PACKAGE_ELM)) {
            String url = "https://" + urlPath.split("//")[1];
            Uri uri = Uri.parse(url); // 商品地址
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(intent);
        } else {
            Uri uri = Uri.parse(urlPath); // 商品地址
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(intent);
        }
    }

    public static void doAction(Context context, String action, String title) {
        if (action == null || action.equalsIgnoreCase("")) {
            return;
        }

        if (action.startsWith("http")) {
            Bundle bundle = new Bundle();
            bundle.putString("url", action);
            bundle.putString("title", title);
            ARouteHelper.routeSkip(RouterActivityPath.Web.PAGER_WEB_ACTIVITY, bundle);
        } else {
            String[] acts = action.split("\\|");
            if (acts.length < 1) {
                return;
            }
            switch (acts[0]) {
                case TaskActionBean.WINNER:
                case TaskActionBean.SHOW:
                    EventBus.getDefault().post(new TaskActionBean(acts[0], ""));
                    break;
                case TaskActionBean.LOTTERY:
                    //抽奖页
                    if (acts.length >= 2) {
                        ARouter.getInstance()
                                .build(RouterFragmentPath.Lottery.PAGER_LOTTERY)
                                .withString("goods_id", acts[1])
                                .navigation();
                    }
                    break;
                case TaskActionBean.INTEGRAL:
                    // 积分墙
                    checkIntegralTask(context);
                    break;
                case TaskActionBean.MONEY:
                    //提现页
                    ARouter.getInstance()
                            .build(RouterActivityPath.Mine.PAGER_ACTIVITY_WITHDRAWAL)
                            .navigation();
                    break;
            }
        }
    }

    private static void getIntegralTask(Context context) {
        if (SPUtils.getInformain(CURRENT_SCORE_TASK_COUNT, 0) >= ABSwitch.Ins().getOpenScoreTaskMax()) {
            ARouter.getInstance()
                    .build(RouterFragmentPath.Integral.PAGER_INTEGRAL_NOT_TASK)
                    .navigation();
            return;
        }

        IntegralComponent.getInstance().getIntegralList(new IntegralComponent.IntegralHttpCallBack() {
            @Override
            public void onSuccess(List<ProxyIntegral> var1) {
                if (var1 != null && var1.size() > 0) {
                    // 积分墙
                    ARouter.getInstance()
                            .build(RouterFragmentPath.Integral.PAGER_INTEGRAL)
                            .navigation();
                }
            }

            @Override
            public void onError(String var1) {

            }

            @Override
            public void onNoTask() {
                ARouter.getInstance()
                        .build(RouterFragmentPath.Integral.PAGER_INTEGRAL_NOT_TASK)
                        .navigation();
//                ToastUtil.showShort(context, "暂无积分任务");
            }
        });
    }

    private static void checkIntegralTask(Context context) {
        if (!ABSwitch.Ins().isOpenScoreTask()) {
//            ARouter.getInstance()
//                    .build(RouterFragmentPath.Integral.PAGER_INTEGRAL_NOT_TASK)
//                    .navigation();
//            ToastUtil.showShort(context, "暂无积分任务");
            return;
        }
        EasyHttp.get(BuildConfig.API_INTEGRAL_URL + "v1/active-task-times")
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<ActiveTaskBean>() {
                    @Override
                    public void onError(ApiException e) {
                        getIntegralTask(context);
                    }

                    @Override
                    public void onSuccess(ActiveTaskBean queryBean) {
                        if (queryBean != null) {
                            SPUtils.setInformain(CURRENT_SCORE_TASK_COUNT, queryBean.times);
                        }
                        getIntegralTask(context);
                    }
                });
    }

    /**
     *
     */
    public static class ActiveTaskBean extends BaseCustomViewModel {

        public int times;
    }
}
