package com.donews.mine.adapters;

import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;

import com.alibaba.android.arouter.launcher.ARouter;
import com.dn.sdk.listener.rewardvideo.SimpleRewardVideoListener;
import com.donews.base.utils.ToastUtil;
import com.donews.common.base.MvvmBaseLiveDataActivity;
import com.donews.common.router.RouterActivityPath;
import com.donews.common.router.RouterFragmentPath;
import com.donews.middle.centralDeploy.ABSwitch;
import com.donews.middle.bean.HighValueGoodsBean;
import com.donews.middle.bean.mine2.emuns.Mine2TaskType;
import com.donews.middle.bean.mine2.reqs.DailyTasksReceiveReq;
import com.donews.middle.bean.mine2.reqs.DailyTasksReportReq;
import com.donews.middle.bean.mine2.resp.DailyTaskResp;
import com.donews.middle.bean.mine2.resp.DailyTasksReceiveResp;
import com.donews.middle.bean.mine2.resp.DailyTasksReportResp;
import com.donews.middle.cache.GoodsCache;
import com.donews.middle.request.RequestUtil;
import com.donews.mine.R;
import com.donews.mine.databinding.Mine2FragmentTaskItemBinding;
import com.donews.mine.dialogs.news.SignInRewardMineDialog;
import com.donews.mine.viewModel.MineViewModel;
import com.donews.middle.base.BaseBindingAdapter;
import com.donews.module_shareui.ShareUIBottomPopup;
import com.donews.yfsdk.loader.AdManager;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.enums.PopupAnimation;

/**
 * @author lcl
 * Date on 2021/10/18
 * Description:
 * 新版个人中心 -> 今日任务(日常任务) 适配器
 */
