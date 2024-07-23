package com.college.managerpengeluaran;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class IncCategoryAdapter extends ArrayAdapter<modelinccategory> {
    private List<modelinccategory> catlist;

    public IncCategoryAdapter(@NonNull Context context, @NonNull List<modelinccategory> categoriesList) {
        super(context, 0, categoriesList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinnerkategori, parent, false);
        }

        modelinccategory category = getItem(position);
        TextView categoryName = convertView.findViewById(R.id.namakategoriselect);
        if (category != null) {
            categoryName.setText(category.getIncome_category_name());
        }

        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinnerkategori, parent, false);
        }

        modelinccategory category = getItem(position);
        TextView categoryName = convertView.findViewById(R.id.namakategoriselect);
        if (category != null) {
            categoryName.setText(category.getIncome_category_name());
        }

        return convertView;
    }
}
