package com.dn.sdk.bean.natives

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import com.dn.sdk.bean.AdRequest
import com.dn.sdk.listener.IAdNativeListener
import com.dn.sdk.loader.SdkType

/**
 * 信息流广告数据接口,暂时只支持多牛sdk
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/12/3 14:31
 */
interface INativeAdData {

    fun getAdRequest(): AdRequest

    /** 数据获取来源平台 */
    fun getSdkType(): SdkType

    /** 广告来自哪个渠道 */
    fun getAdFrom(): Int

    /** 是否是app */
    fun isAPP(): Boolean

    /** 自渲染的信息流的广告描述*/
    fun getDesc(): String?

    /** 自渲染的信息流的标题title，快手渠道没有标题，所以需要app自己判断是否为空，为空自己填充内容，或者用desc填充*/
    fun getTitle(): String?

    /** 自渲染的信息流的大图的背景图片*/
    fun getImgUrl(): String?

    /** 信息流自渲染中广告ICON的图标*/
    fun getIconUrl(): String?

    /** 自渲染的信息流的各个渠道的广告标识 logo,必需要在app的控件中展示出来*/
    fun getLogoUrl(): String?

    /**
     * 广告类型
     * @return Int 1大图 2三小图 3视频
     */
    fun getAdPatternType(): Int

    /** 视频长度 */
    fun getVideoDuration(): Int

    /** 自渲染的信息流的小图的背景图片，有可能为null或者list.size为0，需要app自己判断一下，然后在取值*/
    fun getImgList(): List<String>?

    /** onResume声明周期调用 */
    fun resume()

    /** destroy*/
    fun destroy()

    /**
     * 用户 广告sdk自渲染图片,只有优量汇sdk支持，getAdFrom() ==5 表示 优量汇
     * @param list List<ImageView?>?
     * @param var2 Int 传入0
     */
    fun bindImageViews(list: MutableList<ImageView>?, var2: Int)

    /**
     * 此方法必须在调用，否则会影响曝光点击，详细请看demo,有不明白的地方及时联系我方技术
     *
     * @param context Context
     * @param viewGroup ViewGroup  当前渲染控件的父布局.此ViewGroup外面必须包裹一层父布局，且此父布局中不允许有第二个子布局存在，有疑问请联系我方技术
     * @param frameLayout FrameLayout 如果不接入视频类，请设置为null,如果接入视频类请传入视频的
     * @param clickViews List<View>? 需要点击曝光的渲染控件
     * @param listener NativeAdListener? 回调监听
     */
    fun bindView(
        context: Context,
        viewGroup: ViewGroup,
        frameLayout: FrameLayout,
        clickViews: List<View>?,
        listener: IAdNativeListener?
    )
}