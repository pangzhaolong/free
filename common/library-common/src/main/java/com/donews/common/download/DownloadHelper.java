package com.donews.common.download;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.content.FileProvider;

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

    public static void installApp(Context context, String path) {
        File file = new File(path);
        try {
            if (file.exists()) {
                Intent intent = new Intent(Intent.ACTION_VIEW);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    Uri contentUri = FileProvider.getUriForFile(context, context.getPackageName() + PROVIDER_AUTHORITY, file);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
                } else {
                    intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                context.startActivity(intent);
            }
        } catch (Exception e) {
            Log.e("DownloadHelper", "  DownloadHelper install error: " + getStackTraceString(e));
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
