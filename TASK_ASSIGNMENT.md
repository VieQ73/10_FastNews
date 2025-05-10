# Phân Công Công Việc - Dự Án FastNews

Dưới đây là bảng phân công công việc cho nhóm 5 thành viên phát triển ứng dụng **FastNews**. Mỗi thành viên được gán các chức năng cụ thể, nhiệm vụ chi tiết, và thư viện dự kiến sử dụng.

| Thành viên | Chức năng phụ trách | Nhiệm vụ cụ thể | Thư viện dự kiến |
|-----------|---------------------|-----------------|------------------|
| **Lê Đình Quý (Trưởng nhóm)** | - Tin theo chuyên mục<br>- Quản lý điều hướng (Navigation)<br>- Tích hợp API | - Thiết lập project base<br>- Tích hợp GNews API<br>- Xây dựng logic gọi API theo chuyên mục<br>- Thiết lập ViewPager và Navbar<br>- Review code, quản lý Git | Retrofit, Gson |
| **Phạm Văn Lâm** | - Giao diện HomeFragment<br>- Cải tiến giao diện (Shimmer) | - Xây dựng giao diện danh sách tin tức<br>- Tích hợp hiệu ứng shimmer khi tải<br>- Tối ưu RecyclerView | Shimmer, Glide |
| **Đỗ Phương Thảo** | - Tìm kiếm tin tức<br>- Bộ lọc | - Xây dựng giao diện và logic tìm kiếm<br>- Thêm bộ lọc theo ngôn ngữ và quốc gia<br>- Xử lý khi không có kết quả | Retrofit |
| **Mẫn Văn Trường** | - Xem sau<br>- Xem chi tiết bài viết | - Thiết lập Room database<br>- Xây dựng giao diện và logic xem sau<br>- Tạo Bottom Sheet chi tiết bài viết<br>- Mở liên kết gốc | Room |
| **Trương Văn Tân** | - Chia sẻ tin tức<br>- Thông báo tùy chỉnh | - Logic chia sẻ bài viết qua Intent<br>- Giao diện cài đặt thông báo<br>- Dùng AlarmManager hẹn giờ thông báo<br>- Xử lý thông báo khi khởi động lại | AlarmManager, SharedPreferences |

## Hướng Dẫn Sử Dụng
- Mỗi thành viên tạo branch riêng (VD: `feature/truong van tan`, `feature/do phuong thao`, ...) để phát triển chức năng.
- Commit thường xuyên và tạo pull request để trưởng nhóm (Lê Đình Quý) review.
- Tham khảo đặc tả phần mềm trong `README.md` để hiểu rõ yêu cầu.

## Liên Hệ
Nếu có thắc mắc về phân công, liên hệ trưởng nhóm.