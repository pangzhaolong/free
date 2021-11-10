package com.donews.middle.cache;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;

import com.donews.middle.bean.WalletBean;
import com.donews.middle.bean.fh.HomeDataBean;
import com.donews.middle.bean.front.AwardBean;
import com.donews.middle.bean.front.LotteryCategoryBean;
import com.donews.middle.bean.front.LotteryGoodsBean;
import com.donews.middle.bean.home.HomeCategoryBean;
import com.donews.middle.bean.home.HomeGoodsBean;
import com.donews.middle.bean.home.RealTimeBean;
import com.donews.middle.bean.home.SearchResultTbBean;
import com.donews.middle.bean.home.SecKilBean;
import com.donews.utilslibrary.utils.LogUtil;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class GoodsCache {
    private static String s_base_cache_path = "";

    public static void init(Context context) {
        s_base_cache_path = context.getDir("jdd", MODE_PRIVATE).getAbsolutePath();
    }

    public static synchronized <T> T readGoodsBean(Class c, String ext) {
        T people = null;
        ObjectInputStream ois = null;
        try {
            String path = s_base_cache_path + c.getName() + "_" + ext + ".dat";
            if (c == LotteryCategoryBean.class) {
                ois = new ObjectInputStream(new FileInputStream(path));
            } else if (c == LotteryGoodsBean.class) {
                ois = new ObjectInputStream(new FileInputStream(path));
            } else if (c == AwardBean.class) {
                ois = new ObjectInputStream(new FileInputStream(path));
            } else if (c == WalletBean.class) {
                ois = new ObjectInputStream(new FileInputStream(path));
            } else if (c == HomeDataBean.class) {
                ois = new ObjectInputStream(new FileInputStream(path));
            } else if (c == HomeGoodsBean.class) {
                ois = new ObjectInputStream(new FileInputStream(path));
            } else if (c == RealTimeBean.class) {
                ois = new ObjectInputStream(new FileInputStream(path));
            } else if (c == SecKilBean.class) {
                ois = new ObjectInputStream(new FileInputStream(path));
            } else if (c == SearchResultTbBean.class) {
                ois = new ObjectInputStream(new FileInputStream(path));
            } else if (c == HomeCategoryBean.class) {
                ois = new ObjectInputStream(new FileInputStream(path));
            }
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

    public static synchronized void saveGoodsBean(Object object, String ext) {
        if (object == null) {
            return;
        }
        ObjectOutputStream fos = null;
        try {
            if (object instanceof LotteryCategoryBean) {
                fos = new ObjectOutputStream(new FileOutputStream(s_base_cache_path + LotteryCategoryBean.class.getName() + "_" + ext + ".dat"));
            } else if (object instanceof AwardBean) {
                fos = new ObjectOutputStream(new FileOutputStream(s_base_cache_path + AwardBean.class.getName() + "_" + ext + ".dat"));
            } else if (object instanceof WalletBean) {
                fos = new ObjectOutputStream(new FileOutputStream(s_base_cache_path + WalletBean.class.getName() + "_" + ext + ".dat"));
            } else if (object instanceof HomeDataBean) {
                fos = new ObjectOutputStream(new FileOutputStream(s_base_cache_path + HomeDataBean.class.getName() + "_" + ext + ".dat"));
            } else if (object instanceof HomeGoodsBean) {
                fos = new ObjectOutputStream(new FileOutputStream(s_base_cache_path + HomeGoodsBean.class.getName() + "_" + ext + ".dat"));
            } else if (object instanceof RealTimeBean) {
                fos = new ObjectOutputStream(new FileOutputStream(s_base_cache_path + RealTimeBean.class.getName() + "_" + ext + ".dat"));
            } else if (object instanceof SecKilBean) {
                fos = new ObjectOutputStream(new FileOutputStream(s_base_cache_path + SecKilBean.class.getName() + "_" + ext + ".dat"));
            } else if (object instanceof LotteryGoodsBean) {
                fos = new ObjectOutputStream(new FileOutputStream(s_base_cache_path + LotteryGoodsBean.class.getName() + "_" + ext + ".dat"));
            } else if (object instanceof SearchResultTbBean) {
                fos = new ObjectOutputStream(new FileOutputStream(s_base_cache_path + SearchResultTbBean.class.getName() + "_" + ext + ".dat"));
            } else if (object instanceof HomeCategoryBean) {
                fos = new ObjectOutputStream(new FileOutputStream(s_base_cache_path + HomeCategoryBean.class.getName() + "_" + ext + ".dat"));
            }
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
