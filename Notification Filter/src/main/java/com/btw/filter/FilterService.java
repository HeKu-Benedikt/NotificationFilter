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

    private String[] whitelist;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.i("Service created", TAG);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        whitelist = intent.getStringArrayExtra("whitelist");

        Log.i("Service started", TAG);
        return super.onStartCommand(intent, flags, startId);

    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onNotificationPosted(StatusBarNotification statusBarNotification) {

        if (statusBarNotification.getPackageName().contains(workingApps[0])) {

            CharSequence[] lines = statusBarNotification.getNotification().extras.getCharSequenceArray(Notification.EXTRA_TEXT_LINES);

            boolean delete = true;

            for (int i = 0; i < lines.length; i++) {

                Log.i("In Progress " + i, String.valueOf(lines[i]));

                for (String white : whitelist) {

                    if (String.valueOf(lines[i]).toLowerCase().contains(white)) {
                        Log.i("Delete[False] " + i, String.valueOf(lines[i]));
                        delete = false;
                    }

                }

            }

            if (delete) {

                cancelNotification(statusBarNotification.getPackageName(), statusBarNotification.getTag(), statusBarNotification.getId());
                Log.i("Cancel notification", statusBarNotification.getNotification().extras.getString(Notification.EXTRA_TITLE));
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
