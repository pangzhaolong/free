package com.donews.mine.views.operating;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.donews.mine.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lcl
 * Date on 2021/11/9
 * Description:
 * 个人中心 -> 运营位的自定义视图
 */
public class MineOperatingPosView extends ViewPager {

    /**
     * 页面的每项点击事件
     */
    public interface OnPageItemClick {
        /**
         * 点击监听
         *
         * @param view
         * @param item
         */
        void click(View view, Object item);
    }

    //每一页的资源id
    private int pageViewRes = R.layout.incl_mine_operating_pos_page;
    //适配器
    private MineOperatingPosVpAdapter adapter;
    private List<Object> datas = new ArrayList<>();
    //点击监听
    private OnPageItemClick itemClickListener;

    public MineOperatingPosView(Context context) {
        super(context);
        init();
    }

    public MineOperatingPosView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * 设置数据
     *
     * @param list
     */
    public void setDatas(List<Object> list) {
        datas.clear();
        datas.addAll(list);
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 设置点击监听
     *
     * @param clickListener
     */
    public void setItemClick(OnPageItemClick clickListener) {
        this.itemClickListener = clickListener;
    }

    //初始化
    private void init() {
        adapter = new MineOperatingPosVpAdapter();
        setAdapter(adapter);
    }

    /**
     * 适配器
     */
    class MineOperatingPosVpAdapter extends PagerAdapter {

        //每页大小
        private int pageSize = 4;
        //缓存视图
        private List<View> cacheViews = new ArrayList<>();

        @Override
        public int getCount() {
            return Math.round(datas.size() / (pageSize * 1F));
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View itemView;
            if (cacheViews.size() > 0) {
                itemView = cacheViews.get(cacheViews.size() - 1);
            } else {
                itemView = LayoutInflater.from(container.getContext()).inflate(
                        pageViewRes, container, false);
            }
            if (itemView != null) {
                initViewParams(itemView, position);
                container.addView(itemView, position);
            }
            return itemView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
            cacheViews.add((View) object);
            super.destroyItem(container, position, object);
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        /**
         * 初始化视图参数
         *
         * @param itemView 当前页的视图
         * @param position 当前页的下标
         */
        private void initViewParams(View itemView, int position) {
            View[] itemViews = new View[4];
            //每项数据
            itemViews[0] = itemView.findViewById(R.id.mine_operating_1_0);
            itemViews[1] = itemView.findViewById(R.id.mine_operating_1_1);
            itemViews[2] = itemView.findViewById(R.id.mine_operating_2_0);
            itemViews[3] = itemView.findViewById(R.id.mine_operating_2_1);
            int curPos = 0;
            for (int i = pageSize * position; i < pageSize * position + pageSize; i++) {
                curPos = i % pageSize;
                final int curOldPos = i;
                if (i < datas.size()) {
                    itemViews[curPos].setVisibility(View.VISIBLE);
                    bindData(itemViews[curPos], i);
                    itemViews[curPos].setOnClickListener(v -> {
                        if (MineOperatingPosView.this.itemClickListener != null &&
                                v.getVisibility() == View.VISIBLE) {
                            //点击监听
                            MineOperatingPosView.this.itemClickListener.click(v, datas.get(curOldPos));
                        }
                    });
                } else {
                    itemViews[curPos].setVisibility(View.INVISIBLE);
                }
            }
        }

        //绑定数据
        private void bindData(View view, int pos) {

        }
    }
}
