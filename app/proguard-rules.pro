# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile


-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
-keep public class * extends android.view.View

-keep public class * extends androidx.fragment.app.Fragment
-keep public class * extends androidx.appcompat.app.AppCompatActivity
-keep public class * extends androidx.lifecycle.ViewModel
-keep class * extends androidx.databinding.BaseObservable{*;}
-keep class * extends com.donews.base.viewmodel.MvmBaseViewModel{*;}
-keep class * extends com.donews.base.viewmodel.BaseLiveDataViewModel{*;}
-keep class * extends com.donews.common.base.MvvmBaseLiveDataActivity{*;}
-keep class * extends com.donews.common.contract.BaseCustomViewModel{*;}
-keep class * implements com.donews.common.IModuleInit{*;}

-keep class **Listener
-keep public class * extends com.platform.dai.webview.CommonJSInterface{ *;}

#---------SdkLogUtils-----------begin-----------
-keep class com.dn.sdk.sdk.utils.SdkLogUtils
#---------SdkLogUtils-----------end-------------


#---------[多牛model]其他第三方混淆排除--------------------
#保持多牛的sdk模块中代码不混淆，保持原样
-keep class cn.cd.dn.sdk.** { *; }
-dontwarn cn.cd.dn.sdk.**

#多牛model的aop模块代码不混淆，保持原样
-keep class com.dncd.lib.aop.** { *; }
-dontwarn com.dncd.lib.aop.**

#多牛model的apt模块代码不混淆，保持原样
-keep class com.dncd.apt.** { *; }
-dontwarn com.dncd.apt.**

#用到的第三方库排除。不混淆(包装的hook模块)
-keep class com.chat_hook.** { *; }
-dontwarn com.chat_hook.**
#内部的封装hook核心代码模块
-keep class com.swift.sandhook.** { *; }
-dontwarn com.swift.sandhook.**
#---------[多牛model]其他第三方排除结束--------------------
#EventBus 过滤
-keep class org.greenrobot.** { *; }
-dontwarn org.greenrobot.**
#EventBus的Lib过滤
-keep class com.dn.events.** { *; }
-dontwarn com.dn.events.**

#webview
-keep class com.donews.web.javascript.** { *; }


#androidx 配置
-keep class com.google.android.material.** {*;}
-keep class androidx.** {*;}
-keep public class * extends androidx.**
-keep interface androidx.** {*;}
-dontwarn com.google.android.material.**
-dontnote com.google.android.material.**
-dontwarn androidx.**

-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String,int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
   public static int e(...);
}

-assumenosideeffects class java.io.PrintStream {
    public *** println(...);
    public *** print(...);
}

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}

-keep public class com.daily.walk.R$*{
    public static final int *;
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

-keepclassmembers class * {
    void *(*Event);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keep class * implements java.io.Serializable { *;}

-dontwarn org.apache.**
-keep class org.apache.** { *; }
-keep public class android.net.http.SslError
-keep public class android.webkit.WebViewClient

-dontwarn android.webkit.WebView
-dontwarn android.net.http.SslError
-dontwarn android.webkit.WebViewClient

-dontwarn android.support.**
-keep class android.support.** { *; }

#okhttp
-dontwarn com.squareup.okhttp.**
-dontwarn okio.**
-dontwarn java.nio.file.**
-dontwarn org.codehaus.mojo.**

#微信
-dontwarn com.tencent.mm.opensdk.**
-keep class com.tencent.mm.opensdk.** {*;}
-keep class com.tencent.wxop.** {*;}
-keep class com.tencent.mm.sdk.** {*;}
-dontwarn com.tencent.**
-keep class com.tencent.** {*;}



#状态栏库
-dontwarn cn.ycbjie.ycstatusbarlib.**
-keep class cn.ycbjie.ycstatusbarlib.**{*;}



#友盟统计
-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}


-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep public class **.R$*{
public static final int *;
}

#bugly
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}
-keep class android.support.**{*;}




-keep class com.android.id.impl.IdProviderImpl



# -----------不混淆的类文件 gson--start--------

-keep class com.google.gson.stream.** { *; }
-keep class com.google.gson.** { *; }
-keepattributes EnclosingMethod
-keepattributes Signature
-keepattributes *Annotation*

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
-keep class com.dn.sdk.bean.** { *; }


