package com.dn.feedback.request;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.donews.base.base.BaseApplication;
import com.donews.base.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lcl
 * Date on 2022/2/9
 * Description:
 */
public class PhotoAlbumResultContract extends ActivityResultContract<Boolean, List<String>> {

    @NonNull
    @Override
    public Intent createIntent(@NonNull Context context, Boolean input) {
        Intent intent = new Intent(Intent.ACTION_PICK,null);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        return intent;
    }

    @Override
    public List<String> parseResult(int resultCode, @Nullable Intent intent) {
        List<String> list = new ArrayList<>();
        if (resultCode == Activity.RESULT_OK) {
            if (intent != null) {
                ClipData imageNames = intent.getClipData();
                if (imageNames != null) {
                    for (int i = 0; i < imageNames.getItemCount(); i++) {
                        Uri imageUri = imageNames.getItemAt(i).getUri();
                        list.add(imageUri.toString());
                    }
                    //uri = imageNames.getItemAt(0).getUri();
                } else {
                    list.add(intent.getData().toString());
                    //fileList.add(uri.toString());
                }
            } else {
                ToastUtil.showShort(BaseApplication.getInstance(), "获取图片失败");
            }
        } else {
            ToastUtil.showShort(BaseApplication.getInstance(), "取消选择图片");
        }
        return list;
    }
}
