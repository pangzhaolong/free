package com.donews.middle.bean.home;

import com.donews.common.contract.BaseCustomViewModel;

import java.io.Serializable;
import java.util.List;

public class HomeCategoryBean extends BaseCustomViewModel {
    private List<CategoryItem> list;

    public List<CategoryItem> getList() {
        return list;
    }

    public void setList(List<CategoryItem> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "CategoryBean{" +
                "list=" + list +
                '}';
    }

    public static class CategoryItem extends BaseCustomViewModel {
        String cid;
        String cname;
        String cpic;
//        String subcategories;

        public String getCid() {
            return cid;
        }

        public String getCname() {
            return cname;
        }

        public String getCpic() {
            return cpic;
        }
//
//        public String getSubcategories() {
//            return subcategories;
//        }

        public void setCid(String cid) {
            this.cid = cid;
        }

        public void setCname(String cname) {
            this.cname = cname;
        }

        public void setCpic(String cpic) {
            this.cpic = cpic;
        }
//
//        public void setSubcategories(String subcategories) {
//            this.subcategories = subcategories;
//        }

        @Override
        public String toString() {
            return "CategoryItem{" +
                    "cid='" + cid + '\'' +
                    ", cname='" + cname + '\'' +
                    ", cpic='" + cpic + '\'' +
//                    ", subcategories='" + subcategories + '\'' +
                    '}';
        }
    }


}
