package com.donews.web.model;

import com.donews.base.model.BaseLiveDataModel;
import com.donews.network.EasyHttp;
import com.donews.network.cache.model.CacheMode;
import com.donews.network.callback.SimpleCallBack;
import com.donews.network.exception.ApiException;
import com.donews.web.BuildConfig;

import org.json.JSONObject;

import io.reactivex.disposables.Disposable;

/**
 * <p> </p>
 * 作者： created by honeylife<br>
 * 日期： 2020/12/11 16:22<br>
 * 版本：V1.0<br>
 */
public class WebModels extends BaseLiveDataModel {
    private Disposable disposable;



    public void onUpdateTask(int id) {
        String data = "";
        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", id);
            jsonObject.put("is_append", 1);
            data = jsonObject.toString();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        disposable = EasyHttp.post(BuildConfig.TASK_URL + "task/update")
                .upJson(data)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<Object>() {

                    @Override
                    public void onError(ApiException e) {

                    }

                    @Override
                    public void onSuccess(Object o) {
                    }

                    @Override
                    public void onCompleteOk() {
                        super.onCompleteOk();

                    }
                });
    }

    public void onScoreAdd(int id) {
        String data = "";
        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", id);
            data = jsonObject.toString();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        disposable = EasyHttp.post(BuildConfig.TASK_URL + "score/add")
                .upJson(data)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<Object>() {

                    @Override
                    public void onError(ApiException e) {

                    }

                    @Override
                    public void onSuccess(Object o) {

                    }

                    @Override
                    public void onCompleteOk() {
                        super.onCompleteOk();

                    }
                });

    }

}
