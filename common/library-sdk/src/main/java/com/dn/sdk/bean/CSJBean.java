package com.dn.sdk.bean;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author by SnowDragon
 * Date on 2021/3/29
 * Description: 穿山甲实体
 */
@Keep
public class CSJBean {
    /**
     * open_ad_sdk_download_extra : {"tag":"splash_ad","material_meta":{"interaction_type":4,"target_url":"","ad_id":"1686863580647469","icon":{"url":"http://sf3-be-pack.pglstatp-toutiao.com/obj/ad-app-package/2de32ae431e1d52626efb3a5bd749bca","height":72,"width":72},"cover_image":{"url":"http://sf6-be-pack.pglstatp-toutiao.com/img/arthur/202012165d0d4140720ea8e74c7fb10d~c1_1080x1920_q60.jpeg","height":1920,"width":1080},"image":[{"url":"http://sf6-be-pack.pglstatp-toutiao.com/img/arthur/202012165d0d4140720ea8e74c7fb10d~c1_1080x1920_q60.jpeg","height":1920,"width":1080}],"title":"假期剧荒？来这里，精彩视频让你看个够~","description":"假期剧荒？来这里，精彩视频让你看个够~","ad_logo":1,"app":{"app_name":"B站-看你想看","package_name":"tv.danmaku.bili","download_url":"https://dl.hdslb.com/mobile/latest/iBiliPlayer-xxl_jrtt_rd_015.apk","score":4,"comment_num":6870,"quick_app_url":"","app_size":0},"deep_link":{"deeplink_url":"bilibili://bangumi/season/36720?h5awaken=b3Blbl9hcHBfZnJvbV90eXBlPWRlZXBsaW5rX3R0bGgtYW5kLXJkLTMxeWF6LTE2Nzc5NTY3Mzc5MjgxOTkmb3Blbl9hcHBfdXJsPXNzMzY3MjA=&amp;backurl=__back_url__","fallback_url":"https://dl.hdslb.com/mobile/latest/iBiliPlayer-xxl_jrtt_rd_015.apk","fallback_type":2},"app_manage":{"developer_name":"上海宽娱数码科技有限公司","app_version":"6.18.2","privacy_policy_url":"https://www.bilibili.com/blackboard/privacy-h5.html","package_name":"tv.danmaku.bili","app_name":"哔哩哔哩"}}}
     */

    @SerializedName("open_ad_sdk_download_extra")
    public OpenAdSdkDownloadExtraBean adInfo;

    public static class OpenAdSdkDownloadExtraBean {
        /**
         * tag : splash_ad
         * material_meta : {"interaction_type":4,"target_url":"","ad_id":"1686863580647469","icon":{"url":"http://sf3-be-pack.pglstatp-toutiao.com/obj/ad-app-package/2de32ae431e1d52626efb3a5bd749bca","height":72,"width":72},"cover_image":{"url":"http://sf6-be-pack.pglstatp-toutiao.com/img/arthur/202012165d0d4140720ea8e74c7fb10d~c1_1080x1920_q60.jpeg","height":1920,"width":1080},"image":[{"url":"http://sf6-be-pack.pglstatp-toutiao.com/img/arthur/202012165d0d4140720ea8e74c7fb10d~c1_1080x1920_q60.jpeg","height":1920,"width":1080}],"title":"假期剧荒？来这里，精彩视频让你看个够~","description":"假期剧荒？来这里，精彩视频让你看个够~","ad_logo":1,"app":{"app_name":"B站-看你想看","package_name":"tv.danmaku.bili","download_url":"https://dl.hdslb.com/mobile/latest/iBiliPlayer-xxl_jrtt_rd_015.apk","score":4,"comment_num":6870,"quick_app_url":"","app_size":0},"deep_link":{"deeplink_url":"bilibili://bangumi/season/36720?h5awaken=b3Blbl9hcHBfZnJvbV90eXBlPWRlZXBsaW5rX3R0bGgtYW5kLXJkLTMxeWF6LTE2Nzc5NTY3Mzc5MjgxOTkmb3Blbl9hcHBfdXJsPXNzMzY3MjA=&amp;backurl=__back_url__","fallback_url":"https://dl.hdslb.com/mobile/latest/iBiliPlayer-xxl_jrtt_rd_015.apk","fallback_type":2},"app_manage":{"developer_name":"上海宽娱数码科技有限公司","app_version":"6.18.2","privacy_policy_url":"https://www.bilibili.com/blackboard/privacy-h5.html","package_name":"tv.danmaku.bili","app_name":"哔哩哔哩"}}
         */

        public String tag;
        @SerializedName("material_meta")
        public MaterialMetaBean metaData;

