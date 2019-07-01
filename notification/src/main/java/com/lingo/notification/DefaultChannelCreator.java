package com.lingo.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.O)
public class DefaultChannelCreator implements ChannelCreator {

    private final String mAppName;

    public DefaultChannelCreator(String appName) {
        mAppName = appName;
    }

    @Override
    public NotificationChannel createChannel(String channelId) {
        switch (channelId) {
            case NotificationHelper.CHANNEL_ID_CHAT: {
                NotificationChannel channel = new NotificationChannel(channelId, "新消息通知", NotificationManager.IMPORTANCE_HIGH);
                channel.setDescription((TextUtils.isEmpty(mAppName) ? "" : mAppName) + "收到新消息时使用的通知类别");

                // 可否绕过系统的 请勿打扰模式
                channel.setBypassDnd(false);

                // 锁屏是否显示通知
                channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

                // 是否有闪光灯
                channel.enableLights(true);

                // 设置闪光时的颜色
                channel.setLightColor(Color.BLUE);

                // 是否显示桌面图标角标
                channel.setShowBadge(true);

                // 是否震动
                channel.enableVibration(true);

                // 设置震动模式
//                channel.setVibrationPattern(new long[]{});

                // 设置通知响铃
//                AudioAttributes audioAttributes = channel.getAudioAttributes();
//                channel.setSound();

                return channel;
            }

            case NotificationHelper.CHANNEL_ID_OTHER: {
                NotificationChannel channel = new NotificationChannel(channelId, "其他通知", NotificationManager.IMPORTANCE_HIGH);
                channel.setDescription("下载等场景使用的通知类别");

                // 可否绕过系统的 请勿打扰模式
                channel.setBypassDnd(false);

                // 锁屏是否显示通知
                channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

                // 是否有闪光灯
                channel.enableLights(false);

                // 设置闪光时的颜色
//                channel.setLightColor(Color.BLUE);

                // 是否显示桌面图标角标
                channel.setShowBadge(true);

                // 是否震动
                channel.enableVibration(true);

                // 设置震动模式
//                channel.setVibrationPattern(new long[]{});

                // 设置通知响铃
//                AudioAttributes audioAttributes = channel.getAudioAttributes();
//                channel.setSound();

                return channel;
            }
        }
        return new NotificationChannel(channelId, channelId, NotificationManager.IMPORTANCE_DEFAULT);
    }
}
