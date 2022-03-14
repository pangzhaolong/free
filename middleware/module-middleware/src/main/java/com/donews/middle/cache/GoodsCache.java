package com.donews.middle.cache;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;

import com.donews.utilslibrary.utils.LogUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class GoodsCache {
    private static String s_base_cache_path = "";

    public static void init(Context context) {
        s_base_cache_path = context.getDir("jdd", MODE_PRIVATE).getAbsolutePath()+ File.separator;
    }

    public static synchronized <T> T readGoodsBean(Class c, String ext) {
        T bean = null;
        ObjectInputStream ois = null;
        try {
            String path = s_base_cache_path + c.getName() + "_" + ext + ".dat";
            ois = new ObjectInputStream(new FileInputStream(path));
            bean = (T) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e(e.getMessage());
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bean;
    }

    public static synchronized void saveGoodsBean(Object object, String ext) {
        if (object == null) {
            return;
        }
        ObjectOutputStream fos = null;
        try {
            fos = new ObjectOutputStream(new FileOutputStream(s_base_cache_path + object.getClass().getName() + "_" + ext + ".dat"));
            //要使用writeObject
            assert fos != null;
            fos.writeObject(object);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e(e.getMessage());
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException ignored) {
            }
        }
    }
}
