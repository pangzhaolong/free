package com.donews.collect.util

import android.annotation.SuppressLint
import androidx.fragment.app.FragmentActivity
import com.donews.collect.dialog.*

/**
 *  make in st
 *  on 2022/5/16 17:16
 */
@SuppressLint("StaticFieldLeak")
object DialogUtil {

    private var goodDialog: GoodDialog? = null

    fun showGoodDialog(
        activity: FragmentActivity,
        goodJson:String,
        dis:() -> Unit = {},
        dialogCancel:() -> Unit = {},
        dialogBtn:(goodId: String) -> Unit = {},
    ) {
        if (goodDialog != null && goodDialog?.dialog != null && goodDialog?.dialog!!.isShowing) {
            return
        }
        goodDialog = GoodDialog.newInstance(goodJson).apply {
            setOnDismissListener {
                dis.invoke()
                goodDialog = null
            }
            clickDialogBtn = {
                dialogBtn.invoke(it)
            }
            clickDialogCancel = {
                dialogCancel.invoke()
            }
        }

        goodDialog?.showAllowingStateLoss(activity.supportFragmentManager, GoodDialog::class.simpleName)

    }

    private var changeDialog: ChangeDialog? = null

    fun showChangeGoodDialog(
        activity: FragmentActivity,
        goodJson:String,
        dialogBtn:(cardId: String) -> Unit = {},
    ) {
        if (changeDialog != null && changeDialog?.dialog != null && changeDialog?.dialog!!.isShowing) {
            return
        }
        changeDialog = ChangeDialog.newInstance(goodJson).apply {
            setOnDismissListener {
                changeDialog = null
            }
            clickDialogBtn = {
                dialogBtn.invoke(it)
            }
        }

        changeDialog?.showAllowingStateLoss(activity.supportFragmentManager, ChangeDialog::class.simpleName)

    }

    private var drawDialog: DrawDialog? = null

    fun showDrawDialog(
        activity: FragmentActivity,
        no:Int = 0,
        img:String = "",
        dialogBtn:() -> Unit = {},
    ) {
        if (drawDialog != null && drawDialog?.dialog != null && drawDialog?.dialog!!.isShowing) {
            return
        }
        drawDialog = DrawDialog.newInstance(no,img).apply {
            setOnDismissListener {
                drawDialog = null
            }
            clickDialogBtn = {
                dialogBtn.invoke()
            }
        }

        drawDialog?.showAllowingStateLoss(activity.supportFragmentManager, DrawDialog::class.simpleName)

    }

    private var failDialog: FailDialog? = null

    fun showFailDialog(
        activity: FragmentActivity,
        goodJson:String,
        timeoutCall: () -> Unit = {}
    ) {
        if (failDialog != null && failDialog?.dialog != null && failDialog?.dialog!!.isShowing) {
            return
        }
        failDialog = FailDialog.newInstance(goodJson).apply {
            setOnDismissListener {
                failDialog = null
                timeoutCall.invoke()
            }
        }

        failDialog?.showAllowingStateLoss(activity.supportFragmentManager, FailDialog::class.simpleName)

    }

    private var stepOneDialog: StepOneDialog? = null

    fun showStepOneDialog(
        activity: FragmentActivity,
        timeoutCall: () -> Unit = {}
    ) {
        if (stepOneDialog != null && stepOneDialog?.dialog != null && stepOneDialog?.dialog!!.isShowing) {
            return
        }
        stepOneDialog = StepOneDialog.newInstance().apply {
            setOnDismissListener {
                stepOneDialog = null
                timeoutCall.invoke()
            }
        }

        stepOneDialog?.showAllowingStateLoss(activity.supportFragmentManager, StepOneDialog::class.simpleName)

    }

    private var stepTwoDialog: StepTwoDialog? = null

    fun showStepTwoDialog(
        activity: FragmentActivity,
        timeoutCall: () -> Unit = {}
    ) {
        if (stepTwoDialog != null && stepTwoDialog?.dialog != null && stepTwoDialog?.dialog!!.isShowing) {
            return
        }
        stepTwoDialog = StepTwoDialog.newInstance().apply {
            setOnDismissListener {
                stepTwoDialog = null
                timeoutCall.invoke()
            }
        }

        stepTwoDialog?.showAllowingStateLoss(activity.supportFragmentManager, StepTwoDialog::class.simpleName)

    }

    private var stepThreeDialog: StepThreeDialog? = null

    fun showStepThreeDialog(
        activity: FragmentActivity,
        timeoutCall: () -> Unit = {}
    ) {
        if (stepThreeDialog != null && stepThreeDialog?.dialog != null && stepThreeDialog?.dialog!!.isShowing) {
            return
        }
        stepThreeDialog = StepThreeDialog.newInstance().apply {
            setOnDismissListener {
                stepThreeDialog = null
                timeoutCall.invoke()
            }
        }

        stepThreeDialog?.showAllowingStateLoss(activity.supportFragmentManager, StepThreeDialog::class.simpleName)

    }

    private var stepFourDialog: StepFourDialog? = null

    fun showStepFourDialog(
        activity: FragmentActivity,
        dialogBtn:() -> Unit = {}
    ) {
        if (stepFourDialog != null && stepFourDialog?.dialog != null && stepFourDialog?.dialog!!.isShowing) {
            return
        }
        stepFourDialog = StepFourDialog.newInstance().apply {
            setOnDismissListener {
                stepFourDialog = null
                dialogBtn.invoke()
            }
        }

        stepFourDialog?.showAllowingStateLoss(activity.supportFragmentManager, StepFourDialog::class.simpleName)

    }

}