package com.donews.middle.views;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ScreenUtils;
import com.bumptech.glide.Glide;
import com.donews.middle.bean.front.FrontConfigBean;
import com.donews.middle.front.FrontConfigManager;
import com.donews.middle.go.GotoUtil;
import com.donews.utilslibrary.analysis.AnalysisUtils;
import com.donews.utilslibrary.dot.Dot;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


public class ExchanageTaskView extends LinearLayout {

    private static final int MESSAGE_ID = 10002;
    private static final int MESSAGE_SWITCH_ID = 10003;

    private final Context mContext;

    private final int ARRAY_COUNT = 4;
    private int mSwitchInterval = 10;

    public final static int Place_Front = 0;
    public final static int Place_Show = 1;
    public final static int Place_Mine = 2;
    public final static int Place_Show_Msg = 3;
    public final static int Place_Win_Code = 4;
    private int mCurrentModel = Place_Front;

    private TaskHandler mTaskHandler;

    private final List<FrontConfigBean.SubItems> mYywItemList = new ArrayList<>();

    private int mTaskGroup = 1;
    private boolean mEnableYyw = false;
    private final int mYywIdxs[] = new int[ARRAY_COUNT];
    private final ImageView[] mImageViews = new ImageView[4];
    private final LayoutParams mLayoutParams1 = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1);
    private final LayoutParams mLayoutParams2 = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 2);

    private String mDotFrom = "";

    public ExchanageTaskView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        mImageViews[0] = new ImageView(this.mContext);
        mImageViews[1] = new ImageView(this.mContext);
        mImageViews[2] = new ImageView(this.mContext);
        mImageViews[3] = new ImageView(this.mContext);
        for (ImageView iv : mImageViews) {
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
        }
        for (int i = 0; i < ARRAY_COUNT; i++) {
            mYywIdxs[i] = 0;
        }

        mTaskHandler = new TaskHandler(Looper.getMainLooper(), this);

        this.setVisibility(GONE);
    }

    public void refreshYyw(int model) {
        mCurrentModel = model;

        mYywItemList.clear();
        if (mCurrentModel == Place_Front) {
            mDotFrom = "place_front";
            try {
                mYywItemList.addAll(FrontConfigManager.Ins().getConfigBean().getFrontTask().getItems());
                mTaskGroup = FrontConfigManager.Ins().getConfigBean().getFrontTask().getTaskGroup();
                mEnableYyw = FrontConfigManager.Ins().getConfigBean().getTask();
                mSwitchInterval = FrontConfigManager.Ins().getConfigBean().getFrontTask().getSwitchInterval();
            } catch (Exception e) {
                this.setVisibility(GONE);
                return;
            }
        } else if (mCurrentModel == Place_Mine) {
            mDotFrom = "place_mine";
            try {
                mYywItemList.addAll(FrontConfigManager.Ins().getConfigBean().getMineTask().getItems());
                mTaskGroup = FrontConfigManager.Ins().getConfigBean().getMineTask().getTaskGroup();
                mEnableYyw = FrontConfigManager.Ins().getConfigBean().getMine();
                mSwitchInterval = FrontConfigManager.Ins().getConfigBean().getMineTask().getSwitchInterval();
            } catch (Exception e) {
                this.setVisibility(GONE);
                return;
            }
        } else if (mCurrentModel == Place_Show) {
            mDotFrom = "place_show";
            try {
                mYywItemList.addAll(FrontConfigManager.Ins().getConfigBean().getShowTask().getItems());
                mTaskGroup = FrontConfigManager.Ins().getConfigBean().getShowTask().getTaskGroup();
                mEnableYyw = FrontConfigManager.Ins().getConfigBean().getShowTime();
                mSwitchInterval = FrontConfigManager.Ins().getConfigBean().getShowTask().getSwitchInterval();
            } catch (Exception e) {
                this.setVisibility(GONE);
                return;
            }
        } else if (mCurrentModel == Place_Show_Msg) {
            mDotFrom = "place_show_msg";
            try {
                mYywItemList.addAll(FrontConfigManager.Ins().getConfigBean().getShowMsgTask().getItems());
                mTaskGroup = FrontConfigManager.Ins().getConfigBean().getShowMsgTask().getTaskGroup();
                mEnableYyw = FrontConfigManager.Ins().getConfigBean().getShowTimeMsg();
                mSwitchInterval = FrontConfigManager.Ins().getConfigBean().getShowMsgTask().getSwitchInterval();
            } catch (Exception e) {
                this.setVisibility(GONE);
                return;
            }
        } else if (mCurrentModel == Place_Win_Code) {
            mDotFrom = "place_win_code";
            try {
                mYywItemList.addAll(FrontConfigManager.Ins().getConfigBean().getWinCodeTask().getItems());
                mTaskGroup = FrontConfigManager.Ins().getConfigBean().getWinCodeTask().getTaskGroup();
                mEnableYyw = FrontConfigManager.Ins().getConfigBean().getWinCode();
                mSwitchInterval = FrontConfigManager.Ins().getConfigBean().getWinCodeTask().getSwitchInterval();
            } catch (Exception e) {
                this.setVisibility(GONE);
                return;
            }
        }


        if (!mEnableYyw) {
            this.setVisibility(GONE);
            return;
        }

        this.setVisibility(VISIBLE);

        initViews();

        refreshYywItem();
    }

    private void initViews() {
        this.removeAllViews();
        for (ImageView iv : mImageViews) {
            iv.setLayoutParams(mLayoutParams1);
        }
        switch (mTaskGroup) {
            case 0:
                this.setWeightSum(4);
                this.addView(mImageViews[0]);
                this.addView(mImageViews[1]);
                this.addView(mImageViews[2]);
                this.addView(mImageViews[3]);
                break;
            case 3:
                this.setWeightSum(4);
                mImageViews[0].setLayoutParams(mLayoutParams2);
                this.addView(mImageViews[0]);
                this.addView(mImageViews[1]);
                this.addView(mImageViews[2]);
                break;
            case 4:
                this.setWeightSum(4);
                this.addView(mImageViews[0]);
                this.addView(mImageViews[1]);
                mImageViews[2].setLayoutParams(mLayoutParams2);
                this.addView(mImageViews[2]);
                break;
            case 1:
                this.setWeightSum(2);
                this.addView(mImageViews[0]);
                this.addView(mImageViews[1]);
                break;
            case 2:
                this.setWeightSum(1);
                this.addView(mImageViews[0]);
                break;
        }
        //设置附加相关的参数
        for (ImageView iv : mImageViews) {
            //设置高度智适应
            iv.setAdjustViewBounds(true);
            iv.setMaxHeight((int) (ScreenUtils.getScreenHeight() * 0.35));
        }
        this.measure(getMeasuredWidth(), getMeasuredHeight());
    }

    private void refreshYywItem() {
        try {
            setYywItem();

            if (mTaskHandler != null) {
                mTaskHandler.removeCallbacksAndMessages(null);
                mTaskHandler.sendEmptyMessageDelayed(MESSAGE_ID, mSwitchInterval * 1000L);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setYywItem() {
        if (((Activity) mContext).isDestroyed() || ((Activity) mContext).isFinishing()) {
            return;
        }

        checkYywIndex();

        mImageViews[0].setScaleType(ImageView.ScaleType.FIT_XY);
        switch (mTaskGroup) {
            case 0:
                Glide.with(this).load(mYywItemList.get(0).getSubItems().get(mYywIdxs[0]).getImg()).into(mImageViews[0]);
                Glide.with(this).load(mYywItemList.get(1).getSubItems().get(mYywIdxs[1]).getImg()).into(mImageViews[1]);
                Glide.with(this).load(mYywItemList.get(2).getSubItems().get(mYywIdxs[2]).getImg()).into(mImageViews[2]);
                Glide.with(this).load(mYywItemList.get(3).getSubItems().get(mYywIdxs[3]).getImg()).into(mImageViews[3]);
                break;
            case 1:
                Glide.with(this).load(mYywItemList.get(0).getSubItems().get(mYywIdxs[0]).getImg()).into(mImageViews[0]);
                Glide.with(this).load(mYywItemList.get(1).getSubItems().get(mYywIdxs[1]).getImg()).into(mImageViews[1]);
                break;
            case 2:
                Glide.with(this).load(mYywItemList.get(0).getSubItems().get(mYywIdxs[0]).getImg()).into(mImageViews[0]);
                mImageViews[0].setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                break;
            case 3:
            case 4:
                Glide.with(this).load(mYywItemList.get(0).getSubItems().get(mYywIdxs[0]).getImg()).into(mImageViews[0]);
                Glide.with(this).load(mYywItemList.get(1).getSubItems().get(mYywIdxs[1]).getImg()).into(mImageViews[1]);
                Glide.with(this).load(mYywItemList.get(2).getSubItems().get(mYywIdxs[2]).getImg()).into(mImageViews[2]);
                break;
        }

        int nSize = mYywItemList.size();
        if (nSize >= 1) {
            mImageViews[0].setOnClickListener(v -> {
                GotoUtil.doAction(mContext, mYywItemList.get(0).getSubItems().get(mYywIdxs[0]).getAction()
                        , mYywItemList.get(0).getSubItems().get(mYywIdxs[0]).getTitle());
                mYywIdxs[0]++;
                checkYywIndex();
                if (mTaskHandler != null) {
                    mTaskHandler.removeMessages(MESSAGE_SWITCH_ID);
                    mTaskHandler.sendEmptyMessageDelayed(MESSAGE_SWITCH_ID, 1500);
                }
            });
        }
        if (nSize >= 2) {
            mImageViews[1].setOnClickListener(v -> {
                GotoUtil.doAction(mContext, mYywItemList.get(1).getSubItems().get(mYywIdxs[1]).getAction()
                        , mYywItemList.get(1).getSubItems().get(mYywIdxs[1]).getTitle());
                mYywIdxs[1]++;
                checkYywIndex();
                if (mTaskHandler != null) {
                    mTaskHandler.removeMessages(MESSAGE_SWITCH_ID);
                    mTaskHandler.sendEmptyMessageDelayed(MESSAGE_SWITCH_ID, 1500);
                }
            });
        }
        if (nSize >= 3) {
            mImageViews[2].setOnClickListener(v -> {
                GotoUtil.doAction(mContext, mYywItemList.get(2).getSubItems().get(mYywIdxs[2]).getAction()
                        , mYywItemList.get(2).getSubItems().get(mYywIdxs[2]).getTitle());
                mYywIdxs[2]++;
                checkYywIndex();
                if (mTaskHandler != null) {
                    mTaskHandler.removeMessages(MESSAGE_SWITCH_ID);
                    mTaskHandler.sendEmptyMessageDelayed(MESSAGE_SWITCH_ID, 1500);
                }
            });
        }
        if (nSize >= 4) {
            mImageViews[3].setOnClickListener(v -> {
                GotoUtil.doAction(mContext, mYywItemList.get(3).getSubItems().get(mYywIdxs[3]).getAction()
                        , mYywItemList.get(3).getSubItems().get(mYywIdxs[3]).getTitle());
                mYywIdxs[3]++;
                checkYywIndex();
                if (mTaskHandler != null) {
                    mTaskHandler.removeMessages(MESSAGE_SWITCH_ID);
                    mTaskHandler.sendEmptyMessageDelayed(MESSAGE_SWITCH_ID, 1500);
                }
            });
        }
    }

    private void checkYywIndex() {
        int nSize = mYywItemList.size();
        if (nSize > 0) {
            if (mYywIdxs[0] < 0 || mYywIdxs[0] >= mYywItemList.get(0).getSubItems().size()) {
                mYywIdxs[0] = 0;
            }
        }
        if (nSize > 1) {
            if (mYywIdxs[1] < 0 || mYywIdxs[1] >= mYywItemList.get(1).getSubItems().size()) {
                mYywIdxs[1] = 0;
            }
        }
        if (nSize > 2) {
            if (mYywIdxs[2] < 0 || mYywIdxs[2] >= mYywItemList.get(2).getSubItems().size()) {
                mYywIdxs[2] = 0;
            }
        }
        if (nSize > 3) {
            if (mYywIdxs[3] < 0 || mYywIdxs[3] >= mYywItemList.get(3).getSubItems().size()) {
                mYywIdxs[3] = 0;
            }
        }
    }

    public void release() {
        if (mTaskHandler != null) {
            mTaskHandler.removeCallbacksAndMessages(null);
            mTaskHandler = null;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mTaskHandler != null && (mCurrentModel != Place_Win_Code)) {
            mTaskHandler.removeCallbacksAndMessages(null);
            mTaskHandler = null;
        }
    }

    private static class TaskHandler extends Handler {

        private final WeakReference<ExchanageTaskView> mTaskView;

        public TaskHandler(Looper looper, ExchanageTaskView view) {
            super(looper);
            mTaskView = new WeakReference<>(view);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            ExchanageTaskView view = mTaskView.get();
            if (view == null) {
                return;
            }
            if (msg.what == MESSAGE_ID) {
                for (int i = 0; i < view.ARRAY_COUNT; i++) {
                    view.mYywIdxs[i]++;
                }
                view.refreshYywItem();
            } else if (msg.what == MESSAGE_SWITCH_ID) {
                view.refreshYywItem();
            }
        }
    }
}
