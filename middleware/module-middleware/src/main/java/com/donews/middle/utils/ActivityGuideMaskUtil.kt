package com.donews.middle.utils

import android.app.Activity
import android.graphics.*
import android.view.View
import android.view.ViewParent
import android.widget.Toast
import com.app.hubert.guide.NewbieGuide
import com.app.hubert.guide.core.Controller
import com.app.hubert.guide.listener.OnLayoutInflatedListener
import com.app.hubert.guide.model.GuidePage
import com.app.hubert.guide.model.HighLight
import com.app.hubert.guide.model.HighlightOptions
import com.blankj.utilcode.util.SPUtils

/**
 * @author lcl
 * Date on 2022/5/26
 * Description:
 * Activity的高亮引导工具
 */
object ActivityGuideMaskUtil {

    /**
     * 显示一个引导
     * @param act 页面对象
     * @param maskLayoutResId 遮罩层上面显示的视图
     * @param tagertViewId 需要高亮显示的视图对象的id(注：必须是activity的findById能够找到的)
     * @param clickListener 当高亮区域点击的回调
     * @param layoutInfatedListener 视图加载时候的监听
     */
    fun showGuide(
        act: Activity,
        maskLayoutResId: Int,
        tagertViewId: Int,
        click: (Controller) -> Unit,
        layoutInfatedListener: OnLayoutInflatedListener? = null
    ) {
        var controller: Controller? = null
        val options = HighlightOptions.Builder()
            .setOnClickListener {
                click.invoke(controller!!)
            }
            .setOnHighlightDrewListener { canvas: Canvas, rectF: RectF ->
                //给高亮的地方绘制虚线
                val paint = Paint()
                paint.color = Color.WHITE
                paint.style = Paint.Style.STROKE
                paint.strokeWidth = 3f
                paint.pathEffect = DashPathEffect(floatArrayOf(20f, 20f), 0F)
                canvas.drawCircle(rectF.centerX(), rectF.centerY(), rectF.width() / 2 + 10, paint)
            }
            .build()
        val tagView: View = act.findViewById(tagertViewId)
        val page = GuidePage.newInstance()
            .setEverywhereCancelable(true) //是否点击任意位置消失引导页，默认true
//            .addHighLightWithOptions(tagView, options) //矩形高亮区域
            .addHighLightWithOptions(tagView, HighLight.Shape.CIRCLE, options) //圆形高亮区域
            .setLayoutRes(maskLayoutResId)
        layoutInfatedListener?.apply {
            page.onLayoutInflatedListener = this
        }
        controller = NewbieGuide.with(act)
            .setLabel("relative")
            .alwaysShow(true) //总是显示，调试时可以打开
            .addGuidePage(page)
            .show()
        //保存为已显示
        saveGuideShowRecord(act, tagertViewId, true)
    }

    /**
     * 获取指定View再屏幕中的区域信息。
     * @param v 视图
     * @param r 获取存储的区域信息
     */
    fun getRectInScreen(v: View, r: RectF) {
        var v = v
        val w = v.width
        val h = v.height
        r.left = v.left.toFloat()
        r.top = v.top.toFloat()
        r.right = r.left + w
        r.bottom = r.top + h
        var p = v.parent
        while (p is View) {
            v = p
            p = v.parent
            r.left += v.left.toFloat()
            r.top += v.top.toFloat()
            r.right = r.left + w
            r.bottom = r.top + h
        }
    }

    /**
     * 是否已经显示过
     * @param act 页面或者引导依附的对象实体
     * @param targetViewId 目标的视图ID
     * @return T:已经显示过，F:未显示过
     */
    fun getGuideShowRecord(act: Any, targetViewId: Int): Boolean {
        return SPUtils.getInstance("guideFile")
            .getBoolean("${act.javaClass.name}#${targetViewId}", false)
    }

    /**
     * 更新引导为已显示状态
     * @param act 页面或者引导依附的对象实体
     * @param targetViewId 目标的视图ID
     * @param value 需要保存的值
     * @return Boolean
     */
    fun saveGuideShowRecord(act: Any, targetViewId: Int, value: Boolean) {
        SPUtils.getInstance("guideFile")
            .put("${act.javaClass.name}#${targetViewId}", value)
    }
}