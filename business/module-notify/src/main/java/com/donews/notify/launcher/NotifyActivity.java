package com.donews.notify.launcher;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.donews.base.base.AppManager;
import com.donews.common.NotifyLuncherConfigManager;
import com.donews.keepalive.LaunchStart;
import com.donews.notify.R;
import com.donews.utilslibrary.analysis.AnalysisParam;
import com.donews.utilslibrary.analysis.AnalysisUtils;
import com.gyf.immersionbar.ImmersionBar;

import java.lang.ref.WeakReference;
import java.util.Random;

public class NotifyActivity extends Activity {
    public static final String TAG = NotifyInitProvider.TAG;

    private static WeakReference<NotifyActionActivity> sPopActionRefer = null;
    private static final LaunchStart launchStart = new LaunchStart();


    private ViewGroup mLayoutNotifyRoot = null;
    private ImageView mNotifyIv = null;
    private NotifyAnimationView mNotifyAnimationView = null;

    @SuppressLint("ClickableViewAccessibility")
    private final View.OnTouchListener touch = (view, motionEvent) -> {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            //if else
            int action = NotifyLuncherConfigManager.getInstance().getAppGlobalConfigBean().notifyActionType;
            Log.w(TAG, "open action = " + action);
            switch (action) {
                case 0:
                    mNotifyAnimationView.hide();
                    break;
                case 1:
                    destroy();
                    break;
                case 2:
                    schemeOpen(true);
                    break;
            }
        }
        return false;
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notify_top_banner);
        //统计展示
        try {
            AnalysisUtils.onEvent(this, AnalysisParam.DESKTOP_DISPLAY);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        NotifyScreenDelegate.putTodayShowCount(this);

        sPopActionRefer = new WeakReference(this);
        mLayoutNotifyRoot = findViewById(R.id.layout_notifyroot);
        mNotifyIv = findViewById(R.id.img_notify);
        int notifyShowSizeType = NotifyLuncherConfigManager.getInstance().getAppGlobalConfigBean().notifyShowSizeType;
        if (notifyShowSizeType == 0) {
            mNotifyIv.setImageResource(R.drawable.notifycation_notify_content);
        } else {
            mNotifyIv.setImageResource(R.drawable.launcher_notify_big);
        }
        mNotifyAnimationView = findViewById(R.id.layout_notifyanimation);
        mLayoutNotifyRoot.setBackgroundColor(Color.TRANSPARENT);
        mNotifyAnimationView.setViewDimissListener(new NotifyAnimationView.ViewStatusListener() {
            @Override
            public void onNotifyClose(View view) {
                Log.d(TAG, "NotifyActivity onNotifyClose");
                destroy();
            }

            @Override
            public void onNotifyShow(View view) {
                Log.d(TAG, "NotifyActivity onNotifyShow");
            }
        });
        mLayoutNotifyRoot.setOnTouchListener(touch);
        mNotifyAnimationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                schemeOpen(false);
            }
        });
        String url = NotifyLuncherConfigManager.getInstance().getAppGlobalConfigBean().notifyLauncherImg;
        Log.d(TAG, "start url ,url = " + url);
        if (!TextUtils.isEmpty(url) && url.startsWith("http")) {
            Glide.with(this).asBitmap().load(url).into(new CustomTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    mNotifyIv.setImageBitmap(resource);
                    mNotifyAnimationView.setHideDuration(NotifyLuncherConfigManager.getInstance().getAppGlobalConfigBean().notifyShowTime);
                    Log.d(TAG, "img url loaded , url = " + url);
                    mNotifyAnimationView.start();
                }

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {
                    Log.d(TAG, "img url onLoadCleared , url =" + url);
                }

                @Override
                public void onLoadFailed(@Nullable Drawable errorDrawable) {
                    super.onLoadFailed(errorDrawable);
                    Log.d(NotifyInitProvider.TAG, "tryLoadNewImg onLoadFailed , url = " + url);
                    mNotifyAnimationView.setHideDuration(NotifyLuncherConfigManager.getInstance().getAppGlobalConfigBean().notifyShowTime);
                    mNotifyAnimationView.start();
                }
            });
        } else {
            mNotifyAnimationView.setHideDuration(NotifyLuncherConfigManager.getInstance().getAppGlobalConfigBean().notifyShowTime);
            mNotifyAnimationView.start();
        }
        launchStart.cancel();

        ImmersionBar.with(this)
                .statusBarColor(R.color.transparent)
                .fitsSystemWindows(true)
                .autoDarkModeEnable(false)
                .init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "NotifyActivity onResume");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //关闭队列中的所有当前页面类型的页面
        AppManager.getInstance().finishAllActivity(getClass());
        Log.d(TAG, "NotifyActivity onDestroy");
    }

    public static void actionStart(Context context) {
        Log.i(TAG, "NotifyActivity actionStart");
        destroy();
        try {
            Intent intent = new Intent();
            ComponentName componentName = new ComponentName(context, NotifyLuncherConfigManager.getInstance().getAppGlobalConfigBean().notifyAlias);
            intent.setComponent(componentName);
            launchStart.doStart(context, intent);
        } catch (Throwable t) {
            t.printStackTrace();
            Intent intent = new Intent(context, NotifyActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            launchStart.doStart(context, intent);
        }
    }


    public static void destroy() {
        Log.d(TAG, "NotifyActivity destroy");
        Activity activity = sPopActionRefer != null ? sPopActionRefer.get() : null;
        if (activity != null && !activity.isDestroyed()) {
            Log.d(TAG, "NotifyActivity finish");
            activity.finish();
        }
        NotifyActionActivity.destroy();
    }

    public static void browserOpen(Activity activity, String uri) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            intent.setData(Uri.parse(uri));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
            //报错上一次点击进入的时间
            SPUtils.getInstance().put("notifyClickLastOpenInterval", System.currentTimeMillis());
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    /**
     * @param isProbability T:启用概率
     */
    private void schemeOpen(boolean isProbability) {
        //打开应用的概率
        int po = NotifyLuncherConfigManager.getInstance().getAppGlobalConfigBean().notifyProbabilityOpen;
        if (isProbability) {
            int gernPo = new Random().nextInt(100);
            if (gernPo > po) {
                mNotifyAnimationView.hide();
                return; //小于中台配置的概率。所以需要取大于此值得部分
            }
        }
        try {
            AnalysisUtils.onEvent(NotifyActivity.this, AnalysisParam.DESKTOP_DISPLAY_CLICK);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        String scheme = NotifyLuncherConfigManager.getInstance().getAppGlobalConfigBean().notifySchemeOpen;
        browserOpen(NotifyActivity.this, scheme);
        mNotifyAnimationView.hide();
    }

}
