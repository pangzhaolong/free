package com.donews.unboxing.fragment

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import com.alibaba.android.arouter.facade.annotation.Route
import com.chad.library.adapter.base.diff.BrvahAsyncDifferConfig
import com.donews.base.fragment.MvvmLazyLiveDataFragment
import com.donews.common.router.RouterFragmentPath
import com.donews.unboxing.R
import com.donews.unboxing.adapter.UnboxingRVAdapter
import com.donews.unboxing.bean.UnboxingBean
import com.donews.unboxing.databinding.UnboxingFragUnboxingBinding
import com.donews.unboxing.smartrefreshlayout.SmartRefreshState
import com.donews.unboxing.viewmodel.UnboxingViewModel
import com.donews.utilslibrary.utils.AppInfo
import com.donews.utilslibrary.utils.DeviceUtils
import com.orhanobut.logger.Logger
import com.tencent.mmkv.MMKV

/**
 * 晒单页 fragment
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/20 17:43
 */
@Route(path = RouterFragmentPath.Unboxing.PAGER_UNBOXING_FRAGMENT)
class UnboxingFragment : MvvmLazyLiveDataFragment<UnboxingFragUnboxingBinding, UnboxingViewModel>() {

    private val unboxingRVAdapter = UnboxingRVAdapter(R.layout.unboxing_item_unboxing)

    override fun getLayoutId(): Int {
        return R.layout.unboxing_frag_unboxing
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mDataBinding.viewModel = mViewModel

        unboxingRVAdapter.setDiffCallback(object : DiffUtil.ItemCallback<UnboxingBean>() {
            override fun areItemsTheSame(oldItem: UnboxingBean, newItem: UnboxingBean): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: UnboxingBean, newItem: UnboxingBean): Boolean {
                return oldItem.id == newItem.id
            }

        })
        mDataBinding.rvUnboxing.adapter = unboxingRVAdapter
        mViewModel.run {
            listData.observe(this@UnboxingFragment.viewLifecycleOwner, {
                val data = it as MutableList<UnboxingBean>?
                unboxingRVAdapter.setDiffNewData(data)
            })
        }
    }

    override fun onFragmentFirstVisible() {
        super.onFragmentFirstVisible()
        //加载数据
        mViewModel.refreshing.value = SmartRefreshState(true)
    }
}