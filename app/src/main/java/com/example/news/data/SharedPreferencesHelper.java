package com.example.news.data;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesHelper {
    private static final String PREFS_NAME = "com.example.news";

    // Các khóa cho SharedPreferences
    private static final String KEY_NOTIFICATION_STATUS = "trangThaiThongBao";
    private static final String KEY_FIRST_NOTIFICATION_AT = "thoiGianThongBaoDauTien";
    private static final String KEY_NOTIFICATION_REPEAT_INTERVAL = "khoangLapThongBao";
    private static final String KEY_LANGUAGE = "ngonNgu";
    private static final String KEY_COUNTRY = "quocGia";
    private static final String KEY_MAX_NUMBERS = "soLuongTinToiDa";

    private SharedPreferences sharedPreferences;

    // Constructor khởi tạo SharedPreferences
    public SharedPreferencesHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    // Lưu trạng thái thông báo (0: tắt, 1: bật)
    public void setNotificationStatus(int status) {
        sharedPreferences.edit().putInt(KEY_NOTIFICATION_STATUS, status).apply();
    }

    public int getNotificationStatus() {
        return sharedPreferences.getInt(KEY_NOTIFICATION_STATUS, 0);
    }

    // Lưu thời gian thông báo đầu tiên (giờ)
    public void setFirstNotificationAt(int hour) {
        sharedPreferences.edit().putInt(KEY_FIRST_NOTIFICATION_AT, hour).apply();
    }

    public int getFirstNotificationAt() {
        return sharedPreferences.getInt(KEY_FIRST_NOTIFICATION_AT, 0);
    }

    // Lưu khoảng thời gian lặp lại thông báo (giờ)
    public void setNotificationRepeatInterval(int hours) {
        sharedPreferences.edit().putInt(KEY_NOTIFICATION_REPEAT_INTERVAL, hours).apply();
    }

    public int getNotificationRepeatInterval() {
        return sharedPreferences.getInt(KEY_NOTIFICATION_REPEAT_INTERVAL, 24);
    }

    // Lưu mã ngôn ngữ
    public void setLanguage(String language) {
        sharedPreferences.edit().putString(KEY_LANGUAGE, language).apply();
    }

    public String getLanguage() {
        return sharedPreferences.getString(KEY_LANGUAGE, "vi");
    }

    // Lưu mã quốc gia
    public void setCountry(String country) {
        sharedPreferences.edit().putString(KEY_COUNTRY, country).apply();
    }

    public String getCountry() {
        return sharedPreferences.getString(KEY_COUNTRY, "vn");
    }

    // Lưu số lượng tin tức tối đa
    public void setMaxNumbers(int maxNumbers) {
        sharedPreferences.edit().putInt(KEY_MAX_NUMBERS, maxNumbers).apply();
    }

    public int getMaxNumbers() {
        return sharedPreferences.getInt(KEY_MAX_NUMBERS, 20);
    }

    // Xóa trạng thái thông báo
    public void clearNotificationStatus() {
        sharedPreferences.edit().remove(KEY_NOTIFICATION_STATUS).apply();
    }

    // Xóa thời gian thông báo đầu tiên
    public void clearFirstNotificationAt() {
        sharedPreferences.edit().remove(KEY_FIRST_NOTIFICATION_AT).apply();
    }

    // Xóa khoảng thời gian lặp lại thông báo
    public void clearNotificationRepeatInterval() {
        sharedPreferences.edit().remove(KEY_NOTIFICATION_REPEAT_INTERVAL).apply();
    }

    // Xóa ngôn ngữ
    public void clearLanguage() {
        sharedPreferences.edit().remove(KEY_LANGUAGE).apply();
    }

    // Xóa quốc gia
    public void clearCountry() {
        sharedPreferences.edit().remove(KEY_COUNTRY).apply();
    }

    // Xóa số lượng tin tức tối đa
    public void clearMaxNumbers() {
        sharedPreferences.edit().remove(KEY_MAX_NUMBERS).apply();
    }

    // Xóa toàn bộ dữ liệu SharedPreferences
    public void clear() {
        sharedPreferences.edit().clear().apply();
    }
}