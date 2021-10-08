package com.dn.sdk.bean;

import androidx.annotation.Keep;

import com.donews.common.contract.BaseCustomViewModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author by SnowDragon
 * Date on 2021/3/29
 * Description:
 */
@Keep
public class IntegralBean {
    public List<DataBean> appList;


    /**
     * id : 1
     * name : com
     * pkg : com.wifi.wfdj
     * download_url : http://baidu.com
     * upload_url : http://baidu.com
     * title : 试用 com 30秒，赚取0.03元
     * subtitle : 试用《com》领取
     * status app当前事件 1:点击，2: 下载，3: 下载完成和安装，4：安装完成，5: 激活
     * icon ，icon地址
     * type: 1,穿山甲，2，腾讯
     */
    public static class DataBean extends BaseCustomViewModel {
        public int id;
        public String name;
        public String pkg;
        @SerializedName("download_url")
        public String downLoadUrl;
        @SerializedName("deep_link")
        public String deepLink;
        /**
         * 奖励
         */
        public String title;
        public String subtitle;
        @SerializedName("state")
        public int status;
        @SerializedName("icon")
        public String appIcon;
        public int type;

        public String desc;
        public String text;
        //是否可提现，true可提取，false 不可提现
        public boolean isWithdraw;
        // 提现的金额
        public double money;

        @Override
        public String toString() {
            return "{" +
                    "\"id\":" + id +
                    ", \"name\":\"" + name + "\"" +
                    ", \"pkg\":\"" + pkg + "\"" +
                    ", \"downLoadUrl\":\"" + downLoadUrl + "\"" +
                    ", \"deepLink\":\"" + deepLink + "\"" +
                    ", \"title\":\"" + title + "\"" +
                    ", \"subtitle\":\"" + subtitle + "\"" +
                    ", \"status\":" + status +
                    ", \"appIcon\":\"" + appIcon + "\"" +
                    ", \"type\":" + type +
                    ", \"desc\":\"" + desc + "\"" +
                    ", \"text\":\"" + text + "\"" +
                    ", \"isWithdraw\":" + isWithdraw +
                    ", \"money\":" + money +
                    '}';
        }

    }
}
