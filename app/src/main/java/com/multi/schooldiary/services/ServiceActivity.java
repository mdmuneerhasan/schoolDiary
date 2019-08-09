package com.multi.schooldiary.services;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.multi.schooldiary.R;
import com.multi.schooldiary.activity.MyToolBar;
import com.multi.schooldiary.activity.SetDefaultActivity;
import com.multi.schooldiary.options.ConsoleActivity;
import com.multi.schooldiary.options.FaqsActivity;
import com.multi.schooldiary.options.SettingActivity;
import com.multi.schooldiary.services.shop.MyShop;
import com.multi.schooldiary.services.shop.ShopConsoleActivity;
import com.multi.schooldiary.utility.Connection;
import com.multi.schooldiary.utility.SavedData;
import java.util.ArrayList;

public class ServiceActivity extends AppCompatActivity {
    SavedData savedData;
    private Connection connection;
    ArrayList<Fragment> fragmentArrayList;
    FragmentManager fragmentManager;
    ViewPager viewPager;
    private PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        MyToolBar myToolBar=new MyToolBar((Toolbar) findViewById(R.id.toolbar),getApplicationContext()) {
            @Override
            public void onAlertActionPerformed() {

            }
        };
        setSupportActionBar(myToolBar.getToolBar());
        savedData =new SavedData(this);
        connection=new Connection();
        fragmentArrayList=new ArrayList<>();
        fragmentManager=getSupportFragmentManager();
        viewPager=findViewById(R.id.vpPager);
        viewPager.setOffscreenPageLimit(3);
        fragmentArrayList.add(new MyShop());

        pagerAdapter=new PagerAdapter(fragmentManager,fragmentArrayList);
        viewPager.setAdapter(pagerAdapter);

        try{
            viewPager.setCurrentItem(Integer.parseInt(getIntent().getStringExtra("position")));
        }catch (Exception e){

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.shop, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id ) {
            case R.id.shop_console:
                startActivity(new Intent(getBaseContext(), ShopConsoleActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