        @Keep
        public static class MaterialMetaBean {
            /**
             * interaction_type : 4
             * target_url :
             * ad_id : 1686863580647469
             * icon : {"url":"http://sf3-be-pack.pglstatp-toutiao.com/obj/ad-app-package/2de32ae431e1d52626efb3a5bd749bca","height":72,"width":72}
             * cover_image : {"url":"http://sf6-be-pack.pglstatp-toutiao.com/img/arthur/202012165d0d4140720ea8e74c7fb10d~c1_1080x1920_q60.jpeg","height":1920,"width":1080}
             * image : [{"url":"http://sf6-be-pack.pglstatp-toutiao.com/img/arthur/202012165d0d4140720ea8e74c7fb10d~c1_1080x1920_q60.jpeg","height":1920,"width":1080}]
             * title : 假期剧荒？来这里，精彩视频让你看个够~
             * description : 假期剧荒？来这里，精彩视频让你看个够~
             * ad_logo : 1
             * app : {"app_name":"B站-看你想看","package_name":"tv.danmaku.bili","download_url":"https://dl.hdslb.com/mobile/latest/iBiliPlayer-xxl_jrtt_rd_015.apk","score":4,"comment_num":6870,"quick_app_url":"","app_size":0}
             * deep_link : {"deeplink_url":"bilibili://bangumi/season/36720?h5awaken=b3Blbl9hcHBfZnJvbV90eXBlPWRlZXBsaW5rX3R0bGgtYW5kLXJkLTMxeWF6LTE2Nzc5NTY3Mzc5MjgxOTkmb3Blbl9hcHBfdXJsPXNzMzY3MjA=&amp;backurl=__back_url__","fallback_url":"https://dl.hdslb.com/mobile/latest/iBiliPlayer-xxl_jrtt_rd_015.apk","fallback_type":2}
             * app_manage : {"developer_name":"上海宽娱数码科技有限公司","app_version":"6.18.2","privacy_policy_url":"https://www.bilibili.com/blackboard/privacy-h5.html","package_name":"tv.danmaku.bili","app_name":"哔哩哔哩"}
             */

            public String ad_id;
            public IconBean icon;
            public CoverImageBean cover_image;
            public List<ImageBean> image;
            public String title;
            public String description;
            public AppBean app;
            public DeepLinkBean deep_link;

            @Keep
            public static class IconBean {
                /**
                 * url : http://sf3-be-pack.pglstatp-toutiao.com/obj/ad-app-package/2de32ae431e1d52626efb3a5bd749bca
                 * height : 72
                 * width : 72
                 */

                public String url;
                public int height;
                public int width;
            }

            @Keep
            public static class CoverImageBean {
                /**
                 * url : http://sf6-be-pack.pglstatp-toutiao.com/img/arthur/202012165d0d4140720ea8e74c7fb10d~c1_1080x1920_q60.jpeg
                 * height : 1920
                 * width : 1080
                 */

                public String url;
                public int height;
                public int width;
            }

            @Keep
            public static class AppBean {
                /**
                 * app_name : B站-看你想看
                 * package_name : tv.danmaku.bili
                 * download_url : https://dl.hdslb.com/mobile/latest/iBiliPlayer-xxl_jrtt_rd_015.apk
                 * score : 4
                 * comment_num : 6870
                 * quick_app_url :
                 * app_size : 0
                 */

                public String app_name;
                public String package_name;
                public String download_url;
            }

            @Keep
            public static class DeepLinkBean {
                /**
                 * deeplink_url : bilibili://bangumi/season/36720?h5awaken=b3Blbl9hcHBfZnJvbV90eXBlPWRlZXBsaW5rX3R0bGgtYW5kLXJkLTMxeWF6LTE2Nzc5NTY3Mzc5MjgxOTkmb3Blbl9hcHBfdXJsPXNzMzY3MjA=&amp;backurl=__back_url__
                 * fallback_url : https://dl.hdslb.com/mobile/latest/iBiliPlayer-xxl_jrtt_rd_015.apk
                 * fallback_type : 2
                 */

                public String deeplink_url;
                public String fallback_url;
                public int fallback_type;
            }


            @Keep
            public static class ImageBean {
                /**
                 * url : http://sf6-be-pack.pglstatp-toutiao.com/img/arthur/202012165d0d4140720ea8e74c7fb10d~c1_1080x1920_q60.jpeg
                 * height : 1920
                 * width : 1080
                 */

                public String url;
                public int height;
                public int width;
            }
        }
    }

    @Override
    public String toString() {
        return "CSJBean{" +
                "open_ad_sdk_download_extra=" + adInfo +
                '}';
    }
}
