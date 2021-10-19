package com.donews.common.updatedialog;

import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.donews.base.appdialog.activity.BaseAppDialogActivity;
import com.donews.base.utils.ToastUtil;
import com.donews.common.BR;
import com.donews.common.R;
import com.donews.common.contract.ApplyUpdateBean;
import com.donews.common.databinding.CommonUpdateDialogBinding;
import com.donews.common.download.DownloadListener;
import com.donews.common.download.DownloadManager;

/**
 * @author by SnowDragon
 * Date on 2021/3/23
 * Description:
 */
public class UpdateActivityDialog extends BaseAppDialogActivity {
    private static final String UPDATE_ENTITY = "update_entity";

    public static void showUpdateDialog(Context context, ApplyUpdateBean bean) {
        context.startActivity(new Intent(context, UpdateActivityDialog.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .putExtra(UPDATE_ENTITY, bean));

    }

    private ApplyUpdateBean updateBean;
    private CommonUpdateDialogBinding mDataBinding;
    DownloadManager downloadManager;

    @Override
    public void initView() {
        if (getIntent() != null) {
            updateBean = (ApplyUpdateBean) getIntent().getSerializableExtra(UPDATE_ENTITY);
        }
        if (updateBean == null) {
            finish();
        }
        mDataBinding.setUpdataBean(updateBean);
        mDataBinding.groupProgress.setVisibility(View.GONE);
    }


    @Override
    public void setDataBinding() {
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.common_update_dialog);
        mDataBinding.setVariable(BR.clickProxy, new ClickProxy());
        //设置点击空白处不关闭
        setFinishOnTouchOutside(false);
    }

    public class ClickProxy {
        public void immUpgrade(View view) {
            boolean forceUpgrade = updateBean.getForce_upgrade() == 1;
            downloadApk(UpdateActivityDialog.this, forceUpgrade);
            if (forceUpgrade) {
                mDataBinding.groupProgress.setVisibility(View.VISIBLE);
                mDataBinding.btnForceUpgrade.setVisibility(View.GONE);
            } else {
                UpdateActivityDialog.this.finish();
            }
        }

        public void skipUpdate(View view) {
            UpdateSkipDialog.showSkipDialog(UpdateActivityDialog.this, updateBean);
            UpdateActivityDialog.this.finish();
        }
    }

    private void downloadApk(Context context, boolean callback) {
        //注意这里传入applicationContext，否则在下载完成的时候调用finish，导致上下文无效，无法升级
        DownloadListener listener = null;
        if (callback) {
            listener = new DownloadListener() {
                @Override
                public void onStart() {

                }

                @Override
                public void updateProgress(long currentLength, long totalLength, int progress) {
                    UpdateActivityDialog.this.updateProgress(currentLength, totalLength, progress);
                }

                @Override
                public void downloadComplete(String pkName, String path) {
                    updateBean.setProgress(100);
                    UpdateActivityDialog.this.finish();
                }

                @Override
                public void downloadError(String error) {
                    ToastUtil.show(getApplicationContext(), "下载失败");
                }
            };
        }
        downloadManager = new DownloadManager(context.getApplicationContext(), getPackageName(),
                updateBean.getApk_url(), listener);
        downloadManager.setImmInstall(true);
        downloadManager.start();
    }

    private void updateProgress(long currentLength, long totalLength, int progress) {
        updateBean.setProgress(progress);
        String text = getString(R.string.common_update_progress, progress);
        if (mDataBinding != null) {
            mDataBinding.pbProgress.setProgress(progress);
            mDataBinding.tvProgress.setText(text);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (updateBean.getForce_upgrade() == 1) {
            if (downloadManager != null) {
                downloadManager.pause();
            }
        }
        UpdateManager.getInstance().disposed();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
