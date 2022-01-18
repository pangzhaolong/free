package com.donews.keepalive;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import androidx.core.app.NotificationCompat;

import com.blankj.utilcode.util.AppUtils;
import com.keepalive.daemon.core.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class LaunchStart {
    private static final int ACTION_OPEN_FAIL = -1;
    private static final int ACTION_OPEN_END = -2;
    private static final int ACTION_SHOW_NORMAL = 1;
    private static final int ACTION_SHOW_FULL_INTENT = 2;
    private static final int ACTION_SHOW_PENDING_INTENT = 3;
    private static final int ACTION_SHOW_ALARM = 4;
    private static final int ACTION_SHOW_VIVO = 5;
    private static final int ACTION_SHOW_CMD = 6;

    private final Handler handler = new Handler(Looper.getMainLooper());

    private final ActionStart start;

    static final Map<String, Boolean> status = new HashMap<>();

    {
        ActionStart alarm = new ActionStart(handler) {
            private AlarmHelper alarmHelper;

            @Override
            public void doRun(Context context, Intent intent) {
                alarmHelper = new AlarmHelper();
                alarmHelper.scheduleNextJob(context, intent, 0);
            }

            @Override
            public int getActionId() {
                return ACTION_SHOW_ALARM;
            }

            @Override
            public void cancel() {
                if (alarmHelper != null)
                    alarmHelper.cancel();
                super.cancel();
            }
        };

        ActionStart normal = new ActionStart(handler) {
            private boolean execute = false;

            @Override
            public void doRun(Context context, Intent intent) {
                try {
                    context.startActivity(intent);
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }

            @Override
            public long getDelayAfter() {
                if (execute)
                    return 1000L;
                else {
                    execute = true;
                    return 4000L;
                }
            }

            @Override
            public int getActionId() {
                return ACTION_SHOW_NORMAL;
            }
        };

        ActionStart pending = new ActionStart(handler) {
            @Override
            public void doRun(Context context, Intent intent) {
                try {
                    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
                            PendingIntent.FLAG_UPDATE_CURRENT);
                    pendingIntent.send();
                } catch (Exception e) {
                }
            }

            @Override
            public int getActionId() {
                return ACTION_SHOW_PENDING_INTENT;
            }
        };

        ActionStart fullIntent = new ActionStart(handler) {
            @Override
            public void doRun(Context context, Intent intent) {
                try {
                    new NotificationUtils(context).sendNotificationFullScreen(context.getString(R.string.app_name), "", intent);
                } catch (Throwable t) {
                }
            }

            @Override
            public void cancel() {
                NotificationUtils.clearAllNotification(DazzleReal.application);
                super.cancel();
            }

            @Override
            public int getActionId() {
                return ACTION_SHOW_FULL_INTENT;
            }

            @Override
            public long getDelayAfter() {
                return 2000L;
            }
        };

        ActionStart vivo = new ActionStart(handler) {
            @Override
            public void doRun(Context context, Intent intent) {
                if (isSupportVivoWidgetStart()) {
                    try {
                        Method method = Intent.class.getDeclaredMethod("setIsVivoWidget", new Class[]{Boolean.TYPE});
                        method.setAccessible(true);
                        method.invoke(intent, new Object[]{Boolean.valueOf(true)});
                        PendingIntent pendingIntent = PendingIntent.getActivity(context, 10199, intent,
                                PendingIntent.FLAG_UPDATE_CURRENT);
                        pendingIntent.send();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

                @Override
            public void cancel() {
                super.cancel();
            }

            @Override
            public int getActionId() {
                return ACTION_SHOW_VIVO;
            }
        };


        ActionStart cmd = new ActionStart(handler) {
            @Override
            public void doRun(Context context, Intent intent) {
                try {
                    int flags = intent.getFlags();

                    byte[] bytes = new byte[]{
                            (byte) ((flags >> 24) & 0xFF),
                            (byte) ((flags >> 16) & 0xFF),
                            (byte) ((flags >> 8) & 0xFF),
                            (byte) (flags & 0xFF)
                    };

                    String flag = HEX.encodeHex(bytes, false);
                    String paramsCmd = DeviceHelper.intentToCmd(intent);

                    Process process =
                            Runtime.getRuntime().exec("am start --user 0 -n " + context.getPackageName() + "/" + intent.getComponent().getClassName()
                                    + " -f 0x" + flag + paramsCmd);

                    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    char[] buffer = new char[4096];
                    StringBuilder output = new StringBuilder();

                    int read = reader.read(buffer);
                    while (read > 0) {
                        output.append(buffer, 0, read);
                        read = reader.read(buffer);
                    }

                    reader.close();
                    process.waitFor();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public int getActionId() {
                return ACTION_SHOW_CMD;
            }
        };


        ActionStart end = new ActionStart(handler) {
            @Override
            public void doRun(Context context, Intent intent) {
                LaunchStart.this.cancel();
            }

            @Override
            public int getActionId() {
                return ACTION_OPEN_END;
            }
        };


        start = alarm;

        alarm.setNext(normal);
        normal.setNext(pending);
        pending.setNext(fullIntent);
        fullIntent.setNext(vivo);
        vivo.setNext(cmd);
        cmd.setNext(end);


        if (Build.VERSION.SDK_INT >= 29) {
            ActionStart fPre = fullIntent.getPre();
            ActionStart fNext = fullIntent.getNext();
            fPre.setNext(fNext);

            fullIntent.setNext(alarm.getNext());
            fullIntent.setPre(alarm);
        }

        System.out.println("id = " + start);
        System.out.println("id = " + start.getNext());
        System.out.println("id = " + start.getNext().getNext());
        System.out.println("id = " + start.getNext().getNext().getNext());
        System.out.println("id = " + start.getNext().getNext().getNext().getNext());
    }

    private boolean isSupportVivoWidgetStart() {
        return (!TextUtils.isEmpty(Build.BRAND) && Build.BRAND.toLowerCase().contains("vivo"));
    }

    public void doStart(Context context, Intent intent) {
        start.reset();
        start.run(context, intent);
    }

    public ActionStart getActionStart(){
        return start;
    }

    public void cancel() {
        handler.removeCallbacksAndMessages(null);
        start.cancel();
    }
}
