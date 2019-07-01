package com.lingo.notification;

import android.app.NotificationChannel;

public interface ChannelCreator {

    NotificationChannel createChannel(String channelId);
}
