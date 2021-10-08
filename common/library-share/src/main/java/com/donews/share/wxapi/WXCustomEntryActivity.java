package com.donews.share.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.donews.share.ISWXSuccessCallBack;
import com.donews.share.KeyConstant;
import com.donews.share.ShareItem;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WXCustomEntryActivity extends Activity implements IWXAPIEventHandler {
    private static String TAG = "WXEntryActivity";

    // 判断到底是绑定微信还是登录微信
    public static int state = 0;
    public static ShareItem shareItem;
    public static int cmd;
    private IWXAPI api;
    public static ISWXSuccessCallBack mSuccessCallBack;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: 打开微信页面");
        api = WXAPIFactory.createWXAPI(this, KeyConstant.getWxApi(), false);
        try {
            Intent intent = getIntent();
            api.handleIntent(intent, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "onNewIntent: ");
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
        Log.d(TAG, "onReq:的值为:" + req.getType());
        switch (req.getType()) {
            case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
//                goToGetMsg();
                break;
            case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
//                goToShowMsg((ShowMessageFromWX.Req) req);
                break;
            default:
                break;
        }
//        finish();
    }

    @Override
    public void onResp(BaseResp resp) {

        int result = 0;
        Log.d(TAG, "onResp: resp.errCode:" + resp.errCode);
        Log.d(TAG, "onResp: resp.type:" + resp.getType());
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                Log.d(TAG, "onResp: type:" + resp.getType());
                if (resp.getType() == ConstantsAPI.COMMAND_SENDAUTH) {
                    String code = ((SendAuth.Resp) resp).code;
                    Log.d(TAG, "onResp: code:" + code);
                    Log.e(TAG, "state的值为" + state);
//                    switch (state) {
//                        case WXHolderHelp.STATE_LOGIN:
//                            WXUserInfo.onWXLogin(code, mSuccessCallBack);
//
//                            break;
//                        case WXHolderHelp.STATE_BINDING:
//                            WXUserInfo.bindWX(code, mSuccessCallBack);
//                            break;
//
//
//
//                    }
                    mSuccessCallBack.onSuccess(state,code);
                    finish();
                } else if (resp.getType() == ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX) {
                    Log.d(TAG, "onResp: finish");
                    Toast.makeText(WXCustomEntryActivity.this, "分享成功", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                finish();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                Log.d(TAG, "onResp: 用户拒绝授权");
                finish();
                break;
            case BaseResp.ErrCode.ERR_UNSUPPORT:
                Log.d(TAG, "onResp: 用户取消授权");
                if (resp.getType() == 2) {
                    Log.d(TAG, "onResp: finish");
                    finish();
                }
                break;
            default:
                finish();
                break;
        }

    }
}