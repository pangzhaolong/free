package com.donews.keepalive.accountsync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class AccountBaseSyncService extends Service {
    public static final String TAG = "keepAlive-Account";

    private AccountSyncAdapterStub syncAdapter;

    protected final void setSyncAdapter(AccountSyncAdapterStub syncAdapter) {
        this.syncAdapter = syncAdapter;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG,this.getClass().getSimpleName() + ".onBind");
        IBinder binder;
        if (this.syncAdapter != null) {
            AccountSyncAdapterStub adapterStub = this.syncAdapter;
            binder = adapterStub.getSyncAdapterBinder();
        } else {
            binder = null;
        }
        return binder;
    }

}
