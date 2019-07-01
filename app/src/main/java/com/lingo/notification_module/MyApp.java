package com.lingo.notification_module;

import android.app.Application;

import com.lingo.notification.NotificationHelper;

public class MyApp extends Application {

    private NotificationHelper mNotificationHelper;

    @Override
    public void onCreate() {
        super.onCreate();

        mNotificationHelper = new NotificationHelper(this);
    }


    public NotificationHelper getNotificationHelper() {
        return mNotificationHelper;
    }
}
