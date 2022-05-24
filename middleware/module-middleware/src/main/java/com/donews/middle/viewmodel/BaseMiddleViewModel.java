package com.donews.middle.viewmodel;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.donews.middle.BuildConfig;
import com.donews.middle.bean.mine2.resp.DailyTaskResp;
import com.donews.middle.model.MiddleModel;
import com.donews.network.EasyHttp;
import com.donews.network.cache.model.CacheMode;
import com.donews.network.callback.SimpleCallBack;
import com.donews.network.exception.ApiException;
import com.donews.utilslibrary.utils.HttpConfigUtilsKt;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lcl
 * Date on 2022/5/17
 * Description:
 */
public class BaseMiddleViewModel extends MiddleViewModel<MiddleModel> {

    /**
     * 全局单利的一个对象，再{@link #getBaseViewModel}的context参数使用Application既可获得当前对象
     */
    private static BaseMiddleViewModel _sBaseMiddleViewModel = new BaseMiddleViewModel();

    /**
     * 获取指定生命周期下的ViewModel
     *
     * @param owner
     * @return
     */
    public static BaseMiddleViewModel getBaseViewModel(ViewModelStoreOwner owner) {
        return new ViewModelProvider(owner).get(BaseMiddleViewModel.class);
    }

    /**
     * 获取指定生命周期下的ViewModel
     *
     * @return
     */
    public static BaseMiddleViewModel getBaseViewModel() {
        return _sBaseMiddleViewModel;
    }

    // 金币数量(个人中心的)
    public MutableLiveData<Integer> mine2JBCount = new MutableLiveData<>(0);
    // 积分数量(个人中心的)
    public MutableLiveData<Integer> mine2JFCount = new MutableLiveData<>(0);
    // 每日任务列表(个人中心的)
    public MutableLiveData<List<DailyTaskResp.DailyTaskItemResp>> mine2DailyTask = new MutableLiveData<>();

    /**
     * 创建Model
     *
     * @return Model
     */
    @Override
    public MiddleModel createModel() {
        return new MiddleModel();
    }


    /**
     * （新版本）获取每日任务
     */
    public void getDailyTasks(MutableLiveData<List<DailyTaskResp.DailyTaskItemResp>> liveData) {
        String url = (BuildConfig.BASE_QBN_API + "activity/v1/daily-tasks");
        EasyHttp.get(HttpConfigUtilsKt.withConfigParams(url, true))
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<DailyTaskResp>() {
                    @Override
                    public void onError(ApiException e) {
                        if (liveData != null) {
                            liveData.postValue(null);
                        }
                        mine2DailyTask.postValue(null);
                    }

                    @Override
                    public void onSuccess(DailyTaskResp dailyTaskResp) {
                        if (dailyTaskResp.list != null) {
                            mine2DailyTask.postValue(dailyTaskResp.list);
                            if (liveData != null) {
                                liveData.postValue(dailyTaskResp.list);
                            }
                        } else {
                            mine2DailyTask.postValue(new ArrayList<>());
                            if (liveData != null) {
                                liveData.postValue(new ArrayList<>());
                            }
                        }
                    }
                });
    }
}
