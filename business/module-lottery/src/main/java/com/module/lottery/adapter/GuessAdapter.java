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
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.donews.common.router.RouterActivityPath;
import com.donews.common.router.RouterFragmentPath;
import com.donews.utilslibrary.utils.AppInfo;
import com.donews.utilslibrary.utils.UrlUtils;
import com.module.lottery.bean.CommodityBean;
import com.module.lottery.bean.LotteryCodeBean;
import com.module.lottery.bean.MaylikeBean;
import com.module.lottery.utils.ImageUtils;
import com.module_lottery.BuildConfig;
import com.module_lottery.R;
import com.module_lottery.databinding.GuesslikeHeadLayoutBinding;
import com.module_lottery.databinding.GuesslikeItemLayoutBinding;

import java.util.List;


public class GuessAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final String TAG = "GuessAdapter";
    public static final int TYPE_HEADER = 0;  //说明是带有Header的
    public static final int TYPE_FOOTER = 1;  //说明是带有Footer的
    public static final int TYPE_NORMAL = 2;  //说明是不带有header和footer的
    private GuesslikeHeadLayoutBinding mHeaderView;


    private CommodityBean mCommodityBean;
    private Context mContext;
    private int mLayoutId;

    public GuessAdapter(Context context) {
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



    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (mHeaderView != null && position == TYPE_HEADER) {
            if (holder instanceof ListHolder && mCommodityBean != null) {
                ListHolder listHolder = ((ListHolder) (holder));
                ImageUtils.setImage(mContext, listHolder.mGuesslikeHeadBinding.guessLikeHeadImg, mCommodityBean.getMainPic(), 5);
                //价格
                listHolder.mGuesslikeHeadBinding.price.setText(mCommodityBean.getDisplayPrice() + "");
                //参考价格
                listHolder.mGuesslikeHeadBinding.referPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
                listHolder.mGuesslikeHeadBinding.referPrice.setText("参考价值: " + mCommodityBean.getOriginalPrice() + "");
                listHolder.mGuesslikeHeadBinding.title.setText(mCommodityBean.getTitle());
                listHolder.mGuesslikeHeadBinding.cycle.setText("第" + mCommodityBean.getPeriod() + "期");
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
                    listHolder.mGuesslikeHeadBinding.number.setText("累计" + mCommodityBean.getParticipateBean().getTotal() + "人参与抽奖");
                }
                boolean logType = AppInfo.checkIsWXLogin();
                if (!logType) {
                    listHolder.mGuesslikeHeadBinding.lotteryCodeTitle.setVisibility(View.GONE);
                    listHolder.mGuesslikeHeadBinding.lotteryContainer.setVisibility(View.GONE);
                } else {
                    if (mCommodityBean.getLotteryCodeBean() != null) {
                        listHolder.mGuesslikeHeadBinding.lotteryCodeTitle.setVisibility(View.VISIBLE);
                        listHolder.mGuesslikeHeadBinding.lotteryContainer.setVisibility(View.VISIBLE);
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
                guessViewHolder.mGuesslikeItemLayoutBinding.itemTitle.setText(mCommodityBean.getGuessLikeData().get(position - 1).getTitle());
                guessViewHolder.mGuesslikeItemLayoutBinding.itemPrice.setText("¥ " + mCommodityBean.getGuessLikeData().get(position - 1).getOriginalPrice() + "");
                guessViewHolder.mGuesslikeItemLayoutBinding.itemPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                guessViewHolder.mGuesslikeItemLayoutBinding.lottery.setOnClickListener(new View.OnClickListener() {
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


    public void initListLottery(GuesslikeHeadLayoutBinding guessLikeHead, LotteryCodeBean lotteryCodeBean) {
        if (guessLikeHead != null) {
            int refer = 0;
            for (int i = 0; i < guessLikeHead.lotteryContainer.getChildCount(); i++) {
                LinearLayout linearLayout = (LinearLayout) guessLikeHead.lotteryContainer.getChildAt(i);
                for (int j = 0; j < linearLayout.getChildCount(); j++) {
                    if (refer >= lotteryCodeBean.getCodes().size()) {
                        TextView textView = (TextView) linearLayout.getChildAt(j);
                        textView.setText("待领取");
                        textView.setTextColor(mContext.getResources().getColor(R.color.pending));
                        TextPaint paint = textView.getPaint();
                        textView.setBackgroundDrawable(mContext.getResources().getDrawable(R.mipmap.no_lottery_code_bg));
                        paint.setFakeBoldText(false);
                        continue;
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
    }


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

    public void setHeaderView(GuesslikeHeadLayoutBinding headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }

    public void setGuessLikeData(List<MaylikeBean.ListDTO> guessLikeData) {
        if (mCommodityBean.getGuessLikeData() != null) {
            mCommodityBean.getGuessLikeData().clear();
        }
        this.mCommodityBean.setGuessLikeData(guessLikeData);
    }


    public void addGuessLikeData(List<MaylikeBean.ListDTO> guessLikeData) {
        if (mCommodityBean.getGuessLikeData() != null && guessLikeData != null) {
            mCommodityBean.getGuessLikeData().addAll(guessLikeData);
        }


    }

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
