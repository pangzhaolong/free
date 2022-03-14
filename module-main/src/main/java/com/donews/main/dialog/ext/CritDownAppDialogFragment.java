package com.donews.main.dialog.ext;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.bumptech.glide.Glide;
import com.dn.sdk.bean.integral.IntegralStateListener;
import com.dn.sdk.bean.integral.ProxyIntegral;
import com.dn.sdk.utils.IntegralComponent;
import com.donews.base.utils.ToastUtil;
import com.donews.common.base.MvvmBaseLiveDataActivity;
import com.donews.main.R;
import com.donews.main.utils.integral.MainIntegralTaskManager;
import com.donews.main.utils.integral.beans.MainIntegralTaskDataItem;
import com.donews.middle.abswitch.OtherSwitch;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lcl
 * Date on 2021/12/13
 * Description:
 * 下载App的模块
 */
public class CritDownAppDialogFragment extends DialogFragment {

    private final static String TAG = "CritWelfareDialogFragment";

    /**
     * 确定按钮的监听
     */
    public interface OnItemClickListener {
        /**
         * 确定按钮的监听
         *
         * @param item 点击的Item数据
         */
        void onSur(ItemData item);
    }

    /**
     * 关闭按钮的监听
     */
    public interface OnCloseButListener {
        void close(CritDownAppDialogFragment dialog);
    }

    /**
     * 对话框关闭的监听。全局的关闭监听
     */
    public interface OnFinishDismissListener {
        void close(CritDownAppDialogFragment dialog);
    }

