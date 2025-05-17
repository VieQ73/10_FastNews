package com.example.news;


import java.util.List;
import com.google.gson.annotations.SerializedName;

// NewsModel là một class dùng để ánh xạ (mapping) dữ liệu JSON trả về từ API tin tức.
// Nó chứa thông tin tổng quan về kết quả tìm kiếm tin tức và danh sách các bài viết.
public class NewsModel {

    // totalArticles: Tổng số lượng bài viết tìm thấy.
    private int totalArticles;

    // articles: Danh sách các bài viết (đối tượng Articles).
    private List<Articles> articles;

    // Phương thức getter để lấy tổng số lượng bài viết.
    public int getTotalArticles() {
        return totalArticles;
    }

    // Phương thức setter để thiết lập tổng số lượng bài viết.
    public void setTotalArticles(int totalArticles) {
        this.totalArticles = totalArticles;
    }

    // Phương thức getter để lấy danh sách các bài viết.
    public List<Articles> getArticles() {
        return articles;
    }

    // Phương thức setter để thiết lập danh sách các bài viết.
    public void setArticles(List<Articles> articles) {
        this.articles = articles;
    }

    // Articles là một inner class (class bên trong) dùng để biểu diễn thông tin của một bài viết tin tức.
    public static class Articles {

        // title: Tiêu đề của bài viết.
        private String title;

        // description: Mô tả ngắn gọn của bài viết.
        private String description;

        // content: Nội dung chi tiết của bài viết.
        private String content;

        // url: Đường dẫn (URL) đến bài viết gốc trên trang web.
        private String url;

        // urlToImage: Đường dẫn (URL) đến hình ảnh minh họa cho bài viết.
        // @SerializedName("image"): Sử dụng annotation này để chỉ định rằng trường "image" trong JSON
        // tương ứng với trường "urlToImage" trong class.
        @SerializedName("image")
        private String urlToImage;

        // publishedAt: Thời gian bài viết được xuất bản.
        private String publishedAt;

        // source: Nguồn của bài viết (đối tượng Source).
        private Source source;

        // Phương thức getter để lấy nguồn của bài viết.
        public Source getSource() {
            return source;
        }

<<<<<<< HEAD
=======
        // Phương thức setter để thiết lập nguồn của bài viết.
        public void setSource(Source source) {
            this.source = source;
        }
>>>>>>> 483bc3c5061cd5878fe0d84af5d33002a66d6423

        // Phương thức getter để lấy tiêu đề của bài viết.
        public String getTitle() {
            return title;
        }

        // Phương thức setter để thiết lập tiêu đề của bài viết.
        public void setTitle(String title) {
            this.title = title;
        }

        // Phương thức getter để lấy mô tả của bài viết.
        public String getDescription() {
            return description;
        }

        // Phương thức setter để thiết lập mô tả của bài viết.
        public void setDescription(String description) {
            this.description = description;
        }

        // Phương thức getter để lấy nội dung của bài viết.
        public String getContent() {
            return content;
        }

        // Phương thức setter để thiết lập nội dung của bài viết.
        public void setContent(String content) {
            this.content = content;
        }

        // Phương thức getter để lấy URL của bài viết.
        public String getUrl() {
            return url;
        }

        // Phương thức setter để thiết lập URL của bài viết.
        public void setUrl(String url) {
            this.url = url;
        }

        // Phương thức getter để lấy URL của hình ảnh minh họa.
        public String getUrlToImage() {
            return urlToImage;
        }

        // Phương thức setter để thiết lập URL của hình ảnh minh họa.
        public void setUrlToImage(String urlToImage) {
            this.urlToImage = urlToImage;
        }

        // Phương thức getter để lấy thời gian xuất bản của bài viết.
        public String getPublishedAt() {
            return publishedAt;
        }

        // Phương thức setter để thiết lập thời gian xuất bản của bài viết.
        public void setPublishedAt(String publishedAt) {
            this.publishedAt = publishedAt;
        }

        // Source là một inner class dùng để biểu diễn thông tin về nguồn của bài viết.
        public static class Source {

            // name: Tên của nguồn (ví dụ: "BBC News").
            private String name;

            // url: Đường dẫn (URL) đến trang web của nguồn.
            private String url;

            // Phương thức getter để lấy tên của nguồn.
            public String getName() {
                return name;
            }

            // Phương thức setter để thiết lập tên của nguồn.
            public void setName(String name) {
                this.name = name;
            }

            // Phương thức getter để lấy URL của nguồn.
            public String getUrl() {
                return url;
            }

            // Phương thức setter để thiết lập URL của nguồn.
            public void setUrl(String url) {
                this.url = url;
            }
        }
    }
}}