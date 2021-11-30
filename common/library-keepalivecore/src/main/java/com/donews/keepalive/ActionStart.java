package com.donews.keepalive;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;


public abstract class ActionStart {
    private final Handler handler;

    private ActionStart pre = null;
    private ActionStart next = null;
    private boolean isCancel = false;

    public void setPre(ActionStart pre) {
        if (pre != null) pre.next = this;
        this.pre = pre;
    }

    public void setNext(ActionStart next) {
        if (next != null) next.pre = this;
        this.next = next;
    }

    public ActionStart getPre() {
        return pre;
    }

    public ActionStart getNext() {
        return next;
    }

    public ActionStart(Handler handler) {
        this.handler = handler;
    }

    public void run(Context context, Intent intent) {
        if (isCancel) return;
        doRun(context, intent);

        handler.postDelayed(() -> {

            if (isCancel) return;
            if (next != null)
                next.run(context, intent);
        }, getDelayAfter());
    }

    public abstract void doRun(Context context, Intent intent);

    public long getDelayAfter() {
        return 1000L;
    }

    public void cancel() {
        isCancel = true;
        if (next != null) next.cancel();
    }

    public void reset() {
        isCancel = false;
        if (next != null) next.reset();
    }

    public abstract int getActionId();

    @Override
    public String toString() {
        int thisId = getActionId();
        int preId = pre != null ? pre.getActionId() : -1;
        int nextId = next != null ? next.getActionId() : -1;

        return getClass().getName() + "{" +
                "this = " + thisId +
                ", pre=" + preId +
                ", next=" + nextId +
                '}';
    }
}