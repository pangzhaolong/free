package com.module.lottery.ui;

import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;

import com.module.lottery.viewModel.ContactCustomerViewModel;
import com.module_lottery.R;
import com.module_lottery.databinding.ContactCustomerLayoutBinding;

import java.util.Map;

public class ContactCustomerActivity extends BaseActivity<ContactCustomerLayoutBinding, ContactCustomerViewModel> {
    @Override
    protected int getLayoutId() {
        return R.layout.contact_customer_layout;
    }

    @Override
    public void initView() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(mDataBinding.aboutToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //添加默认的返回图标
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true); //设置返回键可用
//            actionBar.setDisplayShowTitleEnabled(false);
        }
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

        setObserveList();
        requestContactCustomerData();
    }


    public void setObserveList() {

        mViewModel.getMutableLiveData().observe(this, ContactCustomerBean -> {

        });
    }


    public void requestContactCustomerData() {

        Map<String, String> params = BaseParams.getMap();
//        params.put("goods_id", id);
        mViewModel.getContactCustomerData("", params);


    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }


        return true;
    }

}
