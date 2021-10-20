package com.donews.common.updatedialog;

import android.content.Context;

import com.donews.base.utils.ToastUtil;
import com.donews.common.BuildConfig;
import com.donews.common.contract.ApplyUpdateBean;
import com.donews.network.EasyHttp;
import com.donews.network.cache.model.CacheMode;
import com.donews.network.callback.SimpleCallBack;
import com.donews.network.exception.ApiException;
import com.donews.utilslibrary.base.UtilsConfig;
import com.donews.utilslibrary.utils.DateTimeUtils;
import com.donews.utilslibrary.utils.DeviceUtils;
import com.donews.utilslibrary.utils.SPUtils;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;


/**
 * @author by SnowDragon
 * Date on 2021/3/23
 * Description:
 */
public class UpdateManager {
    private static UpdateManager instance;
    private static final String TIMED_TASK_UP_UPDATE_TIME = "timed_task_update_time";
    public static final String APK_INFO = BuildConfig.TASK_URL + "apk/info";

    private UpdateManager() {
    }

    public static UpdateManager getInstance() {
        if (instance == null) {
            synchronized(UpdateManager.class) {
                if (instance == null) {
                    instance = new UpdateManager();
                }
            }
        }
        return instance;
    }

    CompositeDisposable compositeDisposable;

    /**
     * 检查更新
     *
     * @param context context
     */
    public void checkUpdate(Context context) {
        checkUpdate(context, true, null);
    }

    /**
     * 检查更新
     *
     * @param context                context
     * @param commonUpdateShowDialog true: 显示检查更新时，没有更新，文字提示
     */
    public void checkUpdate(Context context, final boolean commonUpdateShowDialog) {
        checkUpdate(context, commonUpdateShowDialog, null);
    }

    /**
     * 调用检查更新
     *
     * @param context                                             context
     * @param commonUpdateShowDialog，主动调用时这个值应为true，在没有新版本的会有提示信息
     * @param updateListener                                      版本检查更新监听
     */
    public void checkUpdate(Context context, final boolean commonUpdateShowDialog, UpdateListener updateListener) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(checkVersionUpdate(context, commonUpdateShowDialog, updateListener));
    }


    /**
     * 客户端更新
     *
     * @param context
     * @param commonUpdateShowDialog
     * @return
     */
    public Disposable checkVersionUpdate(Context context, final boolean commonUpdateShowDialog,
            UpdateListener updateListener) {

        return EasyHttp.get(APK_INFO)
                .params("package_name", DeviceUtils.getPackage())
                .params("channel", DeviceUtils.getChannelName())
//                .params("channel", "cdbx")
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<ApplyUpdateBean>() {
                    @Override
                    public void onError(ApiException e) {
                        if (updateListener != null) {
                            updateListener.onError(e.getMessage());
                        }
                    }

                    @Override
                    public void onSuccess(ApplyUpdateBean updateBean) {
                        verifyYetShowUpdateDialog(context, updateBean, commonUpdateShowDialog, updateListener);
                    }
                });
    }

    /**
     * 校验是否展示更新弹窗
     *
     * @param context    上下文
     * @param updateBean 更新类
     */
    private void verifyYetShowUpdateDialog(Context context, ApplyUpdateBean updateBean,
            final boolean commonUpdateShowDialog, UpdateListener updateListener) {
        if (updateBean == null) {
            return;
        }
        if (updateBean.getVersion_code() <= DeviceUtils.getAppVersionCode()) {
            //版本更新回调
            if (updateListener != null) {
                updateListener.update(false, false);
            }
            if (commonUpdateShowDialog) {
                ToastUtil.show(UtilsConfig.getApplication(), "当前已是最新版本！");
            }
            return;
        }
        if (updateListener != null) {
            updateListener.update(true, updateBean.getForce_upgrade() == 1);
        }
        //主动调用检查更新、或者强制更新
        if (commonUpdateShowDialog || updateBean.getForce_upgrade() == 1) {
            UpdateActivityDialog.showUpdateDialog(context, updateBean);
            return;
        }

        //定时任务状态，非强制更新一天出现一次弹窗
        if (!DateTimeUtils.isSameDay(System.currentTimeMillis(),
                SPUtils.getLongInformain(TIMED_TASK_UP_UPDATE_TIME, 0))) {

            UpdateActivityDialog.showUpdateDialog(context, updateBean);
            SPUtils.setInformain(TIMED_TASK_UP_UPDATE_TIME, System.currentTimeMillis());
        }

    }

    /**
     * 清空资源
     */
    public void disposed() {
        try {
            if (compositeDisposable != null) {
                compositeDisposable.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public interface UpdateListener {
        /**
         * @param needUpdate true:表示需要更新
         * @param isForce    true: 强制更新
         */
        void update(boolean needUpdate, boolean isForce);

        /**
         * @param errorMsg 获取更新错误信息
         */
        void onError(String errorMsg);
    }
}
