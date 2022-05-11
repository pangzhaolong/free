package com.donews.task.app

import com.donews.base.base.BaseApplication
import com.donews.common.IModuleInit
import com.donews.task.util.TaskControlUtil

/**
 *  make in st
 *  on 2022/5/11 09:44
 */
class TaskModuleInit:IModuleInit {

    override fun onInitAhead(application: BaseApplication?): Boolean {
        TaskControlUtil.init()
        return false
    }

    override fun onInitLow(application: BaseApplication?): Boolean {
        return false
    }

}