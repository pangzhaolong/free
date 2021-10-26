package com.donews.front.cache;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;

import com.donews.front.bean.NorGoodsBean;
import com.donews.utilslibrary.utils.LogUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class GoodsCache {
    private static String s_base_cache_path = "";
    private static String s_banner_bean_path = "";
    private static String s_top_goods_bean_path = "";
    private static String s_real_time_bean_path = "";
    private static String s_sec_kil_bean_path = "";
    private static String s_nor_goods_bean_path = "";
    private static String s_search_tb_bean_path = "";

    public static void init(Context context) {
        s_base_cache_path = context.getDir("jdd", MODE_PRIVATE).getAbsolutePath();
        s_banner_bean_path = s_base_cache_path + "/banner_bean.dat";
        s_top_goods_bean_path = s_base_cache_path + "/top_goods_bean.dat";
        s_real_time_bean_path = s_base_cache_path + "/top_real_time_bean.dat";
        s_sec_kil_bean_path = s_base_cache_path + "/top_sec_kil_bean.dat";
        s_nor_goods_bean_path = s_base_cache_path + "/top_nor_goods_bean.dat";
        s_search_tb_bean_path = s_base_cache_path + "/search_tb_bean.dat";
    }

    public static <T> T readGoodsBean(Class c, String ext) {
        T people = null;
        ObjectInputStream ois = null;
        try {
            /*if (c == DataBean.class) {
//                Log.e("TAG", new File(s_banner_bean_path).getAbsolutePath() + "<---");
                ois = new ObjectInputStream(new FileInputStream(s_banner_bean_path + ext));
            } else if (c == TopGoodsBean.class) {
                ois = new ObjectInputStream(new FileInputStream(s_top_goods_bean_path + ext));
            } else if (c == RealTimeBean.class) {
                ois = new ObjectInputStream(new FileInputStream(s_real_time_bean_path + ext));
            } else if (c == SecKilBean.class) {
                ois = new ObjectInputStream(new FileInputStream(s_sec_kil_bean_path + ext));
            } else */if (c == NorGoodsBean.class) {
                ois = new ObjectInputStream(new FileInputStream(s_nor_goods_bean_path + ext));
            } /*else if (c == SearchResultTbBean.class) {
                ois = new ObjectInputStream(new FileInputStream(s_search_tb_bean_path + ext));
            }*/
            assert ois != null;
            people = (T) ois.readObject();
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
        return people;
    }

    public static void saveGoodsBean(Object object, String ext) {
        if (object == null) {
            return;
        }
        ObjectOutputStream fos = null;
        try {
            //如果文件不存在就创建文件
            File file = null;
            /*if (object instanceof DataBean) {
                fos = new ObjectOutputStream(new FileOutputStream(s_banner_bean_path + ext));
            } else if (object instanceof TopGoodsBean) {
                fos = new ObjectOutputStream(new FileOutputStream(s_top_goods_bean_path + ext));
            } else if (object instanceof RealTimeBean) {
                fos = new ObjectOutputStream(new FileOutputStream(s_real_time_bean_path + ext));
            } else if (object instanceof SecKilBean) {
                fos = new ObjectOutputStream(new FileOutputStream(s_sec_kil_bean_path + ext));
            } else */if (object instanceof NorGoodsBean) {
                fos = new ObjectOutputStream(new FileOutputStream(s_nor_goods_bean_path + ext));
            }/* else if (object instanceof SearchResultTbBean) {
                fos = new ObjectOutputStream(new FileOutputStream(s_search_tb_bean_path + ext));
            }*/
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
