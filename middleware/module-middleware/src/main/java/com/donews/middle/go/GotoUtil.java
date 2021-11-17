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
}
