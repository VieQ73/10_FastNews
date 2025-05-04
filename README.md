# 📰 FastNews: Cập nhật tin tức mới nhất mỗi ngày!

## 📖 Giới thiệu

**FastNews** là một ứng dụng Android được phát triển bằng Java và XML, cho phép người dùng cập nhật tin tức mới nhất theo nhiều chuyên mục. Người dùng có thể duyệt tin theo chủ đề, tìm kiếm theo từ khóa, và lưu bài viết để đọc sau. Ứng dụng sử dụng API từ **gnews.io**, mang đến giao diện mượt mà và dễ sử dụng để theo dõi các sự kiện mới nhất.

## ✨ Tính năng nổi bật

- 🌐 **Tin theo chuyên mục**: Xem tin tức theo các mục như Giải trí, Thể thao, Công nghệ,...
- 🔍 **Tìm kiếm tin tức**: Tìm bài viết theo từ khóa.
- ⏳ **Xem sau**: Đánh dấu bài viết để đọc sau.
- 📜 **Xem chi tiết bài viết**: Xem chi tiết và mở liên kết gốc của bài viết.
- 📤 **Chia sẻ tin tức**: Chia sẻ bài viết với bạn bè một cách dễ dàng.
- 🌈 **Cải tiến giao diện**: Hiệu ứng shimmer và Bottom Sheet giúp trải nghiệm tốt hơn.
- 🗂️ **Bộ lọc**: Lọc tin theo ngôn ngữ và quốc gia mong muốn.
- 🔔 **Thông báo tùy chỉnh**: Hẹn giờ nhận thông báo tin tức mỗi ngày (1, 2 hoặc 4 lần/ngày) với Alarm Manager.

## 🚀 Cài đặt

1. **Clone dự án**:
    ```bash
    git clone https://github.com/VieQ73/10_FastNews
    ```

2. **Mở dự án trong Android Studio**:
    - Mở Android Studio
    - Chọn `File -> Open`
    - Mở thư mục chứa dự án vừa clone

3. **Cấu hình API Key**:
    - Đăng ký tài khoản tại [gnews.io](https://gnews.io) để lấy API Key.
    - Thay thế dòng `String API_KEY = "YOUR_API_KEY";` trong các file `HomeFragment` và `SearchFragment`.

4. **Build và chạy ứng dụng**:
    - Kết nối thiết bị Android hoặc bật trình giả lập (emulator).
    - Nhấn nút `Run` trong Android Studio để chạy.

## 🛠️ Hướng dẫn sử dụng

1. **Xem tin theo chuyên mục**:
    - Chọn chuyên mục ở thanh điều hướng (ví dụ: Giải trí, Thể thao,...).
    - Duyệt danh sách bài viết trong mục đã chọn.

2. **Tìm kiếm bài viết**:
    - Nhập từ khóa vào thanh tìm kiếm để lấy bài viết liên quan.

3. **Lưu xem sau**:
    - Nhấn vào biểu tượng lưu để đưa bài viết vào danh sách "Xem sau".

4. **Xem chi tiết**:
    - Nhấn vào bài viết để xem chi tiết, hoặc mở liên kết đến nguồn gốc.

5. **Chia sẻ bài viết**:
    - Nhấn chia sẻ để gửi nội dung cho bạn bè từ ứng dụng.

## 🧩 Tổng quan mã nguồn

### Tích hợp API

- **gnews.io API**: Lấy tin tức theo chuyên mục và từ khóa.
- **Retrofit**: Thư viện HTTP an toàn và đơn giản dùng để gọi API.

## 📱 Ảnh minh họa

---
