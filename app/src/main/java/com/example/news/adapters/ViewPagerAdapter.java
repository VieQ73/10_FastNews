package com.example.news.adapters;

import androidx.annotation.NonNull;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

// NavbarAdapter là một Adapter cho RecyclerView, được sử dụng để hiển thị danh sách các danh mục tin tức
// (Ví dụ: Thể thao, Giải trí, Công nghệ) trong một thanh điều hướng ngang (horizontal navigation bar).
public class NavbarAdapter extends RecyclerView.Adapter<NavbarAdapter.ViewHolder> {

    private ArrayList<String> navItems; // Danh sách các mục điều hướng (tên các danh mục tin tức)
    private Context context; // Context của ứng dụng
    private OnCategoryClickListener onCategoryClickListener; // Listener để xử lý khi một danh mục được click

    // Constructor của NavbarAdapter
    // @param navItems: Danh sách các mục điều hướng
    // @param context: Context của ứng dụng
    public NavbarAdapter(ArrayList<String> navItems, Context context) {
        this.navItems = navItems;
        this.context = context;
    }

    // Phương thức này được gọi khi RecyclerView cần một ViewHolder mới để đại diện cho một mục.
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout cho mỗi item (nav_items_layout.xml)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.nav_items_layout, parent, false);
        return new ViewHolder(view); // Tạo và trả về một ViewHolder mới
    }

    // Phương thức này được gọi để hiển thị dữ liệu tại một vị trí cụ thể trong RecyclerView.
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Lấy tên danh mục từ danh sách navItems tại vị trí position
        String category = navItems.get(position);
        // Đặt tên danh mục vào TextView (navTitle) trong ViewHolder
        holder.navTitle.setText(category);

        // Thiết lập OnClickListener cho mỗi item trong RecyclerView
        holder.itemView.setOnClickListener(v -> {
            // Khi một item được click, gọi phương thức onCategoryClick của listener
            if (onCategoryClickListener != null) {
                onCategoryClickListener.onCategoryClick(category);
            }
        });
    }

    // Phương thức này trả về số lượng mục trong danh sách navItems.
    @Override
    public int getItemCount() {
        return navItems.size(); // Trả về số lượng mục điều hướng
    }

    // ViewHolder là một lớp bên trong NavbarAdapter, dùng để lưu trữ các thành phần giao diện của một item trong RecyclerView.
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView navTitle; // TextView hiển thị tên của danh mục

        // Constructor của ViewHolder
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Tìm TextView (nav_title) trong layout item
            navTitle = itemView.findViewById(R.id.nav_title);
        }
    }

    // OnCategoryClickListener là một interface để thông báo khi một danh mục được click.
    public interface OnCategoryClickListener {
        // Phương thức này được gọi khi một danh mục được click.
        void onCategoryClick(String category);
    }

    // Phương thức này được sử dụng để gán listener cho sự kiện click danh mục.
    public void setOnCategoryClickListener(OnCategoryClickListener listener) {
        this.onCategoryClickListener = listener;
    }
}