package com.donews.main.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import com.donews.base.fragmentdialog.AbstractFragmentDialog
import com.donews.common.utils.DensityUtils
import com.donews.main.R
import com.donews.main.databinding.MainExitDialogContinueLotteryBinding
import com.donews.main.entitys.resps.ContinueLotteryConfig
import com.donews.main.entitys.resps.ExitDialogRecommendGoodsResp
import com.donews.main.entitys.resps.NotLotteryConfig
import com.donews.main.utils.ExitInterceptUtils
import com.donews.network.EasyHttp
import com.donews.network.cache.model.CacheMode
import com.donews.network.callback.SimpleCallBack
import com.donews.network.exception.ApiException
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.random.Random

/**
 * 拦截弹窗之继续抽奖
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/20 20:28
 */
class ContinueLotteryDialog : AbstractFragmentDialog<MainExitDialogContinueLotteryBinding>() {

    companion object {
        const val LIMIT_DATA = "1"

        const val TEN_THOUSAND = 10000

        private const val PARAMS_CONFIG = "config"

        fun newInstance(continueLotteryConfig: ContinueLotteryConfig): ContinueLotteryDialog {
            val args = Bundle()
            args.putSerializable(PARAMS_CONFIG, continueLotteryConfig)
            val fragment = ContinueLotteryDialog()
            fragment.arguments = args
            return fragment
        }
    }


    private lateinit var continueLotteryConfig: ContinueLotteryConfig
    private val handler = Handler(Looper.getMainLooper())

    override fun onAttach(context: Context) {
        super.onAttach(context)
        arguments?.let {
            continueLotteryConfig = it.getSerializable(PARAMS_CONFIG) as ContinueLotteryConfig
        } ?: kotlin.run {
            continueLotteryConfig = ContinueLotteryConfig()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.main_exit_dialog_continue_lottery
    }

    override fun initView() {
        //原价 中划线
        dataBinding.tvOriginalPrice.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
        //点击事件
        dataBinding.eventListener = EventListener()
        //设置标题信息
        setTitle()
        //请求商品信息
        requestGoodsInfo()
        showCloseBtn()
    }

    override fun isUseDataBinding(): Boolean {
        return true
    }

    private fun showCloseBtn() {
        handler.postDelayed(Runnable {
            dataBinding.ivClose.visibility = View.VISIBLE
        }, continueLotteryConfig.closeBtnLazyShow * 1000L)
    }


    private fun setTitle() {
        val random = Random(System.currentTimeMillis())

        val minTimes = continueLotteryConfig.minLotteryTimes
        val maxTimes = continueLotteryConfig.maxLotteryTimes
        val times = (random.nextInt(maxTimes - minTimes) + minTimes).toString()

        val min = continueLotteryConfig.minProbability
        val max = continueLotteryConfig.maxProbability
        val delta = max - min
        val prob = (min + random.nextDouble(delta)) * 100

        val format = DecimalFormat("0.##")
        format.roundingMode = RoundingMode.FLOOR
        val probString = format.format(prob)

        val result = getString(R.string.main_exit_continue_lottery_title, times, probString)
        val spannable: SpannableString = SpannableString(result)
        spannable.setSpan(
            ForegroundColorSpan(Color.parseColor("#F53838")),
            2,
            2+times.length,
            Spannable.SPAN_INCLUSIVE_EXCLUSIVE
        )
        spannable.setSpan(
            AbsoluteSizeSpan(DensityUtils.dip2px(20f)),
            2,
            2+times.length,
            Spannable.SPAN_INCLUSIVE_EXCLUSIVE
        )

        spannable.setSpan(
            ForegroundColorSpan(Color.parseColor("#F53838")),
            result.length - probString.length - 1,
            result.length,
            Spannable.SPAN_INCLUSIVE_EXCLUSIVE
        )
        spannable.setSpan(
            AbsoluteSizeSpan(DensityUtils.dip2px(20f)),
            result.length - probString.length - 1,
            result.length,
            Spannable.SPAN_INCLUSIVE_EXCLUSIVE
        )

        dataBinding.title = spannable
    }

    private fun requestGoodsInfo() {
        val disposable = EasyHttp.get(ExitInterceptUtils.getRecommendGoodsUrl())
            .cacheMode(CacheMode.NO_CACHE)
            .params("limit", LIMIT_DATA)
            .execute(object : SimpleCallBack<ExitDialogRecommendGoodsResp>() {
                override fun onError(e: ApiException?) {

                }

                override fun onSuccess(t: ExitDialogRecommendGoodsResp?) {
                    t?.list?.get(0)?.let {
                        if (dataBinding != null) {
                            dataBinding.goodsBean = it

                            val peopleNumberString = if (it.totalPeople > NotLotteryDialog.TEN_THOUSAND) {
                                (it.totalPeople / TEN_THOUSAND).toString().substring(0, 3)

                            } else {
                                it.totalPeople.toString()
                            }
                            dataBinding.totalPeople = peopleNumberString
                        }
                    }
                }
            })
        addDisposable(disposable)
    }

    inner class EventListener {
        fun clickNext(view: View) {
            requestGoodsInfo()
        }

        fun clickLottery(view: View) {
            if (onSureListener != null) {
                onSureListener.onSure()
            }
        }

        fun clickClose(view: View) {
            if (onCloseListener != null) {
                onCloseListener.onClose()
            }
        }
    }
}