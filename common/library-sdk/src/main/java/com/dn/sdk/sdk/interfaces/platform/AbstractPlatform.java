package com.dn.sdk.sdk.interfaces.platform;


import com.dn.sdk.sdk.interfaces.loader.ILoader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 广告平台控制类
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/9/26 10:40
 */
public abstract class AbstractPlatform {

	private final List<IPlatformListener> mIPlatformListenerList = new ArrayList<>();

	public AbstractPlatform() {
		init();
	}

	public synchronized void addInitListener(IPlatformListener listener) {
		if (isInit()) {
			if (listener != null) {
				listener.initSuccess();
			}
		} else {
			if (listener != null) {
				mIPlatformListenerList.add(listener);
			}
		}
	}

	public synchronized void callInitListener() {
		if (!isInit()) {
			return;
		}
		Iterator<IPlatformListener> iterable = mIPlatformListenerList.iterator();
		while (iterable.hasNext()) {
			IPlatformListener listener = iterable.next();
			if (listener != null) {
				listener.initSuccess();
			}
			iterable.remove();
		}
	}

	public abstract void init();

	public abstract boolean isInit();

	public abstract ILoader getLoader();
}
