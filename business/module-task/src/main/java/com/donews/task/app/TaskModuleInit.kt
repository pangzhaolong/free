package com.donews.task.app

import com.donews.base.base.BaseApplication
import com.donews.common.IModuleInit
import com.donews.task.util.TaskControlUtil
import com.donews.task.util.TaskImgControlUtil

/**
 *  make in st
 *  on 2022/5/11 09:44
 */
class TaskModuleInit:IModuleInit {

    override fun onInitAhead(application: BaseApplication?): Boolean {
        TaskImgControlUtil.init()
        TaskControlUtil.init()
        return false
    }

    override fun onInitLow(application: BaseApplication?): Boolean {
        return false
    }

}