    private OnItemClickListener surListener;
    private OnCloseButListener closeListener;
    private OnFinishDismissListener dissListener;
    //每个item视图id
    private int itemLayoutRes = R.layout.incl_main_crit_down_app_dialog_item;
    //数据列表
    private List<ItemData> dataList = new ArrayList();
    //关闭按钮
    private ImageView close;
    //广告视图
    private RelativeLayout adView;
    //滚动的视图
    private ScrollView slView;
    //列表显示的容器
    private LinearLayout contentListll;
    //标题描述
    private TextView tiltleDesc;
    //内容区最大高度。为屏幕高度的一半
    private int contentMaxHei = (int) (ScreenUtils.getScreenHeight() * 0.5F);
    private ViewTreeObserver.OnGlobalLayoutListener globallListener;
    //item的高度
    private int itemHei = -1;
    //回到页面需要检查安装的应用包名称
    private String checkInstallPack = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //设置dialog的基本样式参数
        this.getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = this.getDialog().getWindow();
        //去掉dialog默认的padding
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //设置dialog的位置在底部
        lp.gravity = Gravity.BOTTOM;
        //设置dialog的动画
        lp.windowAnimations = R.style.Dialog_BottomToTopAnim;
        window.setAttributes(lp);
        window.setBackgroundDrawable(new ColorDrawable());
        View view = inflater.inflate(R.layout.main_crit_down_app_dialog_fragment, null);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().setCanceledOnTouchOutside(false);
        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        return dialog;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adView = getView().findViewById(R.id.ad_view);
        slView = getView().findViewById(R.id.sl_view);
        contentListll = getView().findViewById(R.id.main_crit_down_item_layout);
        close = getView().findViewById(R.id.main_crit_down_close);
        tiltleDesc = getView().findViewById(R.id.main_crit_td);
        int time = OtherSwitch.Ins().getScoreTaskPlayTime();
        tiltleDesc.setText("体验下方任一APP" + time + "s即可解锁");
        close.setOnClickListener(v -> {
            if (closeListener != null) {
                closeListener.close(this);
            }
            try {
                getDialog().dismiss();
                dismiss();
            }catch (Exception e){
                dismiss();
            }
        });
        getDialog().setOnDismissListener(dialog -> {
            if (dissListener != null) {
                dissListener.close(this);
            }
        });
        updateData();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (checkInstallPack != null && checkInstallPack.length() > 0) {
            if (!AppUtils.isAppInstalled(checkInstallPack)) {
                ToastUtil.showShort(getActivity(), "需要安装体验才能获得奖励");
            } else {
                //已安装。检查是否在运行中
                if (!AppUtils.isAppRunning(checkInstallPack)) {
                    ToastUtil.showShort(getActivity(), "快去体验应用获取奖励吧");
                    for (int i = 0; i < dataList.size(); i++) {
                        ItemData itemData = dataList.get(i);
                        if (itemData.srcData.getPkName().equals(checkInstallPack)) {
                            TextView down = contentListll.getChildAt(i).findViewById(R.id.tv_down);
                            down.setText("打开");
                            down.setTag("true");
                            break;
                        }
                    }
                }
            }
            checkInstallPack = null;
        }
    }

    /**
     * 设置数据
     *
     * @param data
     */
    public void setItemDatas(List<ItemData> data) {
        dataList.clear();
        dataList.addAll(data);
        if (contentListll == null) {
            return;
        }
        updateData();
    }

    public void setSurListener(OnItemClickListener surListener) {
        this.surListener = surListener;
    }

    public void setCloseListener(OnCloseButListener closeListener) {
        this.closeListener = closeListener;
    }

    public void setDissListener(OnFinishDismissListener dissListener) {
        this.dissListener = dissListener;
    }

    public void show(FragmentManager fragmentManager) {
        show(fragmentManager, this.toString());
    }

    //更新UI
    private void updateData() {
        contentListll.removeAllViews();
        for (ItemData itemData : dataList) {
            View itemView = LayoutInflater.from(getContext()).inflate(itemLayoutRes, null);
            contentListll.addView(itemView);
            if (itemHei <= 0) {
                buildSlHei(itemView);
                break;
            }
            //可以开始绑定数据了
            bindData(itemData, itemView);
        }

        //计算视图高度
        ViewGroup.LayoutParams lp = slView.getLayoutParams();
        if (dataList.size() * itemHei > contentMaxHei) {
            lp.height = contentMaxHei;
        } else {
            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
        slView.setLayoutParams(lp);
    }

    //调整列表滚动容器的高度
    private void buildSlHei(View view) {
        //计算其中一个item的高度
        if (itemHei >= 0) {
            return; //正在计算中,取消重复计算
        }
        if (globallListener == null) {
            itemHei = 0;
            globallListener = () -> {
                view.getViewTreeObserver().removeOnGlobalLayoutListener(globallListener);
                itemHei = view.getHeight();
                updateData();
            };
            view.getViewTreeObserver().addOnGlobalLayoutListener(globallListener);
        }
    }

    //绑定数据
    private void bindData(ItemData data, View view) {
        ImageView icon = view.findViewById(R.id.iv_down_icon);
        TextView title = view.findViewById(R.id.tv_down_title);
        TextView desc = view.findViewById(R.id.tv_down_desc);
        Glide.with(getActivity()).asDrawable().load(data.srcData.getIcon()).into(icon);
        title.setText(data.srcData.getAppName());
        desc.setText(data.srcData.getDesc());
        if (MainIntegralTaskManager.getCheckTaskIsExties(data.srcData)) {
            //任务已经存在，将任务关联上
            downApp(data, view, false);
        }
        updateDownButStatus(data, view);
    }

    //更新下载按钮状态
    private void updateDownButStatus(ItemData data, View view) {
        TextView down = view.findViewById(R.id.tv_down);
        down.setEnabled(true);
        down.setOnClickListener(v -> {
            if (surListener != null) {
                surListener.onSur(data);
            } else {
                downApp(data, view, true);
            }
        });
        if (AppUtils.isAppInstalled(data.srcData.getPkName())) {
            down.setText("打开");
        }
    }

    /**
     * 每项对应的数据
     */
    public static class ItemData {
        public ProxyIntegral srcData;

        public ItemData(ProxyIntegral srcData) {
            this.srcData = srcData;
        }
    }

    /**
     * 发起下载操作。或者是更新关联操作
     *
     * @param data               数据源
     * @param itemView           当前项目的View
     * @param isBeginImmediately 是否立即发起下载操作(T:立即执行，F:只是为了更新状态)
     */
    private void downApp(ItemData data, View itemView, boolean isBeginImmediately) {
        if (AppUtils.isAppInstalled(data.srcData.getPkName())) {
            IntegralComponent.getInstance()
                    .runIntegralApk(getActivity(), data.srcData);
            dismiss();
            return;
        }
        TextView clickView = itemView.findViewById(R.id.tv_down);
        clickView.setText("下载中");
        //外部新增的监听
        IntegralStateListener newListener = new IntegralStateListener() {
            @Override
            public void onAdShow() {

            }

            @Override
            public void onAdClick() {

            }

            @Override
            public void onStart() {

            }

            @Override
            public void onProgress(long l, long l1) {
                int zjd = (int) ((l1 / (l * 1F)) * 100);
                if (l == l1 || zjd >= 100) {
                    clickView.setText("安装");
                } else {
                    clickView.setText("下载中 " + zjd + "%");
                }
            }

            @Override
            public void onComplete() {
                clickView.setText("安装");
                checkInstallPack = data.srcData.getPkName();
                updateDownButStatus(data, itemView);
                if (getActivity() instanceof MvvmBaseLiveDataActivity) {
                    ((MvvmBaseLiveDataActivity<?, ?>) getActivity()).hideLoading();
                }
            }

            //安装完成
            @Override
            public void onInstalled() {
                clickView.setText("打开");
                updateDownButStatus(data, itemView);
                if (getActivity() instanceof MvvmBaseLiveDataActivity) {
                    ((MvvmBaseLiveDataActivity<?, ?>) getActivity()).hideLoading();
                }
            }

            @Override
            public void onError(Throwable throwable) {
                clickView.setText("下载失败");
                updateDownButStatus(data, itemView);
                if (getActivity() instanceof MvvmBaseLiveDataActivity) {
                    ((MvvmBaseLiveDataActivity<?, ?>) getActivity()).hideLoading();
                }
            }

            @Override
            public void onRewardVerify() {
                try {
                    getDialog().dismiss();
                }catch (Exception e){
                    dismiss();
                }
                ToastUtil.showShort(getActivity(), "任务已完成");
            }

            @Override
            public void onRewardVerifyError(String s) {
                ToastUtil.showShort(getActivity(), "任务奖励领取失败");
            }
        };
        boolean isExitTask = MainIntegralTaskManager.getCheckTaskIsExties(data.srcData);
        IntegralStateListener srcListener = MainIntegralTaskManager.addOrAttchTask(
                getLifecycle(), data.srcData, newListener
        );
        //数据准备
        clickView.setEnabled(false);
        List<View> clickViews = new ArrayList<>();
        clickViews.add(clickView);
        //实际的下载任务
        IntegralComponent.getInstance().setIntegralBindView(
                getActivity(), data.srcData, adView,
                clickViews, srcListener, true);
        //主动触发一次点击
        if (isBeginImmediately) {
            clickView.performClick();
            if (isExitTask) {
                //任务已存在
                MainIntegralTaskDataItem item = MainIntegralTaskManager.getItemTask(data.srcData);
                if (item != null && item.isWaitingInstall() && getActivity() instanceof MvvmBaseLiveDataActivity) {
                    ((MvvmBaseLiveDataActivity<?, ?>) getActivity()).showLoading("安装中");
                }
            }
        }
    }

}
