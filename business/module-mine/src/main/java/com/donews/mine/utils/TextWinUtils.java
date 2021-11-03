package com.donews.mine.utils;

import android.widget.TextView;

import com.donews.mine.bean.resps.RecommendGoodsResp;

import java.util.List;

/**
 * @author lcl
 * Date on 2021/10/21
 * Description:
 * 开奖码相同的文字变红处理
 */
public class TextWinUtils {
    /**
     * 处理原始文字。处理成Html代码。需要使用Html.from加载显示
     *
     * @param winCode 中奖吗
     * @param myCodes 我得抽奖码
     * @return
     */
    public static String drawOldText(String winCode, List<String> myCodes) {
        String flg = "、";
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < myCodes.size(); i++) {
            String myCode = myCodes.get(i);
            if (myCode.length() != winCode.length()) {
                //数据错误，直接加入不进行比较
                if (i == myCodes.size() - 1) {
                    sb.append(myCode);
                } else {
                    sb.append(myCode + flg);
                }
                continue;
            }
            StringBuffer temSb = new StringBuffer();
            for (int ci = 0; ci < winCode.length(); ci++) {
                if (myCode.charAt(ci) == winCode.charAt(ci)) {
                    temSb.append("<font color='#ff0000'>" + myCode.charAt(ci) + "</font>");
                } else {
                    temSb.append(myCode.charAt(ci));
                }
            }
            //将临时字符加入的返回字符集
            if (i == myCodes.size() - 1) {
                sb.append(temSb.toString());
            } else {
                sb.append(temSb.toString() + flg);
            }
        }
        return sb.toString();
    }

    /**
     * 更新抽奖状态，也就是抽奖的各种状态
     *
     * @param tv
     * @param item
     */
    public static void updateWinStatus(
            TextView tv, RecommendGoodsResp.ListDTO item) {
        if (item.lotteryStatus == 1) {
            tv.setText("继续参与");
        } else if (item.lotteryStatus == 2) {
            tv.setText("等待开奖");
        } else {
            tv.setText("0元抽奖");
        }
    }
}
