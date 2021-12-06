package com.donews.keepalive.accountsync;

import android.util.Log;

public class AccountSyncService extends AccountBaseSyncService {
    private final Object sSyncAdapterLock = new Object();

    public void onCreate() {
        super.onCreate();
        Log.i(TAG,"SyncService.onCreate");
        synchronized (sSyncAdapterLock) {
            this.setSyncAdapter(new AccountSyncAdapterStubImpl(getApplicationContext()));
        }

        AccountJobService.startJob(this);
    }

}
