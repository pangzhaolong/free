package com.donews.middle.dialog

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.DialogInterface
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.dn.events.ad.HotStartEvent
import com.dn.sdk.bean.PreloadAdState
import com.dn.sdk.bean.preload.PreloadAd
import com.dn.sdk.listener.splash.SimpleSplashListener
import com.donews.base.base.AppManager
import com.donews.middle.R
import com.donews.middle.adutils.SplashAd
import com.donews.middle.databinding.MainDialogHotStartBinding
import com.donews.middle.utils.HotStartCacheUtils
import com.donews.yfsdk.manager.AdConfigManager
import com.gyf.immersionbar.ImmersionBar
import org.greenrobot.eventbus.EventBus
import java.lang.reflect.Field

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/12/9 10:01
 */
class HotStartDialog : DialogFragment(), DialogInterface.OnKeyListener {

    companion object {
        fun newInstance(): HotStartDialog {
            val args = Bundle()
            val fragment = HotStartDialog()
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var mDataBinding: MainDialogHotStartBinding

    private var mFirstAd: PreloadAd? = null
    private var mIsShow: Boolean = false
    private var mIsAdLoaded: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BaseDialogTheme_FullScreen)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mDataBinding = MainDialogHotStartBinding.inflate(inflater, container, false)
        return mDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        dialog?.setOnKeyListener(this)
    }

    override fun onResume() {
        super.onResume()
        showProgress()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        HotStartCacheUtils.clear()
        if (AppManager.getInstance() != null && AppManager.getInstance().topActivity != null) {
            HotStartCacheUtils.checkActivity(AppManager.getInstance().topActivity)
        }
        EventBus.getDefault().post(HotStartEvent(false))
    }

    fun initView() {
        ImmersionBar.with(this)
                .fullScreen(true)
                .transparentStatusBar()
                .fitsSystemWindows(false)
                .init()

        if (!AdConfigManager.mNormalAdBean.enable) {
            return
        }

        val params: ConstraintLayout.LayoutParams =
                mDataBinding.adFllContainer.layoutParams as ConstraintLayout.LayoutParams
        params.bottomMargin = 0
    }

    private fun loadFirstAd() {
        val splashListener = object : SimpleSplashListener() {
            override fun onAdError(code: Int, errorMsg: String?) {
                super.onAdError(code, errorMsg)
                loadCsjSecondAd()
            }

            override fun onAdDismiss() {
                super.onAdDismiss()
                /*if (mHotDoubleSplash) {
                    loadSecondAd()
                } else {*/
                    dismissDialog()
//                }
            }

            override fun onAdLoad() {
                super.onAdLoad()
                mIsAdLoaded = true
            }
        }

        if (activity == null || !isAdded) {
            return
        }
        try {
            if (activity?.isFinishing == true) {
                return
            }
            SplashAd.loadSplashAd(requireActivity(), true, mDataBinding.adFllContainer, splashListener, false)
        } catch (e: Exception) {}
    }

    private fun loadCsjSecondAd() {
        val splashListener = object : SimpleSplashListener() {
            override fun onAdError(code: Int, errorMsg: String?) {
                super.onAdError(code, errorMsg)
                try {
                    dismissDialog()
                } catch (e: Exception){}
            }

            override fun onAdDismiss() {
                super.onAdDismiss()
                dismissDialog()
            }

            override fun onAdLoad() {
                super.onAdLoad()
                mIsAdLoaded = true
            }
        }

        if (activity == null || !isAdded) {
            return
        }
        try {
            if (activity?.isFinishing == true) {
                return
            }
            SplashAd.loadCsjSplashAd(requireActivity(), true, mDataBinding.adFllContainer, splashListener, false)
        } catch (e: Exception) {}
    }

    fun showAd() {
        if (mFirstAd == null || mFirstAd?.getLoadState() == PreloadAdState.Error) {
            loadFirstAd()
            return
        }

        if (mFirstAd != null) {
            mFirstAd!!.showAd()
            mIsShow = true
        }
    }

    fun showAllowingStateLoss(manager: FragmentManager, tag: String?) {
        try {
            val dismissed: Field = DialogFragment::class.java.getDeclaredField("mDismissed")
            dismissed.isAccessible = true
            dismissed.set(this, false)
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
        try {
            val shown: Field = DialogFragment::class.java.getDeclaredField("mShownByMe")
            shown.isAccessible = true
            shown.set(this, true)
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
        val ft: FragmentTransaction = manager.beginTransaction()
        ft.add(this, tag)
        ft.commitAllowingStateLoss()
    }

    fun dismissDialog() {
        try {
            mIsAdLoaded = false
            stopProgress()
            dismissAllowingStateLoss()
        } catch (e: java.lang.Exception) {
        }
    }

    private var progressAnim: ValueAnimator? = null


    fun showProgress() {
        if (progressAnim != null) {
            progressAnim!!.cancel()
        }
        progressAnim = ValueAnimator.ofInt(0, 100)
                .apply {
                    addUpdateListener {
                        if (mDataBinding != null) {
                            mDataBinding.pbProgress.progress = it.animatedValue as Int
                        }
                    }

                    addListener(object : Animator.AnimatorListener {
                        override fun onAnimationStart(animation: Animator?) {
                            mDataBinding.pbProgress.visibility = View.VISIBLE
                        }

                        override fun onAnimationEnd(animation: Animator?) {
                            mDataBinding.pbProgress.visibility = View.INVISIBLE
                            if (!mIsAdLoaded) {
                                dismissDialog()
                            }
                        }

                        override fun onAnimationCancel(animation: Animator?) {
                            mDataBinding.pbProgress.visibility = View.INVISIBLE
                        }

                        override fun onAnimationRepeat(animation: Animator?) {
                        }
                    })
                    duration = 5000
                }
        progressAnim?.start()
    }

    fun stopProgress() {
        progressAnim?.cancel()
        progressAnim = null
    }

    override fun onKey(dialog: DialogInterface?, keyCode: Int, event: KeyEvent?): Boolean {
        return keyCode == KeyEvent.KEYCODE_BACK
    }
}