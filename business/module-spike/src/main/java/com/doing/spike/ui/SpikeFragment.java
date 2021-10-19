/**
 * <p> </p>
 * 作者： created by hegai<br>
 * 日期： 2021/10/14 15:43<br>
 * 版本：V1.0<br>
 */


package com.doing.spike.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.doing.spike.R;
import com.doing.spike.adapter.SpikeContextAdapter;
import com.doing.spike.adapter.SpikeTimeAdapter;
import com.doing.spike.bean.SpikeBean;
import com.doing.spike.databinding.SpikeFramentBinding;
import com.doing.spike.util.CenterLayoutManager;
import com.doing.spike.viewModel.SpikeViewModel;
import com.donews.base.fragment.MvvmLazyLiveDataFragment;
import com.donews.common.router.RouterActivityPath;
import com.donews.common.router.RouterFragmentPath;

@Route(path = RouterFragmentPath.Spike.PAGER_SPIKE)
public class SpikeFragment extends MvvmLazyLiveDataFragment<SpikeFramentBinding, SpikeViewModel> {
    @Override
    public int getLayoutId() {
        return R.layout.spike_frament;
    }
    private SpikeTimeAdapter mSpikeAdapter;
    private SpikeContextAdapter mSpikeContextAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 处理正常的逻辑。
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        CenterLayoutManager centerLayoutManager = new CenterLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        mSpikeAdapter = new SpikeTimeAdapter(this.getContext(), centerLayoutManager, mDataBinding.spikeTimeScroll);
        mSpikeAdapter.getLayout(R.layout.spike_time_item);

        mSpikeAdapter.setClickListener(new SpikeTimeAdapter.IClickCallbackListener() {
            @Override
            public void onClick(SpikeBean.RoundsListDTO roundsListDTO) {
                mViewModel.getNetHomeData(roundsListDTO.getDdqTime(), roundsListDTO);
            }
        });

        mSpikeContextAdapter = new SpikeContextAdapter(this.getContext());

        mSpikeContextAdapter.getLayout(R.layout.spike_context_item);
        //设置时间的滑动
        mDataBinding.spikeTimeScroll.setAdapter(mSpikeAdapter);
        mDataBinding.spikeTimeScroll.setLayoutManager(centerLayoutManager);
        //设置商品内容
        mDataBinding.spikeContentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mDataBinding.spikeContentRecyclerView.setAdapter(mSpikeContextAdapter);

        mSpikeContextAdapter.setOnItemClickListener(new SpikeContextAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(SpikeBean.GoodsListDTO goodsListDTO) {
                if (goodsListDTO != null) {
                    ARouter.getInstance().build(RouterActivityPath.GoodsDetail.GOODS_DETAIL)
                            .withString("params_id", goodsListDTO.getId())
                            .withString("params_goods_id", goodsListDTO.getGoodsId())
                            .navigation();
                }

            }
        });


    }


    @SuppressLint("FragmentLiveDataObserve")
    private void initContent() {
        // 获取网路数据
        mViewModel.getNetHomeData("", null).observe(SpikeFragment.this, combinationSpikeBean -> {
            // 获取数据
            if (combinationSpikeBean == null) {
                return;
            }
            if (mSpikeAdapter.getSpikeBeans() == null) {
                mSpikeAdapter.setSpikeBeans(combinationSpikeBean.getSpikeBean());
                mSpikeAdapter.notifyDataSetChanged();
            }
            mSpikeContextAdapter.setCombinationSpikeBean(combinationSpikeBean);
            mSpikeContextAdapter.notifyDataSetChanged();

        });
    }


    @Override
    protected void onFragmentFirstVisible() {
        super.onFragmentFirstVisible();
        initContent();
    }


    @Override
    public void onResume() {
        super.onResume();

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
