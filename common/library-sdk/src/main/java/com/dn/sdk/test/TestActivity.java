package com.dn.sdk.test;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.dn.sdk.AdLoadManager;
import com.dn.sdk.R;
import com.dn.sdk.bean.RequestInfo;
import com.dn.sdk.constant.AdIdConfig;
import com.dn.sdk.databinding.ActivityTestBinding;
import com.dn.sdk.lib.ad.FullVideoNative;
import com.dn.sdk.lib.ad.VideoNative;
import com.dn.sdk.listener.AdVideoListener;
import com.donews.common.utils.DensityUtils;

/**
 * @author by SnowDragon
 * Date on 2020/11/19
 * Description:
 */
public class TestActivity extends AppCompatActivity {

    VideoNative rewardVideoPre = null;
    FullVideoNative fullScreenVideoPre = null;
    ActivityTestBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_test);


        Handler handler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                Log.i("chyy", " 倒计时走完啦");

            }
        };

        handler.sendEmptyMessageDelayed(1, 10000);


        //激励视频,RequestInfo，传入广告id即可
        binding.btnLoad.setOnClickListener(v ->
                AdLoadManager.getInstance()
                        .loadRewardVideo(TestActivity.this,
                                new RequestInfo(AdIdConfig.REWARD_VIDEO_ID),
                                new AdVideoListener() {
                                    @Override
                                    public void onAdShow() {

                                    }

                                    @Override
                                    public void onAdClose() {

                                    }

                                    @Override
                                    public void onError(int errorCode, String errorMsg) {

                                    }

                                    @Override
                                    public void videoCoolDownIng() {

                                    }
                                }));

        //激励视频预加载
        binding.btnRewardVideoPre.setOnClickListener(v -> {

            rewardVideoPre = AdLoadManager.getInstance().preLoadRewardVideo(this, new RequestInfo(AdIdConfig.REWARD_VIDEO_ID), null);
        });
        binding.btnRewardVideoShow.setOnClickListener(v -> {
            if (rewardVideoPre != null && rewardVideoPre.isReady()) {
                rewardVideoPre.showRewardVideoAd(this);
            }
        });

        //全屏视频
        binding.btnLoadFull.setOnClickListener(v -> {
            DisplayMetrics metrics = new DisplayMetrics();
            this.getDisplay().getRealMetrics(metrics);
            AdLoadManager.getInstance()
                    .loadFullScreenVideo(this,
                            new RequestInfo(AdIdConfig.FULL_SCREEN_VIDEO_ID, metrics.widthPixels, metrics.heightPixels),
                            new AdVideoListener() {
                                @Override
                                public void onAdShow() {

                                }

                                @Override
                                public void onAdClose() {

                                }

                                @Override
                                public void onError(int errorCode, String errorMsg) {

                                }

                                @Override
                                public void videoCoolDownIng() {

                                }
                            });
        });

        //预加载全屏视频
        binding.btnFullVideoPre.setOnClickListener(v -> {
            DisplayMetrics metrics = new DisplayMetrics();
            this.getDisplay().getRealMetrics(metrics);
//
//            fullScreenVideoPre = AdLoadManager.getInstance().preLoadFullScreenVideo(this,
//                    new RequestInfo(AdIdConfig.FULL_SCREEN_VIDEO_ID, metrics.widthPixels, metrics.heightPixels), null);
//            if (fullScreenVideoPre == null) {
//                Log.i("chyy", "为啥子啊");
//            }
        });
        binding.btnFullVideoShow.setOnClickListener(v -> {
            //注意这是调用的是showFullScreenVideo，全屏视频不能调用fullScreenVideoPre.isLoadReady()判断已经加载完毕
            if (fullScreenVideoPre != null) {
            }
        });

        //加载插屏广告
        binding.btnLoadInterstitial.setOnClickListener(v -> {
            //new RequestInfo(AdIdConfig.INTERSTITIAL_ID,300,500)
            AdLoadManager.getInstance().loadInterstitial(this, new RequestInfo(AdIdConfig.INTERSTITIAL_ID, 300, 800));
        });

        //加载信息流模板 这里的宽搞为dp
        DisplayMetrics outMetrics = new DisplayMetrics();
        getDisplay().getRealMetrics(outMetrics);
        float widthDp = DensityUtils.px2dp(this, outMetrics.widthPixels - 30);
        float heightDp = 0;

        binding.btnLoadNewsFeedTemplate.setOnClickListener(v -> {
            AdLoadManager.getInstance().loadNewsFeedTemplate(this,
                    new RequestInfo(AdIdConfig.NEWS_FEED_TEMPLATE_ID, widthDp, heightDp, binding.container),
                    null);
        });

        //自渲染广告信息流
        binding.btnLoadNewsFeedRender.setOnClickListener(v -> {
            // startActivity(new Intent(this, NewsFeedCustomerRenderActivity.class));

        });

    }

}
