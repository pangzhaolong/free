package com.donews.base.appdialog.permission;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by SnowDragon
 * Date on 2020/11/25
 * Description:
 */
public class GrantActivity extends AppCompatActivity {
    private static List<PermissionListener> mPermissionListenerList;
    private static PermissionListener mPermissionListener;

    private static final int REQUEST_CODE = 101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestAlertWindowPermission();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestAlertWindowPermission() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (PermissionUtil.hasPermissionOnActivityResult(this)) {
                mPermissionListener.onSuccess();
            } else {
                mPermissionListener.onFail();
            }
        }
        finish();
    }

   public static synchronized void request(Context context, PermissionListener permissionListener) {
        if (PermissionUtil.hasPermission(context)) {
            permissionListener.onSuccess();
            return;
        }
        if (mPermissionListenerList == null) {
            mPermissionListenerList = new ArrayList<>();
            mPermissionListener = new PermissionListener() {
                @Override
                public void onSuccess() {
                    for (PermissionListener listener : mPermissionListenerList) {
                        listener.onSuccess();
                    }
                    mPermissionListenerList.clear();
                }

                @Override
                public void onFail() {
                    for (PermissionListener listener : mPermissionListenerList) {
                        listener.onFail();
                    }
                    mPermissionListenerList.clear();
                }
            };
            Intent intent = new Intent(context, GrantActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
        mPermissionListenerList.add(permissionListener);
    }

}
