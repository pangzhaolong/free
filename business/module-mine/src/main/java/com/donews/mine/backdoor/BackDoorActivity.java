package com.donews.mine.backdoor;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.donews.base.activity.MvvmBaseLiveDataActivity;
import com.donews.base.viewmodel.BaseLiveDataViewModel;
import com.donews.mine.BuildConfig;
import com.donews.mine.R;
import com.donews.mine.databinding.MineActivityBackDoorBinding;
import com.donews.utilslibrary.utils.DeviceUtils;
import com.donews.utilslibrary.utils.KeySharePreferences;
import com.donews.utilslibrary.utils.SPUtils;

public class BackDoorActivity extends MvvmBaseLiveDataActivity<MineActivityBackDoorBinding, BaseLiveDataViewModel> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /**
     * 复制粘贴
     *
     * @return
     */
    public void onClickCopy(String code) {
        //获取剪贴板管理器：
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("Label", code);
        // 将ClipData内容放到系统剪贴板里。
        if (cm == null) return;
        cm.setPrimaryClip(mClipData);
        Toast.makeText(this, "复制成功", Toast.LENGTH_SHORT).show();
    }



    @Override
    public int getLayoutId() {
        return R.layout.mine_activity_back_door;
    }

    @Override
    public void initView() {
        mDataBinding.titleBar.setTitle("后门");
        mDataBinding.tvSuuidValue.setText(String.format("%s", DeviceUtils.getMyUUID()));
        mDataBinding.tvUserIdValue.setText(String.format("%s", SPUtils.getInformain(KeySharePreferences.USER_ID, "0")));
        mDataBinding.tvPackValue.setText(String.format("%s", DeviceUtils.getPackage()));
        mDataBinding.tvChannelCodeValue.setText(String.format("%s", DeviceUtils.getChannelName()));
        mDataBinding.tvSurroundingsValue.setText(BuildConfig.HTTP_DEBUG ? "测试" : "正式");
        mDataBinding.tvVersionValue.setText(String.format("%s", DeviceUtils.getVersionName()));
        mDataBinding.tvVersionCodeValue.setText(String.format("%s", DeviceUtils.getAppVersionCode()));
        mDataBinding.rlSuuid.setOnLongClickListener(view -> {
            onClickCopy(mDataBinding.tvSuuidValue.getText() + "");
            return false;
        });

        mDataBinding.rlUserId.setOnLongClickListener(view -> {
            onClickCopy(mDataBinding.tvUserIdValue.getText() + "");
            return false;
        });
    }


}