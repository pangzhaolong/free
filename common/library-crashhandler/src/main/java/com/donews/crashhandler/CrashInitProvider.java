package com.donews.crashhandler;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.donews.common.NotifyLuncherConfigManager;
import com.donews.crashhandler.core.CrashCoreHandler;

/**
 * crash初始化入口，只要依赖该工程，自动生效，入口在该类
 *
 * @author Swei
 * @date 2021/4/6 15:01
 * @since v1.0
 */
public class CrashInitProvider extends ContentProvider {
    public static final String TAG = "CrashCore";

    @Override
    public boolean onCreate() {
        Log.i(TAG,"CrashInitProvider onCreate ...");
        //此处getContext实际就是application
//        ((Application)getContext().getApplicationContext()).registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
        return true;
    }

    @Override
    public void attachInfo(Context context, ProviderInfo info) {
        super.attachInfo(context, info);
        try {
            NotifyLuncherConfigManager.getInstance().addAppConfigDataUpdateListener(new NotifyLuncherConfigManager.AppGlobalConfigDataUpdateListener() {
                @Override
                public void dataUpdate(boolean isRefresh) {
                    Log.d(TAG,"CrashInitProvider dataUpdate ......");
                    installCrash(isRefresh);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i(TAG,"CrashInitProvider attachInfo ...");
    }

    private void installCrash(boolean isRefresh){
        boolean lifecycleHandler = NotifyLuncherConfigManager.getInstance().getAppGlobalConfigBean().crashLifecycleHandler;
        boolean openCrashHandler = NotifyLuncherConfigManager.getInstance().getAppGlobalConfigBean().crashIsOpen;
        if(openCrashHandler){
            //如果未安装
            if(!CrashCoreHandler.installed()){
                CrashCoreHandler.install(lifecycleHandler);
                Log.d(TAG,"crashHandler installed , lifecycleHandler = "+lifecycleHandler);
            }else{
                //如果已安装且数据发生变更
                if(isRefresh){
                    CrashCoreHandler.uninstall();
                    CrashCoreHandler.install(lifecycleHandler);
                }else{
                    Log.w(TAG,"crashHandler global data update,but CrashCoreHandler install");
                }
            }
        }else{
            Log.w(TAG,"crashHandler global data not open");
        }
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
