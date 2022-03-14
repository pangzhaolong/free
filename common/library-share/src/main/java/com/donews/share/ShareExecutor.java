package com.donews.share;

import android.app.Activity;
import android.content.Intent;

import androidx.fragment.app.FragmentActivity;




/**
 * @Author: honeylife
 * @CreateDate: 2020/4/15 17:53
 * @Description:
 */
abstract class ShareExecutor {

    public abstract void share(int cmd, ShareItem shareItem, FragmentActivity baseActivity);

    public abstract void release(Activity activity);

    public abstract void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data);

    public abstract void onNewIntent(Activity activity, Intent intent);
}
