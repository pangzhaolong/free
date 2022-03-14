package com.donews.common.updatedialog;

import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.donews.base.appdialog.activity.BaseAppDialogActivity;
import com.donews.base.utils.ToastUtil;
import com.donews.common.R;
import com.donews.common.contract.ApplyUpdateBean;
import com.donews.common.databinding.CommonUpdateSkipDialogBinding;
import com.donews.common.download.DownloadListener;
import com.donews.common.download.DownloadManager;

/**
 * 更新取消的确认弹出框
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/11 10:09
 */
public class UpdateSkipDialog extends BaseAppDialogActivity {

    private static final String UPDATE_ENTITY = "update_entity";

    public static void showSkipDialog(Context context, ApplyUpdateBean bean) {
        context.startActivity(new Intent(context, UpdateSkipDialog.class)
                .putExtra(UPDATE_ENTITY, bean));
    }

    private CommonUpdateSkipDialogBinding mBinding;
    private ApplyUpdateBean mUpdateBean;


    @Override
    public void initView() {
        if (getIntent() != null) {
            mUpdateBean = (ApplyUpdateBean) getIntent().getSerializableExtra(UPDATE_ENTITY);
        }
        if (mUpdateBean == null) {
            finish();
        }
        mBinding.setEventListener(new EventListener());
    }

    @Override
    public void setDataBinding() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.common_update_skip_dialog);
        setFinishOnTouchOutside(false);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private void downloadApk(Context context, ApplyUpdateBean updateBean) {
        //注意这里传入applicationContext，否则在下载完成的时候调用finish，导致上下文无效，无法升级
        DownloadManager downloadManager = new DownloadManager(context.getApplicationContext(), getPackageName(),
                updateBean.getApk_url(), null);
        downloadManager.setImmInstall(true);
        downloadManager.start();
    }


    public class EventListener {
        public void clickNextTime(View view) {
            UpdateSkipDialog.this.finish();
        }

        public void clickUpdate(View view) {
            UpdateSkipDialog.this.downloadApk(UpdateSkipDialog.this, mUpdateBean);
            UpdateSkipDialog.this.finish();
        }
    }
}
