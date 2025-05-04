package com.example.news.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {

    // Xử lý sự kiện khi thiết bị khởi động lại
    @Override
    public void onReceive(Context context, Intent intent) {
        // Kiểm tra nếu sự kiện là khởi động hoàn tất
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            // Lên lịch lại thông báo sau khi khởi động
            AlarmScheduler.scheduleNotification(context);
        }
    }
}