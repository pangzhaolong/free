# library_sdk使用方式
### 初始化

 ```
  @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //初始化进程守护
        DaemonHolder.getInstance().attach(base, this);
    }
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化sdk
        AdLoadManager.getInstance().init(getApplication(), true);
        //启动广告服务
        Intent intent = new Intent(this, AdDaemonService.class);
        ContextCompat.startForegroundService(this, intent);
    }
 ```
### 类说明
1、在AdIdConfig.class类中配置广告id

2、AdLoadManager.class,广告请求管理类

3、RequestInfo 广告请求封装类，包括广告id,广告数量，宽，高

广告加载具体使用方式参考TestActivity

4、TimeBroadCast 监听时间，根据设置的时间间隔做应用外弹窗

5、ScreenSwitchBroadCast 控制锁屏广告

6、CountTrackImpl 数据打点




