package com.donews.share;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.donews.common.R;
import com.donews.utilslibrary.utils.BaseToast;
import com.donews.utilslibrary.utils.ImageBase64Util;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @Author: honeylife
 * @CreateDate: 2020/4/15 17:54
 * @Description:
 */
public class WXShareExecutor extends ShareExecutor {

    private final Activity mActivity;

    public WXShareExecutor(Activity activity) {
        mActivity = activity;
    }


    //bitmap中的透明色用白色替换
    public static Bitmap changeColor(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int[] colorArray = new int[w * h];
        int n = 0;
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                int color = getMixtureWhite(bitmap.getPixel(j, i));
                colorArray[n++] = color;
            }
        }
        return Bitmap.createBitmap(colorArray, w, h, Bitmap.Config.ARGB_8888);
    }


    @Override
    public void share(final int cmd, final ShareItem shareInfo, FragmentActivity baseActivity) {
        if (shareInfo.getType() == ShareItem.TYPE_IMAGE) {
            Glide.with(mActivity)
                    .asBitmap()
                    .load(shareInfo.getIcon())
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            Log.e("TAG", "=E===" + shareInfo.toString());
                            shareImage(cmd, resource, shareInfo);
                        }
                    });

        } else if (shareInfo.getType() == ShareItem.TYPE_H5) {
            if (TextUtils.isEmpty(shareInfo.getImageUrl())) {
                doShare(cmd, null, shareInfo);
            } else {

                Glide.with(mActivity)
                        .asBitmap()
                        .load(shareInfo.getIcon())
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                doShare(cmd, resource, shareInfo);
                            }
                        });
            }
        } else if (shareInfo.getType() == ShareItem.TYPE_TEXT) {
            doShareText(cmd, shareInfo);
        }


    }

    @Override
    public void release(Activity activity) {

    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void onNewIntent(Activity activity, Intent intent) {

    }

    public void shareImage(int cmd, Bitmap bitmap, ShareItem shareItem) {
        if (shareItem == null || TextUtils.isEmpty(shareItem.getImageUrl())) {
            return;
        }
        WXImageObject imgObj = new WXImageObject(bitmap);
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;
        if (bitmap != null) {
            int WX_THUMB_SIZE = 120;
            int size = Math.min(bitmap.getWidth(), bitmap.getHeight());
            int x = (bitmap.getWidth() - size) / 2;
            int y = (bitmap.getHeight() - size) / 2;
            Bitmap squared = Bitmap.createBitmap(bitmap, x, y, size, size);
            Bitmap thumbBmp = Bitmap.createScaledBitmap(squared, WX_THUMB_SIZE, WX_THUMB_SIZE, true);
            squared.recycle();
            msg.setThumbImage(thumbBmp);
//            msg.thumbData = bmpToByteArray(thumbBmp, true);
        } else {
            int WX_THUMB_SIZE = 120;
            Bitmap bitmapRes = BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.ic_launcher_round);
            Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmapRes, WX_THUMB_SIZE, WX_THUMB_SIZE, true);
            bitmapRes.recycle();
            msg.setThumbImage(thumbBmp);
//            msg.thumbData = bmpToByteArray(thumbBmp, true);
        }

        WXHolderHelp.shareImage(cmd, msg);
    }

    //获取和白色混合颜色
    private static int getMixtureWhite(int color) {
        int alpha = Color.alpha(color);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.rgb(getSingleMixtureWhite(red, alpha), getSingleMixtureWhite(green, alpha),
                getSingleMixtureWhite(blue, alpha));
    }

    // 获取单色的混合值
    private static int getSingleMixtureWhite(int color, int alpha) {
        int newColor = color * alpha / 255 + 255 - alpha;
        return newColor > 255 ? 255 : newColor;
    }

    private void doShare(int cmd, Bitmap bitmap, ShareItem shareItem) {
        if (!WXHolderHelp.isWXAppInstalled()) {
            Toast.makeText(mActivity, "请您安装微信", Toast.LENGTH_SHORT).show();
            return;
        }
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = shareItem.getShareWebUrl();
        WXMediaMessage msg = new WXMediaMessage(webpage);
        if (cmd == ShareManager.SHARE_COMMAND_WX_FRIEND && !TextUtils.isEmpty(shareItem.getContent())) {
            msg.title = shareItem.getContent();
        } else {
            msg.title = shareItem.getTitle();
        }
        msg.description = cmd == ShareManager.SHARE_COMMAND_WX_FRIEND ? "" : shareItem.getContent();
        if (bitmap != null) {
            int WX_THUMB_SIZE = 140;
            int size = Math.min(bitmap.getWidth(), bitmap.getHeight());
            int x = (bitmap.getWidth() - size) / 2;
            int y = (bitmap.getHeight() - size) / 2;
            Bitmap squared = Bitmap.createBitmap(bitmap, x, y, size, size);
            Bitmap thumbBmp = Bitmap.createScaledBitmap(squared, WX_THUMB_SIZE, WX_THUMB_SIZE, true);
            squared.recycle();
            msg.setThumbImage(changeColor(thumbBmp));
        } else {
            int WX_THUMB_SIZE = 140;
            Bitmap bitmapRes = BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.ic_launcher_round);
            Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmapRes, WX_THUMB_SIZE, WX_THUMB_SIZE, true);
            bitmapRes.recycle();
            msg.setThumbImage(changeColor(thumbBmp));
        }
        WXHolderHelp.share(cmd, shareItem, msg);
    }

    public void doShareText(int cmd, ShareItem shareItem) {
        if (!WXHolderHelp.isWXAppInstalled()) {
            Toast.makeText(mActivity, "请您安装微信", Toast.LENGTH_SHORT).show();
            return;
        }
        String text = !TextUtils.isEmpty(shareItem.getContent()) ? shareItem.getContent() : "健康赚邀请您一起来走路赚金币提现啦！";
        //初始化一个 WXTextObject 对象，填写分享的文本内容
        WXTextObject textObj = new WXTextObject();
        textObj.text = text;

        //用 WXTextObject 对象初始化一个 WXMediaMessage 对象
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObj;
        msg.description = text;
        WXHolderHelp.shareText(cmd, shareItem, msg);

    }

    /**
     * 分享图片，来自sd卡的图片分享
     */
    public void onShareImage(int cmd, ShareItem shareItem) {
        if (cmd == 0 || shareItem == null || TextUtils.isEmpty(shareItem.getImageUrl())) {
            BaseToast.makeToast(mActivity).setToastLongText("分享数据出错啦").showToast();
            return;
        }
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(shareItem.getImageUrl());

            Bitmap bitmap = BitmapFactory.decodeStream(fis);

            WXImageObject wxImageObject = new WXImageObject(WXShareExecutor.changeColor(bitmap));
            WXMediaMessage msg = new WXMediaMessage();
            msg.mediaObject = wxImageObject;
            //设置缩略图
            Bitmap mBp = Bitmap.createScaledBitmap(bitmap, 120, 120, true);
            bitmap.recycle();
            msg.thumbData = bmpToByteArray(mBp, true);
            mBp.recycle();
            WXHolderHelp.shareImage(cmd, msg);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 来自base64数据整合，分享图片
     */
    public void onShareImageBase64(int cmd, ShareItem shareItem) {
        if (cmd == 0 || shareItem == null || TextUtils.isEmpty(shareItem.getImageUrl())) {
            BaseToast.makeToast(mActivity).setToastLongText("分享数据出错啦").showToast();
            return;
        }
        BaseToast.makeToast(mActivity).setToastLongText("启动微信中").showToast();

        try {
            ImageBase64Util imageBase64Util = new ImageBase64Util();
            final Bitmap bp = imageBase64Util.stringtoBitmap(shareItem.getImageUrl());
//        Bitmap bp = IMServer.getDiskBitmap(IMServer.url);
            WXImageObject wxImageObject = new WXImageObject(bp);
            WXMediaMessage msg = new WXMediaMessage();
            msg.mediaObject = wxImageObject;
            //设置缩略图
            Bitmap mBp = Bitmap.createScaledBitmap(bp, 120, 240, true);
            bp.recycle();
            msg.thumbData = imageBase64Util.bmpToByteArray(mBp, false);
            mBp.recycle();
            WXHolderHelp.shareImage(cmd, msg);
            // 设置进入微信
            ShareWeixinApp.getWeixinApp().setWeixin(true);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private byte[] bmpToByteArray(Bitmap bmp, boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}

