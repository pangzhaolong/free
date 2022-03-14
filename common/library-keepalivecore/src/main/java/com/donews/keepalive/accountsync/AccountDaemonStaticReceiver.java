package com.donews.keepalive.accountsync;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AccountDaemonStaticReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //此处是玄学，姑且认为能收到信息，notkill 逻辑，埋点看数值真实情况。TODO 日志埋点
        String action = intent.getAction();
        Log.w(AccountCoreJobService.TAG,"AccountDaemonStaticReceiver action : "+ action);
    }
}
