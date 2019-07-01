package com.lingo.notification;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;

import androidx.core.app.NotificationCompat;

public class NotificationHelper {

    public static final String DEFAULT_TAG = "default_tag";
    public static final int DEFAULT_ID = 1234;

    public static final String CHANNEL_ID_CHAT = "channel_id_chat";
    public static final String CHANNEL_ID_OTHER = "channel_id_other";

    private final Application mApplication;
    private final NotificationManager mNotificationManager;

    private ChannelCreator mChannelCreator;

    public NotificationHelper(Application application) {
        mApplication = application;

        mNotificationManager = (NotificationManager) mApplication.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mChannelCreator = new DefaultChannelCreator(getAppName(mApplication));
        }
    }

    public void setChannelCreator(ChannelCreator channelCreator) {
        mChannelCreator = channelCreator;
    }

    public void notify(String tag, int id, Notification notification) {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String channelId = notification.getChannelId();

            if (TextUtils.isEmpty(channelId)) {
                throw new IllegalArgumentException("channelId is null");
            }

            NotificationChannel notificationChannel = mNotificationManager.getNotificationChannel(channelId);
            if (notificationChannel == null) {
                mNotificationManager.createNotificationChannel(mChannelCreator.createChannel(channelId));
            }
        }

        mNotificationManager.notify(tag, id, notification);
    }

    public void notify(Notification notification) {
        notify(DEFAULT_TAG, DEFAULT_ID, notification);
    }

    public void cancel(String tag, int id) {
        mNotificationManager.cancel(tag, id);
    }

    public void cancel(int id) {
        mNotificationManager.cancel(id);
    }

    public void cancelAll() {
        mNotificationManager.cancelAll();
    }

    private static String getAppName(Application application) {

        PackageManager packageManager = application.getPackageManager();

        try {
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(application.getPackageName(), 0);
            CharSequence appName = packageManager.getApplicationLabel(applicationInfo);
            return (String) appName;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public NotificationCompat.Builder createNotificationBuilder(String channelId, String title, String content, int smallIcon) {

        if (TextUtils.isEmpty(channelId)) {
            throw new IllegalArgumentException("channelId is null");
        }

        if (TextUtils.isEmpty(title)) {
            title = getAppName(mApplication);
        }

        if (TextUtils.isEmpty(content)) {
            throw new IllegalArgumentException("content is null");
        }

        if (smallIcon == 0) {
            throw new IllegalArgumentException("icon is empty");
        }

        return new NotificationCompat.Builder(mApplication, channelId)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(smallIcon)
                ;
    }

    public NotificationCompat.Builder createChatNotificationBuilder(String title, String content, int smallIcon) {
        return createNotificationBuilder(CHANNEL_ID_CHAT, title, content, smallIcon);
    }

    public NotificationCompat.Builder createOtherNotificationBuilder(String title, String content, int smallIcon) {
        return createNotificationBuilder(CHANNEL_ID_OTHER, title, content, smallIcon);
    }

    public PendingIntent createPendingIntent(int rc, Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return PendingIntent.getActivity(mApplication, rc, intent, PendingIntent.FLAG_ONE_SHOT, null);
    }

}
