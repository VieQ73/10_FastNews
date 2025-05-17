package com.example.news.fragment;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.news.R;
import com.example.news.data.DbHelper;
import com.example.news.data.SharedPreferencesHelper;
import com.example.news.utils.AlarmScheduler;
import com.example.news.utils.NotificationReceiver;

public class SettingsFragment extends Fragment {
    ImageView toggleNotifications, deleteData;
    Button selectTime, setTime;
    Spinner timeAfterSpinner, languageSpinner, countrySpinner, numberSpinner;
    CardView timePickerLL;
    TimePicker timePicker;
    View dimOverlay;
    SharedPreferencesHelper helper;
    LinearLayout extraNotification;
    DbHelper db;
    final boolean[] isNotificationOn = {false};

    // Constructor rỗng bắt buộc cho Fragment
    public SettingsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Nạp layout cho fragment từ file fragment_settings.xml
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        // Khởi tạo các thành phần giao diện
        toggleNotifications = view.findViewById(R.id.toggleNotifications);
        selectTime = view.findViewById(R.id.selectTime);
        setTime = view.findViewById(R.id.setTime);
        timePickerLL = view.findViewById(R.id.timePickerLL);
        timePicker = view.findViewById(R.id.timePicker);
        dimOverlay = view.findViewById(R.id.dimOverlay);
        timeAfterSpinner = view.findViewById(R.id.timeAfterSpinner);
        languageSpinner = view.findViewById(R.id.languageSpinner);
        countrySpinner = view.findViewById(R.id.countrySpinner);
        numberSpinner = view.findViewById(R.id.numberSpinner);
        extraNotification = view.findViewById(R.id.extraNotification);
        deleteData = view.findViewById(R.id.deleteData);

        // Khởi tạo DbHelper và SharedPreferencesHelper
        db = new DbHelper(getContext());
        helper = new SharedPreferencesHelper(getContext());

        // Tối ưu hóa pin để đảm bảo thông báo hoạt động đúng
        manageBatteryPerformance();
        // Kiểm tra và thiết lập các giá trị cài đặt mặc định
        checkForDefaultValues();

