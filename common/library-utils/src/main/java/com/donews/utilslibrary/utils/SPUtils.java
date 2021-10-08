package com.donews.utilslibrary.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.donews.utilslibrary.BuildConfig;
import com.donews.utilslibrary.base.UtilsConfig;
import com.tencent.mmkv.MMKV;

/**
 * sp存储
 */
public class SPUtils {

    private static MMKV mmkv;
    private static boolean hasInit = false;

    private static void init(Context context) {
        if (!hasInit) {
            MMKV.initialize(context);
            hasInit = true;
        }

    }

    /**
     * 创建新的文件名称
     *
     * @param context 上下文
     * @param key     文件名称
     */
    public static void init(Context context, String key) {
        init(context, key, false);
    }

    /**
     * 创建新的文件名称
     *
     * @param context      上下文
     * @param key          文件名称
     * @param isCanProcess 是否支持进程通信
     */
    public static void init(Context context, String key, boolean isCanProcess) {
        init(context);
        if (TextUtils.isEmpty(key)) {
            mmkv = MMKV.defaultMMKV();
            return;
        }
        if (isCanProcess) {
            mmkv = MMKV.mmkvWithID(key, MMKV.MULTI_PROCESS_MODE);
        } else {
            mmkv = MMKV.mmkvWithID(key);
        }
    }

    public static void init(String key, Context context) {
        init(context);

        mmkv = MMKV.mmkvWithID(key, MMKV.MULTI_PROCESS_MODE);
        // 迁移旧数据 老项目中一直是“walk_sp”.这里可以不用改
        SharedPreferences old_man = context.getSharedPreferences("walk_sp", Context.MODE_PRIVATE);
        if (old_man != null) {
            mmkv.importFromSharedPreferences(old_man);
            old_man.edit().clear().apply();
        }
    }

    /**
     * 设置数据
     *
     * @param key    key
     * @param object 默认值
     */
    public static void setInformain(String key, Object object) {
        isNullMmkv();
        try {
            if (object instanceof String) {
                mmkv.encode(key, (String) object);
            } else if (object instanceof Integer) {
                mmkv.encode(key, (Integer) object);
            } else if (object instanceof Boolean) {
                mmkv.encode(key, (Boolean) object);
            } else if (object instanceof Float) {
                mmkv.encode(key, (Float) object);
            } else if (object instanceof Long) {
                mmkv.encode(key, (Long) object);
            } else if (object instanceof Double) {
                mmkv.encode(key, (Double) object);
            } else if (object instanceof byte[]) {
                mmkv.encode(key, (byte[]) object);
            } else {
                mmkv.encode(key, object.toString());
            }
        } catch (Throwable throwable) {
            if (LogUtil.allow) {
                throwable.printStackTrace();
            }
        }
    }


    /**
     * 获取long的值
     *
     * @param key      key
     * @param defValue 默认值
     * @return
     */
    public static long getLongInformain(String key, long defValue) {
        isNullMmkv();
        try {
            return mmkv.decodeLong(key, defValue);
        } catch (ClassCastException e) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace();
            }
        }
        return defValue;
    }

    /**
     * 获取String的值
     *
     * @param key      key
     * @param defValue 默认值
     * @return
     */
    public static String getInformain(String key, String defValue) {
        isNullMmkv();
        try {
            return mmkv.decodeString(key, defValue);
        } catch (ClassCastException e) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace();
            }
        }
        return defValue;
    }

    /**
     * 获取int的值
     *
     * @param key      key
     * @param defValue 默认值
     * @return
     */
    public static int getInformain(String key, int defValue) {
        isNullMmkv();
        try {
            return mmkv.decodeInt(key, defValue);
        } catch (ClassCastException e) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace();
            }
        }
        return defValue;

    }

    /**
     * 获取double的值
     *
     * @param key      key
     * @param defValue 默认值
     * @return
     */
    public static double getInformain(String key, double defValue) {
        isNullMmkv();
        try {
            return mmkv.decodeDouble(key, defValue);
        } catch (ClassCastException e) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace();
            }
        }
        return defValue;

    }

    /**
     * 获取float的值
     *
     * @param key      key
     * @param defValue 默认值
     * @return
     */
    public static float getInformain(String key, float defValue) {
        isNullMmkv();
        try {
            return mmkv.decodeFloat(key, defValue);
        } catch (ClassCastException e) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace();
            }
        }
        return defValue;

    }

    /**
     * 获取boolean的值
     *
     * @param key          key
     * @param defaultValue 默认值
     * @return
     */
    public static boolean getInformain(String key, boolean defaultValue) {
        isNullMmkv();
        try {
            return mmkv.decodeBool(key, defaultValue);

        } catch (ClassCastException e) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace();
            }
        }
        return defaultValue;
    }

    /**
     * 清除settings.xml某个键值对
     */
    public static void remove(String key) {
        isNullMmkv();
        try {
            mmkv.removeValueForKey(key);
        } catch (ClassCastException e) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 清除所有key
     */
    public static void clearAll() {
        isNullMmkv();
        try {
            mmkv.clearAll();
        } catch (ClassCastException e) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace();
            }
        }

    }

    private static void isNullMmkv() {
        if (mmkv == null) {
            init(UtilsConfig.getApplication(), KeySharePreferences.SP_KEY);
        }
    }
}
