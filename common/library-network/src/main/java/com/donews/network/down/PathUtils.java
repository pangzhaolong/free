package com.donews.network.down;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * @author by SnowDragon
 * Date on 2020/11/26
 * Description:
 */
public class PathUtils {

    public static final String TAG = "PathUtils";

    /**
     * 获取下载地址的后缀名
     *
     * @param url 下载地址
     * @return 后缀名
     */
    public static String getFileExtensionFromUrl(String url) {

        int fileNamePos = url.lastIndexOf('/');
        String filename = 0 <= fileNamePos ? url.substring(fileNamePos + 1) : url;

        // if the filename contains special characters, we don't
        // consider it valid for our matching purposes:
        if (!filename.isEmpty() &&
                Pattern.matches("[a-zA-Z_0-9\\.\\-\\(\\)\\%]+", filename)) {
            int dotPos = filename.lastIndexOf('.');
            if (0 <= dotPos) {
                return filename.substring(dotPos);
            }
        }

        return "";
    }

    public static File getExternalStorageDir(Context context, String fileName) {
        File fileDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        return createFile(fileDir.getAbsolutePath(), fileName);
    }

    /**
     * 默认路径下的文件
     *
     * @param context  context
     * @param fileName fileName
     * @return
     */
    private static File getDefaultFile(Context context, String fileName) {

        File file = null;
        file = getExternalStorageDir(context, fileName);
        if (file != null) {
            return file;
        }
        //系统缓存路径
        return createFile(context.getCacheDir().getAbsolutePath(), fileName);
    }


    /**
     * 获取指定路径下的文件，如果没有，新创建一个
     *
     * @param context  context
     * @param path     路径
     * @param fileName 文件名
     * @return
     */
    public static File getPathFile(Context context, String path, String fileName) {
        if (TextUtils.isEmpty(path)) {
            return PathUtils.getDefaultFile(context, fileName);
        } else {
            return createFile(path, fileName);
        }
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
