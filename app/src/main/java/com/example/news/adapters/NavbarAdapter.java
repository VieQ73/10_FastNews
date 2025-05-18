package com.example.news.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.news.R;
import com.example.news.fragments.HomeFragment;

import java.util.ArrayList;

public class NavbarAdapter extends RecyclerView.Adapter<NavbarAdapter.NavbarViewHolder> {
    ArrayList<String> navItems;
    Context context;
    int selectedPosition = 0;
    private OnCategoryClickListener categoryClickListener;

    // Constructor khởi tạo adapter với danh sách danh mục và context
    public NavbarAdapter(ArrayList<String> navItems, Context context) {
        this.navItems = navItems;
        this.context = context;
    }

    @NonNull
    @Override
    public NavbarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Nạp layout cho mỗi mục danh mục từ file nav_items_layout.xml
        View view = LayoutInflater.from(context).inflate(R.layout.nav_items_layout, parent, false);
        return new NavbarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NavbarViewHolder holder, int position) {
        String item = navItems.get(position);
        holder.navItem.setText(item);

        // Thiết lập trạng thái được chọn hoặc không chọn cho danh mục
        if (selectedPosition == position) {
            holder.navItem.setTextColor(context.getResources().getColor(R.color.themeColor2));
            holder.navUnderline.setVisibility(View.VISIBLE);
        } else {
            holder.navItem.setTextColor(context.getResources().getColor(R.color.navColor));
            holder.navUnderline.setVisibility(View.GONE);
        }

        // Xử lý sự kiện nhấn vào danh mục
        holder.itemView.setOnClickListener(v -> {
            int previousSelectedPosition = selectedPosition;
            selectedPosition = holder.getAdapterPosition();
            notifyItemChanged(previousSelectedPosition);
            notifyItemChanged(selectedPosition);

            if (categoryClickListener != null) {
                categoryClickListener.onCategoryClicked(holder.navItem.getText().toString().toLowerCase());
            }
        });
    }

    @Override
    public int getItemCount() {
        return navItems.size();
    }

    // ViewHolder cho mỗi mục danh mục
    public static class NavbarViewHolder extends RecyclerView.ViewHolder {
        TextView navItem;
        View navUnderline;

        public NavbarViewHolder(@NonNull View itemView) {
            super(itemView);
            navItem = itemView.findViewById(R.id.navItem);
            navUnderline = itemView.findViewById(R.id.navUnderline);
        }
    }

    // Interface để xử lý sự kiện nhấn danh mục
    public interface OnCategoryClickListener {
        void onCategoryClicked(String category);
    }

    // Thiết lập listener cho sự kiện nhấn danh mục
    public void setOnCategoryClickListener(OnCategoryClickListener listener) {
        this.categoryClickListener = listener;
    }
}