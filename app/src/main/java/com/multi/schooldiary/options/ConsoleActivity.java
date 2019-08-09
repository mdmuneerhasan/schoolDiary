package com.multi.schooldiary.options;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.multi.schooldiary.R;
import com.multi.schooldiary.school.Upload;
import com.multi.schooldiary.utility.Connection;
import com.multi.schooldiary.utility.SavedData;
import com.multi.schooldiary.utility.SetDefaultClass;
import com.multi.schooldiary.utility.Storage;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class ConsoleActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_SCHOOL = 123;
    private static final int REQUEST_COLLEGE = 124;
    private static final int REQUEST_COACHING = 125;
    LinearLayout btnSchool,btnCollege,btnIndividual;
    Storage storage;
    SavedData savedData;
    Connection connection;
    CardView cv1,cv2,cv3;
    ImageView imgSchool,imgCoaching,imgCollege;
    EditText edtSchoolName,edtCollegeName,edtDepartmentName,edtCoachingName;
    Button buttonSchool,buttonCollege,buttonCoaching;


    String id="";
    Date date;
    SimpleDateFormat simpleDateFormat;
    ValueEventListener valueEventListener1;
    String name;
    Uri parentUri;
    StorageTask uploadTask;
    String schoolName,collegeName,coachingName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_console);
        buttonCoaching=findViewById(R.id.btnCoaching);
        buttonCollege=findViewById(R.id.btnCollege);
        buttonSchool=findViewById(R.id.btnSchool);
        btnCollege=findViewById(R.id.tvCollege);
        btnSchool=findViewById(R.id.tvSchool);
        btnIndividual=findViewById(R.id.tvIndividual);
        cv1=findViewById(R.id.cv1);
        cv2=findViewById(R.id.cv2);
        cv3=findViewById(R.id.cv3);
        imgSchool=findViewById(R.id.imgSchool);
        imgCollege=findViewById(R.id.imgCollege);
        imgCoaching=findViewById(R.id.imgCoaching);
        edtSchoolName=findViewById(R.id.edtSchoolName);
        edtCollegeName=findViewById(R.id.edtCollegeName);
        edtCoachingName=findViewById(R.id.edtCoachingName);
        edtDepartmentName=findViewById(R.id.edtDepartment);
        btnIndividual.setOnClickListener(this);
        btnSchool.setOnClickListener(this);
        btnCollege.setOnClickListener(this);
        buttonCollege.setOnClickListener(this);
        buttonSchool.setOnClickListener(this);
        buttonCoaching.setOnClickListener(this);
        imgCollege.setOnClickListener(this);
        imgCoaching.setOnClickListener(this);
        imgSchool.setOnClickListener(this);
        savedData=new SavedData(this);
        connection=new Connection();
        storage=new Storage();
        simpleDateFormat = new SimpleDateFormat("yy");
        date = new Date(System.currentTimeMillis());

        set(0);


    }

    private void createAccount2(final int requestCode, final String url) {

        connection.getDbSchool().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){
                    id="c"+simpleDateFormat.format(date)+String.valueOf(dataSnapshot.getChildrenCount());
                }else{
                    id="c"+simpleDateFormat.format(date)+"0";
                }

                switch (requestCode){
                    case REQUEST_SCHOOL:
                        savedData.setValue("deptType","school");
                        savedData.setValue("stClass","class 11");
                        connection.getDbSchool().child(id).child("deptType").setValue("school");
                        name=schoolName;
                        break;
                    case REQUEST_COLLEGE:
                        name=edtDepartmentName.getText().toString()+" ( "+collegeName+" ) ";
                        savedData.setValue("deptType","college");
                        savedData.setValue("stClass","2nd year");
                        connection.getDbSchool().child(id).child("college").setValue(collegeName);
                        connection.getDbSchool().child(id).child("department").setValue(edtDepartmentName.getText().toString());
                        connection.getDbSchool().child(id).child("deptType").setValue("college");
                        break;
                    case REQUEST_COACHING:
                        name=coachingName;
                        savedData.setValue("deptType","coaching");
                        savedData.setValue("stClass","2nd year");
                        connection.getDbSchool().child(id).child("college").setValue(coachingName);
                        connection.getDbSchool().child(id).child("department").setValue("");
                        connection.getDbSchool().child(id).child("deptType").setValue("coaching");
                        break;
                }


                SetDefaultClass setDefaultClass=new SetDefaultClass(name,"7",null,id);
                setDefaultClass.setPermission(true);
                connection.getDbUser().child(savedData.getValue("uid")).child("permission")
                        .child(id).child("7").setValue(setDefaultClass);
                connection.getDbSchool().child(id).child("schoolPhoto").setValue(url);
                connection.getDbSchool().child(id).child("name").setValue(name);
                connection.getDbSchool().child(id).child("id").setValue(id);
                connection.getDbSchool().child(id).child("principal").setValue(savedData.getValue("uid"))
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                savedData.setValue("schoolId",id);
                                savedData.setValue("position","7");
                                savedData.setValue("schoolName",name);
                                savedData.removeAlert();
                                savedData.toast(savedData.getValue("schoolName"));
                                parentUri=null;
                                savedData.removeAlert();
                                connection.getDbSchool().removeEventListener(valueEventListener1);
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void createAccount(final int requestCode) {
        savedData.showAlert("creating account...");
        if (!savedData.hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE,this,new Callback(){
            @Override
            public void onSuccess() {
                createAccount(requestCode);
            }

            @Override
            public void onError(Exception e) {
            }
        })) {
            return;
        }
        if(uploadTask!=null && uploadTask.isInProgress()){
            savedData.toast("uploading your file!!");
            return;
        }
        if (parentUri !=null){
            uploadTask=storage.getSchoolStorage().child("store")
                    .child(savedData.getValue("uid")+System.currentTimeMillis()+"."+getExtension(parentUri)).putFile(parentUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> firebaseUri = taskSnapshot.getStorage().getDownloadUrl();
                            firebaseUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String url = uri.toString();
                                    savedData.toast("Upload Successful");

                                    createAccount2(requestCode,url);

//                                    Upload upload=new Upload(edtTitle.getText().toString(),edtDescription.getText().toString(),
//                                            url,new SimpleDateFormat("hh:mm a dd-MMM-yy")
//                                            .format(new Date(System.currentTimeMillis())),savedData.getValue("name"),
//                                            savedData.getValue("schoolName"),savedData.getValue("uid"));
//                                    String key=connection.getDbSchool().child(savedData.getValue("schoolId")).child("events")
//                                            .push().getKey();
//                                    upload.setKey(key);
//                                    connection.getDbSchool().child(savedData.getValue("schoolId")).child("events")
//                                            .child(key).setValue(upload);
//                                    arrayList.add(0,upload);
//                                    uploadAdapter.notifyItemInserted(0);
//                                    recyclerView.scrollToPosition(0);
//                                    edtTitle.setText("");
//                                    edtDescription.setText("");
//                                    Picasso.get().load(R.drawable.ic_add_a_photo_black_24dp).into(btnPicker);
//                                    parentUri=null;
                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            savedData.toast("Upload Failed");
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //       savedData.showAlert(String.valueOf(100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount()));
                        }
                    });
        }else {
            createAccount2(requestCode,null);
        }
    }


    private String getExtension(Uri uri) {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(getContentResolver().getType(uri));
    }

    private void set(int i) {
        btnCollege.setBackgroundColor(Color.parseColor("#ffffff"));
        btnSchool.setBackgroundColor(Color.parseColor("#ffffff"));
        btnIndividual.setBackgroundColor(Color.parseColor("#ffffff"));
        switch (i%3){
            case 0:
                cv1.animate().translationX(0).setDuration(500);
                cv2.animate().translationX(1024).setDuration(500);
                cv3.animate().translationX(1024).setDuration(500);
                btnSchool.setBackgroundColor(Color.parseColor("#cccccc"));
                break;
            case 1:
                cv1.animate().translationX(-1024).setDuration(500);
                cv2.animate().translationX(0).setDuration(500);
                cv3.animate().translationX(1024).setDuration(500);
                btnCollege.setBackgroundColor(Color.parseColor("#cccccc"));

                break;
            case 2:
                cv1.animate().translationX(-1024).setDuration(500);
                cv2.animate().translationX(-1024).setDuration(500);
                cv3.animate().translationX(0).setDuration(500);
                btnIndividual.setBackgroundColor(Color.parseColor("#cccccc"));

                break;
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent=new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        switch (v.getId()){
            case R.id.tvSchool:
                set(0);
                break;
            case R.id.tvCollege:
                set(1);
                break;
            case R.id.tvIndividual:
                set(2);
                break;
            case R.id.imgSchool:
                startActivityForResult(intent, REQUEST_SCHOOL);
                break;
            case R.id.imgCollege:
                startActivityForResult(intent, REQUEST_COLLEGE);
                break;
            case R.id.imgCoaching:
                startActivityForResult(intent, REQUEST_COACHING);
                break;
            case R.id.btnSchool:
                schoolName=edtSchoolName.getText().toString();
                if(schoolName.trim().length()<1){
                    edtSchoolName.setError("Please enter schoolName");
                    return;
                }
                createAccount(REQUEST_SCHOOL);
                break;
            case R.id.btnCollege:
                collegeName=edtCollegeName.getText().toString();
                if(collegeName.trim().length()<1){
                    edtCollegeName.setError("Please enter schoolName");
                    return;
                }
                createAccount(REQUEST_COLLEGE);
                break;
            case R.id.btnCoaching:
                coachingName=edtCoachingName.getText().toString();
                if(coachingName.trim().length()<1){
                    edtCoachingName.setError("Please enter schoolName");
                    return;
                }
                createAccount(REQUEST_COACHING);
                break;
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK&&data!=null && data.getData()!=null){
            parentUri =data.getData();
           switch (requestCode){
               case REQUEST_SCHOOL:
                   imgSchool.setPadding(0,0,0,0);
                   Picasso.get().load(data.getData()).into(imgSchool);
                   break;
               case REQUEST_COLLEGE:
                   imgCollege.setPadding(0,0,0,0);
                   Picasso.get().load(data.getData()).into(imgCollege);
                   break;
               case REQUEST_COACHING:
                   imgCoaching.setPadding(0,0,0,0);
                   Picasso.get().load(data.getData()).into(imgCoaching);
                   break;
           }
        }
    }


}
