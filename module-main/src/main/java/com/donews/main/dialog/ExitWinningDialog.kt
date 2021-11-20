package com.donews.main.dialog

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import com.donews.base.fragmentdialog.AbstractFragmentDialog
import com.donews.base.utils.glide.GlideUtils
import com.donews.main.R
import com.donews.main.databinding.MainExitDialogRedPacketNotAllOpenBinding
import com.donews.main.databinding.MainExitDialogWinningOpenBinding
import com.donews.middle.bean.HighValueGoodsBean
import com.donews.middle.cache.GoodsCache
import com.donews.utilslibrary.utils.KeySharePreferences
import com.donews.utilslibrary.utils.KeySharePreferences.STEPS_TO_GOLD_RED_PACKAGE_COUNTS
import com.donews.utilslibrary.utils.SPUtils
import com.donews.utilslibrary.utils.UrlUtils
import java.text.SimpleDateFormat
import java.util.*

/**
 * APP退出拦截，恭喜中奖弹窗
 *
 * @author lcl
 * @version v1.0
 */
class ExitWinningDialog : AbstractFragmentDialog<MainExitDialogWinningOpenBinding>() {

    companion object {
        fun newInstance(): ExitWinningDialog {
            val args = Bundle()
            val fragment = ExitWinningDialog()
            fragment.arguments = args
            return fragment
        }
    }

    private val handler = Handler(Looper.getMainLooper())
    private var countTime = 10 * 60
    private var runnableTask: Runnable? = null
    private val goods: HighValueGoodsBean? =
        GoodsCache.readGoodsBean(HighValueGoodsBean::class.java, "exit")
    val item: HighValueGoodsBean.GoodsInfo? by lazy {
        val t: HighValueGoodsBean = GoodsCache.readGoodsBean(HighValueGoodsBean::class.java, "exit")
        if (t.list?.isNotEmpty() == true) {
            t.list[0]
        } else {
            null
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }


    override fun getLayoutId(): Int {
        return R.layout.main_exit_dialog_winning_open
    }

    override fun initView() {
        dataBinding.eventListener = EventListener()
        dataBinding.tvFailure.setOnClickListener {
            if (it.visibility == View.VISIBLE) {
                onLaterListener.onClose()
            }
        }
        showAnimUI()
    }

    //显示动画UI
    private fun showAnimUI() {
        dataBinding.tvTitle.text = "抽取大奖中..."
        dataBinding.llBut.visibility = View.INVISIBLE
        dataBinding.tvFailure.visibility = View.INVISIBLE
        dataBinding.ivIconBy.visibility = View.INVISIBLE
        dataBinding.tvGoodsTitle.visibility = View.INVISIBLE
        if (goods?.list?.isNotEmpty() == true) {
            dataBinding.scroView.setDatas(goods.list)
            handler.postDelayed({
                dataBinding.scroView.startScroll {
                    handler.postDelayed({
                        showWindUI() //延迟显示中奖视图
                    }, 100)
                }
            }, 250)
        } else {
            showWindUI()
        }
    }

    //显示中奖UI
    private fun showWindUI() {
        item?.apply {
            dataBinding.tvGoodsTitle.text = this.title
            GlideUtils.loadImageView(
                activity, UrlUtils.formatHeadUrlPrefix(this.mainPic), dataBinding.ivIcon
            )
        }
        dataBinding.tvTitle.text = "恭喜选中超值大奖"
        dataBinding.llBut.visibility = View.VISIBLE
        dataBinding.tvFailure.visibility = View.VISIBLE
        dataBinding.ivIconBy.visibility = View.VISIBLE
        dataBinding.tvGoodsTitle.visibility = View.VISIBLE
        dataBinding.scroView.visibility = View.GONE
        runnableTask = Runnable {
            if (countTime <= 0) {
                dataBinding.tvFailure.text = "已过期"
            } else {
                updateTime()
                handler.postDelayed(runnableTask!!, 1000)
            }
            countTime--
        }
        runnableTask!!.run()
        showCloseBtn()
        //手
        dataBinding.maskingHand.imageAssetsFolder = "images"
        dataBinding.maskingHand.setAnimation("lottery_finger.json")
        dataBinding.maskingHand.loop(true)
        dataBinding.maskingHand.playAnimation()
    }

    private fun updateTime() {
        var f: String = (countTime / 60).let {
            if (it < 10) {
                "0$it"
            } else {
                it.toString()
            }
        }
        var m: String = (countTime.mod(60)).let {
            if (it < 10) {
                "0$it"
            } else {
                it.toString()
            }
        }
        dataBinding.tvFailure.text = "00:$f:$m"
    }

    override fun isUseDataBinding(): Boolean {
        return true
    }

    private fun showCloseBtn() {
        handler.postDelayed({
            dataBinding.ivClose.visibility = View.VISIBLE
        }, 1000L)
    }

    override fun onDestroy() {
        handler.removeCallbacksAndMessages(activity)
        handler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }

    inner class EventListener {
        fun clickTvClose(view: View) {
            onCancelListener?.onCancel()
        }

        fun clickIvClose(view: View) {
            if (view.visibility == View.VISIBLE) {
                onCloseListener?.onClose()
            }
        }

        fun clickOpenPacket(view: View) {
            onSureListener?.onSure()
        }

        fun onLaterListener(view: View) {
            if (view.visibility == View.VISIBLE) {
                onLaterListener?.onClose()
            }
        }
    }
}