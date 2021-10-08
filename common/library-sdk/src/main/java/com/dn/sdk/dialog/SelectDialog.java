package com.dn.sdk.dialog;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.dn.sdk.R;

/**
 * @auther ming
 * @date $
 */
public class SelectDialog extends BaseFragmentDialog {

    private Dialogtemplate dialogtemplate;
    private TextView btnSelectOne;
    private TextView tvSelectContent;
    private TextView btnSelectTwo;
    private LinearLayout llOverall;
    private TextView tvSelectTitle;
    /**
     * Dialog ui内容
     */
    private String[] uiDate;
    /**
     * Dialog 按钮背景
     */
    private int[] uiBG;
    /**
     * Dialog 字体颜色
     */
    private int[] uiTextColor;
    /**
     * 默认值
     */
    public static final int DEFAULT_VALUE = 0;

    /**
     * Title
     */
    public String titleStr = "";
    /**
     * Title Color
     */
    private int titleStrColor = DEFAULT_VALUE;


    private OnSelectDialogListener onSelectDialogListener;

    @Override
    protected int getLayoutId() {
        switch (dialogtemplate) {
            case BUTTON_ONE:
                return 0;
            case BUTTON_HORIZONTAL_TWO:
                return R.layout.sdk_dialog_select_horizontal_two;
            case BUTTON_HORIZONTAL_THREE:
                return 0;
            default:
                return 0;
        }

    }

    @Override
    protected void initView() {
        initOverall();
        switch (dialogtemplate) {
            case BUTTON_ONE:
                initContent();
                break;
            case BUTTON_HORIZONTAL_TWO:
                initContent();
                initOne();
                initTwo();
                break;
            case BUTTON_HORIZONTAL_THREE:
                initContent();
                initOne();
                initTwo();
                break;
        }


    }

    private void initOverall() {
        llOverall = $(R.id.ll_overall);
        llOverall.setBackgroundResource(uiBG[0] == DEFAULT_VALUE ? R.drawable.sdk_bg_dialog_select : uiBG[0]);

        tvSelectTitle = $(R.id.tv_select_title);
        if (titleStr.isEmpty()) {
            tvSelectTitle.setVisibility(View.GONE);
        } else {
            tvSelectTitle.setVisibility(View.VISIBLE);
            tvSelectTitle.setTextColor(titleStrColor == DEFAULT_VALUE ? getResources().getColorStateList(R.color.black) : getResources().getColorStateList(titleStrColor));
        }

    }

    private void initTwo() {
        btnSelectTwo = $(R.id.btn_select_right);
        btnSelectTwo.setTextColor(getResources().getColorStateList(uiTextColor == null || uiTextColor[2] == DEFAULT_VALUE ? R.color.black : uiTextColor[2]));
        btnSelectTwo.setText(uiDate[2].isEmpty() ? "null" : uiDate[2]);
        btnSelectTwo.setBackgroundResource(uiBG[2]);
        btnSelectTwo.setOnClickListener(view -> {
            if (onSelectDialogListener != null) {
                onSelectDialogListener.TwoButtonClick();
                disMissDialog();
            }
        });
    }

    private void initOne() {
        btnSelectOne = $(R.id.btn_select_left);
        btnSelectOne.setTextColor(getResources().getColorStateList(uiTextColor == null || uiTextColor[1] == DEFAULT_VALUE ? R.color.black : uiTextColor[1]));
        btnSelectOne.setText(uiDate[1].isEmpty() ? "null" : uiDate[1]);
        btnSelectOne.setBackgroundResource(uiBG[1]);
        btnSelectOne.setOnClickListener(view -> {
            if (onSelectDialogListener != null) {
                onSelectDialogListener.oneButtonClick();
                disMissDialog();
            }
        });
    }

    private void initContent() {
        tvSelectContent = $(R.id.tv_select_content);
        tvSelectContent.setTextColor(getResources().getColorStateList(uiTextColor == null || uiTextColor[0] == DEFAULT_VALUE ? R.color.black : uiTextColor[0]));
        tvSelectContent.setText(uiDate[0].isEmpty() ? "null" : uiDate[0]);

    }

    /**
     * 背景
     *
     * @param overallBg  整体背景色
     * @param btnOneBg
     * @param btnTwoBg
     * @param btnThreeBg
     * @return
     */
    public SelectDialog setControlBackgtound(@DrawableRes int overallBg, @DrawableRes int btnOneBg, @DrawableRes int btnTwoBg, @DrawableRes int btnThreeBg) {
        uiBG = new int[]{overallBg, btnOneBg, btnTwoBg, btnThreeBg};
        return this;
    }

    public SelectDialog setControlBackgtound(@DrawableRes int overallBg, @DrawableRes int btnOneBg, @DrawableRes int btnTwoBg) {
        uiBG = new int[]{overallBg, btnOneBg, btnTwoBg};
        return this;
    }

    public SelectDialog setControlBackgtound(@DrawableRes int overallBg, @DrawableRes int btnOneBg) {
        uiBG = new int[]{overallBg, btnOneBg};
        return this;
    }


    public SelectDialog setTextColor(int content, int btnOne, int btnTwo) {
        uiTextColor = new int[]{content, btnOne, btnTwo};
        return this;
    }

    public SelectDialog setTextColor(int content, int btnOne, int btnTwo, int btnThree) {
        uiTextColor = new int[]{content, btnOne, btnTwo, btnThree};
        return this;
    }


    public SelectDialog setDialogText(String content, String btnOne) {
        uiDate = new String[]{content, btnOne};
        dialogtemplate = Dialogtemplate.BUTTON_ONE;
        return this;
    }

    public SelectDialog setDialogText(String content, String btnOne, String btnTwo) {
        uiDate = new String[]{content, btnOne, btnTwo};
        dialogtemplate = Dialogtemplate.BUTTON_HORIZONTAL_TWO;
        return this;
    }

    public SelectDialog setDialogText(String content, String btnOne, String btnTwo, String btnThree) {
        uiDate = new String[]{content, btnOne, btnTwo, btnThree};
        dialogtemplate = Dialogtemplate.BUTTON_HORIZONTAL_THREE;
        return this;
    }

    public SelectDialog setTitle(String titleStr) {
        this.titleStr = titleStr;
        return this;
    }

    public SelectDialog setTitleColor(int titleStrColor) {
        this.titleStrColor = titleStrColor;
        return this;
    }


    @Override
    public int show(@NonNull FragmentTransaction transaction, @Nullable String tag) {
        return super.show(transaction, tag);
    }

    public enum Dialogtemplate {
        BUTTON_ONE, BUTTON_HORIZONTAL_TWO, BUTTON_HORIZONTAL_THREE;
    }


    public SelectDialog setOnSelectDialogListener(OnSelectDialogListener onSelectDialogListener) {
        this.onSelectDialogListener = onSelectDialogListener;
        return this;
    }

    public interface OnSelectDialogListener {
        void oneButtonClick();

        void TwoButtonClick();

    }
}
