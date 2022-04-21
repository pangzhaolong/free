package com.donews.mine.views.operating;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableRow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.airbnb.lottie.LottieAnimationView;
import com.blankj.utilcode.util.ConvertUtils;
import com.donews.mine.R;
import com.donews.mine.utils.GlideUtils;
import com.donews.utilslibrary.analysis.AnalysisUtils;
import com.donews.utilslibrary.dot.Dot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lcl
 * Date on 2021/11/9
 * Description:
 * 个人中心 -> 运营位的自定义视图
 */
public class MineOperatingPosView extends ViewPager {

    /**
     * 数据项的接口。为了规范数据结构
     */
    public interface IOperatingData {
        /**
         * 获取显示的Icon的地址
         *
         * @return 数字字符：表示是本地res资源id
         * 其他表示：是一个文件或者URL地址
         */
        String getIconUrl();

        /**
         * 当这个数据项目被点击之后的操作
         *
         * @param view 视图对象
         * @param data 当前点击的数据实体
         * @return
         */
        void onClick(View view, IOperatingData data);
    }

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
    private List<IOperatingData> datas = new ArrayList<>();
    private int baseHei = 0;
    private int minRowHei = 0;

    public MineOperatingPosView(Context context) {
        super(context);
        init();
    }

    public MineOperatingPosView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (baseHei == 0) {
            baseHei = h;
            minRowHei = (int) getResources().getDimension(R.dimen.mine_operating_hei);
            ViewGroup.LayoutParams lp = MineOperatingPosView.this.getLayoutParams();
            if (lp != null && datas.isEmpty() && lp.height != getRowHei()) {
                MineOperatingPosView.this.getLayoutParams().height = getRowHei();
                MineOperatingPosView.this.setLayoutParams(lp);
            }
        }
    }

    //获取行高
    private int getRowHei() {
        int pyl = ConvertUtils.dp2px(21F);
        if (baseHei / 2 < minRowHei + pyl) {
            return minRowHei + pyl;
        } else {
            return baseHei / 2;
        }
    }

    /**
     * 设置数据
     *
     * @param list
     */
    public void setDatas(List<IOperatingData> list) {
        datas.clear();
        datas.addAll(list);
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    //初始化
    private void init() {
        addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                adapter.initViewParams(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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
        //curr视图
        private Map<Integer, View> hyViews = new HashMap<>();

        @Override
        public int getCount() {
            return (int) Math.ceil(datas.size() / (pageSize * 1F));
        }

        @Override
        public int getItemPosition(Object object) {
            initViewParams(getCurrentItem());
            return super.getItemPosition(object);
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
                hyViews.put(position, itemView);
                initViewParams(position);
                container.addView(itemView, position);

//                AnalysisUtils.onEventEx(container.getContext(), Dot.MINE_YYW_SHOW, "" + position);
            }
            return itemView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            hyViews.remove(position);
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
         * @param position 当前页的下标
         */
        private void initViewParams(int position) {
            View itemView = hyViews.get(position);
            ViewGroup.LayoutParams lp = MineOperatingPosView.this.getLayoutParams();
            if (itemView == null) {
                return;
            }
            View[] itemViews = new View[4];
            TableRow row2 = itemView.findViewById(R.id.mine_yyw_row2);
            //每项数据
            itemViews[0] = itemView.findViewById(R.id.mine_operating_1_0);
            itemViews[1] = itemView.findViewById(R.id.mine_operating_1_1);
            itemViews[2] = itemView.findViewById(R.id.mine_operating_2_0);
            itemViews[3] = itemView.findViewById(R.id.mine_operating_2_1);
            int curPos = 0;
            int startPos = pageSize * position;
            int endPos = pageSize * position + pageSize;
            if (endPos > datas.size() && datas.size() - startPos <= 2) {
                //当前页不足2条数据。那么彻底隐藏视图
                row2.setVisibility(View.GONE);
                if (position == getCurrentItem() && lp.height != getRowHei()) {
                    startAnim(lp.height, getRowHei(), lp);
                }
                //按钮的动画
                LottieAnimationView lottieAnimationView = itemView.findViewById(R.id.la_but_anim);
                lottieAnimationView.setImageAssetsFolder("images");
                lottieAnimationView.setAnimation("mine_liji_but.json");
                lottieAnimationView.loop(true);
                lottieAnimationView.playAnimation();
            } else {
                row2.setVisibility(View.VISIBLE);
                if (position == getCurrentItem() && lp.height != baseHei) {
                    startAnim(lp.height, baseHei, lp);
                }
            }
            for (int i = startPos; i < endPos; i++) {
                curPos = i % pageSize;
                final int curOldPos = i;
                if (i < datas.size()) {
                    itemViews[curPos].setVisibility(View.VISIBLE);
                    bindData(itemViews[curPos], i);
                    itemViews[curPos].setOnClickListener(v -> {
                        if (v.getVisibility() == View.VISIBLE) {
                            //点击监听
                            datas.get(curOldPos).onClick(v, datas.get(curOldPos));
                        }
                    });
                } else {
                    itemViews[curPos].setVisibility(View.INVISIBLE);
                }
            }
        }

        //绑定数据
        private void bindData(View view, int pos) {
            IOperatingData data = datas.get(pos);
            if (!(view instanceof ViewGroup)) {
                return;
            }
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                if ("icon".equals(((ViewGroup) view).getChildAt(i).getTag())) {
                    ImageView icon = (ImageView) ((ViewGroup) view).getChildAt(i);
                    if (data.getIconUrl() != null && data.getIconUrl().length() > 0) {
                        try {
                            int res = Integer.parseInt(data.getIconUrl());
                            icon.setImageResource(res);
//                            Glide.with(getContext())
//                                    .load(res)
//                                    .into(icon);
                        } catch (Exception e) {
                            if (data.getIconUrl().endsWith(".gif")) {
                                GlideUtils.INSTANCE.glideRectCircleOssWGif(
                                        getContext(), "", data.getIconUrl(), 1,
                                        icon.getWidth(), icon, R.mipmap.loading_dialog
                                );
                            } else {
                                com.donews.base.utils.glide.GlideUtils.loadImageView(
                                        getContext(), data.getIconUrl(), icon);
                            }
                        }
                    }
                }
            }
        }

        ValueAnimator valueAnimator;

        //开始动画
        private void startAnim(int startHei, int endHei, ViewGroup.LayoutParams lp) {
            if (valueAnimator != null) {
                valueAnimator.cancel();
            }
            int chazhi = getRowHei();
            float offset = Math.abs(endHei - startHei) / (chazhi * 1F);
            valueAnimator = ValueAnimator.ofInt(startHei, endHei);
            //根据实际的距离来计算时间
            valueAnimator.setDuration((long) (250 * offset));
            valueAnimator.addUpdateListener(animation -> {
                lp.height = (int) animation.getAnimatedValue();
                MineOperatingPosView.this.setLayoutParams(lp);
            });
            valueAnimator.start();
        }
    }
}
