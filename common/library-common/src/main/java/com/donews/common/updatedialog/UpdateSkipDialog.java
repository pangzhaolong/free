package com.donews.common.updatedialog;

import android.content.Context;
import android.content.Intent;

import com.donews.base.appdialog.activity.BaseAppDialogActivity;
import com.donews.common.contract.ApplyUpdateBean;

/**
 * 更新取消的确认弹出框
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/11 10:09
 */
public class UpdateSkipDialog extends BaseAppDialogActivity {

    private static final String UPDATE_ENTITY = "update_entity";

    public static void showSkipDialog(Context context, ApplyUpdateBean bean) {
        context.startActivity(new Intent(context, UpdateActivityDialog.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .putExtra(UPDATE_ENTITY, bean));
    }

    @Override
    public void initView() {

    }

    @Override
    public void setDataBinding() {

    }
}
