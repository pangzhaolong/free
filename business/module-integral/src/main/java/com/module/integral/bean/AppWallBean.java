package com.module.integral.bean;

import com.donews.common.contract.BaseCustomViewModel;

import java.util.List;

/**
 * @author lcl
 * Date on 2021/12/29
 * Description:
 */
public class AppWallBean extends BaseCustomViewModel {
    public List<AppWallBeanItem> apps;

    /**
     * 详细的实体Item
     */
    public static class AppWallBeanItem extends BaseCustomViewModel {
        public String id;
        public String title;
        public String action;
        public String desc;
        public String linkUrl;
        public String icon;
        public String pkgName;
    }
}

