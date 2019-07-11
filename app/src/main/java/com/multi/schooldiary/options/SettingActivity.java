package com.multi.schooldiary.options;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.multi.schooldiary.R;
import com.multi.schooldiary.activity.MainActivity;
import com.multi.schooldiary.activity.MyToolBar;
import com.multi.schooldiary.utility.SavedData;

public class SettingActivity extends AppCompatActivity {
    Switch aSwitch;
    private SavedData storege;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        storege=new SavedData(this);
        aSwitch =findViewById(R.id.swHelp);
        aSwitch.setChecked(storege.needHelp());
        MyToolBar myToolBar= new MyToolBar((Toolbar) findViewById(R.id.toolbar), this) {
            @Override
            public void onAlertActionPerformed() {
            }
        };
        setSupportActionBar(myToolBar.getToolBar());

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){
                    storege.toast("help mode is off");
                    storege.setValue("help","no");
                }else{
                    storege.toast("help mode is on");
                    storege.setValue("help","yes");
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }
}
