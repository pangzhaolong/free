package com.donews.main.dialog

import android.content.Context
import android.content.DialogInterface
import android.graphics.Paint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.launcher.ARouter
import com.donews.base.fragmentdialog.AbstractFragmentDialog
import com.donews.base.model.BaseLiveDataModel
import com.donews.base.viewmodel.BaseLiveDataViewModel
import com.donews.common.router.RouterFragmentPath
import com.donews.main.R
import com.donews.main.databinding.MainExitDialogNotLotteryBinding
import com.donews.main.entitys.resps.ExitDialogRecommendGoods
import com.donews.main.entitys.resps.ExitDialogRecommendGoodsResp
import com.donews.main.entitys.resps.NotLotteryConfig
import com.donews.main.utils.ExitInterceptUtils
import com.donews.network.EasyHttp
import com.donews.network.cache.model.CacheMode
import com.donews.network.callback.SimpleCallBack
import com.donews.network.exception.ApiException
import com.orhanobut.logger.Logger
import io.reactivex.disposables.CompositeDisposable
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

        const val LIMIT_DATA = "1"

        const val TEN_THOUSAND = 10000

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
    private var goodsInfo: ExitDialogRecommendGoods? = null

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
        //原价 中划线
        dataBinding.tvOriginalPrice.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG

        //点击事件
        dataBinding.eventListener = EventListener()

        //请求商品信息
        requestGoodsInfo()

        showCloseBtn()
    }

    private fun showCloseBtn() {
        handler.postDelayed(Runnable {
            dataBinding.ivClose.visibility = View.VISIBLE
        }, notLotteryConfig.closeBtnLazyShow * 1000L)
    }

    private fun randomProbability() {
        val min = notLotteryConfig.minProbability
        val max = notLotteryConfig.maxProbability
        val delta = max - min
        val random = Random(System.currentTimeMillis())
        val probability = min + random.nextDouble(delta)
        dataBinding.probability = probability * 100
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

                            randomProbability()

                            dataBinding.goodsBean = it

                            val peopleNumberString = if (it.totalPeople > TEN_THOUSAND) {
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
                    .navigation();
            }
        }

        fun clickClose(view: View) {
            if (onCloseListener != null) {
                onCloseListener.onClose()
            }
        }
    }
}