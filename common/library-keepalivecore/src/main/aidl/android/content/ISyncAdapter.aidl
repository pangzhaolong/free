// ISyncAdapter.aidl
package android.content;

import android.accounts.Account;
import android.os.Bundle;
import android.content.ISyncAdapterUnsyncableAccountCallback;
import android.content.ISyncContext;

// Declare any non-default types here with import statements

interface ISyncAdapter {

    void onUnsyncableAccount(ISyncAdapterUnsyncableAccountCallback callback);

    void startSync(ISyncContext context, String authority,in Account account,in Bundle bundle);

    void cancelSync(ISyncContext context);

}