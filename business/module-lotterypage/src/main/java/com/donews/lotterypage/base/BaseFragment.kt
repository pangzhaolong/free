package com.donews.lotterypage.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.donews.base.utils.ToastUtil


abstract class BaseFragment<V : ViewDataBinding> : Fragment() {

    abstract val layoutId: Int
    var mDataBinding: V? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ToastUtil.showShort(context,"asdfasdf")
        mDataBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        return mDataBinding?.root
    }



}


