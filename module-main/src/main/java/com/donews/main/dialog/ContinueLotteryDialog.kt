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
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.alibaba.android.arouter.launcher.ARouter
import com.donews.base.fragmentdialog.AbstractFragmentDialog
import com.donews.common.router.RouterFragmentPath
import com.donews.utilslibrary.utils.DensityUtils
import com.donews.main.R
import com.donews.main.databinding.MainExitDialogContinueLotteryBinding
import com.donews.main.entitys.resps.ContinueLotteryConfig
import com.donews.main.entitys.resps.ExitDialogRecommendGoods
import com.donews.main.entitys.resps.ExitDialogRecommendGoodsResp
import com.donews.main.utils.ExitInterceptUtils
import com.donews.middle.abswitch.ABSwitch
import com.donews.network.EasyHttp
import com.donews.network.cache.model.CacheMode
import com.donews.network.callback.SimpleCallBack
import com.donews.network.exception.ApiException
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.random.Random

/**
 * 已登录用户。当日未参与抽奖的弹窗
 *
 * @author XuShuai
 * 改：lcl
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
    private var goodsInfo: ExitDialogRecommendGoods? = null

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

    override fun onDestroy() {
        handler.removeCallbacksAndMessages(activity)
        handler.removeCallbacksAndMessages(null)
        super.onDestroy()
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
        //手
        dataBinding.maskingHand.imageAssetsFolder = "images"
        dataBinding.maskingHand.setAnimation("lottery_finger.json")
        dataBinding.maskingHand.loop(true)
        dataBinding.maskingHand.playAnimation()

        handler.postDelayed({
            val arr = arrayOf(dataBinding.dialogYhL, dataBinding.dialogYhRt)
            var pd = 100L
            for (imageView in arr) {
                handler.postDelayed({
                    if (activity != null) {
                        val anim = AnimationUtils.loadAnimation(
                            activity,
                            R.anim.anim_yh_in
                        )
                        anim.setAnimationListener(object :Animation.AnimationListener{
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
                pd += java.util.Random().nextInt(200) + 1000
            }
        }, 150)

    }

    override fun isUseDataBinding(): Boolean {
        return true
    }

    private fun showCloseBtn() {
        handler.postDelayed({
            dataBinding.ivClose.visibility = View.VISIBLE
            dataBinding.tvPExit.visibility = View.VISIBLE
        }, continueLotteryConfig.closeBtnLazyShow * 1000L)
    }


    private fun setTitle() {
        val prob = "3千+"//Random.nextInt(300).toString()
        val result = "已有${prob}人获得免单奖"
        val spannable: SpannableString = SpannableString(result)
        spannable.setSpan(
            AbsoluteSizeSpan(DensityUtils.dip2px(28f)),
            2,
            2 + prob.length,
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

                            val peopleNumberString =
                                if (it.totalPeople > NotLotteryDialog.TEN_THOUSAND) {
                                    (it.totalPeople / TEN_THOUSAND).toString().substring(0, 3)

                                } else {
                                    it.totalPeople.toString()
                                }
                            dataBinding.totalPeople = peopleNumberString

                            goodsInfo = it

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
            goodsInfo?.run {
                ARouter.getInstance()
                    .build(RouterFragmentPath.Lottery.PAGER_LOTTERY)
                    .withString("goods_id", goodsId)
                    .withBoolean("start_lottery", ABSwitch.Ins().isOpenAutoLottery)
                    .navigation()
            }
        }

        fun clickClose(view: View) {
            if (onCloseListener != null && view.visibility == View.VISIBLE) {
                onCloseListener.onClose()
            }
        }

        fun clickLater(view: View) {
            if (onLaterListener != null && view.visibility == View.VISIBLE) {
                onLaterListener?.onClose()
            }
        }
    }
}