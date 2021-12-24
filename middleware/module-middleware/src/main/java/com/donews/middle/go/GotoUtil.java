package com.donews.middle.go;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.donews.middle.api.MiddleApi;
import com.donews.middle.bean.home.PrivilegeLinkBean;
import com.donews.network.EasyHttp;
import com.donews.network.cache.model.CacheMode;
import com.donews.network.callback.SimpleCallBack;
import com.donews.network.exception.ApiException;

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

    public static void doAction(Context context, String action) {

    }

}