        // Xử lý sự kiện bật/tắt thông báo
        toggleNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manageNotificationPermission();
            }
        });

        // Xử lý sự kiện chọn thời gian cho thông báo đầu tiên
        selectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timePickerLL.getVisibility() == View.GONE) {
                    timePickerLL.setVisibility(View.VISIBLE);
                    dimOverlay.setVisibility(View.VISIBLE);
                    configureTimePicker();
                    setTime.setOnClickListener(view -> {
                        int hour = timePicker.getHour();

                        // Định dạng giờ hiển thị trên nút
                        if (hour < 10) selectTime.setText("0" + hour + ":00");
                        else selectTime.setText(hour + ":00");

                        // Lưu thời gian thông báo vào SharedPreferences
                        helper.setFirstNotificationAt(hour);
                        timePickerLL.setVisibility(View.GONE);
                        dimOverlay.setVisibility(View.GONE);

                        // Gọi hàm thiết lập thông báo
                        callNotification(hour);
                    });
                } else {
                    timePickerLL.setVisibility(View.GONE);
                    dimOverlay.setVisibility(View.GONE);
                }
            }
        });

        // Xử lý sự kiện chọn khoảng thời gian lặp lại thông báo
        timeAfterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedInterval = parent.getItemAtPosition(position).toString();
                int interval = Integer.parseInt(selectedInterval.split(" ")[0]);
                helper.setNotificationRepeatInterval(interval);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Không làm gì
            }
        });

        // Xử lý sự kiện chọn ngôn ngữ
        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                String selectedLanguage;
                if (selectedItem.contains("(") && selectedItem.contains(")")) {
                    selectedLanguage = selectedItem.substring(selectedItem.indexOf('(') + 1, selectedItem.indexOf(')'));
                } else {
                    selectedLanguage = null;
                }
                helper.setLanguage(selectedLanguage);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Không làm gì
            }
        });

        // Xử lý sự kiện chọn quốc gia
        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                String selectedCountry;
                if (selectedItem.contains("(") && selectedItem.contains(")")) {
                    selectedCountry = selectedItem.substring(selectedItem.indexOf('(') + 1, selectedItem.indexOf(')'));
                } else {
                    selectedCountry = null;
                }
                helper.setCountry(selectedCountry);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Không làm gì
            }
        });

        // Xử lý sự kiện chọn số lượng tin tức tối đa
        numberSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedMaxNews = parent.getItemAtPosition(position).toString();
                int maxNews;
                if (!selectedMaxNews.equals("Số bài tối đa")) {
                    maxNews = Integer.parseInt(selectedMaxNews);
                } else {
                    maxNews = 20;
                }
                helper.setMaxNumbers(maxNews);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Không làm gì
            }
        });

        // Xử lý sự kiện xóa toàn bộ dữ liệu
        deleteData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext())
                        .setTitle("Xóa dữ liệu")
                        .setMessage("Dữ liệu sẽ bị xóa vĩnh viễn. Bạn có chắc chắn muốn tiếp tục?")
                        .setPositiveButton("Có", (dialogInterface, i) -> {
                            db.deleteAllSavedNews();
                            helper.clear();
                            Toast.makeText(getContext(), "Dữ liệu đã được xóa thành công.", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("Không", (dialogInterface, i) -> {
                            // Không làm gì
                        });

                dialog.show();
            }
        });

        return view;
    }

    // Hàm tối ưu hóa pin để đảm bảo thông báo hoạt động
    private void manageBatteryPerformance() {
        String packageName = requireActivity().getPackageName();
        PowerManager pm = (PowerManager) requireContext().getSystemService(Context.POWER_SERVICE);
        if (pm != null && !pm.isIgnoringBatteryOptimizations(packageName)) {
            Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + packageName));
            startActivity(intent);
        }
    }

    // Hàm quản lý quyền gửi thông báo
    public void manageNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1);
            } else {
                getAlarmPermission();
                toggleNotificationImage();
            }
        } else {
            getAlarmPermission();
            toggleNotificationImage();
        }
    }

    // Hàm thay đổi trạng thái và biểu tượng thông báo
    public void toggleNotificationImage() {
        if (!isNotificationOn[0]) {
            isNotificationOn[0] = true;
            toggleNotifications.setImageResource(R.drawable.switch_on_icon);
            extraNotification.setVisibility(View.VISIBLE);
            helper.setNotificationStatus(1);
            Toast.makeText(getContext(), "Đã bật thông báo", Toast.LENGTH_SHORT).show();
        } else {
            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext())
                    .setTitle("Tắt thông báo")
                    .setMessage("Bạn sẽ không nhận được thông báo. Bạn có chắc chắn muốn tắt?")
                    .setPositiveButton("Có", (dialogInterface, i) -> {
                        helper.setNotificationStatus(0);
                        toggleNotifications.setImageResource(R.drawable.switch_off_icon);
                        Toast.makeText(getContext(), "Đã tắt thông báo", Toast.LENGTH_SHORT).show();
                        extraNotification.setVisibility(View.GONE);
                        isNotificationOn[0] = false;
                    })
                    .setNegativeButton("Không", (dialogInterface, i) -> {
                        // Không làm gì
                    });
            dialog.show();
        }
    }

    // Hàm cấu hình TimePicker để chọn giờ
    public void configureTimePicker() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            timePicker.setIs24HourView(true);
        }
        int minuteSpinnerId = android.content.res.Resources.getSystem().getIdentifier("minute", "id", "android");
        if (minuteSpinnerId != 0) {
            View minuteSpinner = timePicker.findViewById(minuteSpinnerId);
            if (minuteSpinner != null) {
                minuteSpinner.setVisibility(View.GONE);
            }
        }
        int dividerId = android.content.res.Resources.getSystem().getIdentifier("divider", "id", "android");
        if (dividerId != 0) {
            View divider = timePicker.findViewById(dividerId);
            if (divider != null) {
                divider.setVisibility(View.GONE);
            }
        }
    }

    // Hàm kiểm tra và thiết lập các giá trị cài đặt mặc định
    public void checkForDefaultValues() {
        // Kiểm tra trạng thái thông báo
        int notificationStatus = helper.getNotificationStatus();
        if (notificationStatus == 1) {
            toggleNotifications.setImageResource(R.drawable.switch_on_icon);
            extraNotification.setVisibility(View.VISIBLE);
            isNotificationOn[0] = true;
        } else {
            toggleNotifications.setImageResource(R.drawable.switch_off_icon);
            extraNotification.setVisibility(View.GONE);
            isNotificationOn[0] = false;
        }

        // Kiểm tra thời gian thông báo đầu tiên
        if (extraNotification.getVisibility() != View.GONE) {
            int firstNotification = helper.getFirstNotificationAt();
            if (firstNotification != -1) {
                selectTime.setText(String.format("%02d:00", firstNotification));
            } else {
                selectTime.setText("07:00");
                helper.setFirstNotificationAt(7);
            }

            // Kiểm tra khoảng thời gian lặp lại thông báo
            int repeatInterval = helper.getNotificationRepeatInterval();
            if (repeatInterval != -1) {
                timeAfterSpinner.setSelection(getSpinnerIndex(timeAfterSpinner, repeatInterval + " Giờ"));
            } else {
                timeAfterSpinner.setSelection(getSpinnerIndex(timeAfterSpinner, "24 Giờ"));
                helper.setNotificationRepeatInterval(24);
            }
        }

        // Kiểm tra ngôn ngữ
        String language = helper.getLanguage();
        if (language != null && !language.isEmpty()) {
            languageSpinner.setSelection(getLanguageSpinnerIndex(languageSpinner, language));
        } else {
            languageSpinner.setSelection(getLanguageSpinnerIndex(languageSpinner, "vi"));
            helper.setLanguage("vi");
        }

        // Kiểm tra quốc gia
        String country = helper.getCountry();
        if (country != null && !country.isEmpty()) {
            countrySpinner.setSelection(getCountrySpinnerIndex(countrySpinner, country));
        } else {
            countrySpinner.setSelection(getCountrySpinnerIndex(countrySpinner, "vn"));
            helper.setCountry("vn");
        }

        // Kiểm tra số lượng tin tức tối đa
        int maxNews = helper.getMaxNumbers();
        if (maxNews != -1) {
            numberSpinner.setSelection(getSpinnerIndex(numberSpinner, String.valueOf(maxNews)));
        } else {
            numberSpinner.setSelection(getSpinnerIndex(numberSpinner, "20"));
            helper.setMaxNumbers(20);
        }
    }

    // Hàm tìm chỉ số của mục trong spinner
    private int getSpinnerIndex(Spinner spinner, String value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().contains(value)) {
                return i;
            }
        }
        return 0;
    }

    // Hàm tìm chỉ số của quốc gia trong spinner
    private int getCountrySpinnerIndex(Spinner spinner, String countryCode) {
        for (int i = 0; i < spinner.getCount(); i++) {
            String item = spinner.getItemAtPosition(i).toString();
            if (item.contains("(") && item.contains(")")) {
                String code = item.substring(item.indexOf("(") + 1, item.indexOf(")"));
                if (code.equalsIgnoreCase(countryCode)) {
                    return i;
                }
            }
        }
        return 0;
    }

    // Hàm tìm chỉ số của ngôn ngữ trong spinner
    private int getLanguageSpinnerIndex(Spinner spinner, String languageCode) {
        for (int i = 0; i < spinner.getCount(); i++) {
            String item = spinner.getItemAtPosition(i).toString();
            if (item.contains("(") && item.contains(")")) {
                String code = item.substring(item.indexOf("(") + 1, item.indexOf(")"));
                if (code.equalsIgnoreCase(languageCode)) {
                    return i;
                }
            }
        }
        return 0;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getAlarmPermission();
                toggleNotificationImage();
            } else {
                Toast.makeText(requireContext(), "Cần quyền thông báo để tiếp tục", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Hàm yêu cầu quyền lên lịch thông báo chính xác
    private void getAlarmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            AlarmManager alarmManager = (AlarmManager) requireContext().getSystemService(Context.ALARM_SERVICE);
            if (!alarmManager.canScheduleExactAlarms()) {
                Toast.makeText(requireContext(), "Vui lòng cấp quyền lên lịch thông báo", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                startActivity(intent);
            }
        }
    }

    // Hàm gửi thông báo
    public void callNotification(int hour) {
        Context context = getContext();
        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.putExtra("notification_title", "Tin Tức Nhanh");
        intent.putExtra("notification_message", "Thông báo được thiết lập lúc " + hour + " giờ");

        // Gửi broadcast đến NotificationReceiver
        context.sendBroadcast(intent);
    }
}