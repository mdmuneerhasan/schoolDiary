package com.multi.schooldiary.school;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.multi.schooldiary.utility.Connection;
import com.multi.schooldiary.utility.SavedData;
import com.multi.schooldiary.utility.Storage;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static com.multi.schooldiary.R.*;

public class MainPageActivity extends AppCompatActivity {
    private static final int PICK_REQUEST = 1234;
    ImageView btnPicker;
    Button btnPost;
    EditText edtDescription,edtTitle;
    TextInputLayout cvTitle,cvDescription;
    Boolean isPoped=false;
    CardView cardView;
    Connection connection;
    SavedData savedData;
    Storage storage;
    Uri parentUri;
    StorageTask uploadTask;
    private ArrayList<Upload> arrayList;
    UploadAdapter uploadAdapter;
    RecyclerView recyclerView;
    ValueEventListener valueEventListener;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_main_page);
        btnPost=findViewById(id.btnPost);
        btnPicker=findViewById(id.photoPicker);
        edtDescription=findViewById(id.edtDescription);
        edtTitle=findViewById(id.edtTitle);
        cvDescription=findViewById(id.cvDescription);
        cvTitle=findViewById(id.cvTitle);
        cardView=findViewById(id.card);
        connection=new Connection();
        savedData =new SavedData(this);
        storage=new Storage();
        arrayList=new ArrayList<>();
        uploadAdapter=new UploadAdapter(arrayList,savedData.getValue("uid")){
            @Override
            public void delete(final String uploadKey, final String uri, final int adapterPosition) {
                connection.getDbSchool().child(savedData.getValue("schoolId")).child("events")
                        .child(uploadKey).removeValue();
                uploadAdapter.notifyItemRemoved(adapterPosition);
                arrayList.remove(adapterPosition);
                if(uri!=null){
                    storage.getInsatance().getReferenceFromUrl(uri).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            savedData.test("deleted");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            savedData.test("failed");
                        }
                    });

                }
            }

            @Override
            public void like(String key, TextView tvLikeCount, String likeCount) {
                connection.getDbSchool().child(savedData.getValue("schoolId")).child("events")
                        .child(key).child("likes").child(savedData.getValue("uid"))
                        .setValue(savedData.getValue("name"));
                if(likeCount==null){
                    tvLikeCount.setText(1 +" Likes");
                }else {
                    tvLikeCount.setText(Integer.parseInt(likeCount)+1 +" Likes");
                }
            }

            @Override
            public void comment(String key) {

            }
        };
        recyclerView=findViewById(id.recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(uploadAdapter);

        valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    Upload upload=dataSnapshot1.getValue(Upload.class);
                    upload.setLikeCount(String.valueOf(dataSnapshot1.child("likes").getChildrenCount()));
                    arrayList.add(0,upload);
                }
                uploadAdapter.notifyDataSetChanged();
                connection.getDbSchool().child(savedData.getValue("schoolId")).child("events")
                        .removeEventListener(valueEventListener);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
    }

    public void popUp(View view) {
        if(isPoped){
            isPoped=false;
            cardView.animate().translationY(700).translationX(700).setDuration(500).setStartDelay(500);
            cvTitle.animate().translationY(-128).setDuration(500).setStartDelay(0);
            cvDescription.animate().translationX(500).setDuration(500).setStartDelay(0);
            btnPicker.animate().translationX(-256).setDuration(500).setStartDelay(0);
            btnPost.animate().translationY(256).setDuration(500).setStartDelay(0);
        }else {
            isPoped=true;
            cardView.animate().translationY(0).translationX(0).setDuration(500).setStartDelay(0);
            cvTitle.animate().translationY(0).setDuration(500).setStartDelay(500);
            cvDescription.animate().translationX(0).setDuration(500).setStartDelay(500);
            btnPicker.animate().translationX(0).setDuration(500).setStartDelay(500);
            btnPost.animate().translationY(0).setDuration(500).setStartDelay(500);
        }
    }

    @Override
    public void onBackPressed() {
        if(isPoped){
            popUp(new View(this));
        }else {
            super.onBackPressed();
        }
    }

    public void post(View view) {
        if (!savedData.hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE,this,new Callback(){
            @Override
            public void onSuccess() {
                post(new View(getBaseContext()));
            }

            @Override
            public void onError(Exception e) {
            }
        })) {
            return;
        }
        if(uploadTask!=null && uploadTask.isInProgress()){
            savedData.toast("uploading your file!!");
            popUp(new View(this));
            return;
        }
        if (parentUri !=null){
            savedData.toast("Uploading your post");
            popUp(new View(this));
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
                            Upload upload=new Upload(edtTitle.getText().toString(),edtDescription.getText().toString(),
                                    url,new SimpleDateFormat("hh:mm a dd-MMM-yy")
                                    .format(new Date(System.currentTimeMillis())),savedData.getValue("name"),
                                    savedData.getValue("schoolName"),savedData.getValue("uid"));
                            String key=connection.getDbSchool().child(savedData.getValue("schoolId")).child("events")
                                    .push().getKey();
                            upload.setKey(key);
                            connection.getDbSchool().child(savedData.getValue("schoolId")).child("events")
                                    .child(key).setValue(upload);
                            arrayList.add(0,upload);
                            uploadAdapter.notifyItemInserted(0);
                            recyclerView.scrollToPosition(0);
                            edtTitle.setText("");
                            edtDescription.setText("");
                            Picasso.get().load(drawable.ic_add_a_photo_black_24dp).into(btnPicker);
                            parentUri=null;
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
            if(edtDescription.getText().toString().trim().length()<1){
                edtDescription.setError("Please enter description");
                return;
            }
            popUp(new View(this));
            Upload upload=new Upload(edtTitle.getText().toString(),edtDescription.getText().toString(),
                    null,new SimpleDateFormat("hh:mm a dd-MMM-yy")
                    .format(new Date(System.currentTimeMillis())),savedData.getValue("name"),
                    savedData.getValue("schoolName"),savedData.getValue("uid"));
            String key=connection.getDbSchool().child(savedData.getValue("schoolId")).child("events")
                    .push().getKey();
            upload.setKey(key);
            connection.getDbSchool().child(savedData.getValue("schoolId")).child("events")
                    .child(key).setValue(upload);
            arrayList.add(0,upload);
            uploadAdapter.notifyItemInserted(0);
            recyclerView.scrollToPosition(0);
            edtTitle.setText("");
            edtDescription.setText("");

        }
    }

    private String getExtension(Uri uri) {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(getContentResolver().getType(uri));
    }

    public void pick(View view) {
        openChooser();
    }

    private void openChooser() {
        Intent intent=new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,PICK_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_REQUEST && resultCode==RESULT_OK&&data!=null && data.getData()!=null){
            parentUri =data.getData();
            btnPicker.setPadding(0,0,0,0);
            Picasso.get().load(data.getData()).into(btnPicker);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        connection.getDbSchool().child(savedData.getValue("schoolId")).child("events").addListenerForSingleValueEvent(valueEventListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        }
}
