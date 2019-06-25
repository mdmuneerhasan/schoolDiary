package com.multi.schooldiary.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.multi.schooldiary.R;
import com.multi.schooldiary.utility.Connection;
import com.multi.schooldiary.utility.Storage;
import com.multi.schooldiary.utility.User;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG ="SignUpActivity" ;
    private static final int RC_SIGN_IN = 1234;
    GoogleSignInOptions gso;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth mAuth;
    FirebaseUser user;
    User user1;
    Storage storage;
    String sid;
    Connection connection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        connection=new Connection();
        storage = new Storage(this);
        gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_id))
                .requestEmail().build();
        mGoogleSignInClient= GoogleSignIn.getClient(this,gso);
        mAuth=FirebaseAuth.getInstance();
//        signIn();

    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            storage.showAlert("connecting...");
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Log.w(TAG, "Google sign in failed", e);
                Toast.makeText(SignUpActivity.this,"Google sign in failed",Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI( FirebaseUser user3) {
        if(user3==null){
            return;
        }

        user=user3;
        user1=new User(user.getDisplayName(),"none","none","none",user.getUid(),user.getPhoneNumber(),String.valueOf(user.getPhotoUrl()));
        user1.setEmail(user.getEmail());
        // getting sid
        connection.getDbUser().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    sid= "s0"+String.valueOf(dataSnapshot.getChildrenCount());
                }catch (Exception e){
                    sid= "s00";
                }

                connection.getDbUser().child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user2=dataSnapshot.getValue(User.class);
                        if(user2==null){
                            user1.setSid(sid);
                            connection.getDbUser().child(user.getUid()).setValue(user1);
                            connection.getDbUser().child(sid).child("name").setValue(user1.getName());
                            connection.getDbUser().child(sid).child("uid").setValue(user1.getUid());
                            storage.setValue("sid",sid);
                            storage.setValue("uid",user.getUid());
                            storage.setValue("name",user.getDisplayName());
                            storage.setValue("email",user.getEmail());
                            storage.setValue("number",user.getPhoneNumber());
                            storage.setValue("photoUrl",String.valueOf(user.getPhotoUrl()));
                            startActivity(new Intent(getBaseContext(), MainActivity.class));
                        }else{
                            storage.setValue("sid",user2.getSid());
                            storage.setValue("uid",user2.getUid());
                            storage.setValue("name",user2.getName());
                            storage.setValue("email",user2.getEmail());
                            storage.setValue("number",user2.getNumber());
                            storage.setValue("photoUrl",String.valueOf(user2.getPhotoUrl()));
                            startActivity(new Intent(getBaseContext(), MainActivity.class));
                        }
                        finish();
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

    @Override
    protected void onStart() {
        super.onStart();
        signIn();
    }
}
