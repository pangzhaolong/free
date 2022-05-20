package com.donews.mine.dialogs;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.ScreenUtils;
import com.dn.events.events.UserUnRegisteredEvent;
import com.donews.base.fragmentdialog.LoadingHintDialog;
import com.donews.base.utils.ToastUtil;
import com.donews.common.router.RouterActivityPath;
import com.donews.mine.R;
import com.donews.share.ShareItem;
import com.donews.share.ShareManager;
import com.donews.utilslibrary.analysis.AnalysisUtils;
import com.donews.utilslibrary.dot.Dot;

import org.greenrobot.eventbus.EventBus;

/**
 * @author lcl
 * Date on 2021/10/22
 * Description:
 * 用户分享app。分享到哪里的选择框
 */
@Route(path = RouterActivityPath.Mine.DIALOG_SHARE_TO_DIALOG_FRAGMENT)
public class ShareToDialogFragment extends DialogFragment {

    private final static String ShareDialog_TAG = "ShareDialog";

    /**
     * 选择的监听
     */
    public interface onSelectListener {
        /**
         * 选择的类型
         *
         * @param type 1：分享给好友
         *             2：分享到朋友圈
         */
        void select(int type);
    }

    //选择监听
    private onSelectListener selectListener;

    /**
     * 设置选择监听
     *
     * @param selectListener
     */
    public void setSelectListener(onSelectListener selectListener) {
        this.selectListener = selectListener;
    }

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

        View view = inflater.inflate(R.layout.mine_share_to_select_dialog_fragment, null);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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
        getView().findViewById(R.id.share_select_dialog_py)
                .setOnClickListener(v -> {
                    if (selectListener != null) {
                        selectListener.select(1);
                        dismiss();
                    } else {
                        share(1);
                    }
                });
        getView().findViewById(R.id.share_select_dialog_quan)
                .setOnClickListener(v -> {
                    if (selectListener != null) {
                        selectListener.select(2);
                        dismiss();
                    } else {
                        share(2);
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void show(FragmentManager fragmentManager) {
        show(fragmentManager, ShareDialog_TAG);
    }

    /**
     * 分享app
     *
     * @param type 1-联系人，2-朋友圈
     */
    private void share(int type) {
        ShareManager sharManager = new ShareManager();
        ShareItem shareItem = new ShareItem();
        shareItem.setType(ShareItem.TYPE_H5);
        shareItem.setIcon("" + R.drawable.mine_share_wx_icon);
        shareItem.setTitle("0元抢华为P40~猛戳>>");
        shareItem.setContent("不花一分钱，商品抱回家，超多大牌好物等你抽，锦鲤就是你");
        shareItem.setWebUrl("https://recharge-web.xg.tagtic.cn/jdd/index.html#/download");
        if (type == 1) {
            shareItem.setCmd(ShareManager.SHARE_COMMAND_WX);
        } else if (type == 2) {
            shareItem.setCmd(ShareManager.SHARE_COMMAND_WX_FRIEND);
        } else {
            ToastUtil.showShort(getActivity(), "暂不支持此类型分享");
        }
        sharManager.share(shareItem.getCmd(), shareItem, getActivity());
        dismiss();
    }
}