#------------- GSON end----------------------


#---------------------Glide 混淆文件  start------------------------
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class * extends com.bumptech.glide.module.AppGlideModule {
 <init>(...);
}
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-keep class com.bumptech.glide.load.data.ParcelFileDescriptorRewinder$InternalRewinder {
  *** rewind();
}
-dontwarn com.bumptech.glide.load.resource.bitmap.VideoDecoder
#---------------------Glide 混淆文件  end -------------------------

-dontwarn dalvik.**
-dontwarn com.tencent.smtt.**
# ------------------ Keep LineNumbers and properties ---------------- #
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod
# --------------------------------------------------------------------------

# Addidional for x5.sdk classes for apps

-keep class com.tencent.smtt.export.external.**{
    *;
}

-keep class com.tencent.tbs.video.interfaces.IUserStateChangedListener {
	*;
}

-keep class com.tencent.smtt.sdk.CacheManager {
	public *;
}

-keep class com.tencent.smtt.sdk.CookieManager {
	public *;
}

-keep class com.tencent.smtt.sdk.WebHistoryItem {
	public *;
}

-keep class com.tencent.smtt.sdk.WebViewDatabase {
	public *;
}

-keep class com.tencent.smtt.sdk.WebBackForwardList {
	public *;
}

-keep public class com.tencent.smtt.sdk.WebView {
	public <fields>;
	public <methods>;
}

-keep public class com.tencent.smtt.sdk.WebView$HitTestResult {
	public static final <fields>;
	public java.lang.String getExtra();
	public int getType();
}

-keep public class com.tencent.smtt.sdk.WebView$WebViewTransport {
	public <methods>;
}

-keep public class com.tencent.smtt.sdk.WebView$PictureListener {
	public <fields>;
	public <methods>;
}


-keepattributes InnerClasses

-keep public enum com.tencent.smtt.sdk.WebSettings$** {
    *;
}

-keep public enum com.tencent.smtt.sdk.QbSdk$** {
    *;
}

-keep public class com.tencent.smtt.sdk.WebSettings {
    public *;
}


-keepattributes Signature
-keep public class com.tencent.smtt.sdk.ValueCallback {
	public <fields>;
	public <methods>;
}

-keep public class com.tencent.smtt.sdk.WebViewClient {
	public <fields>;
	public <methods>;
}

-keep public class com.tencent.smtt.sdk.DownloadListener {
	public <fields>;
	public <methods>;
}

-keep public class com.tencent.smtt.sdk.WebChromeClient {
	public <fields>;
	public <methods>;
}

-keep public class com.tencent.smtt.sdk.WebChromeClient$FileChooserParams {
	public <fields>;
	public <methods>;
}

-keep class com.tencent.smtt.sdk.SystemWebChromeClient{
	public *;
}
# 1. extension interfaces should be apparent
-keep public class com.tencent.smtt.export.external.extension.interfaces.* {
	public protected *;
}

# 2. interfaces should be apparent
-keep public class com.tencent.smtt.export.external.interfaces.* {
	public protected *;
}

-keep public class com.tencent.smtt.sdk.WebViewCallbackClient {
	public protected *;
}

-keep public class com.tencent.smtt.sdk.WebStorage$QuotaUpdater {
	public <fields>;
	public <methods>;
}

-keep public class com.tencent.smtt.sdk.WebIconDatabase {
	public <fields>;
	public <methods>;
}

-keep public class com.tencent.smtt.sdk.WebStorage {
	public <fields>;
	public <methods>;
}

-keep public class com.tencent.smtt.sdk.DownloadListener {
	public <fields>;
	public <methods>;
}

-keep public class com.tencent.smtt.sdk.QbSdk {
	public <fields>;
	public <methods>;
}

-keep public class com.tencent.smtt.sdk.QbSdk$PreInitCallback {
	public <fields>;
	public <methods>;
}
-keep public class com.tencent.smtt.sdk.CookieSyncManager {
	public <fields>;
	public <methods>;
}

-keep public class com.tencent.smtt.sdk.Tbs* {
	public <fields>;
	public <methods>;
}

-keep public class com.tencent.smtt.utils.LogFileUtils {
	public <fields>;
	public <methods>;
}

-keep public class com.tencent.smtt.utils.TbsLog {
	public <fields>;
	public <methods>;
}

