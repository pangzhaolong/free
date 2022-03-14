package com.donews.web.javascript;

import android.text.TextUtils;
import android.webkit.JavascriptInterface;

import androidx.fragment.app.FragmentActivity;

import com.donews.share.ShareConstant;
import com.donews.share.ShareItem;
import com.donews.share.ShareManager;
import com.donews.share.WXShareExecutor;
import com.donews.utilslibrary.utils.LogUtil;


/**
 * @Author: honeylife
 * @CreateDate: 2020/6/2 10:02
 * @Description:
 */
public class CommonInterface extends CommonJSInterface {

    private final static String TAG = "CommonInterface";
    private FragmentActivity mContext;

    protected CommonInterface(FragmentActivity context) {
        this.mContext = context;
    }

    /**
     * @param base64Data base64的数据
     * @param page       页面
     * @param type       分享类型 1 微信好友，2微信朋友圈
     */
    @JavascriptInterface
    public void invitationFriends(String base64Data, String page, int type) {
        LogUtil.i("==A==" + base64Data);
        try {
            if (TextUtils.isEmpty(base64Data)) return;
            WXShareExecutor wxShareExecutor = new WXShareExecutor(mContext);
            int cmd = 0;
            ShareItem shareItem = new ShareItem();
            cmd = type == ShareConstant.WX ?
                    ShareManager.SHARE_COMMAND_WX :
                    ShareManager.SHARE_COMMAND_WX_FRIEND;
            shareItem.setImageUrl(base64Data);
            wxShareExecutor.onShareImageBase64(cmd, shareItem);
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

    /**
     * h5 事件统计
     *
     * @param eventName
     */
    @JavascriptInterface
    public void eventReport(String eventName) {
        LogUtil.i("eventReport: eventName：" + eventName);

    }

    /**
     * h5 上报大数据
     *
     * @param eventName
     */
    @JavascriptInterface
    public void eventReportData(String eventName) {
        LogUtil.i("eventReportData: eventName：" + eventName);
//        AnalysisUtils.onEvent(mContext, eventName);
    }

}
