package com.donews.keepalive.accountsync;

import android.content.Context;

import com.donews.utilslibrary.utils.Utils;

public class AccountKeepAliveMain {

    /**
     * 账户同步机制唤醒进程，入口类
     */
    public static void attach(Context context) {
        if (Utils.isMainProcess(context)) {

            //静态广播 保活调用链
            AccountReceiver.register(context,new AccountDaemonStaticReceiver());

            //jobService 保活调用链
            AccountJobService.startJob(context);

            //自动同步
            AccountSyncUtils.autoSyncAccount(context, true);
        }
    }

}
