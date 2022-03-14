package debug;

import com.donews.base.base.BaseApplication;
import com.donews.network.EasyHttp;
import com.donews.network.interceptor.HttpExprInterceptor;

/**
 * <p> </p>
 * 作者： created by honeylife<br>
 * 日期： 2020/11/11 16:46<br>
 * 版本：V1.0<br>
 */
public class LoginApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        EasyHttp.init(this);
        EasyHttp.getInstance().debug("LOGIN")
        .addInterceptor(new HttpExprInterceptor());
//        AnalysisHelp.init(this);
    }
}
