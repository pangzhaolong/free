package com.module.integral.down

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.view.View
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.blankj.utilcode.util.FileUtils
import com.donews.base.base.BaseApplication
import com.donews.network.EasyHttp
import com.donews.network.cache.model.CacheMode
import com.donews.network.callback.DownloadProgressCallBack
import com.donews.network.exception.ApiException
import com.example.module_integral.R
import java.io.File
import java.util.*

/**
 * @author lcl
 * Date on 2021/12/27
 * Description:
 * apk下载任务
 */
class DownApkUtil {

    companion object {
        private val Tag = "taskDown"
        private var NOTIFY_ID_START = 9000

        /** 下载任务的,Key=包的下载路径 */
        private val downCallBackListeners =
            Collections.synchronizedList(mutableListOf<String>())

        /** 下载任务的,Key=包的下载路径，Value=下载的回调(此回调是跟上层业务生命周期绑定的) */
        private val downCallBackTemListeners =
            Collections.synchronizedMap(mutableMapOf<String, (DownApkCallBeanBean, Int) -> Unit>())

        /** 下载任务的通知栏对象集合 */
        private val downNotifyLMap =
            Collections.synchronizedMap(mutableMapOf<String, NotificationCompat.Builder>())

        /** 下载任务的通知栏对象id集合 */
        private val downNotifyIds =
            Collections.synchronizedMap(mutableMapOf<NotificationCompat.Builder, Int>())
    }

    //上一次通知的触发时间
    private var fastNotifyTime = 0L

    /**
     * 检查是否下载了apk
     * @param apkUrl String
     * @return Boolean
     */
    fun checkIsDownApk(
        apkUrl: String
    ): Boolean {
        val savePath = getSavePathDir(apkUrl)
        val saveFileName = getSaveFielName(apkUrl)
        val savePathFile = "${savePath}${File.separator}$saveFileName"
        return FileUtils.isFileExists(savePathFile) &&
                !downCallBackListeners.contains(apkUrl)
    }

    /**
     * 检查是否下载了apk
     * @param apkUrl String
     * @return Boolean
     */
    fun getDownApkFile(apkUrl: String): File? {
        if(checkIsDownApk(apkUrl)){
            val savePath = getSavePathDir(apkUrl)
            val saveFileName = getSaveFielName(apkUrl)
            val savePathFile = "${savePath}${File.separator}$saveFileName"
            val file = File(savePathFile)
            if(!file.exists()){
                return null
            }
            return file
        }
        return null
    }

    /**
     * 下载apk任务，注意:此方法只允许一个连接下关联一个UI组件。其余的将会被替换为最新的生效
     * @param apkName apk名称
     * @param apkUrl apk的URL地址
     * @param lifecy 生命周期管理对象(本监听需要和指定组件共享生命周期),如果为空表示全生命周期
     * @param callBack 回调 //String：apk的名称，String：apk的Url，Int:当前的进度(计算之后的进度)
     *  :Int < 0 表示出错了
     *  :Int = 0 (第一次等于0是表示任务开始，其余则是计算之后的进度值)
     *  :Int >= 0 更新下载进度
     */
    @Synchronized
    fun downApk(
        apkName: String,
        apkUrl: String,
        lifecy: Lifecycle?,
        callBack: ((DownApkCallBeanBean, Int) -> Unit)?
    ) {
        val savePath = getSavePathDir(apkUrl)
        val saveFileName = getSaveFielName(apkUrl)
        val savePathFile = "${savePath}${File.separator}$saveFileName"
        if (FileUtils.isFileExists(savePathFile) &&
            !downCallBackListeners.contains(apkUrl)
        ) {
            //已经存在文件。并且不是当前正在运行的任务
            downCallBackTemListeners.remove(apkUrl)
            callBack?.invoke(
                DownApkCallBeanBean(
                    apkName, apkUrl, savePathFile
                ), 100
            )
            return //文件已经存在,直接返回原文件。不在继续下载
        }
        //添加到临时本次任务监听中
        downCallBackTemListeners[apkUrl] = callBack
        //添加生命周期组件

        lifecy?.apply {
            this.addObserver(object : LifecycleObserver {
                @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
                fun onDestroy() {
                    downCallBackTemListeners.remove(apkUrl)
                }
            })
        }
        //判断是否存在已经进行的任务重
        if (downCallBackListeners.contains(apkUrl)) {
            return //已存在正在下载的任务。直接关联即可
        }
        //添加新的任务
        downCallBackListeners.add(apkUrl)
        val dlr = EasyHttp.downLoad(apkUrl)
        dlr.savePath(savePath)
            .saveName(saveFileName)
            .cacheMode(CacheMode.NO_CACHE)
            .execute(object : DownloadProgressCallBack<Any?>() {
                override fun onStart() {
                    downCallBackTemListeners[apkUrl]?.apply {
                        this.invoke(
                            DownApkCallBeanBean(
                                apkName, apkUrl, ""
                            ), 0
                        )
                    }
                }

                override fun update(bytesRead: Long, contentLength: Long, done: Boolean) {
                    downCallBackTemListeners[apkUrl]?.apply {
                        val process: Float = bytesRead / (contentLength * 1F) * 100
                        this.invoke(
                            DownApkCallBeanBean(
                                apkName, apkUrl, ""
                            ), process.toInt()
                        )
                    }
                }

                // 如果后台返回的data为空时走
                override fun onCompleteOk() {
                }

                override fun onComplete(path: String?) {
                    downCallBackTemListeners[apkUrl]?.apply {
                        this.invoke(
                            DownApkCallBeanBean(
                                apkName, apkUrl, path ?: ""
                            ), 100
                        )
                    }
                    downCallBackListeners.remove(apkUrl)
                }

                override fun onError(e: ApiException?) {
                    downCallBackTemListeners[apkUrl]?.apply {
                        this.invoke(
                            DownApkCallBeanBean(
                                apkName, apkUrl, ""
                            ), -1
                        )
                        downCallBackListeners.remove(apkUrl)
                    }
                }
            })
    }

