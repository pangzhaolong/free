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

    /**
     * 处理后端返回的url没有https前缀(此为头像处理。如果没有头像显示为默认头像)
     * @param  url 需要匹配的url，为空显示为默认头像
     *
     */
    public static String formatHeadUrlPrefix(String url) {
        if(url == null || "".equals(url)){
            return "https://ad-static-xg.tagtic.cn/ad-material/file/0b8f18e1e666474291174ba316cccb51.png";
        }
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
