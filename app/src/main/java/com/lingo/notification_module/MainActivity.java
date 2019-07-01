package com.lingo.notification_module;

import android.app.Notification;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.lingo.notification.NotificationHelper;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText mTagEditView;
    private EditText mIdEditView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTagEditView = findViewById(R.id.et_tag);
        mIdEditView = findViewById(R.id.et_id);
    }

    public void onPostNotification(View view) {

        NotificationHelper notificationHelper = ((MyApp) getApplication()).getNotificationHelper();

        String tag = mTagEditView.getText().toString();
        int id = Integer.parseInt(mIdEditView.getText().toString());

        Notification notification = notificationHelper
                .createChatNotificationBuilder(null, String.format(Locale.getDefault(), "tag:%s, id:%d", tag, id), R.mipmap.ic_launcher)
                .setLargeIcon(createLargeIcon())
                .build();
        notificationHelper.notify(tag, id, notification);

    }

    public void onClearNotification(View view) {

        NotificationHelper notificationHelper = ((MyApp) getApplication()).getNotificationHelper();

        String tag = mTagEditView.getText().toString();
        String idStr = mIdEditView.getText().toString();

        if (TextUtils.isEmpty(tag)) {

            if (TextUtils.isEmpty(idStr)) {
                notificationHelper.cancelAll();
            } else {

                notificationHelper.cancel(Integer.valueOf(idStr));

            }

        } else {

            notificationHelper.cancel(tag, Integer.parseInt(idStr));
        }

    }

    private Bitmap createLargeIcon() {
        Drawable loadIcon = getApplicationInfo().loadIcon(getPackageManager());

        Bitmap appIcon = null;

        try {
            if (Build.VERSION.SDK_INT >= 26 && loadIcon instanceof AdaptiveIconDrawable) {
                appIcon = Bitmap.createBitmap(loadIcon.getIntrinsicWidth(), loadIcon.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(appIcon);
                loadIcon.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                loadIcon.draw(canvas);
            } else {
                appIcon = ((BitmapDrawable) loadIcon).getBitmap();
            }
        } catch (Exception var19) {
            var19.printStackTrace();
        }

        return appIcon;
    }
}
