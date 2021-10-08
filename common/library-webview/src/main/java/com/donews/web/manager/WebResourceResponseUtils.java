package com.donews.web.manager;

import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.donews.base.base.ContextHolder;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.MimeTypeMap;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @Author: honeylife
 * @CreateDate: 2020/9/30 14:25
 * @Description:
 */
public class WebResourceResponseUtils {

    private static ArrayList<String> mKeyList = new ArrayList<>();

    public static void setUrl(String url) {
        mKeyList.add(url);
    }

    /**
     * 拦截url 判断本地是否有缓存资源文件
     *
     * @param uri
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static WebResourceResponse interceptRequest(Uri uri) {
        WebResourceResponse resourceResponse = null;
        try {
            //加载资源类型
            String mimeType = "";
            String path = "";
            File file = new File(uri.getPath());
            Log.e("TAG", "===========================" + file.getName());
            if (uri.getPath().contains(".js")) {
                mimeType = "text/javascript";
                path = "dist/static/js/" + file.getName();
            } else if (uri.getPath().contains(".json")) {
                mimeType = "text/json";
            } else if (uri.getPath().contains(".css")) {
                mimeType = "text/css";
                path = "dist/static/css/" + file.getName();
            } else if (uri.getPath().contains(".png") || uri.getPath().contains(".jpge")) {
                mimeType = "image/png";
                path = "dist/static/img/" + file.getName();
            } else {
                mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(uri.getPath().substring(uri.getPath().lastIndexOf(".") + 1));
            }
            if (!TextUtils.isEmpty(mimeType)) {
                HashMap<String, String> header = new HashMap<>();
                header.put("Access-Control-Allow-Origin", "*");
                header.put("Access-Control-Allow-Headers", "Content-Type");

                if (!TextUtils.isEmpty(path)) {
                    InputStream inputStream1 = ContextHolder.getInstance().getClass().getClassLoader().getResourceAsStream("assets/" + path);
                    resourceResponse = new WebResourceResponse(mimeType, "", 200, "ok", header, inputStream1);
                    return resourceResponse;
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
            return resourceResponse;
        }
//            }
//        }
        return resourceResponse;
    }
}