-keep public class com.tencent.smtt.utils.TbsLogClient {
	public <fields>;
	public <methods>;
}

-keep public class com.tencent.smtt.sdk.CookieSyncManager {
	public <fields>;
	public <methods>;
}

# Added for game demos
-keep public class com.tencent.smtt.sdk.TBSGamePlayer {
	public <fields>;
	public <methods>;
}

-keep public class com.tencent.smtt.sdk.TBSGamePlayerClient* {
	public <fields>;
	public <methods>;
}

-keep public class com.tencent.smtt.sdk.TBSGamePlayerClientExtension {
	public <fields>;
	public <methods>;
}

-keep public class com.tencent.smtt.sdk.TBSGamePlayerService* {
	public <fields>;
	public <methods>;
}

-keep public class com.tencent.smtt.utils.Apn {
	public <fields>;
	public <methods>;
}
-keep class com.tencent.smtt.** {
	*;
}
# end


-keep public class com.tencent.smtt.export.external.extension.proxy.ProxyWebViewClientExtension {
	public <fields>;
	public <methods>;
}

-keep class MTT.ThirdAppInfoNew {
	*;
}

-keep class com.tencent.mtt.MttTraceEvent {
	*;
}

# Game related
-keep public class com.tencent.smtt.gamesdk.* {
	public protected *;
}

-keep public class com.tencent.smtt.sdk.TBSGameBooter {
        public <fields>;
        public <methods>;
}

-keep public class com.tencent.smtt.sdk.TBSGameBaseActivity {
	public protected *;
}

-keep public class com.tencent.smtt.sdk.TBSGameBaseActivityProxy {
	public protected *;
}

-keep public class com.tencent.smtt.gamesdk.internal.TBSGameServiceClient {
	public *;
}
#---------------------------------------------------------------------------


#----------------------oaid start----------------------------
-keep, includedescriptorclasses class com.asus.msa.SupplementaryDID.** { *; }
-keepclasseswithmembernames class com.asus.msa.SupplementaryDID.**{ *; }
-keep, includedescriptorclasses class com.asus.msa.sdid.** { *; }
-keepclasseswithmembernames class com.asus.msa.sdid.** { *; }
-keep public class com.netease.nis.sdkwrapper.Utils {public <methods>;}
-keep class com.bun.miitmdid.**{*;}
-keep class com.bun.lib.**{*;}
-keep class com.samsung.android.deviceidservice.**{*;}
-keep class a.**{*;}

#----------------------oaid end -----------------------------


#----------------------大数据统计----------------------------
#-dontwarn com.donews.donewssdk.**
#-keep class com.donews.donewssdk.** { *; }
#----------------------大数据统计 -----------------------------

#-----------------umeng start----------------------------
-keep class com.umeng.** {*;}

#//您如果使用了稳定性模块可以加入该混淆
-keep class com.uc.** {*;}

-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

#//SDK 9.2.4及以上版本自带oaid采集模块，不再需要开发者再手动引用oaid库，所以可以不添加这些混淆
-keep class com.zui.** {*;}
-keep class com.miui.** {*;}
-keep class com.heytap.** {*;}
-keep class a.** {*;}
-keep class com.vivo.** {*;}

#-----------------umeng end----------------------------

#ARouter
-keep public class com.alibaba.android.arouter.routes.**{*;}
-keep public class com.alibaba.android.arouter.facade.**{*;}
-keep class * implements com.alibaba.android.arouter.facade.template.ISyringe{*;}

# 如果使用了 byType 的方式获取 Service，需添加下面规则，保护接口
-keep interface * implements com.alibaba.android.arouter.facade.template.IProvider

# 如果使用了 单类注入，即不定义接口实现 IProvider，需添加下面规则，保护实现
# -keep class * implements com.alibaba.android.arouter.facade.template.IProvider

# 不混淆的文件
-keep class com.donews.common.CommonModuleInit{*;}
-keep class com.donews.main.application.MainModuleInit{*;}
-keep class com.donews.main.ui.** { *; }
-keep class com.donews.common.download.**{*;}
-keep class com.donews.web.base.WebConfig{*;}

#请避免混淆Bugly，在Proguard混淆文件中增加以下配置：
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}

#充电模块
-keep class com.donews.home.bean.**{*;}
-keep class com.donews.home.viewModel.**{*;}

