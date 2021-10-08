package com.donews.utilslibrary.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.LruCache;

import com.donews.utilslibrary.BuildConfig;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;

/**
 * @Author: honeylife
 * @CreateDate: 2020/4/14 9:37
 * @Description:
 */
public class ImageBase64Util {
    private LruCache<String, Bitmap> mMemoryCache;
    private int cacheSize = 0;

    /**
     * bitmap转换base64
     *
     * @param bitmap
     * @return
     */
    public String bitmapToBase64(Bitmap bitmap) {

        String result = "";
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            if (BuildConfig.DEBUG) {

                e.printStackTrace();
            }
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                if (BuildConfig.DEBUG) {

                    e.printStackTrace();
                }
            }

        }
        return result;
    }

    /**
     * base64转为bitmap
     *
     * @param base64Data
     * @return
     */
    public Bitmap base64ToBitmap(String base64Data) {
        if (cacheSize == 0) {
            // 获取到可用内存的最大值，使用内存超出这个值会引起OutOfMemory异常。
            // LruCache通过构造函数传入缓存值，以KB为单位。
            long maxMemory = Runtime.getRuntime().maxMemory() / 1024;
            // 使用最大可用内存值的1/8作为缓存的大小。
            cacheSize = (int) (maxMemory / 8);
        }

        if (mMemoryCache == null) {
            mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
                @Override
                protected int sizeOf(String key, Bitmap value) {
                    return value.getByteCount() / 1024;
                }
            };
        }

        Bitmap bitmap = null;
        byte[] imgByte = null;

        InputStream inputStream = null;
        try {
            bitmap = mMemoryCache.get(base64Data);
            if (bitmap == null) {
                imgByte = Base64.decode(base64Data, Base64.DEFAULT);
                BitmapFactory.Options option = new BitmapFactory.Options();
                option.inSampleSize = 2;
                option.inTempStorage = new byte[5 * 1024 * 1024];
                inputStream = new ByteArrayInputStream(imgByte);
                SoftReference<Bitmap> softReference = new SoftReference<Bitmap>(BitmapFactory.decodeStream(inputStream, null, option));
                bitmap = softReference.get();
                softReference.clear();
                mMemoryCache.put(base64Data, bitmap);
            }

        } catch (Exception e) {
            if (BuildConfig.DEBUG) {

                e.printStackTrace();
            }
        } finally {
            imgByte = null;
            try {
                assert inputStream != null;
                inputStream.close();
                System.gc();
            } catch (IOException e) {
                if (BuildConfig.DEBUG) {
                    e.printStackTrace();
                }
            }
        }
        return bitmap;
    }

    /**
     * @param string
     * @return
     */
    public Bitmap stringtoBitmap(String string) {
        //将字符串转换成Bitmap类型
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(string, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    public byte[] bmpToByteArray(Bitmap bmp, boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
//            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
