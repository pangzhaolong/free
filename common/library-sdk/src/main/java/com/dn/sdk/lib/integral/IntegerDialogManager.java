package com.dn.sdk.lib.integral;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;

import com.dn.sdk.AdLoadManager;
import com.dn.sdk.BuildConfig;
import com.dn.sdk.api.AdSdkHttp;
import com.dn.sdk.bean.IntegralBean;
import com.dn.sdk.bean.IntegralDialogConfigBean;
import com.dn.sdk.bean.RequestInfo;
import com.dn.sdk.constant.AdIdConfig;
import com.dn.sdk.listener.IAdCallBack;
import com.dn.sdk.manager.IntegralDataSupply;
import com.dn.sdk.widget.IntegralAdDialog;
import com.donews.common.utils.DensityUtils;
import com.donews.network.EasyHttp;
import com.donews.network.cache.model.CacheMode;
import com.donews.network.callback.SimpleCallBack;
import com.donews.network.exception.ApiException;
import com.donews.utilslibrary.utils.DeviceUtils;
import com.donews.utilslibrary.utils.JsonUtils;
import com.donews.utilslibrary.utils.KeySharePreferences;
import com.donews.utilslibrary.utils.LogUtil;
import com.donews.utilslibrary.utils.SPUtils;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.Random;
import java.util.Vector;

/**
 * @author by SnowDragon
 * Date on 2021/4/12
 * Description:
 */
public class IntegerDialogManager {

    // 停止计时
    public final static int STOP_TIME = 1001;
    // 弹窗计时开始
    private final static int POPUP_TIME = 1005;
    // 插屏计时开始
    private final static int INSERT_TIME = 1002;
    // 常量值
    private final static int N = 10;
    // 常量值 弹窗切换的值
    private final static int M = 1;
    private Activity baseActivity;
    private Handler mHandler = new MyHandler(this);
    // 红包弹窗时间记录
    private int time = 0;
    // 插屏时间记录
    private int interstitialTime = 0;

    private IntegralDialogConfigBean configBean;
    // 10次弹窗中，如果是0展示不遮挡关闭按钮的弹窗，如果是1，展示遮挡关闭按钮的弹窗
    private int array[] = new int[N];
    // 表示一共展示的弹窗次数
    private int showCount = 0;
    //表示倒计时是否继续着
    private boolean isStart = false;
    private Runnable mUpdateMicStatusTimer = new Runnable() {

        @Override
        public void run() {
            if (configBean == null) {
                return;
            }
            Log.i("chy", " runnable: -----------");
            time++;
            interstitialTime++;
//            LogUtil.d("time=" + time + "interstitialTime=" + interstitialTime);
            if (interstitialTime == configBean.getInterstitialTime()) {
                showInterstitial();
                stopTime();
                if (time == configBean.getRedPacketTime()) {
                    time = 0;
                }
                return;
            }
//            LogUtil.d("time" + time + "interstitialTime=" + interstitialTime);
            if (time == configBean.getRedPacketTime()) {
                LogUtil.e("time=Thread" + Thread.currentThread().getName());
                showDialog();
                stopTime();
                return;
            }
            update();
        }
    };

    public static IntegerDialogManager getInstance() {
        return IntegerDialogManager.Holder.instance;
    }


    private static final class Holder {
        private static IntegerDialogManager instance = new IntegerDialogManager();

        private Holder() {
        }
    }

    // 初始化数据
    public void init(Activity baseActivity) {
        this.baseActivity = baseActivity;
        isStart = true;
        getNetTimeData();

    }

    // 红包弹窗 主体页面不可见
    public void onRedPacketPause() {
        if (isStart) {
            stopTime();
        }
    }

    // 红包弹窗 主体页面可见
    public void onRedPacketResume() {
        if (!isStart) {
            update();
        }
    }

    //展示插屏广告
    private void showInterstitial() {
        onRequestInterstitial(baseActivity);
        interstitialTime = 0;
    }

    public static void onRequestInterstitial(Activity activity) {
        float widthDp = DensityUtils.px2dp(activity, DeviceUtils.getWidthPixels(activity));
        AdLoadManager.getInstance().loadInterstitial(activity, new RequestInfo(AdIdConfig.INTERSTITIAL_ID,
                widthDp, 300), new IAdCallBack() {
            @Override
            public void onError(String error) {
                LogUtil.d("插屏出错了" + error);
            }

            @Override
            public void onShow() {

                LogUtil.d("插屏展示了");
            }

            @Override
            public void onClose() {
                LogUtil.d("插屏关闭了");
                IntegerDialogManager.getInstance().update();
            }
        });

    }

