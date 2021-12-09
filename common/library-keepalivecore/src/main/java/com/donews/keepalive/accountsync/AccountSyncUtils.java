package com.donews.keepalive.accountsync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.PeriodicSync;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.keepalive.daemon.core.R;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class AccountSyncUtils {
    public static final String TAG = AccountCoreJobService.TAG;
    private static final long DURATION = TimeUnit.SECONDS.toMillis(5L);

    private static String ACCOUNT_NAME;
    private static String ACCOUNT_TYPE ;
    private static String ACCOUNT_AUTHORITY ;

    private static final long POLL_FREQUENCY_MIN = 900;
    private static final long POLL_FREQUENCY_MAX = 3600;

    private static boolean sAccountEnabled = true;
    private static Account sAccount;
    private static String sSyncAuthority;

    private static long sLastWorkTime;

    public static boolean getAccountEnabled() {
        return AccountSyncUtils.sAccountEnabled;
    }

    public static void setAccountEnabled(boolean accountEnabled) {
        AccountSyncUtils.sAccountEnabled = accountEnabled;
    }

    public static void initAccount(Context context) {
        if (AccountSyncUtils.sAccount == null) {
            ACCOUNT_NAME = context.getString(R.string.keepalive_sync_account_name);
            ACCOUNT_TYPE = context.getString(R.string.keepalive_sync_account_type);
            ACCOUNT_AUTHORITY = context.getString(R.string.keepalive_sync_account_authority);

            AccountSyncUtils.sAccount = new Account(ACCOUNT_NAME, ACCOUNT_TYPE);
            AccountSyncUtils.sSyncAuthority = ACCOUNT_AUTHORITY;
        }
    }

    public static void requestSync(Context context, boolean charging) {
        Log.w(TAG, "SyncManager requestSync,authority=" + ACCOUNT_AUTHORITY);
        try {
            Bundle bundle = new Bundle();
            bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
            if (charging) {
                bundle.putBoolean("require_charging", false);
            }

            if (AccountSyncUtils.sAccount == null) {
                initAccount(context);
            }

            ContentResolver.requestSync(AccountSyncUtils.sAccount, ACCOUNT_AUTHORITY, bundle);
        } catch (Throwable e) {
            Log.w(TAG, "requestSync error", e);
        }
    }

    public static void autoSyncAccount(Context context, boolean needAdd) {
        if (!getAccountEnabled()) {
            Log.w(TAG, "autoSyncAccount hit , account not enable");
        } else {
            long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis - AccountSyncUtils.sLastWorkTime < AccountSyncUtils.DURATION) {
                Log.w(TAG, "account sync called, too short time to sync");
                return;
            }

            AccountSyncUtils.sLastWorkTime = currentTimeMillis;
            StringBuilder stringBuilder = (new StringBuilder()).append("SyncManager autoSyncAccount,thread=");
            Thread thread = Thread.currentThread();
            AccountManager accountManager = AccountManager.get(context);
            Log.w(TAG, stringBuilder.append(thread.getName()).toString());
            if (accountManager != null) {
                if (AccountSyncUtils.sAccount == null) {
                    initAccount(context);
                }
                String accountName = ACCOUNT_NAME;
                String accountType = ACCOUNT_TYPE;

                Log.w(TAG, "SyncManager autoSyncAccount,accountName=" + accountName + ",accountType=" + accountType);
                if (needAdd) {
                    try {
                        Account[] accounts = accountManager.getAccountsByType(accountType);
                        if (accounts.length == 0) {
                            Log.w(TAG, "SyncManager autoSyncAccount no accounts will add one");
                            accountManager.addAccountExplicitly(AccountSyncUtils.sAccount, null, Bundle.EMPTY);
                        }
                    } catch (Throwable throwable) {
                        Log.w(TAG, "autoSyncAccount error", throwable);
                    }
                }

                //进行账号信息的同步服务
                try {
                    ContentResolver.setIsSyncable(AccountSyncUtils.sAccount, AccountSyncUtils.sSyncAuthority, 1);
                    ContentResolver.setSyncAutomatically(AccountSyncUtils.sAccount, AccountSyncUtils.sSyncAuthority, true);
                    ContentResolver.setMasterSyncAutomatically(true);
                    requestSync(context, true);
                    List<PeriodicSync> periodicSyncs = ContentResolver.getPeriodicSyncs(AccountSyncUtils.sAccount, AccountSyncUtils.sSyncAuthority);
                    if (periodicSyncs == null || periodicSyncs.isEmpty()) {
                        //应该多久执行一次同步，以秒为单位。在Android API级别24及更高级别上，强制执行15分钟的最小间隔。在以前的版本中，最小间隔为1小时。
                        ContentResolver.addPeriodicSync(AccountSyncUtils.sAccount, AccountSyncUtils.sSyncAuthority, Bundle.EMPTY, Build.VERSION.SDK_INT > 24 ? POLL_FREQUENCY_MIN : POLL_FREQUENCY_MAX);
                    }
                } catch (Throwable throwable) {
                    Log.w(TAG, "autoSyncAccount-2 error", throwable);
                }
            }
        }
    }

    public static void cancelSync(Context mContext, Account account, boolean needAdd) {
        try{
            AccountManager accountManager = AccountManager.get(mContext);
            if (AccountSyncUtils.sAccount == null) {
                initAccount(mContext);
            }
            Log.w(TAG, "SyncManager autoSyncAccount,accountName=" + ACCOUNT_NAME + ",accountType=" + ACCOUNT_TYPE);
            try{
                Account[] accounts = accountManager.getAccountsByType(ACCOUNT_TYPE);
                if (accounts.length == 0) {
                    accountManager.addAccountExplicitly(AccountSyncUtils.sAccount, null, Bundle.EMPTY);

                    ContentResolver.setIsSyncable(AccountSyncUtils.sAccount, AccountSyncUtils.ACCOUNT_AUTHORITY, 1);
                    ContentResolver.setSyncAutomatically(AccountSyncUtils.sAccount, AccountSyncUtils.ACCOUNT_AUTHORITY, true);
                    ContentResolver.setMasterSyncAutomatically(true);

                    List<PeriodicSync> periodicSyncs = ContentResolver.getPeriodicSyncs(AccountSyncUtils.sAccount,AccountSyncUtils.ACCOUNT_AUTHORITY);
                    if (periodicSyncs == null || periodicSyncs.isEmpty()) {
                        ContentResolver.addPeriodicSync(AccountSyncUtils.sAccount, AccountSyncUtils.ACCOUNT_AUTHORITY, Bundle.EMPTY,
                                Build.VERSION.SDK_INT > 24 ? POLL_FREQUENCY_MIN : POLL_FREQUENCY_MAX);
                    }
                }
            }catch (Throwable t){
            	t.printStackTrace();
            }

            Bundle bundle = new Bundle();
            bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
            bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
            if (needAdd) {
                bundle.putBoolean("require_charging", false);
            }
            ContentResolver.requestSync(account, ACCOUNT_AUTHORITY, bundle);
        }catch (Throwable t){
        	t.printStackTrace();
        }
    }
}
