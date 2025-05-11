package com.example.news.ui.search;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fastnews.R;
import com.example.fastnews.adapter.NewsAdapter;
import com.example.fastnews.api.NewsApi;
import com.example.fastnews.model.NewsModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.ArrayList;
import java.util.List;

public class SearchFragment {

    private EditText searchEditText;
    private ImageButton filterButton;
    private Spinner languageSpinner, countrySpinner;
    private RecyclerView searchRecyclerView;
    private NewsAdapter newsAdapter;
    private ImageView noResultsImage;
    private TextView noResultsText;

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
        searchRecyclerView.setAdapter(newsAdapter);
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        filterButton.setOnClickListener(v -> applyFilters());

        return view;
    }

    private void applyFilters() {
        String query = searchEditText.getText().toString().trim();
        String language = languageSpinner.getSelectedItem().toString();
        String country = countrySpinner.getSelectedItem().toString();

        NewsApi.getInstance().getNewsService().searchNews(query, language, country)
                .enqueue(new Callback<List<NewsModel>>() {
                    @Override
                    public void onResponse(Call<List<NewsModel>> call, Response<List<NewsModel>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<NewsModel> newsList = response.body();
                            newsAdapter.updateData(newsList);
                            searchRecyclerView.setVisibility(newsList.isEmpty() ? View.GONE : View.VISIBLE);
                            noResultsImage.setVisibility(newsList.isEmpty() ? View.VISIBLE : View.GONE);
                            noResultsText.setVisibility(newsList.isEmpty() ? View.VISIBLE : View.GONE);
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