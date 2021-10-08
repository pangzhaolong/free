/*
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.donews.network.subsciber;

import android.content.Context;

import com.donews.network.callback.CallBack;
import com.donews.network.callback.ProgressDialogCallBack;
import com.donews.network.exception.ApiException;

import io.reactivex.annotations.NonNull;


/**
 * <p>描述：带有callBack的回调</p>
 * 主要作用是不需要用户订阅，只要实现callback回调<br>
 * 作者： created by honeylife<br>
 * 日期： 2016/12/28 17:10<br>
 * 版本： v2.0<br>
 */
public class CallBackSubsciber<T> extends BaseSubscriber<T> {
    public CallBack<T> mCallBack;


    public CallBackSubsciber(Context context, CallBack<T> callBack) {
        this(context, callBack, true);
    }

    public CallBackSubsciber(Context context, CallBack<T> callBack, boolean isShowToast) {
        super(context, isShowToast);
        mCallBack = callBack;
        if (callBack instanceof ProgressDialogCallBack) {
            ((ProgressDialogCallBack) callBack).subscription(this);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (mCallBack != null) {
            mCallBack.onStart();
        }
    }

    @Override
    public void onError(ApiException e) {
        if (mCallBack != null) {
            mCallBack.onError(e);
        }
    }

    @Override
    public void onNext(@NonNull T t) {
        super.onNext(t);
        if (mCallBack != null) {
            mCallBack.onSuccess(t);
        }
    }

    @Override
    public void onComplete() {
        super.onComplete();
        if (mCallBack != null) {
            mCallBack.onCompleted();
        }
    }

    @Override
    public void onCompleteOk() {
        super.onCompleteOk();
        if (mCallBack != null) {
            mCallBack.onCompleteOk();
        }
    }
}
