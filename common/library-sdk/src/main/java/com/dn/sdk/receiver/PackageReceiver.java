package com.dn.sdk.receiver;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.dn.sdk.utils.SdkLogUtils;
import com.donews.common.utils.ThreadUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * @author by SnowDragon
 * Date on 2021/3/31
 * Description:
 */
public class PackageReceiver extends BroadcastReceiver {
    public static final String RECEIVER_AD_CLICK = "receiver_ad_click";

    public static HashMap<String, InstallListener> installHashMap = new HashMap<>();
    public static HashMap<String, String> installPackageHashMap = new HashMap<>();

    public List<String> pkgList = new ArrayList<>();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) {
            return;
        }

        if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {
            String packageName = intent.getData().getSchemeSpecificPart();

            String path = installPackageHashMap.get(packageName);
            //删除已经安装的文件
            if (!TextUtils.isEmpty(path)) {
                File file = new File(path);
                if (file.exists()) {
                    file.delete();
                }
            }


            InstallListener listener = installHashMap.get(RECEIVER_AD_CLICK);
            if (listener != null) {
                listener.installComplete(packageName);
                pkgList.add(packageName);
                startObserver();
            }


        } else if (intent.getAction().equals(Intent.ACTION_PACKAGE_REPLACED)) {
            //String packageName = intent.getData().getSchemeSpecificPart();

        } else if (intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)) {
            //String packageName = intent.getData().getSchemeSpecificPart();

        }

    }

    public static final long OBSERVER_TIME = 50 * 1000;

    @SuppressLint("SdCardPath")
    private void startObserver() {
        ThreadUtils.getExecutor().execute(() -> {
            long startTime = System.currentTimeMillis();
            SdkLogUtils.i(SdkLogUtils.TAG, " --- startObserver: ");

            while (true) {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (System.currentTimeMillis() - startTime > OBSERVER_TIME) {
                    break;
                }
                try {
                    Iterator<String> iterator = pkgList.iterator();
                    //循环遍历列表中的数据
                    while (iterator.hasNext()) {
                        String pkName = iterator.next();
                        File file = new File("/mnt/sdcard/Android/data/" + pkName);
                        SdkLogUtils.i(SdkLogUtils.TAG, " file isExistance: " + file.exists());


                        if (file.exists()) {
                            InstallListener listener = installHashMap.get(RECEIVER_AD_CLICK);
                            if (listener != null) {
                                listener.activateComplete(pkName);
                            }
                            //回调成功之后，删除监听 ，注意此处，千万别写成pkgList.remove(pkName)
                            iterator.remove();
                        }
                    }
                } catch (Exception e) {

                }


                //如果列表大小为0，中断执行
                if (pkgList.size() == 0) {
                    break;
                }
            }
        });


    }

    public interface InstallListener {
        /**
         * 安装完成
         *
         * @param packageName 包名
         */
        void installComplete(String packageName);

        /**
         * 激活完成
         *
         * @param packageName 包名
         */
        void activateComplete(String packageName);

    }
}
