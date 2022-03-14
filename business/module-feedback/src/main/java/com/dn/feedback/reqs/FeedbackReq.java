package com.dn.feedback.reqs;

import com.blankj.utilcode.util.AppUtils;
import com.donews.common.contract.BaseCustomViewModel;

import java.util.List;

/**
 * @author lcl
 * Date on 2022/2/9
 * Description:
 */
public class FeedbackReq extends BaseCustomViewModel {
    /**
     * 用户id
     */
    public Long user_id;
    /**
     * 包名称
     */
    public String package_name = AppUtils.getAppPackageName();
    /**
     * 反馈类型
     */
    public List<String> types;
    /**
     * 手机号
     */
    public String mobile;
    /**
     * 反馈内容
     */
    public String content;
    /**
     * 附件的地址集合
     */
    public List<String> attachments;
}
