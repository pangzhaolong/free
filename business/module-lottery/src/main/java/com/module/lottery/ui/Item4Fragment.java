package com.module.lottery.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import com.donews.base.fragment.MvvmLazyLiveDataFragment;
import com.module.lottery.bean.RaidersBean;
import com.module.lottery.model.LotteryModel;
import com.module.lottery.viewModel.Item1FragmentViewModel;
import com.module_lottery.R;
import com.module_lottery.databinding.Item1FragmentBinding;

import java.util.Map;

public class Item4Fragment extends MvvmLazyLiveDataFragment<Item1FragmentBinding, Item1FragmentViewModel> {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setObserveList();
        requestRaidersUrl();
    }

    //注册数据变化观察者

    private  void setObserveList(){
        mViewModel.getMutableLiveData().observe(Item4Fragment.this.getActivity(), RaidersBean->{
            mDataBinding.webView.loadUrl("http://www.baidu.com");
            mDataBinding.webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    //使用WebView加载显示url
                    view.loadUrl(url);
                    //返回true
                    return true;
                }
            });
        });
    }

    String id = "tb:655412572200";

    //请求攻略数据
    private  void requestRaidersUrl(){
        Map<String, String> params = BaseParams.getMap();
        params.put("goods_id", id);
        mViewModel.getData(LotteryModel.LOTTERY_PARTICIPATE_NUM,params);

    }

    @Override
    public int getLayoutId() {
        return R.layout.item_1_fragment;
    }
}
