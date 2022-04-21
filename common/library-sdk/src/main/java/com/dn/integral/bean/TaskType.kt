package com.dn.integral.bean

/**
 * 积分墙任务类型
 *
 * @author: cymbi
 * @data: 2021/12/27
 */
enum class TaskType {
    //未知任务
    UNKNOWN_TASK,

    //激活任务（表示可以下载）
    ACTIVATION_TASK,

    //次留任务（表示可以试玩）
    RETENTION_TASK,
}