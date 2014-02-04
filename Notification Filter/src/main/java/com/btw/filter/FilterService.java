package com.btw.filter;

import android.annotation.TargetApi;
import android.app.Notification;
import android.content.Intent;
import android.os.Build;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

/**
 * Created by bbille on 29.01.14.
 */
public class FilterService extends NotificationListenerService {

    private static final String TAG = FilterService.class.getSimpleName();

    private String[] workingApps = new String[]{"ingress"};

    private String[] whitelist = new String[]{"neufahrn", "ottobrunn", "bruck", "eching", "freising", "hadern", "haar", "garching"};

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

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onNotificationPosted(StatusBarNotification statusBarNotification) {

        if (statusBarNotification.getPackageName().contains(workingApps[0])) {

            CharSequence[] lines = statusBarNotification.getNotification().extras.getCharSequenceArray(Notification.EXTRA_TEXT_LINES);

            for (int i = 0; i < lines.length; i++) {

                Log.i("Line " + i, String.valueOf(lines[i]));

                boolean delete = true;

                for (String white : whitelist) {

                    if (String.valueOf(lines[i]).toLowerCase().contains(white)) {
                        delete = false;
                    }

                }

                if (delete) {
                    cancelNotification(statusBarNotification.getPackageName(), statusBarNotification.getTag(), statusBarNotification.getId());
                }

            }

        }

    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onNotificationRemoved(StatusBarNotification statusBarNotification) {
        Log.i("Notification Removed ", statusBarNotification.getNotification().extras.getString(Notification.EXTRA_TITLE));
    }

    @Override
    public void onDestroy() {
        Log.i("Service destroyed", TAG);
        super.onDestroy();
    }

}
