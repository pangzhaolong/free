/**
 * <p> </p>
 * 作者： created by hegai<br>
 * 日期： 2021/10/14 15:43<br>
 * 版本：V1.0<br>
 */
package com.module.lottery.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.donews.common.router.RouterActivityPath;
import com.donews.common.router.RouterFragmentPath;
import com.donews.middle.abswitch.ABSwitch;
import com.donews.utilslibrary.utils.AppInfo;
import com.donews.utilslibrary.utils.UrlUtils;
import com.module.lottery.bean.CommodityBean;
import com.module.lottery.bean.LotteryCodeBean;
import com.module.lottery.bean.MaylikeBean;
import com.module.lottery.bean.WinLotteryBean;
import com.module.lottery.ui.LotteryActivity;
import com.module.lottery.utils.ImageUtils;
import com.module.lottery.utils.ScrollLinearLayoutManager;
import com.module.lottery.utils.TopLinearSmoothScroller;
import com.module.lottery.utils.VerticalImageSpan;
import com.module_lottery.BuildConfig;
import com.module_lottery.R;
import com.module_lottery.databinding.GuesslikeHeadLayoutBinding;
import com.module_lottery.databinding.GuesslikeItemLayoutBinding;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;


public class GuessAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final String TAG = "GuessAdapter";
    public static final int TYPE_HEADER = 0;  //说明是带有Header的
    public static final int TYPE_FOOTER = 1;  //说明是带有Footer的
    public static final int TYPE_NORMAL = 2;  //说明是不带有header和footer的
    private GuesslikeHeadLayoutBinding mHeaderView;
    private CommodityBean mCommodityBean;
    private LotteryActivity mContext;
    private int mLayoutId;
    private int flag = 0;
    ScrollListAdapter mScrollListAdapter;

    public GuessAdapter(LotteryActivity context) {
        this.mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mHeaderView != null && viewType == TYPE_HEADER) {
            return new ListHolder(mHeaderView);
        }
        GuesslikeItemLayoutBinding guesslikeItemLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), mLayoutId, parent, false);
        GuessViewHolder holder = new GuessViewHolder(guesslikeItemLayoutBinding);
        return holder;
    }

    public void getLayout(int layoutId) {
        this.mLayoutId = layoutId;
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
    }


    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (mHeaderView != null && position == TYPE_HEADER) {
            if (holder instanceof ListHolder && mCommodityBean != null) {
                ListHolder listHolder = ((ListHolder) (holder));
                //价格
                listHolder.mGuesslikeHeadBinding.price.setText(ABSwitch.Ins().getLotteryPriceShow() + "");
                listHolder.mGuesslikeHeadBinding.postage.setText(ABSwitch.Ins().getLotteryPriceShow() + "元包邮领奖");
                //参考价格
                listHolder.mGuesslikeHeadBinding.referPrice.setText("参考价: " + mCommodityBean.getOriginalPrice() + "");
                listHolder.mGuesslikeHeadBinding.referPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
                int w = mContext.getResources().getDimensionPixelOffset(R.dimen.lottery_constant_144);
                int h = mContext.getResources().getDimensionPixelOffset(R.dimen.lottery_constant_52);

                setTextImage(w, h, listHolder.mGuesslikeHeadBinding.title, mCommodityBean.getTitle(), R.mipmap.free_panic_buying);

                listHolder.mGuesslikeHeadBinding.cycle.setText("第" + mCommodityBean.getPeriod() + "期");
                initViewPager(listHolder);


                listHolder.mGuesslikeHeadBinding.lotteryContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mContext != null) {
                            mContext.luckyDrawEntrance();
                        }
                    }
                });
                //设置中奖参与人数的滚动列表
                if (mScrollListAdapter == null) {
                    mScrollListAdapter = new ScrollListAdapter(mContext);
                }
                listHolder.mGuesslikeHeadBinding.scrollList.setLayoutManager(new ScrollLinearLayoutManager(mContext));
                listHolder.mGuesslikeHeadBinding.scrollList.setAdapter(mScrollListAdapter);
                listHolder.mGuesslikeHeadBinding.scrollList.setAutomaticScroll(true);
                listHolder.mGuesslikeHeadBinding.scrollList.start();
                //初始化参与者信息
                if (mCommodityBean.getParticipateBean() != null) {
                    if (mCommodityBean.getParticipateBean().getList().size() > 4) {
                        ImageUtils.setImage(mContext, listHolder.mGuesslikeHeadBinding.participateAvatarOne, mCommodityBean.getParticipateBean().getList().get(0).getAvatar(), 360);
                        ImageUtils.setImage(mContext, listHolder.mGuesslikeHeadBinding.participateAvatarTwo, mCommodityBean.getParticipateBean().getList().get(1).getAvatar(), 360);
                        ImageUtils.setImage(mContext, listHolder.mGuesslikeHeadBinding.participateAvatarThree, mCommodityBean.getParticipateBean().getList().get(2).getAvatar(), 360);
                        ImageUtils.setImage(mContext, listHolder.mGuesslikeHeadBinding.participateAvatarFor, mCommodityBean.getParticipateBean().getList().get(3).getAvatar(), 360);
                    } else {
                        Log.d(TAG, "头像数量不满足");
                    }
                    listHolder.mGuesslikeHeadBinding.number.setText(mCommodityBean.getParticipateBean().getTotal());
                }
                boolean logType = AppInfo.checkIsWXLogin();
                if (!logType) {
                    listHolder.mGuesslikeHeadBinding.lotteryCodeTitle.setVisibility(View.GONE);
                    listHolder.mGuesslikeHeadBinding.lotteryContainer.setVisibility(View.GONE);
                    listHolder.mGuesslikeHeadBinding.barLayout.setVisibility(View.GONE);
                } else {
                    if (mCommodityBean.getLotteryCodeBean() != null) {
                        listHolder.mGuesslikeHeadBinding.lotteryCodeTitle.setVisibility(View.VISIBLE);
                        listHolder.mGuesslikeHeadBinding.lotteryContainer.setVisibility(View.VISIBLE);
                        listHolder.mGuesslikeHeadBinding.barLayout.setVisibility(View.VISIBLE);
                        //初始化获取的抽奖码列表
                        initListLottery(listHolder.mGuesslikeHeadBinding, mCommodityBean.getLotteryCodeBean());
                    }
                }
                listHolder.mGuesslikeHeadBinding.raiders.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ARouter.getInstance().build(RouterActivityPath.Web.PAGER_WEB_ACTIVITY).withString("url", BuildConfig.WEB_BASE_URL + "rule").navigation();
                    }
                });

            }


        } else {
            if (holder instanceof GuessViewHolder) {
                GuessViewHolder guessViewHolder = ((GuessViewHolder) (holder));
                String imageUrl = mCommodityBean.getGuessLikeData().get(position - 1).getMainPic();
                imageUrl = UrlUtils.formatUrlPrefix(imageUrl);
                RoundedCorners roundedCorners = new RoundedCorners(5);

                RequestOptions options = RequestOptions.bitmapTransform(roundedCorners);
                Glide.with(mContext).load(imageUrl).apply(options).into(guessViewHolder.mGuesslikeItemLayoutBinding.itemImageSrc);
                int w = mContext.getResources().getDimensionPixelOffset(R.dimen.merchant_icon_w);
                int h = mContext.getResources().getDimensionPixelOffset(R.dimen.merchant_icon_h);

                setTextImage(w, h, guessViewHolder.mGuesslikeItemLayoutBinding.itemTitle, mCommodityBean.getGuessLikeData().get(position - 1).getTitle() + "", R.mipmap.taobao_icon);
                guessViewHolder.mGuesslikeItemLayoutBinding.itemPrice.setText("¥ " + mCommodityBean.getGuessLikeData().get(position - 1).getOriginalPrice() + "");
                guessViewHolder.mGuesslikeItemLayoutBinding.itemPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                guessViewHolder.mGuesslikeItemLayoutBinding.itemLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ARouter.getInstance()
                                .build(RouterFragmentPath.Lottery.PAGER_LOTTERY).withString("goods_id", mCommodityBean.getGuessLikeData().get(position - 1).getGoodsId()).withString("action", "newAction")
                                .navigation();
                    }
                });
            }
        }
    }


    public void setScrollListData(WinLotteryBean winLotteryBean) {
        if (mScrollListAdapter != null && winLotteryBean != null) {
            mScrollListAdapter.setList(winLotteryBean.getList());
            mScrollListAdapter.notifyDataSetChanged();
        }
    }


    private void setTextImage(int w, int h, TextView textView, String value, int id) {
        if ((textView != null) && (mContext != null)) {
            SpannableString spannableString = new SpannableString("  " + value);
            Drawable drawable = mContext.getResources().getDrawable(id);
            drawable.setBounds(0, 0, w, h);
            spannableString.setSpan(new VerticalImageSpan(drawable), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView.setText(spannableString);
        }
    }


    private static void setImage(Context context, ImageView view, String src, int roundingRadius) {
        src = UrlUtils.formatUrlPrefix(src);
        RoundedCorners roundedCorners = new RoundedCorners(roundingRadius);
        RequestOptions options = RequestOptions.bitmapTransform(roundedCorners);
        Glide.with(context).load(src).apply(options).into(view);
    }


    class GuessViewHolder extends RecyclerView.ViewHolder {
        GuesslikeItemLayoutBinding mGuesslikeItemLayoutBinding;

        public GuessViewHolder(@NonNull GuesslikeItemLayoutBinding guesslikeItemLayoutBinding) {
            super(guesslikeItemLayoutBinding.getRoot());
            this.mGuesslikeItemLayoutBinding = guesslikeItemLayoutBinding;

        }
    }

    //在这里面加载ListView中的每个item的布局
    class ListHolder extends RecyclerView.ViewHolder {
        GuesslikeHeadLayoutBinding mGuesslikeHeadBinding;

        public ListHolder(GuesslikeHeadLayoutBinding guesslikeHeadLayoutBinding) {
            super(guesslikeHeadLayoutBinding.getRoot());
            this.mGuesslikeHeadBinding = guesslikeHeadLayoutBinding;
        }
    }

    //初始化详情页面顶部的ViewPager
    private void initViewPager(ListHolder listHolder) {
        if ((mCommodityBean != null) && (mCommodityBean.getPics() != null)) {
            if (mCommodityBean.getPics().size() == 0) {
                mCommodityBean.getPics().add(mCommodityBean.getMainPic());
            }
            flag = 0;
            //将imageView图片资源存在集合中
            List list = new ArrayList<ImageView>();
            for (int i = 0; i < mCommodityBean.getPics().size(); i++) {
                ImageView imageView = new ImageView(mContext);
                imageView.setTag(mCommodityBean.getPics().get(i));
                list.add(imageView);
            }
            ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter();
            if (list != null && list.size() > 0) {
                viewPagerAdapter.setDataList(list);
                listHolder.mGuesslikeHeadBinding.headPager.setAdapter(viewPagerAdapter);
                listHolder.mGuesslikeHeadBinding.pointLayout.removeAllViews();
                //动态创建小点点
                if (list.size() > 1) {
                    for (int i = 0; i < mCommodityBean.getPics().size(); i++) {
                        LinearLayout view = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.point_layout, null);
                        listHolder.mGuesslikeHeadBinding.pointLayout.addView(view);
                    }
                    //默认第一张 所有第一个点点为红色
                    View view = listHolder.mGuesslikeHeadBinding.pointLayout.getChildAt(0);
                    if (view != null && view instanceof LinearLayout) {
                        ((LinearLayout) (view)).getChildAt(0).setSelected(true);
                    }
                    listHolder.mGuesslikeHeadBinding.headPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                        }

                        @Override
                        public void onPageSelected(int position) {
                            try {
                                View childView = listHolder.mGuesslikeHeadBinding.pointLayout.getChildAt(position);
                                View selectView = listHolder.mGuesslikeHeadBinding.pointLayout.getChildAt(flag);
                                if (childView != null && childView instanceof LinearLayout) {
                                    ((LinearLayout) (selectView)).getChildAt(0).setSelected(false);
                                    ((LinearLayout) (childView)).getChildAt(0).setSelected(true);
                                }
                                flag = position;//存一下坐标(表示上一次点击时候的坐标)

                            } catch (Exception e) {
                                Logger.d(e + "");
                            }

                        }

                        @Override
                        public void onPageScrollStateChanged(int state) {

                        }
                    });
                }


            }
        }
    }

    //初始化抽奖码
    public void initListLottery(GuesslikeHeadLayoutBinding guessLikeHead, LotteryCodeBean lotteryCodeBean) {

        try {
            //获取自选码的为位置
            int local = ABSwitch.Ins().getSelectNumberLocation();
            if (guessLikeHead != null) {
                int refer = 0;
                for (int i = 0; i < guessLikeHead.lotteryContainer.getChildCount(); i++) {
                    LinearLayout linearLayout = (LinearLayout) guessLikeHead.lotteryContainer.getChildAt(i);
                    for (int j = 0; j < linearLayout.getChildCount(); j++) {
                        if (refer >= lotteryCodeBean.getCodes().size()) {
                            TextView textView = (TextView) linearLayout.getChildAt(j);
                            textView.setTextColor(mContext.getResources().getColor(R.color.pending));
                            TextPaint paint = textView.getPaint();
                            if (local == (refer + 1)) {
                                textView.setText("自选码");
                                textView.setTextColor(Color.WHITE);
                                textView.setBackgroundDrawable(mContext.getResources().getDrawable(R.mipmap.optional_code_bg));
                            } else {
                                textView.setText("待领取");
                                textView.setBackgroundDrawable(mContext.getResources().getDrawable(R.mipmap.no_lottery_code_bg));
                            }
                            paint.setFakeBoldText(false);
                        } else {
                            TextView textView = (TextView) linearLayout.getChildAt(j);
                            textView.setText(lotteryCodeBean.getCodes().get(refer));
                            textView.setTextColor(mContext.getResources().getColor(R.color.lottery_code));

                            textView.setBackgroundDrawable(mContext.getResources().getDrawable(R.mipmap.select_code_bg));
                            TextPaint paint = textView.getPaint();
                            paint.setFakeBoldText(true);
                        }
                        refer++;
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    //设置item的,类型
    @Override
    public int getItemViewType(int position) {
        if (mHeaderView == null) {
            return TYPE_NORMAL;
        }
        if (position == 0) {
            //第一个item应该加载Header
            return TYPE_HEADER;
        }
        if (position == getItemCount() - 1) {
            //最后一个,应该加载Footer
            return TYPE_FOOTER;
        }
        return TYPE_NORMAL;
    }

    //设置顶部VIew
    public void setHeaderView(GuesslikeHeadLayoutBinding headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }

    //刷新设置底部商品数据来源
    public void setGuessLikeData(List<MaylikeBean.ListDTO> guessLikeData) {
        if (mCommodityBean.getGuessLikeData() != null) {
            mCommodityBean.getGuessLikeData().clear();
        }
        this.mCommodityBean.setGuessLikeData(guessLikeData);
    }

    //上拉加载设置底部商品数据来源
    public void addGuessLikeData(List<MaylikeBean.ListDTO> guessLikeData) {
        if (mCommodityBean != null && guessLikeData != null) {
            if (mCommodityBean.getGuessLikeData() == null) {
                this.mCommodityBean.setGuessLikeData(guessLikeData);
            } else {
                mCommodityBean.getGuessLikeData().addAll(guessLikeData);
            }
        }
    }

    //设置列表的个数，商品详情页面整体是个listView
    @Override
    public int getItemCount() {
        if (mCommodityBean != null && mCommodityBean.getGuessLikeData() != null) {
            if (mHeaderView == null) {
                return mCommodityBean.getGuessLikeData().size();
            } else if (mHeaderView != null) {
                return mCommodityBean.getGuessLikeData().size() + 1;
            }
        } else if (mCommodityBean != null && mCommodityBean.getGuessLikeData() == null) {
            return 1;
        }
        return 0;
    }

    public CommodityBean getCommodityBean() {
        return mCommodityBean;
    }

    public void setCommodityBean(CommodityBean mCommodityBean) {
        this.mCommodityBean = mCommodityBean;
    }


}
