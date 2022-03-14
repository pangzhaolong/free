package debug;

import com.alibaba.android.arouter.launcher.ARouter;
import com.donews.web.BuildConfig;
import com.donews.base.base.BaseApplication;
import com.donews.network.EasyHttp;
import com.donews.network.interceptor.HttpExprInterceptor;
import com.donews.web.base.WebConfig;

/**
 * <p> </p>
 * 作者： created by honeylife<br>
 * 日期： 2020/11/11 16:46<br>
 * 版本：V1.0<br>
 */
public class WebApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(this); // 尽可能早，推荐在Application中初始化

        EasyHttp.init(this);
        EasyHttp.getInstance().debug("LOGIN")
        .addInterceptor(new HttpExprInterceptor());
        WebConfig.init(this);
    }
}
