package com.donews.task

import com.donews.common.base.MvvmLazyLiveDataFragment
import com.donews.task.databinding.TaskFragmentBinding
import com.donews.task.vm.TaskViewModel

/**
 *  make in st
 *  on 2022/5/7 10:37
 */
class TaskFragment: MvvmLazyLiveDataFragment<TaskFragmentBinding,TaskViewModel>() {

    override fun getLayoutId() = R.layout.task_fragment

}