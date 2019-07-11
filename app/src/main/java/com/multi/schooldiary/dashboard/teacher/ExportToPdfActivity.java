package com.multi.schooldiary.dashboard.teacher;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.multi.schooldiary.R;
import com.multi.schooldiary.activity.MyToolBar;
import com.multi.schooldiary.utility.BiHashMap;
import com.multi.schooldiary.utility.Connection;
import com.multi.schooldiary.utility.SavedData;
import com.multi.schooldiary.utility.SetDefaultClass;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ExportToPdfActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1234;
    ArrayList<String> arrayListDate;
    ArrayList<String> uidList;
    BiHashMap<String,String,String> biHashMap;
    Map <String,Integer> mapAttendanceCount;
    SavedData savedData;
    PdfPTable table;
    String name,date;
    Connection connection;
    int totalCount=0,increment=1;
    private String month;
    private Date date1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_to_pdf);
        uidList=new ArrayList<>();
        biHashMap=new BiHashMap<>();
        arrayListDate=new ArrayList<>();
        savedData =new SavedData(this);
        connection=new Connection();
        mapAttendanceCount=new HashMap<>();
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,1);
        gridLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        date1=new Date(System.currentTimeMillis());
        month=new SimpleDateFormat("yyyy-MM").format(date1);
        TextView tvDate=findViewById(R.id.tvDate);
        tvDate.setText("Attendance sheet of "+new SimpleDateFormat("MMM-yyyy").format(date1));
        checkStoragePermission();
        MyToolBar myToolBar= new MyToolBar((Toolbar) findViewById(R.id.toolbar), this) {
            @Override
            public void onAlertActionPerformed() {
            }
        };
        setSupportActionBar(myToolBar.getToolBar());

    }

    private void checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                savedData.toast("Please grant savedData permission to create and save pdf file");
                }
                return;
            }
        }
    }


    @Override
    protected void onStart() {
        savedData.showAlert("please wait...\ndon't press any key");
        super.onStart();
        totalCount=0;
        // fetching names
        connection.getDbSchool().child(savedData.getValue("schoolId")).child("student")
                .child(savedData.getValue("stClass")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayListDate.clear();
                mapAttendanceCount.clear();
                uidList.clear();
                biHashMap.clear();
                biHashMap.put("uid","date","name/date");
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    SetDefaultClass setDefaultClass=dataSnapshot1.getValue(SetDefaultClass.class);
                    name=dataSnapshot1.child("name").getValue(String.class);
                    uidList.add(setDefaultClass.getUid());
                    mapAttendanceCount.put(setDefaultClass.getUid(),0);
                    biHashMap.put(setDefaultClass.getUid(),"date",name+"("+setDefaultClass.getRollNo()+")");
                }
                connection.getDbSchool().child(savedData.getValue("schoolId")).child("student")
                        .child(savedData.getValue("stClass")).removeEventListener(this);
                // sorting can be performed here

               // go for daily record
                connection.getDbSchool().child(savedData.getValue("schoolId")).child("attendance")
                        .child(savedData.getValue("stClass")).child(month).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                            increment=1;
                            date=dataSnapshot1.getKey().substring(8,10);
                            biHashMap.put("uid",date,date);
                            arrayListDate.add(date);
                            for(DataSnapshot dataSnapshot2:dataSnapshot1.child(savedData.getType()).getChildren()){
                                Attendance attendance=dataSnapshot2.child("1").getValue(Attendance.class);
                                if (attendance != null) {
                                    biHashMap.put(attendance.getUid(), date, attendance.getAttendance());
                                }
                                Attendance attendance2 = dataSnapshot2.child("2").getValue(Attendance.class);
                                if (attendance2 != null) {
                                    if( biHashMap.get(attendance2.getUid(), date)==null){
                                        biHashMap.put(attendance2.getUid(), date, "-" + attendance2.getAttendance());
                                    }else{
                                        biHashMap.put(attendance2.getUid(), date, biHashMap.get(attendance2.getUid(), date) + attendance2.getAttendance());
                                    }
                                }
                                if(dataSnapshot2.child("2").hasChildren()){
                                    increment=2;
                                }
                            }
                            totalCount+=increment;
                            for(String id:uidList){
                                if(biHashMap.get(id,date)==null){
                                }else {
                                    if(biHashMap.get(id,date).equals("p")){
                                        mapAttendanceCount.put(id,mapAttendanceCount.get(id)+1);
                                    }else if(biHashMap.get(id,date).equals("pp")){
                                        mapAttendanceCount.put(id,mapAttendanceCount.get(id)+2);
                                    }else if(biHashMap.get(id,date).equals("ap")){
                                        mapAttendanceCount.put(id,mapAttendanceCount.get(id)+1);
                                    }else if(biHashMap.get(id,date).equals("pa")){
                                        mapAttendanceCount.put(id,mapAttendanceCount.get(id)+1);
                                    }
                                }


                            }
                        }
                        // printing attendance count
                        biHashMap.put("uid","32","total");
                        for(String id:uidList){
                            if(mapAttendanceCount.get(id)==null){
                            }else {
                                biHashMap.put(id,"32",mapAttendanceCount.get(id)+"/"+String.valueOf(totalCount));
                            }
                        }
                        for(String id:uidList){
                            if(mapAttendanceCount.get(id)==null){
                            }else {
                                biHashMap.put(id,"33",String.format("%.2f",(float)mapAttendanceCount.get(id)/(float)(totalCount)*100)+"%");
                            }
                        }
                        connection.getDbSchool().child(savedData.getValue("schoolId")).child("attendance")
                                .child(savedData.getValue("stClass")).child(month).removeEventListener(this);
                        savedData.removeAlert();
                        savedData.toast("now create your pdf");

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void creteA4Size(View view) {
        checkStoragePermission();
        savedData.showAlert("creating your pdf");
        createA4Pdf();
    }

    public void registerSize(View view) {
        checkStoragePermission();
        savedData.showAlert("creating your pdf");
        createRegisterPdf();
    }

    private void createRegisterPdf() {
        float[] tab=new float[37];
        tab[0]=25;
        for (int i = 1; i < 32; i++) {
            tab[i]=3;
        }
        for (int i = 32; i < 34; i++) {
            tab[i]=12;
        }
        for (int i = 34; i < 37; i++) {
            tab[i]=3;
        }

        table=new PdfPTable(tab);
        table.addCell("Names/date");

        for (int j = 1; j < 37; j++) {
            if(j<32){
                table.addCell(new DecimalFormat("00").format(j));
            }else if(j<33){
                table.addCell("total");
            }else if(j<34){
                table.addCell("percentage");
            }else {
                table.addCell("");
            }
        }
        for(String id:uidList){
            table.addCell(biHashMap.get(id,"date"));
            for (int j = 1; j < 37; j++) {
                table.addCell(biHashMap.get(id,String.valueOf(new DecimalFormat("00").format(j))));
            }
        }
        Document document = new Document();
        document.setPageSize(PageSize.A2);
        String file_path = Environment.getExternalStorageDirectory().getPath() + "/Attendance/"+new  SimpleDateFormat("MMM-yy").format(date1)+".pdf";
        try {
            PdfWriter.getInstance(document,new FileOutputStream(file_path));
        } catch (Exception e) {
            e.printStackTrace();
        }
        document.open();
        try {
            document.add(table);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        document.close();
        savedData.removeAlert();
        savedData.toast("Created");
        File file2 = new File(file_path);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file2), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }
    private void createA4Pdf() {
        // edit it
        float[] tab=new float[22];
        tab[0]=35;
        for (int i = 1; i <20 ; i++) {
            tab[i]=3;
        }
        for (int i = 20; i < 22; i++) {
            tab[i]=15;
        }

        table=new PdfPTable(tab);
        table.addCell("Names/date");
        // edit
        for (int j = 1; j < 22; j++) {
            if(j<20){
                if(j<arrayListDate.size()+1){
                    table.addCell(arrayListDate.get(j-1));
                }else{
                    table.addCell("");
                }
            }else if(j<21){
                table.addCell("total");
            }else if(j<22){
                table.addCell("  %  ");
            }else {
                table.addCell("");
            }
        }
        for(String id:uidList){
            table.addCell(biHashMap.get(id,"date"));
            for (int j = 1; j < 22; j++) {
                if(j<20){
                    if(j<arrayListDate.size()+1){
                        table.addCell(biHashMap.get(id,arrayListDate.get(j-1)));
                    }else{
                        table.addCell("");
                    }
                }else if(j<21){
                    table.addCell(biHashMap.get(id,"32"));
                }else if(j<22){
                    table.addCell(biHashMap.get(id,"33"));
                }else {
                    table.addCell("");
                }
            }
        }
        Document document = new Document();
        document.setPageSize(PageSize.A4);
        String file_path = Environment.getExternalStorageDirectory().getPath() + "/Attendance/"+new  SimpleDateFormat("MMM-yy").format(date1)+".pdf";
        try {
            PdfWriter.getInstance(document,new FileOutputStream(file_path));
        } catch (Exception e) {
            e.printStackTrace();
        }
        document.open();
        try {
            document.add(table);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        document.close();
        savedData.removeAlert();
        savedData.toast("Created");
        File file2 = new File(file_path);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file2), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }
}