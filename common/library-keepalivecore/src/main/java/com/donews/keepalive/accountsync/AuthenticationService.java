package com.donews.keepalive.accountsync;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.NetworkErrorException;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

public class AuthenticationService extends Service {
    private AbstractAccountAuthenticator mAccountAuthenticator;

    public void onCreate() {
        super.onCreate();
        this.mAccountAuthenticator = new AccountAuthenticator(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        if (this.mAccountAuthenticator != null) {
            AbstractAccountAuthenticator authenticator = this.mAccountAuthenticator;
            return authenticator != null ? authenticator.getIBinder() : null;
        } else {
            return null;
        }
    }

    public static final class AccountAuthenticator extends AbstractAccountAuthenticator {

        public AccountAuthenticator(Context context) {
            super(context);
        }

        public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
            return null;
        }


        public Bundle addAccount(AccountAuthenticatorResponse response, String accountType,
                                 String authTokenType, String[] requiredFeatures, Bundle options) throws NetworkErrorException {
            return null;
        }


        public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account,
                                         Bundle options) throws NetworkErrorException {
            return null;
        }


        public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account,
                                   String authTokenType, Bundle options) throws NetworkErrorException {
            return null;
        }


        public String getAuthTokenLabel(String authTokenType) {
            return null;
        }


        public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account,
                                        String authTokenType, Bundle options) throws NetworkErrorException {
            return null;
        }


        public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account,
                                  String[] features) throws NetworkErrorException {
            return null;
        }
    }
}
