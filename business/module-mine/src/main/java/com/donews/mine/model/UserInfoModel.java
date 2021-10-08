package com.donews.mine.model;

import androidx.lifecycle.MutableLiveData;

import com.dn.drouter.ARouteHelper;
import com.donews.base.model.BaseLiveDataModel;
import com.donews.common.contract.LoginHelp;
import com.donews.common.contract.UserInfoBean;
import com.donews.common.services.config.ServicesConfig;

public class UserInfoModel extends BaseLiveDataModel {




    public void bindWeChat() {
        //调用login微信绑定
        ARouteHelper.routeAccessServiceForResult(ServicesConfig.User.LONGING_SERVICE,
                "weChatBind", new Object[]{this});
    }
}
