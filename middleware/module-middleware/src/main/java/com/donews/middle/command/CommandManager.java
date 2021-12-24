package com.donews.middle.command;

import android.app.Application;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import com.donews.common.BuildConfig;
import com.donews.middle.api.MiddleApi;
import com.donews.middle.bean.globle.CommandBean;
import com.donews.network.EasyHttp;
import com.donews.network.cache.model.CacheMode;
import com.donews.network.callback.SimpleCallBack;
import com.donews.network.exception.ApiException;
import com.donews.utilslibrary.utils.HttpConfigUtilsKt;
import com.donews.utilslibrary.utils.LogUtil;

public class CommandManager {
    private static final int UPDATE_CONFIG_MSG = 11002;
    private CommandBean mCommandBean;
    private static Application sApplication = null;

    private static final class Holder {
        private static final CommandManager s_frontConfigMgr = new CommandManager();
    }

    public static CommandManager Ins() {
        return Holder.s_frontConfigMgr;
    }

    private CommandManager() {
    }

    public CommandBean getConfigBean() {
        if (mCommandBean == null) {
            mCommandBean = new CommandBean();
        }
        return mCommandBean;
    }

    public void setCommandBean(CommandBean bean) {
        mCommandBean = bean;
    }

    private static final Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == UPDATE_CONFIG_MSG) {
                CommandManager.update();
            }
        }
    };

    public void init(Application application) {
        sApplication = application;
        if (mHandler != null) {
            mHandler.sendEmptyMessage(UPDATE_CONFIG_MSG);
        } else {
            update();
        }
    }

    private static void update() {
        LogUtil.i("CommandManager update");
        EasyHttp.get(HttpConfigUtilsKt.withConfigParams(MiddleApi.commandUrl, true))
                .cacheMode(CacheMode.NO_CACHE)
                .isShowToast(BuildConfig.DEBUG)
                .execute(new SimpleCallBack<CommandBean>() {
                    @Override
                    public void onError(ApiException e) {
                        LogUtil.e("CommandManager" + e.getCode() + e.getMessage());
                        if (mHandler != null) {
                            if (mHandler.hasMessages(UPDATE_CONFIG_MSG)) {
                                mHandler.removeMessages(UPDATE_CONFIG_MSG);
                            }
                            mHandler.sendEmptyMessageDelayed(UPDATE_CONFIG_MSG, 20 * 1000);
                        }
                    }

                    @Override
                    public void onSuccess(CommandBean bean) {
                        LogUtil.i("CommandManager update");

                        if (mHandler != null) {
                            if (mHandler.hasMessages(UPDATE_CONFIG_MSG)) {
                                mHandler.removeMessages(UPDATE_CONFIG_MSG);
                            }
                        }

                        CommandManager.Ins().setCommandBean(bean);

                        ClipboardManager clipboardManager = (ClipboardManager) sApplication.getSystemService(
                                Context.CLIPBOARD_SERVICE);
                        ClipData clipData = ClipData.newPlainText(null, bean.getCommand().replace("(*)", "\\n"));
                        clipboardManager.setPrimaryClip(clipData);
                        if (mHandler != null) {
                            mHandler.sendEmptyMessageDelayed(UPDATE_CONFIG_MSG, CommandManager.Ins().getConfigBean().getRefreshInterval() * 1000L);
                        }
                    }
                });
    }
}
