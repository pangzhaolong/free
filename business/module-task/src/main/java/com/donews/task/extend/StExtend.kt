package com.donews.task.extend

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

/**
 * Created by ST on 2021/11/21.
 * describe: wake you
 */
inline fun <reified F : Fragment> Activity.newFragment(vararg args: Pair<String, String>): F{
    val bundle = Bundle()
    args.let {
        for (arg in it){
            bundle.putString(arg.first,arg.second)
        }
    }
    return F::class.java.newInstance().apply {
        this.arguments = bundle
    }
}

/**
 * 批量设置控件点击事件。
 *
 * @param v 点击的控件
 * @param block 处理点击事件回调代码块
 */
fun setOnClickListener(vararg v: View?, block: View.() -> Unit) {
    val listener = View.OnClickListener {
        setSingleClick(it,block)
    }
    v.forEach { it?.setOnClickListener(listener) }
}


/**
 * 批量设置控件点击事件。
 *
 * @param v 点击的控件
 * @param block 处理点击事件回调代码块
 */
fun setOnJustClickListener(vararg v: View?, block: View.() -> Unit) {
    val listener = View.OnClickListener {
        it.block()
    }
    v.forEach { it?.setOnClickListener(listener) }
}

/**
 * 批量设置控件点击事件。
 *
 * @param v 点击的控件
 * @param listener 处理点击事件监听器
 */
fun setOnClickListener(vararg v: View?, listener: View.OnClickListener) {
    v.forEach { it?.setOnClickListener(listener) }
}

fun setSingleClick(view: View?, block: View.() -> Unit){
    view?.let {
        if (it.stIsMultiClick()) return
        it.block()
    }
}

/*是否连点*/
var lastClickTime = 0L
var viewId = 0
var clickDelayTime: Long = 800 //连点间隔

fun View.stIsMultiClick(spanTime: Long = clickDelayTime): Boolean {
    val currentTime = System.currentTimeMillis()
    return if (viewId != this.id) {
        viewId = this.id
        lastClickTime = currentTime
        false
    }else{
        if (currentTime - lastClickTime > spanTime) {
            lastClickTime = currentTime
            false
        } else {
            true
        }
    }
}