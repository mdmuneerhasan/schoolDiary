package com.multi.schooldiary.utility;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.multi.schooldiary.BuildConfig;

import java.text.SimpleDateFormat;

public class SavedData {
    SharedPreferences  sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;
    ProgressDialog progressDialog;

    public SavedData(Context context) {
        this.context = context;
        sharedPreferences=context.getSharedPreferences("store",Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        progressDialog=new ProgressDialog(context);

    }

    public String getValue(String key) {
        return sharedPreferences.getString(key,"Schooly");
    }

    public void setValue(String key,String value) {
        editor.putString(key,value);
        editor.commit();
    }


    public void removeAlert() {
        progressDialog.dismiss();
    }

    public void showAlert(String message) {
        try{
            progressDialog.setMessage(message);
            progressDialog.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public boolean needHelp() {
        if(getValue("help")!=null && getValue("help").equals("no")){
            return false;
        }
        return true;
    }

    public void toast(String message) {
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }

    public String getType() {
        if(getValue("deptType")==null){
            return "school";
        }else if(getValue("deptType").equals("school")){
            return "school";
        }else {
            return getValue("uid");
        }
    }

    public void test(String message) {
        if(BuildConfig.DEBUG){
            Toast.makeText(context,message,Toast.LENGTH_LONG).show();
        }
    }

    public void clear() {
        editor.clear();
        editor.commit();
    }

    public String getDefault() {
        return getValue("default");
    }

    public boolean haveValue(String key) {
        return !getValue(key).equals(getValue("default"));
    }

    public void log(String message) {
        Log.e(context.getClass().getSimpleName(),message);
    }


//    private void popUp() {
//        if (!storage.getType().equals("school")) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setItems(new CharSequence[]
//                            {"1st year", "2nd year", "3rd year", "4th year", "5th year", "6th year", "7th year", "other"},
//                    new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            String[] classList = new String[]{"1st year", "2nd year", "3rd year", "4th year", "5th year", "6th year", "7th year", "other"};
//                            storage.setValue("stClass", classList[which]);
//                            onStart();
//                        }
//                    }).setTitle("Select class");
//            builder.create().show();
//        } else if (storage.getValue("position") != null && Integer.parseInt(storage.getValue("position")) >= 6) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setItems(new CharSequence[]
//                            {"play group", "nursery", "KG", "LKG", "UKG", "class 1", "class 2", "class 3", "class 4", "class 5", "class 6", "class 7", "class 8", "class 9", "class 10", "class 11", "class 12"},
//                    new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            String[] classList = new String[]{"play group", "nursery", "KG", "LKG", "UKG", "class 1", "class 2", "class 3", "class 4", "class 5", "class 6", "class 7", "class 8", "class 9", "class 10", "class 11", "class 12"};
//                            storage.setValue("stClass", classList[which]);
//                            onStart();
//                        }
//                    }).setTitle("Select class");
//            builder.create().show();
//        }
//    }
}
