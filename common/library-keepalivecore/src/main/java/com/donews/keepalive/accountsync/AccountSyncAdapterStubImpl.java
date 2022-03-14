package com.donews.keepalive.accountsync;

import android.accounts.Account;
import android.content.Context;
import android.content.ISyncAdapterUnsyncableAccountCallback;
import android.content.ISyncContext;
import android.content.SyncResult;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import java.lang.reflect.Method;

public class AccountSyncAdapterStubImpl extends AccountSyncAdapterStub {
    public static final String TAG = AccountBaseSyncService.TAG;

    private final Context mContext;
    public AccountSyncAdapterStubImpl(Context context) {
        mContext = context;
    }

    @Override
    public void onUnsyncableAccount(ISyncAdapterUnsyncableAccountCallback callback) throws RemoteException {
        Log.i(TAG,"SyncManager SyncAdapterStubImpl onUnsyncableAccount");
        try {
            callback.onUnsyncableAccountDone(true);
        } catch (Throwable throwable) {
            Log.e(TAG,"onUnsyncableAccount error", throwable);
        }
    }

    @Override
    public void startSync(ISyncContext context, String authority, Account account, Bundle bundle) throws RemoteException {
        try {
            StringBuilder stringBuilder = (new StringBuilder()).append("SyncManager startSync call in thread-");
            Thread thread = Thread.currentThread();
            Log.i(TAG,stringBuilder.append(thread.getName()).toString());
            SyncResult syncResult = new SyncResult();
            syncResult.stats.numIoExceptions = 1L;
            if (context != null) {
                Log.i(TAG,"syncContext.classname=" + context.getClass().getSimpleName());
                Class<?> classInfo = context.getClass();
                Method[] methods = classInfo.getMethods();
                if (methods.length != 0) {
                    for (Method m : methods) {
                        StringBuilder builder = (new StringBuilder()).append("MethodName=");
                        Log.i(TAG,builder.append(m.getName()).toString());
                    }
                }
            }
        } catch (Throwable t) {
            Log.e(TAG,"SyncManager startSync error", t);
        }

    }

    @Override
    public void cancelSync(ISyncContext context) throws RemoteException {
        AccountSyncUtils.cancelSync(this.mContext, null, false);
    }
}
