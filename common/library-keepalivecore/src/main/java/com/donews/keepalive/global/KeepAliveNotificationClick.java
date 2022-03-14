package com.donews.keepalive.global;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.donews.common.NotifyLuncherConfigManager;
import com.donews.keepalive.ForegroundNotificationClickListener;
import com.donews.utilslibrary.analysis.AnalysisParam;
import com.donews.utilslibrary.analysis.AnalysisUtils;

/**
 * 默认的通知栏点击实现
 *
 * @author Swei
 * @date 2021/4/15 21:08
 * @since v1.0
 */
public class KeepAliveNotificationClick implements ForegroundNotificationClickListener {
    private Context mContext;

    public KeepAliveNotificationClick(Context mContext) {
        this.mContext = mContext;
    }

    private void schemeOpen() {
        String scheme = NotifyLuncherConfigManager.getInstance().getAppGlobalConfigBean().notifySchemeOpen;
        browserOpen(mContext, scheme);
    }

    public static void browserOpen(Context context, String uri) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            intent.setData(Uri.parse(uri));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @Override
    public void foregroundNotificationClick(Context context, Intent intent) {
        try {
            AnalysisUtils.onEvent(context, AnalysisParam.NOTICE_BAR_CLICK);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        schemeOpen();
    }
}