    /**
     * 获取保存的文件路径
     * @return String
     */
    private fun getSavePathDir(apkUrl: String): String {
        return BaseApplication.getInstance().getExternalFilesDir(null)
            .toString() + File.separator + apkUrl.hashCode()
    }

    /**
     * 获取保存的文件名称
     * @return String
     */
    private fun getSaveFielName(apkUrl: String): String {
        return FileUtils.getFileName(apkUrl);
    }

    /**
     * 生成通知栏进度
     *  注意：次接口未做发送频率判断。请上层调用方自行判断
     * @param apkUrl apkUrl，对应唯一的一条通知
     * @param progress 进度
     * @param text 进度秒速的文字
     * @return Notification
     */
    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("RestrictedApi")
    fun createOrUpdateNotification(
        apkUrl: String,
        progress: Int,
        text: String
    ) { //自定义View通知
        val notificationManagerCompat =
            NotificationManagerCompat.from(BaseApplication.getInstance())
        if (downNotifyLMap[apkUrl] == null) {
            downNotifyLMap[apkUrl] = NotificationCompat.Builder(BaseApplication.getInstance(), Tag)
            downNotifyIds[downNotifyLMap[apkUrl]] = NOTIFY_ID_START++
        }
        val view = RemoteViews(
            BaseApplication.getInstance().packageName,
            R.layout.notification_upgrade
        )
        downNotifyLMap[apkUrl]!!
            .setCustomContentView(view)
            .setSmallIcon(R.mipmap.ic_launcher)
//            .setOngoing(progress < 100)
            .setOngoing(false)

        //设置值
        view.setProgressBar(R.id.bar, 100, progress, false)
        view.setTextViewText(R.id.tv_des, text)
        view.setTextViewText(
            R.id.tv_progress,
            java.lang.String.format(Locale.getDefault(), "%d%%", progress)
        )
        if (progress >= 100) {
            view.setViewVisibility(R.id.bar, View.INVISIBLE)
            view.setTextViewText(R.id.tv_des, "下载完成")
        }
        //            view.setImageViewIcon(R.id.icon,)

        //显示相关设置
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //通知管理器设置渠道
            val channel = NotificationChannel(
                BaseApplication.getInstance().packageName,
                Tag,
                NotificationManager.IMPORTANCE_LOW
            )
            notificationManagerCompat.createNotificationChannel(channel)
            //设置渠道
        }
        ///< 仅仅响一次
        downNotifyLMap[apkUrl]!!
            .setOnlyAlertOnce(true)
        //不设置不显示通知
        downNotifyLMap[apkUrl]!!.setChannelId(
            BaseApplication.getInstance().packageName
        )
        if (progress >= 100) {
            notificationManagerCompat.notify(
                Tag,
                downNotifyIds[downNotifyLMap[apkUrl]]!!,
                downNotifyLMap[apkUrl]!!.build()
            )
            downNotifyIds.remove(downNotifyLMap[apkUrl])
        } else {
            if (System.currentTimeMillis() - fastNotifyTime > 500) {
                //通知触发必须小于500毫秒
                fastNotifyTime = System.currentTimeMillis()
                notificationManagerCompat.notify(
                    Tag,
                    downNotifyIds[downNotifyLMap[apkUrl]]!!,
                    downNotifyLMap[apkUrl]!!.build()
                )
            }
        }
    }
}