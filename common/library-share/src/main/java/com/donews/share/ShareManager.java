package com.donews.share;

import android.app.Activity;
import android.content.Intent;

import androidx.fragment.app.FragmentActivity;


/**
 * @Author: honeylife
 * @CreateDate: 2020/4/15 17:48
 * @Description:
 */
public class ShareManager {
    // 微信好友
    public final static int SHARE_COMMAND_WX = 1;
    //微信朋友圈
    public final static int SHARE_COMMAND_WX_FRIEND = 2;
    //微信群
    public final static int SHARE_QUN_WX = 3;
    // 微信面对面
    public final static int SHARE_MIAN_WX = 4;
    private ShareExecutor se = null;

    public ShareManager() {

    }


    public void share(final int cmd, ShareItem item, FragmentActivity activity) {
        switch (cmd) {
            case SHARE_COMMAND_WX:
            case SHARE_COMMAND_WX_FRIEND:
            case SHARE_QUN_WX:
            case SHARE_MIAN_WX:
                se = new WXShareExecutor(activity);
                se.share(cmd, item, activity);
                break;

            default:
                break;
        }
    }

    public void release(Activity activity) {
        if (se != null) {
            se.release(activity);
        }
    }

    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        if (se != null) {
            se.onActivityResult(activity, requestCode, resultCode, data);
        }
    }

    public void onNewIntent(Activity activity, Intent intent) {
        if (se != null) {
            se.onNewIntent(activity, intent);
        }
    }
}
