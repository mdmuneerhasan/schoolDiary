package com.multi.schooldiary.utility;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

public class Storage {
    SharedPreferences  sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;
    ProgressDialog progressDialog;

    public Storage(Context context) {
        this.context = context;
        sharedPreferences=context.getSharedPreferences("store",Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        progressDialog=new ProgressDialog(context);

    }

    public String getValue(String key) {
        return sharedPreferences.getString(key,null);
    }

    public void setValue(String key,String value) {
        editor.putString(key,value);
        editor.commit();
    }


    public void removeAlert() {
        progressDialog.dismiss();
    }

    public void showAlert(String message) {
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    public void test(String message) {
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }
    public void toast(String message) {
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }
}
