package com.donews.base.utils.glide;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.cache.DiskCache;

import java.io.File;
import java.math.BigDecimal;

/**
 * @author by SnowDragon
 * Date on 2020/11/12
 * Description:
 */
public class GlideUtils {

    public static final String TAG = "GlideUtils";
    private static boolean checkContextAndUrl(Context context, String url) {
        if (context == null || TextUtils.isEmpty(url)) {
            Log.e(TAG, "The context or URL is empty");
            return false;
        }
        return !(context instanceof Activity) || !((Activity) context).isDestroyed();
    }

    /**
     * @param context   上下文
     * @param url       图片地址
     * @param imageView 视图
     */
    public static void loadImageView(Context context, String url, ImageView imageView) {

        if (checkContextAndUrl(context, url)) {
            Glide.with(context).load(url).into(imageView);
        }
    }


    /**
     * 加载图片指定是否使用内存缓存，本地缓存
     *
     * @param context           上下文
     * @param url               图片地址
     * @param imageView         视图
     * @param useMemoryCache    是否使用内存缓存
     * @param diskCacheStrategy 磁盘缓存策略；NONE 不缓存任何内容；SOURCE：只缓存原始图片
     *                          AUTOMATIC：只缓存转换后的图片；ALL：都缓存
     */
    public static void loadImageView(Context context, String url, ImageView imageView,
                                     boolean useMemoryCache, DiskCacheStrategy diskCacheStrategy) {

        if (checkContextAndUrl(context, url)) {
            Glide.with(context).load(url)
                    .skipMemoryCache(!useMemoryCache)
                    .diskCacheStrategy(diskCacheStrategy)
                    .into(imageView);
        }
    }


    /**
     * 加载指定大小的图片
     *
     * @param mContext  上下文
     * @param url       图片地址
     * @param width     图片宽：px
     * @param height    图片高：px
     * @param imageView 视图
     */
    public static void loadImageViewSize(Context mContext, String url, int width, int height,
                                         ImageView imageView) {
        if (checkContextAndUrl(mContext, url)) {
            Glide.with(mContext).load(url).override(width, height).into(imageView);
        }
    }

    /**
     * 加载中图片，加载失败图片
     *
     * @param mContext  上下文
     * @param url       图片地址
     * @param imageView 视图
     * @param loadingId 加载中需要显示的图片id
     * @param errorId   加载失败需要显示的图片id
     */
    public static void loadImageViewLoading(Context mContext, String url, ImageView imageView,
                                            int loadingId, int errorId) {

        if (checkContextAndUrl(mContext, url)) {
            Glide.with(mContext).load(url).placeholder(loadingId).error(errorId).into(imageView);
        }
    }

    /**
     * 加载圆角图片
     *
     * @param mContext
     * @param url
     * @param imageView
     * @param roundCornersTransform
     */
    public static void loadImageRoundCorner(Context mContext, String url, ImageView imageView,
                                            RoundCornersTransform roundCornersTransform) {
        if (checkContextAndUrl(mContext, url)) {
            Glide.with(mContext).load(url).transform(roundCornersTransform).into(imageView);
        }

    }

    /**
     * 加载带有描边的圆形图片
     *
     * @param mContext
     * @param url
     * @param imageView
     * @param roundCornersTransform
     */
    public static void loadImageCircleBorder(Context mContext, String url, ImageView imageView,
                                             CircleBorderTransform circleBorderTransform) {

        if (checkContextAndUrl(mContext, url)) {
            Glide.with(mContext).load(url).transform(circleBorderTransform).into(imageView);
        }

    }


    /**
     * 清除缓存,必须在子线程程调用
     *
     * @param mContext 上下文
     */
    public void cleanCache(Context mContext) {
        if (mContext == null) {
            return;
        }
        Glide.get(mContext).clearDiskCache();

    }

    /**
     * 清理Glide磁盘缓存
     *
     * @param mContext 上下文
     * @return true 清理完成，false 清理出现异常
     */
    public static boolean cleanCatchDisk(Context mContext) {
        return deleteFolderFile(mContext.getExternalCacheDir() + "/" + DiskCache.Factory.DEFAULT_DISK_CACHE_DIR, true);
    }


    /**
     * 获取Glide磁盘缓存大小
     *
     * @return Glide当前的缓存大小。
     */
    public static String getCacheSize(Context mContext) {
        try {
            return getFormatSize(getFolderSize(new File(mContext.getExternalCacheDir() + "/" + DiskCache.Factory.DEFAULT_DISK_CACHE_DIR)));
        } catch (Exception e) {
            e.printStackTrace();
            return "获取失败";
        }
    }


    /**
     * 获取指定文件夹内所有文件大小的和
     *
     * @param file 文件
     * @return 返回long类型文件大小的和
     */
    private static long getFolderSize(File file) {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (File aFileList : fileList) {
                if (aFileList.isDirectory()) {
                    size = size + getFolderSize(aFileList);
                } else {
                    size = size + aFileList.length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * @param size 文件大小
     * @return 返回格式化后的单位
     */
    private static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "Byte";
        }
        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }
        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }
        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }


    /**
     * 按目录删除文件夹文件方法
     *
     * @param filePath       文件
     * @param deleteThisPath 是否删除当前目录
     * @return
     */
    private static boolean deleteFolderFile(String filePath, boolean deleteThisPath) {
        try {
            File file = new File(filePath);
            if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (File file1 : files) {
                    deleteFolderFile(file1.getAbsolutePath(), true);
                }
            }
            if (deleteThisPath) {
                if (!file.isDirectory()) {
                    file.delete();
                } else {
                    if (file.listFiles().length == 0) {
                        file.delete();
                    }
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }


}
