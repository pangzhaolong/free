package com.donews.main.utils.down

/**
 * @author lcl
 * Date on 2021/12/27
 * Description:
 * 回调的相关参数
 */
class DownApkCallBeanBean(
    /** apk的名称 */
    val apkName: String,
    /** apk下载地址 */
    val apkUrl: String,
    /** apk的本地报错路径 */
    val savePath: String
)