package com.multi.schooldiary.activity;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.multi.schooldiary.options.ConsoleActivity;
import com.multi.schooldiary.options.FaqsActivity;
import com.multi.schooldiary.fragments.DashBoardFragment;
import com.multi.schooldiary.fragments.SetUpFragment;
import com.multi.schooldiary.fragments.MainFragment;
import com.multi.schooldiary.fragments.MyProfileFragment;
import com.multi.schooldiary.R;
import com.multi.schooldiary.notification.ExampleJobService;
import com.multi.schooldiary.notification.NotificationControlActivity;
import com.multi.schooldiary.options.SettingActivity;
import com.multi.schooldiary.timetable.TFri;
import com.multi.schooldiary.timetable.TMon;
import com.multi.schooldiary.timetable.TSat;
import com.multi.schooldiary.timetable.TSun;
import com.multi.schooldiary.timetable.TThu;
import com.multi.schooldiary.timetable.TTue;
import com.multi.schooldiary.timetable.TWed;
import com.multi.schooldiary.utility.SavedData;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    SavedData savedData;
    FragmentTransaction ft;
    CircleImageView imageView;
    ImageView bgapp, clover;
    LinearLayout textsplash, texthome, menus;
    Animation frombottom;
    Button exp, msg, so,st, ao;
    TextView tvName2,tvName,tvSchoolName;
    TextView tvEmail;
    int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        savedData =new SavedData(this);
        tvName2=findViewById(R.id.tvName);
        tvSchoolName=findViewById(R.id.tvSchoolName);
        if(savedData.haveValue("schoolName")){
            tvSchoolName.setText(savedData.getValue("schoolName"));
        }
        if(savedData.haveValue("name")){
            tvName2.setText(savedData.getValue("name"));
        }
            frombottom = AnimationUtils.loadAnimation(this, R.anim.frombottom);
            bgapp = findViewById(R.id.bgapp);
            clover = findViewById(R.id.clover);
            textsplash =findViewById(R.id.textsplash);
            texthome = findViewById(R.id.texthome);
            menus = findViewById(R.id.menus);
            bgapp.animate().translationY(-1400).setDuration(3000).setStartDelay(100);
            clover.animate().alpha(0).setDuration(3000).setStartDelay(1500);
            textsplash.animate().translationY(140).alpha(0).setDuration(3000).setStartDelay(1000);
            menus.animate().translationY(0).setDuration(2000).setStartDelay(1500);
            texthome.startAnimation(frombottom);

        MyToolBar myToolBar= new MyToolBar((Toolbar) findViewById(R.id.toolbar), this) {
            @Override
            public void onAlertActionPerformed() {
            }
        };
        setSupportActionBar(myToolBar.getToolBar());
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView =  findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        tvName=navigationView.getHeaderView(0).findViewById(R.id.tvName);
        tvEmail=navigationView.getHeaderView(0).findViewById(R.id.tvEmail);
        imageView=navigationView.getHeaderView(0).findViewById(R.id.imageView);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer,myToolBar.getToolBar(), R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                exp.animate().translationX(0).setDuration(500);
                ao.animate().translationX(0).setDuration(500);
                msg.animate().translationX(0).setDuration(500);
                st.animate().translationX(0).setDuration(500);
                so.animate().translationX(0).setDuration(500);

                imageView.animate().translationY(0).setDuration(500);
                tvName.animate().translationY(0).setDuration(500);
                tvEmail.animate().translationY(0).setDuration(500);

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                exp.animate().translationX(-256).setDuration(500);
                ao.animate().translationX(400).setDuration(500);
                msg.animate().translationX(-256).setDuration(500);
                st.animate().translationX(400).setDuration(500);
                so.animate().translationX(-256).setDuration(500);
                imageView.animate().translationY(-256).setDuration(500);
                tvName.animate().translationY(-256).setDuration(500);
                tvEmail.animate().translationY(-256).setDuration(500);

            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, new MainFragment());
        ft.commit();
        tvName.setText(savedData.getValue("name"));
        if(savedData.getValue("number")!=null){
            tvEmail.setText(savedData.getValue("number"));
        }

        Picasso.get().load(savedData.getValue("photoUrl")).into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext() , NotificationControlActivity.class));
            }
        });

        exp=navigationView.getHeaderView(0).findViewById(R.id.home);
        msg=navigationView.getHeaderView(0).findViewById(R.id.join);
        so=navigationView.getHeaderView(0).findViewById(R.id.so);
        st=navigationView.getHeaderView(0).findViewById(R.id.st);
        ao=navigationView.getHeaderView(0).findViewById(R.id.profile);
        exp.setOnClickListener(this);
        msg.setOnClickListener(this);
        ao.setOnClickListener(this);
        so.setOnClickListener(this);
        st.setOnClickListener(this);



        ComponentName componentName = new ComponentName(this, ExampleJobService.class);
        JobInfo info = new JobInfo.Builder(123, componentName)
                .setPersisted(true)
                .setPeriodic(1* 60 * 1000)
                .build();

        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        int resultCode = scheduler.schedule(info);
        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.d("Scheduler MainActivity", "Job scheduled");
        } else {
            Log.d("Scheduler MainActivity", "Job scheduling failed");
        }

    }


    private void check(){
        if(!savedData.haveValue("uid")){
            savedData.log("no id");
            startActivity(new Intent(this,SignUpActivity.class));
        }else if(savedData.getValue("progress")==null){
            savedData.toast("please set default school");
            startActivity(new Intent(this,SetDefaultActivity.class));
        }
        if(savedData.getValue("schoolId")!=null && savedData.getValue("stClass")!=null){
            loadTimeTable();
        }
    }

    private void loadTimeTable() {
        switch (Calendar.getInstance().get(Calendar.DAY_OF_WEEK)){
            case 1 :
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.container2, new TSun());
                ft.commit();
                break;
            case 2 :
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.container2, new TMon());
                ft.commit();
                break;
            case 3 :
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.container2, new TTue());
                ft.commit();
                break;
            case 4 :
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.container2, new TWed());
                ft.commit();
                break;
            case 5 :
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.container2, new TThu());
                ft.commit();
                break;
            case 6 :
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.container2, new TFri());
                ft.commit();
                break;
            case 7 :
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.container2, new TSat());
                ft.commit();
                break;


        }
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
            finish();
            startActivity(new Intent(this, SettingActivity.class));
            return true;
        } else if (id == R.id.set_default) {
            startActivity(new Intent(this,SetDefaultActivity.class));
            return true;
        } else if (id == R.id.faqs) {
            startActivity(new Intent(this, FaqsActivity.class));
            return true;
        } else if (id == R.id.console) {
        startActivity(new Intent(this, ConsoleActivity.class));
        return true;
    }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        i=0;
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Download and rate this amazing school app    https://play.google.com/store/apps/details?id=com.multi.schooldiary");
        sendIntent.setType("text/plain");
        int id = item.getItemId();
        switch (id){
            case R.id.nav_send:
                sendIntent.setPackage("com.whatsapp");
                startActivity(sendIntent);
                break;
            case R.id.nav_share:
                startActivity(sendIntent);
                break;

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

    @Override
    public void onBackPressed() {
        tvName.setText(savedData.getValue("name"));
        if(savedData.getValue("number")!=null){
            tvEmail.setText(savedData.getValue("number"));
        }
        Picasso.get().load(savedData.getValue("photoUrl")).into(imageView);
        DrawerLayout drawer =findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(i<1){
            bgapp.animate().translationY(-1400).setDuration(500).setStartDelay(0);
            menus.animate().translationY(0).setDuration(500).setStartDelay(100);
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, new MainFragment());
            ft.commit();
        }else{
            super.onBackPressed();
        }
        i++;
    }

    @Override
    public void onClick(View v) {
        bgapp.animate().translationY(-1400).setDuration(500).setStartDelay(0);
        menus.animate().translationY(0).setDuration(500).setStartDelay(100);
        i=0;
        int id = v.getId();
        if (id == R.id.profile) {
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, new MyProfileFragment());
            ft.commit();
        } else if (id == R.id.join) {

            bgapp.animate().translationY(-1900).setDuration(1000).setStartDelay(200);
            menus.animate().translationY(-500).setDuration(1000).setStartDelay(400);
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, new SetUpFragment());
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
        } else if (id == R.id.so) {
            FirebaseAuth.getInstance().signOut();
            savedData.clear();
            check();
        } else if (id == R.id.st) {
            finish();
            startActivity(new Intent(this,SettingActivity.class));
        }
        DrawerLayout drawer =findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }
}
