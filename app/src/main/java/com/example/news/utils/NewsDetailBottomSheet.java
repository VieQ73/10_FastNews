package com.example.news.utils;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.news.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class NewsDetailBottomSheet extends BottomSheetDialogFragment {


    public NewsDetailBottomSheet() {
        // Required empty public constructor
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.bottom_sheet_layout, container, false);
    }
}