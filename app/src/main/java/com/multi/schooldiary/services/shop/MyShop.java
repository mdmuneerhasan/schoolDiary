package com.multi.schooldiary.services.shop;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.multi.schooldiary.R;
import com.multi.schooldiary.services.ServiceItemClass;
import com.multi.schooldiary.utility.Connection;
import com.multi.schooldiary.utility.SavedData;

import java.util.ArrayList;

public class MyShop extends Fragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shop, container, false);
    }

    ImageView btnPicker;
    Button btnPost;
    EditText edtDescription, edtTitle;
    TextInputLayout cvTitle, cvDescription;
    Boolean isPoped = false;
    CardView cardView;
    RecyclerView recyclerView;
    ArrayList<ServiceItemClass> list;
    Button btnAdd;
    SavedData savedData;
    ShopItemAdapter editAdapter;
    Connection connection;
    FloatingActionButton floatingActionButton;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recycle);
        list = new ArrayList<>();
        btnPost=view.findViewById(R.id.btnPost);
        btnPicker=view.findViewById(R.id.photoPicker);
        edtDescription=view.findViewById(R.id.edtDescription);
        edtTitle=view.findViewById(R.id.edtTitle);
        cvDescription=view.findViewById(R.id.cvDescription);
        cvTitle=view.findViewById(R.id.cvTitle);
        cardView= view.findViewById(R.id.card);
        floatingActionButton= view.findViewById(R.id.btnFloating);


        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(gridLayoutManager);
        editAdapter = new ShopItemAdapter(list);
        recyclerView.setAdapter(editAdapter);
        btnAdd = view.findViewById(R.id.btnAdd);
        savedData = new SavedData(getContext());
        connection = new Connection();


        list.add(new ServiceItemClass("", "nob", "kj", "uhj", "njk", ""));
        list.add(new ServiceItemClass("", "nob", "kj", "uhj", "njk", ""));
        list.add(new ServiceItemClass("", "nob", "kj", "uhj", "njk", ""));
        list.add(new ServiceItemClass("", "nob", "kj", "uhj", "njk", ""));
        list.add(new ServiceItemClass("", "nob", "kj", "uhj", "njk", ""));
        list.add(new ServiceItemClass("", "nob", "kj", "uhj", "njk", ""));
        list.add(new ServiceItemClass("", "nob", "kj", "uhj", "njk", ""));
        list.add(new ServiceItemClass("", "nob", "kj", "uhj", "njk", ""));
        list.add(new ServiceItemClass("", "nob", "kj", "uhj", "njk", ""));
        list.add(new ServiceItemClass("", "nob", "kj", "uhj", "njk", ""));
        list.add(new ServiceItemClass("", "nob", "kj", "uhj", "njk", ""));
        editAdapter.notifyDataSetChanged();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUp(new View(getContext()));
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();


    }

    public void popUp(View view) {
        if (isPoped) {
            isPoped = false;
            cardView.animate().translationY(700).translationX(700).setDuration(500).setStartDelay(500);
            cvTitle.animate().translationY(-128).setDuration(500).setStartDelay(0);
            cvDescription.animate().translationX(500).setDuration(500).setStartDelay(0);
            btnPicker.animate().translationX(-256).setDuration(500).setStartDelay(0);
            btnPost.animate().translationY(256).setDuration(500).setStartDelay(0);
        } else {
            isPoped = true;
            cardView.animate().translationY(0).translationX(0).setDuration(500).setStartDelay(0);
            cvTitle.animate().translationY(0).setDuration(500).setStartDelay(500);
            cvDescription.animate().translationX(0).setDuration(500).setStartDelay(500);
            btnPicker.animate().translationX(0).setDuration(500).setStartDelay(500);
            btnPost.animate().translationY(0).setDuration(500).setStartDelay(500);
        }

    }

}