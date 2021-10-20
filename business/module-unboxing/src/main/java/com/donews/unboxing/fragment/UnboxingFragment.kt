package com.donews.unboxing.fragment

import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.donews.base.fragment.MvvmLazyLiveDataFragment
import com.donews.common.router.RouterFragmentPath
import com.donews.unboxing.R
import com.donews.unboxing.adapter.UnboxingRVAdapter
import com.donews.unboxing.databinding.UnboxingFragUnboxingBinding
import com.donews.unboxing.viewmodel.UnboxingViewModel

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
        mDataBinding.rvUnboxing.adapter = unboxingRVAdapter
        mViewModel.unboxingLiveData.observe(viewLifecycleOwner, {
            unboxingRVAdapter.setNewData(it)
        })

    }

    override fun onFragmentFirstVisible() {
        super.onFragmentFirstVisible()
        mViewModel.getUnboxingData()
    }
}