    // 展示弹窗
    private void showDialog() {
        IntegralBean.DataBean bean = IntegralDataSupply.getInstance().getServerIntegralBean();
        if (bean != null) {
            IntegralAdDialog.showIntegralDialog((FragmentActivity) baseActivity, bean);
            update();
        } else {
            AdSdkHttp adSdkHttp = new AdSdkHttp();
            adSdkHttp.getIntegralList();
        }
        showCount++;
        LogUtil.d("showCount=" + showCount);
        if (showCount == N) {
            showCount = 0;
            onRestartDataArray();
        }
        time = 0;
    }


    public void onDataMessage(Message msg) {

        switch (msg.what) {
            case POPUP_TIME:
                break;
            case INSERT_TIME:
                break;
            case STOP_TIME:
                break;
            default:
                break;
        }
    }


    private void getNetTimeData() {
        EasyHttp.get(BuildConfig.BASE_CONFIG_URL + BuildConfig.APP_IDENTIFICATION + "-redPacketTime" + BuildConfig.BASE_RULE_URL + JsonUtils.getCommonJson(false))
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<IntegralDialogConfigBean>() {
                    @Override
                    public void onError(ApiException e) {
                        LogUtil.d(e.getCode() + e.getMessage());
                    }

                    @Override
                    public void onSuccess(IntegralDialogConfigBean mRedPacketBean) {
                        LogUtil.d(mRedPacketBean.toString());
                        configBean = mRedPacketBean;
                        SPUtils.setInformain(KeySharePreferences.SPLASH_BACKGROUND_INTERVAL_TIME, mRedPacketBean.getSplashBackgroundIntervalTime());

                        onRestartDataArray();
                        update();
                    }
                });
    }

    // 重置array数据
    private void onRestartDataArray() {
        array = new int[N];
        //创建一个产生随机数的对象
        Random r = new Random();
        //创建一个存储随机数的集合
        Vector<Integer> v = new Vector<Integer>();
        //定义一个统计变量
        int count = 0;
        int size = Math.min(configBean.getPercent(), N);
        int sizeIndex = N - 1;
        LogUtil.d("sizeIndex=" + sizeIndex);
        while (count < size - 1) {
            int number = r.nextInt(sizeIndex) + 1;
            //判断number是否在集合中存在
            if (!v.contains(number)) {
                //不在集合中，就添加
                v.add(number);
                array[number] = M;
                count++;
            }
            LogUtil.d("count=" + count);
        }
    }

    //开始计时
    public void update() {
        isStart = true;
        mHandler.postDelayed(mUpdateMicStatusTimer, 1000);
    }

    // 停止计时
    private void stopTime() {
        isStart = false;
        Log.i("chy", " stopTime");
        mHandler.removeCallbacks(mUpdateMicStatusTimer);
    }

    //获取奖励接口，比如收下红心
    public void onHeartDraw() {
        LogUtil.d("TAG-onHeartDraw");
        String data = "";
        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("act_id", String.format("%s", ""));
            data = jsonObject.toString();
        } catch (Throwable e) {
            e.printStackTrace();
        }

        EasyHttp.post(BuildConfig.HTTP_AWARD + "award/v1/draw")
                .upJson(data)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<Object>() {
                    @Override
                    public void onError(ApiException e) {
                        LogUtil.e(e.getMessage());
                    }

                    @Override
                    public void onSuccess(Object o) {

                    }

                    @Override
                    public void onCompleteOk() {
                        LogUtil.e("==onCompleteOk=");
                        update();
                    }

                });
    }


    private static class MyHandler extends Handler {
        private final WeakReference<IntegerDialogManager> mHandler;

        public MyHandler(IntegerDialogManager redPacketManager) {
            super(Looper.getMainLooper());
            mHandler = new WeakReference<IntegerDialogManager>(redPacketManager);
        }

        @Override
        public void handleMessage(Message msg) {
            if (mHandler.get() == null) {
                return;
            }
            mHandler.get().onDataMessage(msg);
        }
    }
}
