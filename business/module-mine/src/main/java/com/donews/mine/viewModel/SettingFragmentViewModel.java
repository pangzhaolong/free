package com.donews.mine.viewModel;

import android.content.Context;
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
import com.donews.mine.model.SettingModel;
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

    //标题集合
    private List<String> itemTitles = new ArrayList() {
        {
            add("用户协议");
            add("隐私政策");
            add("意见反馈");
            add("关于我们");
            add("清除缓存");
            add("分享App");
        }
    };
    //点击监听
    private Map<Integer, Runnable> itemClicks = new HashMap() {
        {
            put(0, (Runnable) () -> { //用户协议
                Bundle bundle = new Bundle();
                bundle.putString("url", BuildConfig.HTTP_H5 + "SLAs");
                bundle.putString("title", "用户协议");
                ARouteHelper.routeSkip(RouterActivityPath.Web.PAGER_WEB_ACTIVITY, bundle);
            });
            put(1, (Runnable) () -> { //隐私政策
                Bundle bundle = new Bundle();
                bundle.putString("url", BuildConfig.HTTP_H5 + "privacy");
                bundle.putString("title", "隐私政策");
                ARouteHelper.routeSkip(RouterActivityPath.Web.PAGER_WEB_ACTIVITY, bundle);
            });
            put(2, (Runnable) () -> { //意见反馈
                helperAndUpload("意见反馈", "customer");
            });
            put(3, (Runnable) () -> { //关于我们
                ToastUtil.show(baseActivity, "关于我们");
            });
            put(4, (Runnable) () -> { //清除缓存
                ConfirmPopupWindow confirmPopupWindow = new ConfirmPopupWindow(baseActivity);
                confirmPopupWindow.show();
                confirmPopupWindow.setTitleText("确定清除缓存？").setOkOnClick(v -> {
                    confirmPopupWindow.hide();
                    clearAppCache();
                }).setCancelOnClick(v -> confirmPopupWindow.hide());
                ToastUtil.show(baseActivity, "清楚缓存");
            });
            put(5, (Runnable) () -> {//分享APP
                ToastUtil.show(baseActivity, "分享APP");
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
            return "邀请好友一起中奖";
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
        updateUIFlg.postValue(updateUIFlg.getValue() + 1);
    }


//
//    @Override
//    public void onLoadFinish(BaseModel model, BaseCustomViewModel data) {
//        if (data instanceof CacheBean) {
//            viewDataBinding.setVariable(BR.cacheBean, data);
//        } else if (data instanceof SignBean) {
//            signBean = (SignBean) data;
//            viewDataBinding.setVariable(BR.signBean, data);
//        } else if (data instanceof ApplyUpdataBean) {
//            updateLogic((ApplyUpdataBean) data);
//        }
//    }

    public void clearCache() {
        mModel.clearCache(baseActivity);
    }

    public void getCacheData() {
        mModel.getCacheData(baseActivity);
    }


}
