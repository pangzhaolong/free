package com.donews.mine.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;

import com.alibaba.android.arouter.launcher.ARouter;
import com.donews.base.utils.ToastUtil;
import com.donews.common.base.MvvmBaseLiveDataActivity;
import com.donews.common.router.RouterActivityPath;
import com.donews.middle.bean.mine2.emuns.Mine2TaskType;
import com.donews.middle.bean.mine2.reqs.DailyTasksReceiveReq;
import com.donews.middle.bean.mine2.resp.DailyTaskResp;
import com.donews.middle.bean.mine2.resp.DailyTasksReceiveResp;
import com.donews.mine.R;
import com.donews.mine.databinding.Mine2FragmentTaskItemBinding;
import com.donews.mine.dialogs.news.SignInMineDialog;
import com.donews.mine.dialogs.news.SignInRewardMineDialog;
import com.donews.mine.viewModel.MineViewModel;
import com.donews.mine.views.refresh.adapters.BaseBindingAdapter;

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
            mViewModel.mine2RefeshDataLive.postValue(true);
        }
    };

    //点击
    private void click(DailyTaskResp.DailyTaskItemResp item) {
        switch (Mine2TaskType.query(item.type)) {
            case none: //领取时可用，领取全部
                break;
            case turntable: // 转盘
                if (item.status == 0) {
                    // 去往大转盘
                    ARouter.getInstance()
                            .build(RouterActivityPath.Turntable.TURNTABLE_ACTIVITY)
                            .navigation();
                } else if (item.status == 1) {
                    // 领取任务奖励模式
                    requestTasksReceive(item);
                }
                break;
            case collect: // 集卡
                break;
            case lottery: // 抽奖
                break;
            case share: // 分享
                break;
            case sign: // 签到
                if (item.status == 0) {
                    //签到弹窗
                    if (mViewModel.mineSignLists.getValue() == null ||
                            mViewModel.mineSignLists.getValue().size() != 7) {
                        ToastUtil.showShort(fragmentActivity, "签到数据天数不正确!");
                        return;
                    }
                    SignInMineDialog.getInstance(mViewModel.mineSignLists.getValue())
                            .show(fragmentActivity.getSupportFragmentManager(), "SignInMineDialog");
                } else if (item.status == 1) {
                    // 领取任务奖励模式
                    requestTasksReceive(item);
                }
                break;
            case video: // 视频(活动列表视频)
                break;
            case taskvideo: // 任务视频(每日任务)
                break;
            case giftbox: // 宝箱
                break;
        }
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
