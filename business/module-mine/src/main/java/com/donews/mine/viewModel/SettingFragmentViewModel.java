package com.donews.mine.viewModel;

import android.content.Context;

import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;

import com.donews.base.utils.ToastUtil;
import com.donews.base.viewmodel.BaseLiveDataViewModel;
import com.donews.mine.databinding.MineSettingFragmentBinding;
import com.donews.mine.model.SettingModel;
import com.donews.utilslibrary.utils.DeviceUtils;

import java.util.ArrayList;
import java.util.List;

public class SettingFragmentViewModel extends BaseLiveDataViewModel<SettingModel> {
    private ViewDataBinding viewDataBinding;
    public LifecycleOwner lifecycleOwner;
    private FragmentActivity baseActivity;

    private List<String> itemTitles = new ArrayList() {
        {
            add("绑定手机");
            add("分析App");
            add("用户协议");
            add("隐私政策");
            add("关于我们");
            add("清除缓存");
            add("注销账号");
        }
    };

    public void setDataBinDing(MineSettingFragmentBinding dataBinding, FragmentActivity baseActivity) {
        this.viewDataBinding = dataBinding;
        this.baseActivity = baseActivity;
        dataBinding.setListener(this);
    }

    @Override
    public SettingModel createModel() {
        return new SettingModel();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    /**
     * 获取列表的数据
     *
     * @param pos 第几个菜单的标题:
     * @return
     */
    public String getItemTitleName(int pos) {
        try {
            return itemTitles.get(pos);
        }catch (Exception e){
            return "--";
        }
    }


//
//    @Override
//    public void onLoadFinish(BaseModel model, BaseCustomViewModel data) {
//        if (data instanceof CacheBean) {
//            viewDataBinding.setVariable(BR.cacheBean, data);
//        } else if (data instanceof SignBean) {
//            signBean = (SignBean) data;
//            viewDataBinding.setVariable(BR.signBean, data);
//        } else if (data instanceof ApplyUpdataBean) {
//            updateLogic((ApplyUpdataBean) data);
//        }
//    }

    public void clearCache() {
        mModel.clearCache(baseActivity);
    }

    public void getCacheData() {
        mModel.getCacheData(baseActivity);
    }


}
