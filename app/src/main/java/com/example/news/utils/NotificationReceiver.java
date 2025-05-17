package com.example.news.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.news.MainActivity;
import com.example.news.R;
import com.example.news.data.SharedPreferencesHelper;

public class NotificationReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "kenh_thong_bao_tin_tuc";
    private static final int NOTIFICATION_ID = 100;

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferencesHelper sharedPreferencesHelper = new SharedPreferencesHelper(context);
        int notificationStatus = sharedPreferencesHelper.getNotificationStatus();

        if (notificationStatus == 1) {
            createNotificationChannel(context);

            String title = intent.getStringExtra("notification_title");
            String message = intent.getStringExtra("notification_message");
            if (title == null || title.isEmpty() || message == null || message.isEmpty()) {
                title = "Cập nhật tin tức hàng ngày";
                message = "Xem ngay các tin tức mới nhất!";
            }

            Intent notificationIntent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.notification_icon)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify(NOTIFICATION_ID, builder.build());
        }
    }

    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Kênh Tin Tức";
            String description = "Kênh thông báo cho tin tức hàng ngày";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}