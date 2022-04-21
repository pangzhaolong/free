package com.donews.yfsdk.manager

import android.os.Handler
import android.os.Looper
import android.os.Message
import com.dn.sdk.utils.AdLoggerUtils
import com.donews.network.EasyHttp
import com.donews.network.cache.model.CacheMode
import com.donews.network.callback.SimpleCallBack
import com.donews.network.exception.ApiException
import com.donews.utilslibrary.utils.AppInfo
import com.donews.utilslibrary.utils.DeviceUtils
import com.donews.utilslibrary.utils.withConfigParams
import com.donews.yfsdk.BuildConfig
import com.donews.yfsdk.bean.*
import com.tencent.mmkv.MMKV

/**
 * 奖多多 广告 管理类
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/12/3 15:52
 */
object AdConfigManager {

    private val mmkv = MMKV.defaultMMKV()!!

    private const val KEY_AD_REWARD_ID_CONFIG = "key_ad_reward_id_config"
    private const val KEY_AD_NORMAL_CONFIG = "key_ad_normal_config"

    private const val MESSAGE_ID: Int = 10001

    var mRewardVideoId: RewardVideoId = getDefaultRewardId()
    var mNormalAdBean: NormalBean = getDefaultNormalBean()

    fun init() {
        updateNormalAd()
        updateRewardId()
    }

    fun init(adConfigBean: AdConfigBean) {
        if (mRewardVideoId.reward_video_id.isBlank()) {
            mRewardVideoId.reward_video_id = adConfigBean.reward
            mNormalAdBean.splash.DnIdNew = adConfigBean.splash
            mNormalAdBean.banner.DnIdNew = adConfigBean.banner
            mNormalAdBean.interstitial.DnIdNew = adConfigBean.interstitial
            mNormalAdBean.information.DnIdNew = adConfigBean.information
            mNormalAdBean.fullScreen.GmIdNew = adConfigBean.full
            mNormalAdBean.drawInformation.CsjIdNew = adConfigBean.drawInformation
            mmkv.encode(KEY_AD_REWARD_ID_CONFIG, mRewardVideoId)
        }
        updateNormalAd()
        updateRewardId()
    }

    fun updateRewardId() {
        try {
            var url = if (BuildConfig.HTTP_DEBUG) {
                "http://ecpm-customer.dev.tagtic.cn/api/v2/reward/ad"
            } else {
                "http://ecpm-customer.xg.tagtic.cn/api/v2/reward/ad"
            }

            url += "?suuid=${DeviceUtils.getMyUUID()}&user_id=${AppInfo.getUserId()}&package_name=${DeviceUtils.getPackage()}&channel=${DeviceUtils.getChannelName()}"
            AdLoggerUtils.d("获取广告分层数据，激励视频广告位， Url: $url")
            EasyHttp.get(url)
                    .cacheMode(CacheMode.NO_CACHE)
                    .isShowToast(false)
                    .execute(object : SimpleCallBack<RewardVideoId>() {
                        override fun onError(e: ApiException?) {
                            mRewardVideoId = getDefaultRewardId()
                            AdLoggerUtils.d("获取广告分层数据失败，使用默认激励视频广告位 ${mRewardVideoId.layer_id}  ${e?.message}")
                        }

                        override fun onSuccess(t: RewardVideoId?) {
                            t?.let {
                                mmkv.encode(KEY_AD_REWARD_ID_CONFIG, it)
                            }
                            mRewardVideoId = t ?: getDefaultRewardId()
                            AdLoggerUtils.d("获取广告分层数据成功，使用新的激励视频广告位: ${mRewardVideoId.layer_id} -> ${mRewardVideoId.reward_video_id}")
                        }
                    })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun updateNormalAd() {
        if (mHandler != null) {
            mHandler.removeMessages(MESSAGE_ID)
        }
        EasyHttp.get(BuildConfig.AD_NORMAL_CONFIG_URL.withConfigParams())
                .cacheMode(CacheMode.NO_CACHE)
                .execute(object : SimpleCallBack<NormalBean>() {
                    override fun onError(e: ApiException?) {
                        mNormalAdBean = getDefaultNormalBean()
                        mHandler?.run {
                            removeMessages(MESSAGE_ID)
                            sendEmptyMessageDelayed(MESSAGE_ID, 20 * 1000L)
                        }
                    }

                    override fun onSuccess(t: NormalBean?) {
                        t?.let {
                            checkNormalBean(it)
                            mmkv.encode(KEY_AD_NORMAL_CONFIG, it)
                        }
                        mNormalAdBean = t ?: getDefaultNormalBean()

                        mHandler?.run {
                            removeMessages(MESSAGE_ID)
                            sendEmptyMessageDelayed(MESSAGE_ID, mNormalAdBean.refreshInterval * 1000L)
                        }
                    }
                })
    }

    private fun checkNormalBean(t: NormalBean?) {
        if (t?.information == null) {
            t?.information = InfoBean()
        }
        if (t?.fullScreen == null) {
            t?.fullScreen = FullScreenBean()
        }
        if (t?.drawInformation == null) {
            t?.drawInformation = DrawInfoBean()
        }
        if (t?.banner == null) {
            t?.banner = BannerBean()
        }
    }

    private val mHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                MESSAGE_ID -> updateNormalAd()
            }
        }
    }

    private fun getDefaultRewardId(): RewardVideoId {
        val last = mmkv.decodeParcelable(KEY_AD_REWARD_ID_CONFIG, RewardVideoId::class.java)
        return last ?: RewardVideoId()
    }

    private fun getDefaultNormalBean(): NormalBean {
        val last = mmkv.decodeParcelable(KEY_AD_NORMAL_CONFIG, NormalBean::class.java)
        return last ?: NormalBean(true, "", 20, RewardVideoBean(true, RVLimitBean(), RVFailBean()),
                SplashBean(), InstlBean(), InstlFullBean(), InfoBean(), FullScreenBean(), BannerBean(), DrawInfoBean())
    }
}