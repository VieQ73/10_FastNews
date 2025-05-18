package com.example.news.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.news.R;
import com.example.news.adapters.NewsAdapter;
import com.example.news.NewsApi;
import com.example.news.NewsModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private EditText searchEditText;
    private ImageButton filterButton;
    private Spinner languageSpinner, countrySpinner;
    private RecyclerView searchRecyclerView;
    private ImageView noResultsImage;
    private TextView noResultsText;
    private NewsAdapter newsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        searchEditText = view.findViewById(R.id.searchEditText);
        filterButton = view.findViewById(R.id.filterButton);
        languageSpinner = view.findViewById(R.id.languageSpinner);
        countrySpinner = view.findViewById(R.id.countrySpinner);
        searchRecyclerView = view.findViewById(R.id.searchRecyclerView);
        noResultsImage = view.findViewById(R.id.noResultsImage);
        noResultsText = view.findViewById(R.id.noResultsText);

        newsAdapter = new NewsAdapter(new ArrayList<>());
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        searchRecyclerView.setAdapter(newsAdapter);

        filterButton.setOnClickListener(v -> applyFilters());

        return view;
    }

    private void applyFilters() {
        String query = searchEditText.getText().toString().trim();
        String language = languageSpinner.getSelectedItem().toString();
        String country = countrySpinner.getSelectedItem().toString();
        String apiKey = "YOUR_API_KEY_HERE"; // Thay bằng API key thực tế từ gnews.io

        if (query.isEmpty()) {
            searchRecyclerView.setVisibility(View.GONE);
            noResultsImage.setVisibility(View.VISIBLE);
            noResultsText.setVisibility(View.VISIBLE);
            return;
        }

        NewsApi.getInstance().getNewsService().searchNews(query, language, country, apiKey)
                .enqueue(new Callback<List<NewsModel>>() {
                    @Override
                    public void onResponse(Call<List<NewsModel>> call, Response<List<NewsModel>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<NewsModel> newsList = response.body();
                            if (newsList != null && !newsList.isEmpty()) {
                                // Lấy danh sách bài viết từ các đối tượng NewsModel
                                List<NewsModel.Article> articles = new ArrayList<>();
                                for (NewsModel model : newsList) {
                                    articles.addAll(model.getArticles());
                                }
                                newsAdapter.updateData(articles);
                                searchRecyclerView.setVisibility(View.VISIBLE);
                                noResultsImage.setVisibility(View.GONE);
                                noResultsText.setVisibility(View.GONE);
                            } else {
                                searchRecyclerView.setVisibility(View.GONE);
                                noResultsImage.setVisibility(View.VISIBLE);
                                noResultsText.setVisibility(View.VISIBLE);
                            }
                        } else {
                            searchRecyclerView.setVisibility(View.GONE);
                            noResultsImage.setVisibility(View.VISIBLE);
                            noResultsText.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<NewsModel>> call, Throwable t) {
                        searchRecyclerView.setVisibility(View.GONE);
                        noResultsImage.setVisibility(View.VISIBLE);
                        noResultsText.setVisibility(View.VISIBLE);
                    }
                });
    }
}