package com.example.easyway.controller;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import java.util.List;

public class AutoCompleteAdapter extends ArrayAdapter<String> {
    public AutoCompleteAdapter(@NonNull Context context, @NonNull List<String> objects) {
        super(context, android.R.layout.simple_dropdown_item_1line, objects);
    }
}