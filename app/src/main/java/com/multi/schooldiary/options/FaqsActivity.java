package com.multi.schooldiary.options;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.multi.schooldiary.R;
import com.multi.schooldiary.activity.MyToolBar;
import com.multi.schooldiary.utility.Connection;
import com.multi.schooldiary.utility.SavedData;

import java.util.ArrayList;

public class FaqsActivity extends AppCompatActivity {
    SavedData savedData;
    Connection connection;
    private ArrayList<Faqs> faqsList;
    RecyclerView recyclerView;
    FaqsAdapter faqsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faqs);
        recyclerView=findViewById(R.id.recycle);
        connection=new Connection();
        savedData =new SavedData(this);
        faqsList=new ArrayList<>();
        setSupportActionBar(new MyToolBar(this, (Toolbar) findViewById(R.id.toolbar)) {
            @Override
            public void onAlertActionPerformed() {
            }
        }.getToolBar());
        faqsAdapter= new FaqsAdapter(faqsList, savedData.getValue("uid")) {
            @Override
            public void onStartAgain() {
                onStart();
            }
        };
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(faqsAdapter);

    }

    public void alert(View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Ask Question");
        final EditText input = new EditText(this);
        alertDialog.setView(input);
        alertDialog.setPositiveButton("ask",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Faqs faqs=new Faqs();
                        faqs.setAsker(savedData.getValue("name"));
                        faqs.setUid(savedData.getValue("uid"));
                        faqs.setQuestion(input.getText().toString());
                        faqs.setAnswer("we will response you back soon");
                        connection.getFaqs().push().setValue(faqs);
                        onStart();

                    }
                });
        alertDialog.setNegativeButton("delete",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        onStart();
                    }
                });
        alertDialog.show();
}

    @Override
    protected void onStart() {
        super.onStart();
        savedData.showAlert("connecting...");
        connection.getFaqs().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                faqsList.clear();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    Faqs faqs= dataSnapshot1.getValue(Faqs.class);
                    faqs.setKey(dataSnapshot1.getKey());
                    faqsList.add(0,faqs);

                }
                savedData.removeAlert();
                faqsAdapter.notifyDataSetChanged();
                connection.getFaqs().removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
