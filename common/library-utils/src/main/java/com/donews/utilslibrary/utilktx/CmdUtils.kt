package com.donews.utilslibrary.utilktx

import java.io.DataOutputStream

/**
 *
 * cmd命令
 * @author XuShuai
 * @version v1.0
 * @date 2021/11/10 16:45
 */

/** 执行cmd命令 */
fun callCmd(cmd: String) {
    try {
        //权限设置
        val process = Runtime.getRuntime().exec("su")
        //获取输出流
        val outputStream = process.outputStream
        val dataOutputStream = DataOutputStream(outputStream)
        //将命令写入
        dataOutputStream.writeBytes(cmd)
        //提交命令
        dataOutputStream.flush();
        //关闭流操作
        dataOutputStream.close();
        outputStream.close();
    } catch (t: Throwable) {
        t.printStackTrace()
    }
}