package com.donews.alive;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
 * Date on 2021/1/6
 * Description:
 */

public class KeepAliveLogger {

    private static final boolean GLOBAL_TAG = true;

    public static final String TAG = "LiveLogger";
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
            Log.i(tag, extraString + message);
        }
    }

    public static void w(String tag, String message) {
        if (isLoggable(tag, WARN)) {
            String extraString = getMethodNameAndLineNumber();
            tag = privateTag() ? tag : getTag();
            Log.w(tag, extraString + message);
        }
    }

    public static void e(String tag, String message) {
        if (isLoggable(tag, ERROR)) {
            String extraString = getMethodNameAndLineNumber();
            tag = privateTag() ? tag : getTag();
            Log.e(tag, extraString + message);
        }
    }

    public static void e(String tag, String message, Throwable e) {
        if (isLoggable(tag, ERROR)) {
            String extraString = getMethodNameAndLineNumber();
            tag = privateTag() ? tag : getTag();
            Log.e(tag, extraString + message, e);
        }
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
            KeepAliveLogger.d(TAG, "error : " + e);
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