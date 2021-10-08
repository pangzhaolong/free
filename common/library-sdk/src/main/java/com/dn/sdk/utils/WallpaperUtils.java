package com.dn.sdk.utils;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author by SnowDragon
 * Date on 2021/1/5
 * Description:
 */
public class WallpaperUtils {
    private static Bitmap bitmap = null;

    public static Bitmap getWallpaper(Context mContext) {
        if (bitmap != null) {
            return bitmap;
        }
        WallpaperManager wallpaperManager = WallpaperManager
                .getInstance(mContext);
        // 获取当前壁纸
        Drawable wallpaperDrawable = wallpaperManager.getDrawable();

        // 将Drawable,转成Bitmap
        Bitmap bm = ((BitmapDrawable) wallpaperDrawable).getBitmap();
        //压缩
        bitmap = compressBitmap(bm, 80);


        return bitmap;
    }

    /**
     * 压缩图片
     *
     * @param bitmap    被压缩的图片
     * @param sizeLimit 大小限制
     * @return 压缩后的图片
     */
    private static Bitmap compressBitmap(Bitmap bitmap, long sizeLimit) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int quality = 60;
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);

        // 循环判断压缩后图片是否超过限制大小
        while (baos.toByteArray().length / 1024 > sizeLimit) {
            baos.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
            quality -= 10;
        }

        Bitmap newBitmap = BitmapFactory.decodeStream(new ByteArrayInputStream(baos.toByteArray()), null, null);
        return newBitmap;
    }

    /**
     * Save Bitmap
     *
     * @param name file name
     * @param bm   picture to save
     */
    static void saveBitmap(String name, Bitmap bm) {
        Log.d("Save Bitmap", "Ready to save picture");
        //指定我们想要存储文件的地址
        String TargetPath = Environment.getExternalStorageDirectory() + "/AAA/images/";
        Log.d("Save Bitmap", "Save Path=" + TargetPath);
        //判断指定文件夹的路径是否存在
        File fileDir = new File(TargetPath);
        fileDir.mkdirs();

        //如果指定文件夹创建成功，那么我们则需要进行图片存储操作
        File saveFile = new File(TargetPath, name);


        try {
            FileOutputStream saveImgOut = new FileOutputStream(saveFile);
            // compress - 压缩的意思
            bm.compress(Bitmap.CompressFormat.JPEG, 100, saveImgOut);

            //存储完成后需要清除相关的进程
            saveImgOut.flush();
            saveImgOut.close();
            Log.d("Save Bitmap", "The picture is save to your phone!");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private String getPath() {
        String path = Environment.getExternalStorageDirectory() + "/Luban/image/";
        File file = new File(path);
        if (file.mkdirs()) {
            return path;
        }
        return path;
    }
}
