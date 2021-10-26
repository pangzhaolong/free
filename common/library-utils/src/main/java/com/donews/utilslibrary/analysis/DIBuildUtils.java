package com.donews.utilslibrary.analysis;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lcl
 * Date on 2021/9/29
 * Description:
 * 多牛大数据对于参数的处理
 */
public class DIBuildUtils {
    /**
     * 对dms、dmn 参数的处理。
     * key：表示属性名称，
     * value ：表示此参数需要放置于第几个位置，也就是：dmn[value] 的位置
     */
    private static Map<String, Integer> DNDI_PARAMS_KEY = new HashMap() {
        {
            /*//主流程-扫描结果页
            put(Dot.scan_page_action_pa, 1);
            put(Dot.scan_page_action_extrinsic_pa, 2);
            put(Dot.scan_page_action_extrinsic_us, 3);
            put(Dot.scan_page_action_extrinsic_ss, 4);
            put(Dot.scan_page_action_extrinsic_ba, 5);
            //主流程-使用过程页
            put(Dot.using_page_action_pa, 1);
            put(Dot.using_page_action_pa_wifi, 2);
            put(Dot.using_page_action_wifi_activation, 3);
            put(Dot.using_page_action_psm, 4);
            //主流程-使用结果页
            put(Dot.results_page_action_pa_wifi, 1);
            put(Dot.results_page_action_wifi_activation, 2);
            put(Dot.results_page_action_psm, 3);
            put(Dot.results_page_action_pa, 4);
            put(Dot.results_page_action_extrinsic_pa, 5);
            put(Dot.results_page_action_extrinsic_us, 6);
            put(Dot.results_page_action_extrinsic_ss, 7);
            put(Dot.results_page_action_extrinsic_ba, 8);
            //提示使用弹窗
            put(Dot.use_popup_action_extrinsic_charge, 1);
            put(Dot.use_popup_action_extrinsic_uninstall, 2);
            put(Dot.use_popup_action_extrinsic_download, 3);
            put(Dot.use_popup_action_extrinsic_wifi_connection, 4);
            put(Dot.use_popup_action_extrinsic_RAM, 5);
            put(Dot.use_popup_action_extrinsic_APP, 6);
            put(Dot.use_popup_action_extrinsic_malware, 7);
            //金币领取弹窗
            put(Dot.receive_popup_actioin_recommended, 1);
            put(Dot.receive_popup_actioin_list, 2);
            //提示使用弹窗-按钮
            put(Dot.use_popup_button_charge, 1);
            put(Dot.use_popup_button_uninstall, 2);
            put(Dot.use_popup_button_download, 3);
            put(Dot.use_popup_button_wifi_connection, 4);
            put(Dot.use_popup_button_RAM, 5);
            put(Dot.use_popup_button_APP, 6);
            put(Dot.use_popup_button_malware, 7);
            //金币领取弹窗-主按钮
            put(Dot.receive_popup_mbutton_home_page, 1);
            put(Dot.receive_popup_mbutton_home_list, 2);
            put(Dot.receive_popup_mbutton_home_icon, 3);
            //金币领取弹窗-次按钮
            put(Dot.receive_popup_sbutton_home_list, 1);
            put(Dot.receive_popup_sbutton_home_icon, 2);
            //首页推荐区入口点击
            put(Dot.home_page_action_wifi_click, 1);
            put(Dot.home_page_action_se, 2);
            put(Dot.home_page_action_pa, 3);
            put(Dot.home_page_action_wsm, 4);
            //首页其它位置入口点击
            put(Dot.home_page_other_action_wm, 1);
            put(Dot.home_page_other_action_coupons, 2);
            put(Dot.home_page_other_action_reward, 3);
            put(Dot.home_page_other_action_leadboard, 4);
            put(Dot.home_page_other_action_sign_in, 5);
            put(Dot.home_page_other_action_coupons_center, 6);
            put(Dot.home_page_other_action_Hongbao, 7);
            //猜成语页入口位置点击
            put(Dot.idiom_page_action_cash, 1);
            put(Dot.idiom_page_action_gold, 2);
            //设置页入口位置点击
            put(Dot.user_center_action_fill_in_ic, 1);
            put(Dot.user_center_action_gold, 2);
            put(Dot.user_center_action_cash, 3);
            put(Dot.user_center_action_wm, 4);
            put(Dot.user_center_action_invite, 5);
            put(Dot.user_center_action_install, 6);
            put(Dot.user_center_action_Help_and_feedback, 7);
            //WiFi连接页-按钮
            put(Dot.wifi_connection_butoon_permissions, 1);
            put(Dot.wifi_connection_butoon_position, 2);*/
        }
    };

    /**
     * 获取指定属性的开始位置。需要上报的位置
     * @param key 表示次属性需要报到dms、dmn第几个位置
     * @return 需要报道的位置，如果为 <1 表示未找到。默认上报即可
     */
    public static int getDIBuildStartIndex(String key){
        Integer pos = DNDI_PARAMS_KEY.get(key);
        if(pos == null){
            return -1;
        }
        return pos;
    }

}
