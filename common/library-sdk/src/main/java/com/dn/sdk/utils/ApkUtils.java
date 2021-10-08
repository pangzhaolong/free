package com.dn.sdk.utils;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.TextUtils;

import androidx.fragment.app.FragmentActivity;

import com.donews.utilslibrary.base.UtilsConfig;

import java.util.List;

/**
 * @author by SnowDragon
 * Date on 2021/3/30
 * Description:
 */
public class ApkUtils {

    public static boolean isAppInstalled(String packageName) {
        if (packageName == null) {
            return false;
        }
        final PackageManager packageManager = UtilsConfig.getApplication().getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;

                if (packageName.equalsIgnoreCase(pn)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * schema 启动activity
     *
     * @param activity
     * @param deepLink schema
     */
    public static void startAppBySchema(FragmentActivity activity, String deepLink) {
        if (TextUtils.isEmpty(deepLink)) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(deepLink));
        activity.startActivity(intent);

    }

    public static void startAppBySchema(String deepLink) {
        if (TextUtils.isEmpty(deepLink)) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(deepLink));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        UtilsConfig.getApplication().startActivity(intent);

    }

    public static void startAppByPackageName( String packageName) {


        // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
        PackageInfo packageinfo = null;
        try {
            packageinfo = UtilsConfig.getApplication().getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageinfo == null) {
            return;
        }

        // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(packageinfo.packageName);

        // 通过getPackageManager()的queryIntentActivities方法遍历
        List<ResolveInfo> resolveinfoList = UtilsConfig.getApplication().getPackageManager()
                .queryIntentActivities(resolveIntent, 0);

        ResolveInfo resolveinfo = resolveinfoList.iterator().next();
        if (resolveinfo != null) {
            // packagename = 参数packname
            String pkName = resolveinfo.activityInfo.packageName;
            // 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packagename.mainActivityname]
            String className = resolveinfo.activityInfo.name;
            // LAUNCHER Intent
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);

            // 设置ComponentName参数1:packagename参数2:MainActivity路径
            ComponentName cn = new ComponentName(pkName, className);

            intent.setComponent(cn);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            UtilsConfig.getApplication().startActivity(intent);
        }
    }
}
