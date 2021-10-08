package com.donews.mine.ui;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.donews.base.activity.MvvmBaseLiveDataActivity;
import com.donews.base.viewmodel.BaseLiveDataViewModel;
import com.donews.mine.Api.MineHttpApi;
import com.donews.mine.R;
import com.donews.mine.bean.CodeBean;
import com.donews.mine.databinding.MineInvitationCodeBinding;
import com.donews.network.EasyHttp;
import com.donews.network.cache.model.CacheMode;
import com.donews.network.callback.SimpleCallBack;
import com.donews.network.exception.ApiException;
import com.donews.utilslibrary.utils.BaseToast;
import com.donews.utilslibrary.utils.LogUtil;

/**
 * <p> </p>
 * 作者： created by honeylife<br>
 * 日期： 2020/12/2 16:00<br>
 * 版本：V1.0<br>
 */
public class InvitationCodeActivity extends MvvmBaseLiveDataActivity<MineInvitationCodeBinding, BaseLiveDataViewModel> {


    @Override
    protected int getLayoutId() {
        return R.layout.mine_invitation_code;
    }

    @Override
    public void initView() {
        mDataBinding.titleBar.setTitle("邀请码");
        initLin();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

      
    }

    private void initLin() {
        mDataBinding.ivCloseInvitationCode.setOnClickListener(v -> mDataBinding.edtInvitationCode.setText(""));
        mDataBinding.rlUploadCode.setOnClickListener(v -> onPutCode());

    }

    private void onPutCode() {
        if (TextUtils.isEmpty(getEditTextStr())) {
            BaseToast.makeToast(InvitationCodeActivity.this).setToastLongText("请填写邀请码").showToast();
            return;
        }
        CodeBean codeBean = new CodeBean();
        codeBean.setCode(getEditTextStr());
        LogUtil.i(codeBean.toString());
        EasyHttp.put(MineHttpApi.CODE)
                .upJson(codeBean.toString()).cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<CodeBean>() {
                    @Override
                    public void onError(ApiException e) {

                    }

                    @Override
                    public void onSuccess(CodeBean codeBean) {

                    }

                    @Override
                    public void onCompleteOk() {
                        super.onCompleteOk();
                        BaseToast.makeToast(InvitationCodeActivity.this).setToastLongText("绑定成功").showToast();
                        InvitationCodeActivity.this.finish();
                    }
                });
    }

    private String getEditTextStr() {
        return mDataBinding.edtInvitationCode.getText().toString();
    }
}
