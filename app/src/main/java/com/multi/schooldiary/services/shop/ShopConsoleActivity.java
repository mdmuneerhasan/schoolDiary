package com.multi.schooldiary.services.shop;

import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.multi.schooldiary.R;
import com.multi.schooldiary.activity.MyToolBar;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class ShopConsoleActivity extends AppCompatActivity {
    Boolean isPoped=false;
    LinearLayout ll1;
    ImageButton btnHome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_console);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ll1= findViewById(R.id.ll1);
        btnHome= findViewById(R.id.btnHome);
        final ImageView imageView=findViewById(R.id.imageView);
        Picasso.get().load("https://previews.123rf.com/images/osaba/osaba1803/osaba180300060/98146878-table-top-view-aerial-image-stationary-on-office-desk-background-concept-flat-lay-objects-the-cup-of.jpg").centerCrop().resize(512,256).into(imageView, new Callback() {
            @Override
            public void onSuccess() {
                ll1.animate().translationY(0).setDuration(500);
            }
            @Override
            public void onError(Exception e) {

            }
        });
    }

    public void popUp(View view) {
        if(isPoped){
            isPoped=false;
            btnHome.animate().translationX(0).setDuration(500);
            ll1.animate().translationX(-512).setDuration(500);
        }else {
            isPoped=true;
            btnHome.animate().translationX(500).setDuration(500);
            ll1.animate().translationX(0).setDuration(500);
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
}
