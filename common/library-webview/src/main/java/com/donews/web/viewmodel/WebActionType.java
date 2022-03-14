package com.donews.web.viewmodel;

/**
 * <p> </p>
 * 作者： created by honeylife<br>
 * 日期： 2020/12/11 17:03<br>
 * 版本：V1.0<br>
 */
public interface WebActionType {
    /**
     * 动作，0:不做任何处理，客户端自己设置，1：加载视频广告，2：跳转html地址，location字段，
     * 3：微信分享，4：浏览sdk视频，5：浏览sdk新闻列表，6：浏览sdk新闻详情，7：绑定微信，8：提现 ，
     * 9：抖动效果，10：绑定手机号 ，11：淘宝授权， 12： 去购物，13： 浏览商品，14：签到提醒，15：分享商品,16：领红包
     * 17:福利
     */

    int no = 0;
    int video = 1;
    int html = 2;
    int share = 3;
    int sdkVideo = 4;
    int sdkList = 5;
    int sdkInfo = 6;
    int bindWx = 7;
    int money = 8;
    int dou = 9;
    int bindMobile = 10;
    int taoBao = 11;
    int goShop = 12;
    int selectShop = 13;
    int sign = 14;
    int shareShop = 15;
    int red = 16;
    int welfare = 17;
    int guess = 18;
    int nativeStart = 20;
    int nativeAction = 21;
}
