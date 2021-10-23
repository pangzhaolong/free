package com.donews.base.fragmentdialog;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.DialogFragment;

import com.donews.base.R;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;


/**
 * @author by SnowDragon
 * Date on 2020/11/20
 * Description:
 */
public abstract class AbstractFragmentDialog<T extends ViewDataBinding> extends DialogFragment {
    protected boolean dismissOnBackPressed = true;
    protected boolean dismissOnTouchOutside = true;

    private int animId = R.style.BaseDialogOutInAnim;
    protected T dataBinding;


    /**
     * 背景色是否变暗
     */
    protected boolean backgroundDim = true;

    private OnDismissListener mOnDismissListener;
    private SureListener mOnSureListener;
    private CancelListener mOnCancelListener;
    private CloseListener mOnCloseListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, getThemeStyle());

    }

    public AbstractFragmentDialog() {
    }


    public AbstractFragmentDialog(boolean dismissOnBackPressed, boolean dismissOnTouchOutside) {
        this.dismissOnBackPressed = dismissOnBackPressed;
        this.dismissOnTouchOutside = dismissOnTouchOutside;
    }

    public AbstractFragmentDialog(boolean dismissOnBackPressed, boolean dismissOnTouchOutside, int animStyleId) {
        this.animId = animStyleId;
        this.dismissOnBackPressed = dismissOnBackPressed;
        this.dismissOnTouchOutside = dismissOnTouchOutside;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        // 弹出框外面是否可取消
        getDialog().setCanceledOnTouchOutside(dismissOnTouchOutside);

        // 返回true表示消费事件，拦截
        getDialog().setOnKeyListener((dialog, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                //不往上传递则关闭不了，默认是可以取消，即return false，
                if (dismissOnBackPressed) {
                    onBackPressDismissBefore();
                }
                return !dismissOnBackPressed;
            } else {
                return false;
            }
        });
        if (isUseDataBinding()) {
            dataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
            rootView = dataBinding.getRoot();
        } else {
            rootView = inflater.inflate(getLayoutId(), container);
        }

        RelativeLayout rl = new RelativeLayout(getContext());
        ViewGroup.LayoutParams pm = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        rl.setLayoutParams(pm);

        rl.setGravity(Gravity.CENTER);
        rl.addView(rootView);

        initView();
        return rl;
    }

    private Context mContext;

    @Override
    public void onStart() {
        super.onStart();
        //获取手机屏幕的长和宽
        mContext = getContext();
        if (mContext != null) {
            WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            Point point = new Point();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                mContext.getDisplay().getRealSize(point);
            } else {
                wm.getDefaultDisplay().getSize(point);
            }
            int width = point.x;

            //这个设置宽高的必须放在onstart方法里，不能放oncreateview里面
            Window dialogWindow = getDialog().getWindow();
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();

            // 布局文件居中
            dialogWindow.setGravity(Gravity.CENTER);
            lp.windowAnimations = animId;
//             dialogWindow.setLayout(lp.MATCH_PARENT, lp.WRAP_CONTENT);// 为了让对话框宽度铺满
            //设置弹窗的宽度，
            lp.width = width;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            //lp.alpha = 0.1f;
            dialogWindow.setAttributes(lp);

        }

    }

    public int getThemeStyle() {
        return backgroundDim ? R.style.BaseDialogStyle : R.style.BaseDialogNoBackground;
    }


    /**
     * 布局文件id
     *
     * @return 返回布局文件Layout 的id
     */
    protected abstract int getLayoutId();

    /**
     * 初始化布局
     */
    protected abstract void initView();

    /**
     * 使用dataBinding 需要泛型传入，不使用dataBing，返回false
     *
     * @return true:使用dataBinding
     */
    protected abstract boolean isUseDataBinding();

    /**
     * 关闭弹窗
     */
    public static final int VERSION_CODES = 29;

    public void disMissDialog() {
        dismissAllowingStateLoss();
    }


    /**
     * 调用返回键，弹窗消失前调用
     */
    public void onBackPressDismissBefore() {
    }

    public OnDismissListener getOnDismissListener() {
        return mOnDismissListener;
    }

    public void setOnDismissListener(OnDismissListener onDismissListener) {
        mOnDismissListener = onDismissListener;
    }

    public SureListener getOnSureListener() {
        return mOnSureListener;
    }

    public void setOnSureListener(SureListener onSureListener) {
        mOnSureListener = onSureListener;
    }

    public CancelListener getOnCancelListener() {
        return mOnCancelListener;
    }

    public void setOnCancelListener(CancelListener onCancelListener) {
        mOnCancelListener = onCancelListener;
    }

    public CloseListener getOnCloseListener() {
        return mOnCloseListener;
    }

    public void setOnCloseListener(CloseListener onCloseListener) {
        mOnCloseListener = onCloseListener;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unDisposable();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mOnDismissListener != null) {
            mOnDismissListener.onDismiss(dialog);
        }
    }


    protected View rootView;

    protected <T extends View> T $(int id) {
        if (rootView == null) {
            return null;
        }
        return (T) rootView.findViewById(id);
    }

    protected SureListener sureListener;


    public interface SureListener {
        void onSure();
    }

    public interface CancelListener {
        void onCancel();
    }

    public interface CloseListener {
        void onClose();
    }

    public interface OnDismissListener {
        void onDismiss(@NonNull DialogInterface dialog);
    }


    private CompositeDisposable mCompositeDisposable;

    public void addDisposable(Disposable disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);
    }

    public void unDisposable() {
        if (mCompositeDisposable != null && mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.clear();
        }
    }
}
