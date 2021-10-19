package com.donews.home.application;

import com.donews.base.base.BaseApplication;
import com.donews.common.IModuleInit;
import com.donews.home.cache.GoodsCache;

/**
 * 应用模块: main
 * <p>
 * 类描述: main组件的业务初始化
 * <p>
 *
 * @author darryrzhoong
 * @since 2020-02-26
 */
public class HomeModuleInit implements IModuleInit {


    @Override
    public boolean onInitAhead(BaseApplication application) {
        GoodsCache.init(application);
        return false;
    }

    @Override
    public boolean onInitLow(BaseApplication application) {
        return false;
    }
}
