package com.donews.mine.dialogs.news;


import android.os.Build;
import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.GsonUtils;
import com.donews.base.base.BaseApplication;
import com.donews.base.utils.ToastUtil;
import com.donews.common.base.MvvmBaseLiveDataActivity;
import com.donews.common.router.RouterFragmentPath;
import com.donews.middle.bean.mine2.reqs.SignReq;
import com.donews.middle.bean.mine2.resp.SignListResp;
import com.donews.middle.dialog.BaseBindingFragmentDialog;
import com.donews.mine.BuildConfig;
import com.donews.mine.R;
import com.donews.mine.databinding.Mine2SigninDialogBinding;
import com.donews.mine.viewModel.MineViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 个人中心任务的签到弹窗
 */
@Route(path = RouterFragmentPath.User.PAGER_USER_SIGN_DIALOG)
public class SignInMineDialog extends BaseBindingFragmentDialog<Mine2SigninDialogBinding> {

    /**
     * 签到Item的点击监听，每天的Item点击监听
     */
    public interface OnSignItemClickListener {
        /**
         * 每天签到点击监听
         *
         * @param item
         */
        void click(SignListResp.SignListItemResp item);
    }

    // 签到数据列表
    private List<SignListResp.SignListItemResp> itemDatas = new ArrayList<>();
    // 不可点击的视图点击之后的toast弹出间隔最小时间
    private long notNextClickToastStepTime = 2500;
    // 不可点击的视图点击之后的toast最近一次弹出时间
    private long notNextClickToastNewTime = 0;
    // ViewModel 对象
    private MineViewModel mineViewModel;

    /**
     * 单利构建对象
     *
     * @param itemDatas 签到的数据集合(一共七天)
     * @return
     */
    public static SignInMineDialog getInstance(SignListResp itemDatas) {
        SignInMineDialog dialog = new SignInMineDialog();
        if (dialog.getArguments() == null) {
            dialog.setArguments(new Bundle());
        }
        dialog.getArguments().putString("itemDatas", GsonUtils.toJson(itemDatas));
        return dialog;
    }

    public SignInMineDialog() {
        super(R.layout.mine2_signin_dialog);
    }

    /**
     * 初始化布局
     */
    @Override
    protected void initView() {
        ARouter.getInstance().inject(this);
        if (getArguments() != null) {
            String json = getArguments().getString("itemDatas");
            try {
                itemDatas = GsonUtils.fromJson(json, SignListResp.class)
                        .items;
            } catch (Exception e) {
            }
        }
        //获取ViewModel对象
        mineViewModel = createViewModel(getActivity(), MineViewModel.class);
        if (itemDatas == null || itemDatas.size() <= 0) {
            mineViewModel.mineSignLists.observe(this, (item) -> {
                if (item != null) {
                    this.itemDatas = item.items;
                    initData();
                } else {
                    ToastUtil.showShort(getActivity(), "签到数据获取异常");
                }
            });
            mineViewModel.getsignList();
            return;
        }
        initData();
    }

    //初始化数据
    private void initData() {
        if (itemDatas.size() == 7) {
            initDatabinding();
        } else {
            if (BuildConfig.DEBUG) {
                throw new ArrayIndexOutOfBoundsException("给定的参数不符合数据长度。请检查!!!");
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    /**
     * 获取当前允许签到的是第几天
     *
     * @return
     */
    public int getCurrentAllowSignDay() {
        for (int i = 0; i < itemDatas.size(); i++) {
            if (itemDatas.get(i).status == 1) {
                return itemDatas.get(i).day;
            }
        }
        return 0;
    }

    private boolean isAddSignResultObser = false;

    //初始化dataBinding数据绑定
    private void initDatabinding() {
        dataBinding.setThiz(this);
        dataBinding.setDatas(itemDatas);
        //设置item的点击，是可签到项目的点击
        dataBinding.setItemClick((item) -> {
            //发起签到
            if (!isAddSignResultObser) {
                mineViewModel.mineSignResult.postValue(null);
                mineViewModel.mineSignResult.observe(this, (result) -> {
                    if (getActivity() instanceof MvvmBaseLiveDataActivity) {
                        ((MvvmBaseLiveDataActivity<?, ?>) getActivity()).hideLoading();
                    }
                    if (result != null) {
                        //开始发起下一步操作。弹出双倍弹窗
                        SignInRewardMineDialog.getInstance(0)
                                .show(getActivity().getSupportFragmentManager(), "aaaa");
                        dismiss();
                    } else {
                        ToastUtil.showShort(getActivity(), "请求失败,请稍后重试");
                    }
                });
                isAddSignResultObser = true;
            }
            if (getActivity() instanceof MvvmBaseLiveDataActivity) {
                ((MvvmBaseLiveDataActivity<?, ?>) getActivity()).showLoading("签到中...");
            }
            mineViewModel.requestSign(new SignReq(), true);
        });
        dataBinding.setItemNotNextClick((item) -> {
            if (System.currentTimeMillis() - notNextClickToastNewTime > notNextClickToastStepTime) {
                int curDay = getCurrentAllowSignDay();
                int clickDay = item.day;
                if (clickDay > curDay) {
                    ToastUtil.show(getContext(), "请在" + (clickDay - curDay) + "天后记得签到哦");
                } else {
                    ToastUtil.show(getContext(), "暂不签到,请正第" + item.day + "天再来!");
                }
                notNextClickToastNewTime = System.currentTimeMillis();
            }
        });
    }
}