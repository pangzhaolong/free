package com.donews.keepalive.accountsync;

import android.content.ISyncAdapter;
import android.os.IBinder;

public abstract class AccountSyncAdapterStub extends ISyncAdapter.Stub {

    public final IBinder getSyncAdapterBinder() {
        return this.asBinder();
    }

}
