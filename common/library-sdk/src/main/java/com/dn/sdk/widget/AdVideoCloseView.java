package com.dn.sdk.widget;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bytedance.sdk.openadsdk.activity.base.TTBaseVideoActivity;
import com.dn.sdk.R;
import com.dn.sdk.api.AdConfigSupply;
import com.dn.sdk.cache.ACache;
import com.dn.sdk.listener.AdVideoListener;
import com.donews.zkad.ad.rewardvideo.ZKRewardVideoActivity;
import com.qq.e.ads.PortraitADActivity;
import com.qq.e.ads.RewardvideoLandscapeADActivity;
import com.qq.e.ads.RewardvideoPortraitADActivity;

/**
 * @Author: honeylife
 * @CreateDate: 2020/7/15 14:24
 * @Description:
 */
public class AdVideoCloseView {
    private static String TAG = "AdVideoCloseView";

    private static final long VIEW_SHOW_DELAY_TIME = 4 * 1000;

    private static int mTime = 6;
    public static Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int time = msg.arg1;
            TextView textView = (TextView) msg.obj;
            textView.setText(String.format("%s 秒之后跳过", time / 1000));
            textView.setClickable(false);
            if (time > 0) {
                Message message = Message.obtain();
                message.arg1 = time - 1000;
                message.obj = textView;
                sendMessageDelayed(message, 1000);
            } else {
                textView.setText(" 跳过 ");
                textView.setClickable(true);
            }
        }
    };


    private static boolean isAddView(Activity activity) {
        return isVideoActivity(activity) && AdConfigSupply.getInstance().isSkipVideo();
    }

    private static boolean isVideoActivity(Activity activity) {
        return isCSJRewardVideo(activity) || isGDTRewardVideo(activity)
                || isZkActivityReward(activity);
    }


    private static boolean isCSJRewardVideo(Activity activity) {
        return activity instanceof TTBaseVideoActivity;
    }

//    private static boolean isYLBRewardVideo(Activity activity) {
//        return activity instanceof RewardVideoActivity;
//    }

    private static boolean isGDTRewardVideo(Activity activity) {

        if (activity instanceof PortraitADActivity || activity instanceof RewardvideoPortraitADActivity
                || activity instanceof RewardvideoLandscapeADActivity) {
            return true;
        }

        return false;
    }

    private static boolean isZkActivityReward(Activity activity) {
        return activity instanceof ZKRewardVideoActivity;
    }


    private static View getWatermarkView(Activity context, ViewGroup parent) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.sdk_view_video_item, parent, false);
        inflate.setId(R.id.sdk_text_video_id);
        TextView videoTv = inflate.findViewById(R.id.view_tv);
        ViewGroup.LayoutParams layoutParams = inflate.getLayoutParams();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        inflate.setLayoutParams(layoutParams);
        inflate.setVisibility(View.VISIBLE);
        Message message = Message.obtain();
        message.arg1 = 6000;
        message.obj = videoTv;
        mHandler.sendMessageDelayed(message, 1000);
        videoTv.setClickable(false);
        videoTv.setOnClickListener(v -> {
            videoListenerCallBack();
            context.finish();
        });
        return inflate;
    }

    /**
     * 激励视频监听回调
     */
    private static void videoListenerCallBack() {
        AdVideoListener rewardVideoAdListener = ACache.getInstance().getRewardVideoListener();
        //sdk变更，此处不再单独回调
        if (rewardVideoAdListener != null) {
//            rewardVideoAdListener.onAdClose();
        }
    }

    /**
     * 添加视图
     *
     * @param activity activity
     */
    public static void onAddView(Activity activity) {
        if (activity == null) {
            return;
        }
        if (AdVideoCloseView.isAddView(activity)) {
            View decorView = activity.getWindow().getDecorView();
            ViewGroup rootView = decorView.findViewById(android.R.id.content);
            mHandler.postDelayed(() -> rootView.addView(getWatermarkView(activity, rootView)), VIEW_SHOW_DELAY_TIME);
        }
    }

    /**
     * 判断视频页面失去焦点
     *
     * @param activity
     */
    public static void onVideoPauseActivity(Activity activity) {
        if (activity == null) {
            return;
        }
        if (isCSJRewardVideo(activity) || isGDTRewardVideo(activity)) {
            //  Intent intent = new Intent(PixelActivity.VIDEO_ACTION_PAUSE);
            //activity.sendBroadcast(intent);

        }
    }

}
