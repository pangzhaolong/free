package com.donews.alive.launch;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.donews.alive.KeepAliveLogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author by SnowDragon
 * Date on 2020/12/3
 * Description:
 */
public class LaunchStart {
	public static final String TAG = "LaunchStart";

	public List<ActionStart> actionStartList;

	public void getActionStartList() {
		if (actionStartList == null) {
			actionStartList = new ArrayList<ActionStart>();

			actionStartList.add(new AlarmAction());
			actionStartList.add(new NormalAction());

			actionStartList.add(new PendingAction());

			if (Build.VERSION.SDK_INT >= 29) {
				actionStartList.add(Build.VERSION.SDK_INT >= 29 ? 1 : 3, new FullAction());
			}

			actionStartList.add(new AmAction());


		}
	}

	public volatile boolean isCancel = false;

	public void doStart(Context context, Intent intent, int ways) {
		isCancel = false;
		AlarmHelper.getInstance(context).cancel();
		getActionStartList();

		KeepAliveLogger.d(KeepAliveLogger.TAG, " doStart");

		ThreadUtils.getExecutor().execute(() -> {

			if (actionStartList == null) {
			}
			for (ActionStart actionStart : actionStartList) {
				KeepAliveLogger.e(KeepAliveLogger.TAG, "--doStart isCancel " + isCancel);
				if (isCancel) {
					NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
					notificationManager.cancelAll();
					break;
				}

				actionStart.doAction(context, intent, ways);

				KeepAliveLogger.i(KeepAliveLogger.TAG, " " + actionStart.getClass().getCanonicalName()
						+ " delayTime " + actionStart.getDelay() + " currentTime: " + System.currentTimeMillis());

				try {
					Thread.sleep(actionStart.getDelay());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}


		});

	}

	private class NormalAction extends ActionStart {

		@Override
		void doAction(Context context, Intent intent, int ways) {
			context.startActivity(intent);
		}
	}

	/**
	 * 闹钟启动类，咋空闲模式时，可以在service/broadcast 启动activity
	 */
	private class AlarmAction extends ActionStart {

		@Override
		void doAction(Context context, Intent intent, int ways) {
			AlarmHelper.getInstance(context)
					.setIntent(intent)
					.scheduleNextJob(1L);
		}

		@Override
		public long getDelay() {
			return 1;
		}
	}

	private class PendingAction extends ActionStart {

		@Override
		void doAction(Context context, Intent intent, int ways) {
			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			try {
				pendingIntent.send();
			} catch (PendingIntent.CanceledException e) {
				e.printStackTrace();
			}
		}
	}

	private class FullAction extends ActionStart {

		@Override
		void doAction(Context context, Intent intent, int ways) {

			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			new NotificationUtil(context)
					.setTitle("通知")
					.setDescription("金币已入账")
					.setPendingIntent(pendingIntent, true)
					.createNotification()
					.sendNotification();


		}

		@Override
		public long getDelay() {
			return 2000L;
		}
	}

	private class AmAction extends ActionStart {

		@Override
		void doAction(Context context, Intent intent, int ways) {
			int flags = intent.getFlags();
			//将将十进制转换成16进制
			byte[] bytes = new byte[4];
			bytes[0] = (byte) (flags >>> 24);
			bytes[1] = (byte) (flags >>> 18);
			bytes[2] = (byte) (flags >>> 8);
			bytes[3] = (byte) (flags);
			try {
				String flag = HexUtil.encodeHex(bytes, false);

//                KeepAliveLogger.d(TAG,
//                        "am start --user 0 -n " + context.getPackageName() + "/"
//                                + intent.getComponent().getClassName() + " -f 0x" + flag + " --ei channelId ${ACTION_SHOW_CMD}"
//                );
				Process process = Runtime.getRuntime()
						.exec("am start --user 0 -n " + context.getPackageName()
								+ "/" + intent.getComponent().getClassName() + " -f 0x"
								+ flag + " --ei channelId ${ACTION_SHOW_CMD}");

				BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
				char[] buffer = new char[4096];
				StringBuffer output = new StringBuffer();

				int read = reader.read(buffer);
				while (read > 0) {
					output.append(buffer, 0, read);
					read = reader.read(buffer);
				}
				reader.close();
				process.waitFor();
				KeepAliveLogger.d(TAG, "LaunchStart action :  cmd");
			} catch (IOException | InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
	}


}
