package com.example.news.utils;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.news.MainActivity;
import com.example.news.R;
import com.example.news.data.SharedPreferencesHelper;

public class NotificationReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "kenh_thong_bao_tin_tuc";
    private static final int NOTIFICATION_ID = 100;

    // Xử lý khi nhận được Intent từ AlarmManager
    @Override
    public void onReceive(Context context, Intent intent) {
        // Khởi tạo SharedPreferencesHelper để kiểm tra trạng thái thông báo
        SharedPreferencesHelper sharedPreferencesHelper = new SharedPreferencesHelper(context);

        // Lấy trạng thái thông báo
        int notificationStatus = sharedPreferencesHelper.getNotificationStatus();

        // Chỉ gửi thông báo nếu thông báo được bật
        if (notificationStatus == 1) {
            // Tạo kênh thông báo
            createNotificationChannel(context);

            // Lấy tiêu đề và nội dung thông báo từ Intent
            String title = intent.getStringExtra("notification_title");
            String message = intent.getStringExtra("notification_message");
            // Sử dụng giá trị mặc định nếu không có tiêu đề hoặc nội dung
            if (title == null || title.isEmpty() || message == null || message.isEmpty()) {
                title = "Cập nhật tin tức hàng ngày";
                message = "Xem ngay các tin tức mới nhất!";
            }

            // Tạo Intent để mở MainActivity khi nhấn vào thông báo
            Intent notificationIntent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

            // Xây dựng thông báo
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.news_icon)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);

            // Hiển thị thông báo
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // Không gửi thông báo nếu thiếu quyền
                return;
            }
            notificationManager.notify(NOTIFICATION_ID, builder.build());
        }
    }

    // Tạo kênh thông báo cho Android 8.0 trở lên
    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Kênh Tin Tức";
            String description = "Kênh thông báo cho tin tức hàng ngày";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            // Đăng ký kênh thông báo với hệ thống
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}