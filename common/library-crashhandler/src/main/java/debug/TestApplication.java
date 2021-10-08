package debug;

import android.content.Context;
import android.util.Log;

import com.donews.base.base.BaseApplication;
import com.donews.common.AppGlobalConfigManager;
import com.donews.common.BuildConfig;
import com.donews.network.EasyHttp;
import com.donews.network.cache.converter.GsonDiskConverter;
import com.donews.network.cache.model.CacheMode;
import com.donews.network.cookie.CookieManger;
import com.donews.network.model.HttpHeaders;
import com.donews.utilslibrary.base.UtilsConfig;
import com.donews.utilslibrary.utils.AppInfo;
import com.donews.utilslibrary.utils.KeyConstant;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * 类描述
 *
 * @author Swei
 * @date 2021/4/7 17:30
 * @since v1.0
 */
public class TestApplication extends BaseApplication {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        Log.w("CrashCore","application attachBaseContext ,application="+this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.w("CrashCore","application onCreate");
        Log.w("CrashCore","application "+this.getApplicationContext());
        UtilsConfig.init(this);
        EasyHttp.init(this);
        if (this.issDebug()) {
            EasyHttp.getInstance().debug("HoneyLife", true);
        }
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.put(HttpHeaders.HEAD_AUTHORIZATION, AppInfo.getToken(""));

        EasyHttp.getInstance()
                .setBaseUrl("http://baobab.kaiyanapp.com")
                .setReadTimeOut(15 * 1000)
                .setWriteTimeOut(15 * 1000)
                .setConnectTimeout(15 * 1000)
                .setRetryCount(3)
                .setCookieStore(new CookieManger(this))
                .setCacheDiskConverter(new GsonDiskConverter())
                .setCacheMode(CacheMode.FIRSTREMOTE)
                .addCommonHeaders(httpHeaders);
        CrashReport.initCrashReport(getApplicationContext(), KeyConstant.getBuglyId(), BuildConfig.DEBUG);
        AppGlobalConfigManager.update();

    }
}
