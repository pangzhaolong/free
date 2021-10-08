package com.dn.sdk.widget;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dn.sdk.R;
import com.dn.sdk.count.CountTrackImpl;
import com.dn.sdk.listener.impl.NativeAdListenerImpl;
import com.dn.sdk.utils.SdkLogUtils;
import com.donews.b.main.info.DoNewsAdNativeData;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by SnowDragon
 * Date on 2020/11/23
 * Description:
 */
public class AdViewWrapper {
    private static final String TAG = "AdViewWrapper";
    public List<AdView> createViewByNewsFeedList(Activity activity, int layoutId, CountTrackImpl track, List<DoNewsAdNativeData> dataList) {

        List<AdView> viewList = new ArrayList<>();

        for (DoNewsAdNativeData nativeData : dataList) {

            View rootView = LayoutInflater.from(activity).inflate(layoutId == 0 ? getAdLayoutId() : layoutId, null);
            viewList.add(createView(activity.getApplicationContext(), track,rootView, nativeData));

        }

        return viewList;
    }

    /**
     * 这里可以根据需求配置不同的布局id
     *
     * @return 返回layoutId
     */
    private int getAdLayoutId() {
        return R.layout.item_news_feed_default;
    }

    /**
     * 返回一个AdView，resId保持和item_news_feed_default一样，布局可以任意调整
     *
     * @param context    application
     * @param rootView   布局
     * @param nativeData 数据接口
     * @return
     */
    private AdView createView(Context context, CountTrackImpl track, View rootView, DoNewsAdNativeData nativeData) {
        ViewHelper viewHelper = new ViewHelper(rootView);

        //这个广告标题有可能为空，adfrom=16时候为null,所以需要app判断一下，如果为null，
        // 则需要用des描述做title
        if (!TextUtils.isEmpty(nativeData.getTitle())) {
            viewHelper.tv_ad_title.setText(nativeData.getTitle());
        } else {
            viewHelper.tv_ad_title.setText(nativeData.getDese());
        }
        //设置广告描述
        viewHelper.tv_ad_desc.setText(nativeData.getDese());

        SdkLogUtils.E("广告类型:" + nativeData.getAdPatternType());

        //如果是图片广告，则需要把视频广告的容器隐藏，图文图片的image显示
        // nativeData.getAdPatternType() == 1, 表示是图片
        Log.i(TAG," ad getAdPatternType : "+nativeData.getAdPatternType());
        if (nativeData.getAdPatternType() == 1) {
            viewHelper.iv_ad_iamge.setVisibility(View.VISIBLE);
            viewHelper.fl_ad_video.setVisibility(View.GONE);
            List<ImageView> imageViews = new ArrayList<>();
            //如果返回的大图不为null

            Log.i(TAG, " ad imgUrl " + nativeData.getImgUrl() + " imgList " + nativeData.getImgList());
            imageViews.add(viewHelper.iv_ad_iamge);

            if (nativeData.getAdFrom() == 5) {
                nativeData.bindImageViews(imageViews, 0);
            } else {
                Glide.with(context).load(nativeData.getImgUrl()).into(viewHelper.iv_ad_iamge);
            }

        } else {//如果图片是视频广告，则需要把图文图片的iamge隐藏
            viewHelper.iv_ad_iamge.setVisibility(View.GONE);
            viewHelper.fl_ad_video.setVisibility(View.VISIBLE);
        }

        viewHelper.iv_ad_logo.setVisibility(View.VISIBLE);
        //加载联盟广告logo
        if (!TextUtils.isEmpty(nativeData.getLogoUrl())) {
            Glide.with(context.getApplicationContext()).load(nativeData.getLogoUrl()).into(viewHelper.iv_ad_logo);
        }
        //bindView的绑定容器，用来监听曝光和点击事件
        //bindView此方法必须要添加，这个是自渲染自动绑定点击和曝光的，如果不加，则会没有曝光点击回调
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(viewHelper.child_rl);

        nativeData.bindView(context, viewHelper.child_rl,
                viewHelper.fl_ad_video, clickableViews, new NativeAdListenerImpl(track));

        return new AdView(rootView, nativeData);
    }

    public class ViewHelper {

        RelativeLayout child_rl;
        TextView tv_ad_title;//标题
        TextView tv_ad_desc;//描述
        FrameLayout rl_ad_content;
        ImageView iv_ad_iamge;//背景图片
        ImageView iv_ad_logo;//logo
        FrameLayout fl_ad_video;//logo

        public ViewHelper(View rootView) {

            child_rl = rootView.findViewById(R.id.child_rl);
            tv_ad_title = rootView.findViewById(R.id.tv_ad_title);
            rl_ad_content = rootView.findViewById(R.id.rl_ad_content);
            iv_ad_iamge = rootView.findViewById(R.id.iv_ad_iamge);
            iv_ad_logo = rootView.findViewById(R.id.iv_ad_logo);
            tv_ad_desc = rootView.findViewById(R.id.tv_ad_desc);
            fl_ad_video = rootView.findViewById(R.id.fl_ad_video);
        }
    }


}
