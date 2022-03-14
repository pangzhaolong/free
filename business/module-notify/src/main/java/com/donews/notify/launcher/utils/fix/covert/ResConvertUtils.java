package com.donews.notify.launcher.utils.fix.covert;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.widget.ImageView;

import com.blankj.utilcode.util.AppUtils;
import com.donews.base.base.BaseApplication;
import com.donews.base.utils.glide.GlideUtils;

import java.io.ByteArrayOutputStream;

/**
 * @author lcl
 * Date on 2022/1/12
 * Description:
 * 资源转换,但是只处理自己转换过得数据
 */
public class ResConvertUtils {

    //追加的标记
    private static String CONVERT_FLG = "convert://";

    /**
     * 绑定图标。但是图标可能是存在转换的内容。所以通过此方法绑定
     * @param icon
     * @param content
     * @return T:已成功转换。F:未处理
     */
    public static boolean buidIcon(ImageView icon,String content){
        if(content.startsWith(CONVERT_FLG)){
            icon.setImageDrawable(string2Drawable(content));
            return true;
        }else{
            GlideUtils.loadImageView(icon.getContext(),content,icon);
            return false;
        }
    }

    /**
     * 将drawable转换为String
     *
     * @param drawable
     * @return null 表示转换错误,或者不是自己转换过的数据
     */
    public static String drawable2String(Drawable drawable) {
        try {
            return CONVERT_FLG + bitmapToStrByBase64(drawableToBitmap(drawable));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 将String转为Drawable
     *
     * @param drawableByte64
     * @return null :表示不存在或者转换错误
     */
    public static Drawable string2Drawable(String drawableByte64) {
        if (drawableByte64 == null || drawableByte64.isEmpty() || !drawableByte64.startsWith(CONVERT_FLG)) {
            return null;
        }
        if (drawableByte64.length() < CONVERT_FLG.length()) {
            return null;
        }
        String convertOldStr = drawableByte64.substring(CONVERT_FLG.length());
        return bitmapToDrawable(base64ToBitmap(convertOldStr));
    }

    /**
     * 将bitmap转为byte64
     *
     * @param bit
     * @return
     */
    private static String bitmapToStrByBase64(Bitmap bit) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.PNG, 100, bos);//参数100表示不压缩
        byte[] bytes = bos.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    /**
     * byte64转为bitmap
     *
     * @param base64Data
     * @return
     */
    private static Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return bitmap;
    }

    /**
     * 将Bit转为Draw
     *
     * @param bitmap
     * @return
     */
    private static Drawable bitmapToDrawable(Bitmap bitmap) {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.TRANSPARENT);
        Paint paint = new Paint();
        canvas.drawBitmap(bitmap, 0, 0, paint);
        BitmapDrawable bd = new BitmapDrawable(
                BaseApplication.getInstance().getResources(), newBitmap);
        Drawable img = bd;
        return img;
    }

    /**
     * Drawable 转  bitmap
     */
    private static Bitmap drawableToBitmap(Drawable img) {
        BitmapDrawable bd = (BitmapDrawable) img;
        return bd.getBitmap();
    }

}
