package com.donews.middle

import androidx.fragment.app.Fragment

/**
 * @author lcl
 * Date on 2022/5/24
 * Description:
 *  首页和下级Fragmnet通讯接口
 */
interface IMainParams {
    /**
     * 获取当前Tab的Fargment 再首页的Tab下标
     * @param f
     * @return 当前Fragment所在的下标,>=0:实际的下标 ， <0:未找到
     */
    fun getThisFragmentCurrentPos(f: Fragment): Int
}