# keeplive模块
-keep class  com.donews.alive.bean.**{*;}
-keep class com.donews.alive.KeepAlive{*;}

#sdk模块
-keep class com.dn.sdk.bean.**{*;}
-keep class com.dn.sdk.AdLoadManager{*;}

#netWork 模块
-keep  class  com.donews.network.model.**{*;}
-keep  class  com.donews.network.cache.stategy.**{*;}

# common 模块
#-keep class com.donews.common.adapter.CommonBindingAdapters{*;}
-keep class com.donews.common.contract.**{*;}

# Arouter 模块
-keep class com.dn.drouter.**{*;}

# dialog 模块
-keep class com.donews.dialog.**{*;}

# login 模块
-keep class com.donews.login.provider.**{*;}

# mine模块
-keep class com.donews.mine.provider.**{*;}

# 数美
-keep class com.ishumei.** {*;}

#EventBus
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }


-keep class * extends com.dn.sdk.sdk.bean.BaseAdIdConfigBean{*;}
-keep class * extends com.dn.sdk.sdk.bean.RequestInfo{*;}



#保持多牛的sdk模块中代码不混淆，保持原样
-keep class cn.cd.dn.sdk.** { *; }
-dontwarn cn.cd.dn.sdk.**

## pangle 穿山甲原有的
-keep class com.bytedance.sdk.openadsdk.** { *; }
-keep public interface com.bytedance.sdk.openadsdk.downloadnew.** {*;}
-keep class com.pgl.sys.ces.** {*;}
-keep class com.bytedance.embed_dr.** {*;}
-keep class com.bytedance.embedapplog.** {*;}

## pangle 插件新增 穿山甲插件化版本新增
-keep public class com.ss.android.**{*;}
-keeppackagenames com.bytedance.sdk.openadsdk.api
-keeppackagenames com.bytedance.embed_dr
-keeppackagenames com.bytedance.embedapplog
-keeppackagenames com.ss.android

#聚合混淆
-keep class bykvm*.**
-keep class com.bytedance.msdk.adapter.**{ public *; }
-keep class com.bytedance.msdk.api.** {
 public *;
}
-keep class com.bytedance.msdk.base.TTBaseAd{*;}
-keep class com.bytedance.msdk.adapter.TTAbsAdLoaderAdapter{
    public *;
    protected <fields>;
}

# baidu sdk 不接入baidu sdk可以不引入
-keepclassmembers enum * {
public static **[] values();
public static ** valueOf(java.lang.String);
}

-keep class com.baidu.mobads.** { *; }
-keep class com.baidu.mobad.** { *; }
-keep class com.bun.miitmdid.core.** {*;}


# Admob 不接入admob sdk可以不引入
-keep class com.google.android.gms.ads.MobileAds {
 public *;
}

#sigmob  不接入sigmob sdk可以不引入
-dontwarn android.support.v4.**
-keep class android.support.v4.** { *; }
-keep interface android.support.v4.** { *; }
-keep public class * extends android.support.v4.**

-keep class sun.misc.Unsafe { *; }
-dontwarn com.sigmob.**
-keep class com.sigmob.**.**{*;}

#oaid 不同的版本混淆代码不太一致，你注意你接入的oaid版本 ，不接入oaid可以不添加
-dontwarn com.bun.**
-keep class com.bun.** {*;}
-keep class a.**{*;}
-keep class XI.CA.XI.**{*;}
-keep class XI.K0.XI.**{*;}
-keep class XI.XI.K0.**{*;}
-keep class XI.vs.K0.**{*;}
-keep class XI.xo.XI.XI.**{*;}
-keep class com.asus.msa.SupplementaryDID.**{*;}
-keep class com.asus.msa.sdid.**{*;}
-keep class com.huawei.hms.ads.identifier.**{*;}
-keep class com.samsung.android.deviceidservice.**{*;}
-keep class com.zui.opendeviceidlibrary.**{*;}
-keep class org.json.**{*;}
-keep public class com.netease.nis.sdkwrapper.Utils {public <methods>;}

-dontoptimize
-dontpreverify

-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }
-keep class * extends cn.jpush.android.helpers.JPushMessageReceiver { *; }

-dontwarn cn.jiguang.**
-keep class cn.jiguang.** { *; }
