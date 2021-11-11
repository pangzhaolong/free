//package com.module.lottery.adapter;
//
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//
//import androidx.annotation.NonNull;
//import androidx.viewpager.widget.PagerAdapter;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class ViewPagerAdapter extends PagerAdapter {
//
//
//    public List<ImageView> getDataList() {
//        return dataList;
//    }
//
//    public void setDataList(List<ImageView> dataList) {
//        this.dataList = dataList;
//    }
//
//    List<ImageView> dataList=new ArrayList<>();
//
//    @Override
//    public int getCount() {
//        return Integer.MAX_VALUE;
//    }
//
//    @Override
//    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
//        return view==object;
//    }
//
//    @Override
//    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//        container.removeView(dataList.get(position%dataList.size()));
//    }
//
//    @NonNull
//    @Override
//    public Object instantiateItem(@NonNull ViewGroup container, int position) {
//        // TODO Auto-generated method stub
//        final ImageView imageView = dataList.get(position % dataList.size());
//        container.addView(dataList.get(position%dataList.size()),0);
//        return dataList.get(position%dataList.size());
//    }
//}