public class Mine2FragmentTaskAdapter extends
        BaseBindingAdapter<DailyTaskResp.DailyTaskItemResp,
                Mine2FragmentTaskItemBinding> {

    FragmentActivity fragmentActivity;
    MineViewModel mViewModel;

    public Mine2FragmentTaskAdapter(FragmentActivity fragmentActivity, MineViewModel mViewModel) {
        super(R.layout.mine2_fragment_task_item);
        this.fragmentActivity = fragmentActivity;
        this.mViewModel = mViewModel;
    }

    @Override
    protected void convert(@NonNull BaseBindViewHolder<Mine2FragmentTaskItemBinding> helper,
                           @Nullable DailyTaskResp.DailyTaskItemResp item) {
        helper.binding.setItemData(item);
        helper.binding.setClickListener((v) -> {
            click(item);
        });
    }

    //领取任务的结果数据变化监听
    final Observer<DailyTasksReceiveResp> mineDailyTaskReceiveObserver = new Observer<DailyTasksReceiveResp>() {
        @Override
        public void onChanged(DailyTasksReceiveResp dailyTasksReceiveResp) {
            checkShowDialog(false);
            //一次性的。消费一次之后结束监听(注:此方法不适用高并发情况)
            mViewModel.mineDailyTaskReceiveResult.removeObserver(mineDailyTaskReceiveObserver);
            if (dailyTasksReceiveResp == null) {
                ToastUtil.showShort(fragmentActivity, "获取奖励失败.请稍后再试");
                return;
            }
            //任务奖励模式
            SignInRewardMineDialog.getInstance(2)
                    .show(fragmentActivity.getSupportFragmentManager(), "aaaa3");
        }
    };

    //上报任务的结果数据变化监听
    final Observer<DailyTasksReportResp> mineDailyTaskReportObserver = new Observer<DailyTasksReportResp>() {
        @Override
        public void onChanged(DailyTasksReportResp item) {
            checkShowDialog(false);
            //一次性的。消费一次之后结束监听(注:此方法不适用高并发情况)
            mViewModel.mineDailyTaskReportResult.removeObserver(mineDailyTaskReportObserver);
            if (item == null) {
                ToastUtil.showShort(fragmentActivity, "获取奖励失败.请稍后再试");
                return;
            }
            //任务上报奖励模式
            SignInRewardMineDialog.getInstance(3)
                    .show(fragmentActivity.getSupportFragmentManager(), "aaaa4");
        }
    };

    //点击
    private void click(DailyTaskResp.DailyTaskItemResp item) {
        if (item.status == 1) {
            // 领取任务奖励模式
            requestTasksReceive(item);
            return;
        }
        switch (Mine2TaskType.query(item.type)) {
            case none: //领取时可用，领取全部
                break;
            case turntable: // 转盘
                if (item.status == 0) {
                    // 去往大转盘
                    ARouter.getInstance()
                            .build(RouterActivityPath.Turntable.TURNTABLE_ACTIVITY)
                            .navigation();
                }
                break;
            case collect: // 集卡
                ARouter.getInstance()
                        .build(RouterFragmentPath.Collect.PAGER_COLLECT)
                        .navigation();
                break;
            case lottery: // 抽奖
                if (item.status == 0) {
                    HighValueGoodsBean goodsBean = GoodsCache.readGoodsBean(HighValueGoodsBean.class, "exit");
                    if (goodsBean.getList() != null && goodsBean.getList().size() > 0) {
                        ARouter.getInstance()
                                .build(RouterFragmentPath.Lottery.PAGER_LOTTERY)
                                .withString("goods_id", goodsBean.getList().get(0).getGoodsId())
                                .withBoolean("start_lottery", ABSwitch.Ins().isOpenAutoLottery())
                                .navigation();
                    }
                    //更新缓存商品
                    RequestUtil.requestHighValueGoodsInfo();
                }
                break;
            case share: // 分享
                new XPopup.Builder(fragmentActivity)
                        .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                        .popupAnimation(PopupAnimation.TranslateFromBottom)
                        .navigationBarColor(Color.BLACK)
                        .asCustom(new ShareUIBottomPopup(fragmentActivity))
                        .show();
                break;
            case sign: // 签到
                if (item.status == 0) {
                    //签到弹窗
                    if (mViewModel.mineSignLists.getValue() == null ||
                            mViewModel.mineSignLists.getValue().items.size() != 7) {
                        ToastUtil.showShort(fragmentActivity, "签到数据天数不正确!");
                        return;
                    }
                    RouterFragmentPath.User.getSingDialog()
                            .show(fragmentActivity.getSupportFragmentManager(), "SignInMineDialog");
//                    SignInMineDialog.getInstance(mViewModel.mineSignLists.getValue())
//                            .show(fragmentActivity.getSupportFragmentManager(), "SignInMineDialog");
                }
                break;
            case taskvideo: // 任务视频(每日任务中的视频，个人中心 -> 每日任务)
                invokVideo(item);
                break;
            case video: // 视频(活动列表视频)
            case giftbox: // 宝箱
                ToastUtil.showShort(fragmentActivity, "暂不支持此类任务");
                break;
        }
    }

    //-------- 以下是相关点击的部分处理 ----------

    //执行每日任务视频
    private void invokVideo(DailyTaskResp.DailyTaskItemResp item) {
        checkShowDialog(true);
        AdManager.INSTANCE.loadRewardVideoAd(fragmentActivity, new SimpleRewardVideoListener() {
            //是否发放了奖励
            boolean isVerifyReward = false;

            @Override
            public void onAdShow() {
                super.onAdShow();
                mViewModel.mineDailyTaskReportResult.removeObserver(mineDailyTaskReportObserver);
                mViewModel.mineDailyTaskReportResult.observe(fragmentActivity, mineDailyTaskReportObserver);
                checkShowDialog(false);
            }

            @Override
            public void onAdError(int code, @Nullable String errorMsg) {
                ToastUtil.showShort(fragmentActivity, "任务领取异常,稍后重试!");
                //一次性的。消费一次之后结束监听(注:此方法不适用高并发情况)
                mViewModel.mineDailyTaskReportResult.removeObserver(mineDailyTaskReportObserver);
                super.onAdError(code, errorMsg);
            }

            @Override
            public void onRewardVerify(boolean result) {
                if (result) {
                    DailyTasksReportReq req = new DailyTasksReportReq();
                    req.id = item.id;
                    req.type = item.type;
                    mViewModel.requestTaskReport(req, false);
                } else {
                    mViewModel.mineDailyTaskReportResult.postValue(null);
                }
            }

            @Override
            public void onAdClose() {
                if (isVerifyReward) {
                    ToastUtil.showShort(fragmentActivity, "需要参与活动才能翻倍领取哦~");
                }
            }
        });
    }

    // 领取奖励任务
    private void requestTasksReceive(DailyTaskResp.DailyTaskItemResp item) {
        checkShowDialog(true);
        mViewModel.mineDailyTaskReceiveResult.observe(fragmentActivity, mineDailyTaskReceiveObserver);
        DailyTasksReceiveReq req = new DailyTasksReceiveReq();
        req.id = item.id;
        req.type = item.type;
        mViewModel.requestDailyTasksReceive(req, true);
    }

    //检查弹窗
    private void checkShowDialog(boolean isShow) {
        if (fragmentActivity instanceof MvvmBaseLiveDataActivity) {
            if (isShow) {
                ((MvvmBaseLiveDataActivity) fragmentActivity).showLoading("处理中");
            } else {
                ((MvvmBaseLiveDataActivity) fragmentActivity).hideLoading();
            }
        }
    }
}
