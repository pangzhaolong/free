package com.donews.main.dialog

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
import com.dn.sdk.bean.PreloadAdState
import com.dn.sdk.bean.preload.PreloadAd
import com.dn.sdk.listener.impl.SimpleSplashListener
import com.donews.base.base.AppManager
import com.donews.common.ad.business.loader.AdManager
import com.donews.common.ad.business.loader.IPreloadAdListener
import com.donews.common.ad.business.manager.JddAdConfigManager
import com.donews.common.contract.LoginHelp
import com.donews.main.R
import com.donews.main.databinding.MainDialogHotStartBinding
import com.donews.main.utils.HotStartCacheUtils
import com.donews.utilslibrary.utils.DensityUtils
import com.gyf.immersionbar.ImmersionBar
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

    private var mHotStartEnable: Boolean = true
    private var mHotDoubleSplash: Boolean = false

    private var mFullAd: Boolean = true

    private var mFirstAd: PreloadAd? = null
    private var mSecondAd: PreloadAd? = null

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
        dialog?.setOnDismissListener {
            HotStartCacheUtils.clear()
            HotStartCacheUtils.checkActivity(AppManager.getInstance().topActivity)
        }
        dialog?.setOnKeyListener(this)
    }

    override fun onResume() {
        super.onResume()
        showProgress()
    }


    fun initView() {
        ImmersionBar.with(this)
            .fullScreen(true)
            .transparentStatusBar()
            .fitsSystemWindows(false)
            .init()

        JddAdConfigManager.addListener {
            val configBean = JddAdConfigManager.jddAdConfigBean;
            mHotStartEnable = configBean.hotStartAdEnable
            mHotDoubleSplash = (configBean.hotStartDoubleSplashOpen
                    && LoginHelp.getInstance().checkUserRegisterTime(configBean.hotStartDoubleSplash))
            mFullAd = configBean.hotStartSplashStyle != 1

            val params: ConstraintLayout.LayoutParams =
                mDataBinding.adFllContainer.layoutParams as ConstraintLayout.LayoutParams
            if (mFullAd) {
                params.bottomMargin = 0
            } else {
                params.bottomMargin = DensityUtils.dip2px(96f)
            }
        }
    }


    fun preloadFirstAd() {
        val preloadListener = object : IPreloadAdListener {
            override fun preloadAd(ad: PreloadAd) {
                mFirstAd = ad
            }
        }

        val splashListener = object : SimpleSplashListener() {
            override fun onAdError(code: Int, errorMsg: String?) {
                super.onAdError(code, errorMsg)
                mFirstAd = null
            }

            override fun onAdExposure() {
                super.onAdExposure()
                if (mHotDoubleSplash) {
                    preloadSecondAd()
                }
            }

            override fun onAdDismiss() {
                super.onAdDismiss()
                if (!mHotDoubleSplash) {
                    dismissAllowingStateLoss()
                } else {
                    showAd2()
                }
            }
        }

        if (mFullAd) {
            AdManager.preloadFullScreenSplashAd(
                requireActivity(),
                mDataBinding.adFllContainer,
                preloadListener,
                splashListener
            )
        } else {
            AdManager.preloadHalfScreenSplashAd(
                requireActivity(),
                mDataBinding.adFllContainer,
                preloadListener,
                splashListener
            )
        }
    }

    private fun preloadSecondAd() {
        val preloadListener = object : IPreloadAdListener {
            override fun preloadAd(ad: PreloadAd) {
                mSecondAd = ad
            }
        }

        val splashListener = object : SimpleSplashListener() {
            override fun onAdError(code: Int, errorMsg: String?) {
                super.onAdError(code, errorMsg)
                mSecondAd = null
            }

            override fun onAdShow() {
                super.onAdShow()
            }

            override fun onAdDismiss() {
                super.onAdDismiss()
                dismissAllowingStateLoss()
            }
        }

        if (mFullAd) {
            AdManager.preloadFullScreenSplashAd(
                requireActivity(),
                mDataBinding.adFllContainer,
                preloadListener,
                splashListener
            )
        } else {
            AdManager.preloadHalfScreenSplashAd(
                requireActivity(),
                mDataBinding.adFllContainer,
                preloadListener,
                splashListener
            )
        }
    }

    private fun loadFirstAd() {
        val splashListener = object : SimpleSplashListener() {
            override fun onAdError(code: Int, errorMsg: String?) {
                super.onAdError(code, errorMsg)
                dismissAllowingStateLoss()
            }

            override fun onAdDismiss() {
                super.onAdDismiss()
                if (mHotDoubleSplash) {
                    loadSecondAd()
                } else {
                    dismissAllowingStateLoss()
                }
            }
        }

        if (mFullAd) {
            AdManager.loadFullScreenSplashAd(
                requireActivity(),
                mDataBinding.adFllContainer,
                splashListener
            )
        } else {
            AdManager.loadHalfScreenSplashAd(
                requireActivity(),
                mDataBinding.adFllContainer,
                splashListener
            )
        }
    }

    private fun loadSecondAd() {
        val splashListener = object : SimpleSplashListener() {
            override fun onAdError(code: Int, errorMsg: String?) {
                super.onAdError(code, errorMsg)
                dismissAllowingStateLoss()
            }

            override fun onAdDismiss() {
                super.onAdDismiss()
                dismissAllowingStateLoss()
            }
        }

        if (mFullAd) {
            AdManager.loadFullScreenSplashAd(
                requireActivity(),
                mDataBinding.adFllContainer,
                splashListener
            )
        } else {
            AdManager.loadHalfScreenSplashAd(
                requireActivity(),
                mDataBinding.adFllContainer,
                splashListener
            )
        }
    }

    fun showAd() {
        if (mFirstAd == null || mFirstAd?.getLoadState() == PreloadAdState.Error) {
            loadFirstAd()
            return
        }

        if (mFirstAd != null) {
            mFirstAd!!.showAd()
        }
    }

    private fun showAd2() {
        if (mSecondAd == null || mSecondAd?.getLoadState() == PreloadAdState.Error) {
            loadSecondAd()
            return
        }
        if (mSecondAd != null) {
            mSecondAd!!.showAd()
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
    }

    override fun onKey(dialog: DialogInterface?, keyCode: Int, event: KeyEvent?): Boolean {
        return keyCode == KeyEvent.KEYCODE_BACK
    }
}