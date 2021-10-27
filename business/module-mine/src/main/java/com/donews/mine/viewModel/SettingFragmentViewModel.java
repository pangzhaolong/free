package com.donews.mine.viewModel;

import static com.donews.common.router.RouterActivityPath.Mine.PAGER_MINE_ABOUT_ACTIVITY;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.CacheDiskStaticUtils;
import com.blankj.utilcode.util.CacheDiskUtils;
import com.dn.drouter.ARouteHelper;
import com.donews.base.popwindow.ConfirmPopupWindow;
import com.donews.base.utils.ToastUtil;
import com.donews.base.viewmodel.BaseLiveDataViewModel;
import com.donews.common.router.RouterActivityPath;
import com.donews.mine.Api.MineHttpApi;
import com.donews.mine.BuildConfig;
import com.donews.mine.R;
import com.donews.mine.databinding.MineSettingFragmentBinding;
import com.donews.mine.dialogs.ShareToDialogFragment;
import com.donews.mine.model.SettingModel;
import com.donews.mine.ui.AboutActivity;
import com.donews.share.ShareManager;
import com.donews.share.WXHolderHelp;
import com.donews.share.WXShareExecutor;
import com.donews.utilslibrary.analysis.AnalysisUtils;
import com.donews.utilslibrary.utils.AppCacheUtils;
import com.donews.utilslibrary.utils.DeviceUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettingFragmentViewModel extends BaseLiveDataViewModel<SettingModel> {
    private ViewDataBinding viewDataBinding;
    public LifecycleOwner lifecycleOwner;
    private FragmentActivity baseActivity;

    //更新UI显示的标志。可以通过监听此标志来达到更新UI的时机
    public MutableLiveData<Integer> updateUIFlg = new MutableLiveData<>(0);
    //微信分享管理对象
    public ShareManager shareManager = new ShareManager();

    //标题集合
    private List<String> itemTitles = new ArrayList() {
        {
            add("用户协议");
            add("隐私政策");
            add("意见反馈");
            add("关于我们");
            add("清除缓存");
            add("分享App");
            add("账户注销");
        }
    };
    //点击监听
    private Map<Integer, Runnable> itemClicks = new HashMap() {
        {
            put(0, (Runnable) () -> { //用户协议
                Bundle bundle = new Bundle();
                bundle.putString("url",
                        "http://ad-static-xg.tagtic.cn/wangzhuan/file/9e5f7a06cbf80a2186e3e34a70f0c360.html");
                bundle.putString("title", "用户协议");
                ARouteHelper.routeSkip(RouterActivityPath.Web.PAGER_WEB_ACTIVITY, bundle);
            });
            put(1, (Runnable) () -> { //隐私政策
                Bundle bundle = new Bundle();
                bundle.putString("url",
                        "http://ad-static-xg.tagtic.cn/wangzhuan/file/b7f18dcb857e80eab353cfb99c3f042e.html");
                bundle.putString("title", "隐私政策");
                ARouteHelper.routeSkip(RouterActivityPath.Web.PAGER_WEB_ACTIVITY, bundle);
            });
            put(2, (Runnable) () -> { //意见反馈
                Bundle bundle = new Bundle();
                bundle.putString("url",
                        "https://www.wjx.top/vm/YhnxHHh.aspx");
                bundle.putString("title", "意见反馈");
                ARouteHelper.routeSkip(RouterActivityPath.Web.PAGER_WEB_ACTIVITY, bundle);
            });
            put(3, (Runnable) () -> { //关于我们
                ARouter.getInstance().build(PAGER_MINE_ABOUT_ACTIVITY)
                        .navigation(baseActivity);
            });
            put(4, (Runnable) () -> { //清除缓存
                ConfirmPopupWindow confirmPopupWindow = new ConfirmPopupWindow(baseActivity);
                confirmPopupWindow.show();
                confirmPopupWindow.setTitleText("确定清除缓存？").setOkOnClick(v -> {
                    confirmPopupWindow.hide();
                    clearAppCache();
                }).setCancelOnClick(v -> confirmPopupWindow.hide());
            });
            put(5, (Runnable) () -> {//分享APP
                ShareToDialogFragment df = (ShareToDialogFragment) ARouter.getInstance()
                        .build(RouterActivityPath.Mine.DIALOG_SHARE_TO_DIALOG_FRAGMENT)
                        .navigation();
                df.setSelectListener(type -> {
                    share(type);
                });
                df.show(baseActivity.getSupportFragmentManager());
            });
            put(6, (Runnable) () -> {//账户注销
                ARouter.getInstance()
                        .build(RouterActivityPath.Mine.PAGER_MINEUSER_CANCELLATION_ACTIVITY)
                        .navigation();
            });
        }
    };

    public void setDataBinDing(MineSettingFragmentBinding dataBinding, FragmentActivity baseActivity) {
        this.viewDataBinding = dataBinding;
        this.baseActivity = baseActivity;
        dataBinding.setListener(this);
    }

    @Override
    public SettingModel createModel() {
        return new SettingModel();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    /**
     * 添加Item点击事件
     *
     * @param view 视图
     * @param pos  所属的是第几个菜单项
     */
    public void addItemClick(View view, int pos) {
        view.setOnClickListener(v -> {
            if (itemClicks.get(pos) != null) {
                itemClicks.get(pos).run();
            }
        });
    }

    /**
     * 获取列表的数据
     *
     * @param pos 第几个菜单的标题:
     * @return
     */
    public String getItemTitleName(int pos) {
        try {
            return itemTitles.get(pos);
        } catch (Exception e) {
            return "--";
        }
    }

    /**
     * 获取列表的右侧描述数据
     *
     * @param pos 第几个菜单的标题:
     * @return
     */
    public String getItemDescText(int pos) {
        if (pos == 1) {
//            return "邀请好友一起中奖";
            return "";
        } else if (pos == 4) { //垃圾
            return getAppCacheSize();
        } else {
            return "";
        }
    }

    /**
     * 获取APP缓存大小
     *
     * @return
     */
    private String getAppCacheSize() {
        try {
            return AppCacheUtils.getTotalCacheSize(baseActivity);
        } catch (Exception e) {
            e.printStackTrace();
            return "0M";
        }
    }

    /**
     * 帮助与反馈
     *
     * @param url
     * @param title
     */
    private void helperAndUpload(String title, String url) {
        ARouter.getInstance().build(RouterActivityPath.Web.PAGER_WEB_ACTIVITY)
                .withString("title", title)
                .withString("url", MineHttpApi.H5_URL + url)
                .navigation();
    }

    /**
     * 清除缓存
     */
    private void clearAppCache() {
        AppCacheUtils.cleanInternalCache(baseActivity);
        AppCacheUtils.clearAllCache(baseActivity);
        updateUIFlg.postValue(updateUIFlg.getValue() + 1);
    }

    /**
     * 分享app
     *
     * @param type 1-联系人，2-朋友圈
     */
    private void share(int type) {
        if(type == 1){
            ToastUtil.showShort(baseActivity,"分享到联系人");
        }else if(type ==2){
            ToastUtil.showShort(baseActivity,"分享到朋友圈");
        }else{
            ToastUtil.showShort(baseActivity,"暂不支持此类型分享");
        }
    }


}
