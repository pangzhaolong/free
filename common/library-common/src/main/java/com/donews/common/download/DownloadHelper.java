package com.donews.common.download;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.content.FileProvider;

import com.donews.base.base.BaseApplication;
import com.donews.utilslibrary.utils.AppStatusUtils;
import com.orhanobut.logger.Logger;
import com.tencent.mmkv.MMKV;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;

/**
 * @author by SnowDragon
 * Date on 2020/11/27
 * Description:
 */
public class DownloadHelper {
    public static final String PROVIDER_AUTHORITY = ".DNFileProvider";

    public static final String KEY_INSTALL_APK = "installApk";
    public static final String KEY_APK_PATH = "apkPath";

    public static void installApp(Context context, String path) {

        if (AppStatusUtils.isForceGround()) {
            File file = new File(path);
            try {
                if (file.exists()) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Uri uri;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        uri = FileProvider.getUriForFile(context, context.getPackageName() + PROVIDER_AUTHORITY,
                                file);
                    } else {
                        uri = Uri.fromFile(file);
                    }
                    intent.setDataAndType(uri, "application/vnd.android.package-archive");
                    context.startActivity(intent);
                    clearInstallApp(context, path);
                }
            } catch (Exception e) {
                Log.e("DownloadHelper", "  DownloadHelper install error: " + getStackTraceString(e));
            }
        } else {
            saveInstallApp(context, path);
        }
    }

    private static void saveInstallApp(Context context, String path) {
        MMKV mmkv = MMKV.defaultMMKV();
        if (mmkv != null) {
            mmkv.encode(KEY_INSTALL_APK, true);
            mmkv.encode(KEY_APK_PATH, path);
        }
    }

    private static void clearInstallApp(Context context, String path) {
        MMKV mmkv = MMKV.defaultMMKV();
        if (mmkv != null) {
            mmkv.encode(KEY_INSTALL_APK, false);
            mmkv.encode(KEY_APK_PATH, "");
        }
    }

    public static void installApp() {
        MMKV mmkv = MMKV.defaultMMKV();
        if (mmkv != null) {
            boolean installApk = mmkv.decodeBool(KEY_INSTALL_APK, false);
            if (installApk) {
                String path = mmkv.decodeString(KEY_APK_PATH);
                installApp(BaseApplication.getInstance().getApplicationContext(), path);
            }
        }
    }


    protected static String getStackTraceString(Throwable tr) {
        if (tr == null) {
            return "";
        }

        Throwable t = tr;
        while (t != null) {
            if (t instanceof UnknownHostException) {
                return "";
            }
            t = t.getCause();
        }

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        tr.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }


}
