package com.multi.schooldiary.activity;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.multi.schooldiary.utility.SavedData;

public abstract class MyToolBar {
    Toolbar toolbar;
    Context context;
    SavedData savedData;

    @SuppressLint("ResourceAsColor")
    public MyToolBar(Toolbar viewById, Context context) {
        this.context=context;
        this.toolbar=viewById;
        savedData =new SavedData(context);
        onSet();
        attachListener();
        toolbar.setBackgroundColor(Color.parseColor("#233ED8"));
    }

    @SuppressLint("ResourceAsColor")
    public MyToolBar(Context context, Toolbar viewById) {
        this.context=context;
        this.toolbar=viewById;
        this.toolbar.setBackgroundColor(Color.parseColor("#2292d7"));
        this.toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        savedData =new SavedData(context);
        onSet();
        attachListener();

    }

    private void attachListener() {
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUp();
            }
        });
    }

    private void onSet() {
        if(savedData.getValue("stClass")!=null){
            toolbar.setTitle(savedData.getValue("stClass"));
        }else if(savedData.getValue("schoolName")!=null){
            toolbar.setTitle(savedData.getValue("schoolName"));
        }
    }

    public Toolbar getToolBar() {
        return toolbar;
    }
    private void popUp() {
        if (!savedData.getType().equals("school")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setItems(new CharSequence[]
                            {"1st year", "2nd year", "3rd year", "4th year", "5th year", "6th year", "7th year", "other"},
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            String[] classList = new String[]{"1st year", "2nd year", "3rd year", "4th year", "5th year", "6th year", "7th year", "other"};
                            savedData.setValue("stClass", classList[which]);
                            onSet();
                            onAlertActionPerformed();
                        }
                    }).setTitle("Select class");
            builder.create().show();
        } else if (savedData.getValue("position") != null && Integer.parseInt(savedData.getValue("position")) >= 6) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setItems(new CharSequence[]
                            {"play group", "nursery", "KG", "LKG", "UKG", "class 1", "class 2", "class 3", "class 4", "class 5", "class 6", "class 7", "class 8", "class 9", "class 10", "class 11", "class 12"},
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            String[] classList = new String[]{"play group", "nursery", "KG", "LKG", "UKG", "class 1", "class 2", "class 3", "class 4", "class 5", "class 6", "class 7", "class 8", "class 9", "class 10", "class 11", "class 12"};
                            savedData.setValue("stClass", classList[which]);
                            onSet();
                            onAlertActionPerformed();
                        }
                    }).setTitle("Select class");
            builder.create().show();
        }
    }

    public abstract void onAlertActionPerformed();
}
