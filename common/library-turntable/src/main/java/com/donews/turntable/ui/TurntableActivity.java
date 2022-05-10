package com.donews.turntable.ui;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.donews.common.router.RouterActivityPath;
import com.donews.turntable.R;
import com.donews.turntable.base.TurntableBaseActivity;
import com.donews.turntable.bean.TurntablePrize;
import com.donews.turntable.databinding.TurntableActivityLayoutBinding;
import com.donews.turntable.dialog.DoingResultDialog;
import com.donews.turntable.dialog.RuleDialog;
import com.donews.turntable.utils.ClickDoubleUtil;
import com.donews.turntable.view.TurntableView;

import java.util.ArrayList;
import java.util.List;

@Route(path = RouterActivityPath.Turntable.TURNTABLE_ACTIVITY)
public class TurntableActivity extends TurntableBaseActivity<TurntableActivityLayoutBinding> implements View.OnClickListener {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ARouter.getInstance().inject(this);
        initTurntableView();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.turntable_activity_layout;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private void initTurntableView() {
        List<String> nameList = new ArrayList<>();
        nameList.add("电视机");
        nameList.add("金币X166");
        nameList.add("金币X6000");
        nameList.add("智能冰箱");
        nameList.add("金币X158");
        nameList.add("苹果13手机");
        nameList.add("金币X105");
        nameList.add("金币X288");
        List<TurntablePrize> itemBitmap = new ArrayList<>();
        //奖item
        for (int i = 0; i < nameList.size(); i++) {
            TurntablePrize turntable = new TurntablePrize();
            String name = "item_0" + (i + 1) + "";
            Bitmap item = ((BitmapDrawable) this.getDrawable(getResources().getIdentifier(name, "mipmap", this.getPackageName()))).getBitmap();
            turntable.setBitmap(item);
            turntable.setName(nameList.get(i));
            itemBitmap.add(turntable);
        }
        mDataBing.turntableView.setInitBitmap(itemBitmap);
        mDataBing.turntableView.setTurntableResultListener(new TurntableView.ITurntableResultListener() {
            @Override
            public void onResult(TurntablePrize prize) {
                //抽奖活动返回结果
                showDoingResultDialog(prize);
            }
        });
        mDataBing.turntableLotteryButton.setOnClickListener(this);
        mDataBing.turntableDrawAgain.setOnClickListener(this);
    }

    private void startLottery() {
        if (ClickDoubleUtil.isFastClick()) {
            mDataBing.turntableView.startAnimator();
        }
    }

    //点击tips
    public void clickTips(View view) {
        if (ClickDoubleUtil.isFastClick()) {
            RuleDialog ruleDialog = new RuleDialog(TurntableActivity.this);
            ruleDialog.show(TurntableActivity.this);
        }
    }


    private void showDoingResultDialog(TurntablePrize prize) {
        DoingResultDialog dialog = new DoingResultDialog(TurntableActivity.this, prize);
        dialog.setStateListener(new DoingResultDialog.OnStateListener() {
            @Override
            public void onOK() {

            }
        });
        dialog.show(TurntableActivity.this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.turntable_draw_again) {
            startLottery();
        }
        if (v.getId() == R.id.turntable_lottery_button) {
            startLottery();
        }
    }
}
