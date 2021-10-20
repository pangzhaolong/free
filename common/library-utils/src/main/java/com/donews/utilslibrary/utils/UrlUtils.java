package com.donews.utilslibrary.utils;
public class UrlUtils {
    /**
     * 处理后端返回的url没有https前缀
     * @param  url 需要匹配的url
     *
     */
    public static String formatUrlPrefix(String url) {
        if (url != null) {
            if (url.contains("https://")) {
            } else if (url.contains("http://")) {
            } else {
                url = "http:" + url;
            }
        } else {
            return "";
        }
        return url;
    }
}
