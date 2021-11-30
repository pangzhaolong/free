package com.donews.alive.provider;

import android.content.Context;
import android.content.Intent;

import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * 保活接口,其他需要使用保活的逻辑可以使用此Provider，然后注册到KeepAlive中使用
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/9/13 13:16
 */
public interface IKeepAliveProvider extends IProvider {

	/**
	 * 在保活onCreate()方法中被调用
	 */
	void doStart(Context context);

	/***
	 * 在保活Service 中的OnStartCommand()方法中被调用
	 * @param intent intent 数据
	 * @param flags 服务启动方式
	 * @param startId 启动id
	 */
	void doStartCommand(Context context,Intent intent, int flags, int startId);

	/**
	 * 在保活Service的onDestroy（）方法中被调用
	 */
	void doRelease(Context context);
}
