package com.donews.notify.launcher;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.blankj.utilcode.util.SPUtils;
import com.donews.base.base.AppManager;
import com.donews.common.NotifyLuncherConfigManager;
import com.donews.common.contract.LoginHelp;
import com.donews.keepalive.LaunchStart;
import com.donews.notify.R;
import com.donews.notify.launcher.configs.Notify2ConfigManager;
import com.donews.notify.launcher.configs.baens.Notify2DataConfigBean;
import com.donews.notify.launcher.utils.NotifyItemType;
import com.donews.notify.launcher.utils.NotifyItemUtils;
import com.donews.notify.launcher.utils.fix.FixTagUtils;
import com.donews.utilslibrary.analysis.AnalysisParam;
import com.donews.utilslibrary.analysis.AnalysisUtils;
import com.gyf.immersionbar.ImmersionBar;

import java.lang.ref.WeakReference;
import java.util.Random;

public class NotifyActivity extends FragmentActivity {
    public static final String TAG = NotifyInitProvider.TAG;

    private static WeakReference<NotifyActionActivity> sPopActionRefer = null;
    private static final LaunchStart launchStart = new LaunchStart();

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

    private ViewGroup mLayoutNotifyRoot = null;
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
        setNotifyAttrs();
        launchStart.cancel();

        ImmersionBar.with(this)
                .statusBarColor(R.color.transparent)
                .fitsSystemWindows(true)
                .autoDarkModeEnable(false)
                .init();

    }

    private static int a = 0;

    //设置通知属性相关内容
    private void setNotifyAttrs() {
        String content =
                "${title}:你好啊";
        Notify2DataConfigBean.UiTemplat uiTemplat = Notify2ConfigManager.Ins().getNotifyConfigBean().notifyConfigs.get(0).uiTemplate.get(0);
        FixTagUtils.buildContentTag(uiTemplat,content);
        //设置消息类型
        int pos = a % NotifyItemType.values().length;
        a++;
        NotifyItemType type = NotifyItemType.values()[pos];
        mNotifyAnimationView.isTopNotify = type.orientation == 0;
        mNotifyAnimationView.notifyType = type;
        //判断方向
        if (!mNotifyAnimationView.isTopNotify) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mNotifyAnimationView.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            mNotifyAnimationView.setLayoutParams(lp);
        }
        //添加当前类型的通知视图和绑定数据
        NotifyItemUtils.addItemView(this, mNotifyAnimationView, type, () -> {
            //需要特殊处理UI的类型。再此处处理即可
        });
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
