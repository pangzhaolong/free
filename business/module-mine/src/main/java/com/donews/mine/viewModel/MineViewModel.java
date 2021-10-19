package com.donews.mine.viewModel;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;

import com.alibaba.android.arouter.launcher.ARouter;
import com.donews.base.viewmodel.BaseLiveDataViewModel;
import com.donews.common.contract.LoginHelp;
import com.donews.common.router.RouterActivityPath;
import com.donews.mine.Api.MineHttpApi;
import com.donews.mine.bean.QueryBean;
import com.donews.mine.databinding.MineFragmentBinding;
import com.donews.mine.model.MineModel;
import com.donews.mine.ui.InvitationCodeActivity;
import com.donews.mine.ui.SettingActivity;
import com.donews.mine.ui.UserCenterActivity;
import com.donews.utilslibrary.utils.BaseToast;
import com.donews.utilslibrary.utils.LogUtil;

/**
 * <p> </p>
 * 作者： created by honeylife<br>
 * 日期： 2020/11/16 15:30<br>
 * 版本：V1.0<br>
 */
public class MineViewModel extends BaseLiveDataViewModel<MineModel> {


    public LifecycleOwner lifecycleOwner;
    private MineFragmentBinding dataBinding;
    private FragmentActivity baseActivity;




    @Override
    public MineModel createModel() {
        return new MineModel();
    }

    public void setDataBinDing(MineFragmentBinding dataBinding, FragmentActivity baseActivity) {
        this.dataBinding = dataBinding;
        this.baseActivity = baseActivity;
    }


    /**
     * 获取金币明细
     */
    public MutableLiveData<QueryBean> getQuery() {
        return mModel.getQuery();
    }

    /**
     * 点击跳转设置页
     */
    public void goToSetting(View view) {
        baseActivity.startActivity(new Intent(baseActivity, SettingActivity.class));
    }

    /**
     * 我的页面，点击跳转中心页或者login
     */
    public void onClickInfoLogin(View view) {
        LogUtil.i("点击登录了");
        if (LoginHelp.getInstance().isLogin()) {
            ARouter.getInstance().build(RouterActivityPath.User.PAGER_LOGIN).greenChannel().navigation();
        } else {
//            BaseToast.makeToast(context).setToastLongText("用户登录了").showToast();
            baseActivity.startActivity(new Intent(baseActivity, UserCenterActivity.class));
        }
    }

    /**
     * 复制粘贴
     *
     * @return
     */
    public void onClickCopy(String code) {
        //获取剪贴板管理器：
        ClipboardManager cm = (ClipboardManager) baseActivity.getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("Label", code);
        // 将ClipData内容放到系统剪贴板里。
        if (cm == null) return;
        cm.setPrimaryClip(mClipData);
        Toast.makeText(baseActivity, "复制成功", Toast.LENGTH_SHORT).show();
    }

    /**
     * @param url
     * @param title
     */
    public void onClickView(String title, String url) {
        ARouter.getInstance().build(RouterActivityPath.Web.PAGER_WEB_ACTIVITY)
                .withString("title", title)
                .withString("url", MineHttpApi.H5_URL + url)
                .navigation();


    }

    public void onCashPay(View view) {
        BaseToast.makeToast(baseActivity).setToastLongText("点击了提现,UI根据自己业务创建").showToast();
    }


    /**
     * @param v 邀请码填写
     */
    public void onInvitationCode(View v) {
        baseActivity.startActivity(new Intent(baseActivity, InvitationCodeActivity.class));
    }



}
