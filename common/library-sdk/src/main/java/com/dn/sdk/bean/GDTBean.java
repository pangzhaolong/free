package com.dn.sdk.bean;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.util.List;


/**
 * @author by SnowDragon
 * Date on 2021/3/29
 * Description: 广点通
 */
@Keep
public class GDTBean {

    /**
     * sdk_proto_type : 1
     * txt : 全民养恐龙！
     * desc : 休闲又好玩，经营你的专属恐龙乐园！
     * screenshot_url_list : ["https://union.gdtimg.com/img/screenshotPic/b77f76450573e6ab16b83ce741295437.jpeg","https://union.gdtimg.com/img/screenshotPic/232a32e5da1a551d7ff033492b63f6d3.jpeg"]
     * domain : www.myapp.com
     * ext : {"appid":1110508754,"applogo":"https://union.ugdtimg.com/img/unionAppPic/1f65042b05677e5285056047e13f8ab2.png","appname":"全民养恐龙","appver":"15","appvername":"15.0.0","downloadnum":0,"mappid":1110508754,"outerurl":"","packagename":"com.kzgame.dino.ylh","pkg_name":"com.kzgame.dino.ylh","pkgsize":50638499,"pkgurl":"http://sdk.e.qq.com/app/redirect?aid=1110508754&amp;cid=65694","qzoneliked":1,"appstatus":0}
     */

    public int sdk_proto_type;
    public String txt;
    public String desc;
    public List<String> screenshot_url_list;
    @SerializedName("corporate_logo")
    public String corporateLogo;
    public String domain;
    public ExtBean ext;

    @Keep
    public static class ExtBean {
        /**
         * appid : 1110508754
         * applogo : https://union.ugdtimg.com/img/unionAppPic/1f65042b05677e5285056047e13f8ab2.png
         * appname : 全民养恐龙
         * appver : 15
         * appvername : 15.0.0
         * downloadnum : 0
         * mappid : 1110508754
         * outerurl :
         * packagename : com.kzgame.dino.ylh
         * pkg_name : com.kzgame.dino.ylh
         * pkgsize : 50638499
         * pkgurl : http://sdk.e.qq.com/app/redirect?aid=1110508754&amp;cid=65694
         * qzoneliked : 1
         * appstatus : 0
         */

        public int appid;
        public String applogo;
        public String appname;
        public String appver;
        public String appvername;
        public long downloadnum;
        public int mappid;
        public String outerurl;
        public String packagename;
        public String pkg_name;
        public int pkgsize;
        public String pkgurl;
        public int qzoneliked;
        public int appstatus;

        @Override
        public String toString() {
            return "ExtBean{" +
                    "applogo='" + applogo + '\'' +
                    ", appname='" + appname + '\'' +
                    ", packagename='" + packagename + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        String extStr = null;
        if (ext!=null){
            extStr = ext.toString();
        }

        return "GDTBean{" +
                "sdk_proto_type=" + sdk_proto_type +
                ", txt='" + txt + '\'' +
                ", desc='" + desc + '\'' +
                ", screenshot_url_list=" + screenshot_url_list +
                ", domain='" + domain + '\'' +
                ", ext=" + extStr +
                '}';
    }
}
