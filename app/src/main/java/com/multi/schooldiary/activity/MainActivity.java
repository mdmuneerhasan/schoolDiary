package com.multi.schooldiary.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.multi.schooldiary.fragments.DashBoardFragment;
import com.multi.schooldiary.fragments.JoinFragment;
import com.multi.schooldiary.fragments.LinkedAccountFragment;
import com.multi.schooldiary.fragments.MainFragment;
import com.multi.schooldiary.fragments.SetUpProfileFragment;
import com.multi.schooldiary.R;
import com.multi.schooldiary.utility.Storage;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Storage storage;
    FragmentTransaction ft;
    ImageView imageView;
    TextView tvName,tvEmail;
    int i=0;
    StringBuilder loggedIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        storage=new Storage(this);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        tvName=navigationView.getHeaderView(0).findViewById(R.id.tvName);
        tvEmail=navigationView.getHeaderView(0).findViewById(R.id.tvEmail);
        imageView=navigationView.getHeaderView(0).findViewById(R.id.imageView);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, new MainFragment());
        ft.commit();
        tvName.setText(storage.getValue("name"));
        tvEmail.setText("Id : "+storage.getValue("sid"));
        Picasso.get().load(storage.getValue("photoUrl")).into(imageView);


    }

    private void check(){
        if(storage.getValue("uid")==null){
            startActivity(new Intent(this,SignUpActivity.class));
        }else if(storage.getValue("schoolId")==null){
            storage.toast("please set default school");
            startActivity(new Intent(this,SetDefaultActivity.class));
        }
    }

    @Override
    public void onBackPressed() {
        tvName.setText(storage.getValue("name"));
        tvEmail.setText("Id : "+storage.getValue("sid"));
        Picasso.get().load(storage.getValue("photoUrl")).into(imageView);
        DrawerLayout drawer =findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(i<1){
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, new MainFragment());
            ft.commit();
        }else{
            super.onBackPressed();
        }
        i++;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.set_default) {
            startActivity(new Intent(this,SetDefaultActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        i=0;
        int id = item.getItemId();
        if (id == R.id.profile) {
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, new SetUpProfileFragment());
            ft.commit();
        } else if (id == R.id.join) {
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, new JoinFragment());
            ft.commit();
        } else if (id == R.id.link) {
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, new LinkedAccountFragment());
            ft.commit();
        } else if (id == R.id.dashBoard) {
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, new DashBoardFragment());
            ft.commit();
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, new MainFragment());
            ft.commit();
        } else if (id == R.id.home) {
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, new MainFragment());
            ft.commit();
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer =findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        check();
    }
}
