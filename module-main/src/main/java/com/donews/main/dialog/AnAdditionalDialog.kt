package com.donews.main.dialog

import android.animation.Animator
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.LinearInterpolator
import com.donews.base.fragmentdialog.AbstractFragmentDialog
import com.donews.main.R
import com.donews.main.databinding.AnAdditionalDialogLayoutBinding
import com.donews.utilslibrary.utils.SoundHelp
import com.vmadalin.easypermissions.EasyPermissions


/**
 * 额外获得奖励的弹窗
 *
 * @author lcl
 * @version v1.0
 * @date 2021/12/3
 */
class AnAdditionalDialog(
        /** 金额 */
        var number: String = "1.8",
        var count: Int = 4 //倒计时三秒
) : AbstractFragmentDialog<AnAdditionalDialogLayoutBinding>(),
        EasyPermissions.PermissionCallbacks {
    lateinit var eventListener: EventListener
    private val handler = Handler(Looper.getMainLooper())
    private var timeTask: Runnable? = null
//    private lateinit var addCoinsAnim: ObjectAnimator

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun getLayoutId(): Int {
        return R.layout.an_additional_dialog_layout
    }

    @SuppressLint("SetTextI18n", "ObjectAnimatorBinding")
    override fun initView() {
        timeTask = Runnable {
            count--
            dataBinding.mainDoubleAutoGetTv.text = "${count}s后自动领取"
            if (count > 0) {
                handler.postDelayed(timeTask!!, 1000)
            } else {
//                dismiss()
            }
        }
        handler.post(timeTask!!)
        SoundHelp.newInstance().init(context)
        SoundHelp.newInstance().onStart()
        dataBinding.tvNum.text = number
        setOnDismissListener {
            handler.removeCallbacksAndMessages(null)
            timeTask?.apply {
                handler.removeCallbacks(this)
            }
            if (eventListener != null) {
                eventListener.dismiss()
            }
        }
        dataBinding.mainDoubleGetTv.setOnClickListener {
            SoundHelp.newInstance().onRelease()
            dismiss()
        }
        dataBinding.mainDoubleCloseIv.setOnClickListener {
            SoundHelp.newInstance().onRelease()
            dismiss()
        }
        /*handler.postDelayed({
            val arr = arrayOf(dataBinding.ivYh0, dataBinding.ivYh1)
            var pd = 100L
            for (imageView in arr) {
                handler.postDelayed({
                    if (activity != null) {
                        val anim = AnimationUtils.loadAnimation(
                                activity,
                                R.anim.anim_yh_in
                        )
                        anim.setAnimationListener(object : Animation.AnimationListener {
                            override fun onAnimationStart(animation: Animation?) {
                            }

                            override fun onAnimationEnd(animation: Animation?) {
                                imageView.visibility = View.INVISIBLE
                            }

                            override fun onAnimationRepeat(animation: Animation?) {
                            }
                        })
                        anim.repeatCount = 1
                        imageView.startAnimation(anim)
                        imageView.visibility = View.VISIBLE
                    }
                }, pd)
                pd += Random().nextInt(200) + 1000
            }
        }, 150)*/

        val addCoinsAnim: ObjectAnimator = ObjectAnimator.ofFloat(dataBinding.mainDoubleAddCoinsTv, "translationY", 0f, -200f)
        addCoinsAnim.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                dataBinding.mainDoubleAddCoinsTv.visibility = View.GONE
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationRepeat(animation: Animator?) {
            }
        })
        addCoinsAnim.interpolator = LinearInterpolator()
        addCoinsAnim.duration = 2000
        addCoinsAnim.start()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        SoundHelp.newInstance().onRelease()
    }

    override fun isUseDataBinding(): Boolean {
        return true
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
    }

    interface EventListener {
        fun dismiss()
    }
}