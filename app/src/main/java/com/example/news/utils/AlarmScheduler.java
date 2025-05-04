package com.example.news.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.example.news.data.SharedPreferencesHelper;

import java.util.Calendar;

public class AlarmScheduler {

    // Lên lịch thông báo định kỳ dựa trên cài đặt người dùng
    public static void scheduleNotification(Context context) {
        // Khởi tạo SharedPreferencesHelper để lấy cài đặt thông báo
        SharedPreferencesHelper sharedPreferencesHelper = new SharedPreferencesHelper(context);

        // Lấy trạng thái thông báo, giờ thông báo đầu tiên, và khoảng thời gian lặp lại
        int notificationStatus = sharedPreferencesHelper.getNotificationStatus();
        int firstNotificationAt = sharedPreferencesHelper.getFirstNotificationAt();
        int notificationInterval = sharedPreferencesHelper.getNotificationRepeatInterval();

        // Chỉ lên lịch nếu thông báo được bật (notificationStatus = 1)
        if (notificationStatus == 1) {
            // Lấy dịch vụ AlarmManager để quản lý thông báo định kỳ
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            // Tạo Intent để gửi tới NotificationReceiver
            Intent intent = new Intent(context, NotificationReceiver.class);
            // Tạo PendingIntent để AlarmManager sử dụng
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

            // Thiết lập thời gian cho thông báo đầu tiên
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, firstNotificationAt);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);

            // Tính khoảng thời gian lặp lại (đổi từ giờ sang mili giây)
            long repeatInterval = notificationInterval * AlarmManager.INTERVAL_HOUR;

            // Lên lịch thông báo định kỳ
            if (alarmManager != null) {
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), repeatInterval, pendingIntent);
            }
        }
    }

    // Hủy thông báo đã lên lịch
    public static void cancelNotification(Context context) {
        // Lấy dịch vụ AlarmManager
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        // Tạo Intent để xác định thông báo cần hủy
        Intent intent = new Intent(context, NotificationReceiver.class);
        // Tạo PendingIntent tương ứng
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        // Hủy thông báo nếu AlarmManager tồn tại
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }
}