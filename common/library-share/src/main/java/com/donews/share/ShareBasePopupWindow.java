package com.donews.share;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.fragment.app.FragmentActivity;

import com.donews.common.base.popwindow.BaseSlideBottomPopupWindow;


public class ShareBasePopupWindow extends BaseSlideBottomPopupWindow {

    private ShareItem shareItem;
    private ShareManager shareManager;
    int cmd = -1;

    public ShareBasePopupWindow(FragmentActivity context) {
        super(context);
    }


    public void setShareItem(ShareItem shareItem) {
        this.shareItem = shareItem;
    }


    @Override
    protected int layoutId() {
        return R.layout.popupwindow_share;
    }

    @Override
    protected void initContentView() {
        bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
            }
        });
        shareManager = new ShareManager();
        final View.OnClickListener mClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                int id = v.getId();
                if (id == R.id.share_weixin) {
                    cmd = ShareManager.SHARE_COMMAND_WX;
                } else if (id == R.id.share_weixin_qun) {
                    cmd = ShareManager.SHARE_QUN_WX;
                } else if (id == R.id.share_weixin_quan) {
                    cmd = ShareManager.SHARE_COMMAND_WX_FRIEND;
                } else if (id == R.id.share_weixin_mian) {
                    cmd = ShareManager.SHARE_MIAN_WX;
                }
                onShare();
                hide();
            }

        };

        View mShareWeiXin = rootView.findViewById(R.id.share_weixin);
        mShareWeiXin.setOnClickListener(mClickListener);
        View mShareWeiXinQun = rootView.findViewById(R.id.share_weixin_qun);
        mShareWeiXinQun.setOnClickListener(mClickListener);
        View mShareWeiXinQuan = rootView.findViewById(R.id.share_weixin_quan);
        mShareWeiXinQuan.setOnClickListener(mClickListener);
        View mShareWeiXinMain = rootView.findViewById(R.id.share_weixin_mian);
        mShareWeiXinMain.setOnClickListener(mClickListener);

    }

    private void onShare() {
        if (cmd >= 0) {

            shareManager.share(cmd, shareItem, getBaseActivity());
            ShareWeixinApp.getWeixinApp().setWeixin(true);
        }
        Log.e("TAG", "====" + shareItem.toString());
    }

    @Override
    protected boolean autoDismiss() {
        return false;
    }

    @Override
    public void onResume() {

    }

    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        if (shareManager != null) {
            shareManager.onActivityResult(activity, requestCode, resultCode, data);
        }
    }

    public void onNewIntent(Activity activity, Intent intent) {
        if (shareManager != null) {
            shareManager.onNewIntent(activity, intent);
        }
    }
}
