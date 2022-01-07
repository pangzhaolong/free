package com.donews.notify.launcher.utils.fix;

import com.donews.common.contract.LoginHelp;
import com.donews.common.contract.WeChatBean;
import com.donews.notify.launcher.configs.baens.Notify2DataConfigBean;
import com.donews.utilslibrary.utils.LogUtil;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lcl
 * Date on 2022/1/6
 * Description:
 * 对内容的标签进行处理。对配置中的标签进行处理：支持标签如下：
 * ${xxx}      :表示再当前对象中使用 xxx 字段进行标签替换
 * ${wx_name}  :表示使用微信的名称进行标签替换
 * ${wx_head}  :表示使用微信头像进行标签替换(只针对icon类型字段)
 */
public class FixTagUtils {
    /**
     * 标记的开始记号
     */
    private static final String startFlg = "${";
    /**
     * 标记的结束记号
     */
    private static final String endFlg = "}";
    /**
     * 本地标签 ${wx_name} 出错的默认填充内容
     */
    private static final String defaultFillWxName = "奖多多";

    /**
     * 本地标签的处理逻辑
     */
    private static final Map<String, ILocalFixTagTask> localFlgTask = new HashMap() {
        {
            put("${wx_name}", (ILocalFixTagTask) () -> { //注册[微信名称标签]的处理逻辑
                WeChatBean wxBean = LoginHelp.getInstance().getUserInfoBean().getWechatExtra();
                if (wxBean == null) {
                    return defaultFillWxName;
                }
                String wxName = wxBean.getNickName();
                if (wxName == null) {
                    return defaultFillWxName;
                }
                return wxName;
            });
            put("${wx_head}", (ILocalFixTagTask) () -> { //注册[微信头像标签]的处理逻辑
                WeChatBean wxBean = LoginHelp.getInstance().getUserInfoBean().getWechatExtra();
                if (wxBean == null) {
                    return "";
                }
                String wxHead = wxBean.getHeadimgurl();
                if (wxHead == null) {
                    return "";
                }
                return wxHead;
            });
        }
    };

    /**
     * 处理内容标签，将内容的相关标签处理为内容
     *
     * @param obj           字段所在的对象
     * @param targetContent 目标内容
     * @return
     */
    public static String buildContentTag(Notify2DataConfigBean.UiTemplat obj, String targetContent) {
        try {
            if (targetContent == null ||
                    targetContent.isEmpty() ||
                    targetContent.length() < startFlg.length() + endFlg.length() + 1) {
                return targetContent;
            }
            Map<String, String> tagMap = getStartAndEndFlgIndex(targetContent);
            Map<String, String> tagValueMap = new HashMap<>();
            //处理标签的值
            for (String key : tagMap.keySet()) {
                try {
                    String fName = tagMap.get(key);
                    if (fName == null || fName.contains("_")) {
                        //不是字段类数据标签。使用本地类型标签处理
                        ILocalFixTagTask localFix = localFlgTask.get(key);
                        if (localFix == null) {
                            LogUtil.e("notify 暂不支持的标签：" + key);
                            continue;
                        }
                        tagValueMap.put(key, localFix.getTagValue());
                        continue;
                    }
                    Field field;
                    try {
                        field = Notify2DataConfigBean.UiTemplat.class.getField(fName);
                    } catch (Exception ef) {
                        field = Notify2DataConfigBean.UiTemplat.class.getDeclaredField(fName);
                    }
                    field.setAccessible(true);
                    Object fValue = field.get(obj);
                    if (fValue != null) {
                        tagValueMap.put(key, (String) field.get(obj));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //处理微信相关的标签内容,替换标签相关的值
            StringBuffer sb = new StringBuffer(targetContent);
            for (String key : tagValueMap.keySet()) {
                String v = tagValueMap.get(key);
                String temTagContent = sb.toString();
                sb.delete(0, sb.length());
                if (v != null) {
                    sb.append(temTagContent.replace(key, v));
                } else {
                    sb.append(temTagContent.replace(key, ""));
                }
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return targetContent;
        }
    }

    /**
     * 获取指定内容中的标签开始和结束标记的位置集合
     *
     * @param content 需要查找标记的内容
     * @return key:带上标记的全名称(例如:${xxx})，value:去掉前后标记的tag名称(例如：xxx)
     */
    private static Map<String, String> getStartAndEndFlgIndex(String content) {
        Map<String, String> flgMap = new HashMap<>();
        if (content == null || content.length() < startFlg.length() + endFlg.length() + 1) {
            return flgMap; //都筹不够一个标记的长度
        }
        int fastIndex = content.indexOf(startFlg);
        int fastEndFlgIndex = 0;
        while (fastIndex != -1) {
            fastEndFlgIndex = content.indexOf(endFlg, fastIndex);
            if (fastEndFlgIndex == -1) {
                fastIndex += startFlg.length();
            } else {
                String tag = content.substring(fastIndex, fastEndFlgIndex + 1);
                if (tag.length() >= 4 && flgMap.get(tag) == null) {
                    //添加标记
                    flgMap.put(tag,
                            tag.substring(startFlg.length(), tag.length() - endFlg.length()));
                }
                fastIndex = fastEndFlgIndex;
            }
            //更新开始下标。寻找下一个开始坐标
            fastIndex = content.indexOf(startFlg, fastIndex);
        }
        return flgMap;
    }

}
