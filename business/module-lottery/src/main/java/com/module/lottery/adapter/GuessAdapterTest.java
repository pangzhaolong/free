/**
 * <p> </p>
 * 作者： created by hegai<br>
 * 日期： 2021/10/14 15:43<br>
 * 版本：V1.0<br>
 */
package com.module.lottery.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.donews.utilslibrary.utils.UrlUtils;
import com.module.lottery.bean.CommodityBean;
import com.module.lottery.bean.LotteryCodeBean;
import com.module.lottery.bean.MaylikeBean;
import com.module_lottery.databinding.GuesslikeHeadLayoutBinding;
import com.module_lottery.databinding.GuesslikeItemLayoutBinding;

import java.util.ArrayList;
import java.util.List;


public class GuessAdapterTest extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    public static final int TYPE_HEADER = 0;  //说明是带有Header的
    public static final int TYPE_FOOTER = 1;  //说明是带有Footer的
    public static final int TYPE_NORMAL = 2;  //说明是不带有header和footer的
    private GuesslikeHeadLayoutBinding mHeaderView;
    private View mFooterView;


    private CommodityBean mCommodityBean;
    private Context mContext;
    private int mLayoutId;
    private List<MaylikeBean.ListDTO> mGuessLikeData = new ArrayList<>();

    public GuessAdapterTest(Context context, CommodityBean commodityBean) {
        mCommodityBean = commodityBean;
        this.mContext = context.getApplicationContext();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mHeaderView != null && viewType == TYPE_HEADER) {
            return new ListHolder(mHeaderView);
        }
        if (mFooterView != null && viewType == TYPE_FOOTER) {
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
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (mHeaderView != null && position == TYPE_HEADER) {
            if (holder instanceof ListHolder && mCommodityBean != null) {
                ListHolder listHolder = ((ListHolder) (holder));
                String imageUrl = mCommodityBean.getMainPic();
                imageUrl = UrlUtils.formatUrlPrefix(imageUrl);
                RoundedCorners roundedCorners = new RoundedCorners(5);
                RequestOptions options = RequestOptions.bitmapTransform(roundedCorners);
                Glide.with(mContext).load(imageUrl).apply(options).into(listHolder.mGuesslikeHeadBinding.guessLikeHeadImg);
                //价格
                listHolder.mGuesslikeHeadBinding.price.setText(mCommodityBean.getDisplayPrice() + "");
                //参考价格
                listHolder.mGuesslikeHeadBinding.referPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
                listHolder.mGuesslikeHeadBinding.referPrice.setText("参考价值: " + mCommodityBean.getOriginalPrice() + "");
                listHolder.mGuesslikeHeadBinding.title.setText(mCommodityBean.getTitle());
                listHolder.mGuesslikeHeadBinding.cycle.setText("第" + mCommodityBean.getPeriod() + "期");

                if (mCommodityBean.getParticipateBean() != null) {
                    listHolder.mGuesslikeHeadBinding.number.setText(mCommodityBean.getParticipateBean().getTotal());
                }
                if (mCommodityBean.getLotteryCodeBean() != null) {

                    initListLottery(listHolder.mGuesslikeHeadBinding, mCommodityBean.getLotteryCodeBean());


                    Log.d("", "");
                    Log.d("", "");
                    Log.d("", "");
                    Log.d("", "");
                    Log.d("", "");
                }


            }


        } else if (mFooterView != null && position == TYPE_FOOTER) {
        } else {
            if (holder instanceof GuessViewHolder) {
                GuessViewHolder guessViewHolder = ((GuessViewHolder) (holder));
                String imageUrl = mGuessLikeData.get(position - 1).getMainPic();
                imageUrl = UrlUtils.formatUrlPrefix(imageUrl);
                RoundedCorners roundedCorners = new RoundedCorners(5);
                RequestOptions options = RequestOptions.bitmapTransform(roundedCorners);
                Glide.with(mContext).load(imageUrl).apply(options).into(guessViewHolder.mGuesslikeItemLayoutBinding.itemImageSrc);
                guessViewHolder.mGuesslikeItemLayoutBinding.itemTitle.setText(mGuessLikeData.get(position - 1).getTitle());


            }


        }
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
            int refer=0;
            for (int i = 0; i < guessLikeHead.lotteryContainer.getChildCount(); i++) {
                LinearLayout linearLayout= (LinearLayout) guessLikeHead.lotteryContainer.getChildAt(i);
                for (int j = 0; j < linearLayout.getChildCount(); j++) {
                    if (refer>=lotteryCodeBean.getCodes().size()) {
                          continue;
                    }else{
                        TextView textView = (TextView) linearLayout.getChildAt(refer);
                        textView.setText(lotteryCodeBean.getCodes().get(refer));
                        textView.setTextColor(Color.BLACK);
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
        if (mHeaderView == null && mFooterView == null) {
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

    public void setGuessLikeData(List<MaylikeBean.ListDTO> mGuessLikeData) {
        this.mGuessLikeData = mGuessLikeData;
    }

    @Override
    public int getItemCount() {
        if (mGuessLikeData != null) {
            if (mHeaderView == null && mFooterView == null) {
                return mGuessLikeData.size();
            } else if (mHeaderView == null && mFooterView != null && mGuessLikeData.size() > 0) {
                return mGuessLikeData.size() + 1;
            } else if (mHeaderView != null && mFooterView == null && mGuessLikeData.size() > 0) {
                return mGuessLikeData.size() + 1;
            } else {
                if (mGuessLikeData.size() > 0) {
                    return mGuessLikeData.size() + 2;
                }
            }
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
