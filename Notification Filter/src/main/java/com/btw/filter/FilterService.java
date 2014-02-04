package com.btw.filter;

import android.content.Intent;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

/**
 * Created by bbille on 29.01.14.
 */
public class FilterService extends NotificationListenerService {

    private static final String TAG = FilterService.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("Service created", TAG);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i("Service started", TAG);
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onNotificationPosted(StatusBarNotification statusBarNotification) {
        Log.i("Notification Posted", statusBarNotification.getNotification().toString());

    }

    @Override
    public void onNotificationRemoved(StatusBarNotification statusBarNotification) {
        Log.i("Notification Removed", statusBarNotification.getNotification().toString());
    }

    @Override
    public void onDestroy() {
        Log.i("Service destroyed", TAG);
        super.onDestroy();
    }
}
