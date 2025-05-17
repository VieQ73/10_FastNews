package com.example.news.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.news.NewsModel;

import java.util.ArrayList;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "news_db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_SAVED_NEWS = "TinTucDB";

    // Column names
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "tieuDe";
    private static final String COLUMN_SOURCE = "nguonTin";
    private static final String COLUMN_PUBLISHED_AT = "thoiGianXuatBan";
    private static final String COLUMN_URL = "urlHinhAnh";
    private static final String COLUMN_IMAGE_URL = "image_url";
    private static final String COLUMN_CONTENT = "noiDung";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Tạo bảng
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_SAVED_NEWS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TITLE + " TEXT,"
                + COLUMN_SOURCE + " TEXT,"
                + COLUMN_PUBLISHED_AT + " TEXT,"
                + COLUMN_URL + " TEXT UNIQUE,"
                + COLUMN_IMAGE_URL + " TEXT,"
                + COLUMN_CONTENT + " TEXT"
                + ")";
        db.execSQL(CREATE_TABLE);
    }

    // Cập nhật bảng (nếu thay đổi version)
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SAVED_NEWS);
        onCreate(db);
    }

    // ✅ Thêm tin tức vào "Xem sau"
    public void saveNews(String imgUrl, String source, String title, String timeAgo, String urlToWeb, String content) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_SOURCE, source);
        values.put(COLUMN_PUBLISHED_AT, timeAgo);
        values.put(COLUMN_URL, urlToWeb);
        values.put(COLUMN_IMAGE_URL, imgUrl);
        values.put(COLUMN_CONTENT, content);

        db.insert(TABLE_SAVED_NEWS, null, values);
        db.close();
    }

    // ✅ Lấy danh sách tin tức đã lưu
    public List<NewsModel.Articles> getSavedNews() {
        List<NewsModel.Articles> newsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SAVED_NEWS, null, null, null, null, null, null);

        try{
            if (cursor.moveToFirst()) {
                do {
                    NewsModel.Articles article = new NewsModel.Articles();

                    NewsModel.Articles.Source source = new NewsModel.Articles.Source();
                    source.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SOURCE)));
                    article.setSource(source);

                    article.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)));
                    article.setPublishedAt(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PUBLISHED_AT)));
                    article.setUrl(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_URL)));
                    article.setUrlToImage(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_URL)));
                    article.setContent(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT)));

                    newsList.add(article);
                } while (cursor.moveToNext());
            }
        } finally {
            if(cursor != null)
                cursor.close();
            db.close();
        }

        return newsList;
    }

    // ✅ Xoá một tin tức theo URL
    public void deleteSavedNews(String url) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SAVED_NEWS, COLUMN_URL + " = ?", new String[]{url});
        db.close();
    }

    // ✅ Xoá toàn bộ tin tức đã lưu
    public void deleteAllSavedNews() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SAVED_NEWS, null, null);
        db.close();
    }

    // ✅ Kiểm tra tin tức đã tồn tại (theo URL)
//    public boolean isNewsSaved(String url) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.query(TABLE_NAME, null, COLUMN_URL + " = ?", new String[]{url}, null, null, null);
//        boolean exists = (cursor.getCount() > 0);
//        cursor.close();
//        db.close();
//        return exists;
//    }
}
