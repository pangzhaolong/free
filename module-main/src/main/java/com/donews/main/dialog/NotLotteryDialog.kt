package com.donews.main.dialog

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import com.donews.base.fragmentdialog.AbstractFragmentDialog
import com.donews.main.R
import com.donews.main.databinding.MainExitDialogNotLotteryBinding
import com.donews.main.entitys.resps.NotLotteryConfig
import com.orhanobut.logger.Logger
import kotlin.random.Random

/**
 * 退出拦截，用户没有抽奖的情况下显示的弹出框
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/20 9:48
 */
class NotLotteryDialog : AbstractFragmentDialog<MainExitDialogNotLotteryBinding>() {

    companion object {

        const val PARAMS_CONFIG = "config"

        fun newInstance(notLotteryConfig: NotLotteryConfig): NotLotteryDialog {
            val args = Bundle()
            args.putSerializable(PARAMS_CONFIG, notLotteryConfig)
            val fragment = NotLotteryDialog()
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var notLotteryConfig: NotLotteryConfig
    private val handler = Handler(Looper.getMainLooper())

    override fun onAttach(context: Context) {
        super.onAttach(context)
        arguments?.let {
            notLotteryConfig = it.getSerializable(PARAMS_CONFIG) as NotLotteryConfig
        } ?: kotlin.run {
            notLotteryConfig = NotLotteryConfig()
        }
    }

    override fun isUseDataBinding(): Boolean {
        return true
    }

    override fun getLayoutId(): Int {
        return R.layout.main_exit_dialog_not_lottery
    }

    override fun initView() {
        val min = notLotteryConfig.minProbability
        val max = notLotteryConfig.maxProbability
        val delta = max - min
        val random = Random(System.currentTimeMillis())
        val probability = min + random.nextDouble(delta)
        Logger.d(probability * 100)
        dataBinding.probability = probability * 100
        handler.postDelayed(Runnable {
            dataBinding.ivClose.visibility = View.VISIBLE
        }, notLotteryConfig.closeBtnLazyShow * 1000L)
        requestGoodsInfo()
    }

    fun requestGoodsInfo() {

    }
}