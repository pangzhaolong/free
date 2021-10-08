package com.dn.sdk.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.dn.sdk.BuildConfig;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.util.Log.DEBUG;
import static android.util.Log.ERROR;
import static android.util.Log.INFO;
import static android.util.Log.VERBOSE;
import static android.util.Log.WARN;

/**
 * @author by SnowDragon
 * Date on 2020/11/23
 * Description:
 */

public class SdkLogUtils {

    private static final boolean GLOBAL_TAG = true;

    public static final String TAG = "sdkLog";
    public static final boolean DEBUGABLE = BuildConfig.DEBUG;

    private static boolean isLoggable(String tag, int level) {
        return DEBUGABLE || Log.isLoggable(tag, level);
    }

    public static void d(String tag, String message) {
        if (isLoggable(tag, DEBUG)) {
            String extraString = getMethodNameAndLineNumber();
            tag = privateTag() ? tag : getTag();
            Log.d(tag, extraString + message, null);
        }
    }

    public static void v(String tag, String message) {
        if (isLoggable(tag, VERBOSE)) {
            String extraString = getMethodNameAndLineNumber();
            tag = privateTag() ? tag : getTag();
            Log.v(tag, extraString + message, null);
        }
    }

    public static void i(String tag, String message) {
        if (isLoggable(tag, INFO)) {
            String extraString = getMethodNameAndLineNumber();
            tag = privateTag() ? tag : getTag();
            Log.i(tag,"-------------------------------");
            Log.i(tag, extraString + message);
            Log.i(tag,"-------------------------------");
        }
    }

    public static void w(String tag, String message) {
        if (isLoggable(tag, WARN)) {
            String extraString = getMethodNameAndLineNumber();
            tag = privateTag() ? tag : getTag();
            Log.w(tag,"-------------------------------");
            Log.w(tag, extraString + message);
            Log.w(tag,"-------------------------------");
        }
    }

    public static void e(String tag, String message) {
        if (isLoggable(tag, ERROR)) {
            String extraString = getMethodNameAndLineNumber();
            tag = privateTag() ? tag : getTag();
            Log.e(tag, extraString + message);
        }
    }

    public static void E(String tag, String message) {
        e(tag, message);
    }

    public static void E(String message) {
        e(TAG, message);
    }

    public static void e(String tag, String message, Throwable e) {
        if (isLoggable(tag, ERROR)) {
            String extraString = getMethodNameAndLineNumber();
            tag = privateTag() ? tag : getTag();
            Log.e(tag, extraString + message, e);
        }
    }
    public static void e(String tag, Throwable throwable) {
        if (isLoggable(tag, ERROR)) {
            String extraString = getMethodNameAndLineNumber();
            tag = privateTag() ? tag : getTag();
            Log.e(tag, "----------------------------------");
            Log.e(tag, extraString + getStackTraceString(throwable));
            Log.e(tag, "----------------------------------");
        }
    }

    public static String getStackTraceString(Throwable tr) {
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

    private static boolean privateTag() {
        return GLOBAL_TAG;
    }

    @SuppressLint("DefaultLocale")
    private static String getMethodNameAndLineNumber() {
        StackTraceElement element[] = Thread.currentThread().getStackTrace();
        if (element != null && element.length >= 4) {
            String methodName = element[4].getMethodName();
            int lineNumber = element[4].getLineNumber();
            return String.format("%s.%s : %d ---> ", getClassName(),
                    methodName, lineNumber, Locale.CHINESE);
        }
        return null;
    }

    private static String getTag() {
        StackTraceElement element[] = Thread.currentThread().getStackTrace();
        if (element != null && element.length >= 4) {
            String className = element[4].getClassName();
            if (className == null) {
                return null;
            }
            int index = className.lastIndexOf(".");
            if (index != -1) {
                className = className.substring(index + 1);
            }
            index = className.indexOf('$');
            if (index != -1) {
                className = className.substring(0, index);
            }
            return className;
        }
        return null;
    }

    private static String getClassName() {
        StackTraceElement element[] = Thread.currentThread().getStackTrace();
        if (element != null && element.length >= 4) {
            String className = element[5].getClassName();
            if (className == null) {
                return null;
            }
            int index = className.lastIndexOf(".");
            if (index != -1) {
                className = className.substring(index + 1);
            }
            index = className.indexOf('$');
            if (index != -1) {
                className = className.substring(0, index);
            }
            return className;
        }
        return null;
    }

    public static void recordOperation(String operation, Context context) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINESE);
        String time = sdf.format(new Date(System.currentTimeMillis())) + " : ";
        try {
            File file = getExternalStorageDir(context, "log.txt");

            if (file != null) {
                FileWriter fp = new FileWriter(file.getAbsoluteFile(), true);
                fp.write(time + operation + "\n");
                fp.close();
            }
        } catch (Exception e) {
            Log.d(TAG, "error : " + e);
        }
    }

    public static File getExternalStorageDir(Context context, String fileName) {
        File extCacheFile = new File(context.getExternalCacheDir(), "log");
        boolean extMk = false;
        if (!extCacheFile.exists()) {
            extMk = extCacheFile.mkdir();
        }

        if (extMk) {
            return createFile(extCacheFile.getAbsolutePath(), fileName);
        }


        File fileDir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        return createFile(fileDir.getAbsolutePath(), fileName);
    }

    /**
     * 创建文件
     *
     * @param path     路径
     * @param fileName 文件名字
     * @return
     */
    private static File createFile(String path, String fileName) {
        File file = new File(path, fileName);
        if (!file.exists()) {
            try {
                boolean createFileResult = file.createNewFile();
                if (createFileResult) {
                    return file;
                } else {
                    return null;
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return file;
